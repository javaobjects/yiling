package com.yiling.user.system.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xuan.zhou
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleDataScopeDTO extends BaseDTO {

    /**
     * 应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手
     */
    private Integer appId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 数据范围：0-未定义 1-本人 2-本部门 3-本部门及下级部门 4-全部数据
     */
    private Integer dataScope;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
