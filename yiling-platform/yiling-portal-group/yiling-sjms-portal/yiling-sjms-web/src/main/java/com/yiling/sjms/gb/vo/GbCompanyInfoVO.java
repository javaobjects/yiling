package com.yiling.sjms.gb.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 团购出库信息
 */
@Data
public class GbCompanyInfoVO {

    @ApiModelProperty(value = "团购出库终端ID")
    private String termainalCompanyId;

    /**
     * 团购出库终端编码
     */
    @ApiModelProperty(value = "团购出库终端编码")
    private String termainalCompanyCode;

    /**
     * 团购出库终端名称
     */
    @ApiModelProperty(value = "团购出库终端名称")
    private String termainalCompanyName;

    /**
     * 购出库终端省区名称
     */
    @ApiModelProperty(value = "购出库终端省区名称")
    private String termainalCompanyProvince;

    /**
     * 购出库终端市区名称
     */
    @ApiModelProperty(value = "购出库终端市区名称")
    private String termainalCompanyCity;

    /**
     * 购出库终端区县名称
     */
    @ApiModelProperty(value = "购出库终端区县名称")
    private String termainalCompanyRegion;


    @ApiModelProperty(value = "团购出库商业ID")
    private String businessCompanyId;

    /**
     * 团购出库商业编码
     */
    @ApiModelProperty(value = "团购出库商业编码")
    private String businessCompanyCode;

    /**
     * 团购出库商业名称
     */
    @ApiModelProperty(value = "团购出库商业名称")
    private String businessCompanyName;


    /**
     * 团购出库商业省区名称
     */
    @ApiModelProperty(value = "团购出库商业省区名称")
    private String businessCompanyProvince;

    /**
     * 团购出库商业市区名称
     */
    @ApiModelProperty(value = "团购出库商业市区名称")
    private String businessCompanyCity;

    /**
     * 团购出库商业区县名称
     */
    @ApiModelProperty(value = "团购出库商业区县名称")
    private String businessCompanyRegion;

    @ApiModelProperty(value = "商品信息")
    private List<GbGoodsInfoVO> gbGoodsInfoList;

}
