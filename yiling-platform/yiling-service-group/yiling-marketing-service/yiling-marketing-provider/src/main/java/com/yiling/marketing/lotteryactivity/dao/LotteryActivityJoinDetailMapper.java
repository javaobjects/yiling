package com.yiling.marketing.lotteryactivity.dao;

import java.util.List;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinDetailDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 抽奖活动参与明细表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Repository
public interface LotteryActivityJoinDetailMapper extends BaseMapper<LotteryActivityJoinDetailDO> {

    /**
     * 获取中奖名单
     *
     * @param lotteryActivityId
     * @param limit
     * @return
     */
    List<LotteryActivityJoinDetailDTO> queryHitList(@Param("lotteryActivityId") Long lotteryActivityId, @Param("limit") Integer limit);
}
