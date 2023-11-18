package com.yiling.hmc.welfare.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.dao.DrugWelfareEnterpriseMapper;
import com.yiling.hmc.welfare.dto.request.DeleteDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareEnterprisePageRequest;
import com.yiling.hmc.welfare.dto.request.SaveDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareDO;
import com.yiling.hmc.welfare.entity.DrugWelfareEnterpriseDO;
import com.yiling.hmc.welfare.entity.DrugWelfareGroupDO;
import com.yiling.hmc.welfare.service.DrugWelfareEnterpriseService;
import com.yiling.hmc.welfare.service.DrugWelfareGroupService;
import com.yiling.hmc.welfare.service.DrugWelfareService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 药品福利参与商家表 服务实现类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Service
public class DrugWelfareEnterpriseServiceImpl extends BaseServiceImpl<DrugWelfareEnterpriseMapper, DrugWelfareEnterpriseDO> implements DrugWelfareEnterpriseService {

    @Autowired
    private DrugWelfareGroupService drugWelfareGroupService;

    @Autowired
    private DrugWelfareService drugWelfareService;

    @Override
    public List<DrugWelfareEnterpriseDO> getByEid(Long eId) {
        QueryWrapper<DrugWelfareEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareEnterpriseDO::getEid, eId);
        return this.list(queryWrapper);
    }

    @Override
    public List<DrugWelfareEnterpriseDO> getByWelfareId(Long welfareId) {
        QueryWrapper<DrugWelfareEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareEnterpriseDO::getDrugWelfareId, welfareId);
        return this.list(queryWrapper);
    }

    @Override
    public List<DrugWelfareEnterpriseDO> getEnterpriseList() {
        QueryWrapper<DrugWelfareEnterpriseDO> queryWrapper = new QueryWrapper<>();
        return this.list(queryWrapper);
    }

    @Override
    public Page<DrugWelfareEnterpriseDO> pageList(DrugWelfareEnterprisePageRequest request) {
        LambdaQueryWrapper<DrugWelfareEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(request.getEid()), DrugWelfareEnterpriseDO::getEid, request.getEid());
        wrapper.eq(Objects.nonNull(request.getDrugWelfareId()), DrugWelfareEnterpriseDO::getDrugWelfareId, request.getDrugWelfareId());
        if (Objects.nonNull(request.getStartTime())){
            wrapper.ge(DrugWelfareEnterpriseDO::getCreateTime, DateUtil.beginOfDay(request.getStartTime()));
        }
        if (Objects.nonNull(request.getEndTime())){
            wrapper.le(DrugWelfareEnterpriseDO::getCreateTime, DateUtil.endOfDay(request.getEndTime()));
        }
        wrapper.orderByDesc(DrugWelfareEnterpriseDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public Boolean saveDrugWelfareEnterprise(SaveDrugWelfareEnterpriseRequest request) {
        List<DrugWelfareDO> drugWelfareList = drugWelfareService.getByIdList(new ArrayList<Long>() {{
            add(request.getDrugWelfareId());
        }});
        if (CollectionUtil.isEmpty(drugWelfareList)) {
            throw new BusinessException(ResultCode.FAILED, "福利计划不存在，请重新选择");
        }

        LambdaQueryWrapper<DrugWelfareEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrugWelfareEnterpriseDO::getEid, request.getEid()).eq(DrugWelfareEnterpriseDO::getDrugWelfareId, request.getDrugWelfareId());
        DrugWelfareEnterpriseDO one = this.getOne(wrapper);
        if (Objects.nonNull(one)) {
            throw new BusinessException(ResultCode.FAILED, "商家和福利计划已存在关系，请勿重复添加");
        }
        DrugWelfareEnterpriseDO map = PojoUtils.map(request, DrugWelfareEnterpriseDO.class);
        return this.save(map);
    }

    @Override
    public Boolean deleteDrugWelfareEnterprise(DeleteDrugWelfareEnterpriseRequest request) {
        DrugWelfareEnterpriseDO drugWelfareEnterpriseDO = this.getById(request.getId());
        if (Objects.isNull(drugWelfareEnterpriseDO)) {
            throw new BusinessException(ResultCode.FAILED, "未查询到关联关系");
        }
        Long drugWelfareId = drugWelfareEnterpriseDO.getDrugWelfareId();
        Long eid = drugWelfareEnterpriseDO.getEid();
        DrugWelfareGroupDO groupDO = drugWelfareGroupService.getValidWelfareByEidAndDrugWelfareId(eid, drugWelfareId);
        if (Objects.nonNull(groupDO)) {
            throw new BusinessException(ResultCode.FAILED, "已有用户入组，请勿删除");
        }
        drugWelfareEnterpriseDO.setUpdateUser(request.getOpUserId());
        drugWelfareEnterpriseDO.setUpdateTime(request.getOpTime());
        return this.deleteByIdWithFill(drugWelfareEnterpriseDO) > 0;
    }
}
