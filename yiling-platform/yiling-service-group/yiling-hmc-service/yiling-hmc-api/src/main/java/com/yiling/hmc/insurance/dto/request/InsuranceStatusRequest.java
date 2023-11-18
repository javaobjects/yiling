package com.yiling.hmc.insurance.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceStatusRequest extends BaseRequest {

    /**
     * 保险id
     */
    private Long id;

    /**
     * 保险状态 1-启用 2-停用
     */
    private Integer status;
}
