package com.yiling.marketing.strategy.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CopyStrategyRequest extends BaseRequest {

    /**
     * 策略满赠活动id
     */
    private Long id;
}
