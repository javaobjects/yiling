package com.yiling.hmc.wechat.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.hmc.wechat.api.InsuranceFetchPlanDetailApi;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanGroupDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanDetailRequest;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanDetailService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Slf4j
@DubboService
public class InsuranceFetchPlanDetailApiImpl implements InsuranceFetchPlanDetailApi {

    @Autowired
    private InsuranceFetchPlanDetailService fetchPlanDetailService;

    @Override
    public Boolean saveFetchPlanDetail(List<SaveInsuranceFetchPlanDetailRequest> requestList) {
        return fetchPlanDetailService.saveFetchPlanDetail(requestList);
    }

    @Override
    public List<InsuranceFetchPlanDetailDTO> getByInsuranceRecordId(Long insuranceRecordId) {
        return fetchPlanDetailService.getByInsuranceRecordId(insuranceRecordId);
    }

    @Override
    public List<InsuranceFetchPlanGroupDTO> groupByInsuranceRecordId(List<Long> insuranceRecordIdList) {
        return fetchPlanDetailService.groupByInsuranceRecordId(insuranceRecordIdList);
    }

    @Override
    public Map<Long, Long> queryGoodsCount(List<Long> goodsIdList) {
        return fetchPlanDetailService.queryGoodsCount(goodsIdList);
    }
}
