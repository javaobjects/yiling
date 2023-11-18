package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业审核分页列表 Form
 *
 * @author: lun.yu
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseAuthRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 认证状态：0-全部 1-未认证 2-认证通过 3-认证不通过
     */
    private Integer authStatus;

    /**
     * 审核驳回原因
     */
    private String authRejectReason;


}
