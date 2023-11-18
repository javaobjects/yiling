package com.yiling.hmc.admin.order.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存处方信息请求参数
 *
 * @author: yong.zhang
 * @date: 2022/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPrescriptionSaveForm extends BaseForm {

    @ApiModelProperty("订单id")
    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @ApiModelProperty("开方医生")
    private String doctor;

    @ApiModelProperty("诊断结果")
    private String interrogationResult;

    @ApiModelProperty("处方图片")
    @NotEmpty(message = "处方图片为空")
    private List<String> prescriptionSnapshotUrlList;
}
