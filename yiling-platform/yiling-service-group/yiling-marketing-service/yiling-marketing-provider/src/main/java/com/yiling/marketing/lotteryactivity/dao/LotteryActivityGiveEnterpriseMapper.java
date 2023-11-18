package com.yiling.marketing.lotteryactivity.dao;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveEnterpriseDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 抽奖活动-赠送指定客户表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Repository
public interface LotteryActivityGiveEnterpriseMapper extends BaseMapper<LotteryActivityGiveEnterpriseDO> {

    /**
     * 查询赠送企业分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseSimpleBO> queryHadAddCustomerPage(Page<EnterpriseSimpleBO> page, @Param("request") QueryCustomerPageRequest request);

    /**
     * 查询赠送企业列表
     *
     * @param request
     * @return
     */
    List<EnterpriseSimpleBO> queryHadAddCustomerPage(@Param("request") QueryCustomerPageRequest request);
}
