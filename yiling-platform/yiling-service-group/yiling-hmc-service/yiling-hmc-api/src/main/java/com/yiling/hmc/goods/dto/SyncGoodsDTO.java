package com.yiling.hmc.goods.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * C端保险药品商家提成设置表
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SyncGoodsDTO extends BaseDTO {

    private Long hmcGoodsId;

    // @NotNull
    // @ApiModelProperty("中台标准库id")
    /**
     * 中台标准库id
     */
    private Long standardId;

    // @NotNull
    // @ApiModelProperty("")
    /**
     * 中台售卖规格id
     */
    private Long sellSpecificationsId;

    // @NotNull
    // @ApiModelProperty("C端平台药房id")
    /**
     * C端平台药房id
     */
    private Long ihCPlatformId;

    private Long ihPharmacyGoodsId;

    // @NotNull
    // @ApiModelProperty("IH 配送id")
    /**
     * IH 配送id
     */
    private Long ihEid;
}
