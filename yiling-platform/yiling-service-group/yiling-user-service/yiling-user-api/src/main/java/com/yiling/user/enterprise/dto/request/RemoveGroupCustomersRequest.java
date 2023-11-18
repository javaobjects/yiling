package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 移除分组客户 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveGroupCustomersRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 客户ID列表
     */
    private List<Long> customerEids;
}
