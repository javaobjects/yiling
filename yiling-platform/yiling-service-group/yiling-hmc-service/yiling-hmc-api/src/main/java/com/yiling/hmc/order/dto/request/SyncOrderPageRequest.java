package com.yiling.hmc.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/7/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SyncOrderPageRequest extends QueryPageListRequest {

    /**
     * 保险提供服务商id
     */
    private Long insuranceCompanyId;

    /**
     * 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
     */
    private Integer orderStatus;

    /**
     * 开方状态 1-已开，2-未开
     */
    private Integer prescriptionStatus;

    /**
     * 与保司数据同步状态:1-待同步/2-已同步/3-同步失败（异常）
     */
    private List<Integer> synchronousTypeList;
}
