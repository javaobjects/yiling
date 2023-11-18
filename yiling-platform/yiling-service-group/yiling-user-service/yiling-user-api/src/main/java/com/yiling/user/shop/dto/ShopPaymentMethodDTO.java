package com.yiling.user.shop.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-店铺支付方式DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopPaymentMethodDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 支付方式（关联payment_method.code）
     */
    private Integer paymentMethod;

}
