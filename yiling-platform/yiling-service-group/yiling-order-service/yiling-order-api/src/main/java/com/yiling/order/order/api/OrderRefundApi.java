package com.yiling.order.order.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.OrderRefundDTO;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.OrderRefundStatusRequest;
import com.yiling.order.order.dto.request.RefundFinishRequest;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.dto.request.RefundQueryRequest;

/**
 * 退款单操作
 *
 * @author: yong.zhang
 * @date: 2021/10/27
 */
public interface OrderRefundApi {

    /**
     * @param id
     * @return
     */
    OrderRefundDTO queryById(Long id);

    /**
     * 新增退款单
     *
     * @param orderRefundDTO
     * @return
     */
    OrderRefundDTO saveOrderRefund(OrderRefundDTO orderRefundDTO);

    /**
     * 批量新增退款单
     * @param orderRefundDTOs
     * @return
     */
    List<OrderRefundDTO> batchSaveOrderRefund(List<OrderRefundDTO> orderRefundDTOs);

    /**
     * 退款单状态修改
     *
     * @param request
     * @return
     */
    boolean editStatus(OrderRefundStatusRequest request);


    /**
     * 批量修改退款单状态
     * @param requestList
     * @return
     */
    boolean editStatus(List<OrderRefundStatusRequest> requestList);

    /**
     * 退款完成回调
     *
     * @param request
     * @return
     */
    boolean finishRefund(RefundFinishRequest request);

    /**
     * 退款单列表查询
     *
     * @param request
     * @return
     */
    Page<PageOrderRefundDTO> pageList(RefundPageRequest request);

    /**
     * 根据条件查询退款单信息
     *
     * @param request
     * @return
     */
    List<OrderRefundDTO> listByCondition(RefundQueryRequest request);

    /**
     * 根据订单号查询退款申请单
     * @param refundNos
     * @return
     */
    List<OrderRefundDTO> listByRefundNos(List<String> refundNos);


    /**
     * 补偿自动自动审核确未自动审核的数据
     * @param count
     * @return
     */
    boolean compensationAutoNotAuditData(Integer count);
}
