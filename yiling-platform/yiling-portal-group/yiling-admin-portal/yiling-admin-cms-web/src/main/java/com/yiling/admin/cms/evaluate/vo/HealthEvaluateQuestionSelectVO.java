package com.yiling.admin.cms.evaluate.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 健康测评 单选题
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateQuestionSelectVO extends BaseVO {

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    private Long healthEvaluateId;

    /**
     * cms_health_evaluate_question主键
     */
    @ApiModelProperty(value = "cms_health_evaluate_question主键")
    private Long healthEvaluateQuestionId;

    /**
     * 选项文本
     */
    @ApiModelProperty(value = "选项文本")
    private String optionText;

    /**
     * 记分是否区分性别 0-否，1-是
     */
    @ApiModelProperty(value = "记分是否区分性别 0-否，1-是")
    private Integer ifScoreSex;

    /**
     * 分值
     */
    @ApiModelProperty(value = "分值")
    private BigDecimal score;

    /**
     * 男性分值
     */
    @ApiModelProperty(value = "男性分值")
    private BigDecimal scoreMen;

    /**
     * 女性分值
     */
    @ApiModelProperty(value = "女性分值")
    private BigDecimal scoreWomen;

    /**
     * 选项排序
     */
    @ApiModelProperty(value = "选项排序")
    private Integer optionRank;

}
