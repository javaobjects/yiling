package com.yiling.admin.b2b.member.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员退款审核列表 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Data
@ApiModel("会员退款审核列表VO")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberReturnVO extends BaseVO {

    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String orderNo;

    /**
     * 购买企业ID
     */
    @ApiModelProperty("购买企业ID")
    private Long eid;

    /**
     * 购买企业名称
     */
    @ApiModelProperty("购买企业名称")
    private String ename;

    /**
     * 支付方式名称
     */
    @ApiModelProperty("支付方式名称")
    private String payMethodName;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")
    private Date payTime;

    /**
     * 申请人名称
     */
    @ApiModelProperty("申请人名称")
    private String applyUserName;

    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")
    private Date applyTime;

    /**
     * 退款原因
     */
    @ApiModelProperty("退款原因")
    private String returnReason;

    /**
     * 退款备注
     */
    @ApiModelProperty("退款备注")
    private String returnRemark;

    /**
     * 审核状态：1-待审核 2-已审核 3-已驳回
     */
    @ApiModelProperty("审核状态：1-待审核 2-已审核 3-已驳回")
    private Integer authStatus;

    /**
     * 退款金额
     */
    @ApiModelProperty("退款金额")
    private BigDecimal returnAmount;

    /**
     * 退款状态：1-待退款 2-退款中 3-退款成功 4-退款失败
     */
    @ApiModelProperty("退款状态：1-待退款 2-退款中 3-退款成功 4-退款失败")
    private Integer returnStatus;

    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    private String updateUserName;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private Date updateTime;

}
