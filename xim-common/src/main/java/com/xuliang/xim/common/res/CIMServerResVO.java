package com.xuliang.xim.common.res;

import com.xuliang.xim.common.pojo.RouteInfo;

import java.io.Serializable;

/**
 * Function:
 *
 * @author xuliang
 *         Date: 2018/12/23 00:43
 * @since JDK 1.8
 */
public class CIMServerResVO implements Serializable {

    private String ip ;
    private Integer cimServerPort;
    private Integer httpPort;

    public CIMServerResVO(RouteInfo routeInfo) {
        this.ip = routeInfo.getIp();
        this.cimServerPort = routeInfo.getCimServerPort();
        this.httpPort = routeInfo.getHttpPort();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCimServerPort() {
        return cimServerPort;
    }

    public void setCimServerPort(Integer cimServerPort) {
        this.cimServerPort = cimServerPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
}
