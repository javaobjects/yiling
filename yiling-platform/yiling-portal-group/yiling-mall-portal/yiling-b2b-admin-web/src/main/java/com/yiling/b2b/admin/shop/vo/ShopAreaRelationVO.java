package com.yiling.b2b.admin.shop.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class ShopAreaRelationVO extends BaseVO {

    /**
     * 店铺ID
     */
    @ApiModelProperty("店铺ID")
    private Long shopId;

    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    private String areaCode;

}
