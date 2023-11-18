package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询企业客户产品线 Form
 *
 * @author: lun.yu
 * @date: 2021/11/29
 */
@Data
@Accessors(chain = true)
public class QueryCustomerLineListRequest extends BaseRequest {

    /**
     * 企业客户ID（企业客户表id字段）
     */
    private Long customerId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户ID
     */
    private Long customerEid;

}
