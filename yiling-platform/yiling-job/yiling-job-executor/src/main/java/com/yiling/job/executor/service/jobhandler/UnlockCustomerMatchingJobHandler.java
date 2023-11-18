package com.yiling.job.executor.service.jobhandler;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.wash.api.UnlockCustomerMatchingInfoApi;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.job.executor.log.JobLog;
import com.yiling.search.flow.api.EsFlowEnterpriseCustomerMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseCustomerMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseCustomerMappingSearchRequest;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/6/6
 */
@Component
@Slf4j
public class UnlockCustomerMatchingJobHandler {

    @DubboReference
    private UnlockCustomerMatchingInfoApi unlockCustomerMatchingInfoApi;

    @JobLog
    @XxlJob("unlockCustomerMatchingJobHandler")
    public ReturnT<String> unlockCustomerMatchingBatchJobHandler(String param) throws Exception {
        //  异步 批量执行匹配度任务
        unlockCustomerMatchingInfoApi.matchingRateBatchJobStart();
        return ReturnT.SUCCESS;
    }

//    @JobLog
//    @XxlJob("unlockCustomerMatchingJobHandler")
//    public ReturnT<String> unlockCustomerMatchingSingleJobHandler(String param) throws Exception {
//        if (StringUtils.isEmpty(param)) {
//            return ReturnT.SUCCESS;
//        }
//        String customerName = param;
//        unlockCustomerMatchingInfoApi.matchingRateExecute(customerName);
//        return ReturnT.SUCCESS;
//    }

}
