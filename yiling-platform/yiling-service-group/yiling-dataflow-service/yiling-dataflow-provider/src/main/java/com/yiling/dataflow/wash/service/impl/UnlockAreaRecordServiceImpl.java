package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockAreaRecordPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockAreaRecordRequest;
import com.yiling.dataflow.wash.entity.UnlockAreaRecordDO;
import com.yiling.dataflow.wash.dao.UnlockAreaRecordMapper;
import com.yiling.dataflow.wash.entity.UnlockAreaRecordRelationDO;
import com.yiling.dataflow.wash.service.UnlockAreaRecordRelationService;
import com.yiling.dataflow.wash.service.UnlockAreaRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 区域备案 服务实现类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
@Service
public class UnlockAreaRecordServiceImpl extends BaseServiceImpl<UnlockAreaRecordMapper, UnlockAreaRecordDO> implements UnlockAreaRecordService {

    @Autowired
    private UnlockAreaRecordRelationService unlockAreaRecordRelationService;

    @DubboReference
    private LocationApi locationApi;

    @Override
    public Page<UnlockAreaRecordDO> listPage(QueryUnlockAreaRecordPageRequest request) {
        Page<UnlockAreaRecordDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<UnlockAreaRecordDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getCustomerClassification() != null) {
            wrapper.eq(UnlockAreaRecordDO::getCustomerClassification, request.getCustomerClassification());
        }
        if (request.getCategoryId() != null) {
            wrapper.eq(UnlockAreaRecordDO::getCategoryId, request.getCategoryId());
        }
        if (StringUtils.isNotEmpty(request.getRepresentativeCode())) {
            wrapper.eq(UnlockAreaRecordDO::getRepresentativeCode, request.getRepresentativeCode());
        }
        if (StringUtils.isNotEmpty(request.getExecutiveCode())) {
            wrapper.eq(UnlockAreaRecordDO::getExecutiveCode, request.getExecutiveCode());
        }
        if (request.getStartOpTime() != null) {
            wrapper.ge(UnlockAreaRecordDO::getLastOpTime, DateUtil.beginOfDay(request.getStartOpTime()));
        }
        if (request.getEndOpTime() != null) {
            wrapper.le(UnlockAreaRecordDO::getLastOpTime, DateUtil.endOfDay(request.getEndOpTime()));
        }
        wrapper.orderByDesc(UnlockAreaRecordDO::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SaveOrUpdateUnlockAreaRecordRequest request) {
        //  检查是否存在重复的区域
        verifyUnlockAreaRecordRelation(request);

        UnlockAreaRecordDO unlockAreaRecordDO = PojoUtils.map(request, UnlockAreaRecordDO.class);
        unlockAreaRecordDO.setLastOpTime(unlockAreaRecordDO.getOpTime());
        unlockAreaRecordDO.setLastOpUser(unlockAreaRecordDO.getOpUserId());
        baseMapper.insert(unlockAreaRecordDO);

        insertUnlockAreaRecordRelationDOList(unlockAreaRecordDO, request.getRegionCodeList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(SaveOrUpdateUnlockAreaRecordRequest request) {
        //  检查是否存在重复的区域
        verifyUnlockAreaRecordRelation(request);

        UnlockAreaRecordDO unlockAreaRecordDO = PojoUtils.map(request, UnlockAreaRecordDO.class);
        if (unlockAreaRecordDO.getType() == 1) {    // 销量计入主管，将代表相关信息置空
            unlockAreaRecordDO.setRepresentativeCode("");
            unlockAreaRecordDO.setRepresentativeName("");
            unlockAreaRecordDO.setRepresentativePostCode("");
            unlockAreaRecordDO.setRepresentativePostName("");
        }

        unlockAreaRecordDO.setLastOpTime(unlockAreaRecordDO.getOpTime());
        unlockAreaRecordDO.setLastOpUser(unlockAreaRecordDO.getOpUserId());
        baseMapper.updateById(unlockAreaRecordDO);

        if (CollUtil.isNotEmpty(request.getRegionCodeList())) {
            // 删除关联的省市区
            unlockAreaRecordRelationService.deleteByUnlockAreaRecordId(request.getId());
            insertUnlockAreaRecordRelationDOList(unlockAreaRecordDO, request.getRegionCodeList());
        }

    }

    @Override
    public UnlockAreaRecordDO getById(Long id) {
        return baseMapper.selectById(id);
    }


    private void insertUnlockAreaRecordRelationDOList(UnlockAreaRecordDO unlockAreaRecordDO, List<String> regionCodeList) {
        List<UnlockAreaRecordRelationDO> unlockAreaRecordRelationDOList = new ArrayList<>();
        //   获取省市区
        List<RegionFullViewDTO> pcrInfoList = locationApi.getAllProvinceCityRegionList();

        regionCodeList = regionCodeList.stream().distinct().collect(Collectors.toList());
        for (String regionCode : regionCodeList) {
            RegionFullViewDTO regionFullViewDTO = pcrInfoList.stream()
                    .filter(pcrInfo -> regionCode.equals(pcrInfo.getRegionCode()))
                    .findAny().orElse(null);
            if (regionFullViewDTO == null) {
                continue;
            }

            UnlockAreaRecordRelationDO unlockAreaRecordRelationDO = new UnlockAreaRecordRelationDO();
            unlockAreaRecordRelationDO.setRegionCode(regionCode);
            unlockAreaRecordRelationDO.setRegionName(regionFullViewDTO.getRegionName());
            unlockAreaRecordRelationDO.setCityCode(regionFullViewDTO.getCityCode());
            unlockAreaRecordRelationDO.setCityName(regionFullViewDTO.getCityName());
            unlockAreaRecordRelationDO.setProvinceCode(regionFullViewDTO.getProvinceCode());
            unlockAreaRecordRelationDO.setProvinceName(regionFullViewDTO.getProvinceName());
            if (unlockAreaRecordDO.getType() == 1) {
                unlockAreaRecordRelationDO.setPostName(unlockAreaRecordDO.getExecutivePostName());
                unlockAreaRecordRelationDO.setEmpName(unlockAreaRecordDO.getExecutiveName());
            } else if (unlockAreaRecordDO.getType() == 2) {
                unlockAreaRecordRelationDO.setPostName(unlockAreaRecordDO.getRepresentativePostName());
                unlockAreaRecordRelationDO.setEmpName(unlockAreaRecordDO.getRepresentativeName());
            }
            unlockAreaRecordRelationDO.setUnlockAreaRecordId(unlockAreaRecordDO.getId());
            unlockAreaRecordRelationDOList.add(unlockAreaRecordRelationDO);
        }

        unlockAreaRecordRelationService.saveBatch(unlockAreaRecordRelationDOList);
    }

    @Override
    public List<UnlockAreaRecordDO> getListByClassAndCategory(Integer customerClassification, Long categoryId) {
        LambdaQueryWrapper<UnlockAreaRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockAreaRecordDO::getCustomerClassification, customerClassification);
        wrapper.eq(UnlockAreaRecordDO::getCategoryId, categoryId);

        return baseMapper.selectList(wrapper);
    }

    @Override
    public UnlockAreaRecordDO getByClassAndCategoryIdAndRegionCode(Integer customerClassification, Long categoryId, String regionCode) {
        List<UnlockAreaRecordDO> unlockAreaRecordDOList = getListByClassAndCategory(customerClassification, categoryId);
        if (CollUtil.isEmpty(unlockAreaRecordDOList)) {
            return null;
        }
        for (UnlockAreaRecordDO unlockAreaRecordDO : unlockAreaRecordDOList) {
            List<UnlockAreaRecordRelationDO> unlockAreaRecordRelationDOList = unlockAreaRecordRelationService.getByUnlockAreaRecordId(unlockAreaRecordDO.getId());
            if (unlockAreaRecordRelationDOList.stream().anyMatch(u -> u.getRegionCode().equals(regionCode))) {
                return unlockAreaRecordDO;
            }
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        UnlockAreaRecordDO unlockAreaRecordDO = new UnlockAreaRecordDO();
        unlockAreaRecordDO.setId(id);
        this.deleteByIdWithFill(unlockAreaRecordDO);
    }

    private void verifyUnlockAreaRecordRelation(SaveOrUpdateUnlockAreaRecordRequest request) {
        if (request.getId() != null && CollUtil.isEmpty(request.getRegionCodeList())) {
            return;     // 编辑时不传regionCode表示无变化。直接验证通过
        }
        if (CollUtil.isEmpty(request.getRegionCodeList())) {
            throw new BusinessException(ResultCode.FAILED, "未设置区域");
        }
        List<UnlockAreaRecordDO> unlockAreaRecordDOList = getListByClassAndCategory(request.getCustomerClassification(), request.getCategoryId());
        if (request.getId() != null) {
            // 如果是修改，则排除当前id项
            unlockAreaRecordDOList = unlockAreaRecordDOList.stream().filter(u -> !u.getId().equals(request.getId())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(unlockAreaRecordDOList)) {
            return;
        }
        for (UnlockAreaRecordDO unlockAreaRecordDO : unlockAreaRecordDOList) {
            List<UnlockAreaRecordRelationDO> unlockAreaRecordRelationDOList = unlockAreaRecordRelationService.getByUnlockAreaRecordId(unlockAreaRecordDO.getId());
            if (unlockAreaRecordRelationDOList == null) {
                continue;
            }
            for (UnlockAreaRecordRelationDO unlockAreaRecordRelationDO : unlockAreaRecordRelationDOList) {
                if (request.getRegionCodeList().stream().anyMatch(regionCode -> unlockAreaRecordRelationDO.getRegionCode().equals(regionCode))) {
                    throw new BusinessException(ResultCode.FAILED, "非锁客户分类+品种+区域不允许重复!");
                }
            }
        }
    }

}
