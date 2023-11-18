package com.yiling.sales.assistant.app.search.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 配送商信息
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DistributorInfoVO extends BaseVO {

    @ApiModelProperty("配送商名称")
    private String name;

    @ApiModelProperty("配送商Eid")
    private Long distributorEid;

    @ApiModelProperty("商品种类数量")
    private Long goodsKind;

    @ApiModelProperty("商品规格数量")
    private Long goodStandards;

    @ApiModelProperty("客户ID")
    private Long purchaseEid;

    @ApiModelProperty("店铺logo")
    private String logo;

    @ApiModelProperty("订单类型:1-pop,2-B2B")
    private Integer orderType;

    @ApiModelProperty("店铺起配金额:b2b以岭全品才有，pop无效")
    private BigDecimal startAmount;

    @ApiModelProperty("是否有参与任务")
    private Boolean    isHasTask;

    @ApiModelProperty(value = "满赠活动")
    private List<SimpleActivityVO> promotionActivitys;





}
