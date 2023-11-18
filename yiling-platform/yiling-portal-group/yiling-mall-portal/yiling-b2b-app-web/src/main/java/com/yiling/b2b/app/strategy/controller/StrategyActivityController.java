package com.yiling.b2b.app.strategy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yiling.b2b.app.goods.utils.GoodsAssemblyUtils;
import com.yiling.b2b.app.strategy.form.QueryLotteryStrategyFrom;
import com.yiling.b2b.app.strategy.form.QueryStrategyGoodsSearchFrom;
import com.yiling.b2b.app.strategy.vo.LotteryBuyStageMemberVO;
import com.yiling.b2b.app.strategy.vo.LotteryStrategyVO;
import com.yiling.b2b.app.strategy.vo.StrategyGoodsSearchListVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.api.StrategyActivityRecordApi;
import com.yiling.marketing.strategy.api.StrategyBuyerApi;
import com.yiling.marketing.strategy.api.StrategyEnterpriseGoodsApi;
import com.yiling.marketing.strategy.api.StrategyGiftApi;
import com.yiling.marketing.strategy.api.StrategyMemberApi;
import com.yiling.marketing.strategy.api.StrategyPlatformGoodsApi;
import com.yiling.marketing.strategy.api.StrategyPromoterMemberApi;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.api.StrategyStageMemberEffectApi;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityEidOrGoodsIdDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyCycleLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.StrategyMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategySellerLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyStageMemberEffectDTO;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyRequest;
import com.yiling.marketing.strategy.enums.StrategyConditionEnterpriseTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.QueryOrderTimeIntervalRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.search.goods.api.EsGoodsSearchApi;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.bo.MemberBuyRecordBO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.request.QueryMemberBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 策略满赠
 *
 * @author: yong.zhang
 * @date: 2022/9/15
 */
@Slf4j
@Api(tags = "策略满赠")
@RestController
@RequestMapping("/strategy/activity")
public class StrategyActivityController extends BaseController {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    EsGoodsSearchApi esGoodsSearchApi;

    @DubboReference
    StrategyGiftApi strategyGiftApi;

    @DubboReference
    StrategyStageMemberEffectApi strategyStageMemberEffectApi;

    @DubboReference
    MemberBuyStageApi memberBuyStageApi;

    @DubboReference
    MemberApi memberApi;

    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;

    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategyBuyerApi strategyBuyerApi;

    @DubboReference
    StrategyMemberApi strategyMemberApi;

    @DubboReference
    StrategyPromoterMemberApi strategyPromoterMemberApi;

    @DubboReference
    OrderFirstInfoApi firstInfoApi;

    @DubboReference
    StrategySellerApi strategySellerApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;

    @DubboReference
    StrategyPlatformGoodsApi strategyPlatformGoodsApi;

    @DubboReference
    StrategyEnterpriseGoodsApi strategyEnterpriseGoodsApi;

    @DubboReference
    StrategyActivityRecordApi strategyActivityRecordApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    CustomerApi customerApi;

    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;

    @UserAccessAuthentication
    @ApiOperation(value = "策略满赠活动-可使用商品列表", httpMethod = "POST")
    @PostMapping(path = "/strategyGoodsSearch")
    public Result<StrategyGoodsSearchListVO> strategyGoodsSearch(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryStrategyGoodsSearchFrom form) {
        log.info("strategyGoodsSearch, form -> {}", JSON.toJSONString(form));
        log.info("strategyGoodsSearch, staffInfo -> {}", JSON.toJSONString(staffInfo));

        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        StrategyActivityEidOrGoodsIdDTO activityEidOrGoodsIdDTO = strategyActivityApi.getGoodsListPageByActivityId(form.getStrategyId(), buyerEid);
        log.info("strategyGoodsSearch, activityEidOrGoodsIdDTO -> {}", JSON.toJSONString(activityEidOrGoodsIdDTO));

        if (activityEidOrGoodsIdDTO == null) {
            StrategyGoodsSearchListVO strategyGoodsSearchListVO = new StrategyGoodsSearchListVO();
            //            strategyGoodsSearchListVO.setTitle(activityEidOrGoodsIdDTO.getTitle());
            return Result.success(strategyGoodsSearchListVO);
        }

        EsActivityGoodsSearchRequest request = PojoUtils.map(form, EsActivityGoodsSearchRequest.class);
        request.setMallFlag(1);
        request.setMallStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAllEidFlag(activityEidOrGoodsIdDTO.getAllEidFlag());
        request.setEidList(activityEidOrGoodsIdDTO.getEidList());
        request.setGoodsIdList(activityEidOrGoodsIdDTO.getGoodsIdList());
        request.setSellSpecificationsIdList(activityEidOrGoodsIdDTO.getSellSpecificationsIdList());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        if (buyerEid > 0) {
            QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
            enterpriseRequest.setCustomerEid(buyerEid);
            enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
            enterpriseRequest.setLimit(50);
            List<Long> sortEid = customerApi.getEidListByCustomerEid(enterpriseRequest);
            request.setSortEid(sortEid);
        }
        EsAggregationDTO data = esGoodsSearchApi.searchActivityGoods(request);
        log.info("strategyGoodsSearch, data -> {}", JSON.toJSONString(data));
        List<GoodsInfoDTO> goodsAggDTOList = data.getData();
        if (CollUtil.isNotEmpty(goodsAggDTOList)) {
            // 商品ID集合
            List<Long> goodsIds = goodsAggDTOList.stream().map(BaseDTO::getId).distinct().collect(Collectors.toList());
            data.setData(goodsAssemblyUtils.assembly(goodsIds, buyerEid));
        }
        StrategyGoodsSearchListVO strategyGoodsSearchListVO = PojoUtils.map(data, StrategyGoodsSearchListVO.class);
        strategyGoodsSearchListVO.setTitle(activityEidOrGoodsIdDTO.getTitle());
        return Result.success(strategyGoodsSearchListVO);
    }

    @UserAccessAuthentication
    @ApiOperation(value = "根据抽奖活动id查询策略满赠活动信息", httpMethod = "POST")
    @PostMapping(path = "/getStrategyInfoByLotteryId")
    public Result<List<LotteryStrategyVO>> getStrategyInfoByLotteryId(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryLotteryStrategyFrom form) {
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        QueryLotteryStrategyRequest lotteryStrategyRequest = PojoUtils.map(form, QueryLotteryStrategyRequest.class);
        lotteryStrategyRequest.setOpUserId(buyerEid);
        List<StrategyActivityDTO> dtoList = strategyActivityApi.pageStrategyByGiftId(lotteryStrategyRequest);
        List<StrategyActivityDTO> strategyActivityDTOList = matchStrategyBuyer(dtoList, buyerEid);
        Boolean newVisitor = firstInfoApi.checkNewVisitor(buyerEid, OrderTypeEnum.B2B);
        List<LotteryStrategyVO> lotteryStrategyList = new ArrayList<>();
        for (StrategyActivityDTO strategyActivityDTO : strategyActivityDTOList) {
            if (strategyActivityDTO.getConditionOther().contains("1") && !newVisitor) {
                log.info("getStrategyInfoByLotteryId 非新客用户，不允许参与新客活动, eid:[{}], 活动id:[{}]", buyerEid, strategyActivityDTO.getId());
                continue;
            }

            LotteryStrategyVO lotteryStrategyVO = PojoUtils.map(strategyActivityDTO, LotteryStrategyVO.class);
            lotteryStrategyVO.setIsSuccess(0);
            // todo 同一种活动分类（平台活动/商家活动），如果有多个策略满赠活动，根据叠加互斥规则，只能参加创建时间最新的一条。 所以，此处只展示创建时间最新的一条

            StrategyTypeEnum strategyTypeEnum = StrategyTypeEnum.getByType(strategyActivityDTO.getStrategyType());
            // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配)
            Integer orderAmountLadderType = strategyActivityDTO.getOrderAmountLadderType();
            if (strategyTypeEnum == StrategyTypeEnum.ORDER_AMOUNT && 3 == orderAmountLadderType) {
                String platformSelected = strategyActivityDTO.getPlatformSelected();
                if (!platformSelected.contains("1") && !platformSelected.contains("2")) {
                    log.info("getStrategyInfoByLotteryId platformSelected fail 活动id:[{}]", strategyActivityDTO.getId());
                    continue;
                }
                List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(strategyActivityDTO.getId(), null);
                List<StrategyGiftDTO> lotteryGiftList = strategyGiftDTOList.stream().filter(e -> e.getType() == 3 && e.getGiftId().equals(lotteryStrategyRequest.getLotteryActivityId())).collect(Collectors.toList());
                Map<Long, StrategyGiftDTO> giftDTOMap = lotteryGiftList.stream().collect(Collectors.toMap(StrategyGiftDTO::getLadderId, e -> e, (k1, k2) -> k1));
                List<Long> ladderIdList = lotteryGiftList.stream().map(StrategyGiftDTO::getLadderId).collect(Collectors.toList());
                List<StrategyAmountLadderDTO> amountLadderDTOList = strategyActivityApi.listAmountLadderByActivityId(strategyActivityDTO.getId());
                List<StrategyAmountLadderDTO> amountLadderFilterList = amountLadderDTOList.stream().filter(e -> ladderIdList.contains(e.getId())).collect(Collectors.toList());
                List<StrategyAmountLadderDTO> amountLadderList = amountLadderFilterList.stream().sorted(Comparator.comparing(StrategyAmountLadderDTO::getAmountLimit)).collect(Collectors.toList());
                // 单笔订单金额满{阶梯金额1}送{赠品}*{数量}；满{阶梯金额2}送{赠品}*{数量}；满{阶梯金额3}送{赠品}*{数量}。
                // 例如：2022.11.1-2022.11.15期间，单笔订单金额满100元送双十一抽奖*2；满500元送双十一抽奖*3；满1000元送双十一抽奖*5。
                StringBuilder contentSb = new StringBuilder();
                String beginTimeStr = DateUtil.format(strategyActivityDTO.getBeginTime(), "yyyy.MM.dd");
                String endTimeStr = DateUtil.format(strategyActivityDTO.getEndTime(), "yyyy.MM.dd");
                contentSb.append(beginTimeStr).append("至").append(endTimeStr).append("期间，单笔订单金额满");
                String remark = "";
                for (StrategyAmountLadderDTO amountLadderDTO : amountLadderList) {
                    StrategyGiftDTO strategyGiftDTO = giftDTOMap.get(amountLadderDTO.getId());
                    contentSb.append(amountLadderDTO.getAmountLimit()).append("元送").append(strategyGiftDTO.getCount()).append("次 ,");
                }
                contentSb.deleteCharAt(contentSb.length() - 1);
                lotteryStrategyVO.setContent(contentSb.toString());
                lotteryStrategyVO.setRemark(remark);
            } else if (strategyTypeEnum == StrategyTypeEnum.ORDER_AMOUNT) {
                String platformSelected = strategyActivityDTO.getPlatformSelected();
                if (!platformSelected.contains("1") && !platformSelected.contains("2")) {
                    log.info("getStrategyInfoByLotteryId platformSelected fail 活动id:[{}]", strategyActivityDTO.getId());
                    continue;
                }

                List<Long> sellerEidList = new ArrayList<>();
                {
                    // 商家范围筛选
                    StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(strategyActivityDTO.getConditionSellerType());
                    if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
                        List<StrategySellerLimitDTO> strategySellerLimitDOList = strategySellerApi.listByActivityIdAndEidList(strategyActivityDTO.getId(), null);
                        sellerEidList = strategySellerLimitDOList.stream().map(StrategySellerLimitDTO::getEid).collect(Collectors.toList());
                    }
                }
                StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(strategyActivityDTO.getConditionGoodsType());
                BigDecimal amount = BigDecimal.ZERO;
                {
                    QueryOrderTimeIntervalRequest request = new QueryOrderTimeIntervalRequest();
                    if (CollUtil.isNotEmpty(sellerEidList)) {
                        request.setSellerEidList(sellerEidList);
                    }
                    request.setBuyerEidList(new ArrayList<Long>() {{
                        add(buyerEid);
                    }});
                    request.setStartCreateTime(strategyActivityDTO.getBeginTime());
                    request.setEndCreateTime(strategyActivityDTO.getEndTime());
                    request.setOrderType(OrderTypeEnum.B2B.getCode());
                    // 订单累计金额-限制支付方式(1-全部支付方式;2-部分支付方式)
                    Integer orderAmountPayType = strategyActivityDTO.getOrderAmountPayType();
                    if (2 == orderAmountPayType) {
                        List<Integer> orderAmountPaymentType = JSON.parseArray(strategyActivityDTO.getOrderAmountPaymentType(), Integer.class);
                        request.setPaymentMethodList(orderAmountPaymentType);
                    }
                    // 选择平台（1-B2B；2-销售助手）逗号隔开
                    if (platformSelected.contains("1") && !platformSelected.contains("2")) {
                        request.setOrderSource(OrderSourceEnum.B2B_APP.getCode());
                    } else if (!platformSelected.contains("1") && platformSelected.contains("2")) {
                        request.setOrderSource(OrderSourceEnum.SA.getCode());
                    }
                    // 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
                    List<Integer> orderStatusList = new ArrayList<Integer>() {{
                        add(25);
                        add(30);
                        add(40);
                        add(100);
                    }};
                    // 订单累计金额-限制订单状态(1-发货计算；2-下单计算)
                    if (1 == strategyActivityDTO.getOrderAmountStatusType()) {
                        request.setOrderStatusList(orderStatusList);
                    }
                    // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配) orderAmountLadderType
                    if (2 == strategyActivityDTO.getOrderAmountStatusType() && 2 == strategyActivityDTO.getOrderAmountLadderType()) {
                        orderStatusList.add(10);
                        orderStatusList.add(20);
                        request.setOrderStatusList(orderStatusList);
                    }
                    request.setCustomerConfirmStatus(-40);
                    log.info("getStrategyInfoByLotteryId get goods amount orderIdList request:[{}]", request);
                    Map<Long, List<OrderDTO>> longListMap = orderApi.getTimeIntervalTypeOrder(request);
                    log.info("getStrategyInfoByLotteryId get goods amount orderListMap:[{}]", longListMap);
                    List<OrderDTO> orderDTOList = longListMap.get(buyerEid);
                    if (CollUtil.isNotEmpty(orderDTOList)) {
                        List<Long> orderIdList = orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList());
                        List<OrderDetailChangeDTO> orderDetailChangeDTOList = orderDetailChangeApi.listByOrderIds(orderIdList);
                        if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ALL) {
                            for (OrderDetailChangeDTO orderDetailChangeDTO : orderDetailChangeDTOList) {
                                if (1 == strategyActivityDTO.getOrderAmountStatusType()) {
                                    BigDecimal deliverAmount = orderDetailChangeDTO.getDeliveryAmount().subtract(orderDetailChangeDTO.getDeliveryTicketDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCashDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCouponDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryPlatformCouponDiscountAmount());
                                    BigDecimal returnAmount = orderDetailChangeDTO.getReturnAmount().subtract(orderDetailChangeDTO.getReturnTicketDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCashDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCouponDiscountAmount()).subtract(orderDetailChangeDTO.getReturnPlatformCouponDiscountAmount());
                                    amount = amount.add(deliverAmount).subtract(returnAmount);
                                } else if (2 == strategyActivityDTO.getOrderAmountStatusType()) {
                                    BigDecimal orderAmount = orderDetailChangeDTO.getGoodsAmount().subtract(orderDetailChangeDTO.getTicketDiscountAmount()).subtract(orderDetailChangeDTO.getCashDiscountAmount()).subtract(orderDetailChangeDTO.getCouponDiscountAmount()).subtract(orderDetailChangeDTO.getPlatformCouponDiscountAmount());
                                    amount = amount.add(orderAmount);
                                }
                            }
                        } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                            List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailByOrderIds(orderIdList);
                            List<Long> goodsId = orderDetailDTOList.stream().map(OrderDetailDTO::getDistributorGoodsId).collect(Collectors.toList());
                            List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsId);
                            Map<Long, Long> goodsIdMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, GoodsDTO::getSellSpecificationsId, (k1, k2) -> k1));
                            List<Long> sellSpecificationsIdList = goodsDTOList.stream().map(GoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
                            List<StrategyPlatformGoodsLimitDTO> platformGoodsLimitDOList = strategyPlatformGoodsApi.listByActivityIdAndSellSpecificationsIdList(strategyActivityDTO.getId(), sellSpecificationsIdList);
                            List<Long> matchSellSpecificationsIdList = platformGoodsLimitDOList.stream().map(StrategyPlatformGoodsLimitDTO::getSellSpecificationsId).collect(Collectors.toList());
                            Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeDTOList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, e -> e, (k1, k2) -> k1));
                            for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
                                Long sellSpecificationsId = goodsIdMap.get(orderDetailDTO.getDistributorGoodsId());
                                if (matchSellSpecificationsIdList.contains(sellSpecificationsId)) {
                                    OrderDetailChangeDTO orderDetailChangeDTO = orderDetailChangeMap.get(orderDetailDTO.getId());
                                    if (1 == strategyActivityDTO.getOrderAmountStatusType()) {
                                        BigDecimal deliverAmount = orderDetailChangeDTO.getDeliveryAmount().subtract(orderDetailChangeDTO.getDeliveryTicketDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCashDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCouponDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryPlatformCouponDiscountAmount());
                                        BigDecimal returnAmount = orderDetailChangeDTO.getReturnAmount().subtract(orderDetailChangeDTO.getReturnTicketDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCashDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCouponDiscountAmount()).subtract(orderDetailChangeDTO.getReturnPlatformCouponDiscountAmount());
                                        amount = amount.add(deliverAmount).subtract(returnAmount);
                                    } else if (2 == strategyActivityDTO.getOrderAmountStatusType()) {
                                        BigDecimal orderAmount = orderDetailChangeDTO.getGoodsAmount().subtract(orderDetailChangeDTO.getTicketDiscountAmount()).subtract(orderDetailChangeDTO.getCashDiscountAmount()).subtract(orderDetailChangeDTO.getCouponDiscountAmount()).subtract(orderDetailChangeDTO.getPlatformCouponDiscountAmount());
                                        amount = amount.add(orderAmount);
                                    }
                                }
                            }
                        } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                            List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailByOrderIds(orderIdList);
                            List<Long> goodsIdList = orderDetailDTOList.stream().map(OrderDetailDTO::getDistributorGoodsId).collect(Collectors.toList());
                            List<StrategyEnterpriseGoodsLimitDTO> enterpriseGoodsLimitDOList = strategyEnterpriseGoodsApi.listByActivityIdAndGoodsIdList(strategyActivityDTO.getId(), goodsIdList);
                            List<Long> matchGoodsIdList = enterpriseGoodsLimitDOList.stream().map(StrategyEnterpriseGoodsLimitDTO::getGoodsId).collect(Collectors.toList());
                            Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeDTOList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, e -> e, (k1, k2) -> k1));
                            for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
                                if (matchGoodsIdList.contains(orderDetailDTO.getDistributorGoodsId())) {
                                    OrderDetailChangeDTO orderDetailChangeDTO = orderDetailChangeMap.get(orderDetailDTO.getId());
                                    if (1 == strategyActivityDTO.getOrderAmountStatusType()) {
                                        BigDecimal deliverAmount = orderDetailChangeDTO.getDeliveryAmount().subtract(orderDetailChangeDTO.getDeliveryTicketDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCashDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCouponDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryPlatformCouponDiscountAmount());
                                        BigDecimal returnAmount = orderDetailChangeDTO.getReturnAmount().subtract(orderDetailChangeDTO.getReturnTicketDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCashDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCouponDiscountAmount()).subtract(orderDetailChangeDTO.getReturnPlatformCouponDiscountAmount());
                                        amount = amount.add(deliverAmount).subtract(returnAmount);
                                    } else if (2 == strategyActivityDTO.getOrderAmountStatusType()) {
                                        BigDecimal orderAmount = orderDetailChangeDTO.getGoodsAmount().subtract(orderDetailChangeDTO.getTicketDiscountAmount()).subtract(orderDetailChangeDTO.getCashDiscountAmount()).subtract(orderDetailChangeDTO.getCouponDiscountAmount()).subtract(orderDetailChangeDTO.getPlatformCouponDiscountAmount());
                                        amount = amount.add(orderAmount);
                                    }
                                }
                            }
                        }
                    }
                }

                lotteryStrategyVO.setAmount(amount);
                List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(strategyActivityDTO.getId(), null);
                List<StrategyGiftDTO> lotteryGiftList = strategyGiftDTOList.stream().filter(e -> e.getType() == 3 && e.getGiftId().equals(lotteryStrategyRequest.getLotteryActivityId())).collect(Collectors.toList());
                Map<Long, StrategyGiftDTO> giftDTOMap = lotteryGiftList.stream().collect(Collectors.toMap(StrategyGiftDTO::getLadderId, e -> e, (k1, k2) -> k1));
                List<Long> ladderIdList = lotteryGiftList.stream().map(StrategyGiftDTO::getLadderId).collect(Collectors.toList());
                List<StrategyAmountLadderDTO> amountLadderDTOList = strategyActivityApi.listAmountLadderByActivityId(strategyActivityDTO.getId());
                List<StrategyAmountLadderDTO> amountLadderFilterList = amountLadderDTOList.stream().filter(e -> ladderIdList.contains(e.getId())).collect(Collectors.toList());
                List<StrategyAmountLadderDTO> amountLadderList = amountLadderFilterList.stream().sorted(Comparator.comparing(StrategyAmountLadderDTO::getAmountLimit)).collect(Collectors.toList());
                // 2022.7.1至2022.7.30期间，订单累计金额满3333.33元送1次 ,5555.55元送3次 ,8888.88元送5次
                StringBuilder contentSb = new StringBuilder();
                String beginTimeStr = DateUtil.format(strategyActivityDTO.getBeginTime(), "yyyy.MM.dd");
                String endTimeStr = DateUtil.format(strategyActivityDTO.getEndTime(), "yyyy.MM.dd");
                contentSb.append(beginTimeStr).append("至").append(endTimeStr).append("期间，订单累计金额满");
                String remark = "";
                for (StrategyAmountLadderDTO amountLadderDTO : amountLadderList) {
                    StrategyGiftDTO strategyGiftDTO = giftDTOMap.get(amountLadderDTO.getId());
                    contentSb.append(amountLadderDTO.getAmountLimit()).append("元送").append(strategyGiftDTO.getCount()).append("次 ,");
                    if (amount.compareTo(amountLadderDTO.getAmountLimit()) >= 0) {
                        lotteryStrategyVO.setIsSuccess(1);
                    } else if (StringUtils.isBlank(remark)) {
                        String endTime = "";
                        BigDecimal needAmount = amountLadderDTO.getAmountLimit().subtract(amount);
                        // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配)
                        if (1 == orderAmountLadderType) {
                            endTime = strategyActivityDTO.getOrderAmountStatusType() == 1 ? "订单发货后" : "订单提交后";
                            // 订单累计金额-限制订单状态(1-发货计算；2-下单计算)
                            remark = "再买" + needAmount + "元，获得" + strategyGiftDTO.getCount() + "次抽奖机会（预计" + endTime + "发放）";
                        } else if (2 == orderAmountLadderType) {
                            DateTime dateTime = DateUtil.offsetDay(strategyActivityDTO.getEndTime(), 1);
                            endTime = DateUtil.format(dateTime, "yyyy.MM.dd");
                            // 订单累计金额-限制订单状态(1-发货计算；2-下单计算)
                            remark = "再买" + needAmount + "元，获得" + strategyGiftDTO.getCount() + "次抽奖机会（预计" + endTime + "发放）";
                        }
                    }
                }
                if (StringUtils.isBlank(remark)) {
                    StrategyAmountLadderDTO strategyAmountLadderDTO = amountLadderList.get(amountLadderList.size() - 1);
                    StrategyGiftDTO strategyGiftDTO = giftDTOMap.get(strategyAmountLadderDTO.getId());
                    if (1 == orderAmountLadderType) {
                        remark = "您已获得" + strategyGiftDTO.getCount() + "次抽奖机会（" + (strategyActivityDTO.getOrderAmountStatusType() == 1 ? "订单发货后" : "订单提交后") + "发放）";
                    } else if (2 == orderAmountLadderType) {
                        DateTime dateTime = DateUtil.offsetDay(strategyActivityDTO.getEndTime(), 1);
                        remark = "您将获得" + strategyGiftDTO.getCount() + "次抽奖机会（预计" + (DateUtil.format(dateTime, "yyyy.MM.dd")) + "发放）";
                    }
                }
                contentSb.deleteCharAt(contentSb.length() - 1);
                lotteryStrategyVO.setContent(contentSb.toString());
                lotteryStrategyVO.setRemark(remark);
            } else if (strategyTypeEnum == StrategyTypeEnum.SIGN_DAYS) {
            } else if (strategyTypeEnum == StrategyTypeEnum.CYCLE_TIME) {
                // 时间周期-频率(1-按周;2-按月)
                Integer cycleRate = strategyActivityDTO.getCycleRate();
                List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(strategyActivityDTO.getId(), null);
                List<StrategyGiftDTO> lotteryGiftList = strategyGiftDTOList.stream().filter(e -> e.getType() == 3 && e.getGiftId().equals(lotteryStrategyRequest.getLotteryActivityId())).collect(Collectors.toList());
                List<StrategyCycleLadderDTO> cycleLadderDTOList = strategyActivityApi.listCycleLadderByActivityId(strategyActivityDTO.getId());
                Map<Long, StrategyCycleLadderDTO> cycleLadderMap = cycleLadderDTOList.stream().collect(Collectors.toMap(StrategyCycleLadderDTO::getId, e -> e, (k1, k2) -> k1));
                StringBuilder contentSb = new StringBuilder();
                for (StrategyGiftDTO strategyGiftDTO : lotteryGiftList) {
                    StrategyCycleLadderDTO cycleLadderDTO = cycleLadderMap.get(strategyGiftDTO.getLadderId());
                    if (StringUtils.isNotBlank(cycleLadderDTO.getConditionValue())) {
                        List<Integer> conditionValue = JSON.parseArray(cycleLadderDTO.getConditionValue(), Integer.class);
                        if (1 == cycleRate) {
                            contentSb.append("每周");
                            for (Integer week : conditionValue) {
                                String time = getWeek(week);
                                if (StringUtils.isNotBlank(time)) {
                                    contentSb.append(time).append("、");
                                }
                            }
                            contentSb.deleteCharAt(contentSb.length() - 1);
                            contentSb.append("赠送").append(strategyGiftDTO.getCount()).append("次；");
                        } else if (2 == cycleRate) {
                            contentSb.append("每月");
                            for (Integer date : conditionValue) {
                                contentSb.append(date).append("、");
                            }
                            contentSb.deleteCharAt(contentSb.length() - 1);
                            contentSb.append("赠送").append(strategyGiftDTO.getCount()).append("次；");
                        }
                    }
                }
                lotteryStrategyVO.setContent(contentSb.toString());
            } else if (strategyTypeEnum == StrategyTypeEnum.PURCHASE_MEMBER) {
                lotteryStrategyVO.setRemark("注：会员生效后发放");
                List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(strategyActivityDTO.getId(), null);
                List<StrategyGiftDTO> lotteryGiftList = strategyGiftDTOList.stream().filter(e -> e.getType() == 3 && e.getGiftId().equals(lotteryStrategyRequest.getLotteryActivityId())).collect(Collectors.toList());
                if (CollUtil.isEmpty(lotteryGiftList)) {
                    continue;
                }
                List<StrategyStageMemberEffectDTO> effectDTOList = strategyStageMemberEffectApi.listByActivityId(strategyActivityDTO.getId());
                if (CollUtil.isEmpty(effectDTOList)) {
                    continue;
                }
                StringBuilder contentSb = new StringBuilder("购买会员（");
                for (StrategyStageMemberEffectDTO memberEffectDTO : effectDTOList) {
                    MemberBuyStageDTO memberBuyStageDTO = memberBuyStageApi.getById(memberEffectDTO.getMemberId());
                    contentSb.append(memberBuyStageDTO.getMemberName()).append(memberBuyStageDTO.getName()).append("，");
                }
                contentSb.deleteCharAt(contentSb.length() - 1);
                contentSb.append("）赠送").append(lotteryGiftList.get(0).getCount()).append("次");
                lotteryStrategyVO.setContent(contentSb.toString());

                List<Long> memberIdList = effectDTOList.stream().map(StrategyStageMemberEffectDTO::getMemberId).collect(Collectors.toList());
                List<MemberBuyStageDTO> memberBuyStageDTOList = memberBuyStageApi.listByIds(memberIdList);
                Map<Long, List<MemberBuyStageDTO>> memberBuyStageMap = memberBuyStageDTOList.stream().collect(Collectors.groupingBy(MemberBuyStageDTO::getMemberId));
                List<LotteryBuyStageMemberVO> lotteryBuyStageMemberList = new ArrayList<>();
                memberBuyStageMap.forEach((key, value) -> {
                    LotteryBuyStageMemberVO lotteryBuyStageMemberVO = new LotteryBuyStageMemberVO();
                    lotteryBuyStageMemberVO.setMemberId(key);
                    List<Long> buyStageIdList = value.stream().map(MemberBuyStageDTO::getId).collect(Collectors.toList());
                    lotteryBuyStageMemberVO.setBuyStageMemberIdList(buyStageIdList);
                    lotteryBuyStageMemberList.add(lotteryBuyStageMemberVO);
                });
                lotteryStrategyVO.setLotteryBuyStageMemberList(lotteryBuyStageMemberList);
                //                lotteryStrategyVO.setBuyStageMemberIdList(memberIdList);
                QueryMemberBuyRecordRequest memberBuyRecordRequest = new QueryMemberBuyRecordRequest();
                memberBuyRecordRequest.setEid(buyerEid);
                memberBuyRecordRequest.setStartCreateTime(strategyActivityDTO.getBeginTime());
                memberBuyRecordRequest.setEndCreateTime(strategyActivityDTO.getEndTime());
                List<MemberBuyRecordBO> memberBuyRecordBOList = memberBuyRecordApi.getMemberBuyRecordByDate(memberBuyRecordRequest);
                log.info("getStrategyInfoByLotteryId getMemberBuyRecordByDate request:[{}],response:[{}]", memberBuyRecordRequest, memberBuyRecordBOList);
                if (CollUtil.isNotEmpty(memberBuyRecordBOList)) {
                    List<MemberBuyRecordBO> buyRecordBOList = memberBuyRecordBOList.stream().filter(e -> memberIdList.contains(e.getBuyStageId())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(buyRecordBOList)) {
                        lotteryStrategyVO.setIsSuccess(1);
                    }
                }
            }
            lotteryStrategyList.add(lotteryStrategyVO);
        }
        return Result.success(lotteryStrategyList);
    }

    /**
     * 获得指定日期是星期几，1表示周日，2表示周一
     *
     * @param week 日期
     * @return 星期几
     */
    private String getWeek(Integer week) {
        String time = "";
        switch (week) {
            case 1:
                time = "日";
                break;
            case 2:
                time = "一";
                break;
            case 3:
                time = "二";
                break;
            case 4:
                time = "三";
                break;
            case 5:
                time = "四";
                break;
            case 6:
                time = "五";
                break;
            case 7:
                time = "六";
                break;
            default:
        }
        return time;
    }

    private List<StrategyActivityDTO> matchStrategyBuyer(List<StrategyActivityDTO> strategyActivityDOList, Long buyerEid) {
        log.info("getStrategyInfoByLotteryId matchStrategyBuyer start buyerEid:[{}], strategyActivityDOList:[{}]", buyerEid, JSONUtil.toJsonStr(strategyActivityDOList));
        List<StrategyActivityDTO> resultDO = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 1).collect(Collectors.toList());

        List<StrategyActivityDTO> assignBuyerStrategy = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 2).collect(Collectors.toList());
        List<StrategyActivityDTO> rangeBuyerStrategy = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 3).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(assignBuyerStrategy)) {
            List<Long> assignIdList = assignBuyerStrategy.stream().map(StrategyActivityDTO::getId).collect(Collectors.toList());
            List<StrategyBuyerLimitDTO> assignStrategyBuyerLimitDOList = strategyBuyerApi.listByActivityIdListAndEid(assignIdList, buyerEid);
            if (CollUtil.isNotEmpty(assignStrategyBuyerLimitDOList)) {
                List<Long> assignStrategyIdList = assignStrategyBuyerLimitDOList.stream().map(StrategyBuyerLimitDTO::getMarketingStrategyId).collect(Collectors.toList());
                List<StrategyActivityDTO> assignBuyerStrategyList = assignBuyerStrategy.stream().filter(e -> assignStrategyIdList.contains(e.getId())).collect(Collectors.toList());
                resultDO.addAll(assignBuyerStrategyList);
            }
        }

        if (CollUtil.isNotEmpty(rangeBuyerStrategy)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(buyerEid);
            // 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
            if (1 == enterpriseDTO.getType() || 2 == enterpriseDTO.getType()) {
                log.info("matchStrategyBuyer match buyer fail 1-工业 2-商业 buyerEid:[{}]", buyerEid);
                return resultDO;
            }
            for (StrategyActivityDTO strategyActivityDO : rangeBuyerStrategy) {
                StrategyConditionEnterpriseTypeEnum conditionEnterpriseTypeEnum = StrategyConditionEnterpriseTypeEnum.getByType(strategyActivityDO.getConditionEnterpriseType());
                if (conditionEnterpriseTypeEnum == StrategyConditionEnterpriseTypeEnum.ASSIGN) {
                    String conditionEnterpriseTypeValue = strategyActivityDO.getConditionEnterpriseTypeValue();
                    if (!conditionEnterpriseTypeValue.contains(enterpriseDTO.getType() + "")) {
                        log.info("matchStrategyBuyer match buyer assign type fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                }
                StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(strategyActivityDO.getConditionUserType());
                if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                    boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                    if (enterpriseMemberStatus) {
                        log.info("matchStrategyBuyer match buyer common fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                    boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                    if (!enterpriseMemberStatus) {
                        log.info("matchStrategyBuyer match buyer all member fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                    List<Integer> conditionUserMemberTypeList = JSON.parseArray(strategyActivityDO.getConditionUserMemberType(), Integer.class);
                    if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                        log.info("matchStrategyBuyer match buyer member 0 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }

                    List<Long> promoterIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                        // 指定推广方会员
                        List<StrategyPromoterMemberLimitDTO> promoterMemberLimitDOList = strategyPromoterMemberApi.listByActivityIdAndEidList(strategyActivityDO.getId(), null);
                        promoterIdList = promoterMemberLimitDOList.stream().map(StrategyPromoterMemberLimitDTO::getEid).collect(Collectors.toList());
                        if (CollUtil.isEmpty(promoterIdList)) {
                            log.info("matchStrategyBuyer match buyer promoter member 1 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }

                    List<Long> memberIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                        // 指定方案会员
                        List<StrategyMemberLimitDTO> strategyMemberLimitDOList = strategyMemberApi.listMemberByActivityId(strategyActivityDO.getId());
                        memberIdList = strategyMemberLimitDOList.stream().map(StrategyMemberLimitDTO::getMemberId).collect(Collectors.toList());
                        if (CollUtil.isEmpty(memberIdList)) {
                            log.info("matchStrategyBuyer match buyer program member 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }
                    QueryMemberListRecordRequest memberListRecordRequest = new QueryMemberListRecordRequest();
                    memberListRecordRequest.setMemberIdList(memberIdList);
                    memberListRecordRequest.setPromoterIdList(promoterIdList);
                    memberListRecordRequest.setEid(buyerEid);
                    memberListRecordRequest.setCurrentValid(true);
                    List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getBuyRecordListByCond(memberListRecordRequest);
                    if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                        log.info("matchStrategyBuyer match buyer member 3 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                }
                resultDO.add(strategyActivityDO);
            }
        }

        log.info("getStrategyInfoByLotteryId matchStrategyBuyer end buyerEid:[{}] --> [{}]", buyerEid, JSONUtil.toJsonStr(resultDO));
        return resultDO;
    }
}
