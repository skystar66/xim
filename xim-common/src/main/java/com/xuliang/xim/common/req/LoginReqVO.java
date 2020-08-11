package com.xuliang.xim.common.req;

import lombok.Data;

/**
 * Function:
 *
 * @author xuliang
 *         Date: 2018/12/23 22:30
 * @since JDK 1.8
 */
@Data
public class LoginReqVO extends BaseRequest{
    private Long userId ;
    private String userName ;



    @Override
    public String toString() {
        return "LoginReqVO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
