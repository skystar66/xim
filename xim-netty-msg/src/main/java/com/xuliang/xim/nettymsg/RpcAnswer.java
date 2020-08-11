package com.xuliang.xim.nettymsg;


import com.xuliang.xim.nettymsg.dto.RpcCmd;


/**
 * @author xuliang
 * @desc: 服务端/客户端 业务处理
 */

public interface RpcAnswer {


    /**
     * 业务处理
     *
     * @param rpcCmd message 曾业务回调函数
     */
    void callback(RpcCmd rpcCmd);


}
