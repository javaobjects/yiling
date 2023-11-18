package com.yiling.sales.assistant.app.commissions.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售助手佣金明细表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommissionsDetailPageListItemVO extends BaseDTO {


	/**
	 * 佣金记录id
	 */
	@ApiModelProperty(value = "佣金记录id")
	private Long commissionsId;

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
	 * 买家名称
	 */
	@ApiModelProperty(value = "买家名称")
	private String buyerEname;

	/**
	 * 下单时间
	 */
	@ApiModelProperty(value = "下单时间")
	private Date createTime;

    /**
     * 拉新企业id
     */
    @ApiModelProperty(value = "拉新企业id",hidden = true)
    @JsonIgnore
    private Long newEntId;

    /**
     * 拉新企业名称
     */
    @ApiModelProperty(value = "拉新企业名称")
    private String newEntName;

    /**
     * 拉新企业联系人
     */
    @ApiModelProperty(value = "拉新企业联系人")
    private String contactor;

    /**
     * 拉新用户id
     */
    @ApiModelProperty(value = "拉新用户id",hidden = true)
    @JsonIgnore
    private Long newUserId;

    /**
     * 拉新用户名
     */
    @ApiModelProperty(value = "拉新用户名")
    private String newUserName;

    /**
     * 拉新用户电话
     */
    @ApiModelProperty(value = "拉新用户电话")
    private String newUserMobile;

    /**
     * 购买会员企业名称
     */
    @ApiModelProperty(value = "购买会员企业名称")
    private String buyMemberName;

    /**
     * 拉新时间
     */
    @ApiModelProperty(value = "拉新&购买会员时间时间")
    private Date newTime;

	/**
	 * 佣金金额
	 */
	@ApiModelProperty(value = "佣金金额")
	private BigDecimal subAmount;

    /**
     * 第一次资料上传时间
     */
    @ApiModelProperty(value = "第一次资料上传时间")
    private Date firstUploadTime;


    public BigDecimal getSubAmount() {
        BigDecimal result=subAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result;
    }
}
