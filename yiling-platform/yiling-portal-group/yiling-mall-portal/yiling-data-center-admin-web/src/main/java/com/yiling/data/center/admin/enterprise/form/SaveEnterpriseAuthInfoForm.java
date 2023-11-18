package com.yiling.data.center.admin.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业副本信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveEnterpriseAuthInfoForm extends BaseForm {

    private static final long serialVersionUID = 1L;

    /**
     * 企业名称
     */
    @NotEmpty
    @Length(max = 30)
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 联系人
     */
    @NotEmpty
    @Length(max = 5)
    @ApiModelProperty("联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @NotEmpty
    @Length(min = 11,max = 11)
    @ApiModelProperty("联系人电话")
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
    @Length(max = 50)
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 企业资质
     */
    @NotEmpty
    @ApiModelProperty("企业资质")
    private List<EnterpriseCertificateAuthInfoForm> certificateFormList;

}
