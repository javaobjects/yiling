package com.yiling.admin.cms.evaluate.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 健康测评 题目
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateQuestionVO extends BaseVO {

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    private Long healthEvaluateId;

    /**
     * 题目类型 1-单选题，2-填空题，3-多选题，4-BMI计算题
     */
    @ApiModelProperty(value = "题目类型 1-单选题，2-填空题，3-多选题，4-BMI计算题")
    private Integer questionType;

    /**
     * 题干
     */
    @ApiModelProperty(value = "题干")
    private String questionTopic;

    /**
     * 是否计分：0-否 1-是
     */
    @ApiModelProperty(value = "是否计分：0-否 1-是")
    private Integer ifScore;

    /**
     * 是否必填：0-否 1-是
     */
    @ApiModelProperty(value = "是否必填：0-否 1-是")
    private Integer ifBlank;

    /**
     * 题目排序
     */
    @ApiModelProperty(value = "题目排序")
    private Integer questionRank;

    /**
     * 选择题
     */
    @ApiModelProperty(value = "选择题")
    private List<HealthEvaluateQuestionSelectVO> selectList;

    /**
     * BMI
     */
    @ApiModelProperty(value = "BMI")
    private List<HealthEvaluateQuestionBmiVO> bmiList;

    /**
     * 填空题
     */
    @ApiModelProperty(value = "填空题")
    private HealthEvaluateQuestionBlankVO blank;


}
