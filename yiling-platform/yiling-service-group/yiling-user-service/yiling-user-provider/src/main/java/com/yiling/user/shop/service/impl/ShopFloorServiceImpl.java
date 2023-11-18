package com.yiling.user.shop.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.shop.bo.ShopFloorBO;
import com.yiling.user.shop.dto.ShopFloorDTO;
import com.yiling.user.shop.dto.ShopFloorGoodsDTO;
import com.yiling.user.shop.dto.request.QueryShopFloorPageRequest;
import com.yiling.user.shop.dto.request.SaveShopFloorRequest;
import com.yiling.user.shop.dto.request.UpdateShopFloorStatusRequest;
import com.yiling.user.shop.entity.ShopFloorDO;
import com.yiling.user.shop.dao.ShopFloorMapper;
import com.yiling.user.shop.entity.ShopFloorGoodsDO;
import com.yiling.user.shop.service.ShopFloorGoodsService;
import com.yiling.user.shop.service.ShopFloorService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.shop.service.ShopService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 店铺楼层表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-20
 */
@Slf4j
@Service
public class ShopFloorServiceImpl extends BaseServiceImpl<ShopFloorMapper, ShopFloorDO> implements ShopFloorService {

    @Autowired
    ShopFloorGoodsService shopFloorGoodsService;
    @Autowired
    ShopService shopService;

    @Override
    public Page<ShopFloorBO> queryListPage(QueryShopFloorPageRequest request) {
        QueryWrapper<ShopFloorDO> wrapper = WrapperUtils.getWrapper(request);
        Page<ShopFloorBO> floorBOPage = PojoUtils.map(this.page(request.getPage(), wrapper), ShopFloorBO.class);

        List<Long> floorIdList = floorBOPage.getRecords().stream().map(BaseDTO::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(floorIdList)) {
            Map<Long, List<ShopFloorGoodsDO>> map = shopFloorGoodsService.getMapByFloorIdList(floorIdList);
            floorBOPage.getRecords().forEach(shopFloorBO -> shopFloorBO.setGoodsNum(map.getOrDefault(shopFloorBO.getId(), ListUtil.toList()).size()));
        }

        return floorBOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFloor(SaveShopFloorRequest request) {
        ShopFloorDO shopFloorDO = PojoUtils.map(request, ShopFloorDO.class);

        List<SaveShopFloorRequest.SaveShopGoodsRequest> saveShopGoodsRequestList = request.getShopGoodsList().stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SaveShopFloorRequest.SaveShopGoodsRequest::getGoodsId))), ArrayList::new));

        if (Objects.isNull(request.getId()) || request.getId() == 0) {
            // 校验楼层名称和数量
            int count = this.getShopFloorCount(request);
            if (count >= 10) {
                throw new BusinessException(UserErrorCode.SHOP_FLOOR_OVER_MAX);
            }
            if (request.getShopGoodsList().size() > 100) {
                throw new BusinessException(UserErrorCode.SHOP_FLOOR_GOODS_OVER_MAX);
            }

            ShopFloorDO floorDO = this.getShopFloorByName(request);
            if (Objects.nonNull(floorDO)) {
                throw new BusinessException(UserErrorCode.SHOP_FLOOR_HAVE_EXIST);
            }

            shopFloorDO.setShopId(shopService.getShop(request.getEid()).getId());
            this.save(shopFloorDO);

            // 保存楼层商品
            List<ShopFloorGoodsDO> floorGoodsDOList = PojoUtils.map(saveShopGoodsRequestList, ShopFloorGoodsDO.class);
            floorGoodsDOList.forEach(shopFloorGoodsDO -> {
                shopFloorGoodsDO.setFloorId(shopFloorDO.getId());
                shopFloorGoodsDO.setOpUserId(request.getOpUserId());
            });
            shopFloorGoodsService.saveBatch(floorGoodsDOList);

        } else {
            // 校验名称重复
            ShopFloorDO floorDO = this.getShopFloorByName(request);
            if (Objects.nonNull(floorDO) && floorDO.getId().compareTo(request.getId()) != 0) {
                throw new BusinessException(UserErrorCode.SHOP_FLOOR_HAVE_EXIST);
            }

            // 校验
            List<ShopFloorGoodsDTO> floorGoodsDTOList = shopFloorGoodsService.queryFloorGoodsList(request.getId());
            if (floorGoodsDTOList.size() + request.getShopGoodsList().size() > 100) {
                throw new BusinessException(UserErrorCode.SHOP_FLOOR_GOODS_OVER_MAX);
            }

            this.updateById(shopFloorDO);

            // 保存楼层商品
            List<Long> existGoodsIdList = floorGoodsDTOList.stream().map(ShopFloorGoodsDTO::getGoodsId).distinct().collect(Collectors.toList());
            List<Long> newGoodsIdList = saveShopGoodsRequestList.stream().map(SaveShopFloorRequest.SaveShopGoodsRequest::getGoodsId).distinct().collect(Collectors.toList());
            // 需要删除的
            List<Long> removeList = existGoodsIdList.stream().filter(goodsId -> !newGoodsIdList.contains(goodsId)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(removeList)) {
                ShopFloorGoodsDO floorGoodsDO = new ShopFloorGoodsDO();
                floorGoodsDO.setOpUserId(request.getOpUserId());
                LambdaQueryWrapper<ShopFloorGoodsDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ShopFloorGoodsDO::getFloorId, shopFloorDO.getId());
                wrapper.in(ShopFloorGoodsDO::getGoodsId, removeList);
                shopFloorGoodsService.batchDeleteWithFill(floorGoodsDO, wrapper);
            }
            // 需要添加的
            List<SaveShopFloorRequest.SaveShopGoodsRequest> addList = saveShopGoodsRequestList.stream().filter(saveShopGoodsRequest -> !existGoodsIdList.contains(saveShopGoodsRequest.getGoodsId())).collect(Collectors.toList());
            addList.forEach(saveShopGoodsRequest -> {
                ShopFloorGoodsDO floorGoodsDO = PojoUtils.map(saveShopGoodsRequest, ShopFloorGoodsDO.class);
                floorGoodsDO.setFloorId(shopFloorDO.getId());
                floorGoodsDO.setOpUserId(request.getOpUserId());
                shopFloorGoodsService.save(floorGoodsDO);
            });
            // 需要更新的
            List<SaveShopFloorRequest.SaveShopGoodsRequest> updateList = saveShopGoodsRequestList.stream().filter(saveShopGoodsRequest -> existGoodsIdList.contains(saveShopGoodsRequest.getGoodsId())).collect(Collectors.toList());
            Map<Long, ShopFloorGoodsDTO> floorGoodsDTOMap = floorGoodsDTOList.stream().collect(Collectors.toMap(ShopFloorGoodsDTO::getGoodsId, Function.identity(), (k1, k2) -> k2));
            updateList.forEach(saveShopGoodsRequest -> {
                ShopFloorGoodsDO floorGoodsDO = new ShopFloorGoodsDO();
                floorGoodsDO.setId(floorGoodsDTOMap.get(saveShopGoodsRequest.getGoodsId()).getId());
                floorGoodsDO.setSort(saveShopGoodsRequest.getSort());
                floorGoodsDO.setOpUserId(request.getOpUserId());
                shopFloorGoodsService.updateById(floorGoodsDO);
            });

        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(UpdateShopFloorStatusRequest request) {
        ShopFloorDO shopFloorDO = PojoUtils.map(request, ShopFloorDO.class);
        return this.updateById(shopFloorDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFloor(Long id, Long currentUserId) {
        ShopFloorDO shopFloorDO = new ShopFloorDO();
        shopFloorDO.setId(id);
        shopFloorDO.setOpUserId(currentUserId);
        return this.deleteByIdWithFill(shopFloorDO) > 0;
    }

    @Override
    public boolean existFloorFlag(Long eid) {
        LambdaQueryWrapper<ShopFloorDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFloorDO::getEid, eid);
        wrapper.eq(ShopFloorDO::getStatus, EnableStatusEnum.ENABLED.getCode());
        return this.count(wrapper) > 0;
    }

    @Override
    public List<ShopFloorDTO> getShopFloorList(Long eid) {
        LambdaQueryWrapper<ShopFloorDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFloorDO::getEid, eid);
        wrapper.eq(ShopFloorDO::getStatus, EnableStatusEnum.ENABLED.getCode());
        wrapper.orderByDesc(ShopFloorDO::getSort, ShopFloorDO::getUpdateTime);
        List<ShopFloorDO> floorDOList = this.list(wrapper);
        return PojoUtils.map(floorDOList, ShopFloorDTO.class);
    }

    /**
     * 根据楼层名获取店铺楼层
     *
     * @param request
     * @return
     */
    private ShopFloorDO getShopFloorByName(SaveShopFloorRequest request) {
        LambdaQueryWrapper<ShopFloorDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopFloorDO::getEid, request.getEid());
        queryWrapper.eq(ShopFloorDO::getName, request.getName());
        queryWrapper.last("limit 1");
        ShopFloorDO floorDO = this.getOne(queryWrapper);
        return floorDO;
    }

    /**
     * 获取店铺楼层数量
     *
     * @param request
     * @return
     */
    private int getShopFloorCount(SaveShopFloorRequest request) {
        LambdaQueryWrapper<ShopFloorDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopFloorDO::getEid, request.getEid());
        return this.count(wrapper);
    }

}
