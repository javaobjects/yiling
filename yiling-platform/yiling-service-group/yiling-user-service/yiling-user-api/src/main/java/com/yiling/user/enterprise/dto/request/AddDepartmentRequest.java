package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询企业部门信息 request
 * </p>
 *
 * @author yeucheng.chen
 * @date 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddDepartmentRequest extends BaseRequest {

    private static final long serialVersionUID = -8562749617522022611L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 部门编码
     */
    private String code;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门描述
     */
    private String description;

    /**
     * 上级部门ID
     */
    private Long parentId;

    /**
     * 部门负责人ID
     */
    private Long managerId;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
