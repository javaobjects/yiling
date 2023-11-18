package com.yiling.order.settlement.service;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.settlement.dto.SettlementFullDTO;
import com.yiling.order.settlement.dto.request.SaveSettlementRequest;
import com.yiling.order.settlement.entity.SettlementDO;

/**
 * <p>
 * 结算单 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-10
 */
public interface SettlementService extends BaseService<SettlementDO> {

     Boolean saveSettlement(SaveSettlementRequest request);

    List<SettlementFullDTO> getSettlementDetailByEidAndTime(QueryOrderUseAgreementRequest request);

    BigDecimal getsettlementAmountByOrderId(Long orderId);
}
