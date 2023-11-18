package com.yiling.f2b.admin.agreementv2.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Digits;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利任务量阶梯 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateTaskStageForm extends BaseForm {

    /**
     * 任务量
     */
    @ApiModelProperty("任务量")
    @Digits(integer = 8, fraction = 2, message = "任务量超出范围")
    private BigDecimal taskNum;

    /**
     * 任务量单位：1-元 2-盒
     */
    @ApiModelProperty("任务量单位：1-元 2-盒")
    private Integer taskUnit;

    /**
     * 超任务量汇总返
     */
    @ApiModelProperty("超任务量汇总返")
    @Digits(integer = 8, fraction = 2, message = "超任务量超出范围")
    private BigDecimal overSumBack;

    /**
     * 超任务量汇总返单位：1-元 2-%
     */
    @ApiModelProperty("超任务量汇总返单位：1-元 2-%")
    private Integer overSumBackUnit;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 协议返利阶梯集合
     */
    @ApiModelProperty("协议返利阶梯集合")
    private List<AddAgreementRebateStageForm> agreementRebateStageList;

}
