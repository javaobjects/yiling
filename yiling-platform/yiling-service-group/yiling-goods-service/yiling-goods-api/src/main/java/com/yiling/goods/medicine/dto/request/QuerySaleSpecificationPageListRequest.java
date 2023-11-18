package com.yiling.goods.medicine.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QuerySaleSpecificationPageListRequest
 * @描述
 * @创建时间 2023/5/8
 * @修改人 shichen
 * @修改时间 2023/5/8
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySaleSpecificationPageListRequest extends QueryPageListRequest {

    /**
     * 是否以岭品，0：不是，1：是
     */
    private Integer ylFlag;

    /**
     * 建采商家eid
     */
    private List<Long> purchaseEids;

    /**
     * 售卖eid
     */
    private List<Long> sellerEids;

    /**
     * 售卖时间开始
     */
    private Date saleTimStart;

    /**
     * 售卖时间结束
     */
    private Date saleTimEnd;
}
