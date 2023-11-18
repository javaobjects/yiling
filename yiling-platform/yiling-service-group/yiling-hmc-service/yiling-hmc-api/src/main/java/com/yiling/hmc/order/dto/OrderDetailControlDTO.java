package com.yiling.hmc.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细管控表
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailControlDTO extends BaseDTO {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品id
     */
    private Long hmcGoodsId;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 管控id
     */
    private Long controlId;

    /**
     * eid
     */
    private Long eid;

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
