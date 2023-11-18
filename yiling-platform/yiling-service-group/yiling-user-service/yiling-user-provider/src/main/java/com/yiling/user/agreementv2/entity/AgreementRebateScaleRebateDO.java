package com.yiling.user.agreementv2.entity;

import java.math.BigDecimal;
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
 * 协议返利-规模返利阶梯表
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_scale_rebate")
public class AgreementRebateScaleRebateDO extends BaseDO {

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
     * 目标达成率
     */
    private BigDecimal targetReachRatio;

    /**
     * 目标达成率单位：1-%
     */
    private Integer reachRatioUnit;

    /**
     * 目标返利率
     */
    private BigDecimal targetRebateRatio;

    /**
     * 目标返利率单位：1-%
     */
    private Integer rebateRatioUnit;

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
