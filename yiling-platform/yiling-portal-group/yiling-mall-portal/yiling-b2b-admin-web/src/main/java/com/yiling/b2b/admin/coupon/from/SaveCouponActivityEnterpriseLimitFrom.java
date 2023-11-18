package com.yiling.b2b.admin.coupon.from;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityEnterpriseLimitFrom extends BaseForm {

    /**
     * 商品信息列表
     */
    private List<SaveCouponActivityEnterpriseLimitDetailFrom> enterpriseLimitList;


}
