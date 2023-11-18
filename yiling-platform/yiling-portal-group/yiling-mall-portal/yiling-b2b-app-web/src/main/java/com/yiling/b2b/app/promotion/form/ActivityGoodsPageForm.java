package com.yiling.b2b.app.promotion.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/12/13
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class ActivityGoodsPageForm {

    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("店铺Eid")
    private Long shopEid;

    @ApiModelProperty("商品名称")
    private String goodsName;
}
