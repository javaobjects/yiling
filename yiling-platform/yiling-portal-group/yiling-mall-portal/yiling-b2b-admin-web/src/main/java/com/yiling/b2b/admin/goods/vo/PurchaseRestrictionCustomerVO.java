package com.yiling.b2b.admin.goods.vo;

import com.yiling.b2b.admin.enterprisecustomer.vo.EnterpriseCustomerListItemVO;

import lombok.Data;

/**
 * @author shichen
 * @类名 PurchaseRestrictionCustomerVO
 * @描述
 * @创建时间 2022/12/8
 * @修改人 shichen
 * @修改时间 2022/12/8
 **/
@Data
public class PurchaseRestrictionCustomerVO extends EnterpriseCustomerListItemVO {
    /**
     * 是否限购客户
     */
    private Boolean isPurchaseRestriction;
}
