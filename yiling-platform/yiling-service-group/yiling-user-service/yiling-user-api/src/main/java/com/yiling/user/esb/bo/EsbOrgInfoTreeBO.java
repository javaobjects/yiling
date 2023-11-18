package com.yiling.user.esb.bo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ESB组织架构结构树 BO
 *
 * @author: lun.yu
 * @date: 2023-04-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsbOrgInfoTreeBO implements java.io.Serializable {

    private static final long serialVersionUID = -5333901645817387960L;

    /**
     * 部门ID
     */
    private Long orgId;

    /**
     * 部门名称
     */
    private String orgName;

    /**
     * 上级部门ID
     */
    private Long orgPid;

    /**
     * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
     */
    private Integer tagType;

    /**
     * 是否失效：0-正常，其他失效
     */
    private Integer state;

    /**
     * 当前节点禁用标志：true禁用，false启用
     */
    private Boolean disabled;

    /**
     * 下级部门
     */
    private List<EsbOrgInfoTreeBO> children;

}