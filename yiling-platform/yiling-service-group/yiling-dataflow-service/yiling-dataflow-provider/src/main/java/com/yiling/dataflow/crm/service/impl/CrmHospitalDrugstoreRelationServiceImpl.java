package com.yiling.dataflow.crm.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmHosDruRelOrgIdBO;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelActiveRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelEffectiveListRequest;
import com.yiling.dataflow.crm.dto.request.QueryHospitalDrugstoreRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.RemoveHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.entity.CrmHospitalDrugstoreRelationDO;
import com.yiling.dataflow.crm.dao.CrmHospitalDrugstoreRelationMapper;
import com.yiling.dataflow.crm.enums.CrmDrugstoreRelStatusEnum;
import com.yiling.dataflow.crm.service.CrmHospitalDrugstoreRelationService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 院外药店关系绑定表 服务实现类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-30
 */
@Service
public class CrmHospitalDrugstoreRelationServiceImpl extends BaseServiceImpl<CrmHospitalDrugstoreRelationMapper, CrmHospitalDrugstoreRelationDO> implements CrmHospitalDrugstoreRelationService {

    @Override
    public Page<CrmHospitalDrugstoreRelationDO> listPage(QueryHospitalDrugstoreRelationPageRequest request) {
        Page<CrmHospitalDrugstoreRelationDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<CrmHospitalDrugstoreRelationDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getDrugstoreOrgId() != null) {
            wrapper.eq(CrmHospitalDrugstoreRelationDO::getDrugstoreOrgId, request.getDrugstoreOrgId());
        }
        if (request.getHospitalOrgId() != null) {
            wrapper.eq(CrmHospitalDrugstoreRelationDO::getHospitalOrgId, request.getHospitalOrgId());
        }
        if (request.getCategoryId() != null) {
            wrapper.eq(CrmHospitalDrugstoreRelationDO::getCategoryId, request.getCategoryId());
        }
        if (StringUtils.isNotEmpty(request.getCategoryName())) {
            wrapper.eq(CrmHospitalDrugstoreRelationDO::getCategoryName, request.getCategoryName());
        }
        if (request.getCrmGoodsCode() != null) {
            wrapper.eq(CrmHospitalDrugstoreRelationDO::getCrmGoodsCode, request.getCrmGoodsCode());
        }
        if (request.getStartOpTime() != null) {
            wrapper.ge(CrmHospitalDrugstoreRelationDO::getLastOpTime, DateUtil.beginOfDay(request.getStartOpTime()));
        }
        if (request.getEndOpTime() != null) {
            wrapper.le(CrmHospitalDrugstoreRelationDO::getLastOpTime, DateUtil.endOfDay(request.getEndOpTime()));
        }
        if (request.getStatus() != null) {
            if (CrmDrugstoreRelStatusEnum.DISABLE.getCode().equals(request.getStatus())) {  // 停用
                wrapper.eq(CrmHospitalDrugstoreRelationDO::getDisableFlag, 1);
            } else {
                wrapper.eq(CrmHospitalDrugstoreRelationDO::getDisableFlag, 0);
                Date now = DateUtil.beginOfDay(new Date());
                if (CrmDrugstoreRelStatusEnum.NOT_EFFECT.getCode().equals(request.getStatus())) {
                    wrapper.gt(CrmHospitalDrugstoreRelationDO::getEffectStartTime, now);
                } else if (CrmDrugstoreRelStatusEnum.EFFECTING.getCode().equals(request.getStatus())) {
                    wrapper.le(CrmHospitalDrugstoreRelationDO::getEffectStartTime, now);
                    wrapper.ge(CrmHospitalDrugstoreRelationDO::getEffectEndTime, now);
                } else if (CrmDrugstoreRelStatusEnum.EXPIRED.getCode().equals(request.getStatus())) {
                    wrapper.lt(CrmHospitalDrugstoreRelationDO::getEffectEndTime, now);
                }
            }
        }
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveOrUpdate(SaveOrUpdateCrmHospitalDrugstoreRelRequest request) {
        //  验证医院绑定药店数量是否已超过3个
        verifyDrugstoreCount(request);

        //  验证药店的同一品种，只能绑定一家医院
        verifyUnique(request);

        CrmHospitalDrugstoreRelationDO entity = PojoUtils.map(request, CrmHospitalDrugstoreRelationDO.class);
        this.saveOrUpdate(entity);
    }

    @Override
    public List<CrmHosDruRelOrgIdBO> listGroupByHospitalIdAndDrugstoreId() {
        return baseMapper.listGroupByHospitalIdAndDrugstoreId();
    }

    @Override
    public List<Long> selectDrugOrgIdByHosOrgId(Long hospitalOrgId) {
        return baseMapper.selectDrugOrgIdByHosOrgId(hospitalOrgId);
    }

    @Override
    public void delete(RemoveHospitalDrugstoreRelRequest request) {
        CrmHospitalDrugstoreRelationDO entity = new CrmHospitalDrugstoreRelationDO();
        entity.setUpdateTime(request.getOpTime());
        entity.setUpdateUser(request.getOpUserId());
        entity.setId(request.getId());
        baseMapper.deleteByIdWithFill(entity);
    }

    @Override
    public void disable(SaveOrUpdateCrmHospitalDrugstoreRelRequest request) {
        CrmHospitalDrugstoreRelationDO entity = new CrmHospitalDrugstoreRelationDO();
        entity.setId(request.getId());
        entity.setDisableFlag(1);
        entity.setLastOpTime(request.getOpTime());
        entity.setLastOpUser(request.getOpUserId());
        entity.setUpdateTime(request.getOpTime());
        entity.setUpdateUser(request.getOpUserId());
        baseMapper.updateById(entity);
    }

    @Override
    public List<CrmHospitalDrugstoreRelationDO> getEffectiveList(QueryCrmHosDruRelEffectiveListRequest request) {
        LambdaQueryWrapper<CrmHospitalDrugstoreRelationDO> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(CrmHospitalDrugstoreRelationDO::getDrugstoreOrgId, request.getDrugstoreOrgId());
        wrapper.eq(CrmHospitalDrugstoreRelationDO::getCrmGoodsCode, request.getCrmGoodsCode());

        wrapper.eq(CrmHospitalDrugstoreRelationDO::getDisableFlag, 0);
        wrapper.ge(CrmHospitalDrugstoreRelationDO::getEffectEndTime, DateUtil.beginOfDay(DateUtil.date()));
        List<CrmHospitalDrugstoreRelationDO> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public CrmHospitalDrugstoreRelationDO getActiveDataByDrugstoreIdAndGoodsCode(QueryCrmHosDruRelActiveRequest request) {
        LambdaQueryWrapper<CrmHospitalDrugstoreRelationDO> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(CrmHospitalDrugstoreRelationDO::getDrugstoreOrgId, request.getDrugstoreOrgId());
        wrapper.eq(CrmHospitalDrugstoreRelationDO::getCrmGoodsCode, request.getCrmGoodsCode());

        wrapper.eq(CrmHospitalDrugstoreRelationDO::getDisableFlag, 0);
        wrapper.le(CrmHospitalDrugstoreRelationDO::getEffectStartTime, DateUtil.beginOfDay(DateUtil.date()));
        wrapper.ge(CrmHospitalDrugstoreRelationDO::getEffectEndTime, DateUtil.beginOfDay(DateUtil.date()));
        List<CrmHospitalDrugstoreRelationDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    private void verifyUnique(SaveOrUpdateCrmHospitalDrugstoreRelRequest request) {
        QueryCrmHosDruRelEffectiveListRequest queryRequest = PojoUtils.map(request, QueryCrmHosDruRelEffectiveListRequest.class);
        List<CrmHospitalDrugstoreRelationDO> list = getEffectiveList(queryRequest);
        if (request.getId() != null && request.getId() != 0) {
            // 若为更新，则去掉该条（此处目前不会有更新操作）
            list = list.stream().filter(e -> !e.getId().equals(request.getId())).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(chdr -> chdr.getHospitalOrgId().equals(request.getHospitalOrgId()))) {
                throw new BusinessException(ResultCode.FAILED, "有效数据内，医院已绑定该药店的药品，请勿重复绑定！");
            } else  {
                // TODO DIH-370 确认一家药店的的不同品是否可以和其他医院绑定
                throw new BusinessException(ResultCode.FAILED, "一家药店的同一个药品，只能跟一个医院进行绑定!");
            }
        }
    }

    private void verifyDrugstoreCount(SaveOrUpdateCrmHospitalDrugstoreRelRequest request) {
        List<Long> drugstoreIdList = this.selectDrugOrgIdByHosOrgId(request.getHospitalOrgId());
        if (CollUtil.isNotEmpty(drugstoreIdList) && drugstoreIdList.size() >= 3) {
            if (!drugstoreIdList.contains(request.getDrugstoreOrgId())) {     // 是否为已绑定的药店，如果是已绑定过的，则可以继续绑定
                throw new BusinessException(ResultCode.FAILED, "该医院已超过绑定上限！");
            }
        }
    }

}
