package com.yiling.sales.assistant.app.usercustomer.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * 企业资质 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@ApiOperation("企业资质")
public class EnterpriseCertificateVO implements java.io.Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("资质类型")
    private Integer type;

    @ApiModelProperty("资质名称")
    private String name;

    @ApiModelProperty("资质有效期是否必填")
    private Boolean periodRequired;

    @ApiModelProperty("资质是否必填")
    private Boolean required;

    @ApiModelProperty("资质文件URL")
    private String fileUrl;

    @ApiModelProperty("资质文件KEY")
    private String fileKey;

    @ApiModelProperty("资质有效期-起")
    private Date periodBegin;

    @ApiModelProperty("资质有效期-止")
    private Date periodEnd;

    @ApiModelProperty("是否长期有效：0-否 1-是")
    private Integer longEffective;
}
