package com.yiling.cms.evaluate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评填空题
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_health_evaluate_question_blank")
public class HealthEvaluateQuestionBlankDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
