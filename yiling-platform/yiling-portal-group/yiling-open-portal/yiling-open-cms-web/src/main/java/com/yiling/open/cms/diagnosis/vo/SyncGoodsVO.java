package com.yiling.open.cms.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 同步商品VO
 * </p>
 *
 * @author fan.shen
 * @date 2023-05-24
 */
@Data
@Accessors(chain = true)
public class SyncGoodsVO {

    @ApiModelProperty("HMC商品id")
    private Long hmcGoodsId;

    @ApiModelProperty("中台标准库id")
    private Long standardId;

    @ApiModelProperty("中台规格id")
    private Long sellSpecificationsId;

    @ApiModelProperty("C端平台药房id")
    private Long ihCPlatformId;

    @ApiModelProperty("配送商商品ID")
    private Long ihPharmacyGoodsId;

    @ApiModelProperty("IH 配送id")
    private Long ihEid;

}
