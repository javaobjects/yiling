package com.yiling.user.enterprise.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业员工所属部门 DTO
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseEmployeeDepartmentDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long departmentId;

}
