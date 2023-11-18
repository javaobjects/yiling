package com.yiling.dataflow.statistics.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class RecommendScoreRequest extends BaseRequest {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String spec;

    /**
     * 商品厂家
     */
    private String manufacturer;

    /**
     * 需要对比的商品名称
     */
    private String targetGoodsName;

    /**
     * 需要对比的商品规格
     */
    private String targetSpec;

    /**
     * 需要对比的商品规格
     */
    private String targetManufacturer;
}
