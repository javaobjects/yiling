package com.yiling.basic.article.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ArticlePageQueryRequest extends QueryPageListRequest {

    /**
     * 文章标题
     */
    private String  articleTitle;

    /**
     * 文章状态
     */
    private Integer articleStatus;

}
