package com.yiling.cms.evaluate.dto;

import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionDetailRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 健康测评题目上下文
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionContextDTO {

    /**
     * 操作人ID
     */
    private Long opUserId;

    /**
     * 测评id
     */
    private Long healthEvaluateId;

    /**
     * 测评题目id
     */
    private Long healthEvaluateQuestionId;

    /**
     * 题目详情
     */
    private HealthEvaluateQuestionDetailRequest questionDetailRequest;

}
