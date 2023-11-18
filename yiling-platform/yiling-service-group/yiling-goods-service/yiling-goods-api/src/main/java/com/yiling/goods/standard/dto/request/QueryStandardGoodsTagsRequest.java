package com.yiling.goods.standard.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;

/**
 * @author shichen
 * @类名 QueryStandardGoodsTagsRequest
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class QueryStandardGoodsTagsRequest extends QueryPageListRequest {
    /**
     * 名称
     */
    private String name;
}
