package com.yiling.sjms.manor.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询医院辖区变更表单
 * @author: gxl
 * @date: 2023/5/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryChangePageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 6853102520741238631L;
    /**
     * form表主键
     */
    private Long formId;

}