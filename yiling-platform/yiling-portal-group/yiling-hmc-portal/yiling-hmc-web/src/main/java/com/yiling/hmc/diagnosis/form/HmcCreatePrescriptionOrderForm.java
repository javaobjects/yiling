package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * HMC创建处方订单form
 *
 * @author: fan.shen
 * @data: 2023/05/11
 */
@Data
public class HmcCreatePrescriptionOrderForm {

    @NotNull
    @ApiModelProperty("处方id")
    private Integer prescriptionId;

    @ApiModelProperty("处方类型 1：西药 0：中药")
    private Integer prescriptionType;

    @NotNull
    @ApiModelProperty("处方价格")
    private BigDecimal prescriptionPrice;

    @ApiModelProperty("健康中心商家id")
    @NotNull
    private Long hmcEid;

    @ApiModelProperty("健康中心商家名称")
    private String hmcEname;

    @ApiModelProperty("互联网医院商家id")
    @NotNull
    private Long ihEid;

    @ApiModelProperty("IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC")
    private Integer ihPharmacySource;

    @ApiModelProperty("互联网医院商家名称")
    private String ihEname;

    @NotNull
    @ApiModelProperty("收货地址id")
    private Integer addressId;

    @ApiModelProperty("备注")
    private String remark;

}
