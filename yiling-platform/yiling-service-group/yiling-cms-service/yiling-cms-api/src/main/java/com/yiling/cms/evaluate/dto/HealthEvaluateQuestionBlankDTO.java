package com.yiling.cms.evaluate.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 健康测评题目 填空题
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateQuestionBlankDTO extends BaseDTO {

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

}
