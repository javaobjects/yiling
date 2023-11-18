package com.yiling.admin.b2b.coupon.from;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMemberCouponActivityBasicFrom extends SaveCouponActivityBasicFrom {

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用 3-废弃")
    private Integer status;

    /**
     * 会员规格id数组
     */
    @ApiModelProperty(value = "会员规格id数组/当删除的时候表示列表id数组")
    private List<Long> ids;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String memberName;
}
