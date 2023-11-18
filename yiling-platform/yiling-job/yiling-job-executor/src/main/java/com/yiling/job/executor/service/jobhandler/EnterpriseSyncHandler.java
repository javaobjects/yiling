package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.EasIncrementStampApi;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpCustomerApi;
import com.yiling.open.erp.api.ErpDataStatApi;
import com.yiling.open.erp.api.ErpFlowControlApi;
import com.yiling.open.erp.api.ErpGoodsApi;
import com.yiling.open.erp.api.ErpGoodsBatchApi;
import com.yiling.open.erp.api.ErpGoodsBatchFlowApi;
import com.yiling.open.erp.api.ErpGoodsCustomerPriceApi;
import com.yiling.open.erp.api.ErpGoodsGroupPriceApi;
import com.yiling.open.erp.api.ErpOrderSendApi;
import com.yiling.open.erp.api.ErpPurchaseFlowApi;
import com.yiling.open.erp.api.ErpSaleFlowApi;
import com.yiling.open.erp.api.ErpSettlementApi;
import com.yiling.open.ftp.api.FlowFtpApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Component
@Slf4j
public class EnterpriseSyncHandler {

    @DubboReference(async = true)
    private EnterpriseApi enterpriseApi;

    @JobLog
    @XxlJob("enterpriseSyncHandler")
    public ReturnT<String> enterpriseSync(String param) throws Exception {
        log.info("任务开始：线上客户信息打标");
        long start = System.currentTimeMillis();
        enterpriseApi.syncHandlerFlag();

        log.info("任务结束：线上客户信息打标。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}
