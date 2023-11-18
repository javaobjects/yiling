package com.yiling.goods.restriction.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.restriction.dao.GoodsPurchaseRestrictionCustomerMapper;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionCustomerDTO;
import com.yiling.goods.restriction.dto.request.DeleteRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.QueryRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.SaveRestrictionCustomerRequest;
import com.yiling.goods.restriction.entity.GoodsPurchaseRestrictionCustomerDO;
import com.yiling.goods.restriction.enums.GoodsRestrictionErrorCode;
import com.yiling.goods.restriction.service.GoodsPurchaseRestrictionCustomerService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionCustomerServiceImpl
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Slf4j
@Service
public class GoodsPurchaseRestrictionCustomerServiceImpl extends BaseServiceImpl<GoodsPurchaseRestrictionCustomerMapper, GoodsPurchaseRestrictionCustomerDO> implements GoodsPurchaseRestrictionCustomerService {
    public static final Integer CUSTOMER_LIMIT=2000;

    @Override
    public Boolean saveRestrictionCustomer(SaveRestrictionCustomerRequest request) {
        if(null==request.getGoodsId() || request.getGoodsId()==0
                || null==request.getCustomerEid() || request.getCustomerEid()==0){
            return false;
        }
        Long customerCount = this.baseMapper.getCustomerCountByGoodsId(request.getGoodsId());
        if(customerCount >= CUSTOMER_LIMIT){
            throw new BusinessException(GoodsRestrictionErrorCode.TOO_MANY_CUSTOMER);
        }
        QueryWrapper<GoodsPurchaseRestrictionCustomerDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionCustomerDO::getCustomerEid,request.getCustomerEid());
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionCustomerDO::getGoodsId,request.getGoodsId());
        queryWrapper.lambda().last(" limit 1");
        GoodsPurchaseRestrictionCustomerDO one = this.getOne(queryWrapper);
        if(null==one){
            GoodsPurchaseRestrictionCustomerDO customerDO = PojoUtils.map(request, GoodsPurchaseRestrictionCustomerDO.class);
            return this.save(customerDO);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSaveRestrictionCustomer(SaveRestrictionCustomerRequest request) {
        if(null==request.getGoodsId() || request.getGoodsId()==0){
            return false;
        }
        if(CollectionUtil.isEmpty(request.getCustomerEidList())){
            return true;
        }
        List<Long> restrictionCustomerEids = this.baseMapper.getCustomerEidByGoodsId(request.getGoodsId());
        List<Long> saveEidList;
        if(CollectionUtil.isNotEmpty(restrictionCustomerEids)){
            saveEidList = request.getCustomerEidList().stream().filter(eid -> !restrictionCustomerEids.contains(eid)).collect(Collectors.toList());
            //剩余可保存客户数量
            int saveSize=CUSTOMER_LIMIT-restrictionCustomerEids.size();
            //若当前数量大于剩余可保存客户数量 则截取剩余数量
            if(saveSize<saveEidList.size()){
                saveEidList = saveEidList.subList(0,saveSize);
            }
        }else {
            if (request.getCustomerEidList().size()>CUSTOMER_LIMIT){
                saveEidList=request.getCustomerEidList().subList(0,CUSTOMER_LIMIT);
            }else {
                saveEidList = request.getCustomerEidList();
            }
        }
        if(CollectionUtil.isNotEmpty(saveEidList)){
            request.setCustomerEidList(saveEidList);
            this.baseMapper.batchSaveRestrictionCustomer(request);
        }
        return true;
    }

    @Override
    public List<GoodsPurchaseRestrictionCustomerDTO> getCustomerByCustomerEidAndGoodsIds(Long customerEid, List<Long> goodsIds) {
        if(CollectionUtil.isEmpty(goodsIds)){
            return Lists.newArrayList();
        }
        QueryWrapper<GoodsPurchaseRestrictionCustomerDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionCustomerDO::getCustomerEid,customerEid);
        queryWrapper.lambda().in(GoodsPurchaseRestrictionCustomerDO::getGoodsId,goodsIds);
        return PojoUtils.map(this.list(queryWrapper),GoodsPurchaseRestrictionCustomerDTO.class);
    }

    @Override
    public List<Long> getCustomerEidByGoodsId(Long goodsId) {
        return this.baseMapper.getCustomerEidByGoodsId(goodsId);
    }

    @Override
    public List<GoodsPurchaseRestrictionCustomerDTO> getCustomerByGoodsIdAndCustomerEids(QueryRestrictionCustomerRequest request) {
        if(null==request.getGoodsId() || request.getGoodsId()==0){
            return Lists.newArrayList();
        }
        if(CollectionUtil.isEmpty(request.getCustomerEidList())){
            return Lists.newArrayList();
        }
        QueryWrapper<GoodsPurchaseRestrictionCustomerDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(GoodsPurchaseRestrictionCustomerDO::getCustomerEid,request.getCustomerEidList());
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionCustomerDO::getGoodsId,request.getGoodsId());
        return PojoUtils.map(this.list(queryWrapper),GoodsPurchaseRestrictionCustomerDTO.class);
    }

    @Override
    public int batchDeleteByCustomerEids(DeleteRestrictionCustomerRequest request) {
        if(null==request.getGoodsId() || request.getGoodsId()==0){
            return 0;
        }
        if(CollectionUtil.isEmpty(request.getCustomerEidList())){
            return 0;
        }
        QueryWrapper<GoodsPurchaseRestrictionCustomerDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(GoodsPurchaseRestrictionCustomerDO::getCustomerEid,request.getCustomerEidList());
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionCustomerDO::getGoodsId,request.getGoodsId());
        GoodsPurchaseRestrictionCustomerDO deleteDO = new GoodsPurchaseRestrictionCustomerDO();
        deleteDO.setOpUserId(request.getOpUserId());
        return this.batchDeleteWithFill(deleteDO,queryWrapper);
    }

    @Override
    public int deleteCustomer(DeleteRestrictionCustomerRequest request) {
        QueryWrapper<GoodsPurchaseRestrictionCustomerDO> wrapper = new QueryWrapper();
        wrapper.lambda().eq(GoodsPurchaseRestrictionCustomerDO::getCustomerEid,request.getCustomerEid());
        wrapper.lambda().eq(GoodsPurchaseRestrictionCustomerDO::getGoodsId,request.getGoodsId());
        wrapper.lambda().last(" limit 1");
        GoodsPurchaseRestrictionCustomerDO one = this.getOne(wrapper);
        if(null!=one){
            GoodsPurchaseRestrictionCustomerDO deleteDO = new GoodsPurchaseRestrictionCustomerDO();
            deleteDO.setId(one.getId());
            deleteDO.setOpUserId(request.getOpUserId());
            return this.deleteByIdWithFill(deleteDO);
        } else {
            return 0;
        }
    }
}
