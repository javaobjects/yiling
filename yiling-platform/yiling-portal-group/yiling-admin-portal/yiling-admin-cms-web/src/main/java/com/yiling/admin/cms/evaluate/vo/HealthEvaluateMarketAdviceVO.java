package com.yiling.admin.cms.evaluate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评-改善建议
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Data
@Accessors(chain = true)
public class HealthEvaluateMarketAdviceVO {

    private Long id;

    /**
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

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String picUrl;
}
