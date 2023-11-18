package com.yiling.basic.article.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class ArticleSaveOrUpdateRequest extends BaseRequest {

    /**
     * 文章ID
     */
    private Long    id;

    /**
     * 文章标题
     */
    private String  articleTitle;

    /**
     * 文章描述
     */
    private String  articleDesc;

    /**
     * 文章内容
     */
    private String  articleContent;

    /**
     * 文章状态
     */
    private Integer articleStatus;

}
