package com.xuliang.cim.server.task;

import com.xuliang.cim.server.manager.SessionManager;
import com.xuliang.cim.server.mq.MQProvider;
import com.xuliang.cim.server.mq.model.P2PMessage;
import com.xuliang.cim.server.util.Constants;
import com.xuliang.cim.server.util.JsonV2Util;
import com.xuliang.xim.nettymsg.dto.RpcCmd;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.xuliang.cim.server.util.Constants.*;

/**
 * 推送失败消息，进行补偿
 */
@Component
@Slf4j
public class AckMessageTask {


    private ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("push-fail-message-schedule-pool").daemon(true).build());


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                ackMessage();
            } catch (Exception e) {
                log.warn("pushFailMessageTask", e);
            }
        }, 5, 1, TimeUnit.SECONDS);

    }

    /**
     * 重发所有的 失败消息
     */
    public void ackMessage() {

        Map<Object, Object> ackgROUPMessageMap = stringRedisTemplate.opsForHash().entries(String.format(Constants.SEND_MESSAGE_MAP, P2P));
        if (ackgROUPMessageMap == null) {
            return;
        }
        for (Map.Entry<Object, Object> objectObjectEntry : ackgROUPMessageMap.entrySet()) {
            P2PMessage groupMessage = JsonV2Util.readValue(objectObjectEntry.getValue().toString(), P2PMessage.class);

            if (groupMessage.getSendTime() == null) {
                continue;
            }

            /**校验ack时间*/
            if (System.currentTimeMillis() - groupMessage.getSendTime() >= CHECK_ACK_MESSAGE_TIME) {
                /**给客户端重发*/
                MQProvider.push(groupMessage.getUserId(), groupMessage);

            }
        }
    }
}
