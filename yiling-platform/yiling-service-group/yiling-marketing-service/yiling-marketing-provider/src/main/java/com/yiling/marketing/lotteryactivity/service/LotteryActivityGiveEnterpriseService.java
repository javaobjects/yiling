package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.EnterpriseSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGiveEnterpriseDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.DeleteCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryCustomerPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveEnterpriseDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

/**
 * <p>
 * 抽奖活动-赠送指定客户表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityGiveEnterpriseService extends BaseService<LotteryActivityGiveEnterpriseDO> {

    /**
     * 查询赠送企业分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseSimpleBO> queryHadAddCustomerPage(QueryCustomerPageRequest request);

    /**
     * 根据抽奖活动ID获取赠送企业ID
     *
     * @param lotteryActivityId
     * @return
     */
    List<Long> getGiveEnterpriseByActivityId(Long lotteryActivityId);

    /**
     * 根据抽奖活动ID保存or更新赠送企业
     *
     * @param lotteryActivityId
     * @param eidList
     * @param opUserId
     * @return
     */
    boolean updateGiveEnterpriseByLotteryActivityId(Long lotteryActivityId, List<Long> eidList, Long opUserId);

    /**
     * 添加指定客户
     *
     * @param request
     * @return
     */
    boolean addCustomer(AddCustomerPageRequest request);

    /**
     * 批量删除指定客户
     *
     * @param request
     * @return
     */
    boolean deleteCustomer(DeleteCustomerPageRequest request);

    /**
     * 获取企业数据
     *
     * @param request
     * @param inTypeList
     * @return
     */
    List<EnterpriseDTO> getEnterpriseDTOList(AddCustomerPageRequest request, List<Integer> inTypeList);
}
