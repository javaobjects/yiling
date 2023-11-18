package com.yiling.f2b.admin.goods.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 GoodsLimitPriceVO
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsLimitPriceVO extends BaseVO {
    /**
     * 标准库ID
     */
    @ApiModelProperty(value = "标准库ID")
    private Long standardId;

    /**
     * 标准库售卖规格ID
     */
    @ApiModelProperty(value = "标准库售卖规格ID")
    private Long  sellSpecificationsId;

    /**
     * 价格上限
     */
    @ApiModelProperty(value = "价格上限")
    private BigDecimal upperLimitPrice;

    /**
     * 价格下限
     */
    @ApiModelProperty(value = "价格下限")
    private BigDecimal lowerLimitPrice;

    /**
     * 状态 0：正常
     */
    @ApiModelProperty(value = "状态 0：正常")
    private Integer status;
}
