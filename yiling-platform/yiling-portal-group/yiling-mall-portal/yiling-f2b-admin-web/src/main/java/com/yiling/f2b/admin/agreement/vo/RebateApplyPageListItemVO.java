package com.yiling.f2b.admin.agreement.vo;

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
 * @date 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateApplyPageListItemVO extends BaseVO {

	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "企业id")
	private Long eid;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String name;

	/**
	 * 渠道ID
	 */
	@ApiModelProperty(value = "渠道ID")
	private Long channelId;

	/**
	 * 状态：1-启用 2-停用
	 */
	@ApiModelProperty(value = "企业状态：1-启用 2-停用")
	private Integer entStatus;

	/**
	 * 企业编码
	 */
	@ApiModelProperty(value = "企业编码")
	private String easCode;

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

	/**
	 * 审核时间
	 */
	@ApiModelProperty(value = "审核时间")
	private Date auditTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createUserName;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人id",hidden = true)
	@JsonIgnore
	private Long createUser;

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
