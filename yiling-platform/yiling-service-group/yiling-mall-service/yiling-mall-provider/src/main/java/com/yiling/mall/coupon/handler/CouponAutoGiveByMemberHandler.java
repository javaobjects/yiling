package com.yiling.mall.coupon.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yiling.mall.coupon.bo.CouponActivityAutoGiveContext;
import com.yiling.mall.coupon.util.CouponListenerUtil;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveCheckEnum;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityPromotionCodeEnum;
import com.yiling.marketing.common.enums.CouponGetEnterpriseLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoDetailRequest;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveDetailRequest;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员自动发券
 * @author: houjie.sun
 * @date: 2021/11/25
 */
@Slf4j
@Service
public class CouponAutoGiveByMemberHandler {

    @DubboReference
    CouponActivityAutoGiveApi  autoGiveApi;
    @Autowired
    private CouponListenerUtil couponListenerUtil;

    /**
     * 会员自动发券规则校验
     * @param context
     * @return
     */
    public List<CouponActivityAutoGiveDetailDTO> checkMemberGiveRules(CouponActivityAutoGiveContext context) {

        EnterpriseDTO enterprise = context.getEnterpriseDTO();

        List<CouponActivityAutoGiveDetailDTO> resultList = Lists.newArrayList();

        Long eid = enterprise.getId();

        QueryCouponActivityGiveDetailRequest request = QueryCouponActivityGiveDetailRequest.builder()
                .type(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode()).build();

        // 获取所有正在进行中的活动
        List<CouponActivityAutoGiveDetailDTO> autoGiveDetailList = autoGiveApi.getAllByCondition(request);
        if (CollUtil.isEmpty(autoGiveDetailList)) {
            log.info("无有效活动，eid:[{}]", eid);
            return resultList;
        }

        List<CouponActivityDTO> effectiveCouponActivityList = couponListenerUtil.getEffectiveCouponActivity(autoGiveDetailList);
        if (CollUtil.isEmpty(effectiveCouponActivityList)) {
            log.info("无有效优惠券，eid:[{}]", eid);
            return resultList;
        }

        // 设置部分会员 -> 关联会员列表 ->
        List<Long> autoGivePartMemberIdList = autoGiveDetailList.stream()
            .filter(item -> CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode().equals(item.getConditionUserType()))
            .map(CouponActivityAutoGiveDetailDTO::getId).collect(Collectors.toList());
        List<CouponActivityAutoGiveEnterpriseLimitDTO> enterpriseLimitList = autoGiveApi.getEnterpriseLimitByAutoGiveIdList(autoGivePartMemberIdList);

        // 部分会员Map
        Map<Long, List<CouponActivityAutoGiveEnterpriseLimitDTO>> autoGiveEnterpriseMap = enterpriseLimitList.stream().collect(Collectors.groupingBy(CouponActivityAutoGiveEnterpriseLimitDTO::getCouponActivityAutoGiveId));

        log.info("[checkMemberGiveRules]#活动列表待匹配：{}",autoGiveDetailList);

        Map<CouponActivityAutoGiveCheckEnum, Function<CouponActivityAutoGiveContext,Boolean>> checkMap = new HashMap<>();
        checkMap.put(CouponActivityAutoGiveCheckEnum.CHECK_COUPON_ACTIVITY_LIST,    this::checkCouponActivityList);
        checkMap.put(CouponActivityAutoGiveCheckEnum.CHECK_ENTERPRISE_TYPE_LIMIT,   this::checkEnterpriseTypeLimit);
        checkMap.put(CouponActivityAutoGiveCheckEnum.CHECK_PROMOTION_CODE,          this::checkPromotionCode);
        checkMap.put(CouponActivityAutoGiveCheckEnum.CHECK_ENTERPRISE_LIMIT,        this::checkEnterpriseLimit);

        List<CouponActivityAutoGiveDetailDTO> list = autoGiveDetailList.stream().filter(item -> {
            // 设置参数
            context.setWaitCheckAutoGiveDTO(item);
            context.setEnterpriseLimitList(autoGiveEnterpriseMap.get(item.getId()));

            // 校验条件
            return checkMap.values().stream().allMatch(check -> check.apply(context));
        }).collect(Collectors.toList());

        log.info("[checkMemberGiveRules]#活动列表匹配结果testList：{}",list);

        return list;
    }

    /**
     * 校验是否包含优惠券活动
     * @param context 自动发放优惠券上下文类
     * @return
     */
    public boolean checkCouponActivityList(CouponActivityAutoGiveContext context){
        if (CollUtil.isEmpty(context.getWaitCheckAutoGiveDTO().getCouponActivityList())) {
            log.info("[checkCouponActivityList]当前活动无优惠券，current:{}", context.getWaitCheckAutoGiveDTO());
            return false;
        }
        return true;
    }

    /**
     * 校验是否包含部分会员
     * @param context 自动发放优惠券上下文类
     * @return
     */
    public boolean checkEnterpriseLimit(CouponActivityAutoGiveContext context){
        CouponActivityAutoGiveDetailDTO current = context.getWaitCheckAutoGiveDTO();
        CurrentMemberForMarketingDTO member = context.getCurrentMemberDTO();
        Integer userTypeLimit = current.getConditionUserType();
        if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), userTypeLimit)) {

            List<CouponActivityAutoGiveEnterpriseLimitDTO> enterpriseList = context.getEnterpriseLimitList();

            if (CollectionUtils.isEmpty(enterpriseList)) {
                log.info("[checkEnterpriseLimit]当前活动部分会员设置为空，current:{}", current);
                return false;
            }

            if (!enterpriseList.stream().anyMatch(item -> member.getMemeberIds().contains(item.getMemberId()))) {
                log.info("[checkEnterpriseLimit]当前活动部分会员设置校验不通过，current:{}", current);
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含推广码
     * @param context 自动发放优惠券上下文类
     * @return
     */
    public boolean checkPromotionCode(CouponActivityAutoGiveContext context){
        CouponActivityAutoGiveDetailDTO current = context.getWaitCheckAutoGiveDTO();
        CurrentMemberForMarketingDTO member = context.getCurrentMemberDTO();
        Integer promotionCode = current.getConditionPromotionCode();
        if (CouponActivityPromotionCodeEnum.CONTAIN.getCode().equals(promotionCode)) {
            // 包含推广码 -> 如果推广人id为空，则continue
            if (Objects.isNull(member.getPromoterId()) || Objects.equals(member.getPromoterId(), Long.valueOf(0))) {
                log.info("[checkPromotionCode]CONTAIN_当前活动是否包含推广码不匹配，current:{}", current);
                return false;
            }
            // 设置推广码企业id
            current.setPromotionEid(member.getPromoterId());
        } else if (CouponActivityPromotionCodeEnum.NOT_CONTAIN.getCode().equals(promotionCode)){
            // 不包含推广码 -> 如果推广人id不为空，则continue
            if (Objects.nonNull(member.getPromoterId()) && !Objects.equals(member.getPromoterId(), Long.valueOf(0))) {
                log.info("[checkPromotionCode]NOT_CONTAIN_当前活动是否包含推广码不匹配，current:{}", current);
                return false;
            }
        }
        return true;
    }

    /**
     * 校验是否包含企业类型
     * @param context 自动发放优惠券上下文类
     * @return
     */
    public boolean checkEnterpriseTypeLimit(CouponActivityAutoGiveContext context){
        EnterpriseDTO enterpriseDTO = context.getEnterpriseDTO();
        CouponActivityAutoGiveDetailDTO current = context.getWaitCheckAutoGiveDTO();
        Integer enterpriseTypeLimit = current.getConditionEnterpriseType();
        if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), enterpriseTypeLimit)) {
            List<String> enterpriseTypeValueList = current.getConditionEnterpriseTypeValueList();
            if (!enterpriseTypeValueList.contains(String.valueOf(enterpriseDTO.getType()))) {
                log.info("[checkEnterpriseTypeLimit]当前活动企业类型不匹配，current:{}", current);
                return false;
            }
        }
        return true;
    }

    public boolean memberHandler(EnterpriseDTO enterprise, List<CouponActivityAutoGiveDetailDTO> autoGiveDetailList) {

        long nowTime = System.currentTimeMillis();
        Date date = new Date();
        // 活动已发放记录
        Map<Long, List<CouponActivityAutoGiveRecordDTO>> autoGiveRecordMap = couponListenerUtil.getAutoGiveRecordMap(enterprise.getId(), autoGiveDetailList);
        log.info("[memberHandler]autoGiveRecordMap:{}", autoGiveRecordMap);
        // 优惠券活动
        List<CouponActivityAutoGiveCouponDetailDTO> autoGiveCouponDetailList = couponListenerUtil.getAutoGiveCouponDetailList(autoGiveDetailList);
        log.info("[memberHandler]autoGiveCouponDetailList:{}", autoGiveCouponDetailList);
        // 可发放的优惠券活动
        List<CouponActivityDetailDTO> couponActivityCanGiveList = new ArrayList<>();
        Map<Long, CouponActivityDetailDTO> couponActivityCanGiveMap = new HashMap<>();
        // 优惠券活动已生成券数量
        Map<Long, Integer> giveCountMap = new HashMap<>();
        // 优惠券活动校验
        couponListenerUtil.verifiedCouponActivity(nowTime, autoGiveCouponDetailList, couponActivityCanGiveList, couponActivityCanGiveMap, giveCountMap);

        // 发放优惠券
        List<SaveCouponRequest> couponList = new ArrayList<>();
        // 发放记录
        List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList = new ArrayList<>();
        // 待更新发放活动
        List<Long> updateAutoGiveIdList = new ArrayList<>();
        // 发放企业信息
        List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> giveDetailList = new ArrayList<>();

        // 处理发放
        couponListenerUtil.handleGive(enterprise, null, autoGiveDetailList, date, autoGiveRecordMap, couponActivityCanGiveMap, giveCountMap,
            couponList, newAutoGiveRecordList, updateAutoGiveIdList, giveDetailList, CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode());
        // 执行发放
        return couponListenerUtil.couponExecute(date, couponList, newAutoGiveRecordList, updateAutoGiveIdList, giveDetailList, true);
    }

}
