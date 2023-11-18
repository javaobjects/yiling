package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建员工信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateEmployeeRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

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
     * 角色ID列表
     */
    private List<Long> roleIds;

    /**
     * 职务ID
     */
    private Long positionId;

    /**
     * 上级领导ID
     */
    private Long parentId;

    /**
     * 部门ID列表
     */
    private List<Long> departmentIds;

    /**
     * 是否为企业管理员：0-否 1-是
     */
    private Integer adminFlag;

    /**
     * 密码，如果为空则为默认密码
     */
    private String password;

}
