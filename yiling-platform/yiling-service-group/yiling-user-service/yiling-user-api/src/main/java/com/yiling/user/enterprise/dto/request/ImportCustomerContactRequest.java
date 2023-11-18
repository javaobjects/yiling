package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 导入企业客户商务联系人 Request
 *
 * @author: lun.yu
 * @date: 2023-06-05
 */
@Data
public class ImportCustomerContactRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户企业ID
     */
    private Long customerEid;

    /**
     * 商务联系人ID
     */
    private Long contactUserId;

}
