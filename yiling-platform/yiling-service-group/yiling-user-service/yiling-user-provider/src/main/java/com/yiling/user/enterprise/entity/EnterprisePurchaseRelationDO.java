package com.yiling.user.enterprise.entity;

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
 * 企业采购关系
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise_purchase_relation")
public class EnterprisePurchaseRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 来源：1-协议生成 2-手动创建
     */
    private Integer source;

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