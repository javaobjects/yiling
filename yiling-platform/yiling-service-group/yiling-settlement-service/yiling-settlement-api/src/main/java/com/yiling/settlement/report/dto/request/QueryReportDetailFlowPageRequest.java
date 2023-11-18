package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportDetailFlowPageRequest extends QueryPageListRequest {

    /**
     * 报表id
     */
    private List<Long> reportIdList;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 开始销售时间
     */
    private Date startSoTime;

    /**
     * 结束销售时间
     */
    private Date endSoTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;


}
