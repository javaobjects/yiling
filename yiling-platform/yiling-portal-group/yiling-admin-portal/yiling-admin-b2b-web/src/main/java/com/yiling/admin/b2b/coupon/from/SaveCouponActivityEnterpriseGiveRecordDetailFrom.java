package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityEnterpriseGiveRecordDetailFrom extends BaseForm {

    /**
     * 优惠券活动ID
     */
    @ApiModelProperty("优惠券活动ID")
    @NotNull
    private Long couponActivityId;

    /**
     * 企业id
     */
    @NotNull
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 企业名称
     */
    @NotEmpty
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private Integer etype;

    /**
     * 终端区域编码
     */
    @ApiModelProperty("终端区域编码")
    private String regionCode;

    /**
     * 终端区域名称
     */
    @ApiModelProperty("终端区域名称")
    private String regionName;

    /**
     * 认证状态（1-未认证 2-认证通过 3-认证不通过）
     */
    @ApiModelProperty("认证状态（1-未认证 2-认证通过 3-认证不通过）")
    private Integer authStatus;

    /**
     * 发放数量
     */
    @ApiModelProperty("发放数量")
    private Integer giveNum;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
