package com.yiling.hmc.admin.insurance.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建订单 Form
 *
 * @author: fan.shen
 * @date: 2022/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateOrderForm extends BaseForm {

    /**
     * 保单支付记录id
     */
    @ApiModelProperty("保单支付记录id")
    @NotNull
    private Long recordPayId;

    /**
     * 医生名称
     */
    @ApiModelProperty("医生名称")
    private String doctor;

    /**
     * 诊断结果
     */
    @ApiModelProperty("诊断结果")
    private String interrogationResult;

    /**
     * 处方快照url
     */
    @ApiModelProperty("处方快照url")
    private List<String> prescriptionSnapshotUrlList;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 订单票据，逗号分隔
     */
    @ApiModelProperty("订单票据，逗号分隔")
    private List<String> orderReceipts;

}
