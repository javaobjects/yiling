package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/9/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBuyerOrderDetailPageRequest extends QueryPageListRequest {

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 企业ID
     */
    private List<Long> buyerEidList;

    /**
     * 商务联系人ID
     */
    private Long contacterId;

    /**
     * 下单时间开始
     */
    private Date startCreateTime;

    /**
     * 下单时间结束
     */
    private Date endCreateTime;

    /**
     * 收货开始时间
     */
    private Date startReceiveTime;

    /**
     * 收货结束时间
     */
    private Date endReceiveTime;

    /**
     * 订单状态
     */
    private List<Integer> orderStatusList;


}
