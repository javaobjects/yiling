package com.yiling.admin.b2b.coupon.from;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGiveGoodsLimitFrom extends BaseForm {

    /**
     * 商品信息列表
     */
    private List<SaveCouponActivityAutoGiveGoodsLimitDetailFrom> goodsLimitList;
}
