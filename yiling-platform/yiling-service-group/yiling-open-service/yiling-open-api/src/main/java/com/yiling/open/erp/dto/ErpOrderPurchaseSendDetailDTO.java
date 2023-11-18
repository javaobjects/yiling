package com.yiling.open.erp.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 采购订单明细
 *
 * @author shuan
 */
@Data
public class ErpOrderPurchaseSendDetailDTO implements java.io.Serializable {
    /**
     * 主键Id
     */
    @JSONField(name = "id", ordinal = 1)
    private Integer id;

    /**
     * 订单ID
     */
    @JSONField(name = "order_id", ordinal = 5)
    private Long orderId;

    /**
     * 订单编号
     */
    @JSONField(name = "order_no", ordinal = 5)
    private String orderNo;

    /**
     * 订单明细ID
     */
    @JSONField(name = "order_detail_id", ordinal = 5)
    private Long orderDetailId;

    /**
     * 商品内码
     */
    @JSONField(name = "goods_in_sn", ordinal = 5)
    @NotBlank(message = "不能为空")
    private String goodsInSn;

    /**
     * 商品名称
     */
    @JSONField(name = "goods_name", ordinal = 5)
    private String goodsName;

    /**
     * 发货数量
     */
    @JSONField(name = "send_number", ordinal = 5)
    private Long sendNumber;

    /**
     * 商品单价
     */
    @JSONField(name = "goods_price", ordinal = 5)
    private BigDecimal goodsPrice;

    /**
     * 商品小计
     */
    @JSONField(name = "goods_amount", ordinal = 5)
    private BigDecimal goodsAmount;

     /**
     * 商品批次
     */
    @JSONField(name = "send_batch_no", ordinal = 5)
    private String sendBatchNo;

    /**
     * 有效期
     */
    @JSONField(name = "effective_time",  format = "yyyy-MM-dd HH:mm:ss",ordinal = 5)
    private Date effectiveTime;

    /**
     * 生产时间
     */
    @JSONField(name = "product_time", format = "yyyy-MM-dd HH:mm:ss",ordinal = 5)
    private Date productTime;

    /**
     * 出库单号
     */
    @JSONField(name = "delivery_number", ordinal = 5)
    private String deliveryNumber;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss", ordinal = 22)
    private Date createTime;
}
