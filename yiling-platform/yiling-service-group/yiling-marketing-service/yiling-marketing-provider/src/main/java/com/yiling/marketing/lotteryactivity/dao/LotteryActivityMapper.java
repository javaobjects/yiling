package com.yiling.marketing.lotteryactivity.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 抽奖活动表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Repository
public interface LotteryActivityMapper extends BaseMapper<LotteryActivityDO> {

    /**
     * 查询抽奖活动分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityItemBO> queryListPage(Page page, @Param("request") QueryLotteryActivityPageRequest request);
}
