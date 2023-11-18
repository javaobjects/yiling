package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryRecommendGoodsGroupPageRequest
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRecommendGoodsGroupPageRequest extends QueryPageListRequest {

    /**
     * 组名称
     */
    private String name;

    /**
     * 所属企业
     */
    private Long eid;

    /**
     * 是否启用快速采购推荐位 0 开启，1关闭
     */
    private Integer quickPurchaseFlag;

    /**
     * 查询条数限制
     */
    private Integer queryLimit;
}
