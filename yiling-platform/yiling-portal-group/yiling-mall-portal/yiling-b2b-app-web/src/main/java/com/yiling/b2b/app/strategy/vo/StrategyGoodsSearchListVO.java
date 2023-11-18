package com.yiling.b2b.app.strategy.vo;

import java.util.List;

import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/9/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyGoodsSearchListVO extends BaseVO {

    @ApiModelProperty(value = "总数")
    private long total;

    @ApiModelProperty(value = "每页显示条数")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页")
    private Integer current = 1;

    @ApiModelProperty(value = "商品信息")
    List<GoodsItemVO> data;

    @ApiModelProperty(value = "活动名称")
    private String title;
}
