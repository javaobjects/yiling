package com.yiling.ih.dept.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author gaoxinlei
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HospitalDeptDTO extends BaseDTO {

    private static final long serialVersionUID = 2549017362253705186L;


    private String label;

    private List<HospitalDeptDTO> children;

}
