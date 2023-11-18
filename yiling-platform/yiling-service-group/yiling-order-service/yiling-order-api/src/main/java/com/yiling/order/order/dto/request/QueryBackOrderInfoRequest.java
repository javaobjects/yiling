package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单列表Request
 * @author:wei.wang
 * @date:2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBackOrderInfoRequest extends QueryPageListRequest {
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 卖家Id
     */
    private Long sellerEid;

    /**
     * 开始下单时间
     */
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    private Date endCreateTime;

    /**
     * 发货开始时间
     */
    private Date  startDeliveryTime;

    /**
     * 发货结束时间
     */
    private Date  endDeliveryTime;


    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private Integer paymentMethod;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    private Integer invoiceStatus;

    /**
     *买家eid
     */
    private List<Long> buyerEid;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     *所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 开始签收时间
     */
    private Date startReceiveTime;

    /**
     * 介绍签收时间
     */
    private Date endReceiveTime;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 订单状态合集
     */
    private List<Integer> orderStatusList;

    /**
     * 活动类型
     */
    private Integer activityType;

    /**
     * 商务联系人
     */
    private List<Long> contacterIdList;

}
