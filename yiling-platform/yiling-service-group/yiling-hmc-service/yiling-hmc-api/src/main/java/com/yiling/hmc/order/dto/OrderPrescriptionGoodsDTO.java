package com.yiling.hmc.order.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单处方药品
 *
 * @author fan.shen
 * @date 2022/4/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPrescriptionGoodsDTO extends BaseDTO {

    /**
     * 兑付订单id
     */
    private Long orderId;

    /**
     * 兑付订单处方id
     */
    private Long orderPrescriptionId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 数量
     */
    private Integer goodsQuantity;

    /**
     * 用法用量
     */
    private String usageInfo;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

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
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

}
