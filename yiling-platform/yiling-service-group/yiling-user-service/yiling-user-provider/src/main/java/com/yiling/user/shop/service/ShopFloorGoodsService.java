package com.yiling.user.shop.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.shop.dto.ShopFloorGoodsDTO;
import com.yiling.user.shop.dto.request.QueryFloorGoodsPageRequest;
import com.yiling.user.shop.entity.ShopFloorGoodsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 店铺楼层商品表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-20
 */
public interface ShopFloorGoodsService extends BaseService<ShopFloorGoodsDO> {

    /**
     * 根据楼层Id获取该楼层商品
     *
     * @param floorId
     * @return
     */
    List<ShopFloorGoodsDTO> getByFloorId(Long floorId);


    /**
     * 根据楼层Id批量获取楼层商品
     *
     * @param floorIdList
     * @return
     */
    Map<Long, List<ShopFloorGoodsDO>> getMapByFloorIdList(List<Long> floorIdList);

    /**
     * 查看楼层商品分页
     *
     * @param request
     * @return
     */
    Page<ShopFloorGoodsDTO> queryFloorGoodsPage(QueryFloorGoodsPageRequest request);

    /**
     * 查看楼层商品列表
     *
     * @param floorId
     * @return
     */
    List<ShopFloorGoodsDTO> queryFloorGoodsList(Long floorId);

}
