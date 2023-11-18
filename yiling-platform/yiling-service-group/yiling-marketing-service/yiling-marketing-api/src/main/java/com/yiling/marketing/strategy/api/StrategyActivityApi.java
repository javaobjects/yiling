package com.yiling.marketing.strategy.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityEidOrGoodsIdDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyCycleLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryGoodsStrategyInfoRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
public interface StrategyActivityApi {

    /**
     * 新增满赠促销活动
     *
     * @param request 满赠促销活动主信息
     * @return 满赠促销活动主信息
     */
    StrategyActivityDTO save(AddStrategyActivityRequest request);

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
     * @return 满赠促销活动主信息
     */
    StrategyActivityDTO copy(CopyStrategyRequest request);

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
    Page<StrategyActivityDTO> pageList(QueryStrategyActivityPageRequest request);

    /**
     * 营销活动分页查询
     *
     * @param request 查询条件
     * @return 营销活动主表信息
     */
    Page<PresaleActivityDTO> pageListForPresale(QueryStrategyActivityPageRequest request);

    /**
     * 根据id查询活动主表信息
     *
     * @param id 主键id
     * @return 营销活动主表信息
     */
    StrategyActivityDTO queryById(Long id);

    /**
     * 根据活动id查询营销活动订单累计金额满赠内容阶梯
     *
     * @param strategyActivityId 活动id
     * @return 营销活动订单累计金额满赠内容阶梯
     */
    List<StrategyAmountLadderDTO> listAmountLadderByActivityId(Long strategyActivityId);

    /**
     * 根据阶梯id查询订单累计金额满赠内容阶梯
     *
     * @param id id
     * @return 订单累计金额满赠内容阶梯
     */
    StrategyAmountLadderDTO queryAmountLadderById(Long id);

    /**
     * 根据阶梯id查询营销活动时间周期满赠内容阶梯
     *
     * @param id id
     * @return 营销活动时间周期满赠内容阶梯
     */
    StrategyCycleLadderDTO queryCycleLadderById(Long id);

    /**
     * 根据活动id查询营销活动时间周期满赠内容阶梯
     *
     * @param strategyActivityId 活动id
     * @return 营销活动时间周期满赠内容阶梯
     */
    List<StrategyCycleLadderDTO> listCycleLadderByActivityId(Long strategyActivityId);

    List<StrategyActivityDTO> listEffectiveStrategy(Integer strategyType, String platformSelected, Integer orderAmountLadderType, Integer orderAmountStatusType);

    List<StrategyActivityDTO> listEffectiveStrategyByTime(Integer strategyType, String platformSelected, List<Integer> orderAmountLadderTypeList, Integer orderAmountStatusType, Date time);

    /**
     * 每日定时任务赠送赠品
     */
    void strategyActivityAutoJobHandler();

    /**
     * 续费会员赠送活动赠品
     */
    void strategyMemberAutoJobHandler();

    List<StrategyActivityDTO> listStopAmountStrategyActivity(Date startTime, Date stopTime);

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


    void sendGift(StrategyActivityDTO activityDO, Long eid, List<StrategyGiftDTO> strategyGiftDOList, Long ladderId, Long orderId, Long memberId);

    /**
     * 根据赠品查询此赠品的活动信息
     *
     * @param request 查询条件
     * @return 活动信息
     */
    List<StrategyActivityDTO> pageStrategyByGiftId(QueryLotteryStrategyRequest request);

    /**
     * 根据赠品分页查询此赠品的活动信息
     *
     * @param request 查询条件
     * @return 活动信息
     */
    Page<StrategyActivityDTO> pageLotteryStrategy(QueryLotteryStrategyPageRequest request);

    /**
     * 根据赠品查询此赠品的活动信息数量
     *
     * @param lotteryActivityIdList 抽奖活动id集合
     * @return 活动信息数量
     */
    Map<Long, Integer> countStrategyByGiftId(List<Long> lotteryActivityIdList);

    PresaleActivityDTO queryPresaleActivityById(Long id);
}
