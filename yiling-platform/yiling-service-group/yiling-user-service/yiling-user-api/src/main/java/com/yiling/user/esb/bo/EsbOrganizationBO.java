package com.yiling.user.esb.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * esb组织架构 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-04-12
 */
@Data
@Accessors(chain = true)
public class EsbOrganizationBO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 是否失效：0-正常，其他失效
     */
    private Integer state;

    /**
     * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
     */
    private Integer tagType;
}
