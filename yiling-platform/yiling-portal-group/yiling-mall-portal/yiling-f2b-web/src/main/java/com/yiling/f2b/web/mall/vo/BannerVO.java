package com.yiling.f2b.web.mall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
@Data
public class BannerVO {

    /**
     * banner标题
     */
    @ApiModelProperty(value = "banner标题")
    private String title;

    /**
     * 链接url
     */
    @ApiModelProperty(value = "链接url")
    private String linkUrl;

    /**
     * banner图片值
     */
    @ApiModelProperty(value = "banner图片值")
    private String pic;
}
