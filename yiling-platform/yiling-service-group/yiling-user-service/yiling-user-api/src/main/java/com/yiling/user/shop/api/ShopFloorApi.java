package com.yiling.user.shop.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.shop.bo.ShopFloorBO;
import com.yiling.user.shop.dto.ShopFloorDTO;
import com.yiling.user.shop.dto.ShopFloorGoodsDTO;
import com.yiling.user.shop.dto.request.QueryFloorGoodsPageRequest;
import com.yiling.user.shop.dto.request.QueryShopFloorPageRequest;
import com.yiling.user.shop.dto.request.SaveShopFloorRequest;
import com.yiling.user.shop.dto.request.UpdateShopFloorStatusRequest;

/**
 * 店铺楼层 API
 *
 * @author: lun.yu
 * @date: 2023-02-20
 */
public interface ShopFloorApi {

    /**
     * 查询店铺楼层分页列表
     *
     * @param request
     * @return
     */
    Page<ShopFloorBO> queryListPage(QueryShopFloorPageRequest request);

    /**
     * 保存楼层
     *
     * @param request
     * @return
     */
    boolean saveFloor(SaveShopFloorRequest request);

    /**
     * 更新楼层状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateShopFloorStatusRequest request);

    /**
     * 删除楼层
     *
     * @param id
     * @param currentUserId
     * @return
     */
    boolean deleteFloor(Long id, Long currentUserId);

    /**
     * 获取店铺楼层详情
     *
     * @param id
     * @return
     */
    ShopFloorDTO get(Long id);

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

    /**
     * 获取店铺是否存在楼层
     *
     * @param eid
     * @return
     */
    boolean existFloorFlag(Long eid);

    /**
     * 获取店铺楼层列表
     *
     * @param eid
     * @return
     */
    List<ShopFloorDTO> getShopFloorList(Long eid);
}
