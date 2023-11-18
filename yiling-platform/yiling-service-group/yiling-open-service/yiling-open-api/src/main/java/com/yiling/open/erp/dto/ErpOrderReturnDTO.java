package com.yiling.open.erp.dto;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/7/22
 */
@Data
public class ErpOrderReturnDTO implements java.io.Serializable{
    /**
     * 主键
     */
    private Integer id;

    /**
     * pop平台的退货明细Id
     */
    @JSONField(name = "order_return_id",ordinal = 20)
    private Long ordeReturnId;

    /**
     * eas销售单号
     */
    @JSONField(name = "erp_order_no",ordinal = 20)
    private String erpOrderNo;

    /**
     * pop订单ID
     * @return
     */
    @JSONField(name = "order_id",ordinal = 20)
    private Long orderId;

    /**
     * pop订单明细ID
     * @return
     */
    @JSONField(name = "order_detail_id",ordinal = 20)
    private Long orderDetailId;

    /**
     * 退货数量
     * @return
     */
    @JSONField(name = "return_number",ordinal = 20)
    private Long returnNumber;

    /**
     * 退货批次
     * @return
     */
    @JSONField(name = "batch_no",ordinal = 20)
    private String batchNo;

    /**
     * 商品内码
     * @return
     */
    @JSONField(name = "goods_in_sn",ordinal = 20)
    private String  goodsInSn;

    /**
     * 客户内码
     * @return
     */
    @JSONField(name = "enterprise_inner_code",ordinal = 20)
    private String   enterpriseInnerCode;

    /**
     * 销售组织编码
     * @return
     */
    @JSONField(name = "seller_inner_code",ordinal = 20)
    private String  sellerInnerCode;

    /**
     * 销售员内码编码
     * @return
     */
    @JSONField(name = "sale_inner_code",ordinal = 20)
    private String  saleInnerCode;

    /**
     * 读取状态
     * @return
     */
    @JSONField(name = "status",ordinal = 20)
    private Integer  status;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time",format="yyyy-MM-dd HH:mm:ss",ordinal = 22)
    private Date createTime;

    /**
     * eas退货接口
     * @return
     */
    @JSONField(name = "eas_delivery_number",ordinal = 20)
    private String  easDeliveryNumber;

    /**
     * eas出库单主键
     *
     * @return
     */
    @JSONField(name = "eas_send_order_id", ordinal = 20)
    private String easSendOrderId;
}
