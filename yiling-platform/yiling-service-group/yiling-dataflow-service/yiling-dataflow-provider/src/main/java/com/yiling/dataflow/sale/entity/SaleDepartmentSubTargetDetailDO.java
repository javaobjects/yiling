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
 * 部门销售指标子项配置详情
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_sale_department_sub_target_detail")
public class SaleDepartmentSubTargetDetailDO extends BaseDO {

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
     * 标签为省区的部门ID
     */
    private Long departProvinceId;

    /**
     * 标签为省区的部门名称
     */
    private String departProvinceName;

    /**
     * 标签为区办的部门ID
     */
    private Long departRegionId;

    /**
     * 标签为区办的部门名称
     */
    private String departRegionName;

    /**
     * 品类ID
     */
    private Long categoryId;

    /**
     * 品类名称
     */
    private String categoryName;

    /**
     * 1月份目标
     */
    private BigDecimal targetJan;

    /**
     * 2月份目标
     */
    private BigDecimal targetFeb;

    /**
     * 3月份目标
     */
    private BigDecimal targetMar;

    /**
     * 4月份目标
     */
    private BigDecimal targetApr;

    /**
     * 5月份目标
     */
    private BigDecimal targetMay;

    /**
     * 6月份目标
     */
    private BigDecimal targetJun;

    /**
     * 7月份目标
     */
    private BigDecimal targetJul;

    /**
     * 8月份目标
     */
    private BigDecimal targetAug;

    /**
     * 9月份目标
     */
    private BigDecimal targetSep;

    /**
     * 10月份目标
     */
    private BigDecimal targetOct;

    /**
     * 11月份目标
     */
    private BigDecimal targetNov;

    /**
     * 12月份目标
     */
    private BigDecimal targetDec;

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
