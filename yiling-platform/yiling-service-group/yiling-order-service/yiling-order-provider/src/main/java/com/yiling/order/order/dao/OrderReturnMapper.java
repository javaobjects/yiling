package com.yiling.order.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.dto.AgreementOrderReturnDetailDTO;
import com.yiling.order.order.dto.request.OrderReturnPullErpPageRequest;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.entity.OrderReturnDO;

/**
 * <p>
 * 退货单 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Repository
public interface OrderReturnMapper extends BaseMapper<OrderReturnDO> {

    Page<OrderReturnDO> selectErpPullReturnOrder(Page<OrderReturnDO> page, @Param("request") OrderReturnPullErpPageRequest request);

    List<AgreementOrderReturnDetailDTO> getOrderReturnDetailByEidAndTime(@Param("request") QueryOrderUseAgreementRequest request);
}
