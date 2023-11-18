package com.yiling.admin.cms.evaluate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 健康测评营销
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-28
 */
@Data
@Accessors(chain = true)
public class HealthEvaluateGoodsInfoVO {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 适应症
     */
    @ApiModelProperty(value = "适应症")
    private String indications;

}
