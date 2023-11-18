package com.yiling.marketing.lotteryactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖活动参与/中奖次数 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryJoinDetailNumberRequest extends BaseRequest {

    /**
     * 抽奖活动ID集合
     */
    @NotEmpty
    private List<Long> lotteryActivityIdList;

    /**
     * 不包含的奖品类型
     */
    private List<Integer> notInRewardTypeList;

}
