package com.yiling.goods.medicine.entity;

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
 * 中药材说明书
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("instructions_materials")
public class InstructionsMaterialsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 标准商品ID
     */
    private Long goodsId;

    /**
     * 性状
     */
    private String drugProperties;

    /**
     * 性味
     */
    private String propertyFlavor;

    /**
     * 功效
     */
    private String effect;

    /**
     * 用法与用量
     */
    private String usageDosage;

    /**
     * 储藏
     */
    private String store;

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
