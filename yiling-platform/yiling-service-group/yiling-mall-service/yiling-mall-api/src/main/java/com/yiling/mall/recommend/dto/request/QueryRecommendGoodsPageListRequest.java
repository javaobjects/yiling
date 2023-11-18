package com.yiling.mall.recommend.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询recommend商品明细分页列表 request
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRecommendGoodsPageListRequest extends QueryPageListRequest {

    /**
     * recommendId
     */
    private Long recommendId;

}
