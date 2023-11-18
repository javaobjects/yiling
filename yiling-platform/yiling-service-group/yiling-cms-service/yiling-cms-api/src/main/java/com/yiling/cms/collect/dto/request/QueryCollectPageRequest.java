package com.yiling.cms.collect.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 收藏
 * @author: gxl
 * @date: 2022/7/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCollectPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -6026782659425887179L;
    /**
     * 标题
     */
    private String title;

    /**
     * 阅读来源
     */
    private Integer source;

}