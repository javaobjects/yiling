package com.yiling.marketing.lotteryactivity.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.marketing.lotteryactivity.bo.MemberSimpleBO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveMemberDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 抽奖活动-赠送范围用户类型指定会员表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Repository
public interface LotteryActivityGiveMemberMapper extends BaseMapper<LotteryActivityGiveMemberDO> {

}
