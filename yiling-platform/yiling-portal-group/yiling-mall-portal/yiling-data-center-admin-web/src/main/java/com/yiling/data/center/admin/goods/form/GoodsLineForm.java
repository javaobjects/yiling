package com.yiling.data.center.admin.goods.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsLineForm extends BaseForm {

    @ApiModelProperty(value = "商品Id")
    private long goodsId;

    /**
     * b2b是否开通：0不开通 1开通
     */
    @ApiModelProperty(value = "b2b是否开通：0不开通 1开通")
    private Integer mallFlag;

    /**
     * pop是否开通：0不开通 1开通
     */
    @ApiModelProperty(value = "pop是否开通：0不开通 1开通")
    private Integer popFlag;
}
