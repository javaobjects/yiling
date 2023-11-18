package com.yiling.hmc.evaluate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 测评结果
 *
 * @author: fan.shen
 * @date: 2022-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "测评结果")
public class HealthEvaluateMarketAdviceVO {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    private Long healthEvaluateId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String sourceDesc;

    /**
     * 更多跳转链接
     */
    @ApiModelProperty(value = "更多跳转链接")
    private String moreJumpUrl;

    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String pic;


}
