package com.yiling.order.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderManageStatusNumberDTO;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.dto.request.QueryOrderManagePageRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.enums.OrderAuditStatusEnum;

/**
 * <p>
 *   订单审核服务
 * </p>
 *
 * @author:wei.wang
 * @date:2021/7/21
 */
public interface OrderAuditService extends BaseService<OrderDO> {
    /**
     * 获取预期订单列表
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderExpectInfo(QueryOrderExpectPageRequest request);

    /**
     * 预订单取消
     * @param orderId 订单id
     * @param opUserId 操作人
     * @return
     */
    Boolean cancelOrderExpect(Long orderId,Long opUserId);

    /**
     * 获取除取消订单外的所有审核订单数量
     * @param sellerEidList
     * @param departmentId
     * @param departmentType
     * @return
     */
    OrderManageStatusNumberDTO getOrderReviewStatusNumber(List<Long> sellerEidList,Long departmentId,Integer departmentType);

    /**
     * 订单审核列表
     * @param request
     * @return
     */
    Page<OrderDTO> getOrderManagePage(QueryOrderManagePageRequest request);



    /**
     * 修改审核状态为待审核
     *
     * @param id 订单ID
     * @param originalStatus 原始状态
     * @param newStatus 新状态
     * @param opUserId 操作人ID
     * @param remark 备注
     * @return
     */
    boolean updateAuditStatus(Long id, OrderAuditStatusEnum originalStatus, OrderAuditStatusEnum newStatus, Long opUserId, String remark);
}
