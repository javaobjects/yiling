package com.yiling.admin.sales.assistant.commissions.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("用户佣金明细VO")
public class CommissionsDetailItemPageListVO extends BaseVO {

	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private Long orderId;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderCode;

	/**
	 * 佣金金额
	 */
	@ApiModelProperty(value = "佣金金额")
	private BigDecimal subAmount;

    /**
     * 拉新企业id
     */
    @ApiModelProperty(value = "拉新企业id",hidden = true)
    @JsonIgnore
    private Long newEntId;

    /**
     * 拉新用户id
     */
    @ApiModelProperty(value = "拉新用户id",hidden = true)
    @JsonIgnore
    private Long newUserId;

	/**
	 * 拉新人/户名称
	 */
	@ApiModelProperty(value = "拉新人/户名称")
	private String name;

    /**
     * 购买会员企业名称
     */
    @ApiModelProperty(value = "购买会员企业名称",hidden = true)
    @JsonIgnore
    private String buyMemberName;

	/**
	 * 兑付状态 1-未兑付 2-以兑付
	 */
	@ApiModelProperty(value = "兑付状态 1-未兑付 2-以兑付")
	private Integer status;

    /**
     * 用户类型：1-以岭人员 2-小三元 3-自然人
     */
    @ApiModelProperty(value = "用户类型：1-以岭人员 2-小三元 3-自然人")
    private Integer taskUserType;

    /**
     * 任务的属企业eid
     */
    @ApiModelProperty(value = "所属企业id",hidden = true)
    @JsonIgnore
    private Long taskOwnershipEid;

    /**
     * 所属企业
     */
    @ApiModelProperty(value = "所属企业")
    private String taskOwnershipName;

    /**
     * 下线推广人
     */
    @ApiModelProperty(value = "下线推广人id",hidden = true)
    @JsonIgnore
    private Long subordinateUserId;

    /**
     * 下线推广人
     */
    @ApiModelProperty(value = "下线推广人")
    private String subordinateUserName;

}
