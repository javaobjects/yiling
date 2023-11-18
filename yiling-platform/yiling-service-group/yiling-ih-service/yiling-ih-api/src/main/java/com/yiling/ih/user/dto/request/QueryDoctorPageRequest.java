package com.yiling.ih.user.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: fan.shen
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryDoctorPageRequest extends QueryPageListRequest {

    /**
     * 医生名称
     */
    private String doctorName;

    /**
     * 医院名称
     */
    private String hospitalName;
}
