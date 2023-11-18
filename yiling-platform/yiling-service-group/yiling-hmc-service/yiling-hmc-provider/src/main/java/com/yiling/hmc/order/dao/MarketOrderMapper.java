package com.yiling.hmc.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.order.dto.AdminMarketOrderDTO;
import com.yiling.hmc.order.dto.request.QueryAdminMarkerOrderPageRequest;
import com.yiling.hmc.order.entity.MarketOrderDO;

/**
 * <p>
 * HMC订单表 Dao 接口
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
@Repository
public interface MarketOrderMapper extends BaseMapper<MarketOrderDO> {

    /**
     * 运营后台分页查询列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<AdminMarketOrderDTO> queryAdminMarketOrderPage(Page page, @Param("request") QueryAdminMarkerOrderPageRequest request);
}
