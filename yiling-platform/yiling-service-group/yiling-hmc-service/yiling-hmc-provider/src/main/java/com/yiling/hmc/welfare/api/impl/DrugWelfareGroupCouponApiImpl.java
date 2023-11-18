package com.yiling.hmc.welfare.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareGroupCouponApi;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponVerificationDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponVerificationRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.service.DrugWelfareGroupCouponService;

/**
 * 药品福利计划入组API
 *
 * @author: fan.shen
 * @date: 2022-09-26
 */
@DubboService
public class DrugWelfareGroupCouponApiImpl implements DrugWelfareGroupCouponApi {

    @Autowired
    private DrugWelfareGroupCouponService groupCouponService;

    @Override
    public List<DrugWelfareGroupCouponDTO> getWelfareGroupCouponByGroupId(Long groupId) {
        return PojoUtils.map(groupCouponService.getWelfareGroupCouponByGroupId(groupId), DrugWelfareGroupCouponDTO.class);
    }

    @Override
    public List<DrugWelfareGroupCouponDTO> getWelfareGroupCouponByGroupIdList(List<Long> groupIdList) {
        return PojoUtils.map(groupCouponService.getWelfareGroupCouponByGroupIdList(groupIdList), DrugWelfareGroupCouponDTO.class);
    }

    @Override
    public Page<DrugWelfareGroupCouponStatisticsPageDTO> exportStatistics(DrugWelfareStatisticsPageRequest request) {
        return groupCouponService.exportStatistics(request);
    }

    @Override
    public Page<DrugWelfareGroupCouponDTO> listDrugWelfareGroupCoupon(DrugWelfareGroupCouponListRequest request){
        return PojoUtils.map(groupCouponService.listDrugWelfareGroupCoupon(request), DrugWelfareGroupCouponDTO.class);
    }

    @Override
    public DrugWelfareGroupCouponVerificationDTO verificationDrugWelfareGroupCoupon(DrugWelfareGroupCouponVerificationRequest request) {
        return PojoUtils.map(groupCouponService.verificationDrugWelfareGroupCoupon(request), DrugWelfareGroupCouponVerificationDTO.class);
    }
}