package com.yiling.hmc.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 药店详情
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/25
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("药店详情")
@AllArgsConstructor
@NoArgsConstructor
public class MedicineShopDetailVO {

    /**
     * 店铺id
     */
    @ApiModelProperty("店铺id")
    private Long eId;

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String eName;

    /**
     * 店铺地址
     */
    @ApiModelProperty("店铺地址")
    private String address;

    /**
     * hmcGoodsId
     */
    @ApiModelProperty("hmcGoodsId")
    private Long hmcGoodsId;

    /**
     * 参保地址
     */
    @ApiModelProperty("参保地址")
    private String buyInsuranceUrl;

}
