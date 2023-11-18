package com.yiling.dataflow.statistics.entity;

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
 * 天数计算结果表
 * </p>
 *
 * @author handy
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_analyse_calculation_result")
public class FlowAnalyseCalculationResultDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
   // private Long eid;

    private Long crmEnterpriseId;

    /**
     * 商品ID
     */
    private Long specificationId;

    /**
     * 近天数
     */
    private Integer day;

    /**
     * 日均销量
     */
    private Long saleAvg;

    /**
     * 库存可销天数
     */
    private BigDecimal supportDay;

    /**
     * 建议补货数量(根据销售15天建议补货数量)
     */
    private Long supplementQuantity;

    /**
     * 库存数量
     */
    private Long stockQuantity;

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
