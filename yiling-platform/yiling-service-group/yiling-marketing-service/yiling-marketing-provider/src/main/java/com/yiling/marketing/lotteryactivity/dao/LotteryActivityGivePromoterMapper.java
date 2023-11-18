package com.yiling.marketing.lotteryactivity.dao;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGivePromoterDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 抽奖活动-赠送范围用户类型指定推广方表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Repository
public interface LotteryActivityGivePromoterMapper extends BaseMapper<LotteryActivityGivePromoterDO> {

    /**
     * 查询赠送推广方分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseSimpleBO> queryHadAddPromoterPage(Page<EnterpriseSimpleBO> page, @Param("request") QueryCustomerPageRequest request);

    /**
     * 查询赠送推广方列表
     *
     * @param request
     * @return
     */
    List<EnterpriseSimpleBO> queryHadAddPromoterPage(@Param("request") QueryCustomerPageRequest request);
}
