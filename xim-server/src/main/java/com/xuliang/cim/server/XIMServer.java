package com.xuliang.cim.server;

import com.xuliang.cim.server.config.XIMServerConfig;
import com.xuliang.xim.netty.properties.ManagerProperties;
import com.xuliang.xim.netty.server.init.RpcServerInitializer;

public class XIMServer implements Runnable {


    XIMServerConfig rpcConfig;

    RpcServerInitializer rpcServerInitializer;

    public XIMServer(XIMServerConfig rpcConfig,
                     RpcServerInitializer rpcServerInitializer) {
        this.rpcConfig = rpcConfig;
        this.rpcServerInitializer = rpcServerInitializer;
    }


    @Override
    public void run() {

//        // 1. 配置
//        if (rpcConfig.getWaitTime() <= 5) {
//            rpcConfig.setWaitTime(1000);
//        }
//        if (rpcConfig.getAttrDelayTime() < 0) {
//            //网络延迟时间 8s
//            rpcConfig.setAttrDelayTime(txManagerConfig.getDtxTime());
//        }

        // 2. 初始化RPC Server
        ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setCheckTime(rpcConfig.getHeartTime());
        managerProperties.setRpcPort(rpcConfig.getPort());
        managerProperties.setRpcHost(rpcConfig.getHost());
        rpcServerInitializer.init(managerProperties);
    }
}