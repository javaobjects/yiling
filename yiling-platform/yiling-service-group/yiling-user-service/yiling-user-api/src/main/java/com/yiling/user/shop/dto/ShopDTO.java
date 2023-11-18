package com.yiling.user.shop.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-店铺设置DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺企业ID
     */
    private Long shopEid;

    /**
     * 店铺名称
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
     * 店铺公告
     */
    private String shopAnnouncement;

    /**
     * 起配金额
     */
    private BigDecimal startAmount;

    /**
     * 是否为优质商家
     */
    private Boolean highQualitySupplierFlag;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}
