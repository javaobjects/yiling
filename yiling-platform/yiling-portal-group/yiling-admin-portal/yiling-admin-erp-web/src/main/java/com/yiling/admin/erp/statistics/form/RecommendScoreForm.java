package com.yiling.admin.erp.statistics.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendScoreForm extends BaseForm {

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品规格")
    private String spec;

    @ApiModelProperty("厂家")
    private String manufacturer;

    @ApiModelProperty("需要对比的商品名称")
    private String targetGoodsName;

    @ApiModelProperty("需要对比的商品规格")
    private String targetSpec;

    @ApiModelProperty("需要对比的商品厂家")
    private String targetManufacturer;
}
