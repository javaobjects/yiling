package com.yiling.open.erp.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/7/22
 */
@Data
public class ErpOrderBillDTO implements java.io.Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * pop平台的应收单明细Id
     */
    @JSONField(name = "order_bill_id", ordinal = 20)
    private String orderBillId;

    /**
     * 订单ID
     */
    @JSONField(name = "order_id", ordinal = 20)
    private Long ordeId;

    /**
     * pop订单明细ID
     *
     * @return
     */
    @JSONField(name = "order_detail_id", ordinal = 20)
    private Long orderDetailId;

    /**
     * 商品内码
     *
     * @return
     */
    @JSONField(name = "goods_in_sn", ordinal = 20)
    private String goodsInSn;

    /**
     * 折扣率
     *
     * @return
     */
    @JSONField(name = "discount_rate", ordinal = 20)
    private BigDecimal discountRate;

    /**
     * 折扣金额
     *
     * @return
     */
    @JSONField(name = "discount_amount", ordinal = 20)
    private BigDecimal discountAmount;

    /**
     * eas出库单号
     *
     * @return
     */
    @JSONField(name = "eas_delivery_number", ordinal = 20)
    private String easDeliveryNumber;

    /**
     * 批次号
     *
     * @return
     */
    @JSONField(name = "batch_no", ordinal = 20)
    private String batchNo;

    /**
     * 电子邮箱
     *
     * @return
     */
    @JSONField(name = "email", ordinal = 20)
    private String email;

    /**
     * 终端号
     *
     * @return
     */
    @JSONField(name = "fnumber", ordinal = 20)
    private String fnumber;

    /**
     * 终端号
     *
     * @return
     */
    @JSONField(name = "bill_remark", ordinal = 20)
    private String billRemark;

    /**
     * eas出库单主键
     *
     * @return
     */
    @JSONField(name = "eas_send_order_id", ordinal = 20)
    private String easSendOrderId;

    /**
     * eas应收单号
     *
     * @return
     */
    @JSONField(name = "eas_bill_number", ordinal = 20)
    private String easBillNumber;

    /**
     * 开票类型编码
     *
     * @return
     */
    @JSONField(name = "ticket_type", ordinal = 20)
    private String ticketType;

    /**
     * 开票数量
     *
     * @return
     */
    @JSONField(name = "bill_quantity", ordinal = 20)
    private Integer billQuantity;

    /**
     * 读取状态
     *
     * @return
     */
    @JSONField(name = "status", ordinal = 20)
    private Integer status;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss", ordinal = 22)
    private Date createTime;
}
