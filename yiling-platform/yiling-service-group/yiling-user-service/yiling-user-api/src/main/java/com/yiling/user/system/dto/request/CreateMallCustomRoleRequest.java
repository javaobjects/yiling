package com.yiling.user.system.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建商城自定义角色 Request
 *
 * @author: xuan.zhou
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateMallCustomRoleRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 角色名称
     */
    @NotEmpty
    @Length(max = 20)
    private String name;

    /**
     * 角色描述
     */
    @Length(max = 100)
    private String remark;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @Range(min = 1, max = 2)
    private Integer status;

    /**
     * 子系统菜单及数据权限列表
     */
    private List<RolePermissionRequest> rolePermissionList;

    @Data
    public static class RolePermissionRequest implements java.io.Serializable {

        /**
         * 应用ID：2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手
         */
        private Integer appId;

        /**
         * 菜单ID集合
         */
        private List<Long> menuIds;

        /**
         * 数据范围：0-未定义 1-本人 2-本部门 3-本部门及下级部门 4-全部数据
         */
        private Integer dataScope;
    }
}
