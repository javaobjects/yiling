package com.yiling.admin.b2b.coupon.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.coupon.from.DeleteCouponActivityAutoGiveGoodsLimitFrom;
import com.yiling.admin.b2b.coupon.from.DeleteCouponActivityAutoGiveMemberLimitFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGiveFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGiveGoodsFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGiveMemberFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGiveMemberLimitFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGivePageFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityGiveFailFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityAutoGiveBasicFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityAutoGiveGoodsLimitFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityAutoGiveMemberLimitFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityAutoGiveRulesFrom;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGiveCouponDetailVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGiveDetailVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGiveFailPageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGiveMemberPageVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGivePageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityGivePageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityGoodsPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityPromotionCodeEnum;
import com.yiling.marketing.common.enums.CouponActivityRepeatGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetEnterpriseLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityAutoGiveGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveGoodsLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveMemberDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveOperateRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveMemberRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveFailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveBasicRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveRulesRequest;
import com.yiling.marketing.couponactivityautogive.enums.CouponActivityAutoGiceErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * B2B运营后台 优惠券活动自动发放
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Api(tags = "优惠券-自动发券活动接口-前端页面已经屏蔽，不再使用")
@RestController
@RequestMapping("/couponActivityAutoGive")
public class CouponActivityAutoGiveController extends BaseController {

    @DubboReference
    CouponActivityAutoGiveApi couponActivityAutoGiveApi;
    @DubboReference
    CouponActivityApi         couponActivityApi;
    @DubboReference
    CouponApi                 couponApi;
    @DubboReference
    UserApi                   userApi;
    @DubboReference
    B2bGoodsApi               b2bgoodsApi;
    @DubboReference
    GoodsApi                  goodsApi;
    @DubboReference
    InventoryApi              inventoryApi;
    @DubboReference
    EnterpriseApi             enterpriseApi;
    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;

    @ApiOperation(value = "列表", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<Page<CouponActivityAutoGivePageVo>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityAutoGiveFrom form) {
        QueryCouponActivityAutoGiveRequest request = PojoUtils.map(form, QueryCouponActivityAutoGiveRequest.class);
        Page<CouponActivityAutoGivePageVo> page = PojoUtils.map(couponActivityAutoGiveApi.queryListPage(request),CouponActivityAutoGivePageVo.class);
        // 创建人、修改人id
        List<Long> userIdList = this.getGiveUserIdList(page.getRecords());
        Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
        long nowTime = System.currentTimeMillis();
        Long currentEid = 0L;
        // 查询自动发放优惠券
        Map<Long, Integer> giveCountMap = buildGetCountMapMap(page);

        for (CouponActivityAutoGivePageVo p : page.getRecords()) {
            Long id = p.getId();
            // 已发放数量
            int giveCount = ObjectUtil.isNull(giveCountMap.get(id)) ? 0 : giveCountMap.get(id);
            p.setGiveCount(giveCount);
            // 活动状态
            if (p.getBeginTime().getTime() > nowTime) {
                p.setActivityStatus(1);
            } else if (p.getBeginTime().getTime() <= nowTime && p.getEndTime().getTime() > nowTime) {
                p.setActivityStatus(2);
            } else if (p.getEndTime().getTime() <= nowTime) {
                p.setActivityStatus(3);
            }
            // 停用、作废, 活动状态 = 已结束
            if(!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), p.getStatus())){
                p.setActivityStatus(3);
            }
            // 操作人姓名
            String createUserName = this.getUserNameById(userMap, p.getCreateUser());
            String updateUserName = this.getUserNameById(userMap, p.getUpdateUser());
            p.setCreateUserName(createUserName);
            p.setUpdateUserName(updateUserName);

            // 操作标识
            Integer status = p.getStatus();
            boolean updateFlag = true;
            boolean stopFlag = true;
            boolean scrapFlag = true;

            // 可停用标识
            if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), status)) {
                updateFlag = false;
                stopFlag = false;
            }
            // 可作废标识
            if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
                scrapFlag = false;
            }
            // 可修改标识
            if (giveCount > 0) {
                updateFlag = false;
            }
            if (p.getEndTime().getTime() <= nowTime) {
                updateFlag = false;
            }

            p.setUpdateFlag(updateFlag);
            p.setStopFlag(stopFlag);
            p.setScrapFlag(scrapFlag);
        }
        return Result.success(page);
    }

    private Map<Long, Integer> buildGetCountMapMap(Page<CouponActivityAutoGivePageVo> page) {
        Map<Long, Integer> getCountMap = new HashMap<>();
        if(ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())){
            return getCountMap;
        }
        List<Long> autoGiveIdList = page.getRecords().stream().map(CouponActivityAutoGivePageVo::getId).collect(Collectors.toList());
        // 查询关联的优惠券活动
        List<CouponActivityAutoGiveCouponDTO> autoGiveCouponList = couponActivityAutoGiveApi.getAutoGiveCouponByAutoGiveIdList(autoGiveIdList);
        if(CollUtil.isEmpty(autoGiveCouponList)){
            return getCountMap;
        }
        List<Long> autoGiveIdEffectiveList = autoGiveCouponList.stream().map(CouponActivityAutoGiveCouponDTO::getCouponActivityAutoGiveId).distinct().collect(Collectors.toList());
        Map<Long, List<CouponActivityAutoGiveCouponDTO>> autoGetMap = autoGiveCouponList.stream().collect(Collectors.groupingBy(CouponActivityAutoGiveCouponDTO::getCouponActivityAutoGiveId));
        List<Long> couponActivityIdList = autoGiveCouponList.stream().map(CouponActivityAutoGiveCouponDTO::getCouponActivityId).distinct().collect(Collectors.toList());
        // 已领取优惠券
        QueryHasGiveCouponAutoRequest getRequest = new QueryHasGiveCouponAutoRequest();
        getRequest.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        getRequest.setCouponActivityIds(couponActivityIdList);
        getRequest.setAutoGetIds(autoGiveIdEffectiveList);
        getRequest.setBusinessType(3);
        List<CouponDTO> couponList = couponApi.getHasGiveListByCouponActivityIdList(getRequest);
        if(CollUtil.isEmpty(couponList)){
            return getCountMap;
        }

        Map<Long, List<CouponDTO>> couponMap = couponList.stream().collect(Collectors.groupingBy(CouponDTO::getCouponActivityAutoId));
        for (Long autoGiveId : autoGiveIdEffectiveList) {
            int getCount = 0;
            List<CouponDTO> couponDTOS = couponMap.get(autoGiveId);
            if(CollUtil.isNotEmpty(couponDTOS)){
                getCount = couponDTOS.size();
            }
            getCountMap.put(autoGiveId, getCount);
        }
        return getCountMap;
    }

    @Log(title = "自动发券活动-新增",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "列表-保存/编辑基本信息", httpMethod = "POST")
    @PostMapping("/saveOrUpdateBasic")
    public Result<String> saveOrUpdateBasic(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGiveBasicFrom form) {
        SaveCouponActivityAutoGiveBasicRequest request = PojoUtils.map(form, SaveCouponActivityAutoGiveBasicRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Long id = couponActivityAutoGiveApi.saveOrUpdateBasic(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    @Log(title = "自动发券活动-修改发放规则",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-保存/编辑规则信息", httpMethod = "POST")
    @PostMapping("/saveOrUpdateRules")
    public Result<String> saveOrUpdateRules(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGiveRulesFrom form) {
        // 必填校验
        rulesCheck(form);

        SaveCouponActivityAutoGiveRulesRequest request = PojoUtils.map(form, SaveCouponActivityAutoGiveRulesRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setCurrentUserId(1L);
        Long id = couponActivityAutoGiveApi.saveOrUpdateRules(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    private void rulesCheck(SaveCouponActivityAutoGiveRulesFrom form) {
        Integer type = form.getType();
        if (ObjectUtil.isNull(type) || type == 0) {
            throw new BusinessException(CouponErrorCode.AUTO_GIVE_TYPE_ERROR);
        }
        /* 订单累计金额必填 */
        if(type.intValue() == 1){
            // 平台限制（1-全部平台；2-部分平台）
            if (ObjectUtil.isNull(form.getConditionOrderPlatform()) || form.getConditionOrderPlatform() == 0) {
                throw new BusinessException(CouponErrorCode.PLATFORM_LIMIT_ERROR);
            }
            // 2
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), form.getConditionOrderPlatform())) {
                if (CollUtil.isEmpty(form.getConditionOrderPlatformValueList())) {
                    throw new BusinessException(CouponErrorCode.PLATFORM_LIMIT_VALUE_ERROR);
                }
            }

            // 支付方式限制（1-全部支付方式；2-部分支付方式）
            if (ObjectUtil.isNull(form.getConditionPaymethod()) || form.getConditionPaymethod() == 0) {
                throw new BusinessException(CouponErrorCode.PAY_METHOD_LIMIT_ERROR);
            }
            // 2
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), form.getConditionPaymethod())) {
                if (CollUtil.isEmpty(form.getConditionPaymethodValueList())) {
                    throw new BusinessException(CouponErrorCode.PAY_METHOD_LIMIT_VALUE_ERROR);
                }
            }

            // 订单状态限制（1-全部订单状态；2-部分订单状态）
            if (ObjectUtil.isNull(form.getConditionOrderStatus()) || form.getConditionOrderStatus() == 0) {
                throw new BusinessException(CouponErrorCode.ORDER_STATUS_LIMIT_ERROR);
            }
            // 2
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), form.getConditionOrderStatus())) {
                if (CollUtil.isEmpty(form.getConditionOrderStatusValueList())) {
                    throw new BusinessException(CouponErrorCode.ORDER_STATUS_VALUE_ERROR);
                }
            }

            // 指定商品
            Integer conditionGoods = form.getConditionGoods();
            if (ObjectUtil.isNull(conditionGoods) || conditionGoods == 0) {
                throw new BusinessException(CouponErrorCode.GOODS_LIMIT_ERROR);
            }
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), conditionGoods)) {
                // 部分商品可用
                List<CouponActivityAutoGiveGoodsLimitDTO> goodsLimits = couponActivityAutoGiveApi.getGoodsLimitByAutoGiveId(form.getId());
                if (CollUtil.isEmpty(goodsLimits)) {
                    throw new BusinessException(CouponErrorCode.GOODS_LIMIT_NULL_ERROR);
                }
            }

            // 累积金额
            if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getCode(), type)) {
                Integer cumulative = form.getCumulative();
                if (ObjectUtil.isNull(cumulative) || cumulative <= 0) {
                    throw new BusinessException(CouponErrorCode.AUTO_GIVE_CUMULATIVE_ERROR);
                }
            }
        }

        /* 会员自动发放必填 */
        if(type.intValue() == 2){
            // 是否有推广码 1-是，2-否
            if (ObjectUtil.isNull(form.getConditionPromotionCode()) || form.getConditionPromotionCode() == 0) {
                throw new BusinessException(CouponErrorCode.PROMOTION_CODE_ERROR);
            }

            // 选择是否有推广码，选择【是\否】只能选择一次性发放
            if (!CouponActivityPromotionCodeEnum.ALL.getCode().equals(form.getConditionPromotionCode())) {
                if(Integer.valueOf(2).equals(form.getRepeatGive())) {
                    throw new BusinessException(CouponErrorCode.PROMOTION_REPEAT_CODE_ERROR);
                }
            }
        }

        /* 企业类型限制（1-全部企业类型；2-部分企业类型） */
        if (ObjectUtil.isNull(form.getConditionEnterpriseType()) || form.getConditionEnterpriseType() == 0) {
            throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_LIMIT_ERROR);
        }
        // 2
        if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), form.getConditionEnterpriseType())) {
            if (CollUtil.isEmpty(form.getConditionEnterpriseTypeValueList())) {
                throw new BusinessException(CouponErrorCode.ENTERPRISE_TYPE_VALUE_ERROR);
            }
        }

        /* 用户类型 */
        Integer conditionUserType = form.getConditionUserType();
        if (ObjectUtil.isNull(conditionUserType) || conditionUserType == 0) {
            throw new BusinessException(CouponErrorCode.USER_TYPE_LIMIT_ERROR);
        }
        if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), conditionUserType)) {
            // 部分会员
            List<CouponActivityAutoGiveEnterpriseLimitDTO> enterpriseLimits = couponActivityAutoGiveApi.getByAutoGiveId(form.getId());
            if (CollUtil.isEmpty(enterpriseLimits)) {
                throw new BusinessException(CouponErrorCode.MEMBER_LIMIT_NULL_ERROR);
            }
        }

        /* 是否重复发放 */
        Integer repeatGive = form.getRepeatGive();
        if (ObjectUtil.isNull(repeatGive) || repeatGive.intValue() <= 0) {
            throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GIVE_REPEAT_GIVE_NULL);
        }
        /* 最多发放次数 */
        if (ObjectUtil.equal(CouponActivityRepeatGiveTypeEnum.MANY.getCode(), repeatGive)) {
            // 多次发放、每月发放
            Integer maxGiveNum = form.getMaxGiveNum();
            if (ObjectUtil.isNull(maxGiveNum) || maxGiveNum <= 0) {
                throw new BusinessException(CouponActivityAutoGiceErrorCode.AUTO_GIVE_REPEAT_GIVE_MAX_NUM_NULL);
            }
            // 每月发放
            if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode(), type)
                    || ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ENTERPRISE_POPULARIZE.getCode(), type)) {
                // 校验时间
                CouponActivityAutoGiveDTO autoGiveDb = couponActivityAutoGiveApi.getAutoGiveById(form.getId());
                Date beginTime = autoGiveDb.getBeginTime();
                Date endTime = autoGiveDb.getEndTime();
                // 取相差月份
                long monthDiff = DateUtil.betweenMonth(beginTime, endTime, false);
                if (maxGiveNum > monthDiff) {
                    throw new BusinessException(CouponErrorCode.AUTO_GIVE_MONTH_ERROR);
                }
            }
        }
    }

    @ApiOperation(value = "列表-查看详情", httpMethod = "GET")
    @GetMapping("/getDetail")
    public Result<CouponActivityAutoGiveDetailVO> getCouponActivity(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityAutoGiveDetailVO autoGiveDetail = PojoUtils.map(couponActivityAutoGiveApi.getDetailById(id),CouponActivityAutoGiveDetailVO.class);
        if(ObjectUtil.isNotNull(autoGiveDetail)){
            // 关联优惠券列表
            List<CouponActivityAutoGiveCouponDTO> list = couponActivityAutoGiveApi.getAutoGiveCouponByAutoGiveIdList(Arrays.asList(autoGiveDetail.getId()));
            if(CollUtil.isEmpty(list)){
                autoGiveDetail.setCouponActivityList(ListUtil.empty());
            } else {
                // 查询优惠券
                List<Long> couponActivityIdList = list.stream().map(CouponActivityAutoGiveCouponDTO::getCouponActivityId).distinct().collect(Collectors.toList());
                Map<Long, CouponActivityDetailDTO> activityMap = new HashMap<>();
                List<CouponActivityDetailDTO> activityList = couponActivityApi.getCouponActivityById(couponActivityIdList);
                if(CollUtil.isNotEmpty(activityList)){
                    activityMap = activityList.stream().collect(Collectors.toMap(a -> a.getId(), a -> a, (v1,v2) -> v1));
                }

                List<CouponActivityAutoGiveCouponDetailVO> couponActivityList = new ArrayList<>();
                CouponActivityAutoGiveCouponDetailVO detail;
                for (CouponActivityAutoGiveCouponDTO dto : list) {
                    detail = new CouponActivityAutoGiveCouponDetailVO();
                    Long couponActivityId = dto.getCouponActivityId();
                    detail.setCouponActivityId(couponActivityId);
                    detail.setGiveNum(dto.getGiveNum());

                    CouponActivityDetailDTO couponActivity = activityMap.get(couponActivityId);
                    if(ObjectUtil.isNotNull(couponActivity)){
                        detail.setEnterpriseLimit(couponActivity.getEnterpriseLimit());
                    }
                    couponActivityList.add(detail);
                }
                autoGiveDetail.setCouponActivityList(couponActivityList);
            }
            // 未开始、已开始状态
            boolean running = false;
            long nowTime = System.currentTimeMillis();
            Date beginTime = autoGiveDetail.getBeginTime();
            Date endTime = autoGiveDetail.getEndTime();
            if (beginTime.getTime() <= nowTime && endTime.getTime() > nowTime) {
                running = true;
            }
            autoGiveDetail.setRunning(running);
        }
        return Result.success(autoGiveDetail);
    }

    @Log(title = "自动发券活动-停用",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-停用", httpMethod = "GET")
    @GetMapping("/stop")
    public Result<Boolean> stop(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityAutoGiveOperateRequest request = new CouponActivityAutoGiveOperateRequest();
        request.setId(id);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGiveApi.stop(request));
    }

    @Log(title = "自动发券活动-启用",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-启用", httpMethod = "GET")
    @GetMapping("/enable")
    public Result<Boolean> enable(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityAutoGiveOperateRequest request = new CouponActivityAutoGiveOperateRequest();
        request.setId(id);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGiveApi.enable(request));
    }

    @Log(title = "自动发券活动-作废",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-作废", httpMethod = "GET")
    @GetMapping("/scrap")
    public Result<Boolean> scrap(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityAutoGiveOperateRequest request = new CouponActivityAutoGiveOperateRequest();
        request.setId(id);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGiveApi.scrap(request));
    }

    @ApiOperation(value = "设置商品-查询商品", httpMethod = "POST")
    @PostMapping("/queryGoodsListPage")
    public Result<Page<CouponActivityGoodsPageVO>> queryGoodsListPage(@CurrentUser CurrentAdminInfo adminInfo,
                                                                      @RequestBody QueryCouponActivityAutoGiveGoodsFrom from) {
        QueryCouponActivityAutoGiveGoodsRequest request = PojoUtils.map(from, QueryCouponActivityAutoGiveGoodsRequest.class);
        Page<CouponActivityGoodsPageVO> page = PojoUtils.map(this.queryGoodsListPage(request), CouponActivityGoodsPageVO.class);
        return Result.success(page);
    }

    public Page<CouponActivityGoodsDTO> queryGoodsListPage(QueryCouponActivityAutoGiveGoodsRequest request) {
        if (ObjectUtil.isNull(request)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        Page<CouponActivityGoodsDTO> page = request.getPage();

        // 查询商品信息
        QueryGoodsPageListRequest goodsRequest = new QueryGoodsPageListRequest();
        goodsRequest.setCurrent(request.getCurrent());
        goodsRequest.setSize(request.getSize());
        if (ObjectUtil.isNotNull(request.getEid())) {
            List<Long> eidList = new ArrayList<>();
            eidList.add(request.getEid());
            goodsRequest.setEidList(eidList);
        }
        if (ObjectUtil.isNotNull(request.getGoodsId())) {
            goodsRequest.setGoodsId(request.getGoodsId());
        }
        if (StrUtil.isNotBlank(request.getGoodsName())) {
            goodsRequest.setName(request.getGoodsName());
        }
        goodsRequest.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
        goodsRequest.setIsAvailableQty(1);
        Page<GoodsListItemBO> goodsPage = b2bgoodsApi.queryB2bGoodsPageList(goodsRequest);
        List<CouponActivityGoodsDTO> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(goodsPage.getRecords())) {
            // 查询库存
            List<Long> goodsIdList = goodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
            Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
            CouponActivityGoodsDTO couponActivityGoods;
            int index = 0;
            for (GoodsListItemBO goods : goodsPage.getRecords()) {
                couponActivityGoods = new CouponActivityGoodsDTO();
                // todo coupon B2B查询商品接口 goodsApi.queryB2bGoodsPageList，（因搜索条件可能只有商品名称）需提供查es接口
                couponActivityGoods.setEid(goods.getEid());
                couponActivityGoods.setEname(goods.getEname());
                couponActivityGoods.setGoodsId(goods.getId());
                couponActivityGoods.setGoodsName(goods.getName());
                couponActivityGoods.setGoodsType(goods.getGoodsType());
                couponActivityGoods.setSellSpecifications(goods.getSellSpecifications());
                couponActivityGoods.setPrice(goods.getPrice());
                couponActivityGoods.setSellUnit(goods.getSellUnit());
                // 商品库存
                Long inventory = inventoryMap.get(goods.getId());
                if (ObjectUtil.isNull(inventory) || inventory.longValue() < 0) {
                    inventory = 0L;
                }
                couponActivityGoods.setGoodsInventory(inventory);

                list.add(index, couponActivityGoods);
                index++;
            }
        }
        page = PojoUtils.map(goodsPage, CouponActivityGoodsDTO.class);
        page.setRecords(list);
        return page;
    }

    @ApiOperation(value = "设置商品-查询已添加商品", httpMethod = "POST")
    @PostMapping("/queryGoodsLimitListPage")
    public Result<Page<CouponActivityGoodsPageVO>> queryGoodsLimitListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityAutoGiveGoodsFrom from) {
        QueryCouponActivityAutoGiveGoodsRequest request = PojoUtils.map(from, QueryCouponActivityAutoGiveGoodsRequest.class);
        Page<CouponActivityGoodsDTO> goodsDTOPage = couponActivityAutoGiveApi.queryGoodsLimitListPage(request);
        buildGoodsLimitPage(goodsDTOPage);
        Page<CouponActivityGoodsPageVO> page = PojoUtils.map(goodsDTOPage,CouponActivityGoodsPageVO.class);
        return Result.success(page);
    }

    public void buildGoodsLimitPage(Page<CouponActivityGoodsDTO> page) {
        List<Long> goodsIdList = page.getRecords().stream().map(CouponActivityGoodsDTO::getGoodsId).collect(Collectors.toList());

        // 根据goodsIds批量查询 商品基础信息
        Map<Long, GoodsDTO> goodsInfoMap = new HashMap<>();
        List<GoodsDTO> goodsInfoList = goodsApi.batchQueryInfo(goodsIdList);
        if (CollUtil.isNotEmpty(goodsInfoList)) {
            goodsInfoMap = goodsInfoList.stream().collect(Collectors.toMap(g -> g.getId(), g -> g, (v1, v2) -> v1));
        }
        // 商品id集合查询商品状态、价格
        Map<Long, B2bGoodsDTO> goodsStatusPriceMap = new HashMap<>();
        List<B2bGoodsDTO> goodsStatusPriceList = b2bgoodsApi.getB2bGoodsListByGoodsIds(goodsIdList);
        if (CollUtil.isNotEmpty(goodsStatusPriceList)) {
            goodsStatusPriceMap = goodsStatusPriceList.stream().collect(Collectors.toMap(g -> g.getGoodsId(), g -> g, (v1, v2) -> v1));
        }
        // 统计商品库存
        Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);

        // 获取商品基本信息、状态、价格、统计商品库存
        for (CouponActivityGoodsDTO entity : page.getRecords()) {
            GoodsDTO goodsInfo = goodsInfoMap.get(entity.getGoodsId());
            B2bGoodsDTO goodsStatusPrice = goodsStatusPriceMap.get(entity.getGoodsId());
            if (ObjectUtil.isNotNull(goodsInfo)) {
                entity.setGoodsName(goodsInfo.getName());
                entity.setGoodsType(goodsInfo.getGoodsType());
                entity.setSellSpecifications(goodsInfo.getSellSpecifications());
                entity.setSellUnit(goodsInfo.getSellUnit());
            }
            if (ObjectUtil.isNotNull(goodsStatusPrice)) {
                entity.setGoodsStatus(goodsStatusPrice.getGoodsStatus());
                entity.setPrice(goodsInfo.getPrice());
            }
            // 商品库存
            Long inventory = inventoryMap.get(entity.getId());
            if (ObjectUtil.isNull(inventory) || inventory.longValue() < 0) {
                inventory = 0L;
            }
            entity.setGoodsInventory(inventory);
        }
    }

    @Log(title = "自动发券活动-添加可发放商品",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "设置商品-添加商品", httpMethod = "POST")
    @PostMapping("/saveGoodsLimit")
    public Result<Boolean> saveGoodsLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGiveGoodsLimitFrom form) {
        SaveCouponActivityAutoGiveGoodsLimitRequest request = PojoUtils.map(form, SaveCouponActivityAutoGiveGoodsLimitRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGiveApi.saveGoodsLimit(request));
    }

    @Log(title = "自动发券活动-删除可发放商品",businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "设置商品-删除商品", httpMethod = "POST")
    @PostMapping("/deleteGoodsLimit")
    public Result<Boolean> deleteGoodsLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteCouponActivityAutoGiveGoodsLimitFrom form) {
        DeleteCouponActivityAutoGiveGoodsLimitRequest request = PojoUtils.map(form, DeleteCouponActivityAutoGiveGoodsLimitRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGiveApi.deleteGoodsLimit(request));
    }

    @ApiOperation(value = "设置会员-查询会员企业", httpMethod = "POST")
    @PostMapping("/queryMemberListPage")
    public Result<Page<CouponActivityAutoGiveMemberPageVO>> queryMemberListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityAutoGiveMemberFrom from) {
        QueryCouponActivityAutoGiveMemberRequest request = PojoUtils.map(from, QueryCouponActivityAutoGiveMemberRequest.class);
        Page<CouponActivityAutoGiveMemberPageVO> page = PojoUtils.map(this.queryEnterpriseListPage(request),CouponActivityAutoGiveMemberPageVO.class);
        return Result.success(page);
    }

    public Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseListPage(QueryCouponActivityAutoGiveMemberRequest request) {
        Page<CouponActivityAutoGiveMemberDTO> page = request.getPage();
        if(ObjectUtil.isNull(request)){
            return page;
        }
        // 查询有效的会员信息
        QueryEnterpriseMemberRequest memberRequest = new QueryEnterpriseMemberRequest();
        memberRequest.setCurrent(request.getCurrent());
        memberRequest.setSize(request.getSize());
        memberRequest.setEid(request.getEid());
        memberRequest.setEname(request.getEname());
        Page<EnterpriseMemberBO> memberPage = enterpriseMemberApi.queryEnterpriseMemberPage(memberRequest);
        if(ObjectUtil.isNull(memberPage) || CollUtil.isEmpty(memberPage.getRecords())){
            return page;
        }
        return PojoUtils.map(memberPage, CouponActivityAutoGiveMemberDTO.class);
    }

    @ApiOperation(value = "设置会员-查询已添加会员企业", httpMethod = "POST")
    @PostMapping("/queryMemberLimitListPage")
    public Result<Page<CouponActivityAutoGiveMemberPageVO>> queryMemberLimitListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityAutoGiveMemberLimitFrom from) {
        QueryCouponActivityAutoGiveMemberLimitRequest request = PojoUtils.map(from, QueryCouponActivityAutoGiveMemberLimitRequest.class);
        Page<CouponActivityAutoGiveMemberPageVO> page = PojoUtils.map(couponActivityAutoGiveApi.queryEnterpriseLimitListPage(request),CouponActivityAutoGiveMemberPageVO.class);
        return Result.success(page);
    }

    @Log(title = "自动发券活动-添加可发放会员企业",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "设置会员-添加会员企业", httpMethod = "POST")
    @PostMapping("/saveEnterpriseLimit")
    public Result<Boolean> saveEnterpriseLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGiveMemberLimitFrom form) {
        SaveCouponActivityAutoGiveMemberLimitRequest request = PojoUtils.map(form, SaveCouponActivityAutoGiveMemberLimitRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGiveApi.saveEnterpriseLimit(request));
    }

    @Log(title = "自动发券活动-删除可发放会员企业",businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "设置商家-删除会员企业", httpMethod = "POST")
    @PostMapping("/deleteEnterpriseLimit")
    public Result<Boolean> deleteEnterpriseLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteCouponActivityAutoGiveMemberLimitFrom form) {
        DeleteCouponActivityAutoGiveMemberLimitRequest request = PojoUtils.map(form, DeleteCouponActivityAutoGiveMemberLimitRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGiveApi.deleteEnterpriseLimit(request));
    }

    @ApiOperation(value = "列表-已发放优惠券查看", httpMethod = "POST")
    @PostMapping("/queryAutoGiveListPage")
    public Result<Page<CouponActivityGivePageVo>> queryAutoGiveListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityAutoGivePageFrom form) {
        QueryCouponActivityGivePageRequest request = PojoUtils.map(form, QueryCouponActivityGivePageRequest.class);
        request.setEid(0L);
        request.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        Page<CouponActivityGiveDTO> page = couponActivityAutoGiveApi.queryGiveListPage(request);
        if(ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())){
            List<Long> userIdList = page.getRecords().stream().map(CouponActivityGiveDTO::getGetUserId).distinct().collect(Collectors.toList());
            Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
            List<Long> eidList = page.getRecords().stream().map(CouponActivityGiveDTO::getEid).distinct().collect(Collectors.toList());
            Map<Long, EnterpriseDTO> enterpriseMap = this.getEnterpriseMapByIds(eidList);
            page.getRecords().forEach(p -> {
                String getUserName = this.getUserNameById(userMap, p.getGetUserId());
                p.setGetUserName(getUserName);
                EnterpriseDTO enterprise = enterpriseMap.get(p.getEid());
                if(ObjectUtil.isNotNull(enterprise)){
                    p.setEname(enterprise.getName());
                }
            });
        }
        return Result.success(PojoUtils.map(page,CouponActivityGivePageVo.class));
    }

    @ApiOperation(value = "自动发放失败列表", httpMethod = "POST")
    @PostMapping("/queryGiveFailListPage")
    public Result<Page<CouponActivityAutoGiveFailPageVo>> queryGiveFailListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityGiveFailFrom form) {
        QueryCouponActivityGiveFailRequest request = PojoUtils.map(form, QueryCouponActivityGiveFailRequest.class);
        Page<CouponActivityAutoGiveFailPageVo> page = PojoUtils.map(couponActivityAutoGiveApi.queryGiveFailListPage(request),CouponActivityAutoGiveFailPageVo.class);
        return Result.success(page);
    }

    @Log(title = "自动发券活动-运营处理发放失败",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "运营处理", httpMethod = "POST")
    @PostMapping("/autoGive")
    public Result<String> autoGive(@CurrentUser CurrentAdminInfo adminInfo, @NotNull @RequestParam("id") Long id) {
        CouponActivityAutoGiveRequest request = new CouponActivityAutoGiveRequest();
        request.setId(id);
        request.setOpUserId(adminInfo.getCurrentUserId());
        // 查询用户信息
        String userName = "";
        UserDTO user = userApi.getById(adminInfo.getCurrentUserId());
        if (ObjectUtil.isNotNull(user)) {
            userName = user.getUsername();
        }
        request.setUserName(userName);
        couponActivityAutoGiveApi.autoGive(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    public Map<Long, UserDTO> getUserMapByIds(List<Long> userIds) {
        Map<Long, UserDTO> userMap = new HashMap<>();
        if (CollUtil.isEmpty(userIds)) {
            return userMap;
        }
        List<UserDTO> userList = userApi.listByIds(userIds);
        if (CollUtil.isEmpty(userList)) {
            return userMap;
        }
        return userList.stream().collect(Collectors.toMap(u -> u.getId(), u -> u, (v1, v2) -> v1));
    }

    public List<Long> getGiveUserIdList(List<CouponActivityAutoGivePageVo> list) {
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        Set<Long> userIdSet = new HashSet<>();
        list.forEach(p -> {
            if (ObjectUtil.isNotNull(p.getCreateUser())) {
                userIdSet.add(p.getCreateUser());
            }
            if (ObjectUtil.isNotNull(p.getUpdateUser())) {
                userIdSet.add(p.getUpdateUser());
            }
        });
        return new ArrayList<>(userIdSet);
    }

    public String getUserNameById(Map<Long, UserDTO> userMap, Long userId) {
        if (MapUtil.isEmpty(userMap) || ObjectUtil.isNull(userId)) {
            return "";
        }
        UserDTO user = userMap.get(userId);
        if (ObjectUtil.isNotNull(user)) {
            return user.getName();
        }
        return "";
    }

    public Map<Long, EnterpriseDTO> getEnterpriseMapByIds(List<Long> eids) {
        Map<Long, EnterpriseDTO> userMap = new HashMap<>();
        if (CollUtil.isEmpty(eids)) {
            return userMap;
        }
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eids);
        if (CollUtil.isEmpty(enterpriseList)) {
            return userMap;
        }
        return enterpriseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e, (v1, v2) -> v1));
    }
}
