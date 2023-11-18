package com.yiling.dataflow.crm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dao.CrmEnterpriseRelationPinchRunnerMapper;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationPinchRunnerPageListRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipPinchRunnerRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationPinchRunnerService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-04-19
 */
@Service
public class CrmEnterpriseRelationPinchRunnerServiceImpl extends BaseServiceImpl<CrmEnterpriseRelationPinchRunnerMapper, CrmEnterpriseRelationPinchRunnerDO> implements CrmEnterpriseRelationPinchRunnerService {

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public Page<CrmEnterpriseRelationPinchRunnerDO> page(QueryCrmEnterpriseRelationPinchRunnerPageListRequest request) {
        Page<CrmEnterpriseRelationPinchRunnerDO> page = new Page<>(request.getCurrent(), request.getSize());
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = request.getUserDatascopeBO();
        boolean datascopeFlag = isUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            return page;
        }
        return this.page(page, getListPageQueryWrapper(request));
    }

    @Override
    public CrmEnterpriseRelationPinchRunnerDO getById(SjmsUserDatascopeBO userDatascopeBO, Long id) {
        if (ObjectUtil.isNull(id) || 0 == id.intValue()) {
            return null;
        }
        // 数据权限
        boolean datascopeFlag = isUserDatascope(userDatascopeBO);
        if (!datascopeFlag) {
            return null;
        }
        LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        // 数据权限
        buildUserDatascopeCondition(lambdaQueryWrapper, userDatascopeBO);
        lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getId, id);
        return this.getOne(lambdaQueryWrapper);
    }


    @Override
    public CrmEnterpriseRelationPinchRunnerDO getByCrmEnterpriseIdAndCrmRelationShipId(Long crmEnterpriseId, Long crmRelationShipId) {
        Assert.notNull(crmEnterpriseId, "参数 crmEnterpriseId 不能为空");
        Assert.notNull(crmRelationShipId, "参数 crmRelationShipId 不能为空");
        LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getCrmEnterpriseId, crmEnterpriseId);
        lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, crmRelationShipId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmEnterpriseRelationPinchRunnerDO> getByCrmEnterpriseIdAndRelationShipIds(Long crmEnterpriseId, List<Long> crmRelationShipIds, String tableSuffix) {
        if (ObjectUtil.isNull(crmEnterpriseId) && CollUtil.isEmpty(crmRelationShipIds)) {
            throw new ServiceException("crmEnterpriseId、crmRelationShipIds, 不能同时为空");
        }
        LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (ObjectUtil.isNotNull(crmEnterpriseId)) {
            lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getCrmEnterpriseId, crmEnterpriseId);
        }
        if (CollUtil.isNotEmpty(crmRelationShipIds)) {
            lambdaQueryWrapper.in(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, crmRelationShipIds);
        }
        List<CrmEnterpriseRelationPinchRunnerDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            ListUtil.empty();
        }
        return list;
    }

    @Override
    public Long add(SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getCrmEnterpriseId(), "参数 crmEnterpriseId 不能为空");
        Assert.notNull(request.getCrmSupplyChainRole(), "参数 crmSupplyChainRole 不能为空");
        Assert.notNull(request.getCrmProvinceCode(), "参数 crmProvinceCode 不能为空");
        Assert.notNull(request.getEnterpriseCersId(), "参数 enterpriseCersId 不能为空");
        Assert.notNull(request.getBusinessSuperiorPostCode(), "参数 superiorPostCode 不能为空");
        request.setId(null);
        CrmEnterpriseRelationPinchRunnerDO entity = PojoUtils.map(request, CrmEnterpriseRelationPinchRunnerDO.class);
        boolean saveResult = this.save(entity);
        if (saveResult) {
            // 更新三者关系的是否代跑字段
            CrmEnterpriseRelationShipDO relationShipDO = new CrmEnterpriseRelationShipDO();
            relationShipDO.setId(request.getEnterpriseCersId());
            relationShipDO.setSubstituteRunning(2);
            relationShipDO.setOpUserId(request.getOpUserId());
            relationShipDO.setOpTime(request.getOpTime());
            crmEnterpriseRelationShipService.updateById(relationShipDO);
        }

        return entity.getId();
    }

    @Override
    public Boolean edit(SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        Assert.notNull(request.getCrmEnterpriseId(), "参数 crmEnterpriseId 不能为空");
        Assert.notNull(request.getCrmSupplyChainRole(), "参数 crmSupplyChainRole 不能为空");
        Assert.notNull(request.getCrmProvinceCode(), "参数 crmProvinceCode 不能为空");
        Assert.notNull(request.getEnterpriseCersId(), "参数 enterpriseCersId 不能为空");
        Assert.notNull(request.getBusinessSuperiorPostCode(), "参数 superiorPostCode 不能为空");
        CrmEnterpriseRelationPinchRunnerDO entity = PojoUtils.map(request, CrmEnterpriseRelationPinchRunnerDO.class);
        boolean updateResult = this.updateById(entity);
        // 更新三者关系的是否代跑字段
        Long newEnterpriseCersId = request.getEnterpriseCersId();
        Long oldEnterpriseCersId = request.getOldEnterpriseCersId();
        boolean newFlag = (ObjectUtil.isNotNull(newEnterpriseCersId) && 0 != newEnterpriseCersId.intValue()) ? true : false;
        boolean oldFlag = (ObjectUtil.isNotNull(oldEnterpriseCersId) && 0 != oldEnterpriseCersId.intValue()) ? true : false;
        if (updateResult && newFlag && oldFlag && !newEnterpriseCersId.equals(oldEnterpriseCersId)) {
            List<CrmEnterpriseRelationShipDO> updaterelationShipList = new ArrayList<>();
            // 旧的解除关联
            CrmEnterpriseRelationShipDO relationShipCancel = new CrmEnterpriseRelationShipDO();
            relationShipCancel.setId(oldEnterpriseCersId);
            relationShipCancel.setSubstituteRunning(1);
            relationShipCancel.setOpUserId(request.getOpUserId());
            relationShipCancel.setOpTime(request.getOpTime());
            updaterelationShipList.add(relationShipCancel);
            // 新的绑定关联
            CrmEnterpriseRelationShipDO relationShipAdd = new CrmEnterpriseRelationShipDO();
            relationShipAdd.setId(request.getEnterpriseCersId());
            relationShipAdd.setSubstituteRunning(2);
            relationShipAdd.setOpUserId(request.getOpUserId());
            relationShipAdd.setOpTime(request.getOpTime());
            updaterelationShipList.add(relationShipAdd);
            return crmEnterpriseRelationShipService.updateBatchById(updaterelationShipList);
        }
        return updateResult;
    }

    @Override
    public Boolean remove(RemoveCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getId, request.getId());
        CrmEnterpriseRelationPinchRunnerDO one = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(one)) {
            return true;
        }

        CrmEnterpriseRelationPinchRunnerDO entity = new CrmEnterpriseRelationPinchRunnerDO();
        entity.setId(request.getId());
        entity.setOpUserId(request.getOpUserId());
        entity.setOpTime(request.getOpTime());
        boolean deleteResult = this.deleteByIdWithFill(entity) > 0;
        // 更新三者关系的是否代跑字段
        if (deleteResult) {
            CrmEnterpriseRelationShipDO relationShipCancel = new CrmEnterpriseRelationShipDO();
            relationShipCancel.setId(one.getEnterpriseCersId());
            relationShipCancel.setSubstituteRunning(1);
            relationShipCancel.setOpUserId(request.getOpUserId());
            relationShipCancel.setOpTime(request.getOpTime());
            return crmEnterpriseRelationShipService.updateById(relationShipCancel);
        }
        return false;
    }

    @Override
    public Integer removeByEnterpriseCersId(RemoveCrmEnterpriseRelationPinchRunnerRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notEmpty(request.getEnterpriseCersIdList(), "参数 enterpriseCersIdList 不能为空");
        Assert.notNull(request.getOpUserId(), "参数 opUserId 不能为空");

        LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmEnterpriseRelationPinchRunnerDO::getEnterpriseCersId, request.getEnterpriseCersIdList());

        CrmEnterpriseRelationPinchRunnerDO entity = new CrmEnterpriseRelationPinchRunnerDO();
        entity.setOpUserId(request.getOpUserId());
        entity.setOpTime(new Date());

        return this.batchDeleteWithFill(entity, queryWrapper);
    }

    @Override
    public List<Long> getBusinessSuperiorPostCode() {
        return this.baseMapper.getBusinessSuperiorPostCode();
    }

    @Override
    public boolean updateBackUpBatchByBusinessSuperiorPostCode(UpdateCrmEnterpriseRelationShipPinchRunnerRequest request, String tableName) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notBlank(tableName, "参数 tableName 不能为空");
        return this.baseMapper.updateBackUpBatchByBusinessSuperiorPostCode(request, tableName);
    }

    private LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> getListPageQueryWrapper(QueryCrmEnterpriseRelationPinchRunnerPageListRequest request) {
        LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        // 数据权限
        SjmsUserDatascopeBO userDatascopeBO = request.getUserDatascopeBO();
        buildUserDatascopeCondition(lambdaQueryWrapper, userDatascopeBO);
        // 销量计入主管工号
        String businessSuperiorCode = request.getBusinessSuperiorCode();
        if (StrUtil.isNotBlank(businessSuperiorCode)) {
            lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getBusinessSuperiorCode, businessSuperiorCode);
        }

        // 岗位代码
        String postCode = request.getPostCode();
        if (StrUtil.isNotBlank(postCode)) {
            lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getBusinessSuperiorPostCode, postCode);
        }
        // 机构id
        Long crmEnterpriseId = request.getCrmEnterpriseId();
        if (ObjectUtil.isNotNull(crmEnterpriseId) && 0 != crmEnterpriseId.intValue()) {
            lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getCrmEnterpriseId, crmEnterpriseId);
        }
        // 机构id列表
        List<Long> crmEnterpriseIdList = request.getCrmEnterpriseIdList();
        if (CollUtil.isNotEmpty(crmEnterpriseIdList)) {
            lambdaQueryWrapper.in(CrmEnterpriseRelationPinchRunnerDO::getCrmEnterpriseId, crmEnterpriseIdList);
        }
        // 供应链角色
        Integer crmSupplyChainRole = request.getCrmSupplyChainRole();
        if (ObjectUtil.isNotNull(crmSupplyChainRole) && 0 != crmSupplyChainRole.intValue()) {
            lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getCrmSupplyChainRole, crmSupplyChainRole);
        }
        // 品种
        Long categoryId = request.getCategoryId();
        if (ObjectUtil.isNotNull(categoryId) && 0 != categoryId.intValue()) {
            lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getCategoryId, categoryId);
        }
        // 辖区
        Long manorId = request.getManorId();
        if (ObjectUtil.isNotNull(manorId) && 0 != manorId.intValue()) {
            lambdaQueryWrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getManorId, manorId);
        }

        // 操作时间：更新时间 或 创建时间
        // 1、更新时间 = '1970-01-01 00:00:00' and 创建时间 >= ${start} and 创建时间 <= ${end}
        // 2、更新时间 > '1970-01-01 00:00:00' and 更新时间 >= ${start} and 更新时间 <= ${end}
        if (ObjectUtil.isNotNull(request.getOpTimeStart()) && ObjectUtil.isNotNull(request.getOpTimeEnd())) {
            Date opTimeStart = DateUtil.beginOfDay(request.getOpTimeStart());
            Date opTimeEnd = DateUtil.endOfDay(request.getOpTimeEnd());
            lambdaQueryWrapper.and((wrapper) -> {
                wrapper.eq(CrmEnterpriseRelationPinchRunnerDO::getUpdateTime, DateUtil.parse("1970-01-01 00:00:00"))
                        .ge(CrmEnterpriseRelationPinchRunnerDO::getCreateTime, opTimeStart).le(CrmEnterpriseRelationPinchRunnerDO::getCreateTime, opTimeEnd)
                        .or()
                        .gt(CrmEnterpriseRelationPinchRunnerDO::getUpdateTime, DateUtil.parse("1970-01-01 00:00:00"))
                        .ge(CrmEnterpriseRelationPinchRunnerDO::getUpdateTime, opTimeStart).le(CrmEnterpriseRelationPinchRunnerDO::getUpdateTime, opTimeEnd);
            });
        }
        lambdaQueryWrapper.orderByDesc(CrmEnterpriseRelationPinchRunnerDO::getCreateTime);
        return lambdaQueryWrapper;
    }

    private void buildUserDatascopeCondition(LambdaQueryWrapper<CrmEnterpriseRelationPinchRunnerDO> lambdaQueryWrapper, SjmsUserDatascopeBO userDatascopeBO) {
        if (ObjectUtil.isNotNull(userDatascopeBO) && OrgDatascopeEnum.PORTION.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
            SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = userDatascopeBO.getOrgPartDatascopeBO();
            List<Long> crmEids = orgPartDatascopeBO.getCrmEids();
            List<String> provinceCodes = orgPartDatascopeBO.getProvinceCodes();
            boolean crmIdListFlag = CollUtil.isNotEmpty(crmEids) ? true : false;
            boolean crmProvinceCodeListFlag = CollUtil.isNotEmpty(provinceCodes) ? true : false;
            if (crmIdListFlag && crmProvinceCodeListFlag){
                lambdaQueryWrapper.and((wrapper) -> {
                    wrapper.in(CrmEnterpriseRelationPinchRunnerDO::getCrmEnterpriseId, crmEids)
                            .or()
                            .in(CrmEnterpriseRelationPinchRunnerDO::getCrmProvinceCode, provinceCodes);
                });
            } else if (crmIdListFlag){
                lambdaQueryWrapper.in(CrmEnterpriseRelationPinchRunnerDO::getCrmEnterpriseId, crmEids);
            } else if (crmProvinceCodeListFlag){
                lambdaQueryWrapper.in(CrmEnterpriseRelationPinchRunnerDO::getCrmProvinceCode, provinceCodes);
            }
        }
    }

    private boolean isUserDatascope(SjmsUserDatascopeBO userDatascopeBO){
        // 数据权限
        if (ObjectUtil.isNotNull(userDatascopeBO) && ObjectUtil.isNotNull(userDatascopeBO.getOrgDatascope())) {
            if (OrgDatascopeEnum.NONE.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
                return false;
            }
            if (OrgDatascopeEnum.PORTION.equals(OrgDatascopeEnum.getFromCode(userDatascopeBO.getOrgDatascope()))) {
                SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = userDatascopeBO.getOrgPartDatascopeBO();
                List<Long> crmEids = orgPartDatascopeBO.getCrmEids();
                List<String> provinceCodes = orgPartDatascopeBO.getProvinceCodes();
                if (CollUtil.isEmpty(crmEids) && CollUtil.isEmpty(provinceCodes)) {
                    return false;
                }
            }
        }
        return true;
    }
}
