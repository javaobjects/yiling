package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改职位状态 Request
 *
 * @author: xuan.zhou
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePositionStatusRequest extends BaseRequest {

    /**
     * 职位ID
     */
    @NotNull
    private Long id;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @Range(min = 1, max = 2)
    private Integer status;
}
