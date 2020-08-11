package com.xuliang.cim.server.mq.consumer;


import com.xuliang.cim.server.connection.Connection;
import com.xuliang.cim.server.creator.MessageCreator;
import com.xuliang.cim.server.manager.SessionManager;
import com.xuliang.cim.server.mq.MQProvider;
import com.xuliang.cim.server.mq.model.P2PMessage;
import com.xuliang.cim.server.mq.queue.MessageQueue;
import com.xuliang.cim.server.util.Constants;
import com.xuliang.cim.server.util.JsonV2Util;
import com.xuliang.xim.common.req.P2PReqVO;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

import static com.xuliang.cim.server.util.Constants.P2P;
import static com.xuliang.xim.nettymsg.MessageConstants.STATE_OK;

/**
 * 处理消费发送P2P消息的mq
 */
@Slf4j
public class SendP2PMessageConsumer implements Runnable {


    private StringRedisTemplate stringRedisTemplate;
    private final Duration timeout = Duration.ofMillis(100);

    private final MessageQueue<P2PMessage> p2PMessageMessageQueue;


    public SendP2PMessageConsumer(int index, StringRedisTemplate fastStorage) {
        this.stringRedisTemplate = fastStorage;
        this.p2PMessageMessageQueue = MQProvider.getSendP2PMessageRPCMsgQueue(index);

    }

    @Override
    public void run() {
        while (true) {
            P2PMessage p2PMessage = null;
            try {
                if (p2PMessageMessageQueue.size() > 0) {
                    p2PMessage = p2PMessageMessageQueue.pop(timeout);

                    Channel channel = SessionManager.getChannel(p2PMessage.getUserId());
                    if (channel == null) {
                        //todo 存储到离线消息db中
//                        stringRedisTemplate.opsForZSet().add()

                        return;
                    }
                    Connection.sendMsg(channel, MessageCreator.okResponse(p2PMessage.getRpcCmd(), STATE_OK));
                    /**发送成功 删除失败集合记录*/
                    P2PReqVO p2PReqVO = p2PMessage.getRpcCmd().getMsg().loadBean(P2PReqVO.class);
                    stringRedisTemplate.opsForHash().delete(String.format(Constants.FAIL_MESSAGE_MAP, P2P), p2PReqVO.getReqNo());
                    /**添加到已发送消息集合中*/
                    p2PMessage.setSendTime(System.currentTimeMillis());
                    stringRedisTemplate.opsForHash().put(String.format(Constants.SEND_MESSAGE_MAP, P2P), p2PReqVO.getReqNo(), JsonV2Util.writeValue(p2PMessage));
                }
            } catch (Exception ex) {
                log.error("p2PMessageMessageQueue.pop", ex);
                try {
                    Thread.sleep(5);
                    /**如果发送消息失败，存储到redis 发送失败表中*/
                    P2PReqVO p2PReqVO = p2PMessage.getRpcCmd().getMsg().loadBean(P2PReqVO.class);
                    stringRedisTemplate.opsForHash().put(String.format(Constants.FAIL_MESSAGE_MAP, P2P), p2PReqVO.getReqNo(), JsonV2Util.writeValue(p2PMessage.getRpcCmd()));
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
