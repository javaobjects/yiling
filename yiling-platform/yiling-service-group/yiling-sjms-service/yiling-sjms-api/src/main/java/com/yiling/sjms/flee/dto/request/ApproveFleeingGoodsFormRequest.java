package com.yiling.sjms.flee.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 窜货申诉表单
 *
 * @author: yong.zhang
 * @date: 2023/3/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApproveFleeingGoodsFormRequest extends BaseRequest {

    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;
}
