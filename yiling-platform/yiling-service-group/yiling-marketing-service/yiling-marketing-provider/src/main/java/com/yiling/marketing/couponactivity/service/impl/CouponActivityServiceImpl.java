package com.yiling.marketing.couponactivity.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.Constants.Constants;
import com.yiling.marketing.common.enums.CouponActivityCanGetEnum;
import com.yiling.marketing.common.enums.CouponActivityGiveRecordStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityLogTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityResultTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityThresholdEnum;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponPayMethodTypeEnum;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.common.enums.MemberCouponActivityStatusEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.dao.CouponMapper;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryCouponPageRequest;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.dto.request.UpdateCouponRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dao.CouponActivityEnterpriseLimitMapper;
import com.yiling.marketing.couponactivity.dao.CouponActivityGoodsLimitMapper;
import com.yiling.marketing.couponactivity.dao.CouponActivityMapper;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityResidueCountDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIncreaseRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityOperateRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponHasGetDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRepeatNameRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityBasicRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityRulesRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseGetRulesDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityGoodsLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityIncreaseDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityMemberLimitDO;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.marketing.couponactivity.event.UpdateCouponGiveNumEvent;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseGetRulesService;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityIncreaseService;
import com.yiling.marketing.couponactivity.service.CouponActivityLogService;
import com.yiling.marketing.couponactivity.service.CouponActivityMemberLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDTO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetCouponService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveCouponDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveCouponService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveRecordService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 优惠券活动表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityServiceImpl extends BaseServiceImpl<CouponActivityMapper, CouponActivityDO> implements CouponActivityService {
    @Autowired
    private ApplicationEventPublisher       applicationEventPublisher;
    //    @DubboReference
    //    private OrderCouponUseApi                       orderCouponUseApi;
    //    @DubboReference
    //    private OrderApi                                orderApi;
    @Autowired
    protected RedisDistributedLock                  redisDistributedLock;
    @Autowired
    private CouponActivityAutoGiveService           autoGiveService;
    @Autowired
    private CouponActivityAutoGetService            autoGetService;
    @Autowired
    private CouponActivityAutoGiveCouponService     autoGiveCouponService;
    @Autowired
    private CouponActivityAutoGetCouponService      autoGetCouponService;
    @Autowired
    private CouponActivityEnterpriseLimitService    couponActivityEnterpriseLimitService;
    @Autowired
    private CouponActivityEnterpriseLimitMapper     couponActivityEnterpriseLimitMapper;
    @Autowired
    private CouponActivityGoodsLimitService         couponActivityGoodsLimitService;
    @Autowired
    private CouponActivityGoodsLimitMapper          couponActivityGoodsLimitMapper;
    @Autowired
    private CouponService                           couponService;
    @Autowired
    private CouponMapper                            couponMapper;
    @Autowired
    private CouponActivityLogService                couponActivityLogService;
    @Autowired
    private CouponActivityIncreaseService           couponActivityIncreaseService;
    @Autowired
    private CouponActivityEnterpriseGetRulesService enterpriseGetRulesService;
    @Autowired
    private CouponActivityMemberLimitService couponActivityMemberLimitService;
    @Autowired
    private CouponActivityAutoGiveRecordService activityAutoGiveRecordService;
    @Autowired
    private CouponActivityMapper                            couponActivityMapper;

    @Override
    public Page<CouponActivityDTO> queryListPage(QueryCouponActivityRequest request) {
        // 运营后台只有账号、没有企业信息 eid = null, currentEid = 0
        Page<CouponActivityDTO> page = request.getPage();
        if (Objects.isNull(request)) {
            return page;
        }
        Long currentEid = request.getCurrentEid();
        try {
            LambdaQueryWrapper<CouponActivityDO> queryWrapper = getCouponActivityWrapper(request);
            page(request.getPage(), queryWrapper);
            page = PojoUtils.map(page(request.getPage(), queryWrapper), CouponActivityDTO.class);
        } catch (Exception e) {
            log.error("获取优惠券活动列表失败，request -> {}, exception -> {}", JSONObject.toJSONString(request), e);
            throw new BusinessException(ResultCode.FAILED);
        }
        return page;
    }


    @Override
    public String buildCouponRules(CouponActivityDTO couponActivity) {
        Integer type = couponActivity.getType();
        BigDecimal thresholdValue = couponActivity.getThresholdValue();
        BigDecimal discountValue = couponActivity.getDiscountValue();
        Integer memberType = couponActivity.getMemberType();
        boolean isMemberCoupon = ObjectUtil.equal(2, memberType);
        StringBuilder sb = new StringBuilder();
        if (!isMemberCoupon) {
            sb.append("订单实付金额满").append(thresholdValue).append("元，则");
        }
        if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), type)) {
            // 满减
            sb.append("减").append(discountValue).append("元");
        } else if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), type)) {
            // 满折
            sb.append("打").append(discountValue).append("%折");
            BigDecimal discountMax = couponActivity.getDiscountMax();
            if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) > 0) {
                if (!isMemberCoupon) {
                    sb.append("，最高优惠").append(discountMax).append("元");
                }
            }
        }
        return sb.toString();
    }

    @Override
    public Map<String, String> buildCouponRulesMobile(CouponActivityDTO couponActivity) {
        Map<String, String> rulesMap = new HashMap<>();
        Integer type = couponActivity.getType();
        BigDecimal thresholdValue = couponActivity.getThresholdValue();
        BigDecimal discountValue = couponActivity.getDiscountValue();
        StringBuilder thresholdValueBuild = new StringBuilder();
        StringBuilder discountValueBuild = new StringBuilder();
        StringBuilder discountMaxBuild = new StringBuilder();

        thresholdValueBuild.append("满").append(thresholdValue).append("可用");
        if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), type)) {
            // 满减
            discountValueBuild.append(discountValue).append("元");
        } else if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), type)) {
            // 满折
            discountValueBuild.append(discountValue).append("折");
            BigDecimal discountMax = couponActivity.getDiscountMax();
            if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) > 0) {
                discountMaxBuild.append("最高减").append(discountMax).append("元");
            }
        }
        rulesMap.put("thresholdValue", thresholdValueBuild.toString());
        rulesMap.put("discountValue", discountValueBuild.toString());
        rulesMap.put("discountMax", discountMaxBuild.toString());
        return rulesMap;
    }

    @Override
    public Boolean updateUseCountByIds(Map<Long, Integer> map, Long userId) {
        if (MapUtil.isEmpty(map)) {
            return false;
        }
        List<CouponActivityDO> list = new ArrayList<>();
        CouponActivityDO couponActivity;
        Date date = new Date();
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            couponActivity = new CouponActivityDO();
            couponActivity.setId(entry.getKey());
            couponActivity.setUseCount(entry.getValue());
            couponActivity.setUpdateUser(userId);
            couponActivity.setUpdateTime(date);
            list.add(couponActivity);
        }
        return this.updateBatchById(list);
    }

    @Override
    public String checkCouponActivity(CouponActivityDO couponActivity, List<Integer> countList, List<Map<String, Long>> giveCountList) {
        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), couponActivity.getStatus())) {
            return "此优惠券活动不是“启用”状态，[" + couponActivity.getId() + "]";
        }
        Long id = couponActivity.getId();
        Date endTime = couponActivity.getEndTime();
        long nowTime = System.currentTimeMillis();
        Integer useDateType = couponActivity.getUseDateType();
        // 固定时间
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
            if (endTime.getTime() <= nowTime) {
                return "此优惠券活动已结束，[" + couponActivity.getId() + "]";
            }
        }

        // 已生成券数量
        Map<Long, Integer> giveCountMap = new HashMap<>();
        if (CollUtil.isNotEmpty(giveCountList)) {
            for (Map<String, Long> map : giveCountList) {
                giveCountMap.put(map.get("couponActivityId").longValue(), map.get("giveCount").intValue());
            }
        }
        Integer totalCount = couponActivity.getTotalCount();
        Integer count = giveCountMap.get(id);
        if (ObjectUtil.isNull(count) || count < 0) {
            count = 0;
        }
        countList.add(count);
        if (count >= totalCount) {
            return MessageFormat.format(CouponErrorCode.COUPON_ACTIVITY_TOTAL_COUNT_ERROR.getMessage(), couponActivity.getId(),
                couponActivity.getTotalCount());
        }
        return null;
    }

    @Override
    public Page<CouponActivityDO> getPageForGoodsGift(QueryCouponActivityRequest request) {
        Page<CouponActivityDO> page = request.getPage();
        if (Objects.isNull(request)) {
            return page;
        }
        return this.baseMapper.getPageForGoodsGift(request.getPage(), request);
    }

    @Override
    public List<CouponActivityDO> getByEnterpriseAndGoodsLimit(QueryCouponActivityLimitRequest request) {
        if(ObjectUtil.isNull(request) || (ObjectUtil.isNull(request.getEnterpriseLimit()) && ObjectUtil.isNull(request.getGoodsLimit()))){
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
        if(ObjectUtil.isNotNull(request.getEnterpriseLimit())){
            queryWrapper.eq(CouponActivityDO::getEnterpriseLimit, request.getEnterpriseLimit());
        }
        if(ObjectUtil.isNotNull(request.getGoodsLimit())){
            queryWrapper.eq(CouponActivityDO::getGoodsLimit, request.getGoodsLimit());
        }
        queryWrapper.eq(CouponActivityDO::getStatus, CouponActivityStatusEnum.ENABLED.getCode());
        List<CouponActivityDO> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            list = list.stream().filter(c -> c.getTotalCount() > c.getUseCount()).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<CouponActivityDO> getCouponActivityByEeid(List<Long> eids) {
        if (CollUtil.isEmpty(eids)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(CouponActivityDO::getEid, eids);
        return PojoUtils.map(this.list(queryWrapper), CouponActivityDO.class);
    }

    @Override
    public String deleteCouponActivityLimitCheck(Long currentEid, Long couponActivityId) {
        CouponActivityDO couponActivityDO = this.getById(couponActivityId);
        if(ObjectUtil.isNull(couponActivityDO)){
            return "优惠券活动信息不存在";
        }
        // 是否已停用、废弃
        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), couponActivityDO.getStatus()) && !ObjectUtil.equal(CouponActivityStatusEnum.DRAFT.getCode(), couponActivityDO.getStatus())) {
            return "优惠券活动已停用或已废弃";
        }
        // 是否已结束
        long nowTime = System.currentTimeMillis();
        if(ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED, couponActivityDO.getUseDateType())){
            if(couponActivityDO.getEndTime().getTime() <= nowTime){
                return "优惠券活动已结束";
            }
        }
        // 是否所属企业
        if (ObjectUtil.isNotNull(currentEid) && !ObjectUtil.equal(currentEid, couponActivityDO.getEid())) {
            return "其他企业创建的优惠券活动";
        }
        // 此优惠券活动是否已经被发放/领取
       // List<CouponDTO> hasGiveCouponList = couponService.getHasGiveListByCouponActivityList(new ArrayList(){{add(couponActivityId);}});
        if(ObjectUtil.isNotNull(couponActivityDO.getGiveCount())&&couponActivityDO.getGiveCount()!=0){
            return "进行的中的活动";
        }
        return null;
    }


    @Override
    public CouponActivityDetailDTO getCouponActivityById(QueryCouponActivityDetailRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getId())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        return getDetail(request.getId());
    }

    private CouponActivityDetailDTO getDetail(Long id) {
        CouponActivityDO couponActivityDO;
        CouponActivityDetailDTO couponActivityDetailDTO;
        try {
            couponActivityDO = this.getById(id);
            couponActivityDetailDTO = PojoUtils.map(couponActivityDO, CouponActivityDetailDTO.class);
            if (ObjectUtil.isNull(couponActivityDetailDTO)) {
                return null;
            }
            List<String> emptyList = ListUtil.empty();
            String platformSelected = couponActivityDO.getPlatformSelected();
            List<String> platformSelectedList = StrUtil.isBlank(platformSelected) ? emptyList : Arrays.asList(platformSelected.split(","));
            String payMethodSelected = couponActivityDO.getPayMethodSelected();
            List<String> payMethodSelectedList = StrUtil.isBlank(payMethodSelected) ? emptyList : Arrays.asList(payMethodSelected.split(","));
            String coexistPromotion = couponActivityDO.getCoexistPromotion();
            List<String> coexistPromotionList = StrUtil.isBlank(coexistPromotion) ? emptyList : Arrays.asList(coexistPromotion.split(","));
            couponActivityDetailDTO.setPlatformSelectedList(platformSelectedList);
            couponActivityDetailDTO.setPayMethodSelectedList(payMethodSelectedList);
            couponActivityDetailDTO.setCoexistPromotionList(coexistPromotionList);
            if (couponActivityDO.getEid() != 0) {
                couponActivityDetailDTO.setEnterpriseLimit(2);
            }
            long nowTime = System.currentTimeMillis();
            // 未开始、已开始状态
            boolean running = false;
            // 是否可以修改名称、预算编号
            boolean editNameFlag = true;
            // 是否可修改商家设置、商品设置
            boolean editLimitFlag = true;

            if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), couponActivityDetailDTO.getUseDateType())) {
                Date beginTime = couponActivityDO.getBeginTime();
                Date endTime = couponActivityDO.getEndTime();
                if (beginTime.getTime() <= nowTime && endTime.getTime() > nowTime) {
                    running = true;
                }
                if (couponActivityDetailDTO.getEndTime().getTime() <= nowTime) {
                    running = true;
                    editNameFlag = false;
                    editLimitFlag = false;
                }
            } else if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.AFTER_GIVE.getCode(), couponActivityDetailDTO.getUseDateType())) {
                // 发放后生效规则，按发放/领取XX天过期
                StringBuilder builder = new StringBuilder().append("按发放/领取").append(couponActivityDetailDTO.getExpiryDays()).append("天过期");
                couponActivityDetailDTO.setGiveOutEffectiveRules(builder.toString());
            }
            // 是否已发放/领取
            // List<CouponDTO> hasGiveCouponList = couponService.getHasGiveListByCouponActivityList(new ArrayList(){{add(id);}});
            if (ObjectUtil.isNotNull(couponActivityDO.getGiveCount()) && couponActivityDO.getGiveCount() != 0) {
                // 此优惠券活动已经被发放/领取
                running = true;
                editNameFlag = false;
            }
            couponActivityDetailDTO.setRunning(running);
            couponActivityDetailDTO.setEditNameFlag(editNameFlag);
            couponActivityDetailDTO.setEditLimitFlag(editLimitFlag);

            // 商家后台，用户领取设置
            Integer canGet = couponActivityDetailDTO.getCanGet();
            if (!ObjectUtil.equal(couponActivityDO.getEid(), 0L) && ObjectUtil.equal(CouponActivityCanGetEnum.CAN_GET.getCode(), canGet)) {
                CouponActivityEnterpriseGetRulesDO enterpriseGetRulesDb = enterpriseGetRulesService.getByCouponActivityId(id);
                couponActivityDetailDTO.setActivityBeginTime(enterpriseGetRulesDb.getBeginTime());
                couponActivityDetailDTO.setActivityEndTime(enterpriseGetRulesDb.getEndTime());
                couponActivityDetailDTO.setGiveNum(enterpriseGetRulesDb.getGiveNum());
                couponActivityDetailDTO.setConditionEnterpriseType(enterpriseGetRulesDb.getConditionEnterpriseType());
                couponActivityDetailDTO.setConditionUserType(enterpriseGetRulesDb.getConditionUserType());
                // 企业类型值
                List<String> enterpriseTypeValueList = ListUtil.empty();
                String enterpriseTypeValue = enterpriseGetRulesDb.getConditionEnterpriseTypeValue();
                if (StrUtil.isNotBlank(enterpriseTypeValue)) {
                    enterpriseTypeValueList = Convert.toList(String.class, enterpriseTypeValue);
                }
                couponActivityDetailDTO.setConditionEnterpriseTypeValueList(enterpriseTypeValueList);
            }
        } catch (Exception e) {
            log.error("获取优惠券活动详情失败，id -> {}, exception -> {}", id, e);
            throw new BusinessException(ResultCode.FAILED);
        }
        return couponActivityDetailDTO;
    }

    @Override
    @GlobalTransactional
    public CouponActivityDetailDTO copy(CouponActivityOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getOpUserId();
        CouponActivityDetailDTO detail = null;
        long newId = 0;

        try {
            detail = getDetail(id);
            if (!ObjectUtil.equal(request.getEid(), detail.getEid())) {
                throw new BusinessException(CouponActivityErrorCode.BUSINESS_COUPON_AUTHORIZATION_ERROR);
            }

            /* 复制基本信息、规则信息 */
            CouponActivityDO couponActivityDO = PojoUtils.map(detail, CouponActivityDO.class);
            couponActivityDO.setId(null);
            couponActivityDO.setStatus(CouponActivityStatusEnum.DRAFT.getCode());
            couponActivityDO.setCreateUser(userId);
            couponActivityDO.setCreateTime(new Date());
            // 开始、结束时间置空

            this.baseMapper.insertCouponActivity(couponActivityDO);
            newId = couponActivityDO.getId();
            detail.setId(newId);

            // 商家后台，用户领取设置
            Integer canGet = detail.getCanGet();
            if (!ObjectUtil.equal(detail.getEid(), 0L) && ObjectUtil.equal(CouponActivityCanGetEnum.CAN_GET.getCode(), canGet)) {
                Date date = new Date();
                CouponActivityEnterpriseGetRulesDO enterpriseGetRules = new CouponActivityEnterpriseGetRulesDO();
                enterpriseGetRules.setCouponActivityId(detail.getId());
                enterpriseGetRules.setBeginTime(detail.getActivityBeginTime());
                enterpriseGetRules.setEndTime(detail.getActivityEndTime());
                enterpriseGetRules.setGiveNum(detail.getGiveNum());
                enterpriseGetRules.setConditionEnterpriseType(detail.getConditionEnterpriseType());
                enterpriseGetRules.setConditionEnterpriseTypeValue(String.join(",", detail.getConditionEnterpriseTypeValueList()));
                enterpriseGetRules.setConditionUserType(detail.getConditionUserType());
                enterpriseGetRules.setCreateUser(request.getOpUserId());
                enterpriseGetRules.setCreateTime(date);
                enterpriseGetRulesService.save(enterpriseGetRules);
            }

            /* 复制关联信息 */


           // 会员全不复制关联商家和关联商品，只复制关联会员
            if (ObjectUtil.equal(MemberCouponActivityStatusEnum.MEMBER_ACTIVITY.getCode(), detail.getMemberType())) {
                QueryWrapper<CouponActivityMemberLimitDO> memberLimitDO= new QueryWrapper<>();
                memberLimitDO.lambda().eq(CouponActivityMemberLimitDO::getCouponActivityId,id).eq(CouponActivityMemberLimitDO::getDelFlag,0);
                List<CouponActivityMemberLimitDO> activityMemberLimitDOS = couponActivityMemberLimitService.list(memberLimitDO);
                if(CollectionUtils.isNotEmpty(activityMemberLimitDOS)){
                    long finalNewId = newId;
                    activityMemberLimitDOS.forEach(item->{
                        item.setCouponActivityId(finalNewId);
                        item.setCreateTime(new Date());
                        item.setCreateUser(userId);
                    });
                    couponActivityMemberLimitService.saveBatch(activityMemberLimitDOS);
                }
            } else if(ObjectUtil.equal(MemberCouponActivityStatusEnum.GOODS_ACTIVITY.getCode(), detail.getMemberType())){
                // 供应商
                LambdaQueryWrapper<CouponActivityEnterpriseLimitDO> enterpriseQueryWrapper = new LambdaQueryWrapper<>();
                enterpriseQueryWrapper.eq(CouponActivityEnterpriseLimitDO::getCouponActivityId, id);
                List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = couponActivityEnterpriseLimitMapper.selectList(enterpriseQueryWrapper);
                if (CollUtil.isNotEmpty(enterpriseLimitList)) {
                    enterpriseLimitList.forEach(e -> {
                        e.setCouponActivityId(couponActivityDO.getId());
                    });
                    couponActivityEnterpriseLimitService.insertBatch(enterpriseLimitList);
                }
                // 商品
                LambdaQueryWrapper<CouponActivityGoodsLimitDO> goodsQueryWrapper = new LambdaQueryWrapper<>();
                goodsQueryWrapper.eq(CouponActivityGoodsLimitDO::getCouponActivityId, id);
                List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitMapper.selectList(goodsQueryWrapper);
                if (CollUtil.isNotEmpty(goodsLimitList)) {
                    goodsLimitList.forEach(g -> {
                        g.setCouponActivityId(couponActivityDO.getId());
                    });
                    couponActivityGoodsLimitService.insertBatch(goodsLimitList);
                }
            }

        } catch (Exception e) {
            log.error("复制优惠券活动失败，id -> {}, exception -> {}", id, e);
            throw new BusinessException(ResultCode.FAILED, e.getMessage());
        }
        return detail;
    }

    @Override
    public Boolean stop(CouponActivityOperateRequest request) {
        Long id = request.getId();
        try {
            CouponActivityDO couponActivityDO = this.getById(id);
            if (!ObjectUtil.equal(request.getEid(), couponActivityDO.getEid())) {
                throw new BusinessException(CouponActivityErrorCode.BUSINESS_COUPON_AUTHORIZATION_ERROR);
            }

            Integer status = couponActivityDO.getStatus();
            if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), status)) {
                log.info("此优惠券活动状态不是“启用”，不能操作“停用”, id -> {}", id);
                throw new BusinessException(CouponActivityErrorCode.STOP_ERROR);
            }
            LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CouponActivityDO::getId, id);
            CouponActivityDO entity = new CouponActivityDO();
            entity.setStatus(CouponActivityStatusEnum.DISABLED.getCode());
            this.update(entity, queryWrapper);
        } catch (Exception e) {
            log.error("停用优惠券活动失败，id -> {}, exception -> {}", id, e);
            throw new BusinessException(ResultCode.FAILED, e.getMessage());
        }
        return true;
    }

    @Override
    @GlobalTransactional
    public Boolean scrap(CouponActivityOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getOpUserId();

        CouponActivityDO couponActivityDO = this.getById(id);
        if (!ObjectUtil.equal(request.getEid(), couponActivityDO.getEid())) {
            throw new BusinessException(CouponActivityErrorCode.BUSINESS_COUPON_AUTHORIZATION_ERROR);
        }

        Integer status = couponActivityDO.getStatus();
        if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
            log.info("此优惠券活动状态是“废弃”，不能再操作“作废”, id -> {}", id);
            throw new BusinessException(CouponActivityErrorCode.SCRAP_ERROR);
        }
        LambdaQueryWrapper<CouponActivityDO> activityQueryWrapper = new LambdaQueryWrapper<>();
        activityQueryWrapper.eq(CouponActivityDO::getId, id);
        CouponActivityDO activityEntity = new CouponActivityDO();
        activityEntity.setStatus(CouponActivityStatusEnum.SCRAP.getCode());
        this.update(activityEntity, activityQueryWrapper);
        // 作废用户卡包中的优惠券
        return scrapUserCoupon(id, userId);
    }

    @Override
    public List<Long> scrapAndReturnCoupon(CouponActivityOperateRequest request) {
        return this.baseMapper.scrapAndReturnCoupon(request.getId());
    }

    @Override
    @GlobalTransactional
    public Boolean increase(CouponActivityIncreaseRequest request) {
        Long id = request.getId();
        Long userId = request.getOpUserId();
        Integer quantity = request.getQuantity();

        CouponActivityDO couponActivityDO = this.getById(id);
        if (ObjectUtil.isNull(couponActivityDO)) {
            log.info("此优惠券活动信息不存在，不能操作“增券”, id -> {}", id);
            throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
        }
        if (!ObjectUtil.equal(request.getEid(), couponActivityDO.getEid())) {
            throw new BusinessException(CouponActivityErrorCode.BUSINESS_COUPON_AUTHORIZATION_ERROR);
        }

        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), couponActivityDO.getStatus())) {
            log.info("此优惠券活动不是“启用”状态，不能增券, id -> {}", id);
            throw new BusinessException(CouponErrorCode.INCREASE_STATUS_ERROR);
        }
        // 规则校验
        increaseCheck(couponActivityDO);
        // 增加优惠券总数量
        String lockName = CouponUtil.getLockName("couponActivity".concat(":").concat(id.toString()), "increase");
        String lockId = "";
        int totalCount = 0;
        Date date = new Date();
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                log.info("增券失败：系统繁忙, 加锁失败，请稍等重试, id -> {}", id);
                throw new BusinessException(CouponErrorCode.REDIS_LOCK_INCREASE_DOING_ERROR);
            }
            // 查询最新数量
            CouponActivityDO couponActivityNewest = this.getById(id);
            totalCount = couponActivityNewest.getTotalCount() + quantity;
            // 更新增加后总数量
            LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CouponActivityDO::getId, id);
            CouponActivityDO entity = new CouponActivityDO();
            entity.setTotalCount(totalCount);
            entity.setRemark(request.getRemark());
            entity.setUpdateUser(userId);
            entity.setUpdateTime(date);
            this.update(entity, queryWrapper);
            // 优惠券活动生成券数量表
            CouponActivityIncreaseDO couponActivityIncreaseDO = new CouponActivityIncreaseDO();
            couponActivityIncreaseDO.setCouponActivityId(id);
            couponActivityIncreaseDO.setIncreaseCount(quantity);
            couponActivityIncreaseDO.setTotalCount(totalCount);
            couponActivityIncreaseDO.setCreateUser(userId);
            couponActivityIncreaseDO.setCreateTime(date);
            couponActivityIncreaseService.save(couponActivityIncreaseDO);
        } catch (InterruptedException e) {
            log.error("增券失败：系统异常，id -> {}, exception -> {}", id, e);
            throw new BusinessException(CouponErrorCode.REDIS_LOCK_INCREASE_ERROR, e.getMessage());
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return true;
    }

    private void increaseCheck(CouponActivityDO couponActivityDO) {
        // 当前时间
        long nowTime = System.currentTimeMillis();
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), couponActivityDO.getUseDateType())) {
            Date endTime = couponActivityDO.getEndTime();
            // 修改校验结束时间
            if (ObjectUtil.isNull(endTime) || endTime.getTime() <= nowTime) {
                throw new BusinessException(CouponActivityErrorCode.INCREASE_TIME_ERROR);
            }
        }
    }

    private boolean scrapUserCoupon(Long id, Long userId) {
        LambdaQueryWrapper<CouponDO> couponQueryWrapper = new LambdaQueryWrapper<>();
        couponQueryWrapper.eq(CouponDO::getCouponActivityId, id);
        couponQueryWrapper.eq(CouponDO::getUsedStatus, CouponUsedStatusEnum.NOT_USED.getCode());
        couponQueryWrapper.eq(CouponDO::getStatus, CouponStatusEnum.NORMAL_COUPON.getCode());
        List<CouponDO> couponDOList = couponMapper.selectList(couponQueryWrapper);
        if (CollUtil.isNotEmpty(couponDOList)) {
            List<UpdateCouponRequest> couponDoUpdateList = new ArrayList<>();
            couponDOList.forEach(c -> {
                UpdateCouponRequest couponDoUpdate = new UpdateCouponRequest();
                couponDoUpdate.setId(c.getId());
                couponDoUpdate.setStatus(CouponStatusEnum.SCRAP_COUPON.getCode());
                couponDoUpdate.setUpdateTime(new Date());
                couponDoUpdate.setUpdateUser(userId);
                couponDoUpdateList.add(couponDoUpdate);
            });
            return couponService.updateBatch(couponDoUpdateList);
        }
        return true;
    }

    private LambdaQueryWrapper<CouponActivityDO> getCouponActivityWrapper(QueryCouponActivityRequest request) {
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper<>();
        Long currentEid = request.getCurrentEid();
        if (ObjectUtil.isNotNull(currentEid) && currentEid != 0) {
            // 商家
            queryWrapper.eq(CouponActivityDO::getEid, currentEid);
        } else {
            if (ObjectUtil.isNotNull(request.getEid())) {
                queryWrapper.eq(CouponActivityDO::getEid, request.getEid());
            }
            if (StrUtil.isNotEmpty(request.getEname())) {
                queryWrapper.like(CouponActivityDO::getEname, request.getEname());
            }
        }

        if (ObjectUtil.isNotNull(request.getId()) && request.getId() != 0) {
            queryWrapper.eq(CouponActivityDO::getId, request.getId());
        }
        if (StrUtil.isNotEmpty(request.getName())) {
            queryWrapper.like(CouponActivityDO::getName, request.getName());
        }
        if (StrUtil.isNotEmpty(request.getBudgetCode())) {
            queryWrapper.like(CouponActivityDO::getBudgetCode, request.getBudgetCode());
        }
        if (ObjectUtil.isNotNull(request.getSponsorType()) && request.getSponsorType() != 0) {
            queryWrapper.eq(CouponActivityDO::getSponsorType, request.getSponsorType());
        }
        if (ObjectUtil.isNotNull(request.getStatus()) && request.getStatus() != 0) {
            queryWrapper.eq(CouponActivityDO::getStatus, request.getStatus());
        }
        queryWrapper.ne(CouponActivityDO::getStatus, 4);
        if (ObjectUtil.isNotNull(request.getActivityStatus()) && request.getActivityStatus() != 0) {
            // 活动状态：1-未开始 2-进行中 3-已结束
            Date date = new Date();
            if (request.getActivityStatus() == 1) {
                // 查开始时间大于等于当前时间的
                queryWrapper.gt(CouponActivityDO::getBeginTime, date);
            }
            if (request.getActivityStatus() == 2) {
                // 查开始时间小于等于当前时间的，且结束时间大于当前时间的
                queryWrapper.le(CouponActivityDO::getBeginTime, date);
                queryWrapper.ge(CouponActivityDO::getEndTime, date);
            }
            if (request.getActivityStatus() == 3) {
                // 查结束时间小于等于当前时间的
                queryWrapper.lt(CouponActivityDO::getEndTime, date);
            }
        }

        if (ObjectUtil.isNotNull(request.getCreateBeginTime())) {
            queryWrapper.ge(CouponActivityDO::getCreateTime, DateUtil.beginOfDay(request.getCreateBeginTime()));
        }
        if (ObjectUtil.isNotNull(request.getCreateEndTime())) {
            queryWrapper.le(CouponActivityDO::getCreateTime, DateUtil.endOfDay(request.getCreateEndTime()));
        }
        queryWrapper.orderByDesc(CouponActivityDO::getCreateTime);
        return queryWrapper;
    }

    @Override
    public Long saveOrUpdateBasic(SaveCouponActivityBasicRequest request) {
        String name = request.getName();
        if (StrUtil.isBlank(name)) {
            throw new BusinessException(CouponActivityErrorCode.NAME_NULL_ERROR);
        }

        // 修改校验
        String checkMsg = updateCouponActivityCheck(request);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        // 保存校验
        checkMsg = saveCouponActivityCheck(request);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        /* 优惠券类型 */
        Integer type = request.getType();
        if (ObjectUtil.isNull(type) || type == 0) {
            throw new BusinessException(CouponActivityErrorCode.TYPE_ERROR);
        }
        // 优惠券活动分类
        Integer sponsorType = request.getSponsorType();
        if (ObjectUtil.isNull(sponsorType) || sponsorType == 0) {
            throw new BusinessException(CouponActivityErrorCode.SPONSOR_TYPE_NULL_ERROR);
        }
        // 活动类型，目前只有支付金额满额
        Integer threshold = request.getThreshold();
        if (ObjectUtil.isNull(threshold) || threshold == 0) {
            throw new BusinessException(CouponActivityErrorCode.THRESHOLD_NULL_ERROR);
        }
        if (!ObjectUtil.equal(CouponActivityThresholdEnum.PAYMENT_SATISFY_AMOUNT.getCode(), threshold)) {
            throw new BusinessException(CouponActivityErrorCode.THRESHOLD_TYPE_ERROR);
        }
        // 门槛金额
        BigDecimal thresholdValue = request.getThresholdValue();
        if (ObjectUtil.isNull(thresholdValue) || thresholdValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(CouponActivityErrorCode.THRESHOLD_VALUE_ERROR);
        }
        // 优惠内容（金额/折扣）
        BigDecimal discountValue = request.getDiscountValue();
        if (ObjectUtil.isNull(discountValue) || discountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(CouponActivityErrorCode.DISCOUNT_VALUE_NULL_ERROR);
        }

        // 满减券，支付金额满额，0 < 优惠金额 <= 门槛金额
        if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), type)) {
            // 优惠金额必须小于门槛金额
            if (discountValue.compareTo(thresholdValue) >= 0) {
                throw new BusinessException(CouponActivityErrorCode.DISCOUNT_VALUE_ERROR);
            }
        }
        // 折扣券，支付金额满额，0 < 优惠折扣 < 100
        if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), type)) {
            BigDecimal oneHundred = new BigDecimal("100.00");
            if (discountValue.compareTo(oneHundred) > 1) {
                throw new BusinessException(CouponActivityErrorCode.DISCOUNT_RATIO_VALUE_ERROR);
            }
        }

        /* 费用承担方（1-平台；2-商家；3-共同承担） */
        Integer bear = request.getBear();
        if (ObjectUtil.isNull(bear) || bear == 0) {
            throw new BusinessException(CouponActivityErrorCode.BEAR_ERROR);
        }
        BigDecimal platformRatio = request.getPlatformRatio();
        BigDecimal businessRatio = request.getBusinessRatio();
        BigDecimal oneHundred = new BigDecimal("100");
        if (ObjectUtil.equal(CouponBearTypeEnum.PLATFORM.getCode(), request.getBear())) {
            // 1
            platformRatio = oneHundred;
            businessRatio = BigDecimal.ZERO;
        } else if (ObjectUtil.equal(CouponBearTypeEnum.BUSINESS.getCode(), request.getBear())) {
            // 2
            platformRatio = BigDecimal.ZERO;
            businessRatio = oneHundred;
        } else if (ObjectUtil.equal(CouponBearTypeEnum.TOGETHER.getCode(), request.getBear())) {
            // 3
            if (ObjectUtil.isNull(platformRatio) || BigDecimal.ZERO.compareTo(platformRatio) == 0 || ObjectUtil.isNull(businessRatio)
                || BigDecimal.ZERO.compareTo(businessRatio) == 0) {
                throw new BusinessException(CouponActivityErrorCode.BUSINESS_RATIO_ERROR);
            }
        }

       /* 有效期 用券时间（1-固定时间；2-发放领取后生效） */
        if (ObjectUtil.isNull(request.getUseDateType())) {
            throw new BusinessException(CouponActivityErrorCode.USE_DATE_TYPE_ERROR);
        }
        long nowTime = System.currentTimeMillis();
        // 1
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), request.getUseDateType())) {
            Date beginTime = request.getBeginTime();
            Date endTime = request.getEndTime();
            if (ObjectUtil.isNull(beginTime)) {
                throw new BusinessException(CouponActivityErrorCode.BEGIN_TIME_NULL_ERROR);
            }
            if (ObjectUtil.isNull(endTime)) {
                throw new BusinessException(CouponActivityErrorCode.END_TIME_NULL_ERROR);
            }
            // 保存校验开始时间
            if (ObjectUtil.isNull(request.getId()) && beginTime.getTime() < nowTime) {
                throw new BusinessException(CouponActivityErrorCode.BEGIN_TIME_ERROR);
            }
            // 保存、修改校验结束时间
            if (endTime.getTime() <= beginTime.getTime()) {
                throw new BusinessException(CouponActivityErrorCode.END_TIME_ERROR);
            }
        }
        // 2
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.AFTER_GIVE.getCode(), request.getUseDateType())) {
            Integer expiryDays = request.getExpiryDays();
            if (ObjectUtil.isNull(expiryDays) || expiryDays <= 0) {
                throw new BusinessException(CouponActivityErrorCode.EXPIRY_DAYS_ERROR);
            }
        }

        CouponActivityDO couponActivityDO = PojoUtils.map(request, CouponActivityDO.class);
        couponActivityDO.setPlatformRatio(platformRatio);
        couponActivityDO.setBusinessRatio(businessRatio);
        try {
            return saveOrUpdate(request.getId(), request.getOpUserId(), couponActivityDO, JSONObject.toJSONString(request));
        } catch (Exception e) {
            log.error("SaveCouponActivityBasic error, exception -> {}", e);
            throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED, e.getMessage());
        }
    }

    @Override
    public Long saveOrUpdateBasicForMember(SaveCouponActivityBasicRequest request) {
        BigDecimal platformRatio = request.getPlatformRatio();
        BigDecimal businessRatio = request.getBusinessRatio();
        // 修改校验
        String checkMsg = updateCouponActivityCheck(request);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        // 保存校验
        /*if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), request.getType())) {
            // 最高优惠金额,可以为0或者不填
            BigDecimal discountMax = request.getDiscountMax();
            if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(CouponActivityErrorCode.DISCOUNT_MAX_VALUE_ERROR);
            }
        }*/

        if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), request.getMemberLimit())) {
            // 部分会员券可用，要关联会员方案不能为空
            request.getId();
            QueryWrapper<CouponActivityMemberLimitDO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().eq(CouponActivityMemberLimitDO::getCouponActivityId,request.getId());
            List<CouponActivityMemberLimitDO> memberLimitDOS = couponActivityMemberLimitService.list(objectQueryWrapper);
            if(CollectionUtils.isEmpty(memberLimitDOS)){
                throw new BusinessException(CouponActivityErrorCode.MEMBER_ACTIVITY_RELATIVE_MEMBER_EMPTY);
            }
        }

        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        // 优惠内容（金额/折扣）
        BigDecimal discountValue = request.getDiscountValue();
        if (ObjectUtil.isNotNull(discountValue) && discountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(CouponActivityErrorCode.DISCOUNT_VALUE_NULL_ERROR);
        }

        // 满减券，支付金额满额，0 < 优惠金额 <= 门槛金额
        if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), request.getType())) {
            // 优惠金额必须小于门槛金额
            if (ObjectUtil.isNotNull(request.getThresholdValue()) && ObjectUtil.isNotNull(request.getDiscountValue())) {
                if (discountValue.compareTo(request.getThresholdValue()) >= 0) {
                    throw new BusinessException(CouponActivityErrorCode.DISCOUNT_VALUE_ERROR);
                }
            }
        }
        // 折扣券，支付金额满额，0 < 优惠折扣 < 100
        if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), request.getType())) {
            BigDecimal oneHundred = new BigDecimal("100.00");
            if (discountValue.compareTo(oneHundred) > 1) {
                throw new BusinessException(CouponActivityErrorCode.DISCOUNT_RATIO_VALUE_ERROR);
            }
        }
        /* 有效期 用券时间（1-固定时间；2-发放领取后生效） */

        long nowTime = System.currentTimeMillis();
        // 1
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), request.getUseDateType())) {
            Date beginTime = request.getBeginTime();
            Date endTime = request.getEndTime();
            if (ObjectUtil.isNull(beginTime)) {
                throw new BusinessException(CouponActivityErrorCode.BEGIN_TIME_NULL_ERROR);
            }
            if (ObjectUtil.isNull(endTime)) {
                throw new BusinessException(CouponActivityErrorCode.END_TIME_NULL_ERROR);
            }
            // 保存校验开始时间
            if (ObjectUtil.isNull(request.getId()) && beginTime.getTime() < nowTime) {
                throw new BusinessException(CouponActivityErrorCode.BEGIN_TIME_ERROR);
            }
            // 保存、修改校验结束时间
            if (endTime.getTime() <= beginTime.getTime()) {
                throw new BusinessException(CouponActivityErrorCode.END_TIME_ERROR);
            }
        }
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.AFTER_GIVE.getCode(), request.getUseDateType())) {
            Integer expiryDays = request.getExpiryDays();
            if (ObjectUtil.isNull(expiryDays) || expiryDays <= 0) {
                throw new BusinessException(CouponActivityErrorCode.EXPIRY_DAYS_ERROR);
            }
        }

        CouponActivityDO couponActivityDO = PojoUtils.map(request, CouponActivityDO.class);
        couponActivityDO.setPlatformRatio(platformRatio);
        couponActivityDO.setBusinessRatio(businessRatio);
        try {
            return saveOrUpdate(request.getId(), request.getOpUserId(), couponActivityDO, JSONObject.toJSONString(request));
        } catch (Exception e) {
            log.error("SaveCouponActivityBasic error, exception -> {}", e);
            throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED, e.getMessage());
        }
    }

    @Override
    @GlobalTransactional
    public Long saveOrUpdateRules(SaveCouponActivityRulesRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            log.info("优惠券活动保存或修改使用规则，ID不能为空");
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ID_NULL_ERROR);
        }
        //领券的结束时间不能大于优惠券的结束时间。
        CouponActivityDO couponActivityDb = this.getById(request.getId());
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), couponActivityDb.getUseDateType()) && ObjectUtil.isNotNull(request.getActivityEndTime())) {
            if (request.getActivityEndTime().after(couponActivityDb.getEndTime())) {
                throw new BusinessException(CouponErrorCode.COUPON_GET_ENDTIME_AFTER_ACTIVITY_ENDTIME);
            }
        }
        // 修改校验
        SaveCouponActivityBasicRequest baseRequest = PojoUtils.map(request, SaveCouponActivityBasicRequest.class);
        String checkMsg = updateCouponActivityCheck(baseRequest);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        /* 平台限制（1-全部平台；2-部分平台） */
        Integer platformLimit = request.getPlatformLimit();
        if (ObjectUtil.isNull(platformLimit) || platformLimit == 0) {
            throw new BusinessException(CouponErrorCode.PLATFORM_LIMIT_ERROR);
        }
        // 2
        String platformSelected = "";
        if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), platformLimit)) {
            if (CollUtil.isEmpty(request.getPlatformSelectedList())) {
                throw new BusinessException(CouponErrorCode.PLATFORM_LIMIT_VALUE_ERROR);
            }
            request.getPlatformSelectedList().forEach(platform -> {
                if(!CouponPlatformTypeEnum.isFitWithNotLimit(Integer.parseInt(platform))){
                    throw new BusinessException(CouponErrorCode.PLATFORM_LIMIT_TYPE_ERROR);
                }
            });
            platformSelected = String.join(",", request.getPlatformSelectedList());
        }

        /* 支付方式限制（1-全部支付方式；2-部分支付方式） */
        if (ObjectUtil.isNull(request.getPayMethodLimit()) || request.getPayMethodLimit() == 0) {
            throw new BusinessException(CouponErrorCode.PAY_METHOD_LIMIT_ERROR);
        }
        // 2
        String payMethodSelected = "";
        if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getPayMethodLimit())) {
            if (CollUtil.isEmpty(request.getPayMethodSelectedList())) {
                throw new BusinessException(CouponErrorCode.PAY_METHOD_LIMIT_VALUE_ERROR);
            }
            request.getPayMethodSelectedList().forEach(payMethod -> {
                if(!CouponPayMethodTypeEnum.isFitWithNotLimit(Integer.parseInt(payMethod))){
                    throw new BusinessException(CouponErrorCode.PAY_METHOD_LIMIT_TYPE_ERROR);
                }
            });
            payMethodSelected = String.join(",", request.getPayMethodSelectedList());
        }

        /* 可叠加促销列表（1-满赠） */
        String coexistPromotion = "";
        if (CollUtil.isNotEmpty(request.getCoexistPromotionList())) {
            coexistPromotion = String.join(",", request.getCoexistPromotionList());
        }

        /* 可用商家 运营后台 */
        if (ObjectUtil.equal(request.getEid(), 0L)) {
            Integer enterpriseLimit = request.getEnterpriseLimit();
            if (ObjectUtil.isNull(enterpriseLimit) || enterpriseLimit == 0) {
                throw new BusinessException(CouponActivityErrorCode.TENTERPRISE_LIMIT_ERROR);
            }
            // 部分可用供应商
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), enterpriseLimit)) {
                // 查询可用供应商是否存在
                String errorMsg = checkEnterpriseLimit(request.getId());
                if (StrUtil.isNotBlank(errorMsg)) {
                    throw new BusinessException(CouponErrorCode.TENTERPRISE_LIMIT_NULL_ERROR);
                }
            }
        }

        /* 优惠券总数量 */
        if (ObjectUtil.isNull(request.getTotalCount()) || request.getTotalCount() == 0) {
            throw new BusinessException(CouponActivityErrorCode.TOTAL_COUNT_ERROR);
        }

        /* 可用商品 */
        Integer goodsLimit = request.getGoodsLimit();
        if (ObjectUtil.isNull(goodsLimit) || goodsLimit == 0) {
            throw new BusinessException(CouponActivityErrorCode.TENTERPRISE_LIMIT_ERROR);
        }
        // 部分可用商品
        if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), goodsLimit)) {
            // 查询可用商品是否存在
            List<CouponActivityGoodsLimitDO> goodsLimits = couponActivityGoodsLimitService.getByCouponActivityId(request.getId());
            if (CollUtil.isEmpty(goodsLimits)) {
                throw new BusinessException(CouponErrorCode.GOODS_LIMIT_NULL_ERROR);
            }
        }

        // 商家后台，用户领取设置
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            buildEnterpriseGetRules(request);
        }

        CouponActivityDO couponActivityDO = PojoUtils.map(request, CouponActivityDO.class);
        // 选择平台
        couponActivityDO.setPlatformSelected(platformSelected);
        // 支付方式
        couponActivityDO.setPayMethodSelected(payMethodSelected);
        // 可叠加促销列表
        couponActivityDO.setCoexistPromotion(coexistPromotion);

        try {

            // 运营后台商家设置,为部分供应商、部分商品可用，校验商品所属企业是否在已选择商家列表中
            if (ObjectUtil.equal(request.getEid(), 0L) && ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getEnterpriseLimit())
                    && ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getGoodsLimit())) {
                checkGoodsLimitEnterprise(request.getId());
            }
            Date date = new Date();
            // 选择全部商家可用，删除之前设置的部分商家(运营后台)
            if (ObjectUtil.equal(request.getEid(), 0L) && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), request.getEnterpriseLimit())) {
                DeleteCouponActivityEnterpriseLimitRequest enterpriseLimitRequest = new DeleteCouponActivityEnterpriseLimitRequest();
                enterpriseLimitRequest.setCouponActivityId(request.getId());
                enterpriseLimitRequest.setOpUserId(request.getOpUserId());
                enterpriseLimitRequest.setOpTime(date);
                couponActivityEnterpriseLimitService.deleteByCouponActivityId(enterpriseLimitRequest);
            }
            // 选择全部商品可用，删除之前设置的部分商品(运营后台、商家后台)
            if (ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), goodsLimit)) {
                DeleteCouponActivityGoodsLimitRequest goodsLimitRequest = new DeleteCouponActivityGoodsLimitRequest();
                goodsLimitRequest.setCouponActivityId(request.getId());
                goodsLimitRequest.setOpUserId(request.getOpUserId());
                goodsLimitRequest.setOpTime(date);
                couponActivityGoodsLimitService.deleteByCouponActivityId(goodsLimitRequest);
            }

            return saveOrUpdate(request.getId(), request.getOpUserId(), couponActivityDO, JSONObject.toJSONString(request));
        } catch (Exception e) {
            throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED, e.getMessage());
        }
    }

    public void checkGoodsLimitEnterprise(long couponActivityId) {
        List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = couponActivityEnterpriseLimitService.getByCouponActivityId(couponActivityId);
        if (CollUtil.isEmpty(enterpriseLimitList)) {
            throw new BusinessException(CouponErrorCode.TENTERPRISE_LIMIT_NULL_ERROR);
        }
        List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getByCouponActivityId(couponActivityId);
        if (CollUtil.isEmpty(goodsLimitList)) {
            throw new BusinessException(CouponErrorCode.GOODS_LIMIT_NULL_ERROR);
        }

        Map<Long, CouponActivityEnterpriseLimitDO> enterpriseLimitMap = enterpriseLimitList.stream()
                .collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
        // 校验商品所属企业是否在已选择商家列表中
        for (CouponActivityGoodsLimitDO goodsLimit : goodsLimitList) {
            CouponActivityEnterpriseLimitDO enterpriseLimit = enterpriseLimitMap.get(goodsLimit.getEid());
            if (ObjectUtil.isNull(enterpriseLimit)) {
                String errorMsg = MessageFormat.format(CouponErrorCode.GOODS_LIMIT_ENTERPRISE_ERROR.getMessage(), goodsLimit.getGoodsId().toString(),
                        goodsLimit.getEid().toString());
                throw new BusinessException(CouponErrorCode.GOODS_LIMIT_ENTERPRISE_ERROR, errorMsg);
            }
        }
    }

    public String checkEnterpriseLimit(Long couponActivityId) {
        List<CouponActivityEnterpriseLimitDO> enterpriseLimits = couponActivityEnterpriseLimitService.getByCouponActivityId(couponActivityId);
        if (CollUtil.isEmpty(enterpriseLimits)) {
            return "商家设置为部分商家可用时，添加供应商不能为空";
        }
        return null;
    }

    private void buildEnterpriseGetRules(SaveCouponActivityRulesRequest request) {
        CouponActivityEnterpriseGetRulesDO enterpriseGetRules = null;
        Integer canGet = request.getCanGet();

        if (ObjectUtil.isNull(canGet) || canGet == 0) {
            throw new BusinessException(CouponActivityErrorCode.CAN_GET_SELECT_NULL_ERROR);
        }

        // 查询用户领取规则
        CouponActivityEnterpriseGetRulesDO enterpriseGetRulesDb = enterpriseGetRulesService.getByCouponActivityId(request.getId());

        if (ObjectUtil.equal(CouponActivityCanGetEnum.CAN_NOT_GET.getCode(), canGet)) {
            if (ObjectUtil.isNotNull(enterpriseGetRulesDb)) {
                // 删除已设置的领券规则
                enterpriseGetRules = new CouponActivityEnterpriseGetRulesDO();
                enterpriseGetRules.setId(enterpriseGetRulesDb.getId());
                enterpriseGetRules.setOpUserId(request.getOpUserId());
                enterpriseGetRules.setOpTime(new Date());
                enterpriseGetRulesService.deleteById(enterpriseGetRules);
            }
            return;
        } else {
            long nowTime = System.currentTimeMillis();
            // 活动起止时间
            Date activityBeginTime = request.getActivityBeginTime();
            if (ObjectUtil.isNull(activityBeginTime) || activityBeginTime.getTime() < nowTime) {
                throw new BusinessException(CouponActivityErrorCode.CAN_GET_BEGIN_TIME_ERROR);
            }
            Date activityEndTime = request.getActivityEndTime();
            if (ObjectUtil.isNull(activityBeginTime) || activityEndTime.getTime() < activityBeginTime.getTime()) {
                throw new BusinessException(CouponActivityErrorCode.CAN_GET_END_TIME_ERROR);
            }
            // 可领取数量
            Integer giveNum = request.getGiveNum();
            if (ObjectUtil.isNull(giveNum) || giveNum == 0) {
                throw new BusinessException(CouponActivityErrorCode.CAN_GET_GIVE_NUM_NULL);
            }
            /* 企业类型（1-全部企业类型；2-部分企业类型） */
            String enterpriseTypeSelected = "";
            Integer conditionEnterpriseType = request.getConditionEnterpriseType();
            if (ObjectUtil.isNull(conditionEnterpriseType) || conditionEnterpriseType == 0) {
                throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_LIMIT_ERROR);
            }
            // 2
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), conditionEnterpriseType)) {
                if (CollUtil.isEmpty(request.getConditionEnterpriseTypeValueList())) {
                    throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_VALUE_ERROR);
                }
                enterpriseTypeSelected = String.join(",", request.getConditionEnterpriseTypeValueList());
            }
            // 用户类型
            Integer conditionUserType = request.getConditionUserType();
            if (ObjectUtil.isNull(conditionUserType) || conditionUserType == 0) {
                throw new BusinessException(CouponErrorCode.USER_TYPE_LIMIT_ERROR);
            }
            // 查询可用供应商是否存在
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), conditionUserType)) {
                String errorMsg = checkEnterpriseLimit(request.getId());
                if (StrUtil.isNotBlank(errorMsg)) {
                    throw new BusinessException(CouponErrorCode.TENTERPRISE_LIMIT_NULL_ERROR, "用户类型为部分用户时，添加用户不能为空");
                }
            }

            Date date = new Date();
            enterpriseGetRules = new CouponActivityEnterpriseGetRulesDO();
            enterpriseGetRules.setCouponActivityId(request.getId());
            enterpriseGetRules.setBeginTime(activityBeginTime);
            enterpriseGetRules.setEndTime(activityEndTime);
            enterpriseGetRules.setGiveNum(giveNum);
            enterpriseGetRules.setConditionEnterpriseType(conditionEnterpriseType);
            enterpriseGetRules.setConditionEnterpriseTypeValue(enterpriseTypeSelected);
            enterpriseGetRules.setConditionUserType(conditionUserType);
            if (ObjectUtil.isNotNull(enterpriseGetRulesDb)) {
                enterpriseGetRules.setId(enterpriseGetRulesDb.getId());
                enterpriseGetRules.setUpdateUser(request.getOpUserId());
                enterpriseGetRules.setUpdateTime(date);
                enterpriseGetRulesService.updateById(enterpriseGetRules);
                return;
            } else {
                enterpriseGetRules.setCreateUser(request.getOpUserId());
                enterpriseGetRules.setCreateTime(date);
                enterpriseGetRulesService.save(enterpriseGetRules);
                return;
            }
        }
    }

    @Override
    public Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request) {
        Page<CouponActivityGiveDTO> page = request.getPage();
        Long couponActivityId = request.getCouponActivityId();
        Long couponActivityAutoId = request.getCouponActivityAutoId();
        Integer getType = request.getGetType();
        if (ObjectUtil.isNull(couponActivityId) && ObjectUtil.isNull(couponActivityAutoId)) {
            return page;
        }
        if (ObjectUtil.isNull(getType)) {
            throw new BusinessException(CouponErrorCode.AUTO_GET_TYPE_NULL);
        }

        try {

            // 查询优惠券
            QueryCouponPageRequest couponPageRequest = new QueryCouponPageRequest();
            couponPageRequest.setCurrent(request.getCurrent());
            couponPageRequest.setSize(request.getSize());
            Page<CouponDTO> couponPage = request.getPage();

           // 已发放优惠券
            List<Long> couponActivityIdList = new ArrayList<>();
            if (ObjectUtil.isNotNull(couponActivityId)) {
                // 已发放，运营后台查所有优惠券、商家后台查自己的优惠券
                CouponActivityDO couponActivity = this.getById(couponActivityId);
                if (ObjectUtil.isNull(couponActivity)) {
                    return page;
                }

                couponActivityIdList.add(couponActivityId);
                // 查询优惠券参数
                couponPageRequest.setCouponActivityIdList(new ArrayList(){{add(couponActivityId);}});
            } else if (ObjectUtil.isNotNull(couponActivityAutoId)) {
                if (ObjectUtil.equal(CouponGetTypeEnum.AUTO_GIVE.getCode(), getType)) {
                    // 已发放，自动
                    // 查询自动发放活动
                    CouponActivityAutoGiveDO autoGive = autoGiveService.getById(couponActivityAutoId);
                    if (ObjectUtil.isNull(autoGive)) {
                        return page;
                    }
                    // 自动发放关联优惠券
                    List<CouponActivityAutoGiveCouponDO> autoGiveCouponList = autoGiveCouponService
                        .getByCouponActivityAutoGiveId(couponActivityAutoId);
                    if (CollUtil.isEmpty(autoGiveCouponList)) {
                        return page;
                    }
                    // 查询自动发放优惠券
                    List<Long> couponActivityIds = autoGiveCouponList.stream().map(CouponActivityAutoGiveCouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
                    couponActivityIdList.addAll(couponActivityIds);
                    // 查询优惠券参数
                    couponPageRequest.setCouponActivityIdList(couponActivityIds);
                    couponPageRequest.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
                    couponPageRequest.setCouponActivityAutoId(couponActivityAutoId);
                }
                if (ObjectUtil.equal(CouponGetTypeEnum.AUTO_GET.getCode(), getType)) {
                    // 已发放，自主领取
                    CouponActivityAutoGetDO autoGet = autoGetService.getById(couponActivityAutoId);
                    if (ObjectUtil.isNull(autoGet)) {
                        return page;
                    }
                    // 自主领取关联优惠券
                    List<CouponActivityAutoGetCouponDO> autoGetCouponList = autoGetCouponService.getByCouponActivityGetId(couponActivityAutoId);
                    if (CollUtil.isEmpty(autoGetCouponList)) {
                        return page;
                    }
                    // 查询自主领取优惠券
                    List<Long> couponActivityIds = autoGetCouponList.stream().map(CouponActivityAutoGetCouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
                    couponActivityIdList.addAll(couponActivityIds);
                    // 查询优惠券参数
                    couponPageRequest.setCouponActivityIdList(couponActivityIds);
                    couponPageRequest.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
                    couponPageRequest.setCouponActivityAutoId(couponActivityAutoId);
                }
            }

            if (CollUtil.isEmpty(couponActivityIdList)) {
                return page;
            }
            // 查询优惠券活动信息
            List<CouponActivityDetailDTO> couponActivityList = this.getCouponActivityById(couponActivityIdList);
            if (CollUtil.isEmpty(couponActivityList)) {
                throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST_ERROR);
            }
            // 优惠规则
            Map<Long, CouponActivityDetailDTO> couponRulesMap = couponActivityList.stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));

            // 查询优惠券
            couponPage = couponService.queryListPage(couponPageRequest);
            if (ObjectUtil.isNull(couponPage) || CollUtil.isEmpty(couponPage.getRecords())) {
                return page;
            }
            // 组装已发放优惠券信息
            page = PojoUtils.map(couponPage, CouponActivityGiveDTO.class);
            List<CouponActivityGiveDTO> list = new ArrayList<>();
            CouponActivityGiveDTO couponActivityGive;
            for (CouponDTO coupon : couponPage.getRecords()) {
                couponActivityGive = PojoUtils.map(coupon, CouponActivityGiveDTO.class);
                CouponActivityDetailDTO couponActivityDetail = couponRulesMap.get(coupon.getCouponActivityId());
                String effectiveTime = "";
                if (ObjectUtil.isNotNull(couponActivityDetail)) {
                    CouponActivityDTO dto = buildCouponActivityDtoForCouponRules(couponActivityDetail);
                    couponActivityGive.setCouponRules(buildCouponRules(dto));
                    effectiveTime = buildEffectiveTime(couponActivityDetail);
                }
                couponActivityGive.setEffectiveTime(effectiveTime);
                list.add(couponActivityGive);
            }
            page.setRecords(list);
        } catch (Exception e) {
            log.error("查询已发放优惠券异常, couponActivityId -> {}, exception -> {}", couponActivityId, e);
            throw new BusinessException(CouponErrorCode.COUPON_QUERY_GIVE_ERROR, e.getMessage());
        }
        return page;
    }

    @Override
    public List<CouponActivityDTO> getAvailableActivity(List<Long> request) {
        List<CouponActivityDO>result=this.baseMapper.getAvailableActivity(request);
        return PojoUtils.map(result,CouponActivityDTO.class);
    }

    public String buildEffectiveTime(CouponActivityDetailDTO couponActivity) {
        Integer useDateType = couponActivity.getUseDateType();
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
            Date beginTime = couponActivity.getBeginTime();
            Date endTime = couponActivity.getEndTime();
            String begin = DateUtil.format(beginTime, "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");
            return begin.concat(" - ").concat(end);
        } else {
            Integer expiryDays = couponActivity.getExpiryDays();
            return "按发放/领取".concat(expiryDays.toString()).concat("天过期");
        }
    }


    @Override
    public List<CouponActivityDetailDTO> getCouponActivityById(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(CouponActivityDO::getId, ids);
        return PojoUtils.map(this.list(queryWrapper), CouponActivityDetailDTO.class);
    }

    @Override
    public List<CouponActivityDO> getEffectiveCouponActivityByIdList(List<Long> ids, int totalCountFlag, int statusFlag) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(CouponActivityDO::getId, ids);
        if (statusFlag == 0) {
            queryWrapper.eq(CouponActivityDO::getStatus, CouponActivityStatusEnum.ENABLED.getCode());
        } else if (statusFlag == 1) {
            queryWrapper.in(CouponActivityDO::getStatus,
                Arrays.asList(CouponActivityStatusEnum.ENABLED.getCode(), CouponActivityStatusEnum.DISABLED.getCode()));
        }
        List<CouponActivityDO> list = this.list(queryWrapper);
        // 已发放数量
        List<Map<String, Long>> giveCountList = couponService.getGiveCountByCouponActivityId(ids);
        if (CollUtil.isNotEmpty(list) && totalCountFlag == 0 && CollUtil.isNotEmpty(giveCountList)) {
            Map<Long, Integer> giveCountMap = new HashMap<>();
            for (Map<String, Long> map : giveCountList) {
                giveCountMap.put(map.get("couponActivityId").longValue(), map.get("giveCount").intValue());
            }
            Iterator<CouponActivityDO> it = list.iterator();
            while (it.hasNext()) {
                CouponActivityDO couponActivity = it.next();
                Integer giveCount = giveCountMap.get(couponActivity.getId());
                if (ObjectUtil.isNull(giveCount) || giveCount < 0) {
                    giveCount = 0;
                }
                Integer totalCount = couponActivity.getTotalCount();
                if (ObjectUtil.isNull(totalCount) || totalCount < 0) {
                    it.remove();
                }
                if (totalCount <= giveCount) {
                    it.remove();
                }
            }
        }
        return list;
    }

    @Override
    public CouponActivityResidueCountDTO getResidueCount(Long couponActivityId) {
        CouponActivityDO couponActivity = this.getById(couponActivityId);
        if (ObjectUtil.isNull(couponActivity)) {
            throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST_ERROR);
        }
        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), couponActivity.getStatus())) {
            throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_STATUS_AUTO_GIVE_ERROR);
        }
        // 平台自主领取不能关联商家券
        if(ObjectUtil.isNotNull(couponActivity.getEid()) && couponActivity.getEid() != 0){
            throw new BusinessException(CouponErrorCode.COUPON_ACTIVITY_STATUS_AUTO_GIVE_ERROR);
        }
        // 总数量
        Integer totalCount = couponActivity.getTotalCount();
        if (ObjectUtil.isNull(totalCount) || totalCount < 0) {
            totalCount = 0;
        }
        // 查询已发放优惠券数量
        Integer effectiveCount = couponService.getEffectiveCountByCouponActivityId(couponActivityId);
        if (ObjectUtil.isNull(effectiveCount)) {
            effectiveCount = 0;
        }
        // 可用供应商类型，商家处理
        int enterpriseLimit = couponActivity.getEnterpriseLimit();
        if (ObjectUtil.isNotNull(couponActivity.getEid()) && couponActivity.getEid().intValue() != 0) {
            enterpriseLimit = 2;
        }
        CouponActivityResidueCountDTO couponActivityResidueCount = new CouponActivityResidueCountDTO();
        couponActivityResidueCount.setId(couponActivity.getId());
        couponActivityResidueCount.setEnterpriseLimit(enterpriseLimit);
        couponActivityResidueCount.setResidueCount(totalCount - effectiveCount);
        return couponActivityResidueCount;
    }

    @Override
    public List<CouponActivityDO> getEffectiveListWithoutEnterpriseLimit() {
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(CouponActivityDO::getEnterpriseLimit, CouponLimitTypeEnum.ALL.getCode());
        queryWrapper.eq(CouponActivityDO::getGoodsLimit, CouponLimitTypeEnum.ALL.getCode());
        queryWrapper.eq(CouponActivityDO::getStatus, CouponActivityStatusEnum.ENABLED.getCode());
        List<CouponActivityDO> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            list = list.stream().filter(c -> c.getTotalCount() > 0).collect(Collectors.toList());

            // 已生成券数量
            List<Long> idList = list.stream().map(CouponActivityDO::getId).distinct().collect(Collectors.toList());
            Map<Long, Integer> giveCountMap = new HashMap<>();
            List<Map<String, Long>> giveCountList = couponService.getGiveCountByCouponActivityId(idList);
            if (CollUtil.isNotEmpty(giveCountList)) {
                for (Map<String, Long> map : giveCountList) {
                    giveCountMap.put(map.get("couponActivityId").longValue(), map.get("giveCount").intValue());
                }
            }
            Iterator<CouponActivityDO> iterator = list.iterator();
            while (iterator.hasNext()) {
                CouponActivityDO couponActivity = iterator.next();
                Integer totalCount = couponActivity.getTotalCount();
                Integer count = giveCountMap.get(couponActivity.getId());
                if (ObjectUtil.isNull(count) || count < 0) {
                    count = 0;
                }
                if (count >= totalCount) {
                    iterator.remove();
                    continue;
                }
            }
        }
        return list;
    }

    @Override
    public CouponActivityDTO buildCouponActivityDtoForCouponRules(CouponActivityDetailDTO couponActivityDetail) {
        CouponActivityDTO dto = new CouponActivityDTO();
        dto.setType(couponActivityDetail.getType());
        dto.setMemberType(couponActivityDetail.getMemberType());
        dto.setThresholdValue(couponActivityDetail.getThresholdValue());
        dto.setDiscountValue(couponActivityDetail.getDiscountValue());
        dto.setDiscountMax(couponActivityDetail.getDiscountMax());
        return dto;
    }

    @Override
    public Map<Long, Integer> getCouponHasGetCountMap(Long eid, List<Long> idAllList) {
        Map<Long, Integer> couponHasGetCountMap = new HashMap<>();
        List<CouponDO> couponList = couponService.getByEidAndCouponActivityId(eid, idAllList);
        Map<Long, List<CouponDO>> couponMap = new HashMap<>();
        if (CollUtil.isNotEmpty(couponList)) {
            // 查询有效的平台领取活动
            List<Long> platformCouponActivityIdList = couponList.stream()
                .filter(c -> ObjectUtil.isNotNull(c.getCouponActivityAutoId()) && c.getCouponActivityAutoId().intValue() != 0)
                .map(CouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(platformCouponActivityIdList)){
                List<CouponActivityAutoGetCouponDTO> platformAutoGetCouponList = autoGetCouponService.getByCouponActivityIdList(platformCouponActivityIdList);
                if (CollUtil.isNotEmpty(platformAutoGetCouponList)) {
                    // 有效的自主领取活动
                    List<Long> autoGetIdList = platformAutoGetCouponList.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityAutoGetId).distinct().collect(Collectors.toList());
                    List<CouponActivityAutoGetDTO> autoGetList = autoGetService.getAutoGetByIdList(autoGetIdList);
                    if(CollUtil.isNotEmpty(autoGetList)){
                        List<Long> autoGetIdEffectiveList = autoGetList.stream().map(CouponActivityAutoGetDTO::getId).distinct().collect(Collectors.toList());
                        List<CouponDO> platformCouponList = couponList.stream().filter(c -> autoGetIdEffectiveList.contains(c.getCouponActivityAutoId())).collect(Collectors.toList());
                        Map<Long, List<CouponDO>> platformCouponMap = platformCouponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
                        couponMap.putAll(platformCouponMap);
                    }
                }
            }
            // 查询有效的商家领取活动
            List<Long> businessCouponActivityIdList = couponList.stream()
                    .filter(c -> ObjectUtil.isNotNull(c.getCouponActivityBusinessAutoId()) && c.getCouponActivityBusinessAutoId().intValue() != 0)
                    .map(CouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(businessCouponActivityIdList)){
                List<CouponActivityEnterpriseGetRulesDO> enterpriseGetRulesList = enterpriseGetRulesService
                        .getByCouponActivityIdList(businessCouponActivityIdList);
                if(CollUtil.isNotEmpty(enterpriseGetRulesList)){
                    List<Long> getRulesIdEffectiveList = enterpriseGetRulesList.stream().map(CouponActivityEnterpriseGetRulesDO::getId).distinct().collect(Collectors.toList());
                    List<CouponDO> businessCouponList = couponList.stream().filter(c -> getRulesIdEffectiveList.contains(c.getCouponActivityBusinessAutoId())).collect(Collectors.toList());
                    Map<Long, List<CouponDO>> businessCouponMap = businessCouponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
                    couponMap.putAll(businessCouponMap);
                }
            }

            couponMap.entrySet().forEach(entry -> {
                couponHasGetCountMap.put(entry.getKey(), entry.getValue().size());
            });
        }
        return couponHasGetCountMap;
    }

    @Override
    public Map<Long, List<CouponDO>> getCouponHasGetCount(Long eid, List<Long> idAllList) {
        Map<Long, List<CouponDO>> couponHasGetCountMap = new HashMap<>();
        List<CouponDO> couponList = couponService.getByEidAndCouponActivityId(eid, idAllList);
        Map<Long, List<CouponDO>> couponMap = new HashMap<>();
        if (CollUtil.isNotEmpty(couponList)) {
            // 查询有效的平台领取活动
            List<Long> platformCouponActivityIdList = couponList.stream()
                    .filter(c -> ObjectUtil.isNotNull(c.getCouponActivityAutoId()) && c.getCouponActivityAutoId().intValue() != 0)
                    .map(CouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(platformCouponActivityIdList)){
                List<CouponActivityAutoGetCouponDTO> platformAutoGetCouponList = autoGetCouponService.getByCouponActivityIdList(platformCouponActivityIdList);
                if (CollUtil.isNotEmpty(platformAutoGetCouponList)) {
                    // 有效的自主领取活动
                    List<Long> autoGetIdList = platformAutoGetCouponList.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityAutoGetId).distinct().collect(Collectors.toList());
                    List<CouponActivityAutoGetDTO> autoGetList = autoGetService.getAutoGetByIdList(autoGetIdList);
                    if(CollUtil.isNotEmpty(autoGetList)){
                        List<Long> autoGetIdEffectiveList = autoGetList.stream().map(CouponActivityAutoGetDTO::getId).distinct().collect(Collectors.toList());
                        List<CouponDO> platformCouponList = couponList.stream().filter(c -> autoGetIdEffectiveList.contains(c.getCouponActivityAutoId())).collect(Collectors.toList());
                        Map<Long, List<CouponDO>> platformCouponMap = platformCouponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
                        couponMap.putAll(platformCouponMap);
                    }
                }
            }
            // 查询有效的商家领取活动
            List<Long> businessCouponActivityIdList = couponList.stream()
                    .filter(c -> ObjectUtil.isNotNull(c.getCouponActivityBusinessAutoId()) && c.getCouponActivityBusinessAutoId().intValue() != 0)
                    .map(CouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(businessCouponActivityIdList)){
                List<CouponActivityEnterpriseGetRulesDO> enterpriseGetRulesList = enterpriseGetRulesService
                        .getByCouponActivityIdList(businessCouponActivityIdList);
                if(CollUtil.isNotEmpty(enterpriseGetRulesList)){
                    List<Long> getRulesIdEffectiveList = enterpriseGetRulesList.stream().map(CouponActivityEnterpriseGetRulesDO::getId).distinct().collect(Collectors.toList());
                    List<CouponDO> businessCouponList = couponList.stream().filter(c -> getRulesIdEffectiveList.contains(c.getCouponActivityBusinessAutoId())).collect(Collectors.toList());
                    Map<Long, List<CouponDO>> businessCouponMap = businessCouponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
                    couponMap.putAll(businessCouponMap);
                }
            }

            couponMap.entrySet().forEach(entry -> {
                couponHasGetCountMap.put(entry.getKey(), entry.getValue());
            });
        }
        return couponHasGetCountMap;
    }

    @Override
    public Map<Long, Integer> getCanGetNumMap(List<CouponActivityDO> couponActivityList) {
        Map<Long, Integer> canGetNumMap = new HashMap<>();
        List<Long> platformIdList = new ArrayList<>();
        List<Long> businessIdList = new ArrayList<>();
        couponActivityList.forEach(couponActivityDO -> {
            if (ObjectUtil.isNotNull(couponActivityDO.getEid()) && couponActivityDO.getEid().intValue() == 0) {
                platformIdList.add(couponActivityDO.getId());
            }
            if (ObjectUtil.isNotNull(couponActivityDO.getEid()) && couponActivityDO.getEid().intValue() != 0) {
                businessIdList.add(couponActivityDO.getId());
            }
        });

        // 平台券可领取数量。优惠券id关联到marketing_coupon_activity_auto_get_coupon（关联表id），再找到领券活动id。
        // 过滤掉了，一个优惠券活动，被多个领券活动关联。每次优惠券只能被一个生效的领券活动绑定
        List<CouponActivityAutoGetCouponDTO> platformAutoGetCouponList = autoGetCouponService.getByCouponActivityIdList(platformIdList);
        Map<Long, Integer> platformGetNumMap = new HashMap<>();
        if (CollUtil.isNotEmpty(platformAutoGetCouponList)) {
            // 有效的自主领取活动
            List<Long> autoGetIdList = platformAutoGetCouponList.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityAutoGetId).distinct().collect(Collectors.toList());
            List<CouponActivityAutoGetDTO> autoGetList = autoGetService.getAutoGetByIdList(autoGetIdList);
            if(CollUtil.isNotEmpty(autoGetList)){
                List<Long> autoGetIdEffectiveList = autoGetList.stream().map(CouponActivityAutoGetDTO::getId).distinct().collect(Collectors.toList());
                platformGetNumMap = platformAutoGetCouponList
                    .stream().filter(c -> autoGetIdEffectiveList.contains(c.getCouponActivityAutoGetId()) && ObjectUtil.isNotNull(c.getGiveNum())
                                          && c.getGiveNum() > 0)
                    .collect(Collectors.toMap(c -> c.getCouponActivityId(), c -> c.getGiveNum(), (v1, v2) -> v1));
            }
        }
        // 商家券可领取数量
        List<CouponActivityEnterpriseGetRulesDO> businessGetRulesList = enterpriseGetRulesService.getByCouponActivityIdList(businessIdList);
        Map<Long, Integer> businessGetNumMap = new HashMap<>();
        if (CollUtil.isNotEmpty(businessGetRulesList)) {
            businessGetNumMap = businessGetRulesList.stream().filter(c -> ObjectUtil.isNotNull(c.getGiveNum()) && c.getGiveNum() > 0)
                .collect(Collectors.toMap(c -> c.getCouponActivityId(), c -> c.getGiveNum(), (v1, v2) -> v1));
        }
        canGetNumMap.putAll(platformGetNumMap);
        canGetNumMap.putAll(businessGetNumMap);
        return canGetNumMap;
    }

    private Long saveOrUpdate(Long id, Long opUserId, CouponActivityDO couponActivityDO, String requestJson) throws Exception {
        // 优惠券活动操作日志
        Integer logType;
        int logStatus;
        String faileReason = "";

        Date date = new Date();
        // 运营后台没有企业信息，企业信息字段不插入、不更新
        if (Objects.isNull(id) || id == 0) {
            // 保存
            couponActivityDO.setStatus(CouponActivityStatusEnum.ENABLED.getCode());
            couponActivityDO.setCreateUser(opUserId);
            couponActivityDO.setCreateTime(date);
            this.baseMapper.insertCouponActivity(couponActivityDO);

            logType = CouponActivityLogTypeEnum.ADD.getCode();
        } else {
            // 更新
            couponActivityDO.setUpdateUser(opUserId);
            couponActivityDO.setUpdateTime(date);
            this.baseMapper.updateCouponActivityById(couponActivityDO);

            logType = CouponActivityLogTypeEnum.UPDATE.getCode();
        }
        logStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
        couponActivityLogService.insertCouponActivityLog(id, logType, requestJson, logStatus, faileReason, opUserId);
        return couponActivityDO.getId();
    }

    /**
     * 保存优惠券活动基本信息校验
     * @param request
     * @return
     */
    public String saveCouponActivityCheck(SaveCouponActivityBasicRequest request) {
        if (ObjectUtil.isNotNull(request.getId())) {
            return null;
        }
        // 折扣券
        if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), request.getType())) {
            // 最高优惠金额
            BigDecimal discountMax = request.getDiscountMax();
            if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(CouponActivityErrorCode.DISCOUNT_MAX_VALUE_ERROR);
            }
        }
        // 名称是否重复
        /*boolean isNameRepeat = checkNameRepeat(request.getEid(), request.getName());
        if (isNameRepeat) {
            return "此优惠券活动名称已存在，请检查";
        }*/
        return null;
    }

    /**
     * 修改优惠券活动校验
     * @param request
     * @return
     */
    public String updateCouponActivityCheck(SaveCouponActivityBasicRequest request) {
        Long id = request.getId();
        Long eid = request.getEid();
        if (ObjectUtil.isNull(id)) {
            return null;
        }

        // id不为空，开始校验是否允许修改
        // 查询已保存信息
        CouponActivityDO couponActivityDb = this.getById(id);
        // 是否自己创建
        if (!ObjectUtil.equal(eid, couponActivityDb.getEid())) {
            return "不能修改他人创建的优惠券活动";
        }

        Integer status = couponActivityDb.getStatus();
        // 状态：停用、废弃，不可修改
        if (ObjectUtil.equal(CouponActivityStatusEnum.DISABLED.getCode(), status)
            || ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
            log.info("updateCouponActivityCheck false, reason -> {}", "优惠券活动状态：停用、废弃，不可修改");
            return "优惠券活动状态：停用、废弃，不可修改";
        }

        //        Integer useDateType = couponActivityDb.getUseDateType();
        //        // 固定时间
        //        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
        //            Date beginTime = couponActivityDb.getBeginTime();
        //            Date endTime = couponActivityDb.getEndTime();
        //            // 开始时间5分钟内不能修改
        //            if (!checkUpdateAutoGiveFixed(beginTime, endTime, nowTime, Constants.TIME_DIFFERENCE_MINUTE * Constants.TIME_DIFFERENCE_SECOND)
        //                .get("updateFlag")) {
        //                log.error("updateCouponActivityCheck false, reason -> {}", "优惠券活动开始时间<=" + Constants.TIME_DIFFERENCE_MINUTE + "分钟，不能修改");
        //                return "优惠券活动开始时间 <=" + Constants.TIME_DIFFERENCE_MINUTE + "分钟，不能修改";
        //            }
        //        }
        //        // 发放/领取后XX天失效
        //        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.AFTER_GIVE.getCode(), useDateType)) {
        //            // 已关联自动发放活动
        //            CouponActivityAutoGiveCouponDTO autoGiveCoupon = autoGiveCouponService.getByCouponActivityId(id);
        //            if (ObjectUtil.isNotNull(autoGiveCoupon)) {
        //                CouponActivityAutoGiveDTO autoGive = autoGiveService.getAutoGiveById(autoGiveCoupon.getCouponActivityAutoGiveId());
        //                if (!checkAutoGiveAfterGive(autoGive, nowTime, Constants.TIME_DIFFERENCE_MINUTE * Constants.TIME_DIFFERENCE_SECOND)
        //                    .get("updateFlag")) {
        //                    return "自动发放活动，开始时间<=" + Constants.TIME_DIFFERENCE_MINUTE + "分钟，不能修改。[自动发放活动id：" + autoGive.getId() + "]";
        //                }
        //            }
        //
        //            // 已关联自动领取活动
        //            CouponActivityAutoGetCouponDTO autoGetCoupon = autoGetCouponService.getByCouponActivityId(id);
        //            if (ObjectUtil.isNotNull(autoGetCoupon)) {
        //                CouponActivityAutoGetDTO autoGet = autoGetService.getAutoGetById(autoGetCoupon.getCouponActivityAutoGetId());
        //                if (!checkAutoGetAfterGive(autoGet, nowTime, Constants.TIME_DIFFERENCE_MINUTE * Constants.TIME_DIFFERENCE_SECOND).get("updateFlag")) {
        //                    return "自动领取活动，开始时间<=" + Constants.TIME_DIFFERENCE_MINUTE + "分钟，不能修改。[自动领取活动id：" + autoGet.getId() + "]";
        //                }
        //            }
        //        }

        // 固定效期，活动开始、结束时间不能为空（复制的会置空开始、结束时间）
        if(ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED, couponActivityDb.getUseDateType())){
            if(ObjectUtil.isNull(couponActivityDb.getBeginTime())){
                throw new BusinessException(CouponErrorCode.UPDATE_RULES_BEGIN_TIME_NULL_ERROR);
            }
            if(ObjectUtil.isNull(couponActivityDb.getEndTime())){
                throw new BusinessException(CouponErrorCode.UPDATE_RULES_END_TIME_NULL_ERROR);
            }
        }
        // 折扣券
        if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), request.getType())) {
            // 最高优惠金额
            BigDecimal discountMax = request.getDiscountMax();
            if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(CouponActivityErrorCode.DISCOUNT_MAX_VALUE_ERROR, "最高优惠金额不能为负数");
            }
        }
        // 是否已发放
        List<Long> couponActivityIdList = new ArrayList<>();
        couponActivityIdList.add(id);
        //List<CouponDTO> couponList = couponService.getHasGiveListByCouponActivityList(couponActivityIdList);
        if (ObjectUtil.isNotNull(couponActivityDb.getGiveCount()) && couponActivityDb.getGiveCount() != 0) {
            return "此优惠券活动已经发放过优惠券，不能修改，请检查";
        }
        // 名称有修改时，校验是否重复
        /*if (StrUtil.isNotBlank(request.getName()) && !ObjectUtil.equal(request.getName(), couponActivityDb.getName())) {
            boolean isNameRepeat = checkNameRepeat(request.getEid(), request.getName());
            if (isNameRepeat) {
                return "此优惠券活动名称已存在，请检查";
            }
        }*/
        return null;
    }

    /**
     * 优惠券活动名称是否重复
     * @param eid
     * @param name
     * @return true-是 false-否
     */
    private boolean checkNameRepeat(Long eid, String name) {
        QueryCouponActivityRepeatNameRequest request = new QueryCouponActivityRepeatNameRequest();
        request.setEid(eid);
        request.setName(name);
        Long repeatId = this.baseMapper.getIdByName(request);
        if (ObjectUtil.isNotNull(repeatId)) {
            return true;
        }
        return false;
    }

    public Map<String, Boolean> checkUpdateAutoGiveFixed(Date beginTime, Date endTime, long nowTime, long timeDifference) {
        Map<String, Boolean> map = new HashMap<>();
        boolean updateFlag = true;
        boolean giveFlag = true;
        // 开始之前5分钟不能修改
        if ((beginTime.getTime() - nowTime) <= timeDifference) {
            updateFlag = false;
        }
        // 结束之前5分钟不能发放
        if ((endTime.getTime() - nowTime) <= timeDifference) {
            giveFlag = false;
        }
        map.put("updateFlag", updateFlag);
        map.put("giveFlag", giveFlag);
        return map;
    }

    public Map<String, Boolean> checkAutoGiveAfterGive(CouponActivityAutoGiveDTO autoGive, long nowTime, long timeDifference) {
        Map<String, Boolean> map = new HashMap<>();
        boolean updateFlag = true;
        boolean giveFlag = true;
        if (ObjectUtil.isNotNull(autoGive)) {
            Integer autoGiveStatus = autoGive.getStatus();
            // 自动发放活动，状态：1-启用
            if (ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), autoGiveStatus)) {
                Date autoGiveBeginTime = autoGive.getBeginTime();
                Date autoGiveEndTime = autoGive.getEndTime();
                // 自动发放活动开始时间5分钟内不能修改
                if ((autoGiveBeginTime.getTime() - nowTime) <= timeDifference) {
                    log.info("updateCouponActivityCheck false, reason -> {}, autoGiveId -> {}",
                        "自动发放活动，开始时间<=" + Constants.TIME_DIFFERENCE_MINUTE + "分钟，不能修改", autoGive.getId());
                    updateFlag = false;
                }
                // 自动发放未开始，不能手动发放
                if (autoGiveBeginTime.getTime() - nowTime > 0) {
                    giveFlag = false;
                }
                //自动发放结束前5分钟，不能手动发放
                if (autoGiveEndTime.getTime() - nowTime <= timeDifference) {
                    giveFlag = false;
                }
            } else {
                giveFlag = false;
            }
        } else {
            giveFlag = false;
        }
        map.put("updateFlag", updateFlag);
        map.put("giveFlag", giveFlag);
        return map;
    }

    public Map<String, Boolean> checkAutoGetAfterGive(CouponActivityAutoGetDTO autoGet, long nowTime, long timeDifference) {
        Map<String, Boolean> map = new HashMap<>();
        boolean updateFlag = true;
        boolean giveFlag = true;
        if (ObjectUtil.isNotNull(autoGet)) {
            Integer autoGetStatus = autoGet.getStatus();
            // 自动领取活动，状态：1-启用
            if (ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), autoGetStatus)) {
                Date autoGetBeginTime = autoGet.getBeginTime();
                Date autoGetEndTime = autoGet.getEndTime();
                if ((autoGetBeginTime.getTime() - nowTime) <= timeDifference) {
                    log.info("updateCouponActivityCheck false, reason -> {}, autoGetId -> {}",
                        "自动领取活动，开始时间<=" + Constants.TIME_DIFFERENCE_MINUTE + "分钟，不能修改", autoGet.getId());
                    updateFlag = false;
                }
                // 自动领取未开始，不能手动发放
                if (autoGetBeginTime.getTime() - nowTime > 0) {
                    giveFlag = false;
                }
                //自动领取结束前5分钟，不能手动发放
                if (autoGetEndTime.getTime() - nowTime <= timeDifference) {
                    giveFlag = false;
                }
            } else {
                giveFlag = false;
            }
        } else {
            giveFlag = false;
        }
        map.put("updateFlag", updateFlag);
        map.put("giveFlag", giveFlag);
        return map;
    }


    @Override
    public List<CouponActivityCanUseDetailDTO> queryByCouponActivityIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(CouponActivityDO::getId, idList);
        List<CouponActivityCanUseDetailDTO> list = PojoUtils.map(this.list(queryWrapper), CouponActivityCanUseDetailDTO.class);
        list.stream().forEach(item -> {
            Integer type = item.getType();
            BigDecimal thresholdValue = item.getThresholdValue();
            BigDecimal discountValue = item.getDiscountValue();
            StringBuilder thresholdValueBuild = new StringBuilder();
            StringBuilder discountValueBuild = new StringBuilder();
            StringBuilder discountMaxBuild = new StringBuilder();

            thresholdValueBuild.append("满").append(thresholdValue).append("可用");
            if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), type)) {
                // 满减
                discountValueBuild.append(discountValue).append("元");
            } else if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), type)) {
                // 满折
                DecimalFormat df = new DecimalFormat("##.##");
                discountValueBuild.append(df.format(discountValue.divide(BigDecimal.valueOf(10)))).append("折");
                BigDecimal discountMax = item.getDiscountMax();
                if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) > 0) {
                    discountMaxBuild.append("最高减").append(discountMax).append("元");
                }
            }
            item.setThresholdValueRules(thresholdValueBuild.toString());
            item.setDiscountValueRules(discountValueBuild.toString());
            item.setDiscountMaxRules(discountMaxBuild.toString());
        });
        return list;
    }

    @Override
    public Page<CouponActivityDTO> queryListForMarketing(QueryCouponActivityRequest request) {
        // 运营后台只有账号、没有企业信息 eid = null, currentEid = 0
        Page<CouponActivityDTO> page = request.getPage();
        if (Objects.isNull(request)) {
            return page;
        }
        if(ObjectUtil.isNotNull(request.getCreateBeginTime())){
            request.setCreateBeginTime(DateUtil.beginOfDay(request.getCreateBeginTime()));
            request.setCreateEndTime(DateUtil.endOfDay(request.getCreateEndTime()));
        }
        try {
            List<CouponActivityDO>couponActivityDOS=this.baseMapper.queryListForMarketing(page,request);
            page.setRecords(PojoUtils.map(couponActivityDOS, CouponActivityDTO.class));
        } catch (Exception e) {
            log.error("获取优惠券活动列表失败，request -> {}, exception -> {}", JSONObject.toJSONString(request), e);
            throw new BusinessException(ResultCode.FAILED);
        }
        return page;
    }

    @Override
    public Long getActivityIdByCouponId(QueryActivityDetailRequest request) {
        log.info("getActivityIdByCouponId request{}"+JSONObject.toJSONString(request));
        // 参数是卡包id
        CouponDO couponDO = couponService.getById(request.getCouponId());
        if(ObjectUtil.isNull(couponDO)||couponDO.getUsedStatus().intValue()==2||couponDO.getStatus().intValue()==2){
            throw new BusinessException(CouponErrorCode.COUPON_NOT_EXIT);
        }
        boolean timeValiable = (couponDO.getBeginTime().getTime() < System.currentTimeMillis()) && (couponDO.getEndTime().getTime() > System.currentTimeMillis());
        if(!timeValiable){
            throw new BusinessException(CouponErrorCode.COUPON_TIME_NOT_VALIABLE);
        }
        CouponActivityDO couponActivityDO = this.getById(couponDO.getCouponActivityId());
        if(ObjectUtil.equal(2,couponActivityDO.getMemberLimit())){
            LambdaQueryWrapper<CouponActivityMemberLimitDO> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            objectLambdaQueryWrapper.eq(CouponActivityMemberLimitDO::getCouponActivityId, couponActivityDO.getId());
            objectLambdaQueryWrapper.eq(CouponActivityMemberLimitDO::getDelFlag, 0);
            List<CouponActivityMemberLimitDO> list = couponActivityMemberLimitService.list(objectLambdaQueryWrapper);
            if(CollectionUtils.isEmpty(list)){
                throw new BusinessException(CouponErrorCode.GET_ACTIVITY_MEMBER_LIMIT_ERROR);
            }
            Optional<CouponActivityMemberLimitDO> any = list.stream().filter(item -> item.getMemberId().equals(request.getMemberId())).findAny();
            if(!any.isPresent()){
                throw new BusinessException(CouponErrorCode.COUPON_NOT_VALIABLE_FOR_GUIGE);
            }
        }
        return couponActivityDO.getId();
    }

    @Override
    public List<CouponDTO> getCouponById(List<Long> ids) {
        List<CouponDO> couponDOList = couponService.listByIds(ids);
        return PojoUtils.map(couponDOList,CouponDTO.class);
    }

    @Override
    public CouponActivityDetailDTO getDetailById(Long id) {
        CouponActivityDO couponActivityDO = getById(id);
        if(ObjectUtil.isNull(couponActivityDO)){
            return null;
        }
        QueryWrapper<CouponDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(CouponDO::getCouponActivityId,id);
        List<CouponDO> couponDOList = couponService.list(objectQueryWrapper);
        int surplusCount = couponActivityDO.getTotalCount() - (CollectionUtils.isEmpty(couponDOList) ? 0 : couponDOList.size());
        CouponActivityDetailDTO couponActivityDetailDTO = PojoUtils.map(couponActivityDO, CouponActivityDetailDTO.class);
        couponActivityDetailDTO.setSurplusCount(surplusCount);
        return couponActivityDetailDTO;
    }


    @Override
    public Boolean giveCouponBySingal(SaveCouponRequest requests,CouponActivityDetailDTO couponActivityDetailDTO) {
        CouponActivityDO couponActivity = PojoUtils.map(couponActivityDetailDTO,CouponActivityDO.class);
        Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(new Date(), couponActivity.getBeginTime(), couponActivity.getEndTime(), couponActivity.getUseDateType(), couponActivity.getExpiryDays());
        Date beginTime = timeMap.get("beginTime");
        Date endTime = timeMap.get("endTime");
        requests.setBeginTime(beginTime);
        requests.setEndTime(endTime);
        Integer expectGiveNumber = requests.getExpectGiveNumber();
        List<CouponDO> objects = new ArrayList<>();
        for(int i=0;i<expectGiveNumber;i++){
            CouponDO couponDO = new CouponDO();
            couponDO.setBeginTime(beginTime);
            couponDO.setEndTime(endTime);
            couponDO.setCouponActivityId(requests.getCouponActivityId());
            couponDO.setEid(requests.getEid());
            couponDO.setGetType(requests.getGetType());
            couponDO.setOpUserId(requests.getCreateUser());
            couponDO.setGetUserId(requests.getGetUserId());
            couponDO.setCreateTime(new Date());
            couponDO.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
            couponDO.setStatus(CouponStatusEnum.NORMAL_COUPON.getCode());
            objects.add(couponDO);
        }
        couponService.saveBatch(objects);
        // 修改优惠券已经发放数量
        List<CouponActivityAutoGiveRecordDO> giveRecordDOS = new ArrayList<>();
        CouponActivityAutoGiveRecordDO couponActivityAutoGiveRecordDO = new CouponActivityAutoGiveRecordDO();
        couponActivityAutoGiveRecordDO.setCouponActivityId(requests.getCouponActivityId());
        couponActivityAutoGiveRecordDO.setGiveNum(requests.getExpectGiveNumber());
        giveRecordDOS.add(couponActivityAutoGiveRecordDO);
        UpdateCouponGiveNumEvent updateCouponGiveNumEvent = new UpdateCouponGiveNumEvent(this,giveRecordDOS);
        this.applicationEventPublisher.publishEvent(updateCouponGiveNumEvent);
        return true;
    }

    @Override
    @GlobalTransactional
    public Boolean giveCoupon(List<SaveCouponRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return false;
        }
        List<Long> activityIds = requests.stream().map(SaveCouponRequest::getCouponActivityId).collect(Collectors.toList());
        List<CouponActivityDetailDTO> couponActivityById = getCouponActivityById(activityIds);
        if (CollectionUtils.isEmpty(couponActivityById)) {
            return false;
        }
        Map<Long, CouponActivityDetailDTO> couponActivityDetailDTOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(couponActivityById)) {
            couponActivityDetailDTOMap = couponActivityById.stream().collect(Collectors.toMap(CouponActivityDetailDTO::getId, Function.identity()));
        }
        Date date = new Date();
        List<CouponActivityAutoGiveRecordDO> newAutoGiveRecordList = new ArrayList<>();
        Map<Long, CouponActivityDetailDTO> finalCouponActivityDetailDTOMap = couponActivityDetailDTOMap;
        requests.forEach(item -> {
            CouponActivityDetailDTO couponActivity = finalCouponActivityDetailDTOMap.get(item.getCouponActivityId());
            couponActivity = ObjectUtil.isNull(couponActivity) ? new CouponActivityDetailDTO() : couponActivity;
            Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(date, couponActivity.getBeginTime(), couponActivity.getEndTime(), couponActivity.getUseDateType(), couponActivity.getExpiryDays());
            Date beginTime = timeMap.get("beginTime");
            Date endTime = timeMap.get("endTime");
            item.setBeginTime(beginTime);
            item.setEndTime(endTime);
            item.setGetTime(date);
            if(ObjectUtil.isEmpty(item.getGetType())){
                item.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
            }
            item.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
            item.setStatus(CouponStatusEnum.NORMAL_COUPON.getCode());
            CouponActivityAutoGiveRecordDO newRecord = new CouponActivityAutoGiveRecordDO();
            newRecord.setCouponActivityAutoGiveId(item.getCouponActivityAutoId());
            newRecord.setCouponActivityAutoGiveName(item.getCouponActivityAutoName());
            newRecord.setCouponActivityId(item.getCouponActivityId());
            newRecord.setCouponActivityName(item.getCouponActivityName());
            newRecord.setEid(item.getEid());
            newRecord.setEname(item.getEname());
            newRecord.setGiveNum(1);
            newRecord.setGetType(item.getGetType());
            newRecord.setStatus(CouponActivityGiveRecordStatusEnum.SUCCESS.getCode());
            newRecord.setCreateTime(date);
            newRecord.setBatchNumber(CouponUtil.getMillisecondTime());
            newRecord.setRemark("");
            newAutoGiveRecordList.add(newRecord);
        });
        // 生成发放记录，状态待发放
        activityAutoGiveRecordService.saveAutoGiveRecordWithWaitStatus(newAutoGiveRecordList);
        // 生成优惠券
        couponService.insertBatch(requests);
        // 更新已经发放数量
        UpdateCouponGiveNumEvent updateCouponGiveNumEvent = new UpdateCouponGiveNumEvent(this,newAutoGiveRecordList);
        this.applicationEventPublisher.publishEvent(updateCouponGiveNumEvent);
        return true;
    }


    @Override
    public Map<Long, CouponActivityDetailDTO> getRemainDtoByActivityIds(List<Long> request) {
        List<CouponActivityDetailDTO> activityDetailDTOS = this.getCouponActivityById(request);
        List<CouponActivityDetailDTO> couponActivityById = new ArrayList<>();
        activityDetailDTOS.forEach(item -> {
            boolean timeCondition = item.getUseDateType() == 2 || (item.getUseDateType() == 1 && item.getEndTime().after(new Date()));
            if (timeCondition && item.getStatus() == 1 && item.getTotalCount() != 0) {
                couponActivityById.add(item);
            }
        });
        if (CollectionUtils.isEmpty(couponActivityById)) {
            return new HashMap<>();
        }
        List<Map<String, Long>> countByCouponActivityId = couponService.getGiveCountByCouponActivityId(request);
        Map<Long, Long> map = new HashMap<>();
        Map<Long, CouponActivityDetailDTO> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(countByCouponActivityId)) {
            countByCouponActivityId.forEach(item -> {
                map.put(item.get("couponActivityId"), item.get("giveCount"));
            });
        }
        couponActivityById.forEach(item -> {
            if (ObjectUtil.isNull(map.get(item.getId()))) {
                item.setSurplusCount(item.getTotalCount());
                result.put(item.getId(), item);
            } else {
                item.setSurplusCount(item.getTotalCount() - map.get(item.getId()).intValue());
                result.put(item.getId(), item);
            }
        });
        return result;
    }

    @Override
    public Map<Long, Integer> getRemainByActivityIds(List<Long> request) {
        List<CouponActivityDetailDTO> couponActivityById = this.getCouponActivityById(request);
        if (CollectionUtils.isEmpty(couponActivityById)) {
            return MapUtil.newHashMap();
        }

        List<Map<String, Long>> countByCouponActivityId = couponService.getGiveCountByCouponActivityId(request);
        Map<Long, Long> map = new HashMap<>();
        Map<Long, Integer> result = new HashMap<>();
        if(CollectionUtils.isNotEmpty(countByCouponActivityId)){
            countByCouponActivityId.forEach(item -> {
                map.put(item.get("couponActivityId"),item.get("giveCount"));
            });
        }

        couponActivityById.forEach(item -> {
            if (ObjectUtil.isNull(map.get(item.getId()))) {
                result.put(item.getId(), item.getTotalCount());
            }else {
                result.put(item.getId(), item.getTotalCount() - map.get(item.getId()).intValue());
            }
        });

        return result;
    }

    @Override
    public Boolean scrapActivity(CouponActivityOperateRequest request) {
        Long id = request.getId();
        CouponActivityDO couponActivityDO = this.getById(id);
        if (!ObjectUtil.equal(request.getEid(), couponActivityDO.getEid())) {
            throw new BusinessException(CouponActivityErrorCode.BUSINESS_COUPON_AUTHORIZATION_ERROR);
        }

        Integer status = couponActivityDO.getStatus();
        if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
            log.info("此优惠券活动状态是“废弃”，不能再操作“作废”, id -> {}", id);
            throw new BusinessException(CouponActivityErrorCode.SCRAP_ERROR);
        }
        LambdaQueryWrapper<CouponActivityDO> activityQueryWrapper = new LambdaQueryWrapper<>();
        activityQueryWrapper.eq(CouponActivityDO::getId, id);
        CouponActivityDO activityEntity = new CouponActivityDO();
        activityEntity.setStatus(CouponActivityStatusEnum.SCRAP.getCode());
        return this.update(activityEntity, activityQueryWrapper);
    }

    @Override
    public Boolean updateCoupon(List<Long> partIds) {
        LambdaQueryWrapper<CouponDO> activityQueryWrapper = new LambdaQueryWrapper<>();
        activityQueryWrapper.in(CouponDO::getId, partIds);
        CouponDO activityEntity = new CouponDO();
        activityEntity.setStatus(2);
        return couponService.update(activityEntity, activityQueryWrapper);
    }

    @Override
    public List<CouponDTO> getHasGiveCountByEids(Long couponActivityId, List<Long> eidList) {
        List<CouponDTO> hasGiveCountByEidList = couponMapper.getHasGiveCountByEidList(eidList, couponActivityId);
        return hasGiveCountByEidList;
    }

    @Override
    public void updateHasGiveNum(Long couponActivityId, int size) {
        couponActivityMapper.updateHasGiveNum(couponActivityId,size);
    }

    @Override
    public void SyncCouponGiveNum() {
        // 1 获取永久生效的券，即领券后生效的券。总数量赋值给优惠券主表。
        // 2 获取生效的券-固定生效时间的券。总数量赋值给优惠券主表。
        // 3 获取固定生效时间，前20失效的券。总数量赋值给优惠券主表。
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponActivityDO::getUseDateType,2);
        queryWrapper.eq(CouponActivityDO::getStatus,1);
        List<CouponActivityDO> list = this.list(queryWrapper);
        List<CouponHasGetDTO> result = new ArrayList<>();
        result=getResult(list,result);

        LambdaQueryWrapper<CouponActivityDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(CouponActivityDO::getUseDateType,1);
        queryWrapper1.ge(CouponActivityDO::getEndTime,new Date());
        List<CouponActivityDO> list2 = this.list(queryWrapper1);
        result=getResult(list2,result);

        LambdaQueryWrapper<CouponActivityDO> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(CouponActivityDO::getUseDateType,1);
        queryWrapper2.lt(CouponActivityDO::getEndTime,new Date());
        queryWrapper2.last("limit 30");
        List<CouponActivityDO> list3 = this.list(queryWrapper2);
        result=getResult(list3,result);

        if(CollectionUtils.isNotEmpty(result)){
            List<CouponActivityDO> collect = result.stream().map(item -> {
                CouponActivityDO couponActivityDO = new CouponActivityDO();
                couponActivityDO.setId(item.getId());
                couponActivityDO.setGiveCount(item.getNum());
                return  couponActivityDO;
            }).collect(Collectors.toList());
            this.updateBatchById(collect);
        }
    }

    private List<CouponHasGetDTO> getResult(List<CouponActivityDO> list, List<CouponHasGetDTO> result) {
        if(CollectionUtils.isNotEmpty(list)){
            List<Long> ids = list.stream().map(CouponActivityDO::getId).collect(Collectors.toList());
            List<List<Long>> lists = splitList(ids, 10);
            lists.forEach(item->{
                List<CouponHasGetDTO> giveCountByActivityId = this.baseMapper.getGiveCountByActivityId(item);
                if(CollectionUtils.isNotEmpty(giveCountByActivityId)){
                    result.addAll(giveCountByActivityId);
                }
            });
        }
        return result;
    }

    /**
     * 分割集合工具类
     */
    public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
        //判断集合是否为空
        if (CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        //计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;
        //开始分割
        return Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> list.parallelStream().skip(a * splitSize).limit(splitSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }
}
