package com.yiling.dataflow.report.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowWashSaleReportRequest extends BaseRequest {
    /**
     * 计入年份
     */
    private String year;

    /**
     * 计入月份
     */
    private String month;

    /**
     * 匹配状态
     */
    private Integer mappingStatus;

    /**
     * 流向是否锁定 1:是 2否
     */
    private Integer isLockFlag;

    /**
     * 客户eid
      */
    private Long customerEid;
}
