package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 健康测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PublishHealthEvaluateRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    private Long id;


    /**
     * 发布状态 1-已发布，0-未发布
     */
    private Integer publishFlag;
}
