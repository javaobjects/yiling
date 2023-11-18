package com.yiling.f2b.admin.order.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询发票管理列表
 * @author:wei.wang
 * @date:2021/7/6
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryInvoicePageForm extends QueryPageListRequest {

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "买家名称")
    private String buyerEname;

    /**
     * 销售主题id
     */
    @ApiModelProperty(value = "销售主题id")
    private Long distributorEid;

    /**
     * 开始发票申请时间
     */
    @ApiModelProperty(value = "开始发票申请时间")
    private Date startInvoiceTime;

    /**
     * 结束发票申请时间
     */
    @ApiModelProperty(value = "结束发票申请时间")
    private Date endInvoiceTime;

    /**
     * 开票状态：1-待申请 2-部分申请  4-部分开票 3-已开票 5-已作废
     */
    @ApiModelProperty(value = "开票状态：1-待申请 2-部分申请  4-部分开票 3-已开票 5-已作废")
    private Integer invoiceStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 订单单号
     */
    @ApiModelProperty(value = "订单单号")
    private String orderNo;

    /**
     * 票单号
     */
    @ApiModelProperty(value = "票单号")
    private String invoiceNo;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     *订单Id
     */
    @ApiModelProperty(value = "订单Id")
    private Long id;

    /**
     * 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
     */
    @ApiModelProperty("部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门")
    private Integer departmentType;

    /**
     * 开始下单申请时间
     */
    @ApiModelProperty(value = "开始下单申请时间")
    private Date startOrderTime;

    /**
     * 结束下单时间
     */
    @ApiModelProperty(value = "结束下单时间")
    private Date endOrderTime;

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
}
