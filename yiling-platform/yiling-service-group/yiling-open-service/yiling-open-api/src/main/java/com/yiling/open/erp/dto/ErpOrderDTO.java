package com.yiling.open.erp.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author shuan
 */
@Data
public class ErpOrderDTO implements java.io.Serializable {
    /**
     * 订单ID
     */
    @JSONField(name = "order_id", ordinal = 20)
    private Long                    ordeId;

    /**
     * 订单编码
     */
    @JSONField(name = "third_code", ordinal = 20)
    private String                  thirdCode;

    /**
     * 订单编码
     */
    @JSONField(name = "order_no", ordinal = 20)
    private String                  orderNo;

    /**
     * pop订单编码
     */
    @JSONField(name = "order_sn", ordinal = 20)
    private String                  orderSn;

    /**
     * 客户内码
     */
    @JSONField(name = "enterprise_inner_code", ordinal = 20)
    @NotBlank(message = "不能为空")
    private String                  enterpriseInnerCode;

    /**
     * 客户名称
     */
    @JSONField(name = "enterprise_name", ordinal = 20)
    private String                  enterpriseName;

    /**
     * 销售组织编码
     */
    @JSONField(name = "seller_inner_code", ordinal = 20)
    private String                  sellerInnerCode;

    /**
     * 销售员内码编码
     */
    @JSONField(name = "sale_inner_code", ordinal = 20)
    private String                  saleInnerCode;

    /**
     * 订单时间
     */
    @JSONField(name = "order_date", format = "yyyy-MM-dd HH:mm:ss", ordinal = 22)
    private Date                    orderDate;

    /**
     * 收货人电话号码
     */
    @JSONField(name = "receive_mobile", ordinal = 20)
    private String                  receiveMobile;

    /**
     * 收货人名称
     */
    @JSONField(name = "receive_user", ordinal = 20)
    private String                  receiveUser;

    /**
     * 收货人地址
     */
    @JSONField(name = "receive_address", ordinal = 20)
    private String                  receiveAddress;

    /**
     * 订单状态 默认为0
     */
    @JSONField(name = "status", ordinal = 20)
    private Integer                 status;

    /**
     * 状态更新时间
     */
    @JSONField(name = "status_time", format = "yyyy-MM-dd HH:mm:ss", ordinal = 22)
    private Date                    statusTime;

    /**
     * 支付方式
     */
    @JSONField(name = "payment_method", ordinal = 20)
    private Integer                 paymentMethod;

    /**
     * 支付状态
     */
    @JSONField(name = "payment_status", ordinal = 20)
    private Integer                 paymentStatus;

    /**
     * 备注
     */
    @JSONField(name = "remark", ordinal = 20)
    private String                  remark;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss", ordinal = 22)
    private Date                    createTime;

    /**
     * 卖家企业ID
     */
    @JSONField(name = "seller_eid", ordinal = 20)
    private Long                    sellerEid;

    /**
     * 促销金额
     */
    @JSONField(name = "promotion_price", ordinal = 20)
    private BigDecimal              promotionPrice;

    /**
     * 文件类型：1-订单销售合同 2-收货回执单
     */
    @JSONField(name = "file_type", ordinal = 20)
    private Integer fileType;

    /**
     * 文件KEY
     */
    @JSONField(name = "file_key", ordinal = 20)
    private String fileKey;

    /**
     * 订单明细
     */
    @JSONField(name = "order_detail_list", ordinal = 25)
    private List<ErpOrderDetailDTO> orderDetailsList;

}
