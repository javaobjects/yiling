package com.yiling.b2b.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrDeleteGoodsLimitForm extends BaseForm {

    @ApiModelProperty(value = "商品集合", example = "11")
    private List<Long> goodsIds;

    /**
     * 客户限价Id
     */
    @ApiModelProperty(value = "客户id", example = "11")
    private Long customerEid;
}
