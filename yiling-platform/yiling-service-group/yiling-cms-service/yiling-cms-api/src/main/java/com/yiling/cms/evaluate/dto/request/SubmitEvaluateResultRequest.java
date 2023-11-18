package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 健康测评题目
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitEvaluateResultRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * 开始测评id
     */
    private Long startEvaluateId;

    /**
     * 测评答案
     */
    private List<EvaluateResultDetailRequest> evaluateResultDetailList;

}
