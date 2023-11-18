package com.yiling.order.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.OrderRefundStatusRequest;
import com.yiling.order.order.dto.request.RefundFinishRequest;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.dto.request.RefundQueryRequest;
import com.yiling.order.order.entity.OrderRefundDO;

/**
 * <p>
 * 退款表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-19
 */
public interface OrderRefundService extends BaseService<OrderRefundDO> {

    /**
     * 退款单状态修改
     *
     * @param request
     * @return
     */
    boolean editStatus(OrderRefundStatusRequest request);

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
    List<OrderRefundDO> listByCondition(RefundQueryRequest request);

    /**
     * 根据订单号查询退款申请单
     * @param refundNos
     * @return
     */
    List<OrderRefundDO> listByRefundNos(List<String> refundNos);


    /**
     * 查询未自动审核的单据
     * @param limit
     * @return
     */
    List<String> selectNotAuditOrderRefunds(Integer limit);
}
