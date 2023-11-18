package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;
import com.yiling.marketing.presale.entity.MarketingPresalePromoterMemberLimitDO;
import com.yiling.marketing.strategy.dto.request.AddPresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresalePromoterMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPromoterMemberLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyPromoterMemberLimitDO;

/**
 * <p>
 * 策略满赠推广方会员表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-29
 */
public interface StrategyPromoterMemberLimitService extends BaseService<StrategyPromoterMemberLimitDO> {

    /**
     * 添加推广方会员商家
     *
     * @param request 添加推广方会员商家内容
     * @return 成功/失败
     */
    boolean add(AddStrategyPromoterMemberLimitRequest request);

    /**
     * 添加推广方会员商家
     *
     * @param request 添加推广方会员商家内容
     * @return 成功/失败
     */
    boolean addForPayPromotion(AddStrategyPromoterMemberLimitRequest request);

    /**
     * 添加推广方会员商家
     *
     * @param request 添加推广方会员商家内容
     * @return 成功/失败
     */
    boolean addForPresale(AddPresalePromoterMemberLimitRequest request);

    /**
     * 复制推广方会员商家
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 删除推广方会员商家
     *
     * @param request 删除推广方会员商家内容
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyPromoterMemberLimitRequest request);

    /**
     * 删除推广方会员商家
     *
     * @param request 删除推广方会员商家内容
     * @return 成功/失败
     */
    boolean deleteForPayPromotion(DeleteStrategyPromoterMemberLimitRequest request);

    /**
     * 删除推广方会员商家
     *
     * @param request 删除推广方会员商家内容
     * @return 成功/失败
     */
    boolean deleteForPresale(DeletePresalePromoterMemberLimitRequest request);

    /**
     * 策略满赠推广方会员商家-已添加推广方会员商家列表查询
     *
     * @param request 查询条件
     * @return 已添加商家列表
     */
    Page<StrategyPromoterMemberLimitDO> pageList(QueryStrategyPromoterMemberLimitPageRequest request);

    /**
     * 策略满赠推广方会员商家-已添加推广方会员商家列表查询
     *
     * @param request 查询条件
     * @return 已添加商家列表
     */
    Page<MarketingPresalePromoterMemberLimitDO> pageListForPresale(QueryPresalePromoterMemberLimitPageRequest request);

    /**
     * 根据活动id查询推广方会员商家范围数量
     *
     * @param strategyActivityId 活动id
     * @return 商家范围数量
     */
    Integer countPromoterMemberByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和企业id查询策略满赠推广方会员商家
     *
     * @param strategyActivityId 活动id
     * @param eidList 企业id
     * @return 推广方会员商家信息
     */
    List<StrategyPromoterMemberLimitDO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList);

    /**
     * 支付促销推广方会员商家-已添加推广方会员商家列表查询
     *
     * @param request 查询条件
     * @return 已添加商家列表
     */
    Page<MarketingPayPromoterMemberLimitDO> pageListForPayPromotion(QueryStrategyPromoterMemberLimitPageRequest request);
}
