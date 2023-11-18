package com.yiling.user.usercustomer.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新客户状态 Request
 * 
 * @author lun.yu
 * @date 2022/1/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerStatusRequest extends BaseRequest {

    /**
     * 客户企业ID
     */
    private Long customerEid;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回
     */
    private Integer status;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 驳回原因
     */
    private String rejectReason;
}
