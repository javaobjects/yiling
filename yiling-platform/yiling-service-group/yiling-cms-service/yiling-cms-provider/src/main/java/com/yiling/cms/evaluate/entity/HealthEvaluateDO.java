package com.yiling.cms.evaluate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评表
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_health_evaluate")
public class HealthEvaluateDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 量表名称
     */
    private String healthEvaluateName;

    /**
     * 量表类型 1-健康，2-心理，3-诊疗
     */
    private Integer healthEvaluateType;

    /**
     * 量表描述
     */
    private String healthEvaluateDesc;

    /**
     * 预计答题时间(m)
     */
    private Integer evaluateTime;

    /**
     * 是否医生私有 0-否，1-是
     */
    private Integer docPrivate;

    /**
     * 所属医生id
     */
    private Long docId;

    /**
     * 封面
     */
    private String coverImage;

    /**
     * 分享背景图
     */
    private String backImage;

    /**
     * 答题须知
     */
    private String answerNotes;

    /**
     * 是否有结果 0-否，1-是
     */
    private Integer resultFlag;

    /**
     * 发布状态 1-已发布，0-未发布
     */
    private Integer publishFlag;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

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
