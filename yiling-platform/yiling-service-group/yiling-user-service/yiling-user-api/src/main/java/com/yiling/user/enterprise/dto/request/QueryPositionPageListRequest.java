package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询职位分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPositionPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 职位状态：0-全部 1-启用 2-停用
     */
    private Integer status;
}
