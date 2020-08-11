package com.xuliang.xim.nettymsg.enums;

/**
 * @author zhulei
 * @date 2017/9/18 20:32
 *
 * 1 通用错误码 10000
 * 2 账户错误码 20000
 * 3 交易错误码 30000
 * 4 提现错误码 40000
 * 5 充值错误码 50000
 * 6 币种错误吗 60000
 * 7 产品错误码 70000
 */
public enum ResponseCode {
    /**
     * 错误码
     */
    INVALID_REQUEST("10000", "无效请求"),
    INVALID_MESSAGE("10001", "无效消息"),
    INVALID_EVENT("10002", "无效事件类型"),
    INVALID_TOPIC("10004", "无效的主题"),
    NOT_LOGIN("10006", "未登录"),
    SEND_USER_NOT_LOGIN("10007", "发送人未登录"),
    RECIVE_USER_NOT_LOGIN("10008", "收信人未登录"),
    UPDATE_READ_FLAG_FAIL("10009", "标记已读失败"),
    DELETE_MESSAGE_FAIL("10010", "删除失败"),
    ;
    private final String code;
    private final String desc;

    ResponseCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return new StringBuffer("{\"code\":\"")
            .append(code)
            .append("\",\"desc\":\"")
            .append(desc)
            .append("\"}")
            .toString();
    }
}
