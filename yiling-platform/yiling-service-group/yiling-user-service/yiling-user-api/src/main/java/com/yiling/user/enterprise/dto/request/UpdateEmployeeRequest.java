package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改员工信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEmployeeRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 工号
     */
    private String code;

    /**
     * 员工类型
     */
    private Integer type;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID列表
     */
    private List<Long> departmentIds;

    /**
     * 上级领导ID
     */
    private Long parentId;
}
