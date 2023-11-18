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
public class HealthEvaluateQuestionBlankRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 填空类型 1-文本，2-数字，3-数字型（区间），4-日期，5-上传图片
     */
    private Integer blankType;

    /**
     * 是否可输入小数 0-否，1-是
     */
    private Integer ifDecimal;

    /**
     * 是否有单位 0-否，1-是
     */
    private Integer ifUnit;

    /**
     * 单位
     */
    private String unit;

}
