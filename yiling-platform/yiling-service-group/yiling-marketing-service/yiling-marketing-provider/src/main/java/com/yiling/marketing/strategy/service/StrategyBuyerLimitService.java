package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayBuyerLimitDO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyerLimitDO;
import com.yiling.marketing.strategy.dto.request.AddPresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleBuyerLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyBuyerLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyBuyerLimitDO;

/**
 * <p>
 * 策略满赠客户 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyBuyerLimitService extends BaseService<StrategyBuyerLimitDO> {

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
     * 复制客户
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

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
    Page<StrategyBuyerLimitDO> pageList(QueryStrategyBuyerLimitPageRequest request);

    /**
     * 策略满赠客户-已添加客户列表查询
     *
     * @param request 查询条件
     * @return 已添加客户列表
     */
    Page<MarketingPayBuyerLimitDO> pageListForPromotion(QueryStrategyBuyerLimitPageRequest request);

    /**
     * 策略满赠客户-已添加客户列表查询
     *
     * @param request 查询条件
     * @return 已添加客户列表
     */
    Page<MarketingPresaleBuyerLimitDO> pageListForPresale(QueryPresaleBuyerLimitPageRequest request);

    /**
     * 根据活动id查询客户范围数量
     *
     * @param strategyActivityId 活动id
     * @return 客户范围数量
     */
    Integer countBuyerByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询客户范围
     *
     * @param strategyActivityId 活动id
     * @return 客户范围
     */
    List<StrategyBuyerLimitDO> listBuyerByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和企业id查询指定商户信息
     *
     * @param strategyActivityId 活动id
     * @param eid 企业id
     * @return 指定客户
     */
    StrategyBuyerLimitDO queryByActivityIdAndEid(Long strategyActivityId, Long eid);

    /**
     * 根据活动id和企业id集合查询指定商户信息
     *
     * @param strategyActivityId 活动id
     * @param eidList 企业id集合
     * @return 指定客户
     */
    List<StrategyBuyerLimitDO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList);

    /**
     * 根据活动id和企业id集合查询指定商户信息
     *
     * @param strategyActivityId 活动id
     * @param eidList 企业id集合
     * @return 指定客户
     */
    List<MarketingPresaleBuyerLimitDO> listByActivityIdAndEidListForPresale(Long strategyActivityId, List<Long> eidList);
    /**
     * 根据活动id集合和企业id查询指定商户信息
     *
     * @param marketingStrategyIdList 活动id集合
     * @param buyerEid 企业id
     * @return 指定客户
     */
    List<StrategyBuyerLimitDO> listByActivityIdListAndEid(List<Long> marketingStrategyIdList, Long buyerEid);
    /**
     * 删除客户
     *
     * @param request 删除客户内容
     * @return 成功/失败
     */
    boolean deleteForPresale(DeletePresaleBuyerLimitRequest request);


    /**
     * 根据活动id查询客户范围数量
     *
     * @param strategyActivityId 活动id
     * @return 客户范围数量
     */
    Integer countBuyerByActivityIdForPayPromotion(Long strategyActivityId);


    /**
     * 根据活动id查询客户范围数量
     *
     * @param strategyActivityId 活动id
     * @return 客户范围数量
     */
    Integer countMemberByActivityId(Long strategyActivityId);


    /**
     * 根据活动id查询客户范围数量
     *
     * @param strategyActivityId 活动id
     * @return 客户范围数量
     */
    Integer countPromoterMemberByActivityId(Long strategyActivityId);
}
