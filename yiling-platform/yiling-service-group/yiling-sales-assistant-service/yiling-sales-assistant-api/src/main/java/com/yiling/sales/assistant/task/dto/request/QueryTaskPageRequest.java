package com.yiling.sales.assistant.task.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskPageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = -7276211266529905362L;
    private Integer taskType;

    private String taskName;


    private String provinceCode;

    private String cityCode;

    private String regionCode;

    private Date startTime;

    private Date endTime;


    private Date starteTime;

    private Date endeTime;

    private Date startcTime;

    private Date endcTime;

    private Integer taskStatus;

    private String createdBy;

    private Integer finishType;

    List<Long> taskIds;
}