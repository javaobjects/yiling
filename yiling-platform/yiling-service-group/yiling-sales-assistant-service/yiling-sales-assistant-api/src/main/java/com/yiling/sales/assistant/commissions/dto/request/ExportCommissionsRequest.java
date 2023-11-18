package com.yiling.sales.assistant.commissions.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportCommissionsRequest extends BaseRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 联系方式
     */
    private String mobile;
}
