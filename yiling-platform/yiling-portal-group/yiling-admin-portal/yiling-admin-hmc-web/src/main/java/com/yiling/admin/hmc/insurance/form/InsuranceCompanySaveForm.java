package com.yiling.admin.hmc.insurance.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险服务商新增和修改
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceCompanySaveForm extends BaseForm {

    @ApiModelProperty("保险服务商id")
    private Long id;

    @ApiModelProperty("保险服务商名称")
    @NotBlank(message = "保险服务商名称不能为空")
    private String companyName;

    @ApiModelProperty("执业许可证号/社会信用统一代码")
    @NotBlank(message = "执业许可证号不能为空")
    private String insuranceNo;

    @ApiModelProperty("保险服务商状态 1-启用 2-停用")
    private Integer status;

    @ApiModelProperty("所属省份编码")
    @NotBlank(message = "所属省份编码不能为空")
    private String provinceCode;

    @ApiModelProperty("所属城市编码")
    @NotBlank(message = "所属城市编码不能为空")
    private String cityCode;

    @ApiModelProperty("所属区域编码")
    @NotBlank(message = "所属区域编码不能为空")
    private String regionCode;

    @ApiModelProperty("详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String address;

    @ApiModelProperty("联系人姓名")
    private String contactName;

    @ApiModelProperty("退保客服电话")
    @NotBlank(message = "退保客服电话不能为空")
    private String cancelInsuranceTelephone;

    @ApiModelProperty("退保地址")
    private String cancelInsuranceAddress;

    @ApiModelProperty("续保地址")
    private String renewInsuranceAddress;

    @ApiModelProperty("互联网问诊地址")
    private String internetConsultationUrl;

    @ApiModelProperty("代理理赔协议地址")
    private String claimProtocolUrl;

    @ApiModelProperty("备注")
    private String remark;
}
