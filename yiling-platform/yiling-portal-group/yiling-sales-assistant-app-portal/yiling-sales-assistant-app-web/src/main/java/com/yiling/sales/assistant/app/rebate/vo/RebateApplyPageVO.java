package com.yiling.sales.assistant.app.rebate.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.user.agreement.enums.AgreementRebateApplyRangeTypeEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/9/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("已申请入账vo")
public class RebateApplyPageVO<T> extends Page<T> {

	@ApiModelProperty(value = "申请单id")
	private Long id;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String name;

	/**
	 * 返利所属企业id
	 */
	@ApiModelProperty(value = "返利所属企业id")
	private Long eid;

	/**
	 * 申请总金额
	 */
	@ApiModelProperty(value = "申请总金额")
	private BigDecimal totalAmount;

	/**
	 * 所属年度
	 */
	@ApiModelProperty(value = "所属年度",hidden = true)
	@JsonIgnore
	private Integer year;

	/**
	 * 所属月度（1-12）、季度（1-4）、其余该值为 0
	 */
	@ApiModelProperty(value = "所属月度（1-12）、季度（1-4）、其余该值为 0",hidden = true)
	@JsonIgnore
	private Integer month;

	/**
	 * 月度类型 1-月度 2-季度 3-上半年 4-下半年 5-全年
	 */
	@ApiModelProperty(value = "月度类型 1-月度 2-季度 3-上半年 4-下半年 5-全年",hidden = true)
	@JsonIgnore
	private Integer rangeType;

	/**
	 * 入账企业名称
	 */
	@ApiModelProperty(value = "入账企业名称")
	private String entryName;

	/**
	 * 入账企业easCode
	 */
	@ApiModelProperty(value = "入账企业easCode")
	private String entryCode;

	/**
	 * 省份
	 */
	@ApiModelProperty(value = "省份")
	private String provinceName;


	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	/**
	 * 状态 1-待审核 2-已入账 3-驳回
	 */
	@ApiModelProperty(value = "状态 1-待审核 2-已入账 3-驳回")
	private Integer status;

	/**
	 * 归属年月
	 */
	@ApiModelProperty(value = "归属年月")
	private String dateTypeStr;


	public String getDateTypeStr() {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append(year);
		stringBuffer.append("年");
		if (month!=0){
			stringBuffer.append(month);
		}
		stringBuffer.append(AgreementRebateApplyRangeTypeEnum.getByCode(rangeType).getName());
		return stringBuffer.toString();
	}
}
