package com.yiling.f2b.web.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsHealthVO extends BaseVO {
    /**
     * 原料
     */
    @ApiModelProperty(value = "原料")
    private String rawMaterial;

    /**
     * 辅料
     */
    @ApiModelProperty(value = "辅料")
    private String ingredients;

    /**
     * 适宜人群
     */
    @ApiModelProperty(value = "适宜人群")
    private String suitablePeople;

    /**
     * 不适宜人群
     */
    @ApiModelProperty(value = "不适宜人群")
    private String unsuitablePeople;

    /**
     * 保健功能
     */
    @ApiModelProperty(value = "保健功能")
    private String healthcareFunction;

    /**
     * 保质期
     */
    @ApiModelProperty(value = "保质期")
    private String expirationDate;

    /**
     * 食用量及食用方法
     */
    @ApiModelProperty(value = "食用量及食用方法")
    private String usageDosage;

    /**
     * 储藏
     */
    @ApiModelProperty(value = "储藏")
    private String store;

    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String noteEvents;

}
