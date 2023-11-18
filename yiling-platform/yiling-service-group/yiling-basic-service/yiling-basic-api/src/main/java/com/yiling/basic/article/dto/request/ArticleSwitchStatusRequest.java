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
public class ArticleSwitchStatusRequest extends BaseRequest {

    /**
     * 文章ID
     */
    private Long    id;

    /**
     * 文章状态
     */
    private Integer articleStatus;

}
