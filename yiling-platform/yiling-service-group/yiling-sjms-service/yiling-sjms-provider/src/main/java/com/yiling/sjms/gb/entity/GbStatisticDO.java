package com.yiling.sjms.gb.entity;

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
 * 团购表单
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gb_statistic")
public class GbStatisticDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 省区名称
     */
    private String provinceName;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 产品code
     */
    private Long goodsCode;

    /**
     * 提报盒数
     */
    private Integer quantityBox;

    /**
     * 提报实际团购金额
     */
    private BigDecimal finalAmount;

    /**
     * 取消盒数
     */
    private Integer cancelQuantityBox;

    /**
     * 取消金额
     */
    private BigDecimal cancelFinalAmount;

    /**
     * 团购月份
     */
    private Date month;

    /**
     * 日期
     */
    private Date dayTime;

    /**
     * 关联团购Id
     */
    private String gbListId;


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
