package com.xuliang.xim.netty.util;


import com.xuliang.xim.netty.enums.NettyType;

/**
 * Description:
 * Company: CodingApi
 * Date: 2020/12/21
 *
 * @author xulia
 */
public class NettyContext {

    public static NettyType nettyType;


    public static NettyType currentType() {
        return nettyType;
    }

    public static Object params;

    public static <T> T currentParam(Class<T> tClass) {
        return (T) params;
    }


}
