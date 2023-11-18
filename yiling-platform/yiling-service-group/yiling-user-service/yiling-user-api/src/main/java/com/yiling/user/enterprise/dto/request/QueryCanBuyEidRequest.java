package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询当前登录企业可采企业ID Request
 *
 * @author: lun.yu
 * @date: 2022-11-03
 */
@Data
@Accessors(chain = true)
public class QueryCanBuyEidRequest extends BaseRequest {

    /**
     * 当前登录企业ID
     */
    @NotNull
    private Long customerEid;

    /**
     * 产品线类型：1-POP 2-B2B
     */
    private Integer line;

    /**
     * 查询结果限制数量（0或空表示不限制）
     */
    private Integer limit;

}
