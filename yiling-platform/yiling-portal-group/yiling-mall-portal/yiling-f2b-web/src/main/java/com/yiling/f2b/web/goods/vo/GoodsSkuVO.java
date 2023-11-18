package com.yiling.f2b.web.goods.vo;

import java.util.Date;

import com.yiling.common.web.util.StockUtils;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSkuVO extends BaseVO {


    @ApiModelProperty("库存描述")
    private String qtyDesc;

    /**
     * 包装数量
     */
    @ApiModelProperty(value = "包装数量")
    private Long packageNumber;

    /**
     * ERP内码
     */
    @ApiModelProperty(value = "ERP内码")
    private String inSn;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Long qty;

    /**
     * 冻结库存
     */
    @ApiModelProperty(value = "冻结库存")
    private Long frozenQty;

    @ApiModelProperty(value = "实际库存")
    private Long realQty;

    @ApiModelProperty(value = "批号")
    private String batchNumber;

    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    /**
     * 是否选中
     */
    @ApiModelProperty(value = "是否选中 0没有选中 1选中")
    private Integer selectFlag;

    public String getQtyDesc() {
        Long availableQty = qty - frozenQty;
        return StockUtils.getStockText(availableQty);
    }
}
