package com.yiling.sales.assistant.app.message.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

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
public class SaVersionVO extends BaseVO {
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

    @ApiModelProperty(value = "安装包MD5")
    private String packageMd5;

    @ApiModelProperty(value = "安装包大小(KB)")
    private Long packageSize;

    @ApiModelProperty(value = "APP地址")
    private String appUrl;

    @ApiModelProperty(value = "是否强制升级：1 是 ，2 否")
    private Integer forceUpgradeFlag;

    @ApiModelProperty(value = "升级提示语")
    private String upgradeTips;

    @ApiModelProperty(value = "发布人id")
    private Long createUser;

    @ApiModelProperty(value = "发布时间")
    private Date createTime;
}
