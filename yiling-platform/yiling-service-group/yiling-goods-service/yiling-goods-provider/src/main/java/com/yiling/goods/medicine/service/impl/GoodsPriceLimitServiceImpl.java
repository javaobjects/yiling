package com.yiling.goods.medicine.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dao.GoodsPriceLimitMapper;
import com.yiling.goods.medicine.dto.GoodsPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.DeleteGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPriceLimitPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsPriceLimitRequest;
import com.yiling.goods.medicine.entity.GoodsPriceLimitDO;
import com.yiling.goods.medicine.enums.GoodsPriceLimitErrorCode;
import com.yiling.goods.medicine.service.GoodsPriceLimitService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 商品限价表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-26
 */
@Service
public class GoodsPriceLimitServiceImpl extends BaseServiceImpl<GoodsPriceLimitMapper, GoodsPriceLimitDO> implements GoodsPriceLimitService {


    @Override
    public Page<GoodsPriceLimitDTO> pageList(QueryGoodsPriceLimitPageRequest request) {
        Page<GoodsPriceLimitDO> page = this.baseMapper.pageList(new Page<>(request.getCurrent(), request.getSize()), request);
        return PojoUtils.map(page, GoodsPriceLimitDTO.class);
    }


    @Override
    public Boolean addGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request) {
        QueryWrapper<GoodsPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsPriceLimitDO::getGoodsId, request.getGoodsId())
                .eq(GoodsPriceLimitDO::getProvinceCode, request.getProvinceCode())
                .eq(GoodsPriceLimitDO::getCityCode, request.getCityCode())
                .eq(GoodsPriceLimitDO::getRegionCode, request.getRegionCode())
                .eq(GoodsPriceLimitDO::getCustomerType, request.getCustomerType());
        GoodsPriceLimitDO goodsPriceLimit = this.getOne(queryWrapper);
        if (goodsPriceLimit != null) {
            throw new BusinessException(GoodsPriceLimitErrorCode.HAS_EXIST);
        }
        GoodsPriceLimitDO goodsPriceLimitDO = PojoUtils.map(request, GoodsPriceLimitDO.class);
        return this.save(goodsPriceLimitDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteGoodsPriceLimit(DeleteGoodsPriceLimitRequest request) {
        for (Long id : request.getIds()) {
            GoodsPriceLimitDO goodsPriceLimitDO = new GoodsPriceLimitDO();
            goodsPriceLimitDO.setId(id);
            goodsPriceLimitDO.setOpUserId(request.getOpUserId());
            this.deleteByIdWithFill(goodsPriceLimitDO);
        }
        return true;
    }

    @Override
    public Boolean updateGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request) {
        GoodsPriceLimitDO goodsPriceLimit=this.getById(request.getId());
        QueryWrapper<GoodsPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsPriceLimitDO::getGoodsId, goodsPriceLimit.getGoodsId())
                .eq(GoodsPriceLimitDO::getProvinceCode, request.getProvinceCode())
                .eq(GoodsPriceLimitDO::getCityCode, request.getCityCode())
                .eq(GoodsPriceLimitDO::getRegionCode, request.getRegionCode())
                .eq(GoodsPriceLimitDO::getCustomerType, request.getCustomerType());
        GoodsPriceLimitDO oldGoodsPriceLimit = this.getOne(queryWrapper);
        if (oldGoodsPriceLimit != null) {
            if (!oldGoodsPriceLimit.getId().equals(request.getId())) {
                throw new BusinessException(GoodsPriceLimitErrorCode.HAS_EXIST);
            }
        }

        GoodsPriceLimitDO goodsPriceLimitDO = PojoUtils.map(request, GoodsPriceLimitDO.class);
        return this.updateById(goodsPriceLimitDO);
    }

    @Override
    public GoodsPriceLimitDTO getGoodsPriceLimitById(Long id) {
        return PojoUtils.map(this.getById(id), GoodsPriceLimitDTO.class);
    }

    @Override
    public Map<Long, List<GoodsPriceLimitDTO>> listGoodsPriceLimitByGoodsIds(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return MapUtil.empty();
        }
        QueryWrapper<GoodsPriceLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsPriceLimitDO::getGoodsId, goodsIds);

        List<GoodsPriceLimitDO> goodsPriceLimitList = this.list(queryWrapper);
        List<GoodsPriceLimitDTO> goodsPriceLimitDTOList = PojoUtils.map(goodsPriceLimitList, GoodsPriceLimitDTO.class);
        return goodsPriceLimitDTOList.stream().collect(Collectors.groupingBy(GoodsPriceLimitDTO::getGoodsId));
    }

}
