package com.yiling.goods.ylprice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.ylprice.dao.GoodsYilingPriceMapper;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.AddReportPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryGoodsYilingPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryReportPricePageRequest;
import com.yiling.goods.ylprice.dto.request.UpdateReportPriceRequest;
import com.yiling.goods.ylprice.entity.GoodsYilingPriceDO;
import com.yiling.goods.ylprice.service.GoodsYilingPriceService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/8/8
 */
@Slf4j
@Service
public class GoodsYilingPriceServiceImpl extends BaseServiceImpl<GoodsYilingPriceMapper, GoodsYilingPriceDO> implements GoodsYilingPriceService {


    @Override
    public GoodsYilingPriceDO add(AddReportPriceRequest addReportPriceRequest) {
        addReportPriceRequest.setEndTime(DateUtil.endOfDay(addReportPriceRequest.getEndTime()));
        LambdaQueryWrapper<GoodsYilingPriceDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GoodsYilingPriceDO::getGoodsId, addReportPriceRequest.getGoodsId()).eq(GoodsYilingPriceDO::getSpecificationId, addReportPriceRequest.getSpecificationId())
                .eq(GoodsYilingPriceDO::getParamId, addReportPriceRequest.getParamId()).le(GoodsYilingPriceDO::getStartTime, addReportPriceRequest.getEndTime())
                .ge(GoodsYilingPriceDO::getEndTime, addReportPriceRequest.getStartTime()).last("limit 1");
        GoodsYilingPriceDO priceDO = this.getOne(wrapper);
        if (Objects.nonNull(priceDO)) {
            throw new BusinessException(ResultCode.FAILED, "该时间段已存在此类型商品价格");
        }
        GoodsYilingPriceDO reportPriceDO = new GoodsYilingPriceDO();
        PojoUtils.map(addReportPriceRequest, reportPriceDO);
        this.save(reportPriceDO);
        return reportPriceDO;
    }

    @Override
    public Page<GoodsYilingPriceDTO> listReportPricePage(QueryReportPricePageRequest queryReportPricePageRequest) {
        LambdaQueryWrapper<GoodsYilingPriceDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GoodsYilingPriceDO::getParamId, queryReportPricePageRequest.getParamId());
        wrapper.like(StringUtils.isNotEmpty(queryReportPricePageRequest.getGoodsName()), GoodsYilingPriceDO::getGoodsName, queryReportPricePageRequest.getGoodsName());
        wrapper.le(Objects.nonNull(queryReportPricePageRequest.getStartTime()), GoodsYilingPriceDO::getUpdateTime, queryReportPricePageRequest.getStartTime());
        wrapper.orderByDesc(GoodsYilingPriceDO::getId);
        if (!Objects.isNull(queryReportPricePageRequest.getEndTime())) {
            wrapper.ge(GoodsYilingPriceDO::getUpdateTime, DateUtil.endOfDay(queryReportPricePageRequest.getEndTime()));
        }
        wrapper.in(CollUtil.isNotEmpty(queryReportPricePageRequest.getUserIds()), GoodsYilingPriceDO::getCreateUser, queryReportPricePageRequest.getUserIds());
        Page<GoodsYilingPriceDO> page = this.page(queryReportPricePageRequest.getPage(), wrapper);
        return PojoUtils.map(page, GoodsYilingPriceDTO.class);
    }

    @Override
    public GoodsYilingPriceDO updateReportPrice(UpdateReportPriceRequest updateReportPriceRequest) {
        updateReportPriceRequest.setEndTime(DateUtil.endOfDay(updateReportPriceRequest.getEndTime()));

        GoodsYilingPriceDO priceDO = this.getById(updateReportPriceRequest.getId());
        //当前类型价格时间校验
        LambdaQueryWrapper<GoodsYilingPriceDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GoodsYilingPriceDO::getGoodsId, priceDO.getGoodsId()).eq(GoodsYilingPriceDO::getSpecificationId, priceDO.getSpecificationId())
                .eq(GoodsYilingPriceDO::getParamId, priceDO.getParamId()).ne(GoodsYilingPriceDO::getId, updateReportPriceRequest.getId())
                .le(GoodsYilingPriceDO::getStartTime, updateReportPriceRequest.getEndTime()).ge(GoodsYilingPriceDO::getEndTime, priceDO.getStartTime()).last("limit 1");
        GoodsYilingPriceDO other = this.getOne(wrapper);
        if (Objects.nonNull(other)) {
            throw new BusinessException(ResultCode.FAILED, "该时间段已存在此类型商品价格");
        }
        GoodsYilingPriceDO reportPriceDO = new GoodsYilingPriceDO();
        PojoUtils.map(updateReportPriceRequest, reportPriceDO);
        this.updateById(reportPriceDO);
        return this.getById(updateReportPriceRequest.getId());
    }

    @Override
    public List<GoodsYilingPriceDO> listByGoodsIdAndDate(List<Long> goodsIds, Date date) {
        LambdaQueryWrapper<GoodsYilingPriceDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(GoodsYilingPriceDO:: getGoodsId,goodsIds)
                .le(GoodsYilingPriceDO:: getStartTime,date)
                .ge(GoodsYilingPriceDO:: getEndTime,date);
        return list(wrapper);
    }

    @Override
    public List<GoodsYilingPriceDO> listBySpecificationIdAndDate(QueryGoodsYilingPriceRequest request) {
        if(ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getSpecificationIdList()) || CollUtil.isEmpty(request.getTimeList())){
            return ListUtil.empty();
        }
        List<GoodsYilingPriceDO> goodsYilingPriceList = this.baseMapper.listBySpecificationIdList(request.getSpecificationIdList());
        if(CollUtil.isEmpty(goodsYilingPriceList)){
            return ListUtil.empty();
        }
        List<GoodsYilingPriceDO> list = new ArrayList<>();
        for(Date time : request.getTimeList()){
            for(GoodsYilingPriceDO goodsYilingPrice : goodsYilingPriceList){
                if(time.getTime() >= goodsYilingPrice.getStartTime().getTime() && time.getTime() <= goodsYilingPrice.getEndTime().getTime()){
                    list.add(goodsYilingPrice);
                }
            }
        }
        return list;
    }

    @Override
    public List<GoodsYilingPriceDO> getPriceBySpecificationIdList(List<Long> specificationIdList, Long paramId, Date date) {
        return baseMapper.getPriceBySpecificationIdList(specificationIdList, paramId, date);
    }


}
