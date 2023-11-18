package com.yiling.f2b.admin.enterprise.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账期使用订单信息
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Data
@Accessors(chain = true)
public class PaymentDaysOrderVO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;
    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String ename;
    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long eid;
    /**
     * 采购商id
     */
    @ApiModelProperty("采购商id")
    private Long customerEid;
    /**
     * 采购商名称
     */
    @ApiModelProperty("采购商名称")
    private String customerName;

    /**
     * 账期账户ID
     */
    @ApiModelProperty("账期账户ID")
    private Long accountId;

    /**
     * 账期（天）
     */
    @ApiModelProperty("账期")
    private Integer period;

    /**
     * 使用金额
     */
    @ApiModelProperty("使用金额")
    private BigDecimal usedAmount;

    /**
     * 使用时间
     */
    @ApiModelProperty("使用时间")
    private Date usedTime;
    /**
     * 退款金额
     */
    @ApiModelProperty("退款金额")
    private BigDecimal returnAmount;

    /**
     * 到期时间
     */
    @ApiModelProperty("到期时间")
    private Date expirationTime;

    /**
     * 还款状态：1-未还款 2-部分还款 3-全部还款
     */
    @ApiModelProperty("还款状态：1-未还款 2-部分还款 3-全部还款")
    private Integer repaymentStatus;

    /**
     * 已还款金额
     */
    @ApiModelProperty("已还款金额")
    private BigDecimal repaymentAmount;

    /**
     * 待还款金额
     */
    @ApiModelProperty("待还款金额")
    private BigDecimal needRepaymentAmount;

    /**
     * 还款时间
     */
    @ApiModelProperty("还款时间")
    private Date repaymentTime;

    /**
     * 是否删除：0-否 1-是
     */
    @ApiModelProperty("是否删除：0-否 1-是")
    private Integer delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


}
