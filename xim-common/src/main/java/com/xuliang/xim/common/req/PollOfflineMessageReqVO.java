package com.xuliang.xim.common.req;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Function: 群发请求
 *
 * @author crossoverJie
 *         Date: 2018/05/21 15:56
 * @since JDK 1.8
 */
@Data
public class PollOfflineMessageReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
/*
    @ApiModelProperty(required = true, value = "消息发送者的 userId", example = "1545574049323")
*/
    private Long userId ;



    private Long start ;
    private Long end ;

    @Override
    public String toString() {
        return "PollOfflineMessageReqVO{" +
                "userId=" + userId +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
