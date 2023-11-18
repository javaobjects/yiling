package com.yiling.user.esb.bo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ESB业务架构结构树 BO
 *
 * @author: lun.yu
 * @date: 2023-04-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsbBusinessOrgTreeBO implements java.io.Serializable {

    private static final long serialVersionUID = -5333901645817387960L;

    /**
     * ID
     */
    private Long id;

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
     * 上级业务架构ID
     */
    private Long bzPid;

    /**
     * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
     */
    private Integer tagType;

    /**
     * 是否可以上传指标：0-否 1-是
     */
    private Integer targetStatus;

    /**
     * 下级业务架构
     */
    private List<EsbBusinessOrgTreeBO> children;

}