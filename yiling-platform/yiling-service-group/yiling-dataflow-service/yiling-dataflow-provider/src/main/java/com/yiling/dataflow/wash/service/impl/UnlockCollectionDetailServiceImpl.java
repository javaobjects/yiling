package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.wash.dao.UnlockCollectionDetailMapper;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCollectionDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCollectionDetailRequest;
import com.yiling.dataflow.wash.entity.UnlockCollectionDetailDO;
import com.yiling.dataflow.wash.entity.UnlockCollectionDetailRelationDO;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.UnlockCollectionDetailRelationService;
import com.yiling.dataflow.wash.service.UnlockCollectionDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 采集明细 服务实现类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
@Service
public class UnlockCollectionDetailServiceImpl extends BaseServiceImpl<UnlockCollectionDetailMapper, UnlockCollectionDetailDO> implements UnlockCollectionDetailService {

    @DubboReference
    private LocationApi locationApi;

    @Autowired
    private CrmGoodsInfoService crmGoodsInfoService;

    @Autowired
    private UnlockCollectionDetailRelationService unlockCollectionDetailRelationService;


    @Override
    public Page<UnlockCollectionDetailDO> listPage(QueryUnlockCollectionDetailPageRequest request) {
        Page<UnlockCollectionDetailDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<UnlockCollectionDetailDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getCrmGoodsCode() != null) {
            wrapper.eq(UnlockCollectionDetailDO::getCrmGoodsCode, request.getCrmGoodsCode());
        }
        if (request.getStartOpTime() != null) {
            wrapper.gt(UnlockCollectionDetailDO::getLastOpTime, DateUtil.beginOfDay(request.getStartOpTime()));
        }
        if (request.getEndOpTime() != null) {
            wrapper.le(UnlockCollectionDetailDO::getLastOpTime, DateUtil.endOfDay(request.getEndOpTime()));
        }
        wrapper.orderByDesc(UnlockCollectionDetailDO::getCreateTime);

        return baseMapper.selectPage(page, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SaveOrUpdateUnlockCollectionDetailRequest request) {
        //  校验是否存在重复的区域
        verifyUnlockCollectionDetailRelation(request);

        CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoService.getCrmGoodsInfoByCode(request.getCrmGoodsCode());
        if (crmGoodsInfoDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "该产品编码不存在");
        }

        UnlockCollectionDetailDO unlockCollectionDetailDO = PojoUtils.map(request, UnlockCollectionDetailDO.class);
        unlockCollectionDetailDO.setCrmGoodsCode(crmGoodsInfoDTO.getGoodsCode());
        unlockCollectionDetailDO.setCrmGoodsName(crmGoodsInfoDTO.getGoodsName());
        unlockCollectionDetailDO.setCrmGoodsSpec(crmGoodsInfoDTO.getGoodsSpec());
        unlockCollectionDetailDO.setCategoryId(crmGoodsInfoDTO.getCategoryId());
        unlockCollectionDetailDO.setVarietyType(crmGoodsInfoDTO.getVarietyType());
        unlockCollectionDetailDO.setGoodsGroup(crmGoodsInfoDTO.getGoodsGroup());
        unlockCollectionDetailDO.setStatus(crmGoodsInfoDTO.getStatus());
        unlockCollectionDetailDO.setLastOpUser(request.getOpUserId());
        unlockCollectionDetailDO.setLastOpTime(request.getOpTime());
        baseMapper.insert(unlockCollectionDetailDO);

        insertUnlockCollectionDetailRelation(unlockCollectionDetailDO.getId(), request);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(SaveOrUpdateUnlockCollectionDetailRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ResultCode.FAILED);
        }
        verifyUnlockCollectionDetailRelation(request);

        CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoService.getCrmGoodsInfoByCode(request.getCrmGoodsCode());
        if (crmGoodsInfoDTO == null) {
            throw new BusinessException(ResultCode.FAILED, "该产品编码不存在");
        }

        UnlockCollectionDetailDO unlockCollectionDetailDO = PojoUtils.map(request, UnlockCollectionDetailDO.class);
        unlockCollectionDetailDO.setCrmGoodsCode(crmGoodsInfoDTO.getGoodsCode());
        unlockCollectionDetailDO.setCrmGoodsName(crmGoodsInfoDTO.getGoodsName());
        unlockCollectionDetailDO.setCrmGoodsSpec(crmGoodsInfoDTO.getGoodsSpec());
        unlockCollectionDetailDO.setCategoryId(crmGoodsInfoDTO.getCategoryId());
        unlockCollectionDetailDO.setVarietyType(crmGoodsInfoDTO.getVarietyType());
        unlockCollectionDetailDO.setGoodsGroup(crmGoodsInfoDTO.getGoodsGroup());
        unlockCollectionDetailDO.setStatus(crmGoodsInfoDTO.getStatus());
        unlockCollectionDetailDO.setLastOpUser(request.getOpUserId());
        unlockCollectionDetailDO.setLastOpTime(request.getOpTime());
        baseMapper.updateById(unlockCollectionDetailDO);

        if (CollUtil.isNotEmpty(request.getRegionCodeList())) {
            unlockCollectionDetailRelationService.deleteByUnlockCollectionDetailId(request.getId());
            insertUnlockCollectionDetailRelation(unlockCollectionDetailDO.getId(), request);
        }

    }

    @Override
    public UnlockCollectionDetailDO getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<UnlockCollectionDetailDO> getListByCrmGoodsCode(Long crmGoodsCode) {
        LambdaQueryWrapper<UnlockCollectionDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCollectionDetailDO::getCrmGoodsCode, crmGoodsCode);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public UnlockCollectionDetailDO getByCrmGoodsCodeAndRegionCode(Long crmGoodsCode, String regionCode) {
        List<UnlockCollectionDetailDO> unlockCollectionDetailDOList = getListByCrmGoodsCode(crmGoodsCode);
        if (CollUtil.isEmpty(unlockCollectionDetailDOList)) {
            return null;
        }
        for (UnlockCollectionDetailDO unlockCollectionDetailDO : unlockCollectionDetailDOList) {
            List<UnlockCollectionDetailRelationDO> unlockCollectionDetailRelationDOList = unlockCollectionDetailRelationService.getListByUnlockCollectionDetailId(unlockCollectionDetailDO.getId());
            if (unlockCollectionDetailRelationDOList.stream().anyMatch(u -> u.getRegionCode().equals(regionCode))) {
                return unlockCollectionDetailDO;
            }
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        UnlockCollectionDetailDO unlockCollectionDetailDO = new UnlockCollectionDetailDO();
        unlockCollectionDetailDO.setId(id);
        this.deleteByIdWithFill(unlockCollectionDetailDO);
    }

    private void verifyUnlockCollectionDetailRelation(SaveOrUpdateUnlockCollectionDetailRequest request) {
        if (request.getId() != null && CollUtil.isEmpty(request.getRegionCodeList())) {
            return;     // 编辑时不传regionCode表示无变化。直接验证通过
        }
        if (CollUtil.isEmpty(request.getRegionCodeList())) {
            throw new BusinessException(ResultCode.FAILED, "未设置区域");
        }
        List<UnlockCollectionDetailDO> unlockCollectionDetailList = getListByCrmGoodsCode(request.getCrmGoodsCode());
        if (request.getId() != null) {
            unlockCollectionDetailList = unlockCollectionDetailList.stream().filter(u -> !u.getId().equals(request.getId())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(unlockCollectionDetailList)) {
            return;
        }
        for (UnlockCollectionDetailDO unlockCollectionDetailDO : unlockCollectionDetailList) {
            List<UnlockCollectionDetailRelationDO> unlockCollectionDetailRelationDOList = unlockCollectionDetailRelationService.getListByUnlockCollectionDetailId(unlockCollectionDetailDO.getId());
            if (unlockCollectionDetailRelationDOList == null) {
                continue;
            }
            for (UnlockCollectionDetailRelationDO relation : unlockCollectionDetailRelationDOList) {
                if (request.getRegionCodeList().stream().anyMatch(regionCode -> relation.getRegionCode().equals(regionCode))) {
                    throw new BusinessException(ResultCode.FAILED, "产品+区域不允许重复!");
                }
            }
        }
    }

    private void insertUnlockCollectionDetailRelation(Long unlockCollectionDetailId, SaveOrUpdateUnlockCollectionDetailRequest request) {
        List<RegionFullViewDTO> pcrInfoList = locationApi.getAllProvinceCityRegionList();

        List<UnlockCollectionDetailRelationDO> unlockAreaRecordRelationDOList = new ArrayList<>();
        List<String> regionCodeList = request.getRegionCodeList().stream().distinct().collect(Collectors.toList());
        for (String regionCode : regionCodeList) {
            RegionFullViewDTO regionFullViewDTO = pcrInfoList.stream()
                    .filter(pcrInfo -> regionCode.equals(pcrInfo.getRegionCode()))
                    .findAny().orElse(null);
            if (regionFullViewDTO == null) {
                continue;
            }

            UnlockCollectionDetailRelationDO unlockCollectionDetailRelation = new UnlockCollectionDetailRelationDO();
            unlockCollectionDetailRelation.setRegionCode(regionCode);
            unlockCollectionDetailRelation.setRegionName(regionFullViewDTO.getRegionName());
            unlockCollectionDetailRelation.setCityCode(regionFullViewDTO.getCityCode());
            unlockCollectionDetailRelation.setCityName(regionFullViewDTO.getCityName());
            unlockCollectionDetailRelation.setProvinceCode(regionFullViewDTO.getProvinceCode());
            unlockCollectionDetailRelation.setProvinceName(regionFullViewDTO.getProvinceName());
            unlockCollectionDetailRelation.setUnlockCollectionDetailId(unlockCollectionDetailId);
            unlockAreaRecordRelationDOList.add(unlockCollectionDetailRelation);
        }
        unlockCollectionDetailRelationService.saveBatch(unlockAreaRecordRelationDOList);
    }
}
