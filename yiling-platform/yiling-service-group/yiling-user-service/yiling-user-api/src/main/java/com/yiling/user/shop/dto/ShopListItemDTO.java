package com.yiling.user.shop.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * B2B-店铺列表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class ShopListItemDTO extends BaseDTO {

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺企业ID
     */
    private Long shopEid;

    /**
     * 店铺logo
     */
    private String shopLogo;

    /**
     * 店铺简介
     */
    private String shopDesc;

    /**
     * 店铺公告
     */
    private String shopAnnouncement;

    /**
     * 起配金额
     */
    private BigDecimal startAmount;

}
