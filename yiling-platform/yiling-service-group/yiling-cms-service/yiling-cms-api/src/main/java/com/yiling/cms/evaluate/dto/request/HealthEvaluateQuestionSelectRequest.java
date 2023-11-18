package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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
public class HealthEvaluateQuestionSelectRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

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

}
