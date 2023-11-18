package com.yiling.user.shop.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-店铺设置 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveShopRequest extends BaseRequest {

    /**
     * 店铺ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long shopEid;

    /**
     * 店铺企业名称
     */
    private String shopName;

    /**
     * 店铺logo
     */
    private String shopLogo;

    /**
     * 店铺简介
     */
    private String shopDesc;

    /**
     * 起配金额
     */
    private BigDecimal startAmount;

    /**
     * 支付方式：1-在线支付 2-线下支付 3-账期
     */
    private List<Integer> paymentMethodList;

    /**
     * 店铺区域编码
     */
    private String areaJsonString;

}
