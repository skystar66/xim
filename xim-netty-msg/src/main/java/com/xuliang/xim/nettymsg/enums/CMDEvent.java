package com.xuliang.xim.nettymsg.enums;


import java.util.Arrays;

/**
 * 事件
 *
 * @author xuliang
 * @date 2017/9/19 17:34
 */

public enum CMDEvent {


    /**
     * 心跳事件
     */
    heartCheck,

    // 请求事件
    /**
     * 私聊
     */
    P2P,
    /**
     * 群聊
     */
    GROUP,
    /**
     * 登录
     */
    LOGIN,
    /**
     * 登出
     */
    LOGOUT,
    /**
     * 发送已读回执
     */
    ACK,
    /**
     * 拉取消息
     */
    POLL_OFFLINE_MESSAGE,

    // 业务响应事件
    /**
     * 发送延迟消息
     */
    DELAY_MESSAGE,

   ;

    public static CMDEvent of(String type) {
        return Arrays.stream(CMDEvent.values())
                .filter(CMDEvent -> CMDEvent.name().equalsIgnoreCase(type))
                .findFirst()
//            .orElseThrow(() ->new InvalidEventException("event "+ type +" not exist."));
                .orElse(null);
    }


}
