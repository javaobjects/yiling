package com.yiling.b2b.admin.coupon.from;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class CouponActivityOperateFrom extends BaseForm {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 当前操作人ID
     */
    private Long userId;

    /**
     * 当前企业ID
     */
    private Long eId;
}
