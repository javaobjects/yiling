package com.yiling.sales.assistant.task.entity;

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
 * 任务商品关联表 
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_goods_relation")
public class TaskGoodsRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务主键
     */
    private Long taskId;

    /**
     * 任务商品销售价
     */
    private BigDecimal salePrice;

    /**
     * 商品主键
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 佣金比例
     */
    private String commissionRate;

    /**
     * 佣金 企业任务不设
     */
    private BigDecimal commission;

    /**
     * 出货价
     */
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    private BigDecimal  sellPrice;

    /**
     * 销售规格id
     */
    private Long sellSpecificationsId;

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


}
