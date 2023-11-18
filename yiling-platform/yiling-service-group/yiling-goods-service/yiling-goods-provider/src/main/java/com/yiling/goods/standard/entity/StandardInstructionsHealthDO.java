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
 * <p>
 * 保健食品说明书
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("standard_instructions_health")
public class StandardInstructionsHealthDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 标准商品ID
     */
    private Long standardId;

    /**
     * 原料
     */
    private String rawMaterial;

    /**
     * 辅料
     */
    private String ingredients;

    /**
     * 适宜人群
     */
    private String suitablePeople;

    /**
     * 不适宜人群
     */
    private String unsuitablePeople;

    /**
     * 保健功能
     */
    private String healthcareFunction;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 食用量及食用方法
     */
    private String usageDosage;

    /**
     * 储藏
     */
    private String store;

    /**
     * 注意事项
     */
    private String noteEvents;

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
