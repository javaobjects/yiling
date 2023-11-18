package com.yiling.order.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDeliveryDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 生产日期
     */
    private Date produceDate;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;
    

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
