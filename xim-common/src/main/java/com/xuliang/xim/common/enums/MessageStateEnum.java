package com.xuliang.xim.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuliang
 * @desc: 消息状态，已发送，已接受
 */

public enum MessageStateEnum {

    /**
     * 已发送
     */
    SEND(0, "已发送"),

    /**
     *
     */
    RECIVED(1, "已接收"),
    ;


    /**
     * 枚举值码
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String message;

    /**
     * 构建一个 StatusEnum 。
     *
     * @param code    枚举值码。
     * @param message 枚举描述。
     */
    private MessageStateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 得到枚举值码。
     *
     * @return 枚举值码。
     */
    public int getCode() {
        return code;
    }

    /**
     * 得到枚举描述。
     *
     * @return 枚举描述。
     */
    public String getMessage() {
        return message;
    }

    /**
     * 得到枚举值码。
     *
     * @return 枚举值码。
     */
    public int code() {
        return code;
    }

    /**
     * 得到枚举描述。
     *
     * @return 枚举描述。
     */
    public String message() {
        return message;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 StatusEnum 。
     */
    public static MessageStateEnum findStatus(int code) {
        for (MessageStateEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("ResultInfo StatusEnum not legal:" + code);
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<MessageStateEnum> getAllStatus() {
        List<MessageStateEnum> list = new ArrayList<MessageStateEnum>();
        for (MessageStateEnum status : values()) {
            list.add(status);
        }
        return list;
    }

}
