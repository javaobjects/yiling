package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.paypromotion.dto.PaypromotionMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyMemberLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyMemberLimitPageRequest;

/**
 * 策略满赠-指定方案会员Api
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
public interface StrategyMemberApi {

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
     * @param request 添加会员方案
     * @return 成功/失败
     */
    boolean add(AddStrategyMemberLimitRequest request);

    /**
     * 添加会员方案
     *
     * @param request 添加会员方案
     * @return 成功/失败
     */
    boolean addForPromotion(AddStrategyMemberLimitRequest request);

    /**
     * 添加会员方案
     *
     * @param request 添加会员方案
     * @return 成功/失败
     */
    boolean addForPresale(AddPresaleMemberLimitRequest request);

    /**
     * 删除会员方案
     *
     * @param request 删除会员方案
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyMemberLimitRequest request);

    /**
     * 删除会员方案
     *
     * @param request 删除会员方案
     * @return 成功/失败
     */
    boolean deleteForPromotion(DeleteStrategyMemberLimitRequest request);

    /**
     * 策略满赠会员方案-已添加会员方案列表查询
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<StrategyMemberLimitDTO> pageList(QueryStrategyMemberLimitPageRequest request);

    /**
     * 策略满赠会员方案-已添加会员方案列表查询
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<PaypromotionMemberLimitDTO> pageListForPayPromotion(QueryStrategyMemberLimitPageRequest request);

    /**
     * 策略满赠会员方案-已添加会员方案列表查询
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<StrategyMemberLimitDTO> pageListForPresale(QueryPresaleMemberLimitPageRequest request);

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
    List<StrategyMemberLimitDTO> listMemberByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和会员id查询指定会员方案
     *
     * @param strategyActivityId 活动id
     * @param memberId 会员id
     * @return 指定会员方案-会员维度
     */
    StrategyMemberLimitDTO queryByActivityIdAndMemberId(Long strategyActivityId, Long memberId);

    /**
     * 删除会员方案
     *
     * @param request 删除会员方案
     * @return 成功/失败
     */
    boolean deleteForPresale(DeletePresaleMemberLimitRequest request);

    /**
     * 删除预售商品限制
     *
     * @param request 删除预售商品限制
     * @return 成功/失败
     */
    boolean deleteForPresaleGoodsLimit(DeletePresaleMemberLimitRequest request);

    /**
     * 停用预售活动
     *
     * @param currentUserId 删除预售商品限制
     * @return 成功/失败
     */
    boolean updatePresaleStatus(Long currentUserId,Long id);

    /**
     * 根据活动id和会员id集合查询指定会员方案
     *
     * @param strategyActivityId 活动id
     * @param memberIdList 会员id集合
     * @return 指定会员方案-会员维度
     */
    List<StrategyMemberLimitDTO> listByActivityIdAndMemberIdList(Long strategyActivityId, List<Long> memberIdList);
}
