package com.xuliang.cim.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xim.server")
@Data
public class XIMServerConfig {

    /**
     * manager host
     */
    private String host = "127.0.0.1";

    /**
     * support  port
     */
    private int port;

    /**
     * netty heart check time (ms) 20s 心跳检测一次
     */
    private long heartTime = 20 * 1000;


}
