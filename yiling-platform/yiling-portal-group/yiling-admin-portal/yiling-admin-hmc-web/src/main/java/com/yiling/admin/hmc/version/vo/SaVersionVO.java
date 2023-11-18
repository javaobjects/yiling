package com.yiling.admin.hmc.version.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaVersionVO extends BaseDTO {
    @ApiModelProperty(value = "版本名称")
    private String name;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "版本描述")
    private String description;

    @ApiModelProperty(value = "版本平台：0-安卓 1-IOS")
    private Integer appType;

    @ApiModelProperty(value = "APP安装包地址")
    private String packageUrl;

    private Long createUser;

    @ApiModelProperty(value = "发布人名称")
    private String createUserName;

    @ApiModelProperty(value = "发布时间")
    private Date createTime;
}
