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
 * @author shichen
 * @类名 InventorySubscriptionDO
 * @描述
 * @创建时间 2022/7/25
 * @修改人 shichen
 * @修改时间 2022/7/25
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_inventory_subscription")
public class InventorySubscriptionDO extends BaseDO {
    /**
     * 企业id
     */
    private Long eid;

    /**
     * 库存Id
     */
    private Long inventoryId;

    /**
     * 订阅企业id
     */
    private Long subscriptionEid;

    /**
     * 订阅企业名称
     */
    private String subscriptionEname;

    /**
     * 订阅库存
     */
    private Long qty;

    /**
     * 订阅内码
     */
    private String inSn;

    /**
     * 订阅类型 1：本店订阅   2：erp订阅  3：pop订阅
     */
    private Integer subscriptionType;

    /**
     * 订阅状态
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
