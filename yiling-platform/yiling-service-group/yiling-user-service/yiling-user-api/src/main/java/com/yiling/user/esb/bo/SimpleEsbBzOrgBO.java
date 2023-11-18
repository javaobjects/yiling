package com.yiling.user.esb.bo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 简单的esb业务架构 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-04-14
 */
@Data
@Accessors(chain = true)
public class SimpleEsbBzOrgBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long orgId;

    /**
     * 部门名称
     */
    private String orgName;

}
