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
 * 药品说明书
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("instructions_goods")
public class InstructionsGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 药品成分
     */
    private String drugDetails;

    /**
     * 药品性状
     */
    private String drugProperties;

    /**
     * 适应症
     */
    private String indications;

    /**
     * 用法与用量
     */
    private String usageDosage;

    /**
     * 不良反应
     */
    private String adverseEvents;

    /**
     * 禁忌症
     */
    private String contraindication;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 药物相互作用
     */
    private String interreaction;

    /**
     * 存储条件
     */
    private String storageConditions;

    /**
     * 包装
     */
    private String packingInstructions;

    /**
     * 保质期
     */
    private String shelfLife;

    /**
     * 执行标准
     */
    private String executiveStandard;

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
