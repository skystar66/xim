package com.xuliang.cim.server.token;

import java.util.UUID;

public class Token {



    /**生成token*/
    public static String createToken(){
        return UUID.randomUUID().toString();
    }


}
