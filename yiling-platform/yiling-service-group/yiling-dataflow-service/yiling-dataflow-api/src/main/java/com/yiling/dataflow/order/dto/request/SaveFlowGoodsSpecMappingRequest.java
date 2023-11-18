package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/8/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowGoodsSpecMappingRequest extends BaseRequest {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String spec;

    private String manufacturer;
}
