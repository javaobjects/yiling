package com.yiling.b2b.admin.coupon.from;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2021-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityGoodsLimitFrom extends BaseForm {

    /**
     * 商品信息列表
     */
    @ApiModelProperty("商品信息列表")
    @NotNull
    private List<SaveCouponActivityGoodsLimitDetailFrom> goodsLimitList;

}
