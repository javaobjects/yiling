package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;

/**
 * 查询标签 Form
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Data
public class QueryTagsRequest extends QueryPageListRequest {

    /**
     * 名称
     */
    private String name;

}
