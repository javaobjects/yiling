package com.yiling.admin.data.center.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
@Accessors(chain = true)
public class MatchGoodsVO {

    /**
     * 匹配的状态
     */
    @ApiModelProperty(value = "匹配的状态")
    private Integer matchStatus;

    /**
     * 标准库商品ID
     */
    @ApiModelProperty(value = "标准库商品ID")
    private Long standardId;

    /**
     * 售卖规格ID
     */
    @ApiModelProperty(value = "售卖规格ID")
    private Long sellSpecificationsId;

    @ApiModelProperty(value = "待匹配商品信息")
    private GoodsAuditListVO goodsAuditListVO;

}
