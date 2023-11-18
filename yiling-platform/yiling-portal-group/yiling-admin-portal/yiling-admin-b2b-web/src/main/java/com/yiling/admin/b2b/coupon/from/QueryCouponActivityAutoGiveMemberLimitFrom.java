package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
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
public class QueryCouponActivityAutoGiveMemberLimitFrom extends QueryPageListForm {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 自动发券活动ID
     */
    @NotNull
    @ApiModelProperty(value = "自动发券活动ID", required = true)
    private Long couponActivityAutoGiveId;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

}
