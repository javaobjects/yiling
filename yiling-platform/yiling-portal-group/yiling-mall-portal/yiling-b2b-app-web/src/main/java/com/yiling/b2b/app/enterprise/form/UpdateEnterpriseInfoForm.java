package com.yiling.b2b.app.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改企业资质管理信息 Form
 *
 * @author: lun.yu
 * @date: 2021/10/21
 */
@Data
@ApiModel
public class UpdateEnterpriseInfoForm extends BaseForm {

    /**
     * 企业名称
     */
    @NotNull
    @Length(max = 50)
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 联系人
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "手机号格式不正确")
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    /**
     * 所属省份编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty("所属省份名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属城市名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty("所属城市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 所属区域名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty("所属区域名称")
    private String regionName;

    /**
     * 详细地址
     */
    @NotEmpty
    @Length(max = 200)
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @NotEmpty
    @Length(max = 30)
    @ApiModelProperty(value = "执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 企业资质集合
     */
    @ApiModelProperty(value = "企业资质集合")
    private List<EnterpriseCertificateForm> certificateFormList;

}
