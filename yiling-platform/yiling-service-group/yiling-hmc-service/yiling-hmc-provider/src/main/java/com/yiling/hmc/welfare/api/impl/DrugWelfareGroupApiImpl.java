package com.yiling.hmc.welfare.api.impl;

import java.util.List;

import com.yiling.hmc.welfare.dto.request.QueryGroupCouponRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareGroupApi;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.dto.request.SaveGroupRequest;
import com.yiling.hmc.welfare.service.DrugWelfareGroupService;

/**
 * 药品福利计划入组API
 *
 * @author: fan.shen
 * @date: 2022-09-26
 */
@DubboService
public class DrugWelfareGroupApiImpl implements DrugWelfareGroupApi {

    @Autowired
    private DrugWelfareGroupService groupService;

    @Override
    public DrugWelfareGroupDTO getWelfareGroupByWelfareIdAndUserId(Long welfareId, Long userId) {
        return PojoUtils.map(groupService.getWelfareGroupByWelfareIdAndUserId(welfareId, userId), DrugWelfareGroupDTO.class);
    }

    @Override
    public List<DrugWelfareGroupDTO> getWelfareGroupByUserId(Long userId) {
        return PojoUtils.map(groupService.getValidWelfareByUserId(userId), DrugWelfareGroupDTO.class);
    }

    @Override
    public Long joinGroup(SaveGroupRequest request) {
        return groupService.joinGroup(request);
    }

    @Override
    public DrugWelfareGroupDTO getById(Long groupId) {
        return PojoUtils.map(groupService.getById(groupId), DrugWelfareGroupDTO.class);
    }

    @Override
    public Page<DrugWelfareStatisticsPageDTO> statisticsPage(DrugWelfareStatisticsPageRequest request) {
        return PojoUtils.map(groupService.statisticsPage(request), DrugWelfareStatisticsPageDTO.class);
    }

    @Override
    public List<Long> getSellerUserIds() {
        return groupService.getSellerUserIds();
    }

    @Override
    public List<DrugWelfareGroupDTO> listDrugWelfareGroup(DrugWelfareGroupListRequest request) {
        return PojoUtils.map(groupService.listDrugWelfareGroup(request), DrugWelfareGroupDTO.class);
    }

    @Override
    public Integer queryVerifyStatus(QueryGroupCouponRequest request) {
        return groupService.queryVerifyStatus(request);
    }
}