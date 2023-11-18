package com.yiling.dataflow.crm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupRelationDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsInfoDO;
import com.yiling.dataflow.crm.dao.CrmGoodsInfoMapper;
import com.yiling.dataflow.crm.enums.CrmGoodsErrorCode;
import com.yiling.dataflow.crm.enums.CrmGoodsStatusEnum;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-03
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "dataflow:CrmGoodsInfo")
public class CrmGoodsInfoServiceImpl extends BaseServiceImpl<CrmGoodsInfoMapper, CrmGoodsInfoDO> implements CrmGoodsInfoService {

    @Autowired
    private CrmGoodsGroupRelationService crmGoodsGroupRelationService;

    @Override
    @Cacheable("getCrmGoodsInfoAll")
    public List<CrmGoodsInfoDTO> getCrmGoodsInfoAll() {
        List<CrmGoodsInfoDTO> list = PojoUtils.map(this.list(), CrmGoodsInfoDTO.class);
        return list;
    }

    @Override
    @Cacheable(key = "#code+'+getCrmGoodsInfoByCode'")
    public CrmGoodsInfoDTO getCrmGoodsInfoByCode(Long code) {
        QueryWrapper<CrmGoodsInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsCode, code);
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), CrmGoodsInfoDTO.class);
    }

    @Override
    public CrmGoodsInfoDTO findByCodeAndName(Long goodsCode, String goodsName) {
        QueryWrapper<CrmGoodsInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsCode, goodsCode);
        if(StringUtils.isNotBlank(goodsName)){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsName, goodsName);
        }
        queryWrapper.lambda().eq(CrmGoodsInfoDO::getStatus, CrmGoodsStatusEnum.NORMAL.getCode());
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), CrmGoodsInfoDTO.class);
    }

    @Override
    public List<CrmGoodsInfoDTO> findByNameAndSpec(String goodsName,String goodsSpec,Integer limit) {
        if(StringUtils.isBlank(goodsName) && StringUtils.isBlank(goodsSpec)){
            return ListUtil.empty();
        }
        QueryWrapper<CrmGoodsInfoDO> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(goodsName)){
            queryWrapper.lambda().like(CrmGoodsInfoDO::getGoodsName, goodsName);
        }
        if(StringUtils.isNotBlank(goodsSpec)){
            queryWrapper.lambda().like(CrmGoodsInfoDO::getGoodsSpec, goodsSpec);
        }
        if(null!= limit && limit > 0){
            queryWrapper.last("limit "+limit);
        }
        queryWrapper.lambda().eq(CrmGoodsInfoDO::getStatus, CrmGoodsStatusEnum.NORMAL.getCode());
        return PojoUtils.map(this.list(queryWrapper), CrmGoodsInfoDTO.class);
    }

    @Override
    public CrmGoodsInfoDTO getByNameAndSpec(String goodsName, String goodsSpec, Integer goodsStatus) {
        if(StringUtils.isBlank(goodsName)){
            return null;
        }
        QueryWrapper<CrmGoodsInfoDO> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(goodsName)){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsName, goodsName);
        }
        if(StringUtils.isNotBlank(goodsSpec)){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsSpec, goodsSpec);
        }
        if(null!=goodsStatus){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getStatus, goodsStatus);
        }
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), CrmGoodsInfoDTO.class);
    }

    @Override
    public List<CrmGoodsInfoDTO> findByCodeList(List<Long> goodsCodeList) {
        QueryWrapper<CrmGoodsInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CrmGoodsInfoDO::getGoodsCode, goodsCodeList);
        return PojoUtils.map(this.list(queryWrapper), CrmGoodsInfoDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    @Cacheable(key = "#tableSuffix+'+getBakCrmGoodsInfoAll'")
    public List<CrmGoodsInfoDTO> getBakCrmGoodsInfoAll(String tableSuffix) {
        List<CrmGoodsInfoDTO> list = PojoUtils.map(this.list(), CrmGoodsInfoDTO.class);
        return list;
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmGoodsInfoDTO> findBakByCodeList(List<Long> goodsCodeList, String tableSuffix) {
        QueryWrapper<CrmGoodsInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CrmGoodsInfoDO::getGoodsCode, goodsCodeList);
        return PojoUtils.map(this.list(queryWrapper), CrmGoodsInfoDTO.class);
    }

    @Override
    public Long saveGoodsInfo(SaveOrUpdateCrmGoodsInfoRequest request) {
        log.info("保存crm商品参数：{}", JSONUtil.toJsonStr(request));
        CrmGoodsInfoDO goodsInfoDO = this.findByCode(request.getGoodsCode());
        if(null != goodsInfoDO){
            throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR,"商品编码重复");
        }
        goodsInfoDO = this.findByName(request.getGoodsName());
        if(null != goodsInfoDO){
            throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR,"商品名称重复");
        }
        CrmGoodsInfoDO saveDO = PojoUtils.map(request, CrmGoodsInfoDO.class);
        this.save(saveDO);
        return saveDO.getId();
    }

    @Override
    public Long editGoodsInfo(SaveOrUpdateCrmGoodsInfoRequest request) {
        log.info("编辑crm商品参数：{}", JSONUtil.toJsonStr(request));
        CrmGoodsInfoDO goodsInfoDO = this.getById(request.getId());
        if(null == goodsInfoDO){
            throw new BusinessException(CrmGoodsErrorCode.EMPTY_ERROR,"编辑的商品不存在");
        }
        goodsInfoDO = this.findByName(request.getGoodsName());
        if(null != goodsInfoDO && !goodsInfoDO.getGoodsName().equals(request.getGoodsName())){
            throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR,"商品名称已存在");
        }
        CrmGoodsInfoDO editDO = PojoUtils.map(request, CrmGoodsInfoDO.class);
        this.updateById(editDO);
        return request.getId();
    }

    @Override
    public Page<CrmGoodsInfoDTO> getPage(QueryCrmGoodsInfoPageRequest request) {
        QueryWrapper<CrmGoodsInfoDO> queryWrapper=new QueryWrapper();
        if(null!=request.getGoodsCode()){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsCode,request.getGoodsCode());
        }
        if(StringUtils.isNotBlank(request.getGoodsName())){
            queryWrapper.lambda().like(CrmGoodsInfoDO::getGoodsName,request.getGoodsName());
        }
        if(null!=request.getCategoryId() && request.getCategoryId()>0){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getCategoryId,request.getCategoryId());
        }
        if(null != request.getIsGroupPurchase()){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getIsGroupPurchase,request.getIsGroupPurchase());
        }
        if(null != request.getStatus()){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getStatus,request.getStatus());
        }
        queryWrapper.lambda().orderByDesc(CrmGoodsInfoDO::getId);
        Page<CrmGoodsInfoDO> page = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(page,CrmGoodsInfoDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public Page<CrmGoodsInfoDTO> getBakPage(QueryCrmGoodsInfoPageRequest request, String tableSuffix) {
        QueryWrapper<CrmGoodsInfoDO> queryWrapper=new QueryWrapper();
        if(null!=request.getGoodsCode()){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsCode,request.getGoodsCode());
        }
        if(StringUtils.isNotBlank(request.getGoodsName())){
            queryWrapper.lambda().like(CrmGoodsInfoDO::getGoodsName,request.getGoodsName());
        }
        if(null!=request.getCategoryId() && request.getCategoryId()>0){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getCategoryId,request.getCategoryId());
        }
        if(null != request.getIsGroupPurchase()){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getIsGroupPurchase,request.getIsGroupPurchase());
        }
        if(null != request.getStatus()){
            queryWrapper.lambda().eq(CrmGoodsInfoDO::getStatus,request.getStatus());
        }
        queryWrapper.lambda().orderByDesc(CrmGoodsInfoDO::getId);
        Page<CrmGoodsInfoDO> page = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(page,CrmGoodsInfoDTO.class);
    }

    @Override
    public Page<CrmGoodsInfoDTO> getPopupPage(QueryCrmGoodsInfoPageRequest request) {
        Page<CrmGoodsInfoDO> page = this.baseMapper.getPopupPage(request.getPage(),request);
        return PojoUtils.map(page,CrmGoodsInfoDTO.class);
    }

    @Override
    public Map<Long, List<CrmGoodsInfoDTO>> findByGroupIds(List<Long> groupIds) {
        HashMap<Long, List<CrmGoodsInfoDTO>> map = MapUtil.newHashMap();
        if(CollectionUtil.isEmpty(groupIds)){
            return map;
        }
        Map<Long, List<CrmGoodsGroupRelationDTO>> goodsGroupMap = crmGoodsGroupRelationService.findGoodsRelationByGroupIds(groupIds);
        if(CollectionUtil.isEmpty(goodsGroupMap)){
            return map;
        }
        List<Long> goodsIds = goodsGroupMap.values().stream().flatMap(Collection::stream).map(CrmGoodsGroupRelationDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<CrmGoodsInfoDO> goodsInfoDOList = this.listByIds(goodsIds);
        Map<Long, CrmGoodsInfoDO> goodsInfoDOMap = goodsInfoDOList.stream().collect(Collectors.toMap(CrmGoodsInfoDO::getId, Function.identity()));
        for(Map.Entry<Long, List<CrmGoodsGroupRelationDTO>> goodsGroup:goodsGroupMap.entrySet()){
            if(CollectionUtil.isNotEmpty(goodsGroup.getValue())){
                List<CrmGoodsInfoDTO> goodsList = ListUtil.toList();
                goodsGroup.getValue().forEach(goodsRelation->{
                    CrmGoodsInfoDO goodsInfoDO = goodsInfoDOMap.get(goodsRelation.getGoodsId());
                    goodsList.add(PojoUtils.map(goodsInfoDO,CrmGoodsInfoDTO.class));
                });
                map.put(goodsGroup.getKey(),goodsList);
            }
        }
        return map;
    }

    private CrmGoodsInfoDO findByName(String name){
        QueryWrapper<CrmGoodsInfoDO> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsName,name);
        queryWrapper.lambda().last("limit 1");
        return this.getBaseMapper().selectOne(queryWrapper);
    }

    private CrmGoodsInfoDO findByCode(Long code){
        QueryWrapper<CrmGoodsInfoDO> queryWrapper=new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsInfoDO::getGoodsCode,code);
        queryWrapper.lambda().last("limit 1");
        return this.getBaseMapper().selectOne(queryWrapper);
    }
}
