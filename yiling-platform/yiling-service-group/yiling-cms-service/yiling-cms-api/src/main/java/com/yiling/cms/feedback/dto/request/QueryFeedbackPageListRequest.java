package com.yiling.cms.feedback.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFeedbackPageListRequest extends QueryPageListRequest {
    private static final long serialVersionUID = -5841674923123369072L;
    private Date startTime;

    private Date endTime;

    private Integer source;
}