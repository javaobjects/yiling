package com.yiling.sjms.gb.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存出库终端和商业信息
 */
@Data
public class SaveGBCompanyRelationForm  extends BaseForm {

    @ApiModelProperty(value = "团购出库终端ID")
    private Long termainalCompanyId;

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
    private Long  businessCompanyId;


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

    /**
     * 保存商品信息
     */
    @ApiModelProperty(value = "保存商品信息")
    private List<SaveGBGoodsInfoForm> gbGoodsInfoList;
}
