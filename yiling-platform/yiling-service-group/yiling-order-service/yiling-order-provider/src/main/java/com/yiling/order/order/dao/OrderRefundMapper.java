package com.yiling.order.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.entity.OrderRefundDO;

/**
 * <p>
 * 退款表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-19
 */
@Repository
public interface OrderRefundMapper extends BaseMapper<OrderRefundDO> {

    /**
     * 退款单分页查询
     *
     * @param page
     * @param request
     * @return
     */
    Page<PageOrderRefundDTO> pageList(Page<OrderRefundDO> page, @Param("request") RefundPageRequest request);

}
