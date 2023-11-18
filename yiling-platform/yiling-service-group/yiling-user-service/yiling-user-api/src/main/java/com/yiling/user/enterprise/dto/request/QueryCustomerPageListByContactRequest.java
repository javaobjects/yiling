package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据商务联系人查询客户列表  Request
 *
 * @author: lun.yu
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerPageListByContactRequest extends QueryPageListRequest {

    /**
     * 商务联系人ID
     */
    private Long contactUserId;

    /**
     * 企业名称/联系人（全模糊匹配）
     */
    private String customerName;
}
