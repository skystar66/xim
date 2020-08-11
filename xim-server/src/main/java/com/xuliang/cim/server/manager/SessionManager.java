package com.xuliang.cim.server.manager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SessionManager {


    private static ConcurrentHashMap<Long, Channel> userChannelMap = new ConcurrentHashMap<>();


    /**
     * 用户登录
     */
    public static void login(Long userId, Channel channel) {

        if (userChannelMap.containsKey(userId)) {
            log.info("用户已登录 [{}]", userId);
            return;
        }
        userChannelMap.put(userId, channel);
    }


    /**
     * 用户下线
     */
    public static void offline(Long userId) {
        if (!userChannelMap.containsKey(userId)) {
            log.info("用户未登录 [{}]", userId);
            return;
        }
        userChannelMap.remove(userId);
    }


    /**
     * 获取用户channel
     */
    public static Channel getChannel(Long userId) {
        if (!userChannelMap.containsKey(userId)) {
            log.info("用户未登录 [{}]", userId);
            return null;
        }
        return userChannelMap.get(userId);
    }

    /**
     * 获取非当前用户的其它用户 所有channel
     */
    public static List<Channel> getAllOtherChannels(Long userId) {
        if (!userChannelMap.containsKey(userId)) {
            log.info("用户未登录 [{}]", userId);
            return null;
        }
        List<Channel> channelList = new ArrayList<>();
        for (Map.Entry<Long, Channel> longChannelEntry : userChannelMap.entrySet()) {
            if (longChannelEntry.getKey().equals(userId)) {
                continue;
            }
            channelList.add(longChannelEntry.getValue());
        }
        return channelList;
    }



    /**
     * 获取非当前用户的其它用户 所有channel
     */
    public static List<Long> getAllOtherUserIds(Long userId) {
        if (!userChannelMap.containsKey(userId)) {
            log.info("用户未登录 [{}]", userId);
            return null;
        }
        List<Long> channelList = new ArrayList<>();
        for (Map.Entry<Long, Channel> longChannelEntry : userChannelMap.entrySet()) {
            if (longChannelEntry.getKey().equals(userId)) {
                continue;
            }
            channelList.add(longChannelEntry.getKey());
        }
        return channelList;
    }


}
