package com.yiling.hmc.admin.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2023/03/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMarketOrderPageForm extends QueryPageListForm {


    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("收货人姓名")
    private String name;

    @ApiModelProperty("收货人手机号")
    private String mobile;

    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    @ApiModelProperty("下单开始时间")
    private Date beginTime;

    @ApiModelProperty("下单结束时间")
    private Date endTime;

    @ApiModelProperty("IH 处方编号")
    private String ihPrescriptionNo;

    @ApiModelProperty("支付状态:1-未支付，2-已支付,3-已全部退款")
    private Integer paymentStatus;

    @ApiModelProperty("处方类型 1：西药 0：中药")
    private Integer prescriptionType;
}
