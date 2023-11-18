package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建订单 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateOrderRequest extends BaseRequest {

    private static final long serialVersionUID = -5808238520038868950L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单批次号
     */
    private String batchNo;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 买家所属省份编码
     */
    private String buyerProvinceCode;

    /**
     * 买家所属城市编码
     */
    private String buyerCityCode;

    /**
     * 买家所属区域编码
     */
    private String buyerRegionCode;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 卖家ERP编码
     */
    private String sellerErpCode;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商名称
     */
    private String distributorEname;

    /**
     * 客户ERP编码
     */
    private String customerErpCode;

    /**
     * 商品总金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付总金额
     */
    private BigDecimal paymentAmount;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 支付类型枚举
     */
    private Integer paymentType;

    /**
     * 支付方式Id
     */
    private Integer paymentMethod;

    /**
     * 支付状态
     */
    private Integer paymentStatus;
    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 订单类型枚举
     */
    private Integer orderType;

    /**
     * 订单类别
     */
    private Integer orderCategory;

    /**
     * 订单拆单类型
     */
    private String splitOrderType;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单来源枚举
     */
    private Integer orderSource;

    /**
     * 下单备注
     */
    private String orderNote;

    /**
     * 商务联系人ID
     */
    private Long contacterId;

    /**
     * 商务联系人姓名
     */
    private String contacterName;

    /**
     * 省区经理ID
     */
    private Long provinceManagerId;

    /**
     * 省区经理工号
     */
    private String provinceManagerCode;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 订单明细列表
     */
    private List<CreateOrderDetailRequest> orderDetailList;

    /**
     * 收货地址信息
     */
    private CreateOrderAddressRequest orderAddressInfo;

    /**
     * 预售订单扩展信息
     */
    private CreatePresaleOrderRequest createPresaleOrderRequest;

    /**
     * 购销合同文件KEY列表
     */
    private List<String> contractFileKeyList;

    /**
     * 卖家是否为以岭
     */
    private boolean yilingSellerFlag;

    /**
     * 客户确认状态
     */
    private Integer customerConfirmStatus;

    /**
     * 客户确认时间
     */
    private Date customerConfirmTime;

    /**
     * 商家优惠劵优惠总金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 商家支付优惠金额
     */
    private BigDecimal shopPaymentDiscountAmount;

    /**
     * 平台优惠劵优惠总金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 平台支付优惠总金额
     */
    private BigDecimal platformPaymentDiscountAmount;

    /**
     * 订单使用的优惠劵
     */
    private List<CreateOrderCouponUseRequest> orderCouponUseList;

    /**
     * 订单是用的赠品
     */
    private List<CreateOrderGiftRequest> orderGiftRequestList;

    /**
     * 订单促销活动信息
     */
    private List<CreateOrderPromotionActivityRequest> promotionActivityRequestList;
}
