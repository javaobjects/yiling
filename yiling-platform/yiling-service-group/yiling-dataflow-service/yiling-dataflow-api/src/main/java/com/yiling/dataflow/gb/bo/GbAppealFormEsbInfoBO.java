package com.yiling.dataflow.gb.bo;

import java.io.Serializable;

import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/19
 */
@Data
public class GbAppealFormEsbInfoBO implements Serializable {

    private static final long serialVersionUID = 7081485122565894979L;

    /**
     * esb员工信息
     */
    private EsbEmployeeDTO esbEmployeeDTO;

    /**
     * 部门
     */
    private EsbOrganizationDTO organizationDTO;

    /**
     * 省区
     */
    private String provinceArea;

}
