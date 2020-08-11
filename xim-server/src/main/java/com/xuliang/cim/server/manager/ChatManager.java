package com.xuliang.cim.server.manager;

import com.xuliang.cim.server.connection.Connection;
import com.xuliang.cim.server.creator.MessageCreator;
import com.xuliang.cim.server.delay.DelayMsgCommand;
import com.xuliang.cim.server.mq.MQProvider;
import com.xuliang.cim.server.mq.model.P2PMessage;
import com.xuliang.cim.server.token.Token;
import com.xuliang.cim.server.token.TokenHelper;
import com.xuliang.cim.server.util.Constants;
import com.xuliang.cim.server.util.JsonV2Util;
import com.xuliang.xim.common.req.*;
import com.xuliang.xim.nettymsg.dto.RpcCmd;
import com.xuliang.xim.nettymsg.enums.CMDEvent;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.xuliang.cim.server.util.Constants.P2P;
import static com.xuliang.xim.nettymsg.MessageConstants.STATE_OK;
import static com.xuliang.xim.nettymsg.enums.ResponseCode.*;

@Component
@Slf4j
public class ChatManager {


    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Autowired
    Executor taskExecutor;


    @Autowired
    DelayMsgCommand delayMsgCommand;

    /**
     * 用户登录
     */
    public void handleLogin(Channel channel, RpcCmd rpcCmd) {
        LoginReqVO loginReqVO = rpcCmd.getMsg().loadBean(LoginReqVO.class);

        /**获取用户的token*/
        String token = tokenHelper.getToken(loginReqVO.getUserId());
        if (StringUtils.isEmpty(token)) {
            token = Token.createToken();
            tokenHelper.setToken(loginReqVO.getUserId(), token);
            log.info("user first login: userId:{}", loginReqVO.getUserId());
            SessionManager.login(loginReqVO.getUserId(), channel);
        }
        /**将token返回客户端*/
        rpcCmd.setToken(token);
        log.info("user login: userId:{}", loginReqVO.getUserId());
        Connection.sendMsg(channel, MessageCreator.okResponse(rpcCmd, STATE_OK));
    }

    /**
     * 私聊
     */
    public void p2pChat(Channel channel, RpcCmd rpcCmd) {
        P2PReqVO p2PReqVO = rpcCmd.getMsg().loadBean(P2PReqVO.class);

        /**校验发信人是否登录*/
        if (StringUtils.isEmpty(rpcCmd.getToken())) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        /**校验收信人是否登录*/
        Channel reciveChannel = SessionManager.getChannel(p2PReqVO.getReceiveUserId());
        if (reciveChannel == null) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, RECIVE_USER_NOT_LOGIN));
            return;
        }

        /**校验用户的token*/
        boolean isLogin = tokenHelper.checkToken(p2PReqVO.getUserId(), rpcCmd.getToken());
        if (!isLogin) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        P2PMessage p2PMessage = new P2PMessage();
        p2PMessage.setUserId(p2PReqVO.getReceiveUserId());
        p2PMessage.setRpcCmd(MessageCreator.okResponse(rpcCmd, STATE_OK));
        MQProvider.push(p2PMessage.getUserId(), p2PMessage);

        /**todo 消息回执*/
        rpcCmd.setEvent(CMDEvent.ACK.name());
        Connection.sendMsg(channel, MessageCreator.okResponse(rpcCmd, STATE_OK));
    }

    /**
     * 群聊
     */
    public void groupChat(Channel channel, RpcCmd rpcCmd) {
        GroupReqVO groupReqVO = rpcCmd.getMsg().loadBean(GroupReqVO.class);

        /**校验发信人是否登录*/
        if (StringUtils.isEmpty(rpcCmd.getToken())) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        /**校验用户的token*/
        boolean isLogin = tokenHelper.checkToken(groupReqVO.getUserId(), rpcCmd.getToken());
        if (!isLogin) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        /**获取其它所有的用户channel*/

        List<Long> userIds = SessionManager.getAllOtherUserIds(groupReqVO.getUserId());

        userIds.stream().forEach(userId -> {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    P2PMessage p2PMessage = new P2PMessage();
                    p2PMessage.setUserId(userId);
                    p2PMessage.setRpcCmd(MessageCreator.okResponse(rpcCmd, STATE_OK));
                    MQProvider.push(userId, p2PMessage);
                }
            });
        });

        /**todo 添加ack 消息回执*/
        rpcCmd.setEvent(CMDEvent.ACK.name());
        Connection.sendMsg(channel, MessageCreator.okResponse(rpcCmd, STATE_OK));
    }

    /**
     * 处理ack
     */
    public void ack(Channel channel, RpcCmd rpcCmd) {
        AckReqVO ackReqVO = rpcCmd.getMsg().loadBean(AckReqVO.class);

        /**校验发信人是否登录*/
        if (StringUtils.isEmpty(rpcCmd.getToken())) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        /**校验用户的token*/
        boolean isLogin = tokenHelper.checkToken(ackReqVO.getUserId(), rpcCmd.getToken());
        if (!isLogin) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }
        stringRedisTemplate.opsForHash().delete(String.format(Constants.SEND_MESSAGE_MAP, P2P), ackReqVO.getMessageId());
        stringRedisTemplate.opsForHash().put(String.format(Constants.RECIVED_MESSAGE_MAP, P2P), ackReqVO.getMessageId(), ackReqVO.getMessage());
    }


    /**
     * 拉取离线消息
     */
    public void pollOfflineMessage(Channel channel, RpcCmd rpcCmd) {
        PollOfflineMessageReqVO pollOfflineMessageReqVO = rpcCmd.getMsg().loadBean(PollOfflineMessageReqVO.class);

        /**校验发信人是否登录*/
        if (StringUtils.isEmpty(rpcCmd.getToken())) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        /**校验用户的token*/
        boolean isLogin = tokenHelper.checkToken(pollOfflineMessageReqVO.getUserId(), rpcCmd.getToken());
        if (!isLogin) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }
        Set<String> messages = stringRedisTemplate.opsForZSet().range(Constants.OFFLINE_MESSAGE_MAP, pollOfflineMessageReqVO.getStart(), pollOfflineMessageReqVO.getEnd());
        rpcCmd.getMsg().setData(JsonV2Util.writeValue(messages));
        Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));

    }

    /**
     * 发送延迟消息
     */
    public void delayMessage(Channel channel, RpcCmd rpcCmd) {
        GroupReqVO groupReqVO = rpcCmd.getMsg().loadBean(GroupReqVO.class);

        /**校验发信人是否登录*/
        if (StringUtils.isEmpty(rpcCmd.getToken())) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        /**校验用户的token*/
        boolean isLogin = tokenHelper.checkToken(groupReqVO.getUserId(), rpcCmd.getToken());
        if (!isLogin) {
            Connection.sendMsg(channel, MessageCreator.bussinesError(rpcCmd, SEND_USER_NOT_LOGIN));
            return;
        }

        /**发送延迟消息*/
        delayMsgCommand.process(rpcCmd);
    }


    /**
     * 用户退出登录
     */
    public void handleLogout(Channel channel, RpcCmd rpcCmd) {
        LoginOutReqVO loginOutReqVO = rpcCmd.getMsg().loadBean(LoginOutReqVO.class);
        /**清除 抹掉 token*/
        tokenHelper.clearToken(loginOutReqVO.getUserId());
        /**清除session  manager*/
        SessionManager.offline(loginOutReqVO.getUserId());

    }


}
