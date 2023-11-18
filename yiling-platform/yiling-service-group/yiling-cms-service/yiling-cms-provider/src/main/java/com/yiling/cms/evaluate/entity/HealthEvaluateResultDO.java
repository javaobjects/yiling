package com.yiling.cms.evaluate.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评结果表
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_health_evaluate_result")
public class HealthEvaluateResultDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * 结果排序
     */
    private Integer resultRank;

    /**
     * 分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    private Integer scoreStartType;

    /**
     * 开始区间
     */
    private BigDecimal scoreStart;

    /**
     * 分值区间类型 1-小于，2-小于等于，3-等于，4-大于等于，5-大于
     */
    private Integer scoreEndType;

    /**
     * 结束区间
     */
    private BigDecimal scoreEnd;

    /**
     * 测评结果
     */
    private String evaluateResult;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 健康小贴士
     */
    private String healthTip;

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
