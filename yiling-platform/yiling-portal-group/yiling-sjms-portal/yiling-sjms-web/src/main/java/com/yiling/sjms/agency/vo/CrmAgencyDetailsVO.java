package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CrmAgencyDetailsVO extends BaseVO {
    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String name;
    /**
     * CRM编码
     */
    @ApiModelProperty("CRM编码")
    private String code;
    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private String licenseNumber;
    /**
     * 药品经营许可证编号
     */
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
    @ApiModelProperty("以岭编码")
    private String ylCode;

    /**
     * 经营范围
     */
    @ApiModelProperty("经营范围")
    private String businessScope;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String businessRemark;

    /**
     * 曾用名
     */
    @ApiModelProperty("曾用名")
    private String formerName;

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
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 传真
     */
    @ApiModelProperty("传真")
    private String fax;

    /**
     * 邮编
     */
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
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 供应链角色：1-商业公司 2-医院 3-零售药店。数据字典：erp_crm_supply_chain_role
     */
    private Integer supplyChainRole;
}
