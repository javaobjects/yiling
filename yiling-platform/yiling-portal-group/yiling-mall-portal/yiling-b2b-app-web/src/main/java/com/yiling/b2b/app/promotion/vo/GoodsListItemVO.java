package com.yiling.b2b.app.promotion.vo;

import java.util.List;

import com.yiling.b2b.app.goods.vo.GoodsSkuVO;
import com.yiling.common.web.goods.vo.GoodsItemVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsListItemVO extends GoodsItemVO {

    /**
     * 销售包装
     */
    @ApiModelProperty(value = "销售包装")
    private List<GoodsSkuVO> goodsSkuList;

    /**
     * 是否有秒杀活动
     */
    @ApiModelProperty(value = "是否有秒杀活动")
    private Boolean          hasSecKill     = false;

    /**
     * 是否有特价活动
     */
    @ApiModelProperty(value = "是否有特价活动")
    private Boolean          hasSpecial     = false;

}
