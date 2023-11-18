package com.yiling.hmc.evaluate.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 健康测评首页
 *
 * @author: fan.shen
 * @date: 2022/12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateVO extends BaseVO {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * 量表名称
     */
    @ApiModelProperty(value = "量表名称")
    private String healthEvaluateName;

    /**
     * 量表类型 1-健康，2-心理，3-诊疗
     */
    @ApiModelProperty(value = "量表类型 1-健康，2-心理，3-诊疗")
    private Integer healthEvaluateType;

    /**
     * 量表描述
     */
    @ApiModelProperty(value = "量表描述")
    private String healthEvaluateDesc;

    /**
     * 预计答题时间(m)
     */
    @ApiModelProperty(value = "预计答题时间(m)")
    private Integer evaluateTime;

    /**
     * 是否医生私有 0-否，1-是
     */
    @ApiModelProperty(value = "是否医生私有 0-否，1-是")
    private Integer docPrivate;

    /**
     * 所属医生id
     */
    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String coverImage;

    /**
     * 分享背景图
     */
    @ApiModelProperty(value = "分享背景图")
    private String backImage;

    /**
     * 答题须知
     */
    @ApiModelProperty(value = "答题须知")
    private String answerNotes;

    /**
     * 题目数量
     */
    @ApiModelProperty(value = "题目数量")
    private Long questionCount;

    /**
     * 参与人数
     */
    @ApiModelProperty(value = "参与人数")
    private Long userCount = 0L;

    /**
     * 完成测试人数
     */
    @ApiModelProperty(value = "完成测试人数")
    private Long finishCount = 0L;

    /**
     * 测评题目
     */
    @ApiModelProperty(value = "测评题目")
    private List<HealthEvaluateQuestionVO> questionList;

}
