package com.xuliang.cim.server.creator;


import com.xuliang.xim.nettymsg.MessageConstants;
import com.xuliang.xim.nettymsg.dto.MessageDto;
import com.xuliang.xim.nettymsg.dto.RpcCmd;
import com.xuliang.xim.nettymsg.enums.ResponseCode;

/**
 * 消息创建器
 *
 * @author xuliang
 */
public class MessageCreator {


    /**
     * 正常响应
     *
     * @param action  action
     * @param message message
     * @return MessageDto
     */
    public static RpcCmd okResponse(RpcCmd message, int action) {
        message.getMsg().setState(action);
        return message;
    }

    /**
     * 失败响应
     *
     * @param action  action
     * @param message message
     * @return MessageDto
     */
    public static RpcCmd failResponse(RpcCmd message, int action) {
        message.getMsg().setState(action);
        return message;
    }


    /**
     * 失败响应
     *
     * @param action  action
     * @param message message
     * @return MessageDto
     */
    public static RpcCmd bussinesError(RpcCmd rpcCmd, ResponseCode responseCode) {
        rpcCmd.getMsg().setState(Integer.parseInt(responseCode.getCode()));
        return rpcCmd;
    }


    /**
     * 服务器错误
     *
     * @param action action
     * @return MessageDto
     */
    public static MessageDto serverException(String action) {
        MessageDto messageDto = new MessageDto();
        messageDto.setCmd(action);
        messageDto.setState(MessageConstants.STATE_EXCEPTION);
        return messageDto;
    }


}
