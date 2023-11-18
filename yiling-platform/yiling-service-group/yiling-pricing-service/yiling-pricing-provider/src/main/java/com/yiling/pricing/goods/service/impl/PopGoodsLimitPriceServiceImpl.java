package com.yiling.pricing.goods.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.pricing.goods.dao.PopGoodsLimitPriceMapper;
import com.yiling.pricing.goods.dto.PopGoodsLimitPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryPopLimitPriceRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdatePopLimitPriceRequest;
import com.yiling.pricing.goods.entity.PopGoodsLimitPriceDO;
import com.yiling.pricing.goods.service.PopGoodsLimitPriceService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 PopGoodsLimitPriceServiceImpl
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@Service
@Slf4j
public class PopGoodsLimitPriceServiceImpl extends BaseServiceImpl<PopGoodsLimitPriceMapper, PopGoodsLimitPriceDO> implements PopGoodsLimitPriceService {
    @Override
    public Long saveOrUpdate(SaveOrUpdatePopLimitPriceRequest request) {
        Assert.notNull(request.getSellSpecificationsId(), "保存或更新：规格id不能为空！");
        Assert.notNull(request.getStandardId(), "保存或更新：标准库id不能为空！");
        if(null==request.getId() || request.getId()==0){
            PopGoodsLimitPriceDTO limitPriceDTO = this.getLimitPriceBySpecificationsId(request.getSellSpecificationsId(), null);
            if(null!=limitPriceDTO){
                request.setId(limitPriceDTO.getId());
            }else {
                PopGoodsLimitPriceDO limitPriceDO = PojoUtils.map(request, PopGoodsLimitPriceDO.class);
                this.save(limitPriceDO);
                return limitPriceDO.getId();
            }
        }
        PopGoodsLimitPriceDO limitDO = new PopGoodsLimitPriceDO();
        limitDO.setId(request.getId());
        limitDO.setUpperLimitPrice(request.getUpperLimitPrice());
        limitDO.setLowerLimitPrice(request.getLowerLimitPrice());
        limitDO.setStatus(request.getStatus());
        limitDO.setOpUserId(request.getOpUserId());
        this.updateById(limitDO);
        return request.getId();
    }

    @Override
    public PopGoodsLimitPriceDTO getLimitPriceBySpecificationsId(Long specificationsId, Integer status) {
        LambdaQueryWrapper<PopGoodsLimitPriceDO> queryWrapper = new QueryWrapper<PopGoodsLimitPriceDO>().lambda();
        queryWrapper.eq(PopGoodsLimitPriceDO::getSellSpecificationsId,specificationsId);
        if(null!=status){
            queryWrapper.eq(PopGoodsLimitPriceDO::getStatus,status);
        }
        queryWrapper.last(" limit 1");
        return PojoUtils.map(this.getOne(queryWrapper), PopGoodsLimitPriceDTO.class);
    }

    @Override
    public List<PopGoodsLimitPriceDTO> batchQueryLimitPrice(QueryPopLimitPriceRequest request) {
        if(CollectionUtil.isEmpty(request.getSellSpecificationsIds())){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<PopGoodsLimitPriceDO> queryWrapper = new QueryWrapper<PopGoodsLimitPriceDO>().lambda();
        queryWrapper.in(PopGoodsLimitPriceDO::getSellSpecificationsId,request.getSellSpecificationsIds());
        if(null!=request.getStatus()){
            queryWrapper.eq(PopGoodsLimitPriceDO::getStatus,request.getStatus());
        }
        return PojoUtils.map(this.list(queryWrapper), PopGoodsLimitPriceDTO.class);
    }
}
