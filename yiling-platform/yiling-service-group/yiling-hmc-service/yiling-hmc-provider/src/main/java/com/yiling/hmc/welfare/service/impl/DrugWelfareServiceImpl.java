package com.yiling.hmc.welfare.service.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareCouponApi;
import com.yiling.hmc.welfare.dao.DrugWelfareMapper;
import com.yiling.hmc.welfare.dto.request.DrugWelfarePageRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareUpdateRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareDO;
import com.yiling.hmc.welfare.service.DrugWelfareService;

/**
 * <p>
 * 药品福利表 服务实现类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Service
public class DrugWelfareServiceImpl extends BaseServiceImpl<DrugWelfareMapper, DrugWelfareDO> implements DrugWelfareService {


    @DubboReference
    DrugWelfareCouponApi drugWelfareCouponApi;

    @Override
    public List<DrugWelfareDO> getByIdList(List<Long> idList) {
        QueryWrapper<DrugWelfareDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(DrugWelfareDO::getId, idList);
        return this.list(queryWrapper);
    }

    @Override
    public Page<DrugWelfareDO> pageList(DrugWelfarePageRequest request) {
        QueryWrapper<DrugWelfareDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(DrugWelfareDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public boolean updateDrugWelfare(DrugWelfareUpdateRequest request) {
        DrugWelfareDO drugWelfareDO = PojoUtils.map(request, DrugWelfareDO.class);
        this.saveOrUpdate(drugWelfareDO);
        request.getDrugWelfareCouponList().forEach(couponUpdateRequest-> couponUpdateRequest.setOpUserId(request.getOpUserId()));
        drugWelfareCouponApi.updateDrugWelfareCoupon(request.getDrugWelfareCouponList());
        return this.saveOrUpdate(drugWelfareDO);
    }
}
