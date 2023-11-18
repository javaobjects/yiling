package com.yiling.mall.agreement.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 计算协议返利申请form
 *
 * @author dexi.yao
 * @date 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CalculateRebateApplyRequest extends BaseRequest {

	/**
	 * 企业id
	 */
	@NotNull
	private Long eid;

	/**
	 * 企业编码
	 */
	@NotNull
	private String easCode;

	/**
	 * 返利申请开始时间
	 */
	@NotNull
	private Date startDate;

	/**
	 * 返利申请结束时间
	 */
	@NotNull
	private Date endDate;

	/**
	 * 是否传入一级商 1-传入 0-未传入
	 */
	private Integer inputEntry;


}
