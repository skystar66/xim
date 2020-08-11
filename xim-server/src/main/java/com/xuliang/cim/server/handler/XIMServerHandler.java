package com.xuliang.cim.server.handler;

import com.xuliang.cim.server.manager.NettyChannelManager;
import com.xuliang.cim.server.manager.ChatManager;
import com.xuliang.xim.nettymsg.RpcAnswer;
import com.xuliang.xim.nettymsg.dto.RpcCmd;
import com.xuliang.xim.nettymsg.enums.CMDEvent;
import com.xuliang.xim.nettymsg.enums.ResponseCode;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class XIMServerHandler implements RpcAnswer {


    @Autowired
    ChatManager chatManager;

    @Override
    public void callback(RpcCmd rpcCmd) {
        //获取channel
        Channel channel = NettyChannelManager.getInstance().getChannel(rpcCmd.getRemoteKey());
        if (channel == null) {
            log.info("channel 已失效！");
            return;
        }
        if (Objects.isNull(rpcCmd)) {
            channel.writeAndFlush(ResponseCode.INVALID_EVENT);
            log.info("msg is null");
            return;
        }

        /**具体业务处理*/
        CMDEvent cmdEvent = CMDEvent.of(rpcCmd.getEvent());
        switch (cmdEvent) {
            /**登录*/
            case LOGIN:
                chatManager.handleLogin(channel, rpcCmd);
                break;
            /**私聊*/
            case P2P:
                chatManager.p2pChat(channel, rpcCmd);
                break;
            /**群聊*/
            case GROUP:
                chatManager.groupChat(channel, rpcCmd);
                break;
            /**心跳*/
            case heartCheck:
                //todo  心跳检测会被拦截，不用检查
                break;
            /**延迟消息*/
            case DELAY_MESSAGE:
                chatManager.delayMessage(channel, rpcCmd);
                break;
            /**ack*/
            case ACK:
                chatManager.ack(channel, rpcCmd);
                break;
            /**拉取离线消息*/
            case POLL_OFFLINE_MESSAGE:
                chatManager.pollOfflineMessage(channel, rpcCmd);
                break;
            /**退出登录*/
            case LOGOUT:
                chatManager.handleLogout(channel, rpcCmd);
                break;
            default:
                break;
        }
    }
}
