package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderGiftDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 赠品ID
     */
    private Long goodsGiftId;

    /**
     * 赠品名称
     */
    private String giftName;

    /**
     * 赠品价格
     */
    private BigDecimal price;

    /**
     * 促销活动ID
     */
    private Long promotionActivityId;

    /**
     * 价格区间ID
     */
    private Long promotionLimitId;

    /**
     * 描述
     */
    private String content;

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


}
