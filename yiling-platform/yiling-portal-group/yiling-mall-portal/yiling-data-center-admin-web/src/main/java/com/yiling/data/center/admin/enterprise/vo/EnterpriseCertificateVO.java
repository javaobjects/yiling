package com.yiling.data.center.admin.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业资质信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/10/25
 */
@Data
public class EnterpriseCertificateVO extends BaseVO {

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
