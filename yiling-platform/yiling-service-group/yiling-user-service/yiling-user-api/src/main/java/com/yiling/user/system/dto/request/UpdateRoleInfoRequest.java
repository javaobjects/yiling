package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改角色信息 Request
 *
 * @author: lun.yu
 * @date: 2021/8/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRoleInfoRequest extends BaseRequest {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色类型：1-系统角色 2-自定义角色
     */
    private Integer type;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
