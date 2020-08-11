package com.xuliang.cim.server.mq.model;

import com.xuliang.xim.nettymsg.dto.RpcCmd;
import io.netty.channel.Channel;
import lombok.Data;

@Data
public class P2PMessage {


    private Long userId;

    private RpcCmd rpcCmd;


    /**消息发送时间*/
    private Long sendTime;



}
