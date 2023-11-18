package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderSyncPageListRequest extends QueryPageListRequest {

    /**
     * idList
     */
    private List<Long> idList;

    /**
     * 卖家eid
     */
    private List<Long> sellerEidList;

    /**
     * 开始签收时间
     */
    private Date startReceiveTime;

    /**
     * 结束签收时间
     */
    private Date endReceiveTime;

    /**
     * 报表计算状态：1-未计算 2-已计算
     */
    private Integer reportSettStatus;

    /**
     * 报表状态
     */
    private List<Integer> reportStatusList;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 是否过滤掉无效订单(1:过滤)
     */
    private Integer filterInvalidOrder;
}
