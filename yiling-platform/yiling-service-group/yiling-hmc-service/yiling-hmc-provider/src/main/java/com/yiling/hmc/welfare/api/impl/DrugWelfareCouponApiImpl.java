package com.yiling.hmc.welfare.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareCouponApi;
import com.yiling.hmc.welfare.dto.DrugWelfareCouponDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareCouponUpdateRequest;
import com.yiling.hmc.welfare.service.DrugWelfareCouponService;

/**
 * 药品福利计划券包API
 *
 * @author: fan.shen
 * @date: 2022-09-26
 */
@DubboService
public class DrugWelfareCouponApiImpl implements DrugWelfareCouponApi {

    @Autowired
    private DrugWelfareCouponService couponService;

    @Override
    public List<DrugWelfareCouponDTO> getByWelfareId(Long id) {
        return PojoUtils.map(couponService.getByWelfareId(id), DrugWelfareCouponDTO.class);
    }

    @Override
    public List<DrugWelfareCouponDTO> queryByDrugWelfareId(Long drugWelfareId) {
        return PojoUtils.map(couponService.queryByDrugWelfareId(drugWelfareId), DrugWelfareCouponDTO.class);
    }

    @Override
    public Boolean updateDrugWelfareCoupon(List<DrugWelfareCouponUpdateRequest> requestList) {
        return couponService.updateDrugWelfareCoupon(requestList);
    }
}