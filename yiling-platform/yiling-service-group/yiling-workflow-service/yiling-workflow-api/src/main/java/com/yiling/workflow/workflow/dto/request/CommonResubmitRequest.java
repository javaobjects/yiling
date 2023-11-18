package com.yiling.workflow.workflow.dto.request;

import java.util.Map;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CommonResubmitRequest extends BaseRequest {

    private static final long serialVersionUID = 3582517288842037180L;
    /**
     * 表单id
     */
    private Long id;
    /**
     * 流程变量
     */
    private Map<String, Object> variables;
}