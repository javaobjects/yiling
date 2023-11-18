package com.yiling.f2b.admin.agreementv2.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利-规模返利阶梯 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateScaleRebateForm extends BaseForm {

    /**
     * 目标达成率
     */
    @ApiModelProperty("目标达成率")
    private BigDecimal targetReachRatio;

    /**
     * 目标达成率单位：1-%
     */
    @ApiModelProperty("目标达成率单位：1-%")
    private Integer reachRatioUnit;

    /**
     * 目标返利率
     */
    @ApiModelProperty("目标返利率")
    private BigDecimal targetRebateRatio;

    /**
     * 目标返利率单位：1-%
     */
    @ApiModelProperty("目标返利率单位：1-%")
    private Integer rebateRatioUnit;

}
