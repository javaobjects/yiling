package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * 编辑保存
 */
@Data
public class EditCrmAgencyForm extends BaseForm {
    /**
     * 机构名称
     */
    @Length(max = 200)
    @ApiModelProperty("机构名称")
    private String name;
    @Length(max = 18)
    @ApiModelProperty("社会信用统一代码")
    private String licenseNumber;
    /**
     * 药品经营许可证编号
     */
    @Length(max = 50)
    @ApiModelProperty("药品经营许可证编号")
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    @ApiModelProperty("医机构执业许可证")
    private String institutionPracticeLicense;

    /**
     * 以岭编码
     */
    @Length(max = 50)
    @ApiModelProperty("以岭编码")
    private String ylCode;

    /**
     * 经营范围
     */
    @Length(max = 1000)
    @ApiModelProperty("经营范围")
    private String businessScope;

    /**
     * 备注
     */
    @Length(max = 200)
    @ApiModelProperty("备注")
    private String businessRemark;

    /**
     * 曾用名
     */
    @Length(max = 500)
    @ApiModelProperty("曾用名")
    private String formerName;

    /**
     *所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;
    /**
     *所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;
    /**
     *所属区区域名称
     */
    @ApiModelProperty("所属区区域名称")
    private String regionName;
    /**
     *详细地址
     */
    @Length(max = 200)
    @ApiModelProperty("详细地址")
    private String address;
    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 电话
     */
    @Length(max = 20)
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 传真
     */
    @Length(max = 20)
    @ApiModelProperty("传真")
    private String fax;

    /**
     * 邮编
     */
    @Length(max = 20)
    @ApiModelProperty("邮编")
    private String postalCode;

    /**
     * 业务状态 1有效 2失效
     */
    @ApiModelProperty("业务状态 1有效 2失效")
    private Integer businessCode;

    /**
     * 机构简称
     */
    @ApiModelProperty("机构简称")
    private String shortName;
}