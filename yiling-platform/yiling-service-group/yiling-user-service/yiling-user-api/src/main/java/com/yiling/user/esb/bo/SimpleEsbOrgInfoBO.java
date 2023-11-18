package com.yiling.user.esb.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单ESB机构信息 BO
 *
 * @author: xuan.zhou
 * @date: 2023/3/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEsbOrgInfoBO implements java.io.Serializable {

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
     * 上级部门信息
     */
    private SimpleEsbOrgInfoBO parentOrgInfo;

}