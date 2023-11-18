package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改客户分组 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerGroupRequest extends BaseRequest {

    /**
     * 客户分组ID
     */
    private Long id;

    /**
     * 分组名称
     */
    private String name;
}
