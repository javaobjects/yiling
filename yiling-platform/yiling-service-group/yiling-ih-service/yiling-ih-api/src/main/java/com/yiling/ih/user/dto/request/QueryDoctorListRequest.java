package com.yiling.ih.user.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryDoctorListRequest extends QueryPageListRequest {

    private String name;

    private Integer hospitalId;

    private Integer source;

    private Date startTime;

    private Date endTime;

    private Integer activityId;

    private Long doctorId;
}
