package com.yiling.goods.restriction.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 DeleteRestrictionCustomerRequest
 * @描述
 * @创建时间 2022/12/7
 * @修改人 shichen
 * @修改时间 2022/12/7
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteRestrictionCustomerRequest extends BaseRequest {
    private Long goodsId;

    /**
     * 客户eid
     */
    private Long customerEid;

    /**
     * 客户id列表
     */
    private List<Long> customerEidList;
}
