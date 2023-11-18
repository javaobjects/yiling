package com.yiling.user.control.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.control.dao.GoodsControlMapper;
import com.yiling.user.control.dto.GoodsControlDTO;
import com.yiling.user.control.dto.request.BatchSaveCustomerControlRequest;
import com.yiling.user.control.dto.request.DeleteGoodsControlRequest;
import com.yiling.user.control.dto.request.QueryCustomerControlPageRequest;
import com.yiling.user.control.dto.request.SaveCustomerControlRequest;
import com.yiling.user.control.dto.request.SaveRegionControlRequest;
import com.yiling.user.control.entity.GoodsControlConditionDO;
import com.yiling.user.control.entity.GoodsControlDO;
import com.yiling.user.control.enums.ControlTypeEnum;
import com.yiling.user.control.service.GoodsControlConditionService;
import com.yiling.user.control.service.GoodsControlService;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterpriseService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 商品控销表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-21
 */
@Service
public class GoodsControlServiceImpl extends BaseServiceImpl<GoodsControlMapper, GoodsControlDO> implements GoodsControlService {

    @Autowired
    private GoodsControlConditionService goodsControlConditionService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EnterpriseCustomerService enterpriseCustomerService;

    @Override
    public List<Long> getCustomerType(Long goodsId) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlDO::getGoodsId, goodsId)
                .eq(GoodsControlDO::getControlType, ControlTypeEnum.CUSTOMER_TYPE.getCode());
        GoodsControlDO goodsControlDO = this.getOne(queryWrapper);
        return goodsControlConditionService.getValueIdByControlId(goodsControlDO.getId());
    }

    @Override
    public List<Long> getRegionId(Long goodsId) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlDO::getGoodsId, goodsId)
                .eq(GoodsControlDO::getControlType, ControlTypeEnum.REGION.getCode());
        GoodsControlDO goodsControlDO = this.getOne(queryWrapper);
        return goodsControlConditionService.getValueIdByControlId(goodsControlDO.getId());
    }

    @Override
    public List<Long> getCustomerId(Long goodsId) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlDO::getGoodsId, goodsId)
                .eq(GoodsControlDO::getControlType, ControlTypeEnum.CUSTOMER.getCode());
        GoodsControlDO goodsControlDO = this.getOne(queryWrapper);
        return goodsControlConditionService.getValueIdByControlId(goodsControlDO.getId());
    }

    @Override
    public GoodsControlDTO getCustomerTypeInfo(Long goodsId, Long eid) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlDO::getGoodsId, goodsId)
                .eq(GoodsControlDO::getControlType, ControlTypeEnum.CUSTOMER_TYPE.getCode())
                .eq(GoodsControlDO::getEid, eid);
        GoodsControlDTO goodsControlDTO = PojoUtils.map(this.getOne(queryWrapper), GoodsControlDTO.class);
        if (goodsControlDTO == null) {
            return null;
        }
        goodsControlDTO.setConditionValue(goodsControlConditionService.getValueIdByControlId(goodsControlDTO.getId()));
        return goodsControlDTO;
    }

    @Override
    public GoodsControlDTO getRegionInfo(Long goodsId, Long eid) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlDO::getGoodsId, goodsId)
                .eq(GoodsControlDO::getControlType, ControlTypeEnum.REGION.getCode())
                .eq(GoodsControlDO::getEid, eid);
        GoodsControlDTO goodsControlDTO = PojoUtils.map(this.getOne(queryWrapper), GoodsControlDTO.class);
        if (goodsControlDTO == null) {
            return null;
        }
        goodsControlDTO.setConditionValue(goodsControlConditionService.getValueIdByControlId(goodsControlDTO.getId()));
        return goodsControlDTO;
    }

    @Override
    public GoodsControlDTO getCustomerInfo(Long goodsId, Long eid) {
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlDO::getGoodsId, goodsId)
                .eq(GoodsControlDO::getControlType, ControlTypeEnum.CUSTOMER.getCode())
                .eq(GoodsControlDO::getEid, eid);
        GoodsControlDTO goodsControlDTO = PojoUtils.map(this.getOne(queryWrapper), GoodsControlDTO.class);
        if (goodsControlDTO == null) {
            return null;
        }
        goodsControlDTO.setConditionValue(goodsControlConditionService.getValueIdByControlId(goodsControlDTO.getId()));
        return goodsControlDTO;
    }

    @Override
    public Page<Long> getPageCustomerInfo(QueryCustomerControlPageRequest request) {
        return this.baseMapper.getPageCustomerInfo(new Page<>(request.getCurrent(), request.getSize()),request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRegion(SaveRegionControlRequest request) {
        Long typeId = 0L;
        GoodsControlDTO customerControllDTO = this.getCustomerTypeInfo(request.getGoodsId(), request.getEid());
        if (customerControllDTO == null) {
            GoodsControlDO customerControlDO = new GoodsControlDO();
            customerControlDO.setControlType(ControlTypeEnum.CUSTOMER_TYPE.getCode());
            customerControlDO.setEid(request.getEid());
            customerControlDO.setSetType(request.getCustomerTypeSet());
            customerControlDO.setGoodsId(request.getGoodsId());
            customerControlDO.setControlDescribe(request.getControlDescribe());
            customerControlDO.setOpUserId(request.getOpUserId());
            this.save(customerControlDO);
            typeId = customerControlDO.getId();
        } else {
            typeId = customerControllDTO.getId();
            GoodsControlDO customerControlDO = new GoodsControlDO();
            customerControlDO.setId(typeId);
            customerControlDO.setControlType(ControlTypeEnum.CUSTOMER_TYPE.getCode());
            customerControlDO.setEid(request.getEid());
            customerControlDO.setSetType(request.getCustomerTypeSet());
            customerControlDO.setControlDescribe(request.getControlDescribe());
            customerControlDO.setGoodsId(request.getGoodsId());
            customerControlDO.setOpUserId(request.getOpUserId());
            this.updateById(customerControlDO);
        }
        if (CollUtil.isNotEmpty(request.getCustomerTypes())) {
            goodsControlConditionService.deleteControlCondition(typeId,request.getOpUserId());
            List<GoodsControlConditionDO> list = new ArrayList<>();
            Long finalId = typeId;
            request.getCustomerTypes().forEach(e -> {
                GoodsControlConditionDO goodsControlConditionDO = new GoodsControlConditionDO();
                goodsControlConditionDO.setConditionValue(e);
                goodsControlConditionDO.setControlId(finalId);
                goodsControlConditionDO.setOpUserId(request.getOpUserId());
                list.add(goodsControlConditionDO);
            });
            goodsControlConditionService.saveBatch(list);
        }

        Long regionId = 0L;
        GoodsControlDTO regionControlDTO = this.getRegionInfo(request.getGoodsId(), request.getEid());
        if (regionControlDTO == null) {
            GoodsControlDO regionControlDO = new GoodsControlDO();
            regionControlDO.setControlType(ControlTypeEnum.REGION.getCode());
            regionControlDO.setEid(request.getEid());
            regionControlDO.setGoodsId(request.getGoodsId());
            regionControlDO.setSetType(request.getRegionSet());
            regionControlDO.setControlDescribe(request.getControlDescribe());
            regionControlDO.setOpUserId(request.getOpUserId());
            this.save(regionControlDO);
            regionId = regionControlDO.getId();
        } else {
            regionId = regionControlDTO.getId();
            GoodsControlDO regionControlDO = new GoodsControlDO();
            regionControlDO.setId(regionId);
            regionControlDO.setControlType(ControlTypeEnum.REGION.getCode());
            regionControlDO.setEid(request.getEid());
            regionControlDO.setSetType(request.getRegionSet());
            regionControlDO.setGoodsId(request.getGoodsId());
            regionControlDO.setControlDescribe(request.getControlDescribe());
            regionControlDO.setOpUserId(request.getOpUserId());
            this.updateById(regionControlDO);
        }
        if (CollUtil.isNotEmpty(request.getRegionIds())) {
            goodsControlConditionService.deleteControlCondition(regionId,request.getOpUserId());
            List<GoodsControlConditionDO> list = new ArrayList<>();
            Long finalId = regionId;
            request.getRegionIds().forEach(e -> {
                GoodsControlConditionDO goodsControlConditionDO = new GoodsControlConditionDO();
                goodsControlConditionDO.setConditionValue(e);
                goodsControlConditionDO.setControlId(finalId);
                goodsControlConditionDO.setOpUserId(request.getOpUserId());
                list.add(goodsControlConditionDO);
            });
            goodsControlConditionService.saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveCustomer(SaveCustomerControlRequest request) {
        Long id = 0L;
        GoodsControlDTO goodsControlDTO = this.getCustomerInfo(request.getGoodsId(), request.getEid());
        if (goodsControlDTO == null) {
            GoodsControlDO goodsControlDO = new GoodsControlDO();
            goodsControlDO.setControlType(ControlTypeEnum.CUSTOMER.getCode());
            goodsControlDO.setEid(request.getEid());
            goodsControlDO.setSetType(request.getCustomerSet());
            goodsControlDO.setGoodsId(request.getGoodsId());
            goodsControlDO.setOpUserId(request.getOpUserId());
            this.save(goodsControlDO);
            id = goodsControlDO.getId();
        } else {
            id = goodsControlDTO.getId();
            GoodsControlDO goodsControlDO = new GoodsControlDO();
            goodsControlDO.setId(id);
            goodsControlDO.setControlType(ControlTypeEnum.CUSTOMER.getCode());
            goodsControlDO.setEid(request.getEid());
            goodsControlDO.setSetType(request.getCustomerSet());
            goodsControlDO.setGoodsId(request.getGoodsId());
            goodsControlDO.setOpUserId(request.getOpUserId());
            this.updateById(goodsControlDO);
        }
        if (CollUtil.isNotEmpty(request.getCustomerIds())) {
            List<Long> ids = goodsControlConditionService.getValueIdByControlId(id);
            List<GoodsControlConditionDO> list = new ArrayList<>();
            Long finalId = id;
            request.getCustomerIds().forEach(e -> {
                if (!ids.contains(e)) {
                    GoodsControlConditionDO goodsControlConditionDO = new GoodsControlConditionDO();
                    goodsControlConditionDO.setConditionValue(e);
                    goodsControlConditionDO.setControlId(finalId);
                    goodsControlConditionDO.setOpUserId(request.getOpUserId());
                    list.add(goodsControlConditionDO);
                }
            });
            goodsControlConditionService.saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSaveCustomer(BatchSaveCustomerControlRequest controlRequest) {
        QueryCustomerPageListRequest request=PojoUtils.map(controlRequest,QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.list(false, controlRequest.getEid()));
        List<Long> customerIds = new ArrayList<>();
        Page<EnterpriseCustomerDO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = enterpriseCustomerService.pageList(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            customerIds.addAll(page.getRecords().stream().map(e -> e.getCustomerEid()).collect(Collectors.toList()));
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        SaveCustomerControlRequest saveCustomerControlRequest = new SaveCustomerControlRequest();
        saveCustomerControlRequest.setCustomerSet(controlRequest.getCustomerSet());
        saveCustomerControlRequest.setId(controlRequest.getId());
        saveCustomerControlRequest.setEid(controlRequest.getEid());
        saveCustomerControlRequest.setCustomerIds(customerIds);
        saveCustomerControlRequest.setGoodsId(controlRequest.getGoodsId());
        return saveCustomer(saveCustomerControlRequest);
    }

    @Override
    public Boolean deleteCustomer(DeleteGoodsControlRequest request) {
        return goodsControlConditionService.deleteCustomer(request);
    }

    @Override
    public Map<Long, Integer> getGoodsControlByBuyerEidAndGid(List<Long> goodsIds, Long eid, Long buyerEid) {
        if (CollUtil.isEmpty(goodsIds)) {
            return Collections.emptyMap();
        }

        EnterpriseDO enterpriseDO = enterpriseService.getById(buyerEid);

        Map<Long, Integer> returnMap = new HashMap<>();
        goodsIds.forEach(e -> {
            returnMap.put(e, 0);
        });
        //查询所有商品的控销信息
        QueryWrapper<GoodsControlDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsControlDO::getGoodsId, goodsIds)
                .eq(GoodsControlDO::getSetType,1)
                .eq(GoodsControlDO::getEid, eid);
        List<GoodsControlDO> goodsControlDOList = this.list(queryWrapper);
        if (CollUtil.isEmpty(goodsControlDOList)) {
            return returnMap;
        }

        List<Long> controlIds = goodsControlDOList.stream().map(e -> e.getId()).collect(Collectors.toList());
        Map<Long, List<GoodsControlConditionDO>> goodsControlMap = goodsControlConditionService.getValueIdByControlIds(controlIds);
        if (CollUtil.isEmpty(goodsControlMap)) {
            return returnMap;
        }

        goodsIds.forEach(e -> {
            List<GoodsControlDO> customerGoodsControlList = goodsControlDOList.stream().filter(f -> f.getControlType().equals(ControlTypeEnum.CUSTOMER.getCode()) && f.getGoodsId().equals(e)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(customerGoodsControlList)) {
                List<GoodsControlConditionDO> goodsControlConditionList = goodsControlMap.get(customerGoodsControlList.get(0).getId());
                if(CollUtil.isNotEmpty(goodsControlConditionList)){
                    List<Long> customerEids = goodsControlConditionList.stream().map(g -> g.getConditionValue()).collect(Collectors.toList());
                    if (customerEids.contains(buyerEid)) {
                        returnMap.put(e, 1);
                    }
                }
            }

            List<GoodsControlDO> customerTypeGoodsControlList = goodsControlDOList.stream().filter(f -> f.getControlType().equals(ControlTypeEnum.CUSTOMER_TYPE.getCode()) && f.getGoodsId().equals(e)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(customerTypeGoodsControlList)) {
                List<GoodsControlConditionDO> goodsControlConditionList = goodsControlMap.get(customerTypeGoodsControlList.get(0).getId());
                if(CollUtil.isNotEmpty(goodsControlConditionList)) {
                    List<Long> types = goodsControlConditionList.stream().map(g -> g.getConditionValue()).collect(Collectors.toList());
                    if (types.contains(enterpriseDO.getType().longValue())) {
                        returnMap.put(e, 1);
                    }
                }
            }

            List<GoodsControlDO> regionGoodsControlList = goodsControlDOList.stream().filter(f -> f.getControlType().equals(ControlTypeEnum.REGION.getCode()) && f.getGoodsId().equals(e)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(regionGoodsControlList)) {
                List<GoodsControlConditionDO> goodsControlConditionList = goodsControlMap.get(regionGoodsControlList.get(0).getId());
                if(CollUtil.isNotEmpty(goodsControlConditionList)) {
                    List<Long> regions = goodsControlConditionList.stream().map(g -> g.getConditionValue()).collect(Collectors.toList());
                    if (regions.contains(Long.parseLong(enterpriseDO.getRegionCode()))) {
                        returnMap.put(e, 1);
                    }
                }
            }
        });
        return returnMap;
    }

}
