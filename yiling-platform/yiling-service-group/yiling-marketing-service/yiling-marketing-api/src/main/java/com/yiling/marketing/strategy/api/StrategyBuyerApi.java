package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.paypromotion.dto.PayPromotionBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.PresaleBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleBuyerLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyBuyerLimitPageRequest;

/**
 * 策略满赠-客户Api
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
public interface StrategyBuyerApi {

    /**
     * 添加客户
     *
     * @param request 添加客户内容
     * @return 成功/失败
     */
    boolean add(AddStrategyBuyerLimitRequest request);

    /**
     * 添加客户
     *
     * @param request 添加客户内容
     * @return 成功/失败
     */
    boolean addForPayPromotion(AddStrategyBuyerLimitRequest request);

    /**
     * 添加客户
     *
     * @param request 添加客户内容
     * @return 成功/失败
     */
    boolean addForPresale(AddPresaleBuyerLimitRequest request);

    /**
     * 删除客户
     *
     * @param request 删除客户内容
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyBuyerLimitRequest request);
    /**
     * 删除客户
     *
     * @param request 删除客户内容
     * @return 成功/失败
     */
    boolean deleteForPayPromotion(DeleteStrategyBuyerLimitRequest request);
    /**
     * 策略满赠客户-已添加客户列表查询
     *
     * @param request 查询条件
     * @return 已添加客户列表
     */
    Page<StrategyBuyerLimitDTO> pageList(QueryStrategyBuyerLimitPageRequest request);

    /**
     * 策略满赠客户-已添加客户列表查询
     *
     * @param request 查询条件
     * @return 已添加客户列表
     */
    Page<PayPromotionBuyerLimitDTO> pageListForPromotion(QueryStrategyBuyerLimitPageRequest request);

    /**
     * 策略满赠客户-已添加客户列表查询
     *
     * @param request 查询条件
     * @return 已添加客户列表
     */
    Page<PresaleBuyerLimitDTO> pageListForPresale(QueryPresaleBuyerLimitPageRequest request);

    /**
     * 根据活动id查询客户数量
     *
     * @param strategyActivityId 活动id
     * @return 客户数量
     */
    Integer countBuyerByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询客户
     *
     * @param strategyActivityId 活动id
     * @return 客户
     */
    List<StrategyBuyerLimitDTO> listBuyerByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和企业id集合查询指定商户信息
     *
     * @param strategyActivityId 活动id
     * @param eidList 企业id集合
     * @return 指定客户
     */
    List<StrategyBuyerLimitDTO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList);


    /**
     * 根据活动id集合和企业id查询指定商户信息
     *
     * @param marketingStrategyIdList 活动id集合
     * @param buyerEid 企业id
     * @return 指定客户
     */
    List<StrategyBuyerLimitDTO> listByActivityIdListAndEid(List<Long> marketingStrategyIdList, Long buyerEid);

    /**
     * 删除客户
     *
     * @param request 删除客户内容
     * @return 成功/失败
     */
    boolean deleteForPresale(DeletePresaleBuyerLimitRequest request);

    /**
     * 根据活动id查询客户数量
     *
     * @param id 活动id
     * @return 客户数量
     */
    Integer countBuyerByActivityIdForPayPromotion(Long id);

    /**
     * 根据活动id查询客户数量
     *
     * @param id 活动id
     * @return 客户数量
     */
    Integer countMemberByActivityId(Long id);

    /**
     * 根据活动id查询客户数量
     *
     * @param id 活动id
     * @return 客户数量
     */
    Integer countPromoterMemberByActivityId(Long id);
}
