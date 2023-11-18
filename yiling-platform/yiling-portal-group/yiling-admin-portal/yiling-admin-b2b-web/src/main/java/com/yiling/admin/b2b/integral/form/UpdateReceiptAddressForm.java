package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 修改收货地址信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateReceiptAddressForm extends BaseForm {

    /**
     * 订单ID
     */
    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long id;

    /**
     * 收货人
     */
    @NotEmpty
    @ApiModelProperty(value = "收货人", required = true)
    private String contactor;

    /**
     * 联系电话
     */
    @NotEmpty
    @ApiModelProperty(value = "联系电话", required = true)
    private String contactorPhone;

    /**
     * 所属省份编码
     */
    @NotEmpty
    @ApiModelProperty(value = "所属省份编码", required = true)
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @NotEmpty
    @ApiModelProperty(value = "所属城市编码", required = true)
    private String cityCode;

    /**
     * 所属区域编码
     */
    @NotEmpty
    @ApiModelProperty(value = "所属区域编码", required = true)
    private String regionCode;

    /**
     * 详细地址
     */
    @NotEmpty
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

}