package com.yiling.hmc.goods.dto.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @author shichen
 * @类名 QueryHmcGoodsRequest
 * @描述
 * @创建时间 2022/4/2
 * @修改人 shichen
 * @修改时间 2022/4/2
 **/
@Data
public class QueryHmcGoodsRequest implements Serializable {
    /**
     * c端goodsId(hmc_goods表)
     */
    private Long id;

    /**
     * 商品id（goods表）
     */
    private Long goodsId;

    /**
     * 商家id
     */
    private Long eid;
    /**
     * 标准库规格id
     */
    private Long sellSpecificationsId;

    /**
     * 商品状态 1上架，2下架
     */
    private Integer goodsStatus;

}
