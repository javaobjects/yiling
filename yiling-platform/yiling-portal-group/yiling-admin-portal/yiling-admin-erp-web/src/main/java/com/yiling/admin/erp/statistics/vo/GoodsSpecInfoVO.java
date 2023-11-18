package com.yiling.admin.erp.statistics.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecInfoVO extends BaseVO {

    @ApiModelProperty("商品+规格id")
    private Long specificationId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品规格")
    private String spec;

    @ApiModelProperty("商品名称+规格")
    private String goodsNameSpecDesc;
}
