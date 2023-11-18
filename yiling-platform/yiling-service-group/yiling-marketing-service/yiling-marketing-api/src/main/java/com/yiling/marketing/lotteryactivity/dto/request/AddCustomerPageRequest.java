package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加客户分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCustomerPageRequest extends QueryPageListRequest {

    /**
     * 抽奖活动ID
     */
    @NotNull
    private Long lotteryActivityId;

    /**
     * 1-单个添加 2-添加当前页 3-添加搜索结果
     */
    @NotNull
    private Integer type;

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业ID集合
     */
    @NotEmpty
    private List<Long> idList;


}
