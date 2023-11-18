package com.yiling.admin.hmc.settlement.form;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/4/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "EnterpriseSettlementImportForm", description = "商家终端订单明细已结导入")
public class EnterpriseSettlementImportForm extends BaseForm {

    @ApiModelProperty(value = "文件")
    private MultipartFile file;

    @ApiModelProperty(value = "企业终端id")
    @NotNull(message = "导入企业不能为空")
    private Long eid;
}