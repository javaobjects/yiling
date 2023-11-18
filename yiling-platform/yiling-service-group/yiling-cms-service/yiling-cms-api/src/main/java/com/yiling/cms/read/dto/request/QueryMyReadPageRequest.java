package com.yiling.cms.read.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMyReadPageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 835353452775514063L;
    /**
     * 标题
     */
    private String title;

    /**
     * 阅读来源
     */
    private Integer source;

}