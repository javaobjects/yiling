package com.yiling.user.agreement.dto.request;

import java.util.List;

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
public class QueryAgreementRebateRequest extends BaseRequest {
    /**
     * 协议ID集合
     */
    private List<Long> agreementIds;
    /**
     * 时间范围
     */
    private List<String> comparisonTimes;
    /**
     * 兑付状态
     */
    private Integer cashStatus;
    /**
     * 是否已经满足条件状态1未满足2已经满足
     */
    private Integer conditionStatus;
    /**
     * 兑付账号
     */
    private String easAccount;
}
