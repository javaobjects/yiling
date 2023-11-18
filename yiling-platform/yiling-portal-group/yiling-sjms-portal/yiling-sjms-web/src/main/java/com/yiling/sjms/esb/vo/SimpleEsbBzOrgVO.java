package com.yiling.sjms.esb.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 简单的esb业务架构 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-04-17
 */
@Data
@Accessors(chain = true)
public class SimpleEsbBzOrgVO implements Serializable {

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
