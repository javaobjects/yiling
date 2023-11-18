package com.yiling.admin.erp.statistics.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecNoMatchedVO extends BaseVO {

    @ApiModelProperty(value = "待匹配商品名称")
    private String goodsName;

    @ApiModelProperty(value = "待匹配商品规格")
    private String spec;

    @ApiModelProperty(value = "待匹配商品厂家")
    private String manufacturer;

    @ApiModelProperty(value = "推荐商品规格Id")
    private Long recommendSpecificationId;

    @ApiModelProperty(value = "推荐商品名")
    private String recommendGoods;

    @ApiModelProperty(value = "推荐规格")
    private String recommendSpec;

    @ApiModelProperty(value = "推荐分数")
    private Long recommendScore;
}
