package com.yiling.goods.restriction.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.restriction.dao.GoodsPurchaseRestrictionMapper;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionCustomerDTO;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.goods.restriction.dto.request.SavePurchaseRestrictionRequest;
import com.yiling.goods.restriction.entity.GoodsPurchaseRestrictionDO;
import com.yiling.goods.restriction.enums.CustomerSettingTypeEnum;
import com.yiling.goods.restriction.enums.GoodsRestrictionErrorCode;
import com.yiling.goods.restriction.enums.GoodsRestrictionStatusEnum;
import com.yiling.goods.restriction.enums.RestrictionTimeTypeEnum;
import com.yiling.goods.restriction.service.GoodsPurchaseRestrictionCustomerService;
import com.yiling.goods.restriction.service.GoodsPurchaseRestrictionService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionServiceImpl
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Slf4j
@Service
public class GoodsPurchaseRestrictionServiceImpl extends BaseServiceImpl<GoodsPurchaseRestrictionMapper, GoodsPurchaseRestrictionDO> implements GoodsPurchaseRestrictionService {
    @Autowired
    private GoodsPurchaseRestrictionCustomerService goodsPurchaseRestrictionCustomerService;

    @Override
    public GoodsPurchaseRestrictionDTO getRestrictionByGoodsId(Long goodsId) {
        QueryWrapper<GoodsPurchaseRestrictionDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionDO::getGoodsId,goodsId);
        return PojoUtils.map(this.getOne(queryWrapper),GoodsPurchaseRestrictionDTO.class);
    }

    @Override
    public Boolean saveGoodsPurchaseRestriction(SavePurchaseRestrictionRequest request) {
        if(null==request.getGoodsId() || request.getGoodsId()==0){
            throw new BusinessException(GoodsRestrictionErrorCode.GOODS_EXIST,"绑定的商品为空");
        }
        if(null==request.getTimeType() || request.getTimeType()==0){
            throw new BusinessException(GoodsRestrictionErrorCode.TIME_TYPE_ERROR);
        }
        if(RestrictionTimeTypeEnum.CUSTOM.getType().equals(request.getTimeType())){
            if(null == request.getStartTime() || null == request.getEndTime()){
                throw new BusinessException(GoodsRestrictionErrorCode.DATE_EXIST,"自定义限购开始时间或者结束时间为空");
            }
        }
        GoodsPurchaseRestrictionDO restrictionDO = PojoUtils.map(request, GoodsPurchaseRestrictionDO.class);
        if(null!=restrictionDO.getId() && restrictionDO.getId()>0){
            this.updateById(restrictionDO);
        }else {
            GoodsPurchaseRestrictionDTO restrictionDTO = this.getRestrictionByGoodsId(request.getGoodsId());
            if(null!=restrictionDTO){
                restrictionDO.setId(restrictionDTO.getId());
                this.updateById(restrictionDO);
            }else {
                this.save(restrictionDO);
            }
        }
        return true;
    }

    @Override
    public List<GoodsPurchaseRestrictionDTO> queryValidPurchaseRestriction(QueryGoodsPurchaseRestrictionRequest request) {
        if(null==request.getCustomerEid() || request.getCustomerEid()==0){
            throw new BusinessException(GoodsRestrictionErrorCode.CUSTOMER_EXIST,"查询客户为空");
        }
        List<GoodsPurchaseRestrictionDTO> list= Lists.newArrayList();
        if(CollectionUtil.isEmpty(request.getGoodsIdList())){
            return list;
        }
        //获取商品id 对应的限购规则
        QueryWrapper<GoodsPurchaseRestrictionDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(GoodsPurchaseRestrictionDO::getGoodsId,request.getGoodsIdList());
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionDO::getStatus, GoodsRestrictionStatusEnum.NORMAL.getCode());
        List<GoodsPurchaseRestrictionDO> restrictionDOList = this.list(queryWrapper);
        //获取属于部分客户限购的商品id
        List<Long> someGoodsIds = restrictionDOList.stream().filter(restriction -> CustomerSettingTypeEnum.SOME.getType().equals(restriction.getCustomerSettingType())).map(GoodsPurchaseRestrictionDO::getGoodsId).collect(Collectors.toList());
        //获取用户存于部分客户限购的客户名单内的商品id
        List<GoodsPurchaseRestrictionCustomerDTO> customerRestrictionList = goodsPurchaseRestrictionCustomerService.getCustomerByCustomerEidAndGoodsIds(request.getCustomerEid(), someGoodsIds);
        List<Long> customerRestrictionGoodsIds = customerRestrictionList.stream().map(GoodsPurchaseRestrictionCustomerDTO::getGoodsId).distinct().collect(Collectors.toList());
        //someGoodsIds 移除存在该客户的商品后，剩下的都不要限购
        someGoodsIds.removeAll(customerRestrictionGoodsIds);
        long now = System.currentTimeMillis();
        restrictionDOList.forEach(restriction->{
            //排除部分客户限购中不属于该客户的商品
            if(!someGoodsIds.contains(restriction.getGoodsId())){
                //排除自定义时间不在时间范围内的限购
                if(RestrictionTimeTypeEnum.CUSTOM.getType().equals(restriction.getTimeType())){
                    if(now > restriction.getStartTime().getTime() && now < restriction.getEndTime().getTime()){
                        GoodsPurchaseRestrictionDTO restrictionDTO = PojoUtils.map(restriction, GoodsPurchaseRestrictionDTO.class);
                        list.add(restrictionDTO);
                    }
                }else {
                    GoodsPurchaseRestrictionDTO restrictionDTO = PojoUtils.map(restriction, GoodsPurchaseRestrictionDTO.class);
                    list.add(restrictionDTO);
                }
            }
        });
        return list;
    }

    @Override
    public GoodsPurchaseRestrictionDTO getValidPurchaseRestriction(QueryGoodsPurchaseRestrictionRequest request) {
        if(null == request.getCustomerEid() || request.getCustomerEid()==0){
            return null;
        }
        QueryWrapper<GoodsPurchaseRestrictionDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionDO::getGoodsId,request.getGoodsId());
        queryWrapper.lambda().eq(GoodsPurchaseRestrictionDO::getStatus, GoodsRestrictionStatusEnum.NORMAL.getCode());
        queryWrapper.lambda().last(" limit 1");
        GoodsPurchaseRestrictionDTO restrictionDTO = PojoUtils.map(this.getOne(queryWrapper), GoodsPurchaseRestrictionDTO.class);
        if(null != restrictionDTO){
            if(RestrictionTimeTypeEnum.CUSTOM.getType().equals(restrictionDTO.getTimeType())){
                long now = System.currentTimeMillis();
                if(now < restrictionDTO.getStartTime().getTime() || now > restrictionDTO.getEndTime().getTime()){
                    return null;
                }
            }
            if(CustomerSettingTypeEnum.SOME.getType().equals(restrictionDTO.getCustomerSettingType())){
                List<GoodsPurchaseRestrictionCustomerDTO> customerList = goodsPurchaseRestrictionCustomerService.getCustomerByCustomerEidAndGoodsIds(request.getCustomerEid(), Lists.newArrayList(request.getGoodsId()));
                if(CollectionUtil.isEmpty(customerList)){
                    return null;
                }
            }
        }
        return restrictionDTO;
    }

    @Override
    public List<GoodsPurchaseRestrictionDTO> getPurchaseRestrictionByGoodsIds(List<Long> goodsIds) {
        if(CollectionUtil.isEmpty(goodsIds)){
            return Lists.newArrayList();
        }
        QueryWrapper<GoodsPurchaseRestrictionDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(GoodsPurchaseRestrictionDO::getGoodsId,goodsIds);
        return PojoUtils.map(this.list(queryWrapper),GoodsPurchaseRestrictionDTO.class);
    }
}
