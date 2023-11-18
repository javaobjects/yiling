package com.yiling.user.shop.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.shop.entity.ShopDO;

/**
 * <p>
 * 店铺设置表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
@Repository
public interface ShopMapper extends BaseMapper<ShopDO> {
    /**
     * 查询建采商家分页
     * @param page
     * @param request
     */
    Page<ShopDO> queryPurchaseShopListPage(Page<ShopDO> page, @Param("request")QueryShopRequest request);

}
