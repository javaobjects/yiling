package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加客户分组 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCustomerGroupRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组类型：1-平台创建 2-ERP同步
     */
    private Integer type;
}
