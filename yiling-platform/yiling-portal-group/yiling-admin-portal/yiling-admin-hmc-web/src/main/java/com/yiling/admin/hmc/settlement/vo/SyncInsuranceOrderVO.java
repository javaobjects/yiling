package com.yiling.admin.hmc.settlement.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.admin.hmc.order.vo.OrderPrescriptionDetailVO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/4
 */
@Data
public class SyncInsuranceOrderVO extends BaseVO {

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("保险公司id")
    private Long insuranceCompanyId;

    @ApiModelProperty("保险公司名称")
    private String insuranceCompanyName;

    @ApiModelProperty("订单总额")
    private BigDecimal totalAmount;

    @ApiModelProperty("以岭计算理赔金额")
    private BigDecimal insuranceSettleAmount;

    @ApiModelProperty("保司结算理赔额")
    private BigDecimal insuranceSettleAmountTrial;

    private String orderReceipts;

    @ApiModelProperty("订单票据，图片存储地址")
    private List<String> orderReceiptsList;

    @ApiModelProperty("处方详情")
    private OrderPrescriptionDetailVO orderPrescriptionDetail;

    @ApiModelProperty("订单商商品信息")
    private List<SyncInsuranceOrderDetailVO> syncInsuranceOrderDetailList;

    @ApiModelProperty("方便前端展示同步失败原因的字段")
    private String failReason;
}
