package com.yiling.b2b.app.coupon.vo;

import java.util.List;

import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Data
@Accessors(chain = true)
public class MyCouponCanUseGoodsListVO extends BaseVO {

    @ApiModelProperty(value = "总数")
    private long total;

    @ApiModelProperty(value = "每页显示条数")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页")
    private Integer current = 1;

    @ApiModelProperty(value = "商品信息")
    List<GoodsItemVO> data;

    @ApiModelProperty(value = "优惠券不可用原因")
    private String unAvailableMessage;
}
