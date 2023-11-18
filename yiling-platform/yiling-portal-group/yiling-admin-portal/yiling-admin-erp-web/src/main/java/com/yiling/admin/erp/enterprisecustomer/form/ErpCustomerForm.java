package com.yiling.admin.erp.enterprisecustomer.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 ErpCustomerForm
 * @描述
 * @创建时间 2022/1/13
 * @修改人 shichen
 * @修改时间 2022/1/13
 **/
@Data
public class ErpCustomerForm {

    @ApiModelProperty(value = "open库id")
    private Long id;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long suId;

    /**
     * 供应商部门
     */
    @ApiModelProperty(value = "供应商部门")
    private String suDeptNo;

    /**
     * 企业内码
     */
    @ApiModelProperty(value = "企业内码")
    private String innerCode;

    /**
     * 企业编码
     */
    @ApiModelProperty(value = "企业编码")
    private String   sn;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String   name;

    /**
     * 企业分组名称
     */
    @ApiModelProperty(value = "企业分组名称")
    private String   groupName;

    /**
     * 营业执照号
     */
    @ApiModelProperty(value = "营业执照号")
    private String   licenseNo;

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "终端类型")
    private String   customerType;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String   contact;

    /**
     * 区号-号码
     */
    @ApiModelProperty(value = "区号-号码")
    private String   phone;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String   province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String   city;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
    private String   region;

    /**
     * 省份编码
     */
    @ApiModelProperty(value = "省份编码")
    private String   provinceCode;

    /**
     * 市编码
     */
    @ApiModelProperty(value = "市编码")
    private String   cityCode;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String   regionCode;

    /**
     * 企业地址
     */
    @ApiModelProperty(value = "企业地址")
    private String   address;

}
