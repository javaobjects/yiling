package com.yiling.job.executor.service.jobhandler;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Component
public class OrderAutomaticReceiveJobHandler {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference(async = true)
    OrderProcessApi orderProcessApi;

    @JobLog
    @XxlJob("orderAutomaticReceiveJobHandler")
    public ReturnT<String> erpSyncGoods(String param) throws Exception {
        log.info("任务开始：非以岭采购未收货超过5天自动收货");
        List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);
        List<Long> longs = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
        subEidLists.addAll(longs);
        orderProcessApi.secondBusinessAutomaticReceive(subEidLists);
        DubboUtils.quickAsyncCall("orderProcessApi","secondBusinessAutomaticReceive");
        log.info("任务结束：非以岭采购未收货超过5天自动收货");
        return ReturnT.SUCCESS;
    }

}
