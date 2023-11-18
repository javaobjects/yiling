package com.yiling.mall.agreement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CashMallAgreementRequest extends BaseRequest {

    /**
     * 协议Id
     */
    private Long agreementId;

    /**
     * 账号信息
     */
    private String easAccount;

    /**
     * 时间
     */
    private List<String> timeRange;
}
