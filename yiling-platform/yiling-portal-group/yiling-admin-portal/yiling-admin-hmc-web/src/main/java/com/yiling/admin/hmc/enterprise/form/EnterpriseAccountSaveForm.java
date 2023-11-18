package com.yiling.admin.hmc.enterprise.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseAccountSaveForm extends BaseForm {

    @ApiModelProperty("保险药品商家结算账号表id")
    private Long id;

    @ApiModelProperty("商家id")
    @NotNull(message = "没有选择商家")
    private Long eid;

    @ApiModelProperty("商家名称")
    @NotBlank(message = "商家名称不能为空")
    private String ename;

    @ApiModelProperty("账户类型 1-对公账户 2-对私账户")
    @NotNull(message = "账户类型选择错误")
    @Max(value = 2, message = "账户类型不规范")
    @Min(value = 1, message = "账户类型不规范")
    private Integer accountType;

    @ApiModelProperty("账户名")
    @NotBlank(message = "账户名不能为空")
    private String accountName;

    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空")
    private String accountNumber;

    @ApiModelProperty("开户行")
    @NotBlank(message = "开户行不能为空")
    private String accountBank;

    @ApiModelProperty("备注")
    private String remark;
}
