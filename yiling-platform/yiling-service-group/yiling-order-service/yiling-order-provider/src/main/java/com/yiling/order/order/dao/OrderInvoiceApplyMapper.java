package com.yiling.order.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.entity.OrderInvoiceApplyDO;

/**
 * <p>
 * 订单开票申请 Dao 接口
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Repository
public interface OrderInvoiceApplyMapper extends BaseMapper<OrderInvoiceApplyDO> {

    /**
     * 获取未同步推送EAS申请发票订单id
     * @param page
     * @param request
     * @return
     */
    Page<OrderInvoicePullErpDTO> getErpPullInvoiceOrderId(Page<Long> page, @Param("request")OrderPullErpPageRequest request);
}
