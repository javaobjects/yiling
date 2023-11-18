package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评营销
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DelHealthEvaluateMarketRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 添加类型 1-关联药品，2-改善建议，3-推广服务
     */
    private Integer type;

    /**
     * id
     */
    private Long id;
}
