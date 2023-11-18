package com.yiling.mall.banner.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppVajraPositionStatusRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer vajraStatus;
}
