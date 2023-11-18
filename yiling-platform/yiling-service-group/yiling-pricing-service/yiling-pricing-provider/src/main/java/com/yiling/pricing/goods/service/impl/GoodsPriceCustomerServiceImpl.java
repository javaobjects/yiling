package com.yiling.pricing.goods.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dao.GoodsPriceCustomerMapper;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsLinePriceRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerDO;
import com.yiling.pricing.goods.service.GoodsPriceCustomerService;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 客户定价 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Service
public class GoodsPriceCustomerServiceImpl extends BaseServiceImpl<GoodsPriceCustomerMapper, GoodsPriceCustomerDO> implements GoodsPriceCustomerService {

    @Override
    public Page<GoodsPriceCustomerDO> pageList(QueryGoodsPriceCustomerPageListRequest request) {
        LambdaQueryWrapper<GoodsPriceCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getCustomerEid() != null && request.getCustomerEid() != 0) {
            queryWrapper.eq(GoodsPriceCustomerDO::getCustomerEid, request.getCustomerEid());
        }
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(GoodsPriceCustomerDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getGoodsIds())) {
            queryWrapper.in(GoodsPriceCustomerDO::getGoodsId, request.getGoodsIds());
        }
        queryWrapper.eq(GoodsPriceCustomerDO::getGoodsLine,request.getGoodsLine());
        return this.baseMapper.selectPage(request.getPage(), queryWrapper);
    }

    @Override
    public Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerRequest request) {
        // 先查询是否存在
        Assert.notNull(request.getEid(), "保存或更新：企业ID不能为空！");
        Assert.notNull(request.getCustomerEid(), "保存或更新：客户企业ID不能为空！");
        Assert.notNull(request.getGoodsId(), "保存或更新：商品ID不能为空！");
        LambdaQueryWrapper<GoodsPriceCustomerDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsPriceCustomerDO::getEid, request.getEid())
                    .eq(GoodsPriceCustomerDO::getGoodsId, request.getGoodsId())
                    .eq(GoodsPriceCustomerDO::getGoodsLine,request.getGoodsLine())
                    .eq(GoodsPriceCustomerDO::getCustomerEid, request.getCustomerEid());
        GoodsPriceCustomerDO goodsPriceCustomerDO = this.baseMapper.selectOne(queryWrapper);
        if (goodsPriceCustomerDO == null || goodsPriceCustomerDO.getId() == null) {
            goodsPriceCustomerDO= PojoUtils.map(request, GoodsPriceCustomerDO.class);
            this.baseMapper.insert(goodsPriceCustomerDO);
            return goodsPriceCustomerDO.getId();
        }
        GoodsPriceCustomerDO entity = new GoodsPriceCustomerDO();
        entity.setId(goodsPriceCustomerDO.getId());
        entity.setPriceRule(request.getPriceRule());
        entity.setPriceValue(request.getPriceValue());
        baseMapper.updateById(entity);
        return entity.getId();
    }

    @Override
    public List<CountGoodsPriceBO> countGoodsCustomerPrice(List<Long> goodsIds, Integer goodsLine) {
        return this.baseMapper.countGoodsCustomerPrice(goodsIds,goodsLine);
    }

    @Override
    public Map<Long, GoodsPriceCustomerDO> listGoodsCustomerPriceInfos(QueryGoodsLinePriceRequest request) {
        List<Long> goodsIds = request.getGoodsIds();
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }

        QueryWrapper<GoodsPriceCustomerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(GoodsPriceCustomerDO::getGoodsLine,request.getGoodsLine())
                .eq(GoodsPriceCustomerDO::getCustomerEid, request.getCustomerEid())
                .in(GoodsPriceCustomerDO::getGoodsId, goodsIds);

        List<GoodsPriceCustomerDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        Map<Long, GoodsPriceCustomerDO> map = list.stream().collect(Collectors.toMap(GoodsPriceCustomerDO::getGoodsId, Function.identity(), (oldData, newData) -> newData));
        return map;
    }

    @Override
    public GoodsPriceCustomerDO get(QueryGoodsPriceCustomerRequest request) {
        LambdaQueryWrapper<GoodsPriceCustomerDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GoodsPriceCustomerDO::getEid,request.getEid());
        lambdaQueryWrapper.eq(GoodsPriceCustomerDO::getCustomerEid,request.getCustomerEid());
        lambdaQueryWrapper.eq(GoodsPriceCustomerDO::getGoodsId,request.getGoodsId());
        lambdaQueryWrapper.eq(GoodsPriceCustomerDO::getGoodsLine,request.getGoodsLine());

        return this.list(lambdaQueryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public List<GoodsPriceCustomerDTO> getByGoodsId(Long goodsId) {
        if(ObjectUtil.isNull(goodsId)){
            return ListUtil.toList();
        }
        LambdaQueryWrapper<GoodsPriceCustomerDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GoodsPriceCustomerDO::getGoodsId, goodsId);
        List<GoodsPriceCustomerDO> list = this.list(lambdaQueryWrapper);
        return PojoUtils.map(list, GoodsPriceCustomerDTO.class);
    }

}
