package com.yiling.settlement.b2b.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySettlementOrderPageListRequest extends QueryPageListRequest {

	/**
	 * 卖家企业id
	 */
	private Long sellerEid;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 货款结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	private Integer goodsStatus;

	/**
	 * 促销结算状态 1-待结算 2-银行处理中 3-已结算 4-结算失败
	 */
	private Integer saleStatus;

    /**
     * 预售违约结算状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败
     */
    private Integer presaleDefaultStatus;

	/**
	 * 最小创建时间
	 */
	private Date minCreateTime;

	/**
	 * 最大创建时间
	 */
	private Date maxCreateTime;

}
