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
public class HealthEvaluateMarketGoodsVO {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "心理")
    private Long healthEvaluateId;

    /**
     * 标准库ID
     */
    @ApiModelProperty(value = "标准库ID")
    private Long standardId;

    /**
     * 结果排序
     */
    @ApiModelProperty(value = "结果排序")
    private Integer resultRank;

    /**
     * 适应症
     */
    @ApiModelProperty(value = "适应症")
    private String indications;

    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;

    /**
     * 图片url
     */
    @ApiModelProperty(value = "图片url")
    private String picUrl;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

}
