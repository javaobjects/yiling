package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockCustomerMatchingInfoApi;
import com.yiling.dataflow.wash.dto.UnlockCustomerMatchingInfoDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerMatchingInfoPageRequest;
import com.yiling.dataflow.wash.service.UnlockCustomerMatchingInfoService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/5/29
 */
@Slf4j
@DubboService
public class UnlockCustomerMatchingInfoApiImpl implements UnlockCustomerMatchingInfoApi {

    @Autowired
    private UnlockCustomerMatchingInfoService unlockCustomerMatchingInfoService;

    @Override
    public List<UnlockCustomerMatchingInfoDTO> getListByCustomerName(String customerName) {
        return PojoUtils.map(unlockCustomerMatchingInfoService.getListByCustomerName(customerName), UnlockCustomerMatchingInfoDTO.class);
    }

    /**
     * 单个名称匹配
     * @param name
     */
    @Override
    public void matchingRateExecute(String name) {
        unlockCustomerMatchingInfoService.matchingRateExecute(name);
    }


    @Override
    public void matchingRateBatchJobStart() {
        unlockCustomerMatchingInfoService.matchingRateBatchExecute();
    }

    @Override
    public Page<UnlockCustomerMatchingInfoDTO> getPageList(QueryUnlockCustomerMatchingInfoPageRequest request) {
        return PojoUtils.map(unlockCustomerMatchingInfoService.getPageList(request), UnlockCustomerMatchingInfoDTO.class);
    }
}
