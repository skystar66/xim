package com.xuliang.cim.server.task;

import com.xuliang.cim.server.mq.MQProvider;
import com.xuliang.cim.server.mq.model.P2PMessage;
import com.xuliang.cim.server.util.Constants;
import com.xuliang.cim.server.util.JsonV2Util;
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

import static com.xuliang.cim.server.util.Constants.P2P;

/**
 * 推送失败消息，进行补偿
 */
@Component
@Slf4j
public class PushFailMessageTask {


    private ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("push-fail-message-schedule-pool").daemon(true).build());


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                retrySendFailMessage();
            } catch (Exception e) {
                log.warn("pushFailMessageTask", e);
            }
        }, 5, 1, TimeUnit.SECONDS);

    }

    /**
     * 重发所有的 失败消息
     */
    public void retrySendFailMessage() {

        Map<Object, Object> failMessageMap = stringRedisTemplate.opsForHash().entries(String.format(Constants.FAIL_MESSAGE_MAP, P2P));
        if (failMessageMap == null) {
            return;
        }
        for (Map.Entry<Object, Object> objectObjectEntry : failMessageMap.entrySet()) {
            P2PMessage p2PMessage = JsonV2Util.readValue(objectObjectEntry.getValue().toString(), P2PMessage.class);
            MQProvider.push(p2PMessage.getUserId(), p2PMessage);
        }
    }
}
