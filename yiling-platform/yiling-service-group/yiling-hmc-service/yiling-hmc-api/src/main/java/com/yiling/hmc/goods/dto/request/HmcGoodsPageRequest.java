package com.yiling.hmc.goods.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 HmcGoodsPageRequest
 * @描述
 * @创建时间 2022/3/30
 * @修改人 shichen
 * @修改时间 2022/3/30
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcGoodsPageRequest extends QueryPageListRequest {
    /**
     * 企业id
     */
    private Long eid;
    /**
     * 药品名称
     */
    private String name;

    /**
     * 商品状态 1上架 2下架
     */
    private Integer goodsStatus;
}
