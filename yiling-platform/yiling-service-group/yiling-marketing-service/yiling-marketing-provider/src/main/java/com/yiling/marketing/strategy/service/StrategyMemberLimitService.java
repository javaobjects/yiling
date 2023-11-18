package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;
import com.yiling.marketing.presale.entity.MarketingPresaleMemberLimitDO;
import com.yiling.marketing.strategy.dto.request.AddPresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyMemberLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyMemberLimitDO;

/**
 * <p>
 * 策略满赠会员方案 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyMemberLimitService extends BaseService<StrategyMemberLimitDO> {

    /**
     * 根据活动id查询这个活动绑定的指定方案会员
     *
     * @param strategyActivityId 活动id
     * @return 会员id集合
     */
    List<Long> listMemberIdByStrategyId(Long strategyActivityId);

    /**
     * 添加会员方案
     *
     * @param request 添加会员方案内容
     * @return 成功/失败
     */
    boolean add(AddStrategyMemberLimitRequest request);

    /**
     * 添加会员方案
     *
     * @param request 添加会员方案内容
     * @return 成功/失败
     */
    boolean addForPromotion(AddStrategyMemberLimitRequest request);

    /**
     * 添加会员方案
     *
     * @param request 添加会员方案内容
     * @return 成功/失败
     */
    boolean addForPresale(AddPresaleMemberLimitRequest request);

    /**
     * 复制会员方案
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 删除会员方案
     *
     * @param request 删除会员方案内容
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyMemberLimitRequest request);

    /**
     * 删除会员方案
     *
     * @param request 删除会员方案内容
     * @return 成功/失败
     */
    boolean deleteForPromotion(DeleteStrategyMemberLimitRequest request);

    /**
     * 策略满赠会员方案-已添加会员方案列表查询
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<StrategyMemberLimitDO> pageList(QueryStrategyMemberLimitPageRequest request);


    /**
     * 策略满赠会员方案-已添加会员方案列表查询
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<MarketingPayMemberLimitDO> pageListForPayPromotion(QueryStrategyMemberLimitPageRequest request);

    /**
     * 策略满赠会员方案-已添加会员方案列表查询
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<MarketingPresaleMemberLimitDO> pageListForPresale(QueryPresaleMemberLimitPageRequest request);

    /**
     * 根据活动id查询指定方案数量
     *
     * @param strategyActivityId 活动id
     * @return 指定方案数量
     */
    Integer countMemberByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询指定方案
     *
     * @param strategyActivityId 活动id
     * @return 指定方案
     */
    List<StrategyMemberLimitDO> listMemberByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和会员id查询指定会员方案
     *
     * @param strategyActivityId 活动id
     * @param memberId 会员id
     * @return 指定会员方案-会员维度
     */
    StrategyMemberLimitDO queryByActivityIdAndMemberId(Long strategyActivityId, Long memberId);

    /**
     * 删除会员方案
     *
     * @param request 删除会员方案内容
     * @return 成功/失败
     */
    boolean deleteForPresale(DeletePresaleMemberLimitRequest request);

    /**
     * 删除会员方案
     *
     * @param request 删除会员方案内容
     * @return 成功/失败
     */
    boolean deleteForPresaleGoodsLimit(DeletePresaleMemberLimitRequest request);

    /**
     * 根据活动id和会员id集合查询指定会员方案
     *
     * @param strategyActivityId 活动id
     * @param memberIdList 会员id集合
     * @return 指定会员方案-会员维度
     */
    List<StrategyMemberLimitDO> listByActivityIdAndMemberIdList(Long strategyActivityId, List<Long> memberIdList);
}
