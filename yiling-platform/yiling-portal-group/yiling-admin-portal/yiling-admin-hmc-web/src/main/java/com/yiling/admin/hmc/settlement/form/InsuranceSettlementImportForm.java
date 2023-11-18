package com.yiling.admin.hmc.settlement.form;

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
@ApiModel(value = "EnterpriseSettlementImportForm", description = "保司导入结算数据")
public class InsuranceSettlementImportForm extends BaseForm {

    @ApiModelProperty(value = "文件")
    private MultipartFile file;
}