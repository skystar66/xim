package com.xuliang.forward.router.service;

import com.xuliang.forward.router.cache.ServerCache;
import com.xuliang.forward.router.exception.CIMException;
import com.xuliang.forward.router.kit.NetAddressIsReachable;
import com.xuliang.xim.common.enums.StatusEnum;
import com.xuliang.xim.common.pojo.RouteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Function:
 *
 * @author xuliang
 * Date: 2020-04-12 21:40
 * @since JDK 1.8
 */
@Component
public class CommonBizService {
    private static Logger logger = LoggerFactory.getLogger(CommonBizService.class) ;


    @Autowired
    private ServerCache serverCache ;

    /**
     * check ip and port
     * @param routeInfo
     */
    public void checkServerAvailable(RouteInfo routeInfo){
        boolean reachable = NetAddressIsReachable.checkAddressReachable(routeInfo.getIp(), routeInfo.getCimServerPort(), 1000);
        if (!reachable) {
            logger.error("ip={}, port={} are not available", routeInfo.getIp(), routeInfo.getCimServerPort());

            // rebuild cache
            serverCache.rebuildCacheList();

            throw new CIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }

    }
}
