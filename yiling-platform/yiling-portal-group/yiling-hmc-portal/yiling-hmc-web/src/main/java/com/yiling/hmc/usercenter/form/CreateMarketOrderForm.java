package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 确认订单 Form
 *
 * @author: fan.shen
 * @date: 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateMarketOrderForm extends BaseForm {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "eid", hidden = true)
    private Long eid ;

    /**
     * 活动id
     */
    @ApiModelProperty("活动id")
    private Long activityId;

    /**
     * 医生id
     */
    @ApiModelProperty("医生id")
    private Long doctorId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", hidden = true)
    private Long goodsId;

    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Long goodsQuantity;

    /**
     * 地址id
     */
    @ApiModelProperty("地址id")
    private Long addressId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
