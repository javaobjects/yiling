package com.yiling.search.word.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryEsWordPageListRequest
 * @描述
 * @创建时间 2022/4/26
 * @修改人 shichen
 * @修改时间 2022/4/26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEsWordPageListRequest extends QueryPageListRequest {
    /**
     * 类型：1-扩展词，2-停止词，3-单向同义词，4-双向同义词
     */
    private Integer type;

    /**
     * 词语
     */
    private String word;
}
