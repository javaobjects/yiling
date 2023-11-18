package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单列表查询request
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderPageRequest extends QueryPageListRequest {
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 1表示销售订单，2表示采购订单
     */
    private Integer type;


    /**
     * 开始下单时间
     */
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    private Date endCreateTime;

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
     * 发票状态
     */
    private Integer invoiceStatus;

    /**
     * 企业Id
     */
    private List<Long> eidList;

    /**
     * 用户id
     */
    private Long userId;


    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 商务联系人
     */
    private List<Long> contacterIdList;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 部门Id
     */
    private Long departmentId;

    /**
     * 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
     */
    private Integer departmentType;

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
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private List<Integer> paymentMethodList;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 状态 0-全部,1-代发货,2-待收货,3-账期待支付,4-已完成,5-已取消
     */
    private Integer stateType;
}
