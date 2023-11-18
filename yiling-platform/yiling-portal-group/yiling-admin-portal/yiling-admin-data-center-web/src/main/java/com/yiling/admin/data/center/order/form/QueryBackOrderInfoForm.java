package com.yiling.admin.data.center.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单列表Form
 * @author:wei.wang
 * @date:2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBackOrderInfoForm extends QueryPageListForm {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "买家名称")
    private String buyerEname;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

    /**
     * 开始下单时间
     */
    @ApiModelProperty(value = "开始下单时间")
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    @ApiModelProperty(value = "结束下单时间")
    private Date endCreateTime;

    /**
     * 发货开始时间
     */
    @ApiModelProperty(value = "发货开始时间")
    private Date  startDeliveryTime;

    /**
     * 发货结束时间
     */
    @ApiModelProperty(value = "发货结束时间")
    private Date  endDeliveryTime;


    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款 4-在线支付")
    private Integer paymentMethod;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;

    @ApiModelProperty(value = "发票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废")
    private Integer invoiceStatus;

    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    /**
     *所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    @ApiModelProperty("订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台")
    private Integer orderSource;

    @ApiModelProperty("活动类型： 2-特价 3-秒杀")
    private Integer activityType;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    @ApiModelProperty("订单类型：1-POP订单,2-B2B订单")
    private Integer orderType;
}
