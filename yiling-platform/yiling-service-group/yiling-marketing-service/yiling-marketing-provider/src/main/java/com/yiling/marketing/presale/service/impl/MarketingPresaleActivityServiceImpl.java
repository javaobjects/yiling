package com.yiling.marketing.presale.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.marketing.presale.dao.MarketingPresaleBuyRecordMapper;
import com.yiling.marketing.presale.dao.MarketingPresaleBuyerLimitMapper;
import com.yiling.marketing.presale.dao.MarketingPresalePromoterMemberLimitMapper;
import com.yiling.marketing.presale.dto.PresaleActivityBuyRecorderDTO;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.PresaleActivityOrderDTO;
import com.yiling.marketing.presale.dto.PresaleRelativeDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.presale.dto.request.QueryPresaleOrderRequest;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.marketing.presale.dao.MarketingPresaleActivityMapper;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyRecordDO;
import com.yiling.marketing.presale.entity.MarketingPresaleGoodsLimitDO;
import com.yiling.marketing.presale.enums.GoodsPresaleEnum;
import com.yiling.marketing.presale.service.MarketingPresaleActivityService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.presale.service.MarketingPresaleBuyRecordService;
import com.yiling.marketing.presale.service.MarketingPresaleGoodsLimitService;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;

import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 预售活动主表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
@Slf4j
@Service
public class MarketingPresaleActivityServiceImpl extends BaseServiceImpl<MarketingPresaleActivityMapper, MarketingPresaleActivityDO> implements MarketingPresaleActivityService {

    @Autowired
    MarketingPresaleActivityMapper presaleActivityMapper;

    @Autowired
    MarketingPresaleBuyRecordMapper buyRecordMapper;

    @Autowired
    MarketingPresaleBuyRecordService buyRecordService;

    @Autowired
    MarketingPresaleGoodsLimitService goodsLimitService;

    @Autowired
    MarketingPresaleBuyerLimitMapper buyerLimitMapper;

    @Autowired
    MarketingPresalePromoterMemberLimitMapper promoterMapper;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private MemberApi memberApi;

    @DubboReference
    private OrderFirstInfoApi firstInfoApi;

    @DubboReference
    private PresaleOrderApi presaleOrderApi;

    @Override
    public List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEid(QueryPresaleInfoRequest request) {
        log.info("getPresaleInfoByGoodsIdAndEid=====>" + JSONUtil.toJsonStr(request));
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(request.getBuyEid())).orElse(new CurrentMemberForMarketingDTO());
        Boolean isMember = member.getCurrentMember() == 1;
        List<Long> zhanwei = new ArrayList<>(1);
        zhanwei.add(-1L);
        if (CollectionUtils.isNotEmpty(member.getMemeberIds())) {
            request.setMemberIds(member.getMemeberIds());
        } else {
            request.setMemberIds(zhanwei);
        }
        List<Long> promoters = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(member.getPromoterIds())) {
            promoters = member.getPromoterIds();
        } else {
            promoters = zhanwei;
        }
        List<PresaleActivityDTO> availablePresaleInfo = presaleActivityMapper.getAvailablePresaleInfo(request);
        log.info("availablePresaleInfo---->" + JSONUtil.toJsonStr(availablePresaleInfo));
        if (CollectionUtils.isEmpty(availablePresaleInfo)) {
            return new ArrayList<>();
        }
        // 校验其他条件0 同一个商品对应的多个预售活动，选择时间最早的 1 客户范围  2新客 3最小预定最大预定总预定

        // 可以操作的原始数据
        List<PresaleActivityDTO> presaleActivityDTOS = availablePresaleInfo;
        if (CollectionUtils.isEmpty(presaleActivityDTOS)) {
            return new ArrayList<>();
        }
        // 指定用户的集合
        List<Long> buyerLimit = new ArrayList<>();
        List<Long> promoterLimit = new ArrayList<>();
        presaleActivityDTOS.forEach(item -> {
            if (item.getConditionBuyerType() == 2) {
                buyerLimit.add(item.getId());
            }
            if (item.getConditionBuyerType() == 3) {
                // 指定会员方案

                // 指定推广方会员
                if (item.getConditionUserType() == 5) {
                    promoterLimit.add(item.getId());
                }
            }
        });
        // 获得关联表的信息，用于过滤
        List<PresaleRelativeDTO> buyLimitDto = new ArrayList<>();
        List<PresaleRelativeDTO> promoterLimitDto = new ArrayList<>();
        // 获取购买人限制
        if (CollectionUtils.isNotEmpty(buyerLimit)) {
            buyLimitDto = buyerLimitMapper.getInfoByPresaleId(buyerLimit);
        }
        // 获取会员方案限制

        // 获取推广人限制
        if (CollectionUtils.isNotEmpty(promoterLimit)) {
            promoterLimitDto = promoterMapper.getInfoByPresaleId(promoterLimit);
        }
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getBuyEid());
        Integer buyerType = enterpriseDTO.getType();

        // 返回的表示优惠券可以领取（下面过滤后可以领取的），但是不一定可以使用的券。
        // 获取推广方会员id和会员方案id
        List<PresaleActivityDTO> finalResult = new ArrayList<>();
        List<PresaleRelativeDTO> finalBuyLimitDto = buyLimitDto;
        List<PresaleRelativeDTO> finalPromoterLimitDto = promoterLimitDto;
        Map<Long, Map<Long, List<PresaleRelativeDTO>>> buyLimitMap = new HashMap<>();
        Map<Long, Map<Long, List<PresaleRelativeDTO>>> promoterMap = new HashMap<>();
        if (finalBuyLimitDto != null) {
            buyLimitMap = finalBuyLimitDto.stream().collect(Collectors.groupingBy(PresaleRelativeDTO::getId, Collectors.groupingBy(PresaleRelativeDTO::getBuyerEid)));
        }
        if (finalPromoterLimitDto != null) {
            promoterMap = finalPromoterLimitDto.stream().collect(Collectors.groupingBy(PresaleRelativeDTO::getId, Collectors.groupingBy(PresaleRelativeDTO::getPromoterEid)));
        }
        Boolean newVisitor = firstInfoApi.checkNewVisitor(request.getBuyEid(), OrderTypeEnum.B2B);
        for (PresaleActivityDTO item : presaleActivityDTOS) {
            // 判断新客
            if (StringUtils.isNotEmpty(item.getConditionOther()) && !newVisitor) {
                continue;
            }
            // 判断指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）
            if (StringUtils.isNotEmpty(item.getConditionEnterpriseTypeValue()) && !item.getConditionEnterpriseTypeValue().contains(buyerType.toString())) {
                continue;
            }
            // 限制普通用户，但是客户是会员不能选择
            if (item.getConditionUserType() == 2 && isMember) {
                continue;
            }
            if ((item.getConditionUserType() == 4 || item.getConditionUserType() == 3 || item.getConditionUserType() == 5) && !isMember) {
                continue;
            }
            if (item.getConditionBuyerType() == 1) {
                finalResult.add(item);
            }
            if (item.getConditionBuyerType() == 2) {
                if (buyLimitMap.get(item.getId()) != null && buyLimitMap.get(item.getId()).get(request.getBuyEid()) != null) {
                    finalResult.add(item);
                }
            }
            if (item.getConditionBuyerType() == 3) {
                // 指定会员方案
                // 指定推广方会员
                if (item.getConditionUserType() == 5) {
                    if (promoterMap.get(item.getId()) != null) {
                        Map<Long, List<PresaleRelativeDTO>> longListMap = promoterMap.get(item.getId());
                        Optional<Long> any = promoters.stream().filter(item2 -> longListMap.containsKey(item2)).findAny();
                        if (!any.isPresent()) {
                            continue;
                        }
                    }
                }
                finalResult.add(item);
            }
        }
        if (CollectionUtils.isEmpty(finalResult)) {
            return new ArrayList<>();
        }
        Map<Long, List<PresaleActivityDTO>> finalResultMap = finalResult.stream().collect(Collectors.groupingBy(PresaleActivityDTO::getGoodsId));
        // 可以操作的原始数据
        //0 同一个商品对应的多个预售活动，选择时间最早的
        List<PresaleActivityDTO> presaleActivityDTOS1 = new ArrayList<>();
        Set<Map.Entry<Long, List<PresaleActivityDTO>>> presaleSet1 = finalResultMap.entrySet();
        for (Map.Entry<Long, List<PresaleActivityDTO>> entry : presaleSet1) {
            List<PresaleActivityDTO> value = entry.getValue();
            Optional<PresaleActivityDTO> max = value.stream().max(Comparator.comparing(PresaleActivityDTO::getCreateTime));
            if (max.isPresent()) {
                presaleActivityDTOS1.add(max.get());
            }
        }
        finalResult=presaleActivityDTOS1;
        List<Long> goodsIds = finalResult.stream().map(PresaleActivityDTO::getGoodsId).collect(Collectors.toList());
        List<Long> presaleIds = finalResult.stream().map(PresaleActivityDTO::getId).collect(Collectors.toList());
        // 获取用户购买单个商品的数量，和获取商品再某个活动下的全部购买数量  购买历史
        List<PresaleActivityBuyRecorderDTO> presaleRcorder = buyRecordMapper.getGooodsBuyCount(goodsIds, null, presaleIds);
        List<PresaleActivityBuyRecorderDTO> buyrecorder = buyRecordMapper.getGooodsBuyCount(goodsIds, request.getBuyEid(), presaleIds);
        // 活动下的商品有人购买过，不一定是当前用户
        if (CollectionUtils.isNotEmpty(presaleRcorder)) {
            finalResult.forEach(item -> {
                item.setAllHasBuyNum(getAllHasBuyNum(presaleRcorder, item));
                item.setCurrentHasBuyNum(getBuyerHasBuyNum(buyrecorder, item));
            });
        } else {
            // 活动下的商品没有购买过
            finalResult.forEach(item -> {
                item.setAllHasBuyNum(0);
                item.setCurrentHasBuyNum(0);
            });
        }
        if (CollectionUtils.isNotEmpty(finalResult)) {
            finalResult.forEach(item->{
                if(item.getPresaleType()==1){
                  // 定金
                    BigDecimal depositAmount = NumberUtil.round(NumberUtil.mul(item.getPresaleAmount(),NumberUtil.div(item.getDepositRatio(),100)),2);
                    item.setDeposit(depositAmount);
                    // 尾款
                    if(item.getGoodsPresaleType()==0){
                        item.setFinalPayment(NumberUtil.sub(item.getPresaleAmount(),depositAmount));
                    }
                    if(item.getGoodsPresaleType()==1){
                        // 定金膨胀金额
                        BigDecimal expansionAmount = NumberUtil.round(NumberUtil.mul(depositAmount,item.getExpansionMultiplier()),2);
                        item.setFinalPayment(NumberUtil.sub(item.getPresaleAmount(),expansionAmount));
                    }
                    if(item.getGoodsPresaleType()==2){
                        // 尾款立减
                        item.setFinalPayment(NumberUtil.sub(item.getPresaleAmount(),depositAmount,item.getFinalPayDiscountAmount()));
                    }
                }
                if(item.getPresaleType()==2){
                    item.setDeposit(BigDecimal.ZERO);
                    item.setFinalPayment(BigDecimal.ZERO);
                }
            });
        }
        return PojoUtils.map(finalResult, PresaleActivityGoodsDTO.class);
    }

    @Override
    public List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEidNoNum(QueryPresaleInfoRequest request) {
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(request.getBuyEid())).orElse(new CurrentMemberForMarketingDTO());
        Boolean isMember = member.getCurrentMember() == 1;
        List<Long> zhanwei = new ArrayList<>(1);
        zhanwei.add(-1L);
        if (CollectionUtils.isNotEmpty(member.getMemeberIds())) {
            request.setMemberIds(member.getMemeberIds());
        } else {
            request.setMemberIds(zhanwei);
        }
        List<Long> promoters = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(member.getPromoterIds())) {
            promoters = member.getPromoterIds();
        } else {
            promoters = zhanwei;
        }
        log.info("getAvailablePresaleInfo=====>" + JSONUtil.toJsonStr(request));
        List<PresaleActivityDTO> availablePresaleInfo = presaleActivityMapper.getAvailablePresaleInfo(request);
        log.info("availablePresaleInfo---->" + JSONUtil.toJsonStr(availablePresaleInfo));
        if (CollectionUtils.isEmpty(availablePresaleInfo)) {
            return new ArrayList<>();
        }
        // 校验其他条件0 同一个商品对应的多个预售活动，选择时间最早的 1 客户范围  2新客 3最小预定最大预定总预定
       // 可以操作的原始数据
        List<PresaleActivityDTO> presaleActivityDTOS = availablePresaleInfo;
        if (CollectionUtils.isEmpty(presaleActivityDTOS)) {
            return new ArrayList<>();
        }
        // 指定用户的集合
        List<Long> buyerLimit = new ArrayList<>();
        List<Long> promoterLimit = new ArrayList<>();
        presaleActivityDTOS.forEach(item -> {
            if (item.getConditionBuyerType() == 2) {
                buyerLimit.add(item.getId());
            }
            if (item.getConditionBuyerType() == 3) {
                // 指定会员方案

                // 指定推广方会员
                if (item.getConditionUserType() == 5) {
                    promoterLimit.add(item.getId());
                }
            }
        });
        // 获得关联表的信息，用于过滤
        List<PresaleRelativeDTO> buyLimitDto = new ArrayList<>();
        List<PresaleRelativeDTO> promoterLimitDto = new ArrayList<>();
        // 获取购买人限制
        if (CollectionUtils.isNotEmpty(buyerLimit)) {
            buyLimitDto = buyerLimitMapper.getInfoByPresaleId(buyerLimit);
        }
        // 获取会员方案限制

        // 获取推广人限制
        if (CollectionUtils.isNotEmpty(promoterLimit)) {
            promoterLimitDto = promoterMapper.getInfoByPresaleId(promoterLimit);
        }
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getBuyEid());
        Integer buyerType = enterpriseDTO.getType();

        // 返回的表示优惠券可以领取（下面过滤后可以领取的），但是不一定可以使用的券。
        // 获取推广方会员id和会员方案id
        Long promoterId = member.getPromoterId();
        List<PresaleActivityDTO> finalResult = new ArrayList<>();
        List<PresaleRelativeDTO> finalBuyLimitDto = buyLimitDto;
        List<PresaleRelativeDTO> finalPromoterLimitDto = promoterLimitDto;
        Map<Long, Map<Long, List<PresaleRelativeDTO>>> buyLimitMap = new HashMap<>();
        Map<Long, Map<Long, List<PresaleRelativeDTO>>> promoterMap = new HashMap<>();
        if (finalBuyLimitDto != null) {
            buyLimitMap = finalBuyLimitDto.stream().collect(Collectors.groupingBy(PresaleRelativeDTO::getId, Collectors.groupingBy(PresaleRelativeDTO::getBuyerEid)));
        }
        if (finalPromoterLimitDto != null) {
            promoterMap = finalPromoterLimitDto.stream().collect(Collectors.groupingBy(PresaleRelativeDTO::getId, Collectors.groupingBy(PresaleRelativeDTO::getPromoterEid)));
        }
        Boolean newVisitor = firstInfoApi.checkNewVisitor(request.getBuyEid(), OrderTypeEnum.B2B);
        for (PresaleActivityDTO item : presaleActivityDTOS) {
            // 判断新客
            if (StringUtils.isNotEmpty(item.getConditionOther()) && !newVisitor) {
                continue;
            }
            // 判断指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）
            if (StringUtils.isNotEmpty(item.getConditionEnterpriseTypeValue()) && !item.getConditionEnterpriseTypeValue().contains(buyerType.toString())) {
                continue;
            }
            // 限制普通用户，但是客户是会员不能选择
            if (item.getConditionUserType() == 2 && isMember) {
                continue;
            }
            if ((item.getConditionUserType() == 4 || item.getConditionUserType() == 3 || item.getConditionUserType() == 5) && !isMember) {
                continue;
            }
            if (item.getConditionBuyerType() == 1) {
                finalResult.add(item);
            }
            if (item.getConditionBuyerType() == 2) {
                if (buyLimitMap.get(item.getId()) != null && buyLimitMap.get(item.getId()).get(request.getBuyEid()) != null) {
                    finalResult.add(item);
                }
            }
            if (item.getConditionBuyerType() == 3) {
                // 指定会员方案
                // 指定推广方会员
                if (item.getConditionUserType() == 5) {
                    if (promoterMap.get(item.getId()) != null) {
                        Map<Long, List<PresaleRelativeDTO>> longListMap = promoterMap.get(item.getId());
                        Optional<Long> any = promoters.stream().filter(item2 -> longListMap.containsKey(item2)).findAny();
                        if (!any.isPresent()) {
                            continue;
                        }
                    }
                }
                finalResult.add(item);
            }
        }
        if (CollectionUtils.isEmpty(finalResult)) {
            return new ArrayList<>();
        }
        Map<Long, List<PresaleActivityDTO>> finalResultMap = finalResult.stream().collect(Collectors.groupingBy(PresaleActivityDTO::getGoodsId));
        // 可以操作的原始数据
        List<PresaleActivityDTO> presaleActivityDTOS1 = new ArrayList<>();
        Set<Map.Entry<Long, List<PresaleActivityDTO>>> presaleSet1 = finalResultMap.entrySet();
        for (Map.Entry<Long, List<PresaleActivityDTO>> entry : presaleSet1) {
            List<PresaleActivityDTO> value = entry.getValue();
            Optional<PresaleActivityDTO> max = value.stream().max(Comparator.comparing(PresaleActivityDTO::getCreateTime));
            if (max.isPresent()) {
                presaleActivityDTOS1.add(max.get());
            }
        }
        finalResult=presaleActivityDTOS1;
        if (CollectionUtils.isEmpty(finalResult)) {
            return new ArrayList<>();
        }
        return PojoUtils.map(finalResult, PresaleActivityGoodsDTO.class);
    }

    @Override
    public Page<PresaleActivityOrderDTO> queryOrderInfoByPresaleId(QueryPresaleOrderRequest request) {
        MarketingPresaleActivityDO presaleActivityDO = this.getById(request.getId());
        if (ObjectUtil.isNull(presaleActivityDO)) {
            return new Page<PresaleActivityOrderDTO>();
        }
        QueryWrapper<MarketingPresaleBuyRecordDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketingPresaleBuyRecordDO::getMarketingPresaleId, request.getId());
        Page<MarketingPresaleBuyRecordDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        Page<MarketingPresaleBuyRecordDO> page = buyRecordService.page(objectPage, queryWrapper);

        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            Page<PresaleActivityOrderDTO> orderDTOS = PojoUtils.map(page, PresaleActivityOrderDTO.class);
            List<Long> orderIds = page.getRecords().stream().map(MarketingPresaleBuyRecordDO::getOrderId).collect(Collectors.toList());
            List<Long> eids = page.getRecords().stream().map(MarketingPresaleBuyRecordDO::getEid).collect(Collectors.toList());
            List<Long> goodsIds = page.getRecords().stream().map(MarketingPresaleBuyRecordDO::getGoodsId).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(eids);
            Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
            List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(goodsIds);

            Map<Long, GoodsDTO> goodsMap = goodsDTOS.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
            List<PresaleOrderDTO> presaleOrderDTOS = presaleOrderApi.listByOrderIds(orderIds);
            Map<Long, PresaleOrderDTO> orderDTOMap = presaleOrderDTOS.stream().collect(Collectors.toMap(PresaleOrderDTO::getOrderId, Function.identity()));

            QueryWrapper<MarketingPresaleGoodsLimitDO> goodsLimitDOQueryWrapper = new QueryWrapper<>();
            goodsLimitDOQueryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getMarketingStrategyId, request.getId());
            goodsLimitDOQueryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getDelFlag, 0);
            List<MarketingPresaleGoodsLimitDO> goodsLimitDOS = goodsLimitService.list(goodsLimitDOQueryWrapper);
            Map<Long, MarketingPresaleGoodsLimitDO> goodsLimitDOMap = goodsLimitDOS.stream().collect(Collectors.toMap(MarketingPresaleGoodsLimitDO::getGoodsId, Function.identity()));
            orderDTOS.getRecords().forEach(item -> {
                item.setId(request.getId());
                item.setName(presaleActivityDO.getName());
                item.setEname(enterpriseDTOMap.get(item.getEid()) == null ? "" : enterpriseDTOMap.get(item.getEid()).getName());
                Boolean exits = goodsMap.get(item.getGoodsId()) == null;
                if (!exits) {
                    item.setGoodsName(goodsMap.get(item.getGoodsId()).getName());
                    item.setManufacturer(goodsMap.get(item.getGoodsId()).getManufacturer());
                    item.setSellSpecifications(goodsMap.get(item.getGoodsId()).getSellSpecifications());
                    item.setSellerEname(goodsMap.get(item.getGoodsId()).getEname());
                }
                Boolean goodsExits = goodsLimitDOMap.get(item.getGoodsId()) == null;
                if (!goodsExits) {
                    MarketingPresaleGoodsLimitDO goodsLimitDO = goodsLimitDOMap.get(item.getGoodsId());
                    item.setPresalePrice(goodsLimitDO.getPresaleAmount());
                    item.setPresaleTypeStr(presaleActivityDO.getPresaleType() == 1 ? "定金预售" : "全款预售");
                    item.setDepositRatio(goodsLimitDO.getDepositRatio());
                    String name = GoodsPresaleEnum.getByType(goodsLimitDO.getPresaleType()).getName();

                    if (goodsLimitDO.getPresaleType().equals(GoodsPresaleEnum.UNABLE.getCode())) {
                        name = name + goodsLimitDO.getExpansionMultiplier() + "倍";
                    }
                    if (goodsLimitDO.getPresaleType().equals(GoodsPresaleEnum.DISCARD.getCode())) {
                        name = name + goodsLimitDO.getFinalPayDiscountAmount() + "元";
                    }
                    item.setGoodsPresaleType(name);
                }
                Boolean orderExits = orderDTOMap.get(item.getOrderId()) == null;
                if (!orderExits) {
                    PresaleOrderDTO presaleOrderDTO = orderDTOMap.get(item.getOrderId());
                    item.setOrderNo(presaleOrderDTO.getOrderNo());
                    item.setDepositAmount(presaleOrderDTO.getDepositAmount());
                    item.setBalanceAmount(presaleOrderDTO.getBalanceAmount());
                    item.setDiscountAmount(presaleOrderDTO.getPresaleDiscountAmount());
                    item.setTotalAmount(presaleOrderDTO.getPaymentAmount());
                    item.setStatus(OrderStatusEnum.getByCode(presaleOrderDTO.getOrderStatus()).getName());
                    if (presaleOrderDTO.getOrderStatus() == 10 ) {
                        if(presaleOrderDTO.getIsPayDeposit() == 0){
                            item.setStatus("待付定金");
                        }else if (presaleOrderDTO.getIsPayBalance() == 0){
                            item.setStatus("待付尾款");
                        }
                    }
                }
            });
            return orderDTOS;
        }
        return new Page<PresaleActivityOrderDTO>();
    }


    private Integer getAllHasBuyNum(List<PresaleActivityBuyRecorderDTO> buyrecorder, PresaleActivityDTO item) {
        Optional<PresaleActivityBuyRecorderDTO> first = buyrecorder.stream().filter(item1 -> item1.getPresaleActivityId().equals(item.getId()) && item.getGoodsId().equals(item1.getGoodsId())).findFirst();
        return first.isPresent() ? first.get().getCountNum() : 0;
    }

    private Integer getBuyerHasBuyNum(List<PresaleActivityBuyRecorderDTO> presaleRcorder, PresaleActivityDTO item) {
        Optional<PresaleActivityBuyRecorderDTO> first = presaleRcorder.stream().filter(item1 -> item1.getPresaleActivityId().equals(item.getId()) && item.getGoodsId().equals(item1.getGoodsId())).findFirst();
        return first.isPresent() ? first.get().getCountNum() : 0;
    }
}
