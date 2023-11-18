package com.yiling.dataflow.sale.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售指标部门配置
 * </p>
 *
 * @author gxl
 * @date 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_sale_department_target")
public class SaleDepartmentTargetDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 指标ID
     */
    private Long saleTargetId;

    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 上一年目标单位元
     */
    private BigDecimal lastTarget;

    /**
     * 上一年目标比例
     */
    private BigDecimal lastTargetRatio;

    /**
     * 本年目标单位元
     */
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    private BigDecimal currentTargetRatio;

    /**
     * 实际分解目标总和
     */
    private BigDecimal realTarget;

    /**
     * 状态 1未配置,2已配置3-配置中
     */
    private Integer configStatus;

    /**
     * 目标配置时间
     */
    private Date configTime;

    /**
     * 目标分解状态 1未分解 2已分解
     */
    private Integer resolveStatus;

    /**
     * 分解模板osskey
     */
    private String templateUrl;

    /**
     * 目标配置时间
     */
    private Date resolveTime;

    /**
     * 已分解数
     */
    private Integer resolved;

    /**
     * 总分解数
     */
    private Integer goal;

    /**
     * 生成模板失败原因
     */
    private String generateTempErrMsg;

    /**
     * 是否删除0否1是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 本年度增加单位元
     */
    private BigDecimal currentIncrease;


}
