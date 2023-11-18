package com.yiling.ih.user.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryActivityDoctorListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -544667472485532361L;

    private Long doctorId;

    private String name;

    private Integer hospitalId;

    private Integer activityId;
}
