package com.yiling.goods.restriction.entity;

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
 * @类名 GoodsPurchaseRestrictionDO
 * @描述 商品限购表
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_purchase_restriction")
public class GoodsPurchaseRestrictionDO extends BaseDO {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 每单限购数量
     */
    private Long orderRestrictionQuantity;

    /**
     * 时间内限购数量
     */
    private Long timeRestrictionQuantity;

    /**
     * 限购时间类型 1 每天 2 每周 3每月 4自定义
     */
    private Integer timeType;

    /**
     * 限购开始时间
     */
    private Date startTime;

    /**
     * 限购结束时间
     */
    private Date endTime;

    /**
     * 客户设置类型 0：全部客户  1:部分客户
     */
    private Integer customerSettingType;

    /**
     * 0 正常状态 1 关闭状态
     */
    private Integer status;

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
