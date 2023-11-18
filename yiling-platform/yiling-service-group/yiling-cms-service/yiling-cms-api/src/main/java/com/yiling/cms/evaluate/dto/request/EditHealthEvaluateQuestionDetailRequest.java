package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class EditHealthEvaluateQuestionDetailRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 题目类型 1-单选题，2-填空题，3-多选题，4-BMI计算题
     */
    private Integer questionType;

    /**
     * 题干
     */
    private String questionTopic;

    /**
     * 是否计分：0-否 1-是
     */
    private Integer ifScore;

    /**
     * 是否必填：0-否 1-是
     */
    private Integer ifBlank;

    /**
     * 题目排序
     */
    private Integer questionRank;

    /**
     * 选择题
     */
    private List<HealthEvaluateQuestionSelectRequest> selectList;

    /**
     * BMI
     */
    private List<HealthEvaluateQuestionBmiRequest> bmiList;

    /**
     * 填空题
     */
    private HealthEvaluateQuestionBlankRequest blank;

}
