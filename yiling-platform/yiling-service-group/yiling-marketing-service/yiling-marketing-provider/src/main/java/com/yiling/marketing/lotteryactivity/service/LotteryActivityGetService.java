package com.yiling.marketing.lotteryactivity.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGetDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGetDO;

/**
 * <p>
 * 获取抽奖机会明细表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
public interface LotteryActivityGetService extends BaseService<LotteryActivityGetDO> {

    /**
     * 查询抽奖机会明细分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityGetDTO> queryPageList(QueryLotteryActivityGetPageRequest request);

    /**
     * 批量获取抽奖活动机会数量
     *
     * @param lotteryActivityIdList
     * @return
     */
    Map<Long, Integer> getNumberByLotteryActivityId(List<Long> lotteryActivityIdList);

    /**
     * 获取用户某活动获得的次数
     *
     * @param request 查询条件
     * @return 数量
     */
    Integer countByUidAndGetType(QueryLotteryActivityGetCountRequest request);
}
