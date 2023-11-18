package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 移动分组客户 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MoveGroupCustomersRequest extends BaseRequest {

    /**
     * 原分组ID
     */
    @NotNull
    private Long originalGroupId;

    /**
     * 目标分组ID
     */
    @NotNull
    private Long targetGroupId;
}
