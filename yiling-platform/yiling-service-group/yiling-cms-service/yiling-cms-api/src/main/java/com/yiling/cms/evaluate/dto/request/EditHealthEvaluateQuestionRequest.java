package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 编辑健康测评题目
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditHealthEvaluateQuestionRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 测评id
     */
    private Long healthEvaluateId;

    /**
     * 题目
     */
    private HealthEvaluateQuestionDetailRequest question;

}
