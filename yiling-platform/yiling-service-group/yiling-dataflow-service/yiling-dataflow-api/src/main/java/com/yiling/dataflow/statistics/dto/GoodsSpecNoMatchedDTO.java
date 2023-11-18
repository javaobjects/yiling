package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecNoMatchedDTO  extends BaseDTO {

    /**
     * 待匹配商品名称
     */
    private String goodsName;

    /**
     * 待匹配商品规格
     */
    private String spec;

    /**
     * 待匹配商品规格
     */
    private String manufacturer;

    /**
     * 推荐商品规格id
     */
    private Long recommendSpecificationId;

    /**
     * 推荐商品名
     */
    private String recommendGoods;

    /**
     * 推荐规格
     */
    private String recommendSpec;

    /**
     * 推荐分数
     */
    private Long recommendScore;
}
