package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.user.agreement.enums.AgreementStatusEnum;
import com.yiling.user.agreement.enums.QueryAgreementStatusEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("返利-协议分页vo")
public class AgreementAccountPageListItemVO extends BaseVO {

	/**
	 * 协议Id
	 */
	@ApiModelProperty(value = "协议Id",hidden = true)
	@JsonIgnore
	private Long agreementId;

	/**
	 * 计算时间
	 */
	@ApiModelProperty(value = "待返利金额")
	private Date calculateTime;

	/**
	 * 待返利金额
	 */
	@ApiModelProperty(value = "待返利金额")
	private BigDecimal rebateAmount;

	/**
	 * 已兑付金额
	 */
	@ApiModelProperty(value = "已兑付金额")
	private BigDecimal cashAmount;

	/**
	 * 已完成的值，但是没有达到条件
	 */
	@ApiModelProperty(value = "已完成的值")
	private BigDecimal completedConditionValue;

	/**
	 * 协议名称
	 */
	@ApiModelProperty(value = "协议名称")
	private String name;

	/**
	 * 状态：1-启用 2-停用
	 */
	@ApiModelProperty(value = "状态：1-启用 2-停用",hidden = true)
	@JsonIgnore
	private Integer status;

	/**
	 * 协议状态：1-进行中 2-未开始 3-已停用 4-已过期
	 */
	@ApiModelProperty(value = "协议状态：1-进行中 2-未开始 3-已停用 4-已过期")
	private Integer agreementStatus;

	/**
	 * 协议分类：1-年度协议 2-补充协议
	 */
	@ApiModelProperty(value = "协议分类：1-年度协议 2-补充协议")
	private Integer category;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "履约开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "履约结束时间")
	private Date endTime;

	/**
	 * 协议主体(甲方)
	 */
	@ApiModelProperty(value = "协议主体名称,如果当前是主协议详情则该字段为协议基本信息的协议主体,如果为补充协议详情则该字段为顶部的甲方名称")
	private String ename;

	/**
	 * 是否可点击兑付按钮
	 */
	@ApiModelProperty(value = "是否可点击兑付按钮 0-不可点击 1-可点击")
	private Integer liquidationButton;

	/**
	 * 协议规则
	 */
	@ApiModelProperty(value = "协议规则")
	private String rule;

	public Integer getLiquidationButton() {
		if (BigDecimal.ZERO.compareTo(rebateAmount)==-1){
			return 1;
		}else {
			return 0;
		}
	}

	public Integer getAgreementStatus() {
		//停用
		if (AgreementStatusEnum.CLOSE.getCode().equals(status)){
			return QueryAgreementStatusEnum.OUT_OF_SERVICE.getCode();
		}
		Date currentDate=new Date();
		//未开始
		if(startTime.compareTo(currentDate)==1){
			return QueryAgreementStatusEnum.NOT_STARTED.getCode();
		}
		//进行中
		if(startTime.compareTo(currentDate)==-1&&endTime.compareTo(currentDate)==1){
			return QueryAgreementStatusEnum.HAVE_IN_HAND.getCode();
		}
		//已过期
		if(endTime.compareTo(currentDate)==-1){
			return QueryAgreementStatusEnum.EXPIRED.getCode();
		}
		return 0;
	}

}
