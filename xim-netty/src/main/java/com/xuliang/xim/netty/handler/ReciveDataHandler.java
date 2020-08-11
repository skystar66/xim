package com.xuliang.xim.netty.handler;


import com.xuliang.xim.nettymsg.MessageConstants;
import com.xuliang.xim.nettymsg.dto.RpcCmd;
import com.xuliang.xim.netty.enums.NettyType;
import com.xuliang.xim.netty.util.NettyContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class ReciveDataHandler extends SimpleChannelInboundHandler<RpcCmd> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCmd cmd) throws Exception {
        log.info("cmd->{}", cmd);
        //心态数据包直接响应
        if (cmd.getEvent() != null &&
                MessageConstants.ACTION_HEART_CHECK.equals(cmd.getEvent())) {
            if (NettyContext.currentType().equals(NettyType.client)) {
                //设置值
                ctx.writeAndFlush(cmd);
                return;
            }
            return;
        }

        // 通知执行下一个InboundHandler
        ctx.fireChannelRead(cmd);
    }
}
