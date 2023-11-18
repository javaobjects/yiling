package com.yiling.b2b.app.coupon.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponCanUseListDetailForm extends BaseForm {

    /**
     * 商品ID
     */
    @NotNull
    private Long       goodsId;

    /**
     * 商品小计
     */
    @NotNull
    private BigDecimal goodsAmount;

}
