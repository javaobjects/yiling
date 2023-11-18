package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询客户分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerPageRequest extends QueryPageListRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

}
