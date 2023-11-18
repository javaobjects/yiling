package com.yiling.cms.evaluate.entity;

import java.math.BigDecimal;
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
 * 健康测评BIM题
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_health_evaluate_question_bmi")
public class HealthEvaluateQuestionBmiDO extends BaseDO {

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
