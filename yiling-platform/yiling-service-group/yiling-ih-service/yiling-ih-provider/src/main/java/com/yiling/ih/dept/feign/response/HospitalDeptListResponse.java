package com.yiling.ih.dept.feign.response;


import com.yiling.ih.common.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class HospitalDeptListResponse extends BaseResponse {

    private static final long serialVersionUID = 3623687645821754777L;
    private Long id;

    private String departmentName;


}
