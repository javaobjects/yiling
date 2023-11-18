package com.yiling.order.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;
import com.yiling.order.order.entity.OrderDeliveryDO;

/**
 * <p>
 * 订单发货信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Repository
public interface OrderDeliveryMapper extends BaseMapper<OrderDeliveryDO> {

    /**
     * 获取流向信息
     *
     * @param request
     * @return
     */
    Page<OrderFlowBO> getPageList(Page<OrderFlowBO> page, @Param("request") QueryOrderFlowRequest request);
}
