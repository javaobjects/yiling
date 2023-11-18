package com.yiling.marketing.strategy.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyRecordDO;
import com.yiling.marketing.presale.service.MarketingPresaleBuyRecordService;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
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
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyAmountLadderDO;
import com.yiling.marketing.strategy.entity.StrategyCycleLadderDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;
import com.yiling.marketing.strategy.service.StrategyActivityService;
import com.yiling.marketing.strategy.service.StrategyAmountLadderService;
import com.yiling.marketing.strategy.service.StrategyCycleLadderService;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;

/**
 * 营销活动Api(策略满赠)
 *
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyActivityApiImpl implements StrategyActivityApi {

    private final StrategyActivityService activityService;

    private final StrategyAmountLadderService amountLadderService;

    private final StrategyCycleLadderService cycleLadderService;

    @Autowired
    private MarketingPresaleBuyRecordService buyRecordService;

    @Override
    public StrategyActivityDTO save(AddStrategyActivityRequest request) {
        StrategyActivityDO activityDO = activityService.save(request);
        return PojoUtils.map(activityDO, StrategyActivityDTO.class);
    }

    @Override
    public boolean saveAll(SaveStrategyActivityRequest request) {
        return activityService.saveAll(request);
    }

    @Override
    public StrategyActivityDTO copy(CopyStrategyRequest request) {
        StrategyActivityDO activityDO = activityService.copy(request);
        return PojoUtils.map(activityDO, StrategyActivityDTO.class);
    }

    @Override
    public boolean stop(StopStrategyRequest request) {
        return activityService.stop(request);
    }

    @Override
    public Page<StrategyActivityDTO> pageList(QueryStrategyActivityPageRequest request) {
        Page<StrategyActivityDO> doPage = activityService.pageList(request);
        return PojoUtils.map(doPage, StrategyActivityDTO.class);
    }

    @Override
    public Page<PresaleActivityDTO> pageListForPresale(QueryStrategyActivityPageRequest request) {
        Page<MarketingPresaleActivityDO> doPage = activityService.pageListForPresale(request);
        Page<PresaleActivityDTO> result = PojoUtils.map(doPage, PresaleActivityDTO.class);
        if(CollectionUtils.isNotEmpty(result.getRecords())){
            List<Long> presaleIds = result.getRecords().stream().map(PresaleActivityDTO::getId).collect(Collectors.toList());
            List<PresaleActivityDTO>orderCountMap=buyRecordService.getOrderCountBypresaleIds(presaleIds);
            if(ObjectUtil.isNotEmpty(orderCountMap)){
                Map<Long, Integer> orderCountMap1 = orderCountMap.stream().collect(Collectors.toMap(PresaleActivityDTO::getId, PresaleActivityDTO::getOrderNum));
                result.getRecords().forEach(item->{
                    int count = orderCountMap1.get(item.getId()) == null ? 0 : orderCountMap1.get(item.getId());
                    item.setPresaleOrderNum(count);
                });
            }
        }
        return result;
    }

    @Override
    public StrategyActivityDTO queryById(Long id) {
        StrategyActivityDO strategyActivityDO = activityService.getById(id);
        return PojoUtils.map(strategyActivityDO, StrategyActivityDTO.class);
    }

    @Override
    public PresaleActivityDTO queryPresaleActivityById(Long id) {
        PresaleActivityDTO strategyActivityDO = activityService.queryPresaleActivityById(id);
        return strategyActivityDO;
    }

    @Override
    public List<StrategyAmountLadderDTO> listAmountLadderByActivityId(Long strategyActivityId) {
        List<StrategyAmountLadderDO> doList = amountLadderService.listAmountLadderByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyAmountLadderDTO.class);
    }

    @Override
    public StrategyAmountLadderDTO queryAmountLadderById(Long id) {
        StrategyAmountLadderDO amountLadderDO = amountLadderService.getById(id);
        return PojoUtils.map(amountLadderDO, StrategyAmountLadderDTO.class);
    }

    @Override
    public StrategyCycleLadderDTO queryCycleLadderById(Long id) {
        StrategyCycleLadderDO cycleLadderDO = cycleLadderService.getById(id);
        return PojoUtils.map(cycleLadderDO, StrategyCycleLadderDTO.class);
    }

    @Override
    public List<StrategyCycleLadderDTO> listCycleLadderByActivityId(Long strategyActivityId) {
        List<StrategyCycleLadderDO> doList = cycleLadderService.listCycleLadderByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyCycleLadderDTO.class);
    }

    @Override
    public List<StrategyActivityDTO> listEffectiveStrategy(Integer strategyType, String platformSelected, Integer orderAmountLadderType, Integer orderAmountStatusType) {
        List<StrategyActivityDO> doList = activityService.listEffectiveStrategy(strategyType, platformSelected, orderAmountLadderType, orderAmountStatusType);
        return PojoUtils.map(doList, StrategyActivityDTO.class);
    }

    @Override
    public List<StrategyActivityDTO> listEffectiveStrategyByTime(Integer strategyType, String platformSelected, List<Integer> orderAmountLadderTypeList, Integer orderAmountStatusType, Date time) {
        List<StrategyActivityDO> doList = activityService.listEffectiveStrategyByTime(strategyType, platformSelected, orderAmountLadderTypeList, orderAmountStatusType, time);
        return PojoUtils.map(doList, StrategyActivityDTO.class);
    }

    @Override
    public void strategyActivityAutoJobHandler() {
        activityService.strategyActivityAutoJobHandler();
    }

    @Override
    public void strategyMemberAutoJobHandler() {
        activityService.strategyMemberAutoJobHandler();
    }

    @Override
    public List<StrategyActivityDTO> listStopAmountStrategyActivity(Date startTime, Date stopTime) {
        List<StrategyActivityDO> doList = activityService.listStopAmountStrategyActivity(startTime, stopTime);
        return PojoUtils.map(doList, StrategyActivityDTO.class);
    }

    @Override
    public List<Long> queryGoodsStrategyInfo(QueryGoodsStrategyInfoRequest request) {
        return activityService.queryGoodsStrategyInfo(request);
    }

    @Override
    public List<StrategyActivityDTO> queryGoodsStrategyGift(QueryGoodsStrategyInfoRequest request) {
        return activityService.queryGoodsStrategyGift(request);
    }

    @Override
    public Boolean sendGiftAfterBuyMember(String orderNo) {
        return activityService.sendGiftAfterBuyMember(orderNo);
    }

    @Override
    public StrategyActivityEidOrGoodsIdDTO getGoodsListPageByActivityId(Long strategyActivityId, Long buyerEid) {
        return activityService.getGoodsListPageByActivityId(strategyActivityId, buyerEid);
    }

    @Override
    public void sendGift(StrategyActivityDTO activityDO, Long eid, List<StrategyGiftDTO> strategyGiftDOList, Long ladderId, Long orderId, Long memberId) {
        StrategyActivityDO strategyActivityDO = PojoUtils.map(activityDO, StrategyActivityDO.class);
        List<StrategyGiftDO> giftDOList = PojoUtils.map(strategyGiftDOList, StrategyGiftDO.class);
        activityService.sendGift(strategyActivityDO, eid, giftDOList, ladderId, orderId, memberId);
    }

    @Override
    public List<StrategyActivityDTO> pageStrategyByGiftId(QueryLotteryStrategyRequest request) {
        List<StrategyActivityDO> doList = activityService.pageStrategyByGiftId(request);
        return PojoUtils.map(doList, StrategyActivityDTO.class);
    }

    @Override
    public Page<StrategyActivityDTO> pageLotteryStrategy(QueryLotteryStrategyPageRequest request) {
        Page<StrategyActivityDO> doPage = activityService.pageLotteryStrategy(request);
        return PojoUtils.map(doPage, StrategyActivityDTO.class);
    }

    @Override
    public Map<Long, Integer> countStrategyByGiftId(List<Long> lotteryActivityIdList) {
        return activityService.countStrategyByGiftId(lotteryActivityIdList);
    }
}
