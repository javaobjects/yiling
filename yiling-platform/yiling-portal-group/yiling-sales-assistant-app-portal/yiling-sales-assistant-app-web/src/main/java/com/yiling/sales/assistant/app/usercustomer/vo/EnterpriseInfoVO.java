package com.yiling.sales.assistant.app.usercustomer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户信息 VO
 * @author:lun.yu
 * @date:2021/9/17
 */
@Data
public class EnterpriseInfoVO implements java.io.Serializable {

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private Integer type;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    /**
     * 社会统一信用代码
     */
    @ApiModelProperty(value = "社会统一信用代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    @ApiModelProperty(value = "所属城市编码")
    private String cityCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "所属城市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    @ApiModelProperty(value = "所属区域编码")
    private String regionCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty(value = "所属区域名称")
    private String regionName;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

}
