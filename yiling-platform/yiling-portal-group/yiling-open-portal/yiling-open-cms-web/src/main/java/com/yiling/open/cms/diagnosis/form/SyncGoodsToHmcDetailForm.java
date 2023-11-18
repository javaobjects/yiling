package com.yiling.open.cms.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 同步商品到HMC form
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SyncGoodsToHmcDetailForm extends BaseForm {

    @NotNull
    @ApiModelProperty("中台标准库id")
    private Long standardId;

    @NotNull
    @ApiModelProperty("中台售卖规格id")
    private Long sellSpecificationsId;

    @NotNull
    @ApiModelProperty("C端平台药房id")
    private Long ihCPlatformId;

    @NotNull
    @ApiModelProperty("IH 配送商商品ID")
    private Long ihPharmacyGoodsId;

    @NotNull
    @ApiModelProperty("IH 配送商id")
    private Long ihEid;

}