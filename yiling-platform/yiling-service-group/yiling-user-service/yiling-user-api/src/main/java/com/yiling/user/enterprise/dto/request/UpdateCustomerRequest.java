package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/9/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerRequest extends BaseRequest {

    /**
     * ID
     */
    private Long   id;
    /**
     * 企业ID
     */
    private Long   eid;

    /**
     * 客户企业ID
     */
    private Long   customerEid;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户ERP编码
     */
    private String customerErpCode;

    /**
     * 客户分组ID
     */
    private Long   customerGroupId;

    /**
     * 最后购买时间
     */
    private Date lastPurchaseTime;

}
