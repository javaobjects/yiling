package com.yiling.hmc.common.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 广告信息
 *
 * @author: yong.zhang
 * @date: 2022/3/23
 */
@Data
@Accessors(chain = true)
@ApiModel("广告信息VO")
public class AdvertisementVO extends BaseVO {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图片地址")
    private String pic;

    @ApiModelProperty(value = "链接地址")
    private String url;

}
