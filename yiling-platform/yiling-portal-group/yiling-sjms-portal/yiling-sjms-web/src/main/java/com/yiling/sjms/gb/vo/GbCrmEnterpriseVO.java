package com.yiling.sjms.gb.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GbCrmEnterpriseVO extends BaseVO {

    /**
     * crm系统对应客户编码
     */
    @ApiModelProperty(value = "crm系统对应客户编码")
    private String code;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty(value = "crm系统对应客户名称")
    private String name;

    /**
     * 省名称
     */
    @ApiModelProperty(value = "省名称")
    private String provinceName;

    /**
     * 市名称
     */
    @ApiModelProperty(value = "市名称")
    private String cityName;

    /**
     *区名称
     */
    @ApiModelProperty(value = "区名称")
    private String regionName;
}
