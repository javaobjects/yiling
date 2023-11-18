package com.yiling.ih.dept.dto;


import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author gxl
 * @date 2022-6-10
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class HospitalDeptListDTO extends BaseDTO {

    private static final long serialVersionUID = 3623687645821754777L;

    private String label;


}
