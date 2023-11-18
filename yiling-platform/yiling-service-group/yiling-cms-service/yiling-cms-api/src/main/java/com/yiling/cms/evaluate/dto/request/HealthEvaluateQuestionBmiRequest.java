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
public class HealthEvaluateQuestionBmiRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    private Integer rangeStartType;

    /**
     * 开始区间
     */
    private BigDecimal rangeStart;

    /**
     * 区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    private Integer rangeEndType;

    /**
     * 结束区间
     */
    private BigDecimal rangeEnd;

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
