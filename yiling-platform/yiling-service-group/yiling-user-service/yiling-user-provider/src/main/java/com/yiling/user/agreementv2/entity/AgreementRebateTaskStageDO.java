package com.yiling.user.agreementv2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import java.math.BigDecimal;
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
 * 协议返利任务量阶梯表
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_task_stage")
public class AgreementRebateTaskStageDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议时段ID
     */
    private Long segmentId;

    /**
     * 商品组ID
     */
    private Long groupId;

    /**
     * 协议返利范围ID
     */
    private Long rebateScopeId;

    /**
     * 任务量
     */
    private BigDecimal taskNum;

    /**
     * 任务量单位：1-元 2-盒
     */
    private Integer taskUnit;

    /**
     * 超任务量汇总返
     */
    private BigDecimal overSumBack;

    /**
     * 超任务量汇总返单位：1-元 2-%
     */
    private Integer overSumBackUnit;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
