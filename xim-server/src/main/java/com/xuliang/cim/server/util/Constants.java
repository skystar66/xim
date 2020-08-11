package com.xuliang.cim.server.util;

public class Constants {


    /**
     * redis 发送失败消息 key
     */
    public static final String FAIL_MESSAGE_MAP = "fail_message_mqp:%s";


    /**
     * redis 已发送消息集合 key
     */
    public static final String SEND_MESSAGE_MAP = "send_message_map:%s";


    /**
     * redis 已接收消息集合 key
     */
    public static final String RECIVED_MESSAGE_MAP = "recived_message_map:%s";

    /**
     * redis 离线消息集合 key
     */
    public static final String OFFLINE_MESSAGE_MAP = "offline_message_map";


    /**
     * 多长时间检查一次ack消息 单位ms 消息延迟性 1s
     */
    public static final Long CHECK_ACK_MESSAGE_TIME = 1000L;


    public static final String P2P="p2p";




}
