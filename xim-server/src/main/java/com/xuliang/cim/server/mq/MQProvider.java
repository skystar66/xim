package com.xuliang.cim.server.mq;

import com.xuliang.cim.server.mq.model.P2PMessage;
import com.xuliang.cim.server.mq.queue.DefaultMQ;
import com.xuliang.cim.server.mq.queue.MessageQueue;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MQProvider {


    /**
     * 发送消息的mq p2p
     */
    private static final Map<Integer, MessageQueue<P2PMessage>> sendP2PMessageRPCMsgQueueMap = new HashMap<>();


    public static final int threadCnt = Runtime.getRuntime().availableProcessors() * 2;


    static {
        for (int i = 0; i < threadCnt; i++) {
            /**cpu*2个队列对应cpu*2个消费线程*/
            sendP2PMessageRPCMsgQueueMap.put(i, new DefaultMQ<>());
        }
    }


    /**
     * 得到与index相匹配的队列
     *
     * @param index
     * @return
     */
    public static MessageQueue<P2PMessage> getSendP2PMessageRPCMsgQueue(int index) {
        return sendP2PMessageRPCMsgQueueMap.get(index);
    }

    /**
     * 得到与key相匹配的队列
     *
     * @param key
     * @return
     */
    public static MessageQueue<P2PMessage> getSendP2PMessageRPCMsgQueue(long key) {
        Long index = key % threadCnt;
        return sendP2PMessageRPCMsgQueueMap.get(index.intValue());
    }

    public static void push(Long userId, P2PMessage rpcCmd) {
        getSendP2PMessageRPCMsgQueue(userId).push(rpcCmd, Duration.ofMillis(100));
    }


    public static int getToSendP2PMessageRPCMsgQueueSize(int index) {
        return getSendP2PMessageRPCMsgQueue(index).size();
    }

    public static int getToSendP2PMessageRPCMsgQueueSize(long key) {
        return getSendP2PMessageRPCMsgQueue(key).size();
    }


}
