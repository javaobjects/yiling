package com.yiling.hmc.address.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/16
 */
@Data
@Accessors(chain = true)
@ApiModel("收货地址信息VO")
public class QueryAddressVO extends BaseVO {

    /**
     * 省份编码
     */
    @ApiModelProperty("省份编码")
    private String provinceCode;

    @ApiModelProperty("省名称")
    private String provinceName;

    /**
     * 市code
     */
    @ApiModelProperty("市code")
    private String cityCode;

    @ApiModelProperty("市名称")
    private String cityName;

    /**
     * 区县编码
     */
    @ApiModelProperty("区县编码")
    private String regionCode;

    @ApiModelProperty("区县名称")
    private String regionName;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 收货人姓名
     */
    @ApiModelProperty("收货人姓名")
    private String name;

    /**
     * 收货人手机号
     */
    @ApiModelProperty("收货人手机号")
    private String mobile;

    /**
     * 是否默认：0-否 1-是
     */
    @ApiModelProperty("是否默认：0-否 1-是")
    private Integer defaultFlag;
}
