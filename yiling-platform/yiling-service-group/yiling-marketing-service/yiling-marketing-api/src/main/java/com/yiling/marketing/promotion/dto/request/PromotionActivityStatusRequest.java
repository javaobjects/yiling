package com.yiling.marketing.promotion.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionActivityStatusRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 活动状态（1-启用；2-停用；）
     */
    private Integer status;
}
