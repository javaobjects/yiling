package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.dto.request.SavePresaleActivityRequest;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityEidOrGoodsIdDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryGoodsStrategyInfoRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;

/**
 * <p>
 * 营销活动主表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyActivityService extends BaseService<StrategyActivityDO> {

    /**
     * 新增满赠促销活动
     *
     * @param request 满赠促销活动主信息
     * @return 满赠促销活动主信息
     */
    StrategyActivityDO save(AddStrategyActivityRequest request);

    /**
     * 新增满赠促销活动-具体内容
     *
     * @param request 满赠促销活动具体内容
     * @return 成功/失败
     */
    boolean saveAll(SaveStrategyActivityRequest request);

    /**
     * 策略满赠的复制
     *
     * @param request 活动id
     * @return 营销活动主表信息
     */
    StrategyActivityDO copy(CopyStrategyRequest request);

    /**
     * 策略满赠的停用
     *
     * @param request 活动id
     * @return 成功/失败
     */
    boolean stop(StopStrategyRequest request);

    /**
     * 营销活动分页查询
     *
     * @param request 查询条件
     * @return 营销活动主表信息
     */
    Page<StrategyActivityDO> pageList(QueryStrategyActivityPageRequest request);

    /**
     * 营销活动分页查询
     *
     * @param request 查询条件
     * @return 营销活动主表信息
     */
    Page<MarketingPresaleActivityDO> pageListForPresale(QueryStrategyActivityPageRequest request);

    List<StrategyActivityDO> listEffectiveStrategy(Integer strategyType, String platformSelected, Integer orderAmountLadderType, Integer orderAmountStatusType);

    List<StrategyActivityDO> listEffectiveStrategyByTime(Integer strategyType, String platformSelected, List<Integer> orderAmountLadderTypeList, Integer orderAmountStatusType, Date time);

    /**
     * 每日定时任务赠送赠品--时间周期(周，月)
     */
    void strategyActivityAutoJobHandler();

    /**
     * 续费会员赠送活动赠品
     */
    void strategyMemberAutoJobHandler();

    List<StrategyActivityDO> listStopAmountStrategyActivity(Date startTime, Date stopTime);

    /**
     * 查询商品是否有参与策略满赠活动
     *
     * @param request 查询条件
     * @return 满足满赠活动商品id集合
     */
    List<Long> queryGoodsStrategyInfo(QueryGoodsStrategyInfoRequest request);

    /**
     * 查询商品是否有参与策略满赠活动
     *
     * @param request 查询条件
     * @return 赠品活动
     */
    List<StrategyActivityDTO> queryGoodsStrategyGift(QueryGoodsStrategyInfoRequest request);

    /**
     * 购买会员赠送赠品api
     *
     * @param orderNo 会员购买生成订单号
     * @return 成功/失败
     */
    Boolean sendGiftAfterBuyMember(String orderNo);

    /**
     * 根据策略满赠活动id查询可用商品列表分页
     *
     * @param strategyActivityId 策略满赠活动id
     * @param buyerEid 商家企业id
     * @return 可用商品列表分页
     */
    StrategyActivityEidOrGoodsIdDTO getGoodsListPageByActivityId(Long strategyActivityId, Long buyerEid);

    /**
     * 策略满赠赠送赠品
     *
     * @param activityDO 活动信息
     * @param eid 赠送用户id
     * @param strategyGiftDOList 赠送的赠品信息
     * @param ladderId 阶梯id(赠送的赠品是哪一个阶梯的，主要是为了存储赠送记录)
     * @param orderId 订单id(主要是为了存储赠送记录)
     * @param memberId 会员id(主要是为了赠送记录)
     */
    void sendGift(StrategyActivityDO activityDO, Long eid, List<StrategyGiftDO> strategyGiftDOList, Long ladderId, Long orderId, Long memberId);

    /**
     * 根据赠品分页查询此赠品的活动信息
     *
     * @param request 查询条件
     * @return 活动信息
     */
    List<StrategyActivityDO> pageStrategyByGiftId(QueryLotteryStrategyRequest request);

    /**
     * 根据赠品分页查询此赠品的活动信息
     *
     * @param request 查询条件
     * @return 活动信息
     */
    Page<StrategyActivityDO> pageLotteryStrategy(QueryLotteryStrategyPageRequest request);

    /**
     * 根据赠品查询此赠品的活动信息数量
     *
     * @param lotteryActivityIdList 抽奖活动id集合
     * @return 活动信息数量
     */
    Map<Long, Integer> countStrategyByGiftId(List<Long> lotteryActivityIdList);

    /**
     * 新增预售活动-具体内容
     *
     * @param request 满赠促销活动具体内容
     * @return 成功/失败
     */
    boolean saveAllForPresale(SavePresaleActivityRequest request);

    /**
     * 查询预售活动-具体内容
     *
     * @param id 满赠促销活动具体内容
     * @return 成功/失败
     */
    PresaleActivityDTO queryPresaleActivityById(Long id);
}
