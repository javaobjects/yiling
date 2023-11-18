package com.yiling.hmc.goods.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 EnterpriseGoodsCountBO
 * @描述  c端商品统计
 * @创建时间 2022/3/31
 * @修改人 shichen
 * @修改时间 2022/3/31
 **/
@Data
public class EnterpriseGoodsCountBO implements Serializable {
    private Long eid;

    private Long goodsCount;

    private Long upGoodsCount;

    private Long unGoodsCount;
}
