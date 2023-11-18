package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询企业部门信息 request
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDepartmentPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;
}
