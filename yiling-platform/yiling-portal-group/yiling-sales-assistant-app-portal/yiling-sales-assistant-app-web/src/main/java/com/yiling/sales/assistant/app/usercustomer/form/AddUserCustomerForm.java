package com.yiling.sales.assistant.app.usercustomer.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加用户客户 Form
 * 
 * @author lun.yu
 * @date 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddUserCustomerForm extends BaseForm {

    /**
     * 企业类型
     */
    @Min(1)
    @NotNull
    @ApiModelProperty(value = "企业类型", required = true)
    private Integer type;

    /**
     * 企业名称
     */
    @NotEmpty
    @Length(max = 30)
    @ApiModelProperty(value = "企业名称", required = true)
    private String name;

    /**
     * 社会统一信用代码
     */
    @NotEmpty
    @Length(max = 30)
    @ApiModelProperty(value = "社会统一信用代码", required = true)
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属省份编码", required = true)
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属省份名称", required = true)
    private String provinceName;

    /**
     * 所属城市编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属城市编码", required = true)
    private String cityCode;

    /**
     * 所属城市名称
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属城市名称", required = true)
    private String cityName;

    /**
     * 所属区域编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属区域编码", required = true)
    private String regionCode;

    /**
     * 所属区域名称
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属区域名称", required = true)
    private String regionName;

    /**
     * 详细地址
     */
    @NotEmpty
    @Length(max = 120)
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

    /**
     * 联系人
     */
    @Length(max = 10)
    @ApiModelProperty(value = "联系人", required = true)
    private String contactor;

    /**
     * 联系人电话
     */
    @NotEmpty
    @Length(max = 11)
    @Pattern(regexp = Constants.REGEXP_SPECIAL_MOBILE, message = "手机号格式不正确")
    @ApiModelProperty(value = "联系人电话", required = true)
    private String contactorPhone;

    /**
     * 企业资质列表
     */
    @ApiModelProperty(value = "企业资质列表")
    private List<CreateEnterpriseCertificateForm> certificateList;

}
