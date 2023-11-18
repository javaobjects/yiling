package com.yiling.sales.assistant.app.rebate.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.user.agreement.enums.AgreementRebateApplyRangeTypeEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApplyPageListItemVO extends BaseVO {

	/**
	 * 申请单号
	 */
	@ApiModelProperty(value = "申请单号")
	private String code;

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
	 * 所属月度（1-12）、季度（1-4）、年度（1-2）、全年（1）
	 */
	@ApiModelProperty(value = "所属月度（1-12）、季度（1-4）、年度（1-2）、全年（1）",hidden = true)
	@JsonIgnore
	private Integer month;

	/**
	 * 月度类型 1-月度 2-季度 3-年度 4-全年
	 */
	@ApiModelProperty(value = "月度类型 1-月度 2-季度 3-年度 4-全年",hidden = true)
	@JsonIgnore
	private Integer rangeType;

	/**
	 * 归属年月
	 */
	@ApiModelProperty(value = "归属年月")
	private String dateTypeStr;

	/**
	 * 申请单状态 1-待审核 2-已入账 3-驳回
	 */
	@ApiModelProperty(value = "申请单状态 1-待审核 2-已入账 3-驳回")
	private Integer status;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;


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
