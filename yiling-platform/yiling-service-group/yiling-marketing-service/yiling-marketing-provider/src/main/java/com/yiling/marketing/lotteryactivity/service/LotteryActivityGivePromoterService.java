package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.DeleteCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGivePromoterDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动-赠送范围用户类型指定推广方表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityGivePromoterService extends BaseService<LotteryActivityGivePromoterDO> {


    /**
     * 查询赠送推广方分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseSimpleBO> queryHadAddPromoterPage(QueryCustomerPageRequest request);

    /**
     * 根据抽奖活动ID获取指定推广方ID
     *
     * @param lotteryActivityId
     * @return
     */
    List<Long> getGivePromoterByActivityId(Long lotteryActivityId);

    /**
     * 根据抽奖活动ID保存or更新赠送指定推广方会员方案
     *
     * @param lotteryActivityId
     * @param promoterIdList
     * @param opUserId
     * @return
     */
    boolean updateGivePromoterByLotteryActivityId(Long lotteryActivityId, List<Long> promoterIdList, Long opUserId);

    /**
     * 添加推广方
     *
     * @param request
     * @return
     */
    boolean addPromoter(AddCustomerPageRequest request);

    /**
     * 删除推广方
     *
     * @param request
     * @return
     */
    boolean deletePromoter(DeleteCustomerPageRequest request);
}
