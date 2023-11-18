package com.yiling.mall.hotwords.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsHotWordsAvailableDTO extends BaseDTO {
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 热词名称
     */
    private String name;


}
