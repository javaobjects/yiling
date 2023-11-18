package com.yiling.admin.b2b.strategy.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.strategy.form.AddStrategyActivityForm;
import com.yiling.admin.b2b.strategy.form.CopyStrategyForm;
import com.yiling.admin.b2b.strategy.form.QueryLotteryStrategyPageFrom;
import com.yiling.admin.b2b.strategy.form.QueryStrategyActivityPageForm;
import com.yiling.admin.b2b.strategy.form.SaveStrategyActivityForm;
import com.yiling.admin.b2b.strategy.form.StopStrategyForm;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityDetailVO;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityPageVO;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityVO;
import com.yiling.admin.b2b.strategy.vo.StrategyAmountLadderVO;
import com.yiling.admin.b2b.strategy.vo.StrategyCycleLadderVO;
import com.yiling.admin.b2b.strategy.vo.StrategyGiftVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
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
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyCycleLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;
import com.yiling.marketing.strategy.enums.StrategyConditionBuyerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 营销活动主表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Slf4j
@Api(tags = "策略满赠-营销活动主表管理接口-运营后台")
@RestController
@RequestMapping("/strategy/activity")
public class StrategyActivityController extends BaseController {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    StrategyActivityRecordApi strategyActivityRecordApi;

    @DubboReference
    StrategyBuyerApi strategyBuyerApi;

    @DubboReference
    StrategySellerApi strategySellerApi;

    @DubboReference
    StrategyEnterpriseGoodsApi strategyEnterpriseGoodsApi;

    @DubboReference
    StrategyPlatformGoodsApi strategyPlatformGoodsApi;

    @DubboReference
    StrategyMemberApi strategyMemberApi;

    @DubboReference
    StrategyPromoterMemberApi strategyPromoterMemberApi;

    @DubboReference
    StrategyStageMemberEffectApi strategyStageMemberEffectApi;

    @DubboReference
    StrategyGiftApi strategyGiftApi;

    @DubboReference
    CouponActivityApi couponActivityApi;

    @DubboReference
    LotteryActivityApi lotteryActivityApi;

    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "分页列表营销活动策略满赠-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyActivityPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryStrategyActivityPageForm form) {
        QueryStrategyActivityPageRequest request = PojoUtils.map(form, QueryStrategyActivityPageRequest.class);
        request.setType(1);
        Page<StrategyActivityDTO> activityDtoPage = strategyActivityApi.pageList(request);
        Page<StrategyActivityPageVO> voPage = PojoUtils.map(activityDtoPage, StrategyActivityPageVO.class);
        for (StrategyActivityPageVO record : voPage.getRecords()) {
            {
                // 创建人名称    创建人手机号
                UserDTO userDTO = userApi.getById(record.getCreateUser());
                record.setCreateUserName(userDTO.getName());
                record.setCreateUserTel(userDTO.getMobile());
            }

            {
                // 活动进度 0-全部 1-未开始 2-进行中 3-已结束
                if (record.getBeginTime().compareTo(new Date()) > 0) {
                    record.setProgress(1);
                } else if (record.getEndTime().compareTo(new Date()) > 0) {
                    record.setProgress(2);
                } else {
                    record.setProgress(3);
                }
                // 状态：1-启用 2-停用 3-废弃
                if (record.getStatus() != 1) {
                    record.setProgress(3);
                }
            }

            {
                Integer times = strategyActivityRecordApi.countRecordByActivityId(record.getId());
                record.setTimes(times);
            }

            {
                record.setLookFlag(true);
                record.setUpdateFlag(1 == record.getProgress() || 2 == record.getProgress());
                record.setCopyFlag(true);
                record.setStopFlag(1 == record.getProgress() || 2 == record.getProgress());
            }
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "查询策略满赠详情-运营后台")
    @GetMapping("queryDetail")
    public Result<StrategyActivityDetailVO> queryDetail(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam("id") Long id) {
        StrategyActivityDTO strategyActivityDTO = strategyActivityApi.queryById(id);
        StrategyActivityDetailVO detailVO = PojoUtils.map(strategyActivityDTO, StrategyActivityDetailVO.class);

        {
            if (StringUtils.isNotBlank(strategyActivityDTO.getPlatformSelected())) {
                List<Integer> platformSelectedList = JSON.parseArray(strategyActivityDTO.getPlatformSelected(), Integer.class);
                detailVO.setPlatformSelected(platformSelectedList);
            }

            if (StringUtils.isNotBlank(strategyActivityDTO.getConditionEnterpriseTypeValue())) {
                List<Integer> conditionEnterpriseTypeValue = JSON.parseArray(strategyActivityDTO.getConditionEnterpriseTypeValue(), Integer.class);
                detailVO.setConditionEnterpriseTypeValue(conditionEnterpriseTypeValue);
            }

            if (StringUtils.isNotBlank(strategyActivityDTO.getConditionUserMemberType())) {
                List<Integer> conditionUserMemberType = JSON.parseArray(strategyActivityDTO.getConditionUserMemberType(), Integer.class);
                detailVO.setConditionUserMemberType(conditionUserMemberType);
            }

            if (StringUtils.isNotBlank(strategyActivityDTO.getMemberRepeatDay())) {
                List<Integer> memberRepeatDay = JSON.parseArray(strategyActivityDTO.getMemberRepeatDay(), Integer.class);
                detailVO.setMemberRepeatDay(memberRepeatDay);
            }

            if (StringUtils.isNotBlank(strategyActivityDTO.getOrderAmountPaymentType())) {
                List<Integer> orderAmountPaymentType = JSON.parseArray(strategyActivityDTO.getOrderAmountPaymentType(), Integer.class);
                detailVO.setOrderAmountPaymentType(orderAmountPaymentType);
            }

            if (StringUtils.isNotBlank(strategyActivityDTO.getConditionOther())) {
                List<Integer> conditionOther = JSON.parseArray(strategyActivityDTO.getConditionOther(), Integer.class);
                detailVO.setConditionOther(conditionOther);
            }

            detailVO.setRunning(false);
            // 活动进度 0-全部 1-未开始 2-进行中 3-已结束
            if (strategyActivityDTO.getBeginTime().compareTo(new Date()) > 0) {
                detailVO.setProgress(1);
            } else if (strategyActivityDTO.getEndTime().compareTo(new Date()) > 0) {
                detailVO.setProgress(2);
                detailVO.setRunning(true);
            } else {
                detailVO.setProgress(3);
            }
            // 状态：1-启用 2-停用 3-废弃
            if (strategyActivityDTO.getStatus() != 1) {
                detailVO.setProgress(3);
            }
        }

        {
            // 商家范围类型（1-全部商家；2-指定商家；）
            if (StrategyConditionSellerTypeEnum.getByType(detailVO.getConditionSellerType()) == StrategyConditionSellerTypeEnum.ASSIGN) {
                detailVO.setStrategySellerLimitCount(strategySellerApi.countSellerByActivityId(id));
            }
        }

        {
            // 商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
            if (StrategyConditionGoodsTypeEnum.getByType(detailVO.getConditionGoodsType()) == StrategyConditionGoodsTypeEnum.PLATFORM) {
                detailVO.setStrategyPlatformGoodsLimitCount(strategyPlatformGoodsApi.countPlatformGoodsByActivityId(id));
            }
            if (StrategyConditionGoodsTypeEnum.getByType(detailVO.getConditionGoodsType()) == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                detailVO.setStrategyEnterpriseGoodsLimitCount(strategyEnterpriseGoodsApi.countEnterpriseGoodsByActivityId(id));
            }
        }

        {
            // 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
            if (StrategyConditionBuyerTypeEnum.getByType(detailVO.getConditionBuyerType()) == StrategyConditionBuyerTypeEnum.ASSIGN) {
                detailVO.setStrategyBuyerLimitCount(strategyBuyerApi.countBuyerByActivityId(id));
            } else if (StrategyConditionBuyerTypeEnum.getByType(detailVO.getConditionBuyerType()) == StrategyConditionBuyerTypeEnum.RANGE) {
                if (CollUtil.isNotEmpty(detailVO.getConditionUserMemberType())) {
                    if (detailVO.getConditionUserMemberType().contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                        detailVO.setStrategyMemberLimitCount(strategyMemberApi.countMemberByActivityId(id));
                    }
                    if (detailVO.getConditionUserMemberType().contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                        detailVO.setStrategyPromoterMemberLimitCount(strategyPromoterMemberApi.countPromoterMemberByActivityId(id));
                    }
                }
            }
        }

        {
            // 购买会员方案
            if (StrategyTypeEnum.getByType(strategyActivityDTO.getStrategyType()) == StrategyTypeEnum.PURCHASE_MEMBER) {
                detailVO.setStrategyStageMemberEffectCount(strategyStageMemberEffectApi.countStageMemberEffectByActivityId(id));
            }
        }

        {
            List<StrategyGiftDTO> giftDtoList = strategyGiftApi.listGiftByActivityIdAndLadderId(id, null);
            // 生效条件&计算规则
            if (StrategyTypeEnum.getByType(detailVO.getStrategyType()) == StrategyTypeEnum.ORDER_AMOUNT) {
                Map<Long, List<StrategyGiftDTO>> giftListMap = giftDtoList.stream().collect(Collectors.groupingBy(StrategyGiftDTO::getLadderId));
                List<StrategyAmountLadderDTO> amountLadderDtoList = strategyActivityApi.listAmountLadderByActivityId(id);
                List<StrategyAmountLadderVO> amountLadderVoList = PojoUtils.map(amountLadderDtoList, StrategyAmountLadderVO.class);
                for (StrategyAmountLadderVO amountLadder : amountLadderVoList) {
                    List<StrategyGiftDTO> dtoList = giftListMap.get(amountLadder.getId());
                    List<StrategyGiftVO> giftVOList = PojoUtils.map(dtoList, StrategyGiftVO.class);
                    for (StrategyGiftVO strategyGiftVO : giftVOList) {
                        if (1 == strategyGiftVO.getType() || 2 == strategyGiftVO.getType()) {
                            CouponActivityDetailDTO couponActivityDetailDTO = couponActivityApi.getActivityCouponById(strategyGiftVO.getGiftId());
                            strategyGiftVO.setGiftName(couponActivityDetailDTO.getName());
                        }
                        if (3 == strategyGiftVO.getType()) {
                            LotteryActivityDTO lotteryActivityDTO = lotteryActivityApi.getById(strategyGiftVO.getGiftId());
                            strategyGiftVO.setGiftName(lotteryActivityDTO.getActivityName());
                        }
                    }
                    amountLadder.setStrategyGiftList(giftVOList);
                }
                detailVO.setStrategyAmountLadderList(amountLadderVoList);
            } else if (StrategyTypeEnum.getByType(detailVO.getStrategyType()) == StrategyTypeEnum.CYCLE_TIME) {
                Map<Long, List<StrategyGiftDTO>> giftListMap = giftDtoList.stream().collect(Collectors.groupingBy(StrategyGiftDTO::getLadderId));
                List<StrategyCycleLadderDTO> cycleLadderDtoList = strategyActivityApi.listCycleLadderByActivityId(id);
                List<StrategyCycleLadderVO> cycleLadderVoList = new ArrayList<>();
                for (StrategyCycleLadderDTO cycleLadderDTO : cycleLadderDtoList) {
                    StrategyCycleLadderVO cycleLadderVO = PojoUtils.map(cycleLadderDTO, StrategyCycleLadderVO.class);
                    if (StringUtils.isNotBlank(cycleLadderDTO.getConditionValue())) {
                        List<Integer> conditionValue = JSON.parseArray(cycleLadderDTO.getConditionValue(), Integer.class);
                        cycleLadderVO.setConditionValue(conditionValue);
                    }
                    List<StrategyGiftDTO> dtoList = giftListMap.get(cycleLadderDTO.getId());
                    List<StrategyGiftVO> giftVOList = PojoUtils.map(dtoList, StrategyGiftVO.class);
                    for (StrategyGiftVO strategyGiftVO : giftVOList) {
                        if (1 == strategyGiftVO.getType() || 2 == strategyGiftVO.getType()) {
                            CouponActivityDetailDTO couponActivityDetailDTO = couponActivityApi.getActivityCouponById(strategyGiftVO.getGiftId());
                            strategyGiftVO.setGiftName(couponActivityDetailDTO.getName());
                        }
                        if (3 == strategyGiftVO.getType()) {
                            LotteryActivityDTO lotteryActivityDTO = lotteryActivityApi.getById(strategyGiftVO.getGiftId());
                            strategyGiftVO.setGiftName(lotteryActivityDTO.getActivityName());
                        }
                    }
                    cycleLadderVO.setStrategyGiftList(giftVOList);
                    cycleLadderVoList.add(cycleLadderVO);
                }
                detailVO.setStrategyCycleLadderList(cycleLadderVoList);
            } else if (StrategyTypeEnum.getByType(detailVO.getStrategyType()) == StrategyTypeEnum.PURCHASE_MEMBER) {
                List<StrategyGiftVO> giftVOList = PojoUtils.map(giftDtoList, StrategyGiftVO.class);
                for (StrategyGiftVO strategyGiftVO : giftVOList) {
                    if (1 == strategyGiftVO.getType() || 2 == strategyGiftVO.getType()) {
                        CouponActivityDetailDTO couponActivityDetailDTO = couponActivityApi.getActivityCouponById(strategyGiftVO.getGiftId());
                        strategyGiftVO.setGiftName(couponActivityDetailDTO.getName());
                    }
                    if (3 == strategyGiftVO.getType()) {
                        LotteryActivityDTO lotteryActivityDTO = lotteryActivityApi.getById(strategyGiftVO.getGiftId());
                        strategyGiftVO.setGiftName(lotteryActivityDTO.getActivityName());
                    }
                }
                detailVO.setStrategyGiftList(giftVOList);
            }
        }
        return Result.success(detailVO);
    }

    @ApiOperation(value = "策略满赠主信息保存--上面的保存按钮")
    @PostMapping("/save")
    public Result<StrategyActivityVO> save(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategyActivityForm form) {
        AddStrategyActivityRequest request = PojoUtils.map(form, AddStrategyActivityRequest.class);
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.setTime(request.getBeginTime());
        //        calendar.add(Calendar.MINUTE, -5);
        //        Date startTime = calendar.getTime();
        //        if (Objects.nonNull(request.getBeginTime()) && startTime.compareTo(new Date()) < 0) {
        //            return Result.failed("生效开始时间不能在5分钟之内");
        //        }
        request.setType(1);
        request.setStatus(1);
        request.setOpUserId(staffInfo.getCurrentUserId());
        StrategyActivityDTO strategyActivityDTO = strategyActivityApi.save(request);
        return Result.success(PojoUtils.map(strategyActivityDTO, StrategyActivityVO.class));
    }

    @ApiOperation(value = "策略满赠主信息保存--具体内容")
    @PostMapping("/saveAll")
    public Result<Object> saveAll(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveStrategyActivityForm form) {
        SaveStrategyActivityRequest request = PojoUtils.map(form, SaveStrategyActivityRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyActivityApi.saveAll(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value = "策略满赠-复制")
    @PostMapping("/copy")
    public Result<StrategyActivityVO> copy(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid CopyStrategyForm form) {
        CopyStrategyRequest request = PojoUtils.map(form, CopyStrategyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        StrategyActivityDTO strategyActivityDTO = strategyActivityApi.copy(request);
        StrategyActivityVO activityVO = PojoUtils.map(strategyActivityDTO, StrategyActivityVO.class);
        return Result.success(activityVO);
    }

    @ApiOperation(value = "策略满赠-停用")
    @PostMapping("/stop")
    public Result<Object> stop(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid StopStrategyForm form) {
        StopStrategyRequest request = PojoUtils.map(form, StopStrategyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyActivityApi.stop(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("停用失败");
    }

    @ApiOperation(value = "分页列表营销活动策略满赠-运营后台")
    @PostMapping("/lotteryPageList")
    public Result<Page<StrategyActivityPageVO>> lotteryPageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryLotteryStrategyPageFrom form) {
        QueryLotteryStrategyPageRequest request = PojoUtils.map(form, QueryLotteryStrategyPageRequest.class);
        Page<StrategyActivityDTO> dtoPage = strategyActivityApi.pageLotteryStrategy(request);
        Page<StrategyActivityPageVO> voPage = PojoUtils.map(dtoPage, StrategyActivityPageVO.class);
        for (StrategyActivityPageVO record : voPage.getRecords()) {
            {
                // 创建人名称    创建人手机号
                UserDTO userDTO = userApi.getById(record.getCreateUser());
                record.setCreateUserName(userDTO.getName());
            }
            {
                // 活动进度 0-全部 1-未开始 2-进行中 3-已结束
                if (record.getBeginTime().compareTo(new Date()) > 0) {
                    record.setProgress(1);
                } else if (record.getEndTime().compareTo(new Date()) > 0) {
                    record.setProgress(2);
                } else {
                    record.setProgress(3);
                }
                // 状态：1-启用 2-停用 3-废弃
                if (record.getStatus() != 1) {
                    record.setProgress(3);
                }
            }
        }
        return Result.success(voPage);
    }
}
