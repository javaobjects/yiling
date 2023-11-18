package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询买家已收货订单信息
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/9/28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBuyerReceiveOrderPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID=-8180687184486208706L;

    /**
     * 买家Eid
     */
    private List<Long> buyerEidList;

    /**
     * 是否按照，下单时间升序排序，如果不传或者false,默认按照下单时间降序排序
     */
    private Boolean isAscByCreateTime = false;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单时间开始
     */
    private Date startCreateTime;

    /**
     * 下单时间结束
     */
    private Date endCreateTime;

}
