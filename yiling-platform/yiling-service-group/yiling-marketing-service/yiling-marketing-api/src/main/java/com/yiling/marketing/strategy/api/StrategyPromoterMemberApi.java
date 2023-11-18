package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.paypromotion.dto.PayPromotionPromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.PresalePromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresalePromoterMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPromoterMemberLimitPageRequest;

/**
 * 策略满赠推广方会员Api
 *
 * @author: yong.zhang
 * @date: 2022/8/29
 */
public interface StrategyPromoterMemberApi {

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
    Page<StrategyPromoterMemberLimitDTO> pageList(QueryStrategyPromoterMemberLimitPageRequest request);

    /**
     * 策略满赠推广方会员商家-已添加推广方会员商家列表查询
     *
     * @param request 查询条件
     * @return 已添加商家列表
     */
    Page<PresalePromoterMemberLimitDTO> pageListForPresale(QueryPresalePromoterMemberLimitPageRequest request);

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
    List<StrategyPromoterMemberLimitDTO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList);

    /**
     * 支付促销推广方会员商家-已添加推广方会员商家列表查询
     *
     * @param request 查询条件
     * @return 已添加商家列表
     */
    Page<PayPromotionPromoterMemberLimitDTO> pageListForPayPromotion(QueryStrategyPromoterMemberLimitPageRequest request);
}
