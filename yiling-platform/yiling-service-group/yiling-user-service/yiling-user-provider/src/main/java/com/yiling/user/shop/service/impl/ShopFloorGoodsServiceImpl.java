package com.yiling.user.shop.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.shop.dto.ShopFloorGoodsDTO;
import com.yiling.user.shop.dto.request.QueryFloorGoodsPageRequest;
import com.yiling.user.shop.entity.ShopFloorGoodsDO;
import com.yiling.user.shop.dao.ShopFloorGoodsMapper;
import com.yiling.user.shop.service.ShopFloorGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 店铺楼层商品表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-20
 */
@Slf4j
@Service
public class ShopFloorGoodsServiceImpl extends BaseServiceImpl<ShopFloorGoodsMapper, ShopFloorGoodsDO> implements ShopFloorGoodsService {

    @Override
    public List<ShopFloorGoodsDTO> getByFloorId(Long floorId) {
        LambdaQueryWrapper<ShopFloorGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFloorGoodsDO::getFloorId, floorId);
        return PojoUtils.map(this.list(wrapper), ShopFloorGoodsDTO.class);
    }

    @Override
    public Map<Long, List<ShopFloorGoodsDO>> getMapByFloorIdList(List<Long> floorIdList) {
        if (CollUtil.isEmpty(floorIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<ShopFloorGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ShopFloorGoodsDO::getFloorId, floorIdList);
        return this.list(wrapper).stream().collect(Collectors.groupingBy(ShopFloorGoodsDO::getFloorId));
    }

    @Override
    public Page<ShopFloorGoodsDTO> queryFloorGoodsPage(QueryFloorGoodsPageRequest request) {
        LambdaQueryWrapper<ShopFloorGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFloorGoodsDO::getFloorId, request.getFloorId());
        wrapper.orderByDesc(ShopFloorGoodsDO::getSort, ShopFloorGoodsDO::getUpdateTime);
        Page<ShopFloorGoodsDO> floorGoodsDOPage = this.page(request.getPage(), wrapper);
        return PojoUtils.map(floorGoodsDOPage, ShopFloorGoodsDTO.class);
    }

    @Override
    public List<ShopFloorGoodsDTO> queryFloorGoodsList(Long floorId) {
        LambdaQueryWrapper<ShopFloorGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFloorGoodsDO::getFloorId, floorId);
        wrapper.orderByDesc(ShopFloorGoodsDO::getSort, ShopFloorGoodsDO::getUpdateTime);
        List<ShopFloorGoodsDO> floorGoodsDOPage = this.list(wrapper);
        return PojoUtils.map(floorGoodsDOPage, ShopFloorGoodsDTO.class);
    }
}
