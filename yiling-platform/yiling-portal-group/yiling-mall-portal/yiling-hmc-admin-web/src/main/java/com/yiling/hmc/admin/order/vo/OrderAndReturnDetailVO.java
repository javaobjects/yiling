package com.yiling.hmc.admin.order.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Data
public class OrderAndReturnDetailVO {

    @ApiModelProperty("参保记录信息")
    private InsuranceRecordVO insuranceRecord;

    @ApiModelProperty("订单信息")
    private OrderVO order;

    @ApiModelProperty("订单明细信息")
    private List<OrderDetailVO> orderDetailList;

    @ApiModelProperty("收货人信息")
    private OrderRelateUserVO orderRelateUser;

    @ApiModelProperty("退货单信息")
    private OrderReturnVO orderReturn;

    @ApiModelProperty("退货单明细信息")
    private List<OrderReturnDetailVO> orderReturnDetailList;

    @ApiModelProperty("拿货核销码")
    private String offCode;

    @ApiModelProperty("保险服务提供商名称")
    private String insuranceCompanyName;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    @ApiModelProperty("下单用户名称")
    private String orderUserName;

    @ApiModelProperty("订单处理者id")
    private Long operateUserId;

    @ApiModelProperty("订单处理者名称")
    private String operateUserName;

    @ApiModelProperty("处方详情")
    private OrderPrescriptionDetailVO orderPrescriptionDetail;

    @ApiModelProperty("订单操作记录")
    private List<OrderOperateVO> operateList;

    @ApiModelProperty("订单票据")
    private List<OrderReceiptsVO> orderReceiptsList;

    @ApiModelProperty("患者信息")
    private String patientsName;

    @ApiModelProperty("患者手机号")
    private String patientsPhone;

    @ApiModelProperty("患者年龄")
    private Integer patientsAge;

    @ApiModelProperty("患者性别")
    private String patientsGender;
}
