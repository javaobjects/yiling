package com.yiling.b2b.app.lotteryactivity.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动-奖品收货地址 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveDeliveryAddressForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 收货人
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "收货人", required = true)
    private String receiver;

    /**
     * 手机号
     */
    @NotEmpty
    @Length(max = 11)
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 省份编码
     */
    @NotEmpty
    @ApiModelProperty(value = "省份编码", required = true)
    private String provinceCode;

    /**
     * 城市编码
     */
    @NotEmpty
    @ApiModelProperty(value = "城市编码", required = true)
    private String cityCode;

    /**
     * 区域编码
     */
    @NotEmpty
    @ApiModelProperty(value = "区域编码", required = true)
    private String regionCode;

    /**
     * 详细地址
     */
    @NotEmpty
    @Length(max = 200)
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

}
