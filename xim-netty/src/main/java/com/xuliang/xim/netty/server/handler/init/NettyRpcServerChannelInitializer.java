package com.xuliang.xim.netty.server.handler.init;

import com.xuliang.xim.netty.handler.ReciveDataHandler;
import com.xuliang.xim.netty.handler.RpcAnswerHandler;
import com.xuliang.xim.netty.handler.SendDataHandler;
import com.xuliang.xim.netty.server.handler.SocketManagerInitHandler;
import com.xuliang.xim.netty.properties.ManagerProperties;
import com.xuliang.xim.netty.protocol.ObjectSerializerDecoder;
import com.xuliang.xim.netty.protocol.ObjectSerializerEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * Description:
 * Company: app
 * Date: 2020/2/10
 *
 * @author xuliang
 * @desc:服务端 handler
 */

@Component
public class NettyRpcServerChannelInitializer extends ChannelInitializer<Channel> {

    @Autowired
    RpcAnswerHandler rpcAnswerHandler;

    @Autowired
    SocketManagerInitHandler socketManagerInitHandler;


    @Autowired
    ReciveDataHandler reciveDataHandler;

    private ManagerProperties managerProperties;


    public void setManagerProperties(ManagerProperties managerProperties) {
        this.managerProperties = managerProperties;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {

        //这两个类配合使用解决半包与粘包问题
        ch.pipeline().addLast(new LengthFieldPrepender(4, false));
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

        ch.pipeline().addLast(new IdleStateHandler(managerProperties.getCheckTime(),
                managerProperties.getCheckTime(), managerProperties.getCheckTime(), TimeUnit.MILLISECONDS));

        //编码与解码 protosbuff
        ch.pipeline().addLast(new ObjectSerializerEncoder());
        ch.pipeline().addLast(new ObjectSerializerDecoder());


        //框架特有的Handler
        ch.pipeline().addLast(reciveDataHandler);
        ch.pipeline().addLast(new SendDataHandler());
        ch.pipeline().addLast(socketManagerInitHandler);
        ch.pipeline().addLast(rpcAnswerHandler);
    }
}
