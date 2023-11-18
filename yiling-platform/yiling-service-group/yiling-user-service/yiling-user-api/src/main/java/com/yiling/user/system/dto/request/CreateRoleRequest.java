package com.yiling.user.system.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建角色 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateRoleRequest extends BaseRequest {

    /**
     * 应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手
     */
    private Integer appId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色类型：1-系统角色 2-自定义角色
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 菜单ID集合
     */
    private List<Long> menuIds;

}
