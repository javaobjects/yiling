package com.yiling.mall.agreement.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRebateApplyRequest extends BaseRequest {

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
	 * 企业名称
	 */
	@NotNull
	private String name;

	/**
	 * 年度
	 */
	@NotNull
	private Integer year;

	/**
	 * 月度
	 */
	@NotNull
	private Integer month;

	/**
	 * 月度类型 1-月度 2-季度 3-上半年 4-下半年 5-全年
	 */
	@NotNull
	private Integer rangeType;


	/**
	 * 入账企业id
	 */
	private Long entryEid;

	/**
	 * 入账企业名称
	 */
	private String entryName;

	/**
	 * 入账企业easCode
	 */
	private String entryCode;

	/**
	 * 是否传入一级商 1-传入 0-未传入
	 */
	private Integer inputEntry;

	/**
	 * 省份
	 */
	@NotNull
	private String provinceName;

	/**
	 * 商品id
	 */
	@NotNull
	private Long goodsId;

	/**
	 * 商品名称
	 */
	@NotNull
	private String goodsName;

	/**
	 * 返利总额
	 */
	@NotNull
	private BigDecimal totalAmount;

	/**
	 * 是否点击计算按钮：0-否 1-是
	 */
	@NotNull
	private Integer calculateStatus;

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
	 * 创建人工号
	 */
	private String createUserCode;

	private List<RebateApplyDetail> applyDetails;

	@Data
	public static class RebateApplyDetail implements Serializable {

		/**
		 * 金额
		 */
		@NotNull
		private BigDecimal amount;

		/**
		 * 金额
		 */
		@NotNull
		private String entryDescribe;

		/**
		 * 销售组织id
		 */
		@NotNull
		private Long sellerEid;

		/**
		 * 销售组织名称
		 */
		@NotNull
		private String sellerName;


	}


}
