package com.yiling.user.shop.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-店铺区域关联DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopAreaRelationDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 区域编码
     */
    private String areaCode;

}
