package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCouponActivityEnterpriseGiveFrom extends QueryPageListForm {

    /**
     * 优惠券活动ID
     */
    @NotNull
    @ApiModelProperty(value = "优惠券活动ID", required = true)
    private Long couponActivityId;

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

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private Integer etype;

    /**
     * 终端区域编码（所属省份编码）
     */
    @ApiModelProperty("终端区域编码（所属省份编码）")
    private String regionCode;

}
