package com.xuliang.xim.common.req;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Function: ack
 *
 * @author crossoverJie
 * Date: 2018/05/21 15:56
 * @since JDK 1.8
 */
@Data
public class AckReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
/*
    @ApiModelProperty(required = true, value = "消息发送者的 userId", example = "1545574049323")
*/
    private Long userId;


    @NotNull(message = "messageId 不能为空")
/*
    @ApiModelProperty(required = true, value = "msg", example = "hello")
*/
    private Long messageId;

    @NotNull(message = "messageId 不能为空")
    private String message;

    @Override
    public String toString() {
        return "AckReqVO{" +
                "userId=" + userId +
                ", messageId=" + messageId +
                '}';
    }
}
