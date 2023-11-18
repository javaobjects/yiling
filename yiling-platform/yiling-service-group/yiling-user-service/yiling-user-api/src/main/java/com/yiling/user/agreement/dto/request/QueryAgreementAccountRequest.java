package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementAccountRequest extends BaseRequest {

    /**
     * 协议Id
     */
    private Long agreementId;

    /**
     * eas账号
     */
    private String easAccount;
}
