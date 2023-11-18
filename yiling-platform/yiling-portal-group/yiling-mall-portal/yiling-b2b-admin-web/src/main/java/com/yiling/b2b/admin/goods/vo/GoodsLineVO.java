package com.yiling.b2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsLineVO extends BaseVO {

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

    /**
     * b2b是否开通：0不开通 1开通
     */
    @ApiModelProperty(value = "b2b是否开通：0不开通 1开通")
    private Integer mallFlag;

    /**
     * pop是否开通：0不开通 1开通
     */
    @ApiModelProperty(value = "pop是否开通：0不开通 1开通")
    private Integer popFlag;
}
