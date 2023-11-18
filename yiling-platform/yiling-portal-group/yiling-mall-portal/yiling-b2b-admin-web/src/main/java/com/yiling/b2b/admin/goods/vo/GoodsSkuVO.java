package com.yiling.b2b.admin.goods.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

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

    @ApiModelProperty(value = "实际库存")
    private Long realQty;

    @ApiModelProperty(value = "商品内码")
    private String inSn;

    @ApiModelProperty(value = "商品编码")
    private String sn;

    @ApiModelProperty(value = "批号")
    private String batchNumber;

    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    @ApiModelProperty(value = "状态：0.正常 1.停用 2.隐藏")
    private Integer status;

    @ApiModelProperty(value = "true:隐藏 false:不隐藏")
    private Boolean hideFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

}
