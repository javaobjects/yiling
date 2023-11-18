package com.yiling.dataflow.sale.entity;

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
 * 部门销售指标子项配置
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_sale_department_sub_target")
public class SaleDepartmentSubTargetDO extends BaseDO {

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
     * 指标配置类型1-省区2-月份3-品种4区办
     */
    private Integer type;

    /**
     * 类型关联ID:部门ID,商品品类ID
     */
    private Long relId;

    /**
     * 类型关联名称：部门名称,商品品类名称
     */
    private String relName;

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
