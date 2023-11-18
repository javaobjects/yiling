package com.yiling.marketing.couponactivityautogive.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityLogTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityPromotionCodeEnum;
import com.yiling.marketing.common.enums.CouponActivityRepeatGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityResultTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetEnterpriseLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRepeatNameRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.service.CouponActivityLogService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautogive.dao.CouponActivityAutoGiveMapper;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveOperateRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveDetailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveBasicRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveCouponRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveRulesRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveCountRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveGoodsLimitDO;
import com.yiling.marketing.couponactivityautogive.enums.CouponActivityAutoGiceErrorCode;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveCouponService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveEnterpriseLimitService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveGoodsLimitService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveRecordService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 自动发券活动表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Service
public class CouponActivityAutoGiveServiceImpl extends BaseServiceImpl<CouponActivityAutoGiveMapper, CouponActivityAutoGiveDO>
                                               implements CouponActivityAutoGiveService {

    @Autowired
    private CouponActivityService                        couponActivityService;
    @Autowired
    private CouponService                                couponService;
    @Autowired
    private CouponActivityLogService                     couponActivityLogService;
    @Autowired
    private CouponActivityAutoGiveEnterpriseLimitService autoGiveEnterpriseLimitService;
    @Autowired
    private CouponActivityAutoGiveGoodsLimitService      autoGiveGoodsLimitService;
    @Autowired
    private CouponActivityAutoGiveCouponService          autoGiveCouponService;
    @Autowired
    private CouponActivityAutoGiveRecordService          autoGiveRecordService;

    @Override
    public Page<CouponActivityAutoGiveDO> queryListPage(QueryCouponActivityAutoGiveRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGiveDO> queryWrapper = getCouponActivityWrapper(request);
        return page(request.getPage(), queryWrapper);
    }

    @Override
    @GlobalTransactional
    public Long saveOrUpdateBasic(SaveCouponActivityAutoGiveBasicRequest request) {
        String name = request.getName();
        if (StrUtil.isBlank(name)) {
            throw new BusinessException(CouponActivityErrorCode.NAME_NULL_ERROR);
        }

        // 时间校验
        long nowTime = System.currentTimeMillis();
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
            throw new BusinessException(CouponErrorCode.COUPON_BEGIN_TIME_ERROR);
        }
        // 保存、修改校验结束时间
        if (endTime.getTime() <= beginTime.getTime()) {
            throw new BusinessException(CouponErrorCode.COUPON_END_TIME_ERROR);
        }

        // 修改校验
        String checkMsg = updateCouponActivityAutoGiveCheck(request.getId(), name, nowTime, beginTime, endTime);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityAutoGiceErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        // 保存校验
        checkMsg = saveCouponActivityAutoGiveCheck(request);
        if (StrUtil.isNotBlank(checkMsg)) {
            throw new BusinessException(CouponActivityAutoGiceErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
        }

        try {

            // 关联优惠券空值校验
            List<SaveCouponActivityAutoGiveCouponRequest> couponActivityList = request.getCouponActivityList();
            if (CollUtil.isEmpty(couponActivityList)) {
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GIVE_RELATION_COUPON_ACTIVITY_NULL);
            }
            couponActivityList.forEach(couponActivity -> {
                if (ObjectUtil.isNull(couponActivity.getCouponActivityId()) || couponActivity.getCouponActivityId() == 0) {
                    throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GIVE_RELATION_COUPON_ACTIVITY_NULL);
                }
                if (ObjectUtil.isNull(couponActivity.getGiveNum()) || couponActivity.getGiveNum() == 0) {
                    throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GIVE_RELATION_COUPON_ACTIVITY_NUM_NULL);
                }
            });

            // 校验重复值
            List<Long> couponActivityIdList = couponActivityList.stream().map(SaveCouponActivityAutoGiveCouponRequest::getCouponActivityId)
                .collect(Collectors.toList());
            String format = "";
            Set<Long> set = new HashSet<>(couponActivityIdList);
            Collection difference = CollectionUtils.disjunction(couponActivityIdList, set);
            List<Long> differenceList = new ArrayList<>(difference);
            if (CollUtil.isNotEmpty(differenceList)) {
                format = MessageFormat.format(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_REPEAT_ERROR.getMessage(), differenceList.toString());
                throw new BusinessException(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_REPEAT_ERROR, format);
            }

            // 查询优惠券活动信息
            List<CouponActivityDetailDTO> couponActivityDbList = couponActivityService.getCouponActivityById(couponActivityIdList);
            // 优惠券活动全部不存在
            if (CollUtil.isEmpty(couponActivityDbList)) {
                format = MessageFormat.format(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL.getMessage(), couponActivityIdList.toString());
                throw new BusinessException(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL, format);
            }
            // 优惠券活动有部分不存在
            Map<Long, CouponActivityDetailDTO> couponActivityDbMap = couponActivityDbList.stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));
            // 优惠券活动已发放数量
            List<Map<String, Long>> giveCountList = couponService.getGiveCountByCouponActivityId(couponActivityIdList);
            for (SaveCouponActivityAutoGiveCouponRequest autoGiveCouponRequest : couponActivityList) {
                CouponActivityDetailDTO couponActivity = couponActivityDbMap.get(autoGiveCouponRequest.getCouponActivityId());
                if (ObjectUtil.isNull(couponActivity)) {
                    format = MessageFormat.format(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL.getMessage(), couponActivity.getId());
                    throw new BusinessException(CouponActivityAutoGiceErrorCode.COUPON_ACTIVITY_NULL, format);
                }
                // 优惠券活动校验
                List<Integer> countList = new ArrayList<>();
                CouponActivityDO couponActivityDo = PojoUtils.map(couponActivity, CouponActivityDO.class);
                String couponActivityErrorMsg = couponActivityService.checkCouponActivity(couponActivityDo, countList, giveCountList);
                if (StrUtil.isNotBlank(couponActivityErrorMsg)) {
                    throw new BusinessException(CouponErrorCode.COUPON_RELEVANCE_ERROR, couponActivityErrorMsg);
                }
            }
            // 是否已被其他发放活动关联
            String errorMsg = this.getByAutoGiveIdAndCouponActivityId(request.getId(), couponActivityIdList);
            if (StrUtil.isNotBlank(errorMsg)) {
                throw new BusinessException(CouponErrorCode.COUPON_RELEVANCE_ERROR, errorMsg);
            }

            CouponActivityAutoGiveDO couponActivityAutoGiveDO = PojoUtils.map(request, CouponActivityAutoGiveDO.class);
            // 发券类型默认为 订单累计金额-1
            couponActivityAutoGiveDO.setType(1);
            // 发放活动
            Long autoGiveId = saveOrUpdateAutoGive(request.getId(), request.getOpUserId(), couponActivityAutoGiveDO);
            // 保存关联优惠券
            autoGiveCouponService.saveGiveCoupon(request.getOpUserId(), autoGiveId, couponActivityList);
            return autoGiveId;
        } catch (Exception e) {
            log.error("saveOrUpdateBasic error, id -> {}, exception -> {}", request.getId(), e);
            throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED, e.getMessage());
        }
    }

    @Override
    public Long saveOrUpdateRules(SaveCouponActivityAutoGiveRulesRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            log.info("自动发放优惠券活动保存或修改使用规则，ID不能为空");
            throw new BusinessException(CouponActivityErrorCode.UPDATE_ID_NULL_ERROR);
        }

        try {
            // 修改校验
            String checkMsg = updateCouponActivityAutoGiveCheck(request.getId(), null, 0, null, null);
            if (StrUtil.isNotBlank(checkMsg)) {
                throw new BusinessException(CouponActivityAutoGiceErrorCode.UPDATE_ALLOW_CHECK_ERROR, checkMsg);
            }

            Integer type = request.getType();
            if (ObjectUtil.isNull(type) || type == 0) {
                throw new BusinessException(CouponErrorCode.AUTO_GIVE_TYPE_ERROR);
            }

            String platformSelected = "";
            String payMethodSelected = "";
            String orderStatusSelected = "";
            if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getCode(), type)) {
                Integer cumulative = request.getCumulative();
                if (ObjectUtil.isNull(cumulative) || cumulative <= 0) {
                    throw new BusinessException(CouponErrorCode.AUTO_GIVE_CUMULATIVE_ERROR);
                }
                // 平台限制（1-全部平台；2-部分平台）
                if (ObjectUtil.isNull(request.getConditionOrderPlatform()) || request.getConditionOrderPlatform() == 0) {
                    throw new BusinessException(CouponErrorCode.PLATFORM_LIMIT_ERROR);
                }
                if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getConditionOrderPlatform())) {
                    if (CollUtil.isEmpty(request.getConditionOrderPlatformValueList())) {
                        throw new BusinessException(CouponErrorCode.PLATFORM_LIMIT_VALUE_ERROR);
                    }
                    platformSelected = String.join(",", request.getConditionOrderPlatformValueList());
                }
                // 支付方式限制（1-全部支付方式；2-部分支付方式）
                if (ObjectUtil.isNull(request.getConditionPaymethod()) || request.getConditionPaymethod() == 0) {
                    throw new BusinessException(CouponErrorCode.PAY_METHOD_LIMIT_ERROR);
                }
                if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getConditionPaymethod())) {
                    if (CollUtil.isEmpty(request.getConditionPaymethodValueList())) {
                        throw new BusinessException(CouponErrorCode.PAY_METHOD_LIMIT_VALUE_ERROR);
                    }
                    payMethodSelected = String.join(",", request.getConditionPaymethodValueList());
                }
                // 订单状态限制（1-全部订单状态；2-部分订单状态）
                if (ObjectUtil.isNull(request.getConditionOrderStatus()) || request.getConditionOrderStatus() == 0) {
                    throw new BusinessException(CouponErrorCode.ORDER_STATUS_LIMIT_ERROR);
                }
                if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getConditionOrderStatus())) {
                    if (CollUtil.isEmpty(request.getConditionOrderStatusValueList())) {
                        throw new BusinessException(CouponErrorCode.ORDER_STATUS_VALUE_ERROR);
                    }
                    orderStatusSelected = String.join(",", request.getConditionOrderStatusValueList());
                }
                // 指定商品
                Integer conditionGoods = request.getConditionGoods();
                if (ObjectUtil.isNull(conditionGoods) || conditionGoods == 0) {
                    throw new BusinessException(CouponErrorCode.GOODS_LIMIT_ERROR);
                }
                if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), conditionGoods)) {
                    // 部分商品可用
                    List<CouponActivityAutoGiveGoodsLimitDO> goodsLimits = autoGiveGoodsLimitService.getGoodsLimitByAutoGiveId(request.getId());
                    if (CollUtil.isEmpty(goodsLimits)) {
                        throw new BusinessException(CouponErrorCode.GOODS_LIMIT_NULL_ERROR);
                    }
                }

            } else if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode(), type)){
                // 是否有推广码 1-是，2-否
                if (ObjectUtil.isNull(request.getConditionPromotionCode()) || request.getConditionPromotionCode() == 0) {
                    throw new BusinessException(CouponErrorCode.PROMOTION_CODE_ERROR);
                }
                // 选择是否有推广码，选择【是\否】只能选择一次性发放
                if (!CouponActivityPromotionCodeEnum.ALL.getCode().equals(request.getConditionPromotionCode())) {
                    if(Integer.valueOf(2).equals(request.getRepeatGive())) {
                        throw new BusinessException(CouponErrorCode.PROMOTION_REPEAT_CODE_ERROR);
                    }
                }
            }

            /* 企业类型限制（1-全部企业类型；2-部分企业类型） */
            if (ObjectUtil.isNull(request.getConditionEnterpriseType()) || request.getConditionEnterpriseType() == 0) {
                throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_LIMIT_ERROR);
            }
            // 2
            String enterpriseTypeSelected = "";
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), request.getConditionEnterpriseType())) {
                if (CollUtil.isEmpty(request.getConditionEnterpriseTypeValueList())) {
                    throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_VALUE_ERROR);
                }
                enterpriseTypeSelected = String.join(",", request.getConditionEnterpriseTypeValueList());
            }

            /* 用户类型 */
            Integer conditionUserType = request.getConditionUserType();
            if (ObjectUtil.isNull(conditionUserType) || conditionUserType == 0) {
                throw new BusinessException(CouponErrorCode.USER_TYPE_LIMIT_ERROR);
            }
            if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), conditionUserType)) {
                // 部分会员
                List<CouponActivityAutoGiveEnterpriseLimitDO> enterpriseLimits = autoGiveEnterpriseLimitService.getByAutoGiveId(request.getId());
                if (CollUtil.isEmpty(enterpriseLimits)) {
                    throw new BusinessException(CouponErrorCode.MEMBER_LIMIT_NULL_ERROR);
                }
            }

            /* 是否重复发放 */
            Integer repeatGive = request.getRepeatGive();
            if (ObjectUtil.isNull(repeatGive) || repeatGive.intValue() <= 0) {
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GIVE_REPEAT_GIVE_NULL);
            }
            /* 最多发放次数 */
            Integer maxGiveNum = 1;
            if (ObjectUtil.equal(CouponActivityRepeatGiveTypeEnum.MANY.getCode(), repeatGive)) {
                // 多次发放、每月发放
                maxGiveNum = request.getMaxGiveNum();
                if (ObjectUtil.isNull(maxGiveNum) || maxGiveNum <= 0) {
                    throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GIVE_REPEAT_GIVE_MAX_NUM_NULL);
                }
                // 每月发放
                if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode(), type)
                    || ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ENTERPRISE_POPULARIZE.getCode(), type)) {
                    // 校验时间
                    CouponActivityAutoGiveDO autoGiveDb = this.getById(request.getId());
                    Date beginTime = autoGiveDb.getBeginTime();
                    Date endTime = autoGiveDb.getEndTime();
                    // 取相差月份
                    long monthDiff = DateUtil.betweenMonth(beginTime, endTime, false);
                    if (maxGiveNum > monthDiff) {
                        throw new BusinessException(CouponErrorCode.AUTO_GIVE_MONTH_ERROR);
                    }
                }
            }

            CouponActivityAutoGiveDO couponActivityutoGiveDO = PojoUtils.map(request, CouponActivityAutoGiveDO.class);
            // 选择平台
            couponActivityutoGiveDO.setConditionOrderPlatformValue(platformSelected);
            // 支付方式
            couponActivityutoGiveDO.setConditionPaymethodValue(payMethodSelected);
            // 订单状态
            couponActivityutoGiveDO.setConditionOrderStatusValue(orderStatusSelected);
            // 企业类型
            couponActivityutoGiveDO.setConditionEnterpriseTypeValue(enterpriseTypeSelected);
            // 最多发放次数
            couponActivityutoGiveDO.setMaxGiveNum(maxGiveNum);

            return saveOrUpdateAutoGive(request.getId(), request.getCurrentUserId(), couponActivityutoGiveDO);
        } catch (Exception e) {
            log.error("saveOrUpdateRules error, id -> {}, exception -> {}", request.getId(), e);
            throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED, e.getMessage());
        }
    }

    @Override
    public CouponActivityAutoGiveDetailDTO getDetailById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }
        return PojoUtils.map(this.getDetail(id), CouponActivityAutoGiveDetailDTO.class);
    }

    @Override
    @GlobalTransactional
    public Boolean stop(CouponActivityAutoGiveOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getUserId();
        // 优惠券活动操作日志构建
        Integer logType = CouponActivityLogTypeEnum.STOP.getCode();
        int logStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
        String faileReason = "";
        try {
            CouponActivityAutoGiveDO couponActivityAutoGive = this.getById(id);
            if (ObjectUtil.isNull(couponActivityAutoGive)) {
                throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
            }

            Integer status = couponActivityAutoGive.getStatus();
            if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), status)) {
                log.info("自动发券活动状态不是“启用”，不能操作“停用”, id -> {}", id);
                throw new BusinessException(CouponActivityErrorCode.STOP_ERROR);
            }
            LambdaQueryWrapper<CouponActivityAutoGiveDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CouponActivityAutoGiveDO::getId, id);
            CouponActivityAutoGiveDO entity = new CouponActivityAutoGiveDO();
            entity.setStatus(CouponActivityStatusEnum.DISABLED.getCode());
            this.update(entity, queryWrapper);
            // 保存操作日志
            couponActivityLogService.insertCouponActivityLog(id, logType, JSONObject.toJSONString(request), logStatus, faileReason, userId);
        } catch (Exception e) {
            log.error("停用自动发券活动失败，id -> {}, exception -> {}", id, e);
            throw new BusinessException(ResultCode.FAILED, e.getMessage());
        }
        return true;
    }

    @Override
    @GlobalTransactional
    public Boolean enable(CouponActivityAutoGiveOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getUserId();
        // 优惠券活动操作日志构建
        Integer logType = CouponActivityLogTypeEnum.ENABLE.getCode();
        int logStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
        String faileReason = "";
        try {
            CouponActivityAutoGiveDO couponActivityAutoGive = this.getById(id);
            if (ObjectUtil.isNull(couponActivityAutoGive)) {
                throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
            }

            Integer status = couponActivityAutoGive.getStatus();
            if (!ObjectUtil.equal(CouponActivityStatusEnum.DISABLED.getCode(), status)) {
                log.info("自动发券活动状态不是“停用”，不能操作“启用”, id -> {}", id);
                throw new BusinessException(CouponActivityErrorCode.STOP_ERROR);
            }
            LambdaQueryWrapper<CouponActivityAutoGiveDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CouponActivityAutoGiveDO::getId, id);
            CouponActivityAutoGiveDO entity = new CouponActivityAutoGiveDO();
            entity.setStatus(CouponActivityStatusEnum.ENABLED.getCode());
            this.update(entity, queryWrapper);
            // 保存操作日志
            couponActivityLogService.insertCouponActivityLog(id, logType, JSONObject.toJSONString(request), logStatus, faileReason, userId);
        } catch (Exception e) {
            log.error("启用自动发券活动失败，id -> {}, exception -> {}", id, e);
            throw new BusinessException(ResultCode.FAILED, e.getMessage());
        }
        return true;
    }

    @Override
    @GlobalTransactional
    public Boolean scrap(CouponActivityAutoGiveOperateRequest request) {
        Long id = request.getId();
        Long userId = request.getUserId();
        // 优惠券活动操作日志构建
        Integer logType = CouponActivityLogTypeEnum.SCRAP.getCode();
        int logStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
        String faileReason = "";
        try {
            CouponActivityAutoGiveDO couponActivityAutoGive = this.getById(id);
            if (ObjectUtil.isNull(couponActivityAutoGive)) {
                throw new BusinessException(CouponErrorCode.DATA_NOT_EXIST);
            }

            Integer status = couponActivityAutoGive.getStatus();
            if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
                log.info("自动发券活动状态是“废弃”，不能再操作“作废”, id -> {}", id);
                throw new BusinessException(CouponActivityErrorCode.SCRAP_ERROR);
            }
            LambdaQueryWrapper<CouponActivityAutoGiveDO> activityQueryWrapper = new LambdaQueryWrapper<>();
            activityQueryWrapper.eq(CouponActivityAutoGiveDO::getId, id);
            CouponActivityAutoGiveDO activityEntity = new CouponActivityAutoGiveDO();
            activityEntity.setStatus(CouponActivityStatusEnum.SCRAP.getCode());
            this.update(activityEntity, activityQueryWrapper);
            // 保存操作日志
            couponActivityLogService.insertCouponActivityLog(id, logType, JSONObject.toJSONString(request), logStatus, faileReason, userId);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.FAILED, e.getMessage());
        }
        return true;
    }

    @Override
    public CouponActivityAutoGiveDTO getAutoGiveById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }
        LambdaQueryWrapper<CouponActivityAutoGiveDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CouponActivityAutoGiveDO::getId, id);
        CouponActivityAutoGiveDO one = this.getOne(lambdaQueryWrapper);
        return PojoUtils.map(one, CouponActivityAutoGiveDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveDTO> getAutoGiveByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CouponActivityAutoGiveDO::getId, idList);
        List<CouponActivityAutoGiveDO> list = this.list(lambdaQueryWrapper);
        return PojoUtils.map(list, CouponActivityAutoGiveDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveDetailDTO> getAllByCondition(QueryCouponActivityGiveDetailRequest request) {
        if (ObjectUtil.isNull(request)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveDO> queryWrapper = new LambdaQueryWrapper<>();
        Date date = new Date();
        queryWrapper.eq(CouponActivityAutoGiveDO::getType, request.getType());
        queryWrapper.le(CouponActivityAutoGiveDO::getBeginTime, date);
        queryWrapper.gt(CouponActivityAutoGiveDO::getEndTime, date);
        queryWrapper.eq(CouponActivityAutoGiveDO::getStatus, CouponActivityStatusEnum.ENABLED.getCode());
        List<CouponActivityAutoGiveDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        List<CouponActivityAutoGiveDetailDTO> autoGiveDetailList = new ArrayList<>();
        CouponActivityAutoGiveDetailDTO detail;
        for (CouponActivityAutoGiveDO autoGiveDO : list) {
            detail = PojoUtils.map(autoGiveDO, CouponActivityAutoGiveDetailDTO.class);
            buildSelectedValueList(autoGiveDO, detail);
            autoGiveDetailList.add(detail);
        }
        return autoGiveDetailList;
    }

    @Override
    public Boolean updateGiveCountByIdList(UpdateAutoGiveCountRequest request) {
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getIds())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        return this.baseMapper.updateGiveCountByIdList(request);
    }


    public String getByAutoGiveIdAndCouponActivityId(Long autoGiveId, List<Long> couponActivityIds) {
        if (CollUtil.isEmpty(couponActivityIds)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        List<CouponActivityAutoGiveCouponDTO> autoGiveCouponList = autoGiveCouponService.getByCouponByCouponActivityIdList(couponActivityIds);
        if (CollUtil.isEmpty(autoGiveCouponList)) {
            return null;
        }
        List<Long> autoGiveIdDbList = autoGiveCouponList.stream().map(CouponActivityAutoGiveCouponDTO::getCouponActivityAutoGiveId).distinct()
            .collect(Collectors.toList());
        List<CouponActivityAutoGiveDTO> autoGiveDbList = this.getAutoGiveByIdList(autoGiveIdDbList);
        if (CollUtil.isEmpty(autoGiveDbList)) {
            return null;
        }
        if (autoGiveDbList.size() == 1 && ObjectUtil.equal(autoGiveDbList.get(0).getId(), autoGiveId)) {
            return null;
        }

        Map<Long, List<CouponActivityAutoGiveCouponDTO>> autoGiveCouponMap = autoGiveCouponList.stream()
            .collect(Collectors.groupingBy(CouponActivityAutoGiveCouponDTO::getCouponActivityAutoGiveId));
        for (CouponActivityAutoGiveDTO autoGiveDb : autoGiveDbList) {
            if (ObjectUtil.equal(autoGiveDb.getId(), autoGiveId)) {
                continue;
            }
            List<CouponActivityAutoGiveCouponDTO> autoGiveCoupon = autoGiveCouponMap.get(autoGiveDb.getId());
            if (ObjectUtil.isNull(autoGiveCoupon)) {
                continue;
            }

            boolean timeFlag = false;
            boolean statusFlag = false;
            if (autoGiveDb.getEndTime().getTime() >= System.currentTimeMillis()) {
                timeFlag = true;
            }
            if (!ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), autoGiveDb.getStatus())) {
                statusFlag = true;
            }
            if (timeFlag && statusFlag) {
                List<Long> list = autoGiveCoupon.stream().map(CouponActivityAutoGiveCouponDTO::getCouponActivityId).distinct()
                    .collect(Collectors.toList());
                return "此优惠券活动id:" + list.toString() + "已被其他自动发放活动关联[id:" + autoGiveDb.getId() + "]";
            }
        }
        return null;
    }

    private LambdaQueryWrapper<CouponActivityAutoGiveDO> getCouponActivityWrapper(QueryCouponActivityAutoGiveRequest request) {
        LambdaQueryWrapper<CouponActivityAutoGiveDO> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getId()) && request.getId() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveDO::getId, request.getId());
        }
        if (StrUtil.isNotEmpty(request.getName())) {
            queryWrapper.like(CouponActivityAutoGiveDO::getName, request.getName());
        }
        if (ObjectUtil.isNotNull(request.getType()) && request.getType() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveDO::getType, request.getType());
        }
        if (ObjectUtil.isNotNull(request.getStatus()) && request.getStatus() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveDO::getStatus, request.getStatus());
        }
        if (ObjectUtil.isNotNull(request.getRepeatGive()) && request.getRepeatGive() != 0) {
            queryWrapper.eq(CouponActivityAutoGiveDO::getRepeatGive, request.getRepeatGive());
        }
        if (ObjectUtil.isNotNull(request.getConditionPromotionCode()) && request.getConditionPromotionCode() != 0) {
            int conditionPromotionCode = request.getConditionPromotionCode().intValue();
            if(conditionPromotionCode == 1){
                queryWrapper.eq(CouponActivityAutoGiveDO::getConditionPromotionCode, 1);
            }
            if(conditionPromotionCode == 2){
                queryWrapper.notIn(CouponActivityAutoGiveDO::getConditionPromotionCode, 1);
            }
        }

        if (ObjectUtil.isNotNull(request.getActivityStatus()) && request.getActivityStatus() != 0) {
            // 活动状态：1-未开始 2-进行中 3-已结束
            Date date = new Date();
            if (request.getActivityStatus() == 1) {
                // 查开始时间大于等于当前时间的
                queryWrapper.gt(CouponActivityAutoGiveDO::getBeginTime, date);
            }
            if (request.getActivityStatus() == 2) {
                // 查开始时间小于等于当前时间的，且结束时间大于当前时间的
                queryWrapper.le(CouponActivityAutoGiveDO::getBeginTime, date);
                queryWrapper.gt(CouponActivityAutoGiveDO::getEndTime, date);
            }
            if (request.getActivityStatus() == 3) {
                // 查结束时间小于等于当前时间的
                queryWrapper.lt(CouponActivityAutoGiveDO::getEndTime, date);
            }
        }
        if (ObjectUtil.isNotNull(request.getBeginCreateTime())) {
            queryWrapper.ge(CouponActivityAutoGiveDO::getCreateTime, DateUtil.beginOfDay(request.getBeginCreateTime()));
        }
        if (ObjectUtil.isNotNull(request.getEndCreateTime())) {
            queryWrapper.le(CouponActivityAutoGiveDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        queryWrapper.orderByDesc(CouponActivityAutoGiveDO::getCreateTime);
        return queryWrapper;
    }

    private CouponActivityAutoGiveDetailDTO getDetail(Long id) {
        CouponActivityAutoGiveDO couponActivityAutoGiveDO;
        CouponActivityAutoGiveDetailDTO couponActivityAutoGiveDetailDTO;
        try {
            couponActivityAutoGiveDO = this.getById(id);
            couponActivityAutoGiveDetailDTO = Optional.ofNullable(PojoUtils.map(couponActivityAutoGiveDO, CouponActivityAutoGiveDetailDTO.class))
                .orElseThrow(() -> new BusinessException(CouponErrorCode.DATA_NOT_EXIST));
            buildSelectedValueList(couponActivityAutoGiveDO, couponActivityAutoGiveDetailDTO);
        } catch (Exception e) {
            log.error("获取自动发放优惠券活动详情失败，id -> {}, exception -> {}", id, e);
            throw new BusinessException(ResultCode.FAILED);
        }
        return couponActivityAutoGiveDetailDTO;
    }

    private void buildSelectedValueList(CouponActivityAutoGiveDO autoGiveDO, CouponActivityAutoGiveDetailDTO autoGiveDetailDTO) {
        List<String> emptyList = ListUtil.empty();
        // 选择平台
        String platformSelected = autoGiveDO.getConditionOrderPlatformValue();
        List<String> platformSelectedList = StrUtil.isBlank(platformSelected) ? emptyList : Arrays.asList(platformSelected.split(","));
        // 支付方式
        String payMethodSelected = autoGiveDO.getConditionPaymethodValue();
        List<String> payMethodSelectedList = StrUtil.isBlank(payMethodSelected) ? emptyList : Arrays.asList(payMethodSelected.split(","));
        // 订单状态
        String orderStatusSelected = autoGiveDO.getConditionOrderStatusValue();
        List<String> orderStatusSelectedList = StrUtil.isBlank(orderStatusSelected) ? emptyList : Arrays.asList(orderStatusSelected.split(","));
        // 企业类型
        String enterpriseTypeSelected = autoGiveDO.getConditionEnterpriseTypeValue();
        List<String> enterpriseTypeSelectedList = StrUtil.isBlank(enterpriseTypeSelected) ? emptyList
            : Arrays.asList(enterpriseTypeSelected.split(","));

        // 选择平台
        autoGiveDetailDTO.setConditionOrderPlatformValueList(platformSelectedList);
        // 支付方式
        autoGiveDetailDTO.setConditionPaymethodValueList(payMethodSelectedList);
        // 订单状态
        autoGiveDetailDTO.setConditionOrderStatusValueList(orderStatusSelectedList);
        // 企业类型
        autoGiveDetailDTO.setConditionEnterpriseTypeValueList(enterpriseTypeSelectedList);
    }

    /**
     * 保存自动发券活动基本信息校验
     * @param request
     * @return
     */
    public String saveCouponActivityAutoGiveCheck(SaveCouponActivityAutoGiveBasicRequest request) {
        if (ObjectUtil.isNotNull(request.getId())) {
            return null;
        }
        // 保存，校验名称是否重复
        boolean isNameRepeat = checkNameRepeat(request.getName());
        if (isNameRepeat) {
            return "此自动发券活动名称已存在，请检查";
        }
        return null;
    }

    /**
     * 修改自动发券活动校验
     * @param id
     * @param name
     * @return
     */
    public String updateCouponActivityAutoGiveCheck(Long id, String name, long nowTime, Date beginTime, Date endTime) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }

        // id不为空，开始校验是否允许修改
        // 查询已保存信息
        CouponActivityAutoGiveDO autoGiveDb = this.getById(id);

        Integer status = autoGiveDb.getStatus();
        // 状态：停用、废弃，不可修改
        if (ObjectUtil.equal(CouponActivityStatusEnum.DISABLED.getCode(), status)
            || ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
            log.info("updateCouponActivityAutoGiveCheck false, reason -> {}", "自动发放优惠券活动状态：停用、废弃，不可修改");
            return "自动发放优惠券活动状态：停用、废弃，不可修改";
        }

        if (nowTime != 0 && ObjectUtil.isNotNull(beginTime) && ObjectUtil.isNotNull(endTime)) {
            // 已开始，不能修改开始时间
            if (autoGiveDb.getBeginTime().getTime() <= nowTime && autoGiveDb.getBeginTime().getTime() != beginTime.getTime()) {
                return "自动发放优惠券活动已开始，不能修改活动开始时间，请检查";
            }
            // 已结束，不能修改时间
            if (autoGiveDb.getEndTime().getTime() <= nowTime
                && (autoGiveDb.getBeginTime().getTime() != beginTime.getTime() || autoGiveDb.getEndTime().getTime() != endTime.getTime())) {
                return "自动发放优惠券活动已结束，不能修改活动起止时间，请检查";
            }
        }

        // 是否已发放
        List<CouponDTO> couponList = couponService.getByCouponActivityAutoId(id);
        if (CollUtil.isNotEmpty(couponList)) {
            return "此优自动发放优惠券活动已经发放过优惠券，不能修改，请检查";
        }
        // 名称有修改时，校验是否重复
        if (StrUtil.isNotBlank(name) && !ObjectUtil.equal(name, autoGiveDb.getName())) {
            boolean isNameRepeat = checkNameRepeat(name);
            if (isNameRepeat) {
                return "此自动发券活动名称已存在，请检查";
            }
        }
        return null;
    }

    /**
     * 优惠券活动名称是否重复
     * @param name
     * @return true-是 false-否
     */
    private boolean checkNameRepeat(String name) {
        QueryCouponActivityRepeatNameRequest request = new QueryCouponActivityRepeatNameRequest();
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

    private Long saveOrUpdateAutoGive(Long id, Long currentUserId, CouponActivityAutoGiveDO couponActivityAutoGiveDO) throws Exception {
        Date date = new Date();
        // 运营后台没有企业信息，企业信息字段不插入、不更新
        if (Objects.isNull(id) || id == 0) {
            // 保存
            couponActivityAutoGiveDO.setStatus(CouponActivityStatusEnum.ENABLED.getCode());
            couponActivityAutoGiveDO.setCreateUser(currentUserId);
            couponActivityAutoGiveDO.setCreateTime(date);
            this.save(couponActivityAutoGiveDO);
            //            this.baseMapper.insertCouponActivity(couponActivityAutoGiveDO);
        } else {
            // 更新
            couponActivityAutoGiveDO.setUpdateUser(currentUserId);
            couponActivityAutoGiveDO.setUpdateTime(date);
            this.updateById(couponActivityAutoGiveDO);
            //            this.baseMapper.updateCouponActivityById(couponActivityAutoGiveDO);
        }
        return couponActivityAutoGiveDO.getId();
    }

}
