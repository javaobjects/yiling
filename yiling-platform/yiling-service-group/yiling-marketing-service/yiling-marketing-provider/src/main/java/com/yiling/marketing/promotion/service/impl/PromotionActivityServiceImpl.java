package com.yiling.marketing.promotion.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.ValidateUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;

import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.common.enums.PromotionErrorCode;
import com.yiling.marketing.common.util.PromotionAreaUtil;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.promotion.dao.PromotionActivityMapper;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.PromotionActivityPageDTO;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.PromotionCheckContextDTO;
import com.yiling.marketing.promotion.dto.PromotionCombinationPackDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.PromotionEnterpriseLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionSecKillSpecialDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityGoodsInfoDTO;

import com.yiling.marketing.promotion.dto.request.ActivityGoodsPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivitySaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartGoods;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionEnterpriseLimitSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionEnterpriseRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftLimitSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftReturnRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsLimitSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeGoodsRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionReduceRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequestItem;
import com.yiling.marketing.promotion.dto.request.SpecialActivityInfoRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionBuyRecordDO;
import com.yiling.marketing.promotion.entity.PromotionCombinationPackageDO;
import com.yiling.marketing.promotion.entity.PromotionEnterpriseLimitDO;
import com.yiling.marketing.promotion.entity.PromotionGoodsGiftLimitDO;
import com.yiling.marketing.promotion.entity.PromotionGoodsLimitDO;
import com.yiling.marketing.promotion.entity.PromotionSecKillSpecialDO;
import com.yiling.marketing.promotion.enums.PromotionPermittedTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionProgressEnum;
import com.yiling.marketing.promotion.enums.PromotionSponsorTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionStatusEnum;
import com.yiling.marketing.promotion.enums.PromotionTerminalTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.marketing.promotion.service.PromotionActivityService;
import com.yiling.marketing.promotion.service.PromotionBuyRecordService;
import com.yiling.marketing.promotion.service.PromotionCombinationPackageService;
import com.yiling.marketing.promotion.service.PromotionEnterpriseLimitService;
import com.yiling.marketing.promotion.service.PromotionGoodsGiftLimitService;
import com.yiling.marketing.promotion.service.PromotionGoodsLimitService;
import com.yiling.marketing.promotion.service.PromotionSecKillSpecialService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.shop.api.ShopApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 促销活动主表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class PromotionActivityServiceImpl extends BaseServiceImpl<PromotionActivityMapper, PromotionActivityDO> implements PromotionActivityService {

    @Autowired
    private PromotionEnterpriseLimitService enterpriseLimitService;

    @Autowired
    private PromotionSecKillSpecialService secKillSpecialService;

    @Autowired
    private PromotionGoodsGiftLimitService goodsGiftLimitService;

    @Autowired
    private PromotionGoodsLimitService goodsLimitService;

    @Autowired
    private GoodsGiftService goodsGiftService;

    @Autowired
    private PromotionBuyRecordService buyRecordService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private MemberApi memberApi;

    @Autowired
    private SpringAsyncConfig springAsyncConfig;

    @Autowired
    private PromotionCombinationPackageService combinationPackageService;

    @Autowired
    private PromotionActivityService promotionActivityService;

    @DubboReference
    protected GoodsApi goodsApi;

    @DubboReference
    protected ShopApi shopApi;

    @DubboReference
    protected EnterprisePurchaseApplyApi enterprisePurchaseApplyApi;


    /**
     * 提交订单校验满赠活动
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<List<PromotionGoodsGiftLimitDTO>> judgePromotion(PromotionJudgeRequest request) {

        log.info("[judgePromotion]提交订单判断是否参与满赠活动，参数转换前：{}", request);
        request.platformConvert();
        log.info("[judgePromotion]提交订单判断是否参与满赠活动，参数转换后:[{}]", request);

        // 1.查询参与活动的商品
        List<PromotionJudgeGoodsRequest> goodsRequestList = request.getGoodsRequestList();
        List<Long> goodsIdList = goodsRequestList.stream().map(PromotionJudgeGoodsRequest::getGoodsId).distinct().collect(Collectors.toList());
        List<PromotionGoodsLimitDO> goodsLimitDOList = this.queryByGoodsIdList(goodsIdList, PromotionTypeEnum.FULL_GIFT, request.getPlatform());
        if (CollUtil.isEmpty(goodsLimitDOList)) {
            log.info("[judgePromotion]未匹配到活动，参数：{}", request);
            return Result.success();
        }

        // 2、获取赠品信息
        List<Long> promotionActivityIdList = goodsLimitDOList.stream().map(PromotionGoodsLimitDO::getPromotionActivityId).distinct().collect(Collectors.toList());
        List<PromotionGoodsGiftLimitDO> goodsGiftList = goodsGiftLimitService.listByActivityIdList(promotionActivityIdList);
        List<PromotionActivityDO> promotionActivityList = this.listByIds(promotionActivityIdList);
        Map<Long, PromotionActivityDO> promotionMap = promotionActivityList.stream().collect(Collectors.toMap(PromotionActivityDO::getId, o -> o, (k1, k2) -> k1));

        // 3、校验活动
        List<PromotionGoodsGiftLimitDO> goodsGiftLimitDOS = orderSubmitCheck(promotionActivityList, goodsLimitDOList, goodsGiftList, goodsRequestList);
        if (CollUtil.isEmpty(goodsGiftLimitDOS)) {
            log.info("[judgePromotion]未匹配到赠品，参数：{}", request);
            return Result.success();
        }

        // 4.赋值赠品名称
        List<PromotionGoodsGiftLimitDTO> limitDTOS = PojoUtils.map(goodsGiftLimitDOS, PromotionGoodsGiftLimitDTO.class);
        List<Long> giftIdList = limitDTOS.stream().map(PromotionGoodsGiftLimitDTO::getGoodsGiftId).distinct().collect(Collectors.toList());
        List<GoodsGiftDO> goodsGiftDOList = goodsGiftService.listByIds(giftIdList);
        Map<Long, GoodsGiftDO> giftDOMap = goodsGiftDOList.stream().collect(Collectors.toMap(GoodsGiftDO::getId, o -> o, (k1, k2) -> k1));
        limitDTOS = limitDTOS.stream().map(item -> {
            item.setGiftName(Optional.ofNullable(giftDOMap.get(item.getGoodsGiftId())).map(GoodsGiftDO::getName).orElse(null));
            item.setSponsorType(Optional.ofNullable(promotionMap.get(item.getPromotionActivityId())).map(PromotionActivityDO::getSponsorType).orElse(null));
            item.setBear(Optional.ofNullable(promotionMap.get(item.getPromotionActivityId())).map(PromotionActivityDO::getBear).orElse(null));
            item.setPlatformPercent(Optional.ofNullable(promotionMap.get(item.getPromotionActivityId())).map(PromotionActivityDO::getPlatformPercent).orElse(null));
            item.setPrice(Optional.ofNullable(giftDOMap.get(item.getGoodsGiftId())).map(GoodsGiftDO::getPrice).orElse(null));
            item.setPromotionName(Optional.ofNullable(promotionMap.get(item.getPromotionActivityId())).map(PromotionActivityDO::getName).orElse(null));
            return item;
        }).collect(Collectors.toList());

        log.info("[judgePromotion]根据商品判断是否满足满赠活动，返参:[{}]", limitDTOS);

        return Result.success(limitDTOS);
    }

    @Override
    public Result<List<Long>> settleJudgePromotion(PromotionSettleJudgeRequest request) {
        log.info("[settleJudgePromotion]结算匹配满赠活动，参数：{}", request);

        List<PromotionSettleJudgeRequestItem> requestItemList = request.getRequestItemList();
        Map<Long, PromotionSettleJudgeRequestItem> requestItemMap = requestItemList.stream().collect(Collectors.toMap(PromotionSettleJudgeRequestItem::getPromotionActivityId, o -> o, (k1, k2) -> k1));

        List<Long> promotionActivityIdListParam = requestItemList.stream().map(PromotionSettleJudgeRequestItem::getPromotionActivityId).collect(Collectors.toList());

        List<PromotionJudgeGoodsRequest> goodsRequestList = request.getGoodsRequestList();

        Map<Long, PromotionJudgeGoodsRequest> goodsRequestMap = goodsRequestList.stream().collect(Collectors.toMap(PromotionJudgeGoodsRequest::getGoodsId, o -> o, (k1, k2) -> k1));

        // 1、根据活动id获取所有活动
        List<PromotionActivityDO> promotionActivityList = listByIdList(promotionActivityIdListParam);
        if (CollUtil.isEmpty(promotionActivityList)) {
            log.info("[settleJudgePromotion]listByIdList未匹配到活动，参数：{}", request);
            return Result.success();
        }
        List<Long> promotionActivityIdList = promotionActivityList.stream().map(PromotionActivityDO::getId).collect(Collectors.toList());

        // 2、获取数据库对应商品信息
        List<PromotionGoodsLimitDO> targetGoodsLimitList = goodsLimitService.queryByActivityIdList(promotionActivityIdList);
        Map<Long, List<PromotionGoodsLimitDO>> goodsMap = targetGoodsLimitList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));

        // 3、获取赠品
        List<PromotionGoodsGiftLimitDO> goodsGiftList = goodsGiftLimitService.listByActivityIdList(promotionActivityIdList);

        Map<Long, PromotionGoodsGiftLimitDO> goodsGiftListMap = goodsGiftList.stream().collect(Collectors.toMap(PromotionGoodsGiftLimitDO::getId, o -> o, (k1, k2) -> k1));


        List<Long> resultList = Lists.newArrayList();
        promotionActivityList.stream().forEach(promotionActivity -> {

            // 获取活动所有有效商品的金额总和
            List<PromotionGoodsLimitDO> goodsLimitList = goodsMap.get(promotionActivity.getId());
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (PromotionGoodsLimitDO item : goodsLimitList) {
                BigDecimal amount = Optional.ofNullable(goodsRequestMap.get(item.getGoodsId())).map(PromotionJudgeGoodsRequest::getAmount).orElse(BigDecimal.ZERO);
                totalAmount = totalAmount.add(amount);
            }

            Long goodsGiftLimitId = requestItemMap.get(promotionActivity.getId()).getGoodsGiftLimitId();

            BigDecimal amount = Optional.ofNullable(goodsGiftListMap.get(goodsGiftLimitId)).map(PromotionGoodsGiftLimitDO::getPromotionAmount).orElse(BigDecimal.ZERO);

            if (totalAmount.compareTo(amount) > -1) {
                resultList.add(promotionActivity.getId());
            }

        });
        log.info("[settleJudgePromotion]结算匹配满赠活动，返参:{}", resultList);
        return Result.success(resultList);
    }

    /**
     * 提交订单-校验满足要求的活动
     *
     * @param promotionActivityList - 活动信息
     * @param goodsLimitDOList - 商品信息
     * @param goodsGiftList - 赠品信息
     * @param goodsRequestList - 商品参数
     * @return 满足要求的赠品
     */
    private List<PromotionGoodsGiftLimitDO> orderSubmitCheck(List<PromotionActivityDO> promotionActivityList, List<PromotionGoodsLimitDO> goodsLimitDOList, List<PromotionGoodsGiftLimitDO> goodsGiftList, List<PromotionJudgeGoodsRequest> goodsRequestList) {

        // 1、对活动根据活动id映射
        Map<Long, PromotionActivityDO> promotionMap = promotionActivityList.stream().collect(Collectors.toMap(PromotionActivityDO::getId, o -> o, (k1, k2) -> k1));

        // 2、对商品根据活动id分组
        Map<Long, List<PromotionGoodsLimitDO>> promotionGoodsMap = goodsLimitDOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));

        // 3、对赠品根据活动id分组
        Map<Long, List<PromotionGoodsGiftLimitDO>> giftMap = goodsGiftList.stream().collect(Collectors.groupingBy(PromotionGoodsGiftLimitDO::getPromotionActivityId));

        // 4、对参数根据goodsId映射
        Map<Long, PromotionJudgeGoodsRequest> goodsRequestMap = goodsRequestList.stream().collect(Collectors.toMap(PromotionJudgeGoodsRequest::getGoodsId, o -> o, (k1, k2) -> k1));

        // 5、满足要求的赠品
        List<PromotionGoodsGiftLimitDO> resultList = Lists.newArrayList();

        for (Map.Entry<Long, List<PromotionGoodsLimitDO>> entry : promotionGoodsMap.entrySet()) {

            List<PromotionGoodsLimitDO> goodsList = entry.getValue();
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (PromotionGoodsLimitDO item : goodsList) {
                BigDecimal amount = Optional.ofNullable(goodsRequestMap.get(item.getGoodsId())).map(PromotionJudgeGoodsRequest::getAmount).orElse(BigDecimal.ZERO);
                totalAmount = totalAmount.add(amount);
            }

            List<PromotionGoodsGiftLimitDO> giftList = giftMap.get(entry.getKey());
            // 按照满赠金额倒序排序
            giftList.sort((o1, o2) -> o2.getPromotionAmount().compareTo(o1.getPromotionAmount()));

            for (PromotionGoodsGiftLimitDO item : giftList) {

                PromotionActivityDO promotion = promotionMap.get(entry.getKey());

                boolean hasStock = false;

                // 1、判断是否商家活动
                if (item.getPromotionStock().equals(0)) {
                    hasStock = true;
                }

                // 2、判断是否平台活动 & 有库存
                if (item.getPromotionStock() > 0 && item.getPromotionStock() > item.getUsedStock()) {
                    hasStock = true;
                }

                // 存在库存->取出->break；否则，continue下一个赠品
                if (hasStock) {
                    // 总金额大于满赠金额
                    if (totalAmount.compareTo(item.getPromotionAmount()) > -1) {
                        resultList.add(item);
                        break;
                    }
                }
            }
        }

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean promotionReduceStock(PromotionReduceRequest request) {
        return goodsGiftLimitService.reducePromotion(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public Long savePromotionActivity(PromotionSaveRequest request) {

        log.info("满赠活动保存，请求数据为:[{}]", request);

        // 1、活动条件校验
        checkPromotion(request);

        // 2、构建基础信息
        buildBasicInfo(request);

        // 3、营销活动，基础信息
        savePromotion(request);

        // 4、保存秒杀特价信息
        saveSecKillSpecial(request);

        // 5、保存企业、商品、赠品
        savePromotionExtend(request);

        // 6、保存组合包信息
        saveCombinationPackage(request);

        log.info("满赠活动保存，返回数据为:[{}]", request.getId());

        return request.getId();
    }

    /**
     * 保存营销活动
     *
     * @param request
     */
    private void savePromotion(PromotionSaveRequest request) {
        PromotionActivityDO promotionDO = PojoUtils.map(request.getActivity(), PromotionActivityDO.class);
        promotionDO.setStatus(PromotionStatusEnum.ENABLED.getType());
        if (ValidateUtils.greaterThanZero(request.getId())) {
            promotionDO.setId(request.getId());
            this.updateById(promotionDO);
        } else {
            this.save(promotionDO);
            // 这里回设活动id
            request.setId(promotionDO.getId());
        }
    }

    /**
     * 保存秒杀 & 特价 & 组合包
     *
     * @param request
     */
    private void saveSecKillSpecial(PromotionSaveRequest request) {
        // 如果是秒杀 & 特价
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(request.getActivity().getType())) {
            PromotionSecKillSpecialDO oldData = secKillSpecialService.queryByPromotionActivityId(request.getId());
            if (Objects.isNull(oldData)) {
                PromotionSecKillSpecialDO secKillSpecialDO = PojoUtils.map(request.getSecKillSpecial(), PromotionSecKillSpecialDO.class);
                secKillSpecialDO.setPromotionActivityId(request.getId());
                log.info("[saveSecKillSpecial]参数：{}", secKillSpecialDO);
                secKillSpecialService.save(secKillSpecialDO);
            } else {
                oldData.setTerminalType(request.getSecKillSpecial().getTerminalType());
                oldData.setPermittedAreaType(request.getSecKillSpecial().getPermittedAreaType());
                oldData.setPermittedAreaDetail(request.getSecKillSpecial().getPermittedAreaDetail());
                oldData.setPermittedEnterpriseType(request.getSecKillSpecial().getPermittedEnterpriseType());
                oldData.setPermittedEnterpriseDetail(request.getSecKillSpecial().getPermittedEnterpriseDetail());
                log.info("[saveSecKillSpecial]参数：{}", oldData);
                secKillSpecialService.updateById(oldData);
            }
        }
    }

    /**
     * 保存 组合包扩展信息
     *
     * @param request
     */
    private void saveCombinationPackage(PromotionSaveRequest request) {
        // 如果是组合包
        if (PromotionTypeEnum.isCombinationPackage(request.getActivity().getType())) {
            PromotionCombinationPackageDO oldData = combinationPackageService.queryByPromotionActivityId(request.getId());
            if (Objects.isNull(oldData)) {
                PromotionCombinationPackageDO secKillSpecialDO = PojoUtils.map(request.getCombinationPackage(), PromotionCombinationPackageDO.class);
                secKillSpecialDO.setPromotionActivityId(request.getId());
                secKillSpecialDO.setReturnRequirement("整包退货，不允许单产品退货。");
                log.info("[saveCombinationPackage]参数：{}", secKillSpecialDO);
                combinationPackageService.save(secKillSpecialDO);
            } else {
                oldData.setPackageName(request.getCombinationPackage().getPackageName());
                oldData.setInitialNum(request.getCombinationPackage().getInitialNum());
                oldData.setTotalNum(request.getCombinationPackage().getTotalNum());
                oldData.setPerDayNum(request.getCombinationPackage().getPerDayNum());
                oldData.setPerPersonNum(request.getCombinationPackage().getPerPersonNum());
                oldData.setReturnRequirement(request.getCombinationPackage().getReturnRequirement());
                oldData.setPackageShortName(request.getCombinationPackage().getPackageShortName());
                oldData.setPic(request.getCombinationPackage().getPic());
                oldData.setDescriptionOfOtherActivity(request.getCombinationPackage().getDescriptionOfOtherActivity());
                oldData.setRemark(request.getCombinationPackage().getRemark());
                log.info("[updateCombinationPackage]参数：{}", oldData);
                combinationPackageService.updateById(oldData);
            }
        }
    }

    /**
     * 构建基础信息
     *
     * @param request
     */
    private void buildBasicInfo(PromotionSaveRequest request) {
        Date opTime = new Date();
        request.setOpTime(opTime);
        request.getActivity().setOpUserId(request.getOpUserId());
        request.getActivity().setOpTime(opTime);
        request.getEnterpriseLimitList().forEach(item -> {
            item.setOpUserId(request.getOpUserId());
            item.setOpTime(opTime);
        });
        request.getGoodsLimitList().forEach(item -> {
            item.setOpUserId(request.getOpUserId());
            item.setOpTime(opTime);
        });
        if (CollUtil.isNotEmpty(request.getGoodsGiftLimit())) {
            request.getGoodsGiftLimit().forEach(item -> {
                item.setOpUserId(request.getOpUserId());
                item.setOpTime(opTime);
            });
        }
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(request.getActivity().getType())) {
            request.getSecKillSpecial().setOpUserId(request.getOpUserId());
            request.getSecKillSpecial().setOpTime(opTime);
        }
        if (PromotionTypeEnum.isCombinationPackage(request.getActivity().getType())) {
            request.getCombinationPackage().setOpUserId(request.getOpUserId());
            request.getCombinationPackage().setOpTime(opTime);
        }
        PromotionActivitySaveRequest activity = request.getActivity();
        Integer bear = activity.getBear();
        if (ObjectUtil.isNull(bear) || bear == 0) {
            throw new BusinessException(CouponActivityErrorCode.BEAR_ERROR);
        }
        BigDecimal platformRatio = activity.getPlatformPercent();
        BigDecimal businessRatio = activity.getMerchantPercent();
        BigDecimal oneHundred = new BigDecimal("100");
        if (ObjectUtil.equal(CouponBearTypeEnum.PLATFORM.getCode(), activity.getBear())) {
            // 1
            platformRatio = oneHundred;
            businessRatio = BigDecimal.ZERO;
        } else if (ObjectUtil.equal(CouponBearTypeEnum.BUSINESS.getCode(), activity.getBear())) {
            // 2
            platformRatio = BigDecimal.ZERO;
            businessRatio = oneHundred;
        } else if (ObjectUtil.equal(CouponBearTypeEnum.TOGETHER.getCode(), activity.getBear())) {
            // 3
            if (ObjectUtil.isNull(platformRatio) || BigDecimal.ZERO.compareTo(platformRatio) == 0 || ObjectUtil.isNull(businessRatio)
                    || BigDecimal.ZERO.compareTo(businessRatio) == 0) {
                throw new BusinessException(CouponActivityErrorCode.BUSINESS_RATIO_ERROR);
            }
        }
        activity.setPlatformPercent(platformRatio);
        activity.setMerchantPercent(businessRatio);
    }

    /**
     * 活动条件校验
     *
     * @param request
     */
    private void checkPromotion(PromotionSaveRequest request) {
        PromotionActivitySaveRequest activity = request.getActivity();
        String name = activity.getName();
        Long eid = 0L;
        if (PromotionTypeEnum.isFullGift(request.getActivity().getType())) {
            eid = request.getEnterpriseLimitList().get(0).getEid();
        }
        //校验活动名称是否重复
        List<PromotionActivityDO> activityDOList = baseMapper.selectByParam(name, eid, activity.getSponsorType(), request.getId());
        if (CollectionUtils.isNotEmpty(activityDOList)) {
            throw new BusinessException(PromotionErrorCode.NAME_IS_REPEAT);
        }
        //校验满赠活动库存数量是否充足
        if (PromotionTypeEnum.FULL_GIFT.getType().equals(request.getActivity().getType()) && CollUtil.isNotEmpty(request.getGoodsGiftLimit())) {
            List<PromotionGoodsGiftLimitSaveRequest> goodsGiftLimit = request.getGoodsGiftLimit();
            goodsGiftLimit.forEach(item -> {
                GoodsGiftDO goodsGiftDO = goodsGiftService.getById(item.getGoodsGiftId());
                if (Objects.nonNull(goodsGiftDO) && item.getPromotionStock() > goodsGiftDO.getAvailableQuantity()) {
                    throw new BusinessException(PromotionErrorCode.GIFT_NOT_AVAILABLE);
                }
            });
        }

        // 校验赠品总金额不允许大于预算金额
        checkPromotionExtend(request);

        List<PromotionGoodsLimitSaveRequest> goodsLimitList = request.getGoodsLimitList();

        List<Long> goodsIdList = goodsLimitList.stream().map(PromotionGoodsLimitSaveRequest::getGoodsId).collect(Collectors.toList());
        List<PromotionGoodsLimitDO> goodsList = queryNotRepeatByGoodsIdList(goodsIdList, activity.getSponsorType(), activity.getType(), request.getId());
        if (CollectionUtils.isNotEmpty(goodsList)) {
            throw new BusinessException(PromotionErrorCode.GOODS_HAVE_ACTIVITY);
        }
    }

    /**
     * 新增活动的校验
     *
     * @param request
     */
    private void checkPromotionExtend(PromotionSaveRequest request) {
        if (!PromotionTypeEnum.FULL_GIFT.getType().equals(request.getActivity().getType())) {
            return;
        }
        if (StringUtils.isNotEmpty(request.getGoodsGiftName())) {
            return;
        }
        List<PromotionGoodsGiftLimitSaveRequest> goodsGiftLimit = request.getGoodsGiftLimit();
        BigDecimal total = BigDecimal.ZERO;

        for (PromotionGoodsGiftLimitSaveRequest item : goodsGiftLimit) {
            GoodsGiftDO goodsGiftDO = goodsGiftService.getById(item.getGoodsGiftId());
            BigDecimal amount = goodsGiftDO.getPrice().multiply(new BigDecimal(item.getPromotionStock()));
            total = total.add(amount);
        }
        PromotionActivitySaveRequest activity = request.getActivity();
        // 赠品总金额不能大于活动预算金额
        if (total.compareTo(activity.getBudgetAmount()) > 0) {
            log.info("满赠活动，赠品总金额为:[{}],预算金额为:[{}]", total, activity.getBudgetAmount());
            throw new BusinessException(PromotionErrorCode.GIFT_AMOUNT_PASS);
        }
    }

    private void savePromotionExtend(PromotionSaveRequest request) {

        List<PromotionEnterpriseLimitSaveRequest> enterpriseLimitList = request.getEnterpriseLimitList();
        List<PromotionEnterpriseLimitDO> enterpriseLimitDOS = PojoUtils.map(enterpriseLimitList, PromotionEnterpriseLimitDO.class);
        List<PromotionGoodsLimitSaveRequest> goodsLimitList = request.getGoodsLimitList();
        List<PromotionGoodsLimitDO> goodsLimitDOS = PojoUtils.map(goodsLimitList, PromotionGoodsLimitDO.class);

        // 2.促销企业保存
        enterpriseLimitService.editEnterprise(enterpriseLimitDOS, request.getId(), request.getOpUserId(), request.getOpTime());

        // 3.促销商品保存
        goodsLimitService.editGoods(goodsLimitDOS, request.getId(), request.getOpUserId(), request.getOpTime());

        // 4.满赠 -> 赠品
        if (PromotionTypeEnum.FULL_GIFT.getType().equals(request.getActivity().getType())) {
            if (StringUtils.isBlank(request.getGoodsGiftName())) {
                goodsGiftLimitService.editGoodsGift(request);
            } else {
                // 商家端填赠品名称
                List<PromotionGoodsGiftLimitDO> goodsGiftLimitDOList = goodsGiftLimitService.queryByActivityId(request.getId());
                BigDecimal promotionAmount = request.getGoodsGiftLimit().get(0).getPromotionAmount();
                if (CollUtil.isEmpty(goodsGiftLimitDOList)) {
                    // 商家端填赠品名称
                    GoodsGiftDO goodsGiftDO = GoodsGiftDO.builder().eid(request.getEnterpriseLimitList().get(0).getEid()).ename(request.getEnterpriseLimitList().get(0).getEname()).sponsorType(PromotionSponsorTypeEnum.MERCHANT.getType()).name(request.getGoodsGiftName()).createUser(request.getOpUserId()).updateUser(request.getOpUserId()).createTime(request.getOpTime()).updateTime(request.getOpTime()).build();
                    goodsGiftService.save(goodsGiftDO);

                    PromotionGoodsGiftLimitDO entity = new PromotionGoodsGiftLimitDO();
                    entity.setPromotionActivityId(request.getId()).setPromotionAmount(promotionAmount).setGoodsGiftId(goodsGiftDO.getId()).setCreateUser(request.getOpUserId()).setCreateTime(request.getOpTime()).setUpdateUser(request.getOpUserId()).setUpdateTime(request.getOpTime());
                    goodsGiftLimitService.save(entity);
                } else {
                    // 商家端修改赠品
                    GoodsGiftDO goodsGiftDO = new GoodsGiftDO();
                    goodsGiftDO.setId(goodsGiftLimitDOList.get(0).getGoodsGiftId());
                    goodsGiftDO.setName(request.getGoodsGiftName()).setCreateUser(request.getOpUserId()).setCreateTime(request.getOpTime()).setUpdateUser(request.getOpUserId()).setUpdateTime(request.getOpTime());
                    goodsGiftService.updateById(goodsGiftDO);

                    // 修改赠品信息
                    PromotionGoodsGiftLimitDO one = goodsGiftLimitDOList.get(0).setPromotionAmount(promotionAmount).setUpdateUser(request.getOpUserId()).setUpdateTime(request.getOpTime());

                    goodsGiftLimitService.updateById(one);
                }
            }
        }
    }

    @Override
    public Page<PromotionActivityPageDTO> pageList(PromotionActivityPageRequest request) {
        Page<PromotionActivityDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        Page<PromotionActivityPageDTO> promotionActivityPageDTOPage = this.baseMapper.pageList(objectPage, request);
        List<PromotionActivityPageDTO> pageRecords = promotionActivityPageDTOPage.getRecords();
        // 获取所有的满赠的活动的id，设置活动参与数量
        List<Long> activityIdList = pageRecords.stream().filter(item -> PromotionTypeEnum.FULL_GIFT.getType().equals(item.getType())).map(PromotionActivityPageDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(activityIdList)) {
            List<PromotionGoodsGiftLimitDO> promotionGoodsGiftLimitDOS = goodsGiftLimitService.listByActivityId(activityIdList);
            Map<Long, PromotionGoodsGiftLimitDO> promotionGoodsGiftMap = promotionGoodsGiftLimitDOS.stream().collect(Collectors.toMap(PromotionGoodsGiftLimitDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));
            pageRecords.stream().forEach(item -> item.setActivityQuantity(Optional.ofNullable(promotionGoodsGiftMap.get(item.getId())).map(PromotionGoodsGiftLimitDO::getUsedStock).orElse(null)));
        }
        for (PromotionActivityPageDTO promotionActivityPageDTO : pageRecords) {
            // progress 活动进度 1-待开始 2-进行中 3-已结束
            // status 活动状态（1-启用；2-停用；）
            if (PromotionStatusEnum.ENABLED.getType().equals(promotionActivityPageDTO.getStatus())) {
                Date beginTime = promotionActivityPageDTO.getBeginTime();
                Date endTime = promotionActivityPageDTO.getEndTime();
                if (beginTime.getTime() > System.currentTimeMillis()) {
                    promotionActivityPageDTO.setProgress(PromotionProgressEnum.READY.getType());
                } else if (endTime.getTime() < System.currentTimeMillis()) {
                    promotionActivityPageDTO.setProgress(PromotionProgressEnum.END.getType());
                } else {
                    promotionActivityPageDTO.setProgress(PromotionProgressEnum.PROCESSING.getType());
                }
            } else {
                promotionActivityPageDTO.setProgress(PromotionProgressEnum.END.getType());
            }
        }
        return promotionActivityPageDTOPage;
    }

    @Override
    public boolean editStatusById(PromotionActivityStatusRequest request) {
        PromotionActivityDO promotionActivityDO = PojoUtils.map(request, PromotionActivityDO.class);
        promotionActivityDO.setUpdateUser(request.getOpUserId());
        promotionActivityDO.setUpdateTime(request.getOpTime());
        return this.updateById(promotionActivityDO);
    }

    @Override
    public PromotionActivityDTO copy(PromotionActivityStatusRequest request) {
        PromotionActivityDO promotion = this.getById(request.getId());
        if (null == promotion) {
            return null;
        }
        PromotionActivityDO activityDO = new PromotionActivityDO();
        activityDO.setName(promotion.getName()).setSponsorType(promotion.getSponsorType()).setBudgetAmount(promotion.getBudgetAmount()).setBear(promotion.getBear()).setType(promotion.getType()).setBeginTime(promotion.getBeginTime()).setEndTime(promotion.getEndTime()).setPlatformSelected(promotion.getPlatformSelected()).setStatus(request.getStatus()).setCreateUser(request.getOpUserId()).setCreateTime(request.getOpTime()).setUpdateUser(request.getOpUserId()).setUpdateTime(request.getOpTime());
        this.save(activityDO);
        enterpriseLimitService.copy(request, activityDO.getId());

        if (PromotionTypeEnum.isFullGift(promotion.getType())) {
            goodsGiftLimitService.copy(request, activityDO.getId());
        }

        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(promotion.getType())) {
            secKillSpecialService.copy(request, activityDO.getId());
        }

        if (PromotionTypeEnum.isCombinationPackage(promotion.getType())) {
            combinationPackageService.copy(request, activityDO.getId());
        }

        return PojoUtils.map(activityDO, PromotionActivityDTO.class);
    }

    @Override
    public List<PromotionActivityDO> listByIdList(List<Long> idList, Integer platform) {
        QueryWrapper<PromotionActivityDO> wrapper = new QueryWrapper<>();
        Date now = new Date();
        wrapper.lambda().in(PromotionActivityDO::getId, idList);
        wrapper.lambda().eq(PromotionActivityDO::getStatus, PromotionStatusEnum.ENABLED.getType());
        wrapper.lambda().le(PromotionActivityDO::getBeginTime, now);
        wrapper.lambda().ge(PromotionActivityDO::getEndTime, now);

        // 销售渠道
        if (ValidateUtils.greaterThanZero(platform)) {
            wrapper.lambda().like(PromotionActivityDO::getPlatformSelected, platform);
        }

        return this.list(wrapper);
    }

    @Override
    public List<PromotionActivityDO> listByIdList(List<Long> idList) {
        QueryWrapper<PromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionActivityDO::getId, idList);
        return this.list(wrapper);
    }

    @Override
    public List<PromotionGoodsGiftLimitDTO> queryByGiftIdList(List<Long> goodsGiftIdList) {
        List<PromotionGoodsGiftLimitDTO> resultList = new ArrayList<>();
        List<PromotionGoodsGiftLimitDTO> goodsGiftList = goodsGiftLimitService.queryByGiftIdList(goodsGiftIdList);
        Map<Long, List<PromotionGoodsGiftLimitDTO>> promotionMap = goodsGiftList.stream().collect(Collectors.groupingBy(PromotionGoodsGiftLimitDTO::getPromotionActivityId));
        List<Long> longList = goodsGiftList.stream().map(PromotionGoodsGiftLimitDTO::getPromotionActivityId).collect(Collectors.toList());
        if (longList.size() < 1) {
            return resultList;
        }
        List<PromotionActivityDO> promotionActivityDOList = this.listByIdList(longList, null);
        for (PromotionActivityDO activityDO : promotionActivityDOList) {
            List<PromotionGoodsGiftLimitDTO> goodsGiftLimitDTOS = promotionMap.get(activityDO.getId());
            resultList.addAll(goodsGiftLimitDTOS);
        }
        return resultList;
    }

    @Override
    public List<PromotionGoodsLimitDO> queryByGoodsIdList(List<Long> goodsIdList, PromotionTypeEnum typeEnum, Integer platform) {
        List<PromotionGoodsLimitDO> resultList = new ArrayList<>();
        List<PromotionGoodsLimitDO> goodsLimitDOS = goodsLimitService.queryByGoodsIdList(goodsIdList);
        Map<Long, List<PromotionGoodsLimitDO>> longListMap = goodsLimitDOS.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));
        List<Long> promotionIdList = goodsLimitDOS.stream().map(PromotionGoodsLimitDO::getPromotionActivityId).collect(Collectors.toList());
        if (CollUtil.isEmpty(promotionIdList)) {
            return resultList;
        }
        List<PromotionActivityDO> promotionActivityDOList = this.listByIdList(promotionIdList, platform);
        for (PromotionActivityDO activityDO : promotionActivityDOList) {
            if (typeEnum.getType().equals(activityDO.getType())) {
                resultList.addAll(longListMap.get(activityDO.getId()));
            }
        }
        return resultList;
    }

    /**
     * 判断商品是否被重复添加
     * 满赠、特价、秒杀彼此之间互斥；
     * 满赠可以添加平台和商家两个
     *
     * @param goodsIdList - 商品列表
     * @param sponsorType - 活动分类（1-平台活动；2-商家活动）
     * @param type - 活动类型（1-满赠；2-特价；3-秒杀）
     * @return
     */
    @Override
    public List<PromotionGoodsLimitDO> queryNotRepeatByGoodsIdList(List<Long> goodsIdList, Integer sponsorType, Integer type, Long id) {
        List<PromotionGoodsLimitDO> resultList = new ArrayList<>();
        List<PromotionGoodsLimitDO> promotionGoodsLimitDOS = goodsLimitService.queryByGoodsIdList(goodsIdList);
        Map<Long, List<PromotionGoodsLimitDO>> longListMap = promotionGoodsLimitDOS.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));
        List<Long> longList = promotionGoodsLimitDOS.stream().map(PromotionGoodsLimitDO::getPromotionActivityId).collect(Collectors.toList());
        if (longList.size() < 1) {
            return resultList;
        }
        QueryWrapper<PromotionActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionActivityDO::getId, longList);
        wrapper.lambda().eq(PromotionActivityDO::getStatus, PromotionStatusEnum.ENABLED.getType());
        wrapper.lambda().ge(PromotionActivityDO::getEndTime, new Date());
        // 商品参加组合包活动，还可以参加别的任何活动-组合包，特价，秒杀
        wrapper.lambda().ne(PromotionActivityDO::getType, 4);
        if (Objects.nonNull(id) && !Objects.equals(id, Long.valueOf(0))) {
            wrapper.lambda().ne(PromotionActivityDO::getId, id);
        }

        List<PromotionActivityDO> activityDOS = this.list(wrapper);
        log.info("[queryNotRepeatByGoodsIdList] 根据id查询活动信息结果:{}", activityDOS);
        for (PromotionActivityDO activityDO : activityDOS) {

            // 1、判断添加活动类型是满赠
            if (PromotionTypeEnum.isFullGift(type)) {
                if (PromotionTypeEnum.isFullGift(activityDO.getType())) {
                    if (activityDO.getSponsorType().equals(sponsorType)) {
                        List<PromotionGoodsLimitDO> goodsLimitDOS = longListMap.get(activityDO.getId());
                        resultList.addAll(goodsLimitDOS);
                    }
                } else {
                    List<PromotionGoodsLimitDO> goodsLimitDOS = longListMap.get(activityDO.getId());
                    resultList.addAll(goodsLimitDOS);
                }
            }

            // 2、判断添加活动类型是特价&秒杀
            if (PromotionTypeEnum.isSecKillOrSpecialPrice(type)) {
                List<PromotionGoodsLimitDO> goodsLimitDOS = longListMap.get(activityDO.getId());
                resultList.addAll(goodsLimitDOS);
            }

        }
        log.info("[queryNotRepeatByGoodsIdList]校验商品是否被重复添加返回结果：{}", resultList);
        return resultList;
    }

    @Override
    public List<PromotionGoodsLimitDTO> queryGoodsPromotionInfo(PromotionAppRequest request) {

        log.info("[queryGoodsPromotionInfo]查询商品参与的促销活动，参数转换前：{}", request);
        request.platformConvert();
        log.info("[queryGoodsPromotionInfo]查询商品参与的促销活动，参数转换后：{}", request);

        List<PromotionGoodsLimitDTO> promotionGoodsList = this.baseMapper.queryPromotionGoodsInfo(request);

        EnterpriseDTO enterprise = enterpriseApi.getById(request.getBuyerEid());

        CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(request.getBuyerEid());

        List<PromotionGoodsLimitDTO> result = Lists.newArrayList();

        // 构建校验上下文对象
        PromotionCheckContextDTO contextDTO = PromotionCheckContextDTO.builder().build();
        contextDTO.setEnterprise(enterprise);
        contextDTO.setMember(member);
        contextDTO.setTargetGoodsList(promotionGoodsList);
        contextDTO.setBuyerEid(request.getBuyerEid());

        result.addAll(this.buildFullGiftInfo(contextDTO));

        result.addAll(this.buildSecKillSpecial(contextDTO, false));

        result.addAll(this.buildCombinationPackage(contextDTO));

        //        // 构建满赠异步任务
        //        CompletableFuture<List<PromotionGoodsLimitDTO>> fullGiftFuture = CompletableFuture
        //            .supplyAsync(() -> this.buildFullGiftInfo(contextDTO), springAsyncConfig.getAsyncExecutor())
        //            .whenComplete((goodsLimitDTOList, throwable) -> result.addAll(goodsLimitDTOList));
        //
        //        // 构建秒杀特价异步任务
        //        CompletableFuture<List<PromotionGoodsLimitDTO>> secKillSpecialFuture = CompletableFuture
        //            .supplyAsync(() -> this.buildSecKillSpecial(contextDTO), springAsyncConfig.getAsyncExecutor())
        //            .whenComplete((goodsLimitDTOList, throwable) -> result.addAll(goodsLimitDTOList));

        //        CompletableFuture.allOf(fullGiftFuture, secKillSpecialFuture).join();

        log.info("[queryAppGoodsPromotion]查询商品参与的促销活动，返参：{}", result);

        return result;
    }

    @Override
    public List<PromotionDTO> queryPromotionInfoByActivityAndBuyerId(PromotionActivityRequest request) {
        LambdaQueryWrapper<PromotionActivityDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PromotionActivityDO::getId, request.getGoodsPromotionActivityIdList());
        List<PromotionActivityDO> list = this.list(queryWrapper);
        EnterpriseDTO enterprise = enterpriseApi.getById(request.getBuyerEid());
        CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(request.getBuyerEid());

        // 通过活动id获取秒杀信息
        List<PromotionSecKillSpecialDO> secKillSpecialList = secKillSpecialService.queryByPromotionActivityIdList(request.getGoodsPromotionActivityIdList());
        Map<Long, PromotionSecKillSpecialDO> secKillSpecialMap = secKillSpecialList.stream().collect(Collectors.toMap(PromotionSecKillSpecialDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));

        // 通过活动id获取产品信息包含goodsID和skuid
        List<PromotionGoodsLimitDO> promotionGoodsLimitList = goodsLimitService.queryByActivityIdList(request.getGoodsPromotionActivityIdList());
        Map<Long, List<PromotionGoodsLimitDO>> goodsLimitMap = promotionGoodsLimitList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));
        //通过skuid获取库存数量
        List<Long> goodsSukIds = promotionGoodsLimitList.stream().map(PromotionGoodsLimitDO::getGoodsSkuId).collect(Collectors.toList());
        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
        Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
            skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        }
        // 通过活动id获取企业信息
        LambdaQueryWrapper<PromotionEnterpriseLimitDO> enterpriseQueryWrapper = new LambdaQueryWrapper<>();
        enterpriseQueryWrapper.in(PromotionEnterpriseLimitDO::getPromotionActivityId, request.getGoodsPromotionActivityIdList());
        List<PromotionEnterpriseLimitDO> enterpriseLimitList = enterpriseLimitService.list(enterpriseQueryWrapper);
        Map<Long, PromotionEnterpriseLimitDO> enterpriseLimitMap = enterpriseLimitList.stream().collect(Collectors.toMap(PromotionEnterpriseLimitDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));

        // 通过活动id获取组合包信息
        LambdaQueryWrapper<PromotionCombinationPackageDO> combinationWrapper = new LambdaQueryWrapper<>();
        combinationWrapper.in(PromotionCombinationPackageDO::getPromotionActivityId, request.getGoodsPromotionActivityIdList());
        List<PromotionCombinationPackageDO> combinationPackageList = combinationPackageService.list(combinationWrapper);
        Map<Long, PromotionCombinationPackageDO> combinationPackageMap = combinationPackageList.stream().collect(Collectors.toMap(PromotionCombinationPackageDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));
        List<PromotionDTO> promotionList = new ArrayList<>();
        Map<Long, GoodsSkuDTO> finalSkuGoodsMap = skuGoodsMap;
        list.forEach(item -> {
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setAvailable(0);
            promotionDTO.setInStock(1);
            PromotionSecKillSpecialDO secKillSpecialDO = secKillSpecialMap.get(item.getId());
            PromotionEnterpriseLimitDO enterpriseLimitDO = enterpriseLimitMap.get(item.getId());
            PromotionCombinationPackageDO combinationPackageDO = combinationPackageMap.get(item.getId());
            List<PromotionGoodsLimitDO> promotionGoodsLimitDO = goodsLimitMap.get(item.getId());

            // 营销活动企业信息
            PromotionEnterpriseLimitDTO enterpriseLimitDTO = PojoUtils.map(enterpriseLimitDO, PromotionEnterpriseLimitDTO.class);
            promotionDTO.setEnterpriseLimitDTOList(Arrays.asList(enterpriseLimitDTO));
            // 营销活动产品信息,并且判断产品库存是否充足
            List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = promotionGoodsLimitDO.stream().map(goodsLimitDO -> {
                return PojoUtils.map(goodsLimitDO, PromotionGoodsLimitDTO.class);
            }).collect(Collectors.toList());
            promotionDTO.setGoodsLimitDTOList(promotionGoodsLimitDTOS);
            boolean zeroQty = promotionGoodsLimitDTOS.stream().anyMatch(e -> {
                GoodsSkuDTO goodsSkuDTO = finalSkuGoodsMap.get(e.getGoodsSkuId());
                return (goodsSkuDTO == null) || goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty() == 0;
            });
            if (zeroQty) {
                promotionDTO.setInStock(0);
            }
            // 营销活动组合包产品信息
            PromotionCombinationPackDTO promotionCombinationPackDTO = PojoUtils.map(combinationPackageDO, PromotionCombinationPackDTO.class);
            promotionDTO.setPromotionCombinationPackDTO(promotionCombinationPackDTO);
            // 秒杀信息
            PromotionSecKillSpecialDTO secKillSpecialDTO = PojoUtils.map(secKillSpecialDO, PromotionSecKillSpecialDTO.class);
            promotionDTO.setPromotionSecKillSpecialDTO(secKillSpecialDTO);
            // 营销活动主表信息
            PromotionActivityDTO promotionActivityDTO = PojoUtils.map(item, PromotionActivityDTO.class);
            promotionDTO.setPromotionActivityDTO(promotionActivityDTO);

            PromotionCheckContextDTO contextDTO = PromotionCheckContextDTO.builder().build();
            contextDTO.setSecKillSpecialDTO(secKillSpecialDTO);
            contextDTO.setEnterprise(enterprise);
            contextDTO.setMember(member);
            boolean isInProgress = (item.getEndTime().after(new Date())) && (item.getBeginTime().before(new Date()));
            if (terminalTypeCheck(contextDTO) && permittedAreaTypeCheck(contextDTO) && permittedEnterpriseTypeCheck(contextDTO) && isInProgress && item.getStatus() == 1) {
                promotionDTO.setAvailable(1);
            }
            promotionList.add(promotionDTO);
        });
        return promotionList;
    }

    @Override
    public List<PromotionGoodsLimitDTO> pagePromotionByShopId(ActivityGoodsPageRequest request) {
        PromotionAppRequest promotionAppRequest = new PromotionAppRequest();
        promotionAppRequest.setEIdList(request.getShopEid() == null ? null : ListUtil.toList(request.getShopEid()));
        promotionAppRequest.setTypeList(request.getType() == null ? null : Arrays.asList(Integer.parseInt(request.getType())));
        promotionAppRequest.setPromotionActivityId(request.getId());
        // 通过shopId获取营销活动主表和组合包表信息
        List<PromotionGoodsLimitDTO> promotionGoodsList = this.baseMapper.pagePromotionGoodsInfo(promotionAppRequest);
        if (CollectionUtils.isEmpty(promotionGoodsList)) {
            return new ArrayList<PromotionGoodsLimitDTO>();
        }
        List<Long> promotionIdList = promotionGoodsList.stream().map(PromotionGoodsLimitDTO::getPromotionActivityId).collect(Collectors.toList());
        // 通过活动id获取产品信息
        List<PromotionGoodsLimitDO> promotionGoodsLimitList = goodsLimitService.queryByActivityIdList(promotionIdList);

        QueryWrapper<PromotionSecKillSpecialDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionSecKillSpecialDO::getPromotionActivityId, promotionIdList);
        List<PromotionSecKillSpecialDO> secKillSpecialDOS = secKillSpecialService.list(wrapper);

        Map<Long, List<PromotionGoodsLimitDO>> goodsLimitMap = promotionGoodsLimitList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));
        Map<Long, PromotionSecKillSpecialDO> SecKillSpecialMap = secKillSpecialDOS.stream().collect(Collectors.toMap(PromotionSecKillSpecialDO::getPromotionActivityId,Function.identity()));
        promotionGoodsList.stream().forEach(item -> {
            Long promotionActivityId = item.getPromotionActivityId();
            List<PromotionGoodsLimitDO> promotionGoodsLimitDO = goodsLimitMap.get(promotionActivityId);
            List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = promotionGoodsLimitDO.stream().map(goodsLimitDO -> {
               return PojoUtils.map(goodsLimitDO, PromotionGoodsLimitDTO.class);
            }).collect(Collectors.toList());
            item.setGoodsLimitDTOS(promotionGoodsLimitDTOS);
            PromotionSecKillSpecialDO SecKillSpecialDo = SecKillSpecialMap.get(promotionActivityId);
            item.setSecKillSpecialDTO(PojoUtils.map(SecKillSpecialDo, PromotionSecKillSpecialDTO.class));
        });
        return promotionGoodsList;
    }

    @Override
    public Boolean promotionIsAvailable(PromotionCheckContextDTO contextDTO) {
        PromotionSecKillSpecialDO secKillSpecialDO = secKillSpecialService.queryByPromotionActivityId(contextDTO.getPromotionActivityId());
        PromotionSecKillSpecialDTO secKillSpecialDTO = PojoUtils.map(secKillSpecialDO, PromotionSecKillSpecialDTO.class);
        contextDTO.setSecKillSpecialDTO(secKillSpecialDTO);
        if (terminalTypeCheck(contextDTO) && permittedAreaTypeCheck(contextDTO) && permittedEnterpriseTypeCheck(contextDTO)) {
            return true;
        }
        return false;
    }
    @Override
    public Boolean promotionIsAvailableByContext(PromotionCheckContextDTO contextDTO) {
        if (terminalTypeCheck(contextDTO) && permittedAreaTypeCheck(contextDTO) && permittedEnterpriseTypeCheck(contextDTO)) {
            return true;
        }
        return false;
    }

    @Override
    public PromotionGoodsLimitDTO queryPromotionInfoByActivity(ActivityGoodsPageRequest request) {
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = pagePromotionByShopId(request);
        return promotionGoodsLimitDTOS.get(0);
    }

    @Override
    public List<SpecialAvtivityGoodsInfoDTO> getGoodsInfoByActivityId(SpecialActivityInfoRequest request) {
        QueryWrapper<PromotionGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionGoodsLimitDO::getPromotionActivityId, request.getPromotionActivityId());
        wrapper.lambda().eq(PromotionGoodsLimitDO::getDelFlag, 0);
        if (StringUtils.isNotEmpty(request.getGoodsName())) {
            wrapper.lambda().like(PromotionGoodsLimitDO::getGoodsName, request.getGoodsName());
        }
        Page<PromotionGoodsLimitDO> page = new Page<>(request.getCurrent(), request.getSize());

        Page<PromotionGoodsLimitDO> promotionGoodsLimitDOS = goodsLimitService.page(page, wrapper);
        if (CollectionUtils.isEmpty(promotionGoodsLimitDOS.getRecords())) {
            return new ArrayList<SpecialAvtivityGoodsInfoDTO>();
        }
        PromotionActivityDO promotionActivityDO = promotionActivityService.getById(request.getPromotionActivityId());
        if (promotionActivityDO == null || promotionActivityDO.getStatus() == 2) {
            log.info("获取营销活动信息失败，营销活动id:{}", request.getPromotionActivityId());
            throw new BusinessException(PromotionErrorCode.GET_ACTIVITY_INFO_ERROR);
        }
        //是否建材
        List<Long> eids = new ArrayList<>();
        eids.add(request.getEid());
        Map<Long, Integer> longBooleanMap = enterprisePurchaseApplyApi.getPurchaseApplyStatus(eids, request.getCurrentEid());
        List<SpecialAvtivityGoodsInfoDTO> specialAvtivityInfoDTOS = PojoUtils.map(promotionGoodsLimitDOS.getRecords(), SpecialAvtivityGoodsInfoDTO.class);
        Date beginTime = promotionActivityDO.getBeginTime();
        Date endTime = promotionActivityDO.getEndTime();
        Boolean isProcess = beginTime.before(new Date()) && endTime.after(new Date());

        if (promotionActivityDO.getType() == 4) {
            // 如果是组合包
            SpecialAvtivityGoodsInfoDTO combinationInfo = new SpecialAvtivityGoodsInfoDTO();
            PromotionCombinationPackageDO combinationPackageDO = combinationPackageService.queryByPromotionActivityId(request.getPromotionActivityId());
            combinationInfo.setInitialNum(combinationPackageDO.getInitialNum());
            combinationInfo.setPic(combinationPackageDO.getPic());
            combinationInfo.setPackageName(combinationPackageDO.getPackageName());

            combinationInfo.setIsStarted(isProcess);
            combinationInfo.setIsContract(longBooleanMap.get(request.getEid())==3);
            combinationInfo.setPackageShortName(combinationPackageDO.getPackageShortName());
            List<Long> skuIds = specialAvtivityInfoDTOS.stream().map(SpecialAvtivityGoodsInfoDTO::getGoodsSkuId).collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(skuIds);
            Map<Long, GoodsSkuDTO> goodsSkuDTOMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
            List<BigDecimal> canBuyNums = new ArrayList<>();
            List<BigDecimal> combinationPackagePrice = new ArrayList<>();
            //计算组合包划线价
            specialAvtivityInfoDTOS.forEach(goodsLimit -> {
                Integer allowBuyCount = goodsLimit.getAllowBuyCount();
                GoodsSkuDTO goodsSkuDTO = goodsSkuDTOMap.get(goodsLimit.getGoodsSkuId());
                if ((goodsSkuDTO != null) && (goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty()) != 0) {
                    long AvailableNum = goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty();
                    BigDecimal canBuyNum = NumberUtil.round(NumberUtil.div(AvailableNum, allowBuyCount.longValue()), 0);
                    canBuyNums.add(canBuyNum);
                } else {
                    canBuyNums.add(new BigDecimal(0));
                }
                BigDecimal promotionGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsLimit.getPromotionPrice(), goodsLimit.getAllowBuyCount()), 2);
                combinationPackagePrice.add(promotionGoodsPrice);
            });
            BigDecimal bigDecimal = canBuyNums.stream().min(Comparator.comparing(x -> x)).orElse(new BigDecimal(0));
            combinationInfo.setMaxBuyCount(bigDecimal.intValue());

            combinationInfo.setPromotionPrice(combinationPackagePrice.stream().reduce(BigDecimal::add).get());
            combinationInfo.setGoodsInfoDTOS(specialAvtivityInfoDTOS);
            List<SpecialAvtivityGoodsInfoDTO> result = new ArrayList<>();
            result.add(combinationInfo);
            return result;
        }

        List<Long> goodsIds = promotionGoodsLimitDOS.getRecords().stream().map(PromotionGoodsLimitDO::getGoodsId).collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> goodsDTOMap = goodsDTOS.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));

        // 图片，非组合包通过goods表查询,只返回卖家id相同的商品
        specialAvtivityInfoDTOS = specialAvtivityInfoDTOS.stream().filter(e -> e.getEid().equals(request.getEid())).collect(Collectors.toList());
        List<Long> goodsSkuDTOList = specialAvtivityInfoDTOS.stream().map(SpecialAvtivityGoodsInfoDTO::getGoodsId).collect(Collectors.toList());
        List<GoodsSkuDTO> goodsSkuByGoodsIds = goodsApi.getGoodsSkuByGoodsIds(goodsSkuDTOList);
        goodsSkuByGoodsIds = goodsSkuByGoodsIds.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
        Map<Long, List<GoodsSkuDTO>> goodsSkuDTOMap = goodsSkuByGoodsIds.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
        specialAvtivityInfoDTOS.forEach(item -> {
            // 是否建采
            item.setIsContract(longBooleanMap.get(item.getEid())==3);
            GoodsDTO goodsDTO = goodsDTOMap.get(item.getGoodsId());
            item.setGoodsName(goodsDTO.getName());
            BigDecimal zero = new BigDecimal("0.00000000");
            if (item.getPromotionPrice().compareTo(zero) == 0) {
                item.setPromotionPrice(item.getPrice());
            }
            // 规格和生产厂家
            item.setSpecifications(goodsDTO.getSpecifications());
            item.setManufacturer(goodsDTO.getManufacturer());
            // 活动是否开始
            item.setIsStarted(isProcess);
            List<GoodsSkuDTO> goodsSkuDTOS = goodsSkuDTOMap.get(item.getGoodsId());
            item.setGoodsSkuList(goodsSkuDTOS);
        });
        return specialAvtivityInfoDTOS;
    }

    @Override
    public SpecialAvtivityAppointmentItemDTO myAppointmentCount(Long currentUserId) {
        return this.baseMapper.myAppointmentCount(currentUserId);
    }

    /**
     * 构建秒杀&特价
     *
     * @param contextDTO
     */
    public List<PromotionGoodsLimitDTO> buildSecKillSpecial(PromotionCheckContextDTO contextDTO, Boolean isCombination) {
        log.info("开始构建秒杀特价信息开始 时间戳{}"+System.currentTimeMillis());
        List<PromotionGoodsLimitDTO> promotionGoodsList = contextDTO.getTargetGoodsList();
        if (CollUtil.isEmpty(promotionGoodsList)) {
            log.info("[buildSecKillSpecial]无活动数据");
            return Lists.newArrayList();
        }
        // 获取秒杀&特价商品
        List<PromotionGoodsLimitDTO> goodsList;
        if (!isCombination) {
            goodsList = promotionGoodsList.stream().filter(item -> PromotionTypeEnum.isSecKillOrSpecialPrice(item.getType())).collect(Collectors.toList());
        } else {
            goodsList = promotionGoodsList.stream().filter(item -> PromotionTypeEnum.isCombinationPackage(item.getType())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(goodsList)) {
            log.info("[buildSecKillSpecial]无特价秒杀数据");
            return Lists.newArrayList();
        }

        Long buyerEid = contextDTO.getBuyerEid();
        if (Objects.isNull(buyerEid)) {
            log.info("[checkSecKillOrSpecial]buyerEid为空，跳过秒杀特价校验，参数：{}", contextDTO);
            return Lists.newArrayList();
        }

        // 获取所有活动id
        List<Long> promotionIdList = goodsList.stream().map(PromotionGoodsLimitDTO::getPromotionActivityId).collect(Collectors.toList());
        List<PromotionSecKillSpecialDO> secKillSpecialList = secKillSpecialService.queryByPromotionActivityIdList(promotionIdList);

        Map<Long, PromotionSecKillSpecialDO> secKillSpecialMap = secKillSpecialList.stream().collect(Collectors.toMap(PromotionSecKillSpecialDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));

        Map<String, Function<PromotionCheckContextDTO, Boolean>> checkerMap = new HashMap<>();
        checkerMap.put("terminalTypeCheck", this::terminalTypeCheck);
        checkerMap.put("permittedAreaTypeCheck", this::permittedAreaTypeCheck);
        checkerMap.put("permittedEnterpriseTypeCheck", this::permittedEnterpriseTypeCheck);
        checkerMap.put("leftBuyCountCheck", this::leftBuyCountCheck);

        List<PromotionGoodsLimitDTO> result = goodsList.stream().filter(item -> {
            PromotionSecKillSpecialDO secKillSpecialDO = secKillSpecialMap.get(item.getPromotionActivityId());

            PromotionSecKillSpecialDTO secKillSpecialDTO = PojoUtils.map(secKillSpecialDO, PromotionSecKillSpecialDTO.class);

            contextDTO.setCurrentGoodsLimitDTO(item);
            contextDTO.setSecKillSpecialDTO(secKillSpecialDTO);

            return checkerMap.values().stream().allMatch(checker -> checker.apply(contextDTO));
        }).collect(Collectors.toList());
        log.info("开始构建秒杀特价信息结束 时间戳{}"+System.currentTimeMillis());
        return result;
    }

    /**
     * 构建组合包
     *
     * @param contextDTO
     */
    public List<PromotionGoodsLimitDTO> buildCombinationPackage(PromotionCheckContextDTO contextDTO) {
        log.info("开始构建组合包信息开始 时间戳{}"+System.currentTimeMillis());
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = buildSecKillSpecial(contextDTO, true);
        List<PromotionGoodsLimitDTO> goodsList = promotionGoodsLimitDTOS.stream().filter(item -> PromotionTypeEnum.isCombinationPackage(item.getType())).collect(Collectors.toList());
        if (CollUtil.isEmpty(goodsList)) {
            log.info("[uildCombinationPackage]无组合包数据");
            return Lists.newArrayList();
        }
        List<Long> ids = goodsList.stream().map(PromotionGoodsLimitDTO::getPromotionActivityId).collect(Collectors.toList());
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS1 = getCombinationPackByActivityID(ids, goodsList);

        // 计算最大购买数量maxBuyCounnt
        List<PromotionGoodsLimitDTO> promotionGoodsLimits = new ArrayList<>();
        promotionGoodsLimitDTOS1.forEach(item -> promotionGoodsLimits.addAll(item.getGoodsLimitDTOS()));
        List<Long> goodsSkuIds = promotionGoodsLimits.stream().map(PromotionGoodsLimitDTO::getGoodsSkuId).collect(Collectors.toList());
        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSkuIds);
        Map<Long, GoodsSkuDTO> goodsSkuDTOMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        promotionGoodsLimitDTOS1.forEach(item -> {
            List<PromotionGoodsLimitDTO> goodsLimitDTOS = item.getGoodsLimitDTOS();
            List<BigDecimal> canBuyNums = new ArrayList<>();
            goodsLimitDTOS.forEach(e -> {
                Integer allowBuyCount = e.getAllowBuyCount();
                GoodsSkuDTO goodsSkuDTO = goodsSkuDTOMap.get(e.getGoodsSkuId());
                if ((goodsSkuDTO != null) && (goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty()) != 0) {
                    long AvailableNum = goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty();
                    BigDecimal canBuyNum = NumberUtil.round(NumberUtil.div(AvailableNum, allowBuyCount.longValue()), 0);
                    canBuyNums.add(canBuyNum);
                } else {
                    canBuyNums.add(new BigDecimal(0));
                }
            });
            BigDecimal bigDecimal = canBuyNums.stream().min(Comparator.comparing(x -> x)).orElse(new BigDecimal(0));
            item.setMaxBuyNum(bigDecimal.intValue());
        });
        log.info("开始构建秒杀组合包信息开始 时间戳{}"+System.currentTimeMillis());
        return promotionGoodsLimitDTOS1;
    }

    private List<PromotionGoodsLimitDTO> getCombinationPackByActivityID(List<Long> ids, List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS) {
        //获取组合包下的商品信息，通过营销活动id
        List<PromotionGoodsLimitDO> goodsLimits = goodsLimitService.queryByActivityIdList(ids);
        Map<Long, List<PromotionGoodsLimitDO>> promotionGoods = goodsLimits.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));
        QueryWrapper<PromotionCombinationPackageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionCombinationPackageDO::getPromotionActivityId, ids);
        // 获取组合包活动表信息。
        List<PromotionCombinationPackageDO> combinationPackages = combinationPackageService.list(wrapper);
        Map<Long, PromotionCombinationPackageDO> promotionPackages = combinationPackages.stream().collect(Collectors.toMap(PromotionCombinationPackageDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));
        if (CollectionUtils.isNotEmpty(promotionGoodsLimitDTOS)) {
            promotionGoodsLimitDTOS.stream().forEach(item -> {
                PromotionCombinationPackageDO combinationPackage = promotionPackages.get(item.getPromotionActivityId());
                // 填写组合包信息从组合包表
                item.setPackageName(combinationPackage.getPackageName());
                item.setInitialNum(combinationPackage.getInitialNum());
                item.setReturnRequirement(combinationPackage.getReturnRequirement());
                item.setPackageShortName(combinationPackage.getPackageShortName());
                item.setPic(combinationPackage.getPic());
                item.setDescriptionOfOtherActivity(combinationPackage.getDescriptionOfOtherActivity());
                List<PromotionGoodsLimitDO> promotionGoodsLimitDOS = promotionGoods.get(item.getPromotionActivityId());
                item.setGoodsLimitDTOS(PojoUtils.map(promotionGoodsLimitDOS, PromotionGoodsLimitDTO.class));
            });
        }
        return promotionGoodsLimitDTOS;
    }

    /**
     * 剩余购买数量校验
     *
     * @param context
     * @return
     */
    private Boolean leftBuyCountCheck(PromotionCheckContextDTO context) {
        Long buyerEid = context.getBuyerEid();
        PromotionGoodsLimitDTO item = context.getCurrentGoodsLimitDTO();
        // 当组合包的时候不限制组合包的购买历史数量
        if (PromotionTypeEnum.isCombinationPackage(item.getType())) {
            return true;
        }
        PromotionBuyRecord param = PromotionBuyRecord.builder().build();
        param.setEid(buyerEid);
        param.setPromotionActivityId(item.getPromotionActivityId());
        param.setGoodsId(item.getGoodsId());
        List<PromotionBuyRecordDO> buyRecordList = buyRecordService.query(param);
        Integer allowBuyCount = item.getAllowBuyCount();
        item.setLeftBuyCount(allowBuyCount);
        if (CollUtil.isEmpty(buyRecordList)) {
            return Boolean.TRUE;
        }
        int sum = buyRecordList.stream().mapToInt(PromotionBuyRecordDO::getGoodsQuantity).sum();
        int leftBuyCount = allowBuyCount - sum;
        item.setLeftBuyCount(leftBuyCount);
        // 这里判断如果剩余可购数量>0,则返回true
        if (leftBuyCount > 0) {
            return Boolean.TRUE;
        }

        log.info("[leftBuyCountCheck]剩余可购数量为0，校验不通过，参数:{}", context);

        return Boolean.FALSE;
    }

    /**
     * 允许购买企业类型校验
     *
     * @param context
     * @return
     */
    public boolean permittedEnterpriseTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        Integer type = context.getEnterprise().getType();

        // 企业类型 1-全部，2-部分
        Integer permittedEnterpriseType = context.getSecKillSpecialDTO().getPermittedEnterpriseType();

        // 企业类型json
        String permittedEnterpriseDetail = context.getSecKillSpecialDTO().getPermittedEnterpriseDetail();

        if (PromotionPermittedTypeEnum.ALL.getType().equals(permittedEnterpriseType)) {
            return true;
        }
        if (PromotionPermittedTypeEnum.PART.getType().equals(permittedEnterpriseType) && StringUtils.isNotBlank(permittedEnterpriseDetail)) {
            List<Integer> integers = JSONObject.parseArray(permittedEnterpriseDetail, Integer.class);
            if (CollUtil.isNotEmpty(integers) && integers.contains(type)) {
                return true;
            }
        }
        log.info("[permittedEnterpriseTypeCheck]购买企业类型校验不通过，参数：{}", context);
        return false;
    }

    /**
     * 允许购买区域校验
     *
     * @param context
     * @return
     */
    public boolean permittedAreaTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        // 允许购买区域 1-全部，2-部分
        Integer permittedAreaType = context.getSecKillSpecialDTO().getPermittedAreaType();

        // 允许购买区域明细json
        String permittedAreaDetail = context.getSecKillSpecialDTO().getPermittedAreaDetail();

        // 所属区域编码
        String regionCode = context.getEnterprise().getRegionCode();

        if (PromotionPermittedTypeEnum.ALL.getType().equals(permittedAreaType)) {
            return true;
        }
        if (PromotionPermittedTypeEnum.PART.getType().equals(permittedAreaType) && StringUtils.isNotBlank(permittedAreaDetail)) {
            boolean checkResult = PromotionAreaUtil.check(permittedAreaDetail, regionCode);
            log.info("[terminalTypeCheck]购买区域校验结果，PromotionAreaUtil.check：{}，参数：{}", checkResult, context);
            return checkResult;

        }
        log.info("[terminalTypeCheck]购买区域校验不通过，参数：{}", context);
        return false;
    }

    /**
     * 终端身份校验
     *
     * @param context
     * @return
     */
    public boolean terminalTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        // 终端身份 1-会员，2-非会员
        Integer terminalType = context.getSecKillSpecialDTO().getTerminalType();

        CurrentMemberForMarketingDTO member = context.getMember();
        Integer currentMember = member.getCurrentMember();

        if (PromotionTerminalTypeEnum.MEMBER.getType().equals(terminalType) && Integer.valueOf(1).equals(currentMember)) {
            return true;
        }
        if (PromotionTerminalTypeEnum.NOT_MEMBER.getType().equals(terminalType) && Integer.valueOf(0).equals(currentMember)) {
            return true;
        }
        // 如果终端身份是3，表示不限制，不做校验
        if (PromotionTerminalTypeEnum.ALL.getType().equals(terminalType)) {
            return true;
        }
        log.info("[terminalTypeCheck]终端身份校验不通过，参数terminalType：{}, currentMember: {}", terminalType, currentMember);
        return false;
    }

    /**
     * 构建赠品信息
     *
     * @param contextDTO
     */
    private List<PromotionGoodsLimitDTO> buildFullGiftInfo(PromotionCheckContextDTO contextDTO) {
        log.info("开始构建赠品信息开始 时间戳{}"+System.currentTimeMillis());
        List<PromotionGoodsLimitDTO> promotionGoodsList = contextDTO.getTargetGoodsList();
        if (CollUtil.isEmpty(promotionGoodsList)) {
            log.info("[buildFullGiftInfo]无活动数据");
            return Lists.newArrayList();
        }
        // 获取满赠活动商品
        List<PromotionGoodsLimitDTO> goodsList = promotionGoodsList.stream().filter(item -> PromotionTypeEnum.isFullGift(item.getType())).collect(Collectors.toList());
        if (CollUtil.isEmpty(goodsList)) {
            log.info("[buildFullGiftInfo]未匹配到满赠活动");
            return Lists.newArrayList();
        }
        goodsList.stream().forEach(goods -> {
            List<PromotionGoodsGiftLimitDO> giftList = goodsGiftLimitService.queryByActivityId(goods.getPromotionActivityId());
            if (CollUtil.isEmpty(giftList)) {
                return;
            }
            List<PromotionGoodsGiftLimitDTO> goodsGiftList = Lists.newArrayList();
            giftList.stream().forEach(gift -> {
                GoodsGiftDO goodsGiftDO = goodsGiftService.getById(gift.getGoodsGiftId());
                if (Objects.isNull(goodsGiftDO)) {
                    return;
                }
                PromotionGoodsGiftLimitDTO goodsGiftDTO = PromotionGoodsGiftLimitDTO.builder().build();
                goodsGiftDTO.setPromotionActivityId(goods.getPromotionActivityId());
                goodsGiftDTO.setPromotionAmount(gift.getPromotionAmount());
                goodsGiftDTO.setGiftName(goodsGiftDO.getName());
                goodsGiftDTO.setPrice(goodsGiftDO.getPrice());
                goodsGiftDTO.setBear(goods.getBear());
                goodsGiftDTO.setPlatformPercent(goods.getPlatformPercent());
                goodsGiftDTO.setPromotionName(goods.getPromotionName());
                goodsGiftList.add(goodsGiftDTO);
            });
            goods.setGoodsGiftLimitList(goodsGiftList);
        });
        log.info("开始构建赠品信息结束 时间戳{}"+System.currentTimeMillis());
        return goodsList;
    }

    /**
     * app购物车展示满赠信息
     *
     * @param request
     * @return
     */
    @Override
    public List<PromotionAppListDTO> queryAppCartPromotion(PromotionAppCartRequest request) {
        log.info("[queryAppCartPromotion]查询购物车商品赠品活动，参数转换前：{}", request);
        request.platformConvert();
        log.info("[queryAppCartPromotion]查询购物车商品赠品活动，参数转换后：{}", request);

        // 1.查询参与活动的商品
        List<PromotionAppCartGoods> cartGoodsList = request.getCartGoodsList();
        List<Long> cartGoodsIdList = cartGoodsList.stream().map(PromotionAppCartGoods::getGoodsId).collect(Collectors.toList());
        List<PromotionGoodsLimitDO> goodsLimitDOList = this.queryByGoodsIdList(cartGoodsIdList, PromotionTypeEnum.FULL_GIFT, request.getPlatform());

        if (CollUtil.isEmpty(goodsLimitDOList)) {
            log.info("[queryAppCartPromotion]未查询到赠品活动，参数：{}", request);
            return Lists.newArrayList();
        }

        // 2、获取赠品信息
        List<Long> promotionActivityIdList = goodsLimitDOList.stream().map(PromotionGoodsLimitDO::getPromotionActivityId).distinct().collect(Collectors.toList());
        List<PromotionGoodsGiftLimitDO> goodsGiftLimitList = goodsGiftLimitService.listByActivityIdList(promotionActivityIdList);
        List<Long> goodsGiftIdList = goodsGiftLimitList.stream().map(PromotionGoodsGiftLimitDO::getGoodsGiftId).distinct().collect(Collectors.toList());
        List<GoodsGiftDO> goodsGiftList = goodsGiftService.listByIds(goodsGiftIdList);
        List<PromotionActivityDO> promotionActivityList = this.listByIds(promotionActivityIdList);

        if (CollUtil.isEmpty(promotionActivityList)) {
            log.info("[queryAppCartPromotion]未查询到有效赠品活动，参数：{}", request);
            return Lists.newArrayList();
        }

        // 3、查询活动下的所有商品，用来构建<去凑单>的商品
        List<PromotionGoodsLimitDO> targetGoodsLimitList = goodsLimitService.queryByActivityIdList(promotionActivityIdList);

        // 4、校验活动
        List<PromotionAppListDTO> result = orderCartCheck(promotionActivityList, goodsLimitDOList, goodsGiftLimitList, goodsGiftList, targetGoodsLimitList, cartGoodsList);
        log.info("[queryAppCartPromotion]查询购物车商品赠品活动，返参：{}", result);
        return result;

    }

    private List<PromotionAppListDTO> orderCartCheck(List<PromotionActivityDO> promotionActivityList, List<PromotionGoodsLimitDO> goodsLimitDOList, List<PromotionGoodsGiftLimitDO> goodsGiftLimitList, List<GoodsGiftDO> goodsGiftList, List<PromotionGoodsLimitDO> targetGoodsLimitList, List<PromotionAppCartGoods> cartGoodsList) {

        // 1、对活动根据 promotionActivityId 映射
        Map<Long, PromotionActivityDO> promotionMap = promotionActivityList.stream().collect(Collectors.toMap(PromotionActivityDO::getId, o -> o, (k1, k2) -> k1));

        // 2、对商品根据 promotionActivityId 分组
        Map<Long, List<PromotionGoodsLimitDO>> promotionGoodsMap = goodsLimitDOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));

        // 3、对赠品根据 promotionActivityId 分组
        Map<Long, List<PromotionGoodsGiftLimitDO>> giftLimitMap = goodsGiftLimitList.stream().collect(Collectors.groupingBy(PromotionGoodsGiftLimitDO::getPromotionActivityId));
        Map<Long, GoodsGiftDO> goodsGiftMap = goodsGiftList.stream().collect(Collectors.toMap(GoodsGiftDO::getId, o -> o, (k1, k2) -> k1));

        // 4、对参数根据 goodsId 映射
        Map<Long, PromotionAppCartGoods> goodsRequestMap = cartGoodsList.stream().collect(Collectors.toMap(PromotionAppCartGoods::getGoodsId, o -> o, (k1, k2) -> k1));

        // 5、数据库商品分组，供去凑单使用
        Map<Long, List<PromotionGoodsLimitDO>> targetPromotionMap = targetGoodsLimitList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDO::getPromotionActivityId));

        // 6、满足要求的赠品
        List<PromotionAppListDTO> result = Lists.newArrayList();

        for (Map.Entry<Long, List<PromotionGoodsLimitDO>> entry : promotionGoodsMap.entrySet()) {

            List<PromotionGoodsLimitDO> goodsList = entry.getValue();
            Long eid = goodsList.get(0).getEid();
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (PromotionGoodsLimitDO item : goodsList) {
                BigDecimal amount = Optional.ofNullable(goodsRequestMap.get(item.getGoodsId())).map(PromotionAppCartGoods::getAmount).orElse(BigDecimal.ZERO);
                totalAmount = totalAmount.add(amount);
            }

            List<PromotionGoodsGiftLimitDO> giftList = giftLimitMap.get(entry.getKey());
            // 按照满赠金额排序
            giftList.sort(Comparator.comparing(PromotionGoodsGiftLimitDO::getPromotionAmount));

            PromotionActivityDO promotion = promotionMap.get(entry.getKey());

            for (PromotionGoodsGiftLimitDO item : giftList) {

                boolean hasStock = false;

                // 1、判断是否商家活动
                if (item.getPromotionStock().equals(0)) {
                    hasStock = true;
                }

                // 2、判断是否平台活动 & 有库存
                if (item.getPromotionStock() > 0 && item.getPromotionStock() > item.getUsedStock()) {
                    hasStock = true;
                }

                // 存在库存->取出->break
                if (hasStock) {
                    PromotionAppListDTO dto = PromotionAppListDTO.builder().eid(eid).promotionActivityId(promotion.getId()).promotionName(promotion.getName()).giftAmountLimit(item.getPromotionAmount()).goodsGiftId(item.getGoodsGiftId()).giftName(goodsGiftMap.get(item.getGoodsGiftId()).getName()).price(goodsGiftMap.get(item.getGoodsGiftId()).getPrice()).pictureUrl(goodsGiftMap.get(item.getGoodsGiftId()).getPictureUrl()).goodsIdList(targetPromotionMap.get(entry.getKey()).stream().map(PromotionGoodsLimitDO::getGoodsId).collect(Collectors.toList())).build();
                    // 这里判断差额 -> 有差额说明未达到满赠条件
                    if (item.getPromotionAmount().compareTo(totalAmount) > -1) {
                        BigDecimal diff = item.getPromotionAmount().subtract(totalAmount);
                        dto.setDiff(diff);
                    }
                    result.add(dto);
                    break;
                }
                log.info("[orderCartCheck]购物车赠品校验库存不通过，参数 item: {}", item);

            }
        }

        return result;
    }

    @Override
    public boolean returnPromotionGoodsGift(PromotionGoodsGiftReturnRequest request) {
        int current = 1;
        int size = 500;
        Page<PromotionActivityDO> page;
        do {
            QueryWrapper<PromotionActivityDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(PromotionActivityDO::getStatus, 1);
            wrapper.lambda().eq(PromotionActivityDO::getSponsorType, 1);
            wrapper.lambda().ge(PromotionActivityDO::getEndTime, request.getStartTime());
            wrapper.lambda().le(PromotionActivityDO::getEndTime, request.getEndTime());
            wrapper.lambda().lt(PromotionActivityDO::getEndTime, new Date());
            Page<PromotionActivityDO> objectPage = new Page<>(current, size);
            page = this.page(objectPage, wrapper);
            log.info("满赠退回赠品的满赠活动为:[{}]", JSON.toJSONString(page));
            List<Long> longList = page.getRecords().stream().map(PromotionActivityDO::getId).collect(Collectors.toList());
            if (longList.size() > 0) {
                List<PromotionGoodsGiftLimitDO> goodsGiftLimitDOS = goodsGiftLimitService.listByActivityId(longList);
                log.info("满赠退回赠品的赠品信息为:[{}]", goodsGiftLimitDOS);
                for (PromotionGoodsGiftLimitDO goodsGiftLimitDO : goodsGiftLimitDOS) {
                    if (goodsGiftLimitDO.getPromotionStock() > 0 && goodsGiftLimitDO.getPromotionStock() > goodsGiftLimitDO.getUsedStock()) {
                        log.info("开始退回赠品:[{}]", goodsGiftLimitDO);
                        int quantity = goodsGiftLimitDO.getPromotionStock() - goodsGiftLimitDO.getUsedStock();
                        goodsGiftService.increase(quantity, goodsGiftLimitDO.getGoodsGiftId());
                        goodsGiftLimitService.reduceActivityQuantity(goodsGiftLimitDO.getId(), quantity, request.getOpUserId(), request.getOpTime());
                        log.info("完成退回赠品");
                    }
                }
                log.info("满赠活动修改为停用:[{}]", longList);
                this.stopPromotion(longList, request.getOpUserId(), request.getOpTime());
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        return false;
    }

    @Override
    public boolean stopPromotion(List<Long> idList, Long opUserId, Date opTime) {
        List<PromotionActivityDO> activityDOList = new ArrayList<>();
        for (Long id : idList) {
            PromotionActivityDO activityDO = new PromotionActivityDO();
            activityDO.setId(id);
            activityDO.setStatus(2);
            activityDO.setUpdateUser(opUserId);
            activityDO.setUpdateTime(opTime);
            activityDOList.add(activityDO);
        }
        return this.updateBatchById(activityDOList);
    }

    /**
     * 查询企业参与的活动
     *
     * @param enterpriseRequest
     * @return
     */
    @Override
    public List<PromotionAppListDTO> queryEnterprisePromotion(PromotionEnterpriseRequest enterpriseRequest) {
        if (CollUtil.isEmpty(enterpriseRequest.getEIdList())) {
            return Lists.newArrayList();
        }
        List<PromotionAppListDTO> promotionList = this.baseMapper.queryEnterprisePromotion(enterpriseRequest);
        return promotionList;
    }
}
