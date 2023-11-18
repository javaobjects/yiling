package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存企业渠道商信息 Request
 *
 * @author: yuecheng.chen
 * @date: 2021/6/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerInfoRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户企业ID
     */
    private Long customerEid;

    /**
     * 客户联系人ID列表
     */
    private List<Long> contactUserIds;

    /**
     * 支付方式ID列表
     */
    private List<Long> paymentMethodIds;

    /**
     * 客户分组ID
     */
    private Long customerGroupId;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;
}
