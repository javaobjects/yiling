package com.yiling.user.agreement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.agreement.dto.request.AgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailRequest;
import com.yiling.user.agreement.entity.AgreementRebateOrderDetailDO;

/**
 * <p>
 * 协议兑付订单明细表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-07
 */
@Repository
public interface AgreementRebateOrderDetailMapper extends BaseMapper<AgreementRebateOrderDetailDO> {

    /**
     * 计算返利金额
     * @param request
     * @return
     */
    Integer updateBatchDiscountAmount(@Param("request") AgreementConditionCalculateRequest request);


    /**
     * 重新计算返利金额
     * @param request
     * @return
     */
    Integer clearDiscountAmountByOrderIdsAndAgreementIds(@Param("request") ClearAgreementConditionCalculateRequest request);


	/**
	 * 根据订单id查询返利订单明细
	 *
	 * @param request
	 * @return
	 */
	List<AgreementRebateOrderDetailDO> queryAgreementRebateOrderDetailList(@Param("request")QueryRebateOrderDetailRequest request);


}
