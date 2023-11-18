package com.yiling.goods.restriction.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionCustomerDTO
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPurchaseRestrictionCustomerDTO extends BaseDTO {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 客户id
     */
    private Long customerEid;
}
