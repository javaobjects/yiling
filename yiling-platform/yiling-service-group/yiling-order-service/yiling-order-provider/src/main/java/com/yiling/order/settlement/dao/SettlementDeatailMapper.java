package com.yiling.order.settlement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.settlement.entity.SettlementDetailDO;

/**
 * <p>
 * 结算单明细 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-10
 */
@Repository
public interface SettlementDeatailMapper extends BaseMapper<SettlementDetailDO> {

    List<SettlementDetailDO> getSettlementDetailByEidAndTime(@Param("request")QueryOrderUseAgreementRequest request);
}
