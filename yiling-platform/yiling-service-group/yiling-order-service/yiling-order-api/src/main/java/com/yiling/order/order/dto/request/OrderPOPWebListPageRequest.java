package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;


import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;

@Data
public class OrderPOPWebListPageRequest extends QueryPageListRequest {
    /**
     * 供应商名称
     */

    private String sellerEname;

    /**
     * 订单编号
     */

    private String orderNo;

    /**
     *订单ID
     */

    private Long id;

    /**
     * 下单开始时间
     */

    private Date startCreateTime;

    /**
     * 下单开始时间
     */

    private Date endCreateTime;

    /**
     * 类型 1-未提交,2-待审核,3-待发货,4-部分发货,5-已发货,6-已收货,7-已取消,8-已驳回
     */
    private Integer type;

    /**
     * 采购商企业Id
     */
    private List<Long> buyerEidList;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;
}
