package com.yiling.hmc.medinstruct.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
public class StandardGoodsAllInfoVO {

    /**
     * 基础信息和其他信息
     */
    @ApiModelProperty(value = "基础信息和其他信息")
    private StandardGoodsBasicInfoVO baseInfo;

    /**
     * 药品说明书信息
     */
    @ApiModelProperty(value = "药品说明书信息")
    private StandardInstructionsGoodsVO goodsInstructionsInfo;

    /**
     * 图片链接
     */
    @ApiModelProperty(value = "图片链接")
    private String picUrl;


}
