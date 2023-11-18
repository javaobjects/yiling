package com.yiling.goods.restriction.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author shichen
 * @类名 SaveRestrictionCustomerRequest
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Data
public class SaveRestrictionCustomerRequest extends BaseRequest {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 客户eid
     */
    private Long customerEid;

    /**
     * 批量添加的客户eid列表
     */
    private List<Long> customerEidList;
}
