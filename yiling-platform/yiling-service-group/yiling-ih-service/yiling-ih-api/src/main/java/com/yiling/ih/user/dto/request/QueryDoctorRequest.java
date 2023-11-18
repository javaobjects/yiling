package com.yiling.ih.user.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryDoctorRequest extends QueryPageListRequest {

    private String doctorName;

    private Long doctorId;
}
