package com.xuliang.cim.server.connection;

import com.xuliang.xim.nettymsg.dto.RpcCmd;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Connection {


    /**
     * Push msg to client.
     *
     * @param rpcCmd 消息
     */
    public static void sendMsg(Channel channel, RpcCmd rpcCmd) {
        ChannelFuture future = channel.writeAndFlush(rpcCmd);
        future.addListener((ChannelFutureListener) channelFuture ->
                log.info("server push msg:[{}]", rpcCmd.toString()));
    }


}
