package com.yiling.mall.banner.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询banner商品明细分页列表 request
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBannerGoodsPageListRequest extends QueryPageListRequest {

    /**
     * bannerId
     */
    private Long bannerId;

}
