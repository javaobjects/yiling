package com.yiling.sjms.gb.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 团购列表信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbFlowFormInfoListDTO extends GbFormInfoListDTO {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品code
     */
    private String goodsCode;
    /**
     * 商品规格
     */
    private String specification;

}
