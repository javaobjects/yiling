package com.yiling.goods.inventory.entity;

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
 * 商品库存表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_inventory")
public class InventoryDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 商品内码
     */
    private String inSn;

    /**
     * 库存数量
     */
    private Long qty;

    /**
     * 库存冻结数量
     */
    private Long frozenQty;

    /**
     * 是否超卖商品 0-非超卖 1-超卖
     */
    private Integer overSoldType;

//    /**
//     * 批号
//     */
//    private String batchNumber;
//
//    /**
//     * 有效期
//     */
//    private Date expiryDate;

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
