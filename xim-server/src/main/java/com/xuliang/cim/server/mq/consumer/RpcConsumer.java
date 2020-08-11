package com.xuliang.cim.server.mq.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.xuliang.cim.server.mq.MQProvider.threadCnt;


@Component
public class RpcConsumer {


    private ExecutorService msgSenderExecutor;

    @Autowired
    Executor taskExecutor;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**
     * 消费者数目
     */
    private static int consumerQueueCount = 1;


    public void start() {
        msgSenderExecutor = Executors.newFixedThreadPool(threadCnt * consumerQueueCount);
        for (int i = 0; i < threadCnt; i++) {
            msgSenderExecutor.execute(new SendP2PMessageConsumer(i, stringRedisTemplate));
        }
    }


}
