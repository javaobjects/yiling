package com.yiling.basic.dict.bo.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典类型列表 Request
 *
 * @author lun.yu
 * @date 2021-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDictTypeRequest extends QueryPageListRequest {

    /**
     * 字典名称
     */
    private String name;
}

