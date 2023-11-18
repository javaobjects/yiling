package com.yiling.pricing.goods.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dao.GoodsPriceCustomerGroupMapper;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsLinePriceRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerGroupPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceGroupRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerGroupDO;
import com.yiling.pricing.goods.service.GoodsPriceCustomerGroupService;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 客户分组定价 服务实现类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Service
public class GoodsPriceCustomerGroupServiceImpl extends BaseServiceImpl<GoodsPriceCustomerGroupMapper, GoodsPriceCustomerGroupDO> implements GoodsPriceCustomerGroupService {

    @DubboReference
    CustomerApi customerApi;

    @Override
    public Page<GoodsPriceCustomerGroupDO> pageList(QueryGoodsPriceCustomerGroupPageListRequest request) {
        LambdaQueryWrapper<GoodsPriceCustomerGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getCustomerGroupId() != null && request.getCustomerGroupId() != 0) {
            queryWrapper.eq(GoodsPriceCustomerGroupDO::getCustomerGroupId, request.getCustomerGroupId());
        }
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            queryWrapper.eq(GoodsPriceCustomerGroupDO::getEid, request.getEid());
        }
        if (CollUtil.isNotEmpty(request.getGoodsIds())) {
            queryWrapper.in(GoodsPriceCustomerGroupDO::getGoodsId, request.getGoodsIds());
        }
        queryWrapper.eq(GoodsPriceCustomerGroupDO::getGoodsLine,request.getGoodsLine());
        return this.baseMapper.selectPage(request.getPage(), queryWrapper);
    }

    @Override
    public Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerGroupRequest request) {
        // 先查询是否存在
        Assert.notNull(request.getEid(), "保存或更新：企业ID不能为空！");
        Assert.notNull(request.getCustomerGroupId(), "保存或更新：客户分组ID不能为空！");
        Assert.notNull(request.getGoodsId(), "保存或更新：商品ID不能为空！");
        LambdaQueryWrapper<GoodsPriceCustomerGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsPriceCustomerGroupDO::getEid, request.getEid())
                .eq(GoodsPriceCustomerGroupDO::getGoodsLine, request.getGoodsLine())
                .eq(GoodsPriceCustomerGroupDO::getGoodsId, request.getGoodsId())
                .eq(GoodsPriceCustomerGroupDO::getCustomerGroupId, request.getCustomerGroupId());
        GoodsPriceCustomerGroupDO goodsPriceCustomerGroupDO = this.baseMapper.selectOne(queryWrapper);
        if (goodsPriceCustomerGroupDO == null || goodsPriceCustomerGroupDO.getId() == null) {
            goodsPriceCustomerGroupDO = PojoUtils.map(request, GoodsPriceCustomerGroupDO.class);
            this.baseMapper.insert(goodsPriceCustomerGroupDO);
            return goodsPriceCustomerGroupDO.getId();
        }
        GoodsPriceCustomerGroupDO entity = new GoodsPriceCustomerGroupDO();
        entity.setId(goodsPriceCustomerGroupDO.getId());
        entity.setPriceRule(request.getPriceRule());
        entity.setPriceValue(request.getPriceValue());
        baseMapper.updateById(entity);
        return entity.getId();
    }

    @Override
    public List<CountGoodsPriceBO> countGoodsCustomerGroupPrice(List<Long> goodsIds,Integer goodsLine) {
        return this.baseMapper.countGoodsCustomerGroupPrice(goodsIds,goodsLine);
    }

    @Override
    public GoodsPriceCustomerGroupDO get(QueryGoodsPriceGroupRequest request) {
        LambdaQueryWrapper<GoodsPriceCustomerGroupDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(GoodsPriceCustomerGroupDO::getEid,request.getEid());
        lambdaQueryWrapper.eq(GoodsPriceCustomerGroupDO::getCustomerGroupId,request.getCustomerGroupId());
        lambdaQueryWrapper.eq(GoodsPriceCustomerGroupDO::getGoodsId,request.getGoodsId());
        lambdaQueryWrapper.eq(GoodsPriceCustomerGroupDO::getGoodsLine,request.getGoodsLine());

        return this.list(lambdaQueryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public Map<Long, GoodsPriceCustomerGroupDO> listGoodsCustomerGroupPriceInfos(QueryGoodsLinePriceRequest request) {
        List<Long> goodsIds = request.getGoodsIds();
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }

        EnterpriseCustomerDTO enterpriseCustomerDTO = customerApi.get(request.getEid(), request.getCustomerEid());
        if (enterpriseCustomerDTO == null) {
            return MapUtil.empty();
        }

        Long customerGroupId = enterpriseCustomerDTO.getCustomerGroupId();

        QueryWrapper<GoodsPriceCustomerGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(GoodsPriceCustomerGroupDO::getGoodsLine,request.getGoodsLine())
                .eq(GoodsPriceCustomerGroupDO::getCustomerGroupId, customerGroupId)
                .in(GoodsPriceCustomerGroupDO::getGoodsId, goodsIds);

        List<GoodsPriceCustomerGroupDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        Map<Long, GoodsPriceCustomerGroupDO> map = list.stream().collect(Collectors.toMap(GoodsPriceCustomerGroupDO::getGoodsId, Function.identity(), (oldData, newData) -> newData));
        return map;
    }

    @Override
    public List<GoodsPriceCustomerGroupDTO> getByGoodsId(Long goodsId) {
        if (ObjectUtil.isNull(goodsId)) {
            return ListUtil.toList();
        }
        QueryWrapper<GoodsPriceCustomerGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsPriceCustomerGroupDO::getGoodsId, goodsId);
        List<GoodsPriceCustomerGroupDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, GoodsPriceCustomerGroupDTO.class);
    }
}
