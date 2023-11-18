package com.yiling.hmc.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.entity.OrderDO;

/**
 * <p>
 * 订单表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Repository
public interface OrderMapper extends BaseMapper<OrderDO> {

    /**
     * 订单分页查询(多表关联)
     *
     * @param page 分页信息
     * @param request 查询条件
     * @return 订单信息
     */
    Page<OrderDO> pageListByCondition(Page<OrderDO> page, @Param("request") OrderPageRequest request);
}
