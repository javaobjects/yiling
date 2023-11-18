package com.yiling.hmc.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SyncGoodsSaveDetailRequest extends BaseRequest {

    // @NotNull
    // @ApiModelProperty("中台标准库id")
    private Long standardId;

    // @NotNull
    // @ApiModelProperty("中台售卖规格id")
    private Long sellSpecificationsId;

    // @NotNull
    // @ApiModelProperty("C端平台药房id")
    private Long ihCPlatformId;

    // 配送商商品ID
    private Long ihPharmacyGoodsId;

    // @NotNull
    // @ApiModelProperty("IH 配送id")
    private Long ihEid;
}
