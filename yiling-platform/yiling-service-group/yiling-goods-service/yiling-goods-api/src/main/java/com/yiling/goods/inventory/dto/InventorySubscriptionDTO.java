package com.yiling.goods.inventory.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 InventorySubscriptionDTO
 * @描述
 * @创建时间 2022/7/25
 * @修改人 shichen
 * @修改时间 2022/7/25
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InventorySubscriptionDTO extends BaseDTO {
    /**
     * 订阅方企业id
     */
    private Long eid;

    /**
     * 库存Id
     */
    private Long inventoryId;

    /**
     * 被订阅方企业id
     */
    private Long subscriptionEid;

    /**
     * 订阅企业名称
     */
    private String subscriptionEname;


    /**
     * 订阅类型 1：本店订阅   2：erp订阅  3：pop订阅
     */
    private Integer subscriptionType;
    /**
     * 订阅内码
     */
    private String inSn;

    /**
     * 订阅状态
     */
    private Integer status;

    /**
     * 订阅库存
     */
    private Long qty;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
