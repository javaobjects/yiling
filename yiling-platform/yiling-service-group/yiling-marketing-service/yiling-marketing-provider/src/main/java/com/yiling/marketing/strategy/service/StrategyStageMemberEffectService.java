package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.request.AddStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyStageMemberEffectPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyStageMemberEffectDO;

/**
 * <p>
 * 策略满赠购买会员方案表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-05
 */
public interface StrategyStageMemberEffectService extends BaseService<StrategyStageMemberEffectDO> {

    /**
     * 添加购买会员方案
     *
     * @param request 添加购买会员方案
     * @return 成功/失败
     */
    boolean add(AddStrategyStageMemberEffectRequest request);

    /**
     * 复制购买会员方案数据
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 删除购买会员方案
     *
     * @param request 删除购买会员方案
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyStageMemberEffectRequest request);

    /**
     * 策略满赠客户-购买会员方案列表查询
     *
     * @param request 查询条件
     * @return 购买会员方案
     */
    Page<StrategyStageMemberEffectDO> pageList(QueryStrategyStageMemberEffectPageRequest request);

    /**
     * 根据活动id查询购买会员方案数量
     *
     * @param strategyActivityId 活动id
     * @return 购买会员方案数量
     */
    Integer countStageMemberEffectByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询支持条件的购买会员方案
     *
     * @param strategyActivityId 活动id
     * @return 支持条件的购买会员方案
     */
    List<StrategyStageMemberEffectDO> listByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和会员id(会员购买条件id)查询支持条件的购买会员方案
     *
     * @param strategyActivityId 活动id
     * @param memberIdList 会员购买条件id
     * @return 支持条件的购买会员方案
     */
    List<StrategyStageMemberEffectDO> listByActivityIdAndMemberIdList(Long strategyActivityId, List<Long> memberIdList);
}
