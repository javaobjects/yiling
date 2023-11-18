package com.yiling.settlement.b2b.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySettOrderDataPageListRequest extends QueryPageListRequest {

    /**
     * 签收开始时间
     */
    private Date startReceiveTime;

    /**
     * 签收结束时间
     */
    private Date endReceiveTime;

    /**
     * 取消订单开始时间
     */
    private Date startCancelTime;

    /**
     * 取消订单结束时间
     */
    private Date endCancelTime;

    /**
     * 预售违约状态 1-履约 2-违约
     */
    private Integer defaultStatus;

    /**
     * 结算单生成状态：1-未生成 2-已生成
     */
    private Integer generateStatus;

    /**
     * 是否可生成结算单：1-不可生成 2-可生成
     */
    private Integer dataStatus;

    /**
     * 当前订单数据同步状态：1-成功 2-失败
     */
    private Integer syncStatus;

    /**
     * 订单状态
     */
    private List<Integer> orderStatusList;

    /**
     * 卖家企业ID
     */
    private Long sellerEid;
}
