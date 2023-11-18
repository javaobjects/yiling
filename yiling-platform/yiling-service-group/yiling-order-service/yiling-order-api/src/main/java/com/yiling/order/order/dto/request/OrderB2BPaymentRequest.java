package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;

/**
 * B2B移动端订单账期还款和待还款入参
 * @author:wei.wang
 * @date:2021/10/22
 */
@Data
public class OrderB2BPaymentRequest extends QueryPageListRequest {

    /**
     * 订单单号和供应商名称
     */
    private String condition;

    /**
     * 还款状态 ：1-待还款 2-已还款
     */
    private List<Integer> statusList;

    /**
     * 还款状态 ：1-待还款 2-已还款
     */
    private Integer status;

    /**
     * 采购商id
     */
    private Long buyerEid;

    /**
     *授信主体
     */
    private Long sellerEid;

}
