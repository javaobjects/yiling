package com.yiling.admin.pop.goods.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;

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
public class UpdateGoodsSkuForm extends BaseForm {

    @ApiModelProperty(value = "id", example = "1111")
    private Long id;

    @ApiModelProperty(value = "商品Id", example = "1111")
    private Long goodsId;

    @ApiModelProperty(value = "包装数量", example = "1111")
    private Long packageNumber;

    @ApiModelProperty(value = "库存数量", example = "1111")
    private Long qty;

    @ApiModelProperty(value = "内码", example = "1111")
    private String inSn;

    @ApiModelProperty(value = "编码", example = "1111")
    private String sn;

    @ApiModelProperty(value = "备注", example = "1111")
    private String remark;
}
