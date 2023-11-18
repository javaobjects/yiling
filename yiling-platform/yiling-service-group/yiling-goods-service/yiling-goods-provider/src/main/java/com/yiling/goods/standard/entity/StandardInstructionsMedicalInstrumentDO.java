package com.yiling.goods.standard.entity;

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
 * @author shichen
 * @类名 StandardInstructionsMedicalInstrumentDO
 * @描述 医疗器械说明书
 * @创建时间 2022/7/18
 * @修改人 shichen
 * @修改时间 2022/7/18
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("standard_instructions_medical_instrument")
public class StandardInstructionsMedicalInstrumentDO extends BaseDO {

    /**
     * 标准商品ID
     */
    private Long standardId;

    /**
     * 组成结构
     */
    private String structure;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 包装
     */
    private String packingInstructions;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 使用范围
     */
    private String useScope;

    /**
     * 使用方法
     */
    private String usageDosage;

    /**
     * 存储条件
     */
    private String storageConditions;

    /**
     * 备注
     */
    private String remark;

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
