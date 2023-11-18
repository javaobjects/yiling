package com.yiling.sjms.agency.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 机构新增表单
 *
 * @author: yong.zhang
 * @date: 2023/2/28 0028
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApproveAgencyAddFormRequest extends BaseRequest {

    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;
}
