package com.yiling.user.procrelation.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveProcTemplateRequest extends BaseRequest {


    private static final long serialVersionUID = 8463124847876407018L;

    /**
     * id
     */
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;
}
