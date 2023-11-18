package com.yiling.sjms.gb.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGBGoodsInfoPageRequest extends QueryPageListRequest {

    /**
     * 商品名称
     */
    private String name;
}
