package com.xuliang.cim.server.delay;

import com.xuliang.cim.server.mq.MQProvider;
import com.xuliang.cim.server.mq.model.P2PMessage;
import com.xuliang.xim.common.req.P2PReqVO;
import com.xuliang.xim.nettymsg.dto.RpcCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2019-09-25 00:37
 * @since JDK 1.8
 */
@Service
public class DelayMsgCommand {


    @Autowired
    private RingBufferWheel ringBufferWheel;

    public void process(RpcCmd msg) {
        P2PReqVO pReqVO = msg.getMsg().loadBean(P2PReqVO.class);

        if (pReqVO.getMsg().split(" ").length <= 2) {
            return;
        }

        String message = pReqVO.getMsg().split(" ")[1];
        Integer delayTime = Integer.valueOf(pReqVO.getMsg().split(" ")[2]);

        P2PMessage p2PMessage = new P2PMessage();
        p2PMessage.setRpcCmd(msg);
        p2PMessage.setUserId(pReqVO.getReceiveUserId());
        RingBufferWheel.Task task = new DelayMsgJob(p2PMessage);
        task.setKey(delayTime);
        ringBufferWheel.addTask(task);
    }


    private class DelayMsgJob extends RingBufferWheel.Task {

        private P2PMessage msg;

        public DelayMsgJob(P2PMessage msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            MQProvider.push(msg.getUserId(), msg);
        }
    }
}
