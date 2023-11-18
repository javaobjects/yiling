package com.yiling.hmc.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退货单明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnDetailDTO extends BaseDTO {

    /**
     * 退货单id
     */
    private Long returnId;

    /**
     * 订单明细id
     */
    private Long detailId;

    /**
     * 药品id
     */
    private Long goodsId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 退货数量
     */
    private Long returnQuality;

    /**
     * 退单价格
     */
    private BigDecimal returnPrice;

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
