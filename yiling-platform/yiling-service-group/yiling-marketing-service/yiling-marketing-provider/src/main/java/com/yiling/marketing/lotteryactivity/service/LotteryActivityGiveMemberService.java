package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.MemberSimpleBO;
import com.yiling.marketing.lotteryactivity.dto.request.AddOrDeleteMemberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryMemberPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveMemberDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动-赠送范围用户类型指定会员表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityGiveMemberService extends BaseService<LotteryActivityGiveMemberDO> {

    /**
     * 查询赠送会员分页列表
     *
     * @param request
     * @return
     */
    Page<MemberSimpleBO> queryHadAddMemberPage(QueryMemberPageRequest request);

    /**
     * 根据抽奖活动ID获取赠送方案会员ID
     *
     * @param lotteryActivityId
     * @return
     */
    List<Long> getGiveMemberByActivityId(Long lotteryActivityId);

    /**
     * 根据抽奖活动ID保存or更新赠送指定会员方案
     *
     * @param lotteryActivityId
     * @param memberIdList
     * @param opUserId
     * @return
     */
    boolean updateGiveMemberByLotteryActivityId(Long lotteryActivityId, List<Long> memberIdList, Long opUserId);

    /**
     * 添加会员
     *
     * @param request
     * @return
     */
    boolean addMember(AddOrDeleteMemberRequest request);

    /**
     * 删除会员
     *
     * @param request
     * @return
     */
    boolean deleteMember(AddOrDeleteMemberRequest request);
}
