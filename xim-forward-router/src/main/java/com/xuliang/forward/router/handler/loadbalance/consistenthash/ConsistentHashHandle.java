package com.xuliang.forward.router.handler.loadbalance.consistenthash;

import com.xuliang.forward.router.handler.RouteHandle;

import java.util.List;

/**
 * Function:
 *
 * @author xuliang
 * Date: 2019-02-27 00:33
 * @since JDK 1.8
 */
public class ConsistentHashHandle implements RouteHandle {
    private AbstractConsistentHash hash ;

    public void setHash(AbstractConsistentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values, key);
    }
}
