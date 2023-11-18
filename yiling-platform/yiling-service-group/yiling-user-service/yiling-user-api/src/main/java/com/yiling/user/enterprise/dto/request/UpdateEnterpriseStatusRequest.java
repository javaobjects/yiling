package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新企业状态 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseStatusRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;
}
