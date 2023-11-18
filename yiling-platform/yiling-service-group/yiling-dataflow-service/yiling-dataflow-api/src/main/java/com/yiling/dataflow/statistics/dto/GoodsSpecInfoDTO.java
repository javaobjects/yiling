package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecInfoDTO extends BaseDTO {

    /**
     * 商品+规格id
     */
    private Long specificationId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String spec;
}
