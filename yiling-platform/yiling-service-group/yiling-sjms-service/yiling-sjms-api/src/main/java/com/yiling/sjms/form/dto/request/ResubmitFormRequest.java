package com.yiling.sjms.form.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * （驳回后）重新提交 Request
 *
 * @author: xuan.zhou
 * @date: 2023/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ResubmitFormRequest extends BaseRequest {

    private static final long serialVersionUID = -9012182136225173646L;

    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;

}
