package com.yiling.b2b.app.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册企业资质信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/10/25
 */
@Data
public class RegistEnterpriseCertificateVO {

    @ApiModelProperty("资质类型")
    private Integer type;

    @ApiModelProperty("资质名称")
    private String name;

    @ApiModelProperty("资质有效期是否必填")
    private Boolean periodRequired;

    @ApiModelProperty("资质是否必填")
    private Boolean required;
}
