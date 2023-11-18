package com.yiling.b2b.app.paypromotion.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/5/5 0005
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPayPromotionGoodsSearchFrom extends QueryPageListForm {

    @NotNull
    @ApiModelProperty(value = "支付促销活动id")
    private Long activityId;

    @ApiModelProperty(value = "卖家企业id")
    private Long sellerEid;
}
