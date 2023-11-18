package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryCrmGoodsInfoPageRequest
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmGoodsInfoPageRequest extends QueryPageListRequest {

    private Long ruleId;
    /**
     * crm商品编码
     */
    private Long goodsCode;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 是否团购 0：否 1：是
     */
    private Integer isGroupPurchase;

    /**
     * 品类id
     */
    private Long categoryId;

    /**
     * 品类名称
     */
    private String category;

    /**
     * 0:有效，1：失效
     */
    private Integer status;

}
