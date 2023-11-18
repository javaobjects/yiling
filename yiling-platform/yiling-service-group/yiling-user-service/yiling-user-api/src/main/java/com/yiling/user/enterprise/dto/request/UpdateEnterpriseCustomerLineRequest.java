package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改企业客户产品线 Request
 *
 * @author: lun.yu
 * @date: 2021/11/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseCustomerLineRequest extends BaseRequest {

    /**
     * 企业客户ID（企业客户表id字段）
     */
    private Long customerId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 客户ID
     */
    private Long customerEid;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 使用产品线：1-POP 2-B2B
     */
    private List<Integer> useLineList;
}
