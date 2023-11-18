package com.yiling.f2b.admin.agreementv2.form;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利阶梯 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateStageForm extends BaseForm {

    /**
     * 返利任务量阶梯ID
     */
    @ApiModelProperty("返利任务量阶梯ID")
    private Long taskStageId;

    /**
     * 满
     */
    @ApiModelProperty("满")
    @Digits(integer = 8, fraction = 2,message = "满超出范围")
    private BigDecimal full;

    /**
     * 满单位：1-元 2-盒
     */
    @ApiModelProperty("满单位：1-元 2-盒")
    private Integer fullUnit;

    /**
     * 返
     */
    @ApiModelProperty("返")
    @Digits(integer = 8, fraction = 2,message = "返超出范围")
    private BigDecimal back;

    /**
     * 返单位：1-元 2-%
     */
    @ApiModelProperty("返单位：1-元 2-%")
    private Integer backUnit;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

}
