package com.yiling.hmc.usercenter.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
public class OrderVO extends BaseVO {

    @ApiModelProperty("平台订单编号")
    private String orderNo;

    @ApiModelProperty("购买保险id")
    private Long insuranceRecordId;

    @ApiModelProperty("保险单号")
    private String policyNo;

    @ApiModelProperty("第三方兑保编号")
    private String thirdConfirmNo;

    @ApiModelProperty("保险公司id")
    private Long insuranceCompanyId;

    @ApiModelProperty("订单状态:1-待确认/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退")
    private Integer orderStatus;

    @ApiModelProperty("药品服务终端id")
    private Long eid;

    @ApiModelProperty("药品服务终端名称")
    private String ename;

    @ApiModelProperty("支付方式:1-保险理赔结算")
    private Integer paymentMethod;

    @ApiModelProperty("支付状态:1-未支付/2-已支付/3-已退款/4-部分退款")
    private Integer paymentStatus;

    @ApiModelProperty("支付时间")
    private Date paymentTime;

    @ApiModelProperty("下单人")
    private Long orderUser;

    @ApiModelProperty("下单时间")
    private Date orderTime;

    @ApiModelProperty("完成时间")
    private Date finishTime;

    @ApiModelProperty("订单类型:1-其他/2-虚拟商品订单/3-普通商品/4-药品订单")
    private Integer orderType;

    @ApiModelProperty("是否为保险理赔兑付单1-是 2-否")
    private Integer isInsuranceOrder;

    @ApiModelProperty("订单额总额")
    private BigDecimal totalAmount;

    @ApiModelProperty("与保司数据同步状态:1-待同步/2-已同步/3-同步失败（异常）")
    private Integer synchronousType;

    @ApiModelProperty("处方id")
    private Long prescriptionId;

    @ApiModelProperty("配送方式 1-自提 2-物流")
    private Integer deliverType;

    /**
     * 条形码路径
     */
    @ApiModelProperty("条形码路径")
    private String barCodeUrl;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 维护身份证手写签名标签 true-已维护，false-未维护
     */
    @ApiModelProperty("维护身份证手写签名标签 true-已维护，false-未维护")
    private Boolean hasMaintainFlag;



}
