package com.yiling.marketing;

/**
 * @author: ray
 * @date: 2021/5/21
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dto.request.QueryActivityDetailRequest;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.presale.service.MarketingPresaleActivityService;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartGoods;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionEnterpriseRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeGoodsRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionReduceRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequestItem;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateDetailRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionGoodsLimitDO;
import com.yiling.marketing.promotion.service.PromotionActivityService;
import com.yiling.marketing.promotion.service.PromotionBuyRecordService;
import com.yiling.marketing.strategy.service.StrategyActivityService;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gxl
 * @description:
 * @date: Created in 8:44 2021/5/21
 * @modified By:
 */
@Slf4j
public class PromotionServiceTest extends BaseTest {

    @Autowired
    PromotionActivityService promotionActivityService;

    @Autowired
    PromotionBuyRecordService buyRecordService;

    @Autowired
    CouponActivityService couponActivityService;

    @Autowired
    StrategyActivityService strategyActivityService;
    @Autowired
    CouponService couponService;
    @Autowired
    private GoodsGiftService goodsGiftService;

    @Autowired
    MarketingPresaleActivityService presaleActivityService;

    @Test
    public void presaleActivityService(){
        QueryPresaleInfoRequest queryPresaleInfoRequest = new QueryPresaleInfoRequest();
        List<Long> goodsIds = new ArrayList<>(1);
        goodsIds.add(3457L);
        queryPresaleInfoRequest.setGoodsId(goodsIds);
        queryPresaleInfoRequest.setPresaleId(1L);
        queryPresaleInfoRequest.setBuyEid(54L);
        queryPresaleInfoRequest.setPlatformSelected(1);
        List<PresaleActivityGoodsDTO> presaleInfoByGoodsIdAndBuyEid = presaleActivityService.getPresaleInfoByGoodsIdAndBuyEid(queryPresaleInfoRequest);
        System.out.println(presaleInfoByGoodsIdAndBuyEid);
    }

    @Test
    public void test() {
        //        strategyActivityService.strategyActivityAutoJobHandler();
        Map<Long, Integer> longIntegerMap = strategyActivityService.countStrategyByGiftId(new ArrayList<Long>() {{
            add(1L);
            add(2L);
            add(3L);
        }});
        System.out.println(longIntegerMap);
        //        Date now = DateTime.now();
        //        int dayOfWeek = DateUtil.dayOfWeek(now);
        //        System.out.println(dayOfWeek);
    }

    @Test
    public void listByI1d () {
        List<Long> ids = new ArrayList<>();
        ids.add(4L);
        ids.add(2L);
        ids.add(3L);
        List<GoodsGiftDO> goodsGiftList = goodsGiftService.listByIds(ids);
        if (CollectionUtils.isEmpty(goodsGiftList)) {

        }
        Map<Long, Long> collect = goodsGiftList.stream().collect(Collectors.toMap(GoodsGiftDO::getId, GoodsGiftDO::getAvailableQuantity));
        Map<Long, Long> result = new HashMap<>(ids.size());
        ids.forEach(item -> {
            if (collect.get(item) != null) {
                result.put(item, collect.get(item));
            } else {
                result.put(item, 0L);
            }
        });
    }


    @Test
    public void listById() {
        List<PromotionActivityDO> list = promotionActivityService.listByIdList(Arrays.asList(1L));
        System.out.println(list);
    }

    @Test
    public void checkRepeat() {
        List<PromotionGoodsLimitDO> result = promotionActivityService.queryNotRepeatByGoodsIdList(Arrays.asList(2728L), 2, 1, null);
        System.out.println(result);
    }

    @Test
    public void enterprisePromotionTest() {
        PromotionEnterpriseRequest request = PromotionEnterpriseRequest.builder().eIdList(Arrays.asList(35L)).platform(1).build();
        List<PromotionAppListDTO> result = promotionActivityService.queryEnterprisePromotion(request);
        System.out.println(result);
    }

    @Test
    public void promotionReduceStock() {

        PromotionReduceRequest request = new PromotionReduceRequest();

        request.setReduceStockList(Arrays.asList(PromotionReduceStockDTO.builder().promotionActivityId(268L).goodsGiftId(61L).build()));

        boolean b = promotionActivityService.promotionReduceStock(request);
        System.out.println(b);

    }

    @Test
    public void testJudge() {
        PromotionJudgeRequest request = new PromotionJudgeRequest();
        List<PromotionJudgeGoodsRequest> goodsRequestList = new ArrayList<>();
        goodsRequestList.add(PromotionJudgeGoodsRequest.builder().goodsId(2784L).amount(BigDecimal.valueOf(700L)).build());
        goodsRequestList.add(PromotionJudgeGoodsRequest.builder().goodsId(2785L).amount(BigDecimal.valueOf(100L)).build());
        request.setGoodsRequestList(goodsRequestList);

        promotionActivityService.judgePromotion(request);

    }

    @Test
    public void testSettleJudge() {
        PromotionSettleJudgeRequest request = new PromotionSettleJudgeRequest();
        PromotionSettleJudgeRequestItem item1 = PromotionSettleJudgeRequestItem.builder().promotionActivityId(204L).goodsGiftLimitId(154L).build();
        PromotionSettleJudgeRequestItem item2 = PromotionSettleJudgeRequestItem.builder().promotionActivityId(205L).goodsGiftLimitId(155L).build();
        List<PromotionSettleJudgeRequestItem> requestItemList = Lists.newArrayList(item1, item2);

        PromotionJudgeGoodsRequest goods1 = PromotionJudgeGoodsRequest.builder().goodsId(2784L).amount(BigDecimal.valueOf(1)).build();
        PromotionJudgeGoodsRequest goods2 = PromotionJudgeGoodsRequest.builder().goodsId(2785L).amount(BigDecimal.valueOf(2)).build();
        List<PromotionJudgeGoodsRequest> goodsRequestList = Lists.newArrayList(goods1, goods2);

        request.setRequestItemList(requestItemList);
        request.setGoodsRequestList(goodsRequestList);

        promotionActivityService.settleJudgePromotion(request);

    }

    @Test
    public void test1() {
        PromotionAppCartRequest cartRequest = PromotionAppCartRequest.builder().build();
        cartRequest.setCartGoodsList(Arrays.asList(PromotionAppCartGoods.builder().goodsId(3484L).amount(BigDecimal.valueOf(100)).build()));
        List<PromotionAppListDTO> promotionAppListDTOS = promotionActivityService.queryAppCartPromotion(cartRequest);
        System.out.println(promotionAppListDTOS);

    }

    @Test
    public void queryGoodsInfo() {
        Arrays.asList(5110, 5113, 5116, 5118, 2006, 2681, 2828, 2831, 18139, 2740, 2741, 2742, 2744, 43618, 43620, 43621, 43624).stream().forEach(item -> {
            MDC.put(Constants.TRACE_ID, "123456789012345678");
            PromotionAppRequest request = new PromotionAppRequest();
            request.setGoodsIdList(Arrays.asList(Long.valueOf(item)));
            //        request.setGoodsIdList(Arrays.asList(2728L));
            request.setPlatform(PlatformEnum.B2B.getCode());
            request.setBuyerEid(220L);
            promotionActivityService.queryGoodsPromotionInfo(request);
        });

    }

    @Test
    public void saveBuyRecord() {
        PromotionSaveBuyRecordRequest request = new PromotionSaveBuyRecordRequest();
        List<PromotionBuyRecord> buyRecordList = Lists.newArrayList();
        PromotionBuyRecord rec = PromotionBuyRecord.builder().promotionActivityId(1L).eid(1L).goodsId(2825L).goodsQuantity(1).build();
        rec.setOrderId(222L);
        rec.setOpTime(new Date());
        rec.setOpUserId(46L);
        buyRecordList.add(rec);
        request.setBuyRecordList(buyRecordList);
        buyRecordService.saveBuyRecord(request);
    }

    @Test
    public void updateBuyRecord() {
        PromotionUpdateBuyRecordRequest rec = new PromotionUpdateBuyRecordRequest();
        List<PromotionUpdateDetailRequest> detailList = new ArrayList<>();
        PromotionUpdateDetailRequest detail = new PromotionUpdateDetailRequest();
        detail.setGoodsId(2825L);
        detail.setQuantity(1);
        detailList.add(detail);

        rec.setOrderId(222L);
        rec.setDetailList(detailList);
        rec.setOpTime(new Date());
        rec.setOpUserId(46L);
        Boolean result = buyRecordService.updateBuyRecordQuantity(rec);
        System.out.println("============result:" + result);
    }

    @Test
    public void getActivityIdByCouponId() {
        QueryActivityDetailRequest request = new QueryActivityDetailRequest();
        request.setEid(8L);
        request.setCouponId(750L);
        request.setMemberId(2L);
        Long activityIdByCouponId = couponActivityService.getActivityIdByCouponId(request);
        System.out.println("============result:" + activityIdByCouponId);
    }

    @Test
    public void getActivityId1ByCouponId() {
        QueryPresaleInfoRequest presaleRequest = new QueryPresaleInfoRequest();
        presaleRequest.setBuyEid(220L);
        presaleRequest.setGoodsId(ListUtil.toList(43744l));
        presaleRequest.setPlatformSelected(1);
        List<PresaleActivityGoodsDTO> result = presaleActivityService.getPresaleInfoByGoodsIdAndBuyEid(presaleRequest);
        System.out.println("============result:" + result);
    }


}
