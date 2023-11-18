package com.yiling.b2b.admin.shop.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-店铺设置 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveShopForm extends BaseForm {

    /**
     * 店铺logo
     */
    @NotNull
    @ApiModelProperty("店铺logo")
    private String shopLogo;

    /**
     * 店铺简介
     */
    @NotNull
    @Length(max = 1200)
    @ApiModelProperty("店铺简介")
    private String shopDesc;

    /**
     * 起配金额
     */
    @NotNull
    @DecimalMin(value = "0.00",message = "起配金额不能小于0")
    @Digits(integer = 10, fraction = 2,message = "起配金额超出范围")
    @ApiModelProperty("起配金额")
    private BigDecimal startAmount;

    /**
     * 支付方式
     */
    @NotEmpty
    @ApiModelProperty("支付方式")
    private List<Integer> paymentMethodList;

    /**
     * 店铺区域编码
     */
    @NotEmpty
    @ApiModelProperty("店铺区域编码Json串")
    private String areaJsonString;

}
