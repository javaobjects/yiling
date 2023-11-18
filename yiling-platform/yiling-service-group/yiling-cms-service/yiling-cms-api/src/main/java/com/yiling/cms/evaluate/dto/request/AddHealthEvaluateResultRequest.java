package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 健康测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddHealthEvaluateResultRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 健康测评结果 list
     */
    private List<HealthEvaluateResultDetailRequest> HealthEvaluateResultList;
}
