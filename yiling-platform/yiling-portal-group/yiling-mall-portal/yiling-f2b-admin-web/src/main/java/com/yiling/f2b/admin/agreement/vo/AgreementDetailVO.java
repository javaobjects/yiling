package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("协议详情VO--用id请求协议商品，id为协议id")
public class AgreementDetailVO extends BaseVO {

	/**
     * 协议客户ID（乙方）
     */
    @ApiModelProperty(value = "协议客户ID")
    private Long secondEid;

	/**
     * 企业名称（乙方）
     */
    @ApiModelProperty(value = "当前企业名称,如果当前是主协议详情则该字段为当前渠道商信息的企业名称" +
			",如果为补充协议详情则该字段为顶部乙方相关信息")
    private String secondName;

    /**
     * 渠道业务人员数量（乙方）
     */
    @ApiModelProperty(value = "业务人员数量,如果当前是主协议详情则该字段为当前渠道商信息的商务人个数,如果为补充协议详情则该字段为顶部乙方业务人员个数")
    private Long secondSalesmanNum;

    /**
     * 渠道类型名称（乙方）
     */
    @ApiModelProperty(value = "渠道类型名称,如果当前是主协议详情则该字段为当前渠道商信息的渠道类型名称,如果为补充协议详情则该字段为顶部乙方的渠道类型名称")
    private String secondChannelName;

	/**
	 * 丙方名称
	 */
	@ApiModelProperty(value = "丙方企业名称,只有在补充协议详情会有该字段")
	private String thirdName;

	/**
	 * 丙方渠道类型名称
	 */
	@ApiModelProperty(value = "丙方渠道类型名称,只有在补充协议详情会有该字段")
	private String thirdChannelName;

	/**
	 * 渠道业务人员数量（丙方）
	 */
	@ApiModelProperty(value = "丙方业务人员数量,只有在补充协议详情会有该字段")
	private Long thirdSalesmanNum;

	/**
	 * 协议主体(甲方)
	 */
	@ApiModelProperty(value = "协议主体名称,如果当前是主协议详情则该字段为协议基本信息的协议主体,如果为补充协议详情则该字段为顶部的甲方名称")
	private String ename;

	/**
	 * 协议分类：1-年度协议 2-补充协议
	 */
	@ApiModelProperty(value = "协议分类：1-年度协议 2-补充协议")
	private Integer category;

	/**
     * 协议名称
     */
    @ApiModelProperty(value = "协议名称")
    private String name;

    /**
     * 协议描述
     */
    @ApiModelProperty(value = "协议描述")
    private String content;

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
	 * 协议类型 1-采购类 2-其他
	 */
	@ApiModelProperty(value = "协议类型 1-采购类 2-其他，，，只有当前是补充协议详情会有该字段")
	private Integer type;

	/**
	 * 子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利
	 */
	@ApiModelProperty(value = "子类型 1-购进额 2-购进量 3-回款额 4-数据直连 5-破损返利，，，只有当前是补充协议详情（category=2时）会有该字段")
	private Integer childType;

	/**
     * 补充协议数量
     */
    @ApiModelProperty(value = "补充协议数量，，，只有当前是年度协议详情（category=1时）会有该字段")
    private Integer supplementAgreementNum;

	/**
	 * 返利周期 1-订单立返 2-进入返利池
	 */
	@ApiModelProperty(value = "返利周期 1-订单立返 2-进入返利池，，，只有当前是补充协议详情（category=2时）会有该字段")
	private Integer rebateCycle;

	/**
	 * 返利形式：1- 票折 2- 现金 3-冲红 4-健康城卡
	 */
	@ApiModelProperty(value = "返利形式：1- 票折 2- 现金 3-冲红 4-健康城卡，，，只有当前是补充协议详情（category=2时）会有该字段")
	private Integer restitutionType;

	/**
	 * 返利百分比
	 */
	@ApiModelProperty(value = "返利政策值，，，只有当前是补充协议详情（category=2时）会有该字段")
	private BigDecimal policyValue;

	/**
	 * 返利条件
	 */
	@ApiModelProperty(value = "返利条件，，，只有当前是补充协议详情（category=2时）会有该字段，但该字段可能为空，为空时不展示返利条件设置")
	private AgreementConditionVO agreementsCondition;
}
