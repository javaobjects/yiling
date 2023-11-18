package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.thirdbase.api.ErpBaseInterfaceConfigApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 广西明合药业有限责任公司-基础数据获取
 *
 * @author: houjie.sun
 * @date: 2023/6/17
 */
@Component
@Slf4j
public class ErpBaseInfoInterfaceJobHandler {

    @DubboReference(async = true)
    private ErpBaseInterfaceConfigApi erpBaseInterfaceConfigApi;

    /**
     * 广西明合药业有限责任公司, 查询商品信息
     * 每5分钟调度
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("erpBaseInfoInterfaceHandler")
    public ReturnT<String> erpBaseInfoInterfaceHandler(String param) {
        log.info("任务开始：erp基础对接接口任务同步");
        long start = System.currentTimeMillis();
        erpBaseInterfaceConfigApi.executeBaseInterface();
        log.info("任务结束：erp基础对接接口任务同步结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}
