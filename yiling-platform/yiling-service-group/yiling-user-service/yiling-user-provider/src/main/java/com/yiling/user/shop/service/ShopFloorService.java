package com.yiling.user.shop.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.shop.bo.ShopFloorBO;
import com.yiling.user.shop.dto.ShopFloorDTO;
import com.yiling.user.shop.dto.request.QueryShopFloorPageRequest;
import com.yiling.user.shop.dto.request.SaveShopFloorRequest;
import com.yiling.user.shop.dto.request.UpdateShopFloorStatusRequest;
import com.yiling.user.shop.entity.ShopFloorDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 店铺楼层表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-20
 */
public interface ShopFloorService extends BaseService<ShopFloorDO> {

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
