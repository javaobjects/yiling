package com.yiling.cms.evaluate.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 健康测评题目 选择题
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateQuestionSelectDTO extends BaseDTO {

    private static final long serialVersionUID = -7863296268309963238L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * cms_health_evaluate_question主键
     */
    private Long healthEvaluateQuestionId;

    /**
     * 选项文本
     */
    private String optionText;

    /**
     * 选项内容
     */
    private String optionContent;

    /**
     * 记分是否区分性别 0-否，1-是
     */
    private Integer ifScoreSex;

    /**
     * 分值
     */
    private BigDecimal score;

    /**
     * 男性分值
     */
    private BigDecimal scoreMen;

    /**
     * 女性分值
     */
    private BigDecimal scoreWomen;

    /**
     * 选项排序
     */
    private Integer optionRank;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "HealthEvaluateQuestionSelectDTO{" +
                "healthEvaluateId=" + healthEvaluateId +
                ", healthEvaluateQuestionId=" + healthEvaluateQuestionId +
                ", optionText='" + optionText + '\'' +
                ", optionContent='" + optionContent + '\'' +
                ", ifScoreSex=" + ifScoreSex +
                ", score=" + score +
                ", scoreMen=" + scoreMen +
                ", scoreWomen=" + scoreWomen +
                ", optionRank=" + optionRank +
                ", createUser=" + createUser +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}
