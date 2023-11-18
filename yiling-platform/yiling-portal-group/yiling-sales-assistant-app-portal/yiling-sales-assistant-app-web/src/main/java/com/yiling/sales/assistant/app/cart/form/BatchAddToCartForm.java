package com.yiling.sales.assistant.app.cart.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批量加入进货单 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@Data
public class BatchAddToCartForm {

    @NotEmpty
    @ApiModelProperty(value = "快速采购信息", required = true)
    private List<QuickPurchaseInfoForm> quickPurchaseInfoList;

    @Data
    private static class QuickPurchaseInfoForm {

        @NotNull
        @ApiModelProperty(value = "商品ID", required = true)
        private Long goodsId;

        @NotNull
        @Min(1)
        @ApiModelProperty(value = "购买数量", required = true)
        private Integer quantity;

        @NotNull
        @ApiModelProperty(value = "配送商企业ID", required = true)
        private Long distributorEid;

        @NotNull
        @ApiModelProperty(value = "配送商商品ID", required = true)
        private Long distributorGoodsId;
    }
}
