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
public class CreateReportB2bRequest extends QueryPageListRequest {

    /**
     * 卖家eid
     */
    private Long sellerEid;

    /**
     * 报表状态
     */
    private List<Integer> reportStatusList;

    /**
     * 收货时间
     */
    private Date startReceiveTime;

    /**
     * 收货时间
     */
    private Date endReceiveTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

}
