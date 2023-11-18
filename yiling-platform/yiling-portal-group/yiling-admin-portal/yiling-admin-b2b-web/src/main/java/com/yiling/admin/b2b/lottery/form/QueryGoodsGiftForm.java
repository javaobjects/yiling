package com.yiling.admin.b2b.lottery.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 赠品库查询条件
 * @author:wei.wang
 * @date:2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsGiftForm extends BaseForm {

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品编号")
    private Long id;

    @ApiModelProperty("所属业务（1-全部；2-2b；3-2c")
    private Integer businessType;

    @ApiModelProperty("1-真实物品；2-虚拟物品；3-优惠券；4-会员）")
    private List<Integer> goodsType;
}
