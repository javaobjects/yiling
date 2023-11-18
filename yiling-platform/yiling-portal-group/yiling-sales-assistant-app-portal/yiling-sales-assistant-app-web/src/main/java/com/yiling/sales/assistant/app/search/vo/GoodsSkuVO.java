package com.yiling.sales.assistant.app.search.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.sales.assistant.app.cart.util.StockUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSkuVO extends BaseVO {

    @ApiModelProperty(value = "销售包装数量")
    private Long packageNumber;

    @ApiModelProperty(value = "库存数量")
    private Long qty;

    @ApiModelProperty(value = "库存冻结数量")
    private Long frozenQty;

    @ApiModelProperty(value = "商品内码")
    private String inSn;

    @ApiModelProperty(value = "商品编码")
    private String sn;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 库存描述
     */
    @ApiModelProperty("库存描述")
    private String qtyDesc;

    public String getQtyDesc() {

        Long availableQty = qty - frozenQty;

        return StockUtils.getStockText(availableQty);
    }

}
