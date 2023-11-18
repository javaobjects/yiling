package com.yiling.settlement.report.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryReportParPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 3240707594623719966L;
}