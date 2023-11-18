package com.yiling.admin.b2b.paypromotion.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
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
import com.yiling.admin.b2b.paypromotion.from.AddAllPayPromotionActivityForm;
import com.yiling.admin.b2b.paypromotion.from.AddPayPromotionActivityForm;
import com.yiling.admin.b2b.paypromotion.from.QueryPayPromotionActivityPageForm;
import com.yiling.admin.b2b.paypromotion.vo.PayPromotionActivityDetailVO;
import com.yiling.admin.b2b.paypromotion.vo.PayPromotionActivityPageVO;
import com.yiling.admin.b2b.paypromotion.vo.PayPromotionActivityVO;
import com.yiling.admin.b2b.paypromotion.vo.PayPromotionCalculateVO;
import com.yiling.admin.b2b.paypromotion.vo.PayPromotionParticipatePageVO;
import com.yiling.admin.b2b.strategy.form.AddStrategyActivityForm;
import com.yiling.admin.b2b.strategy.form.CopyStrategyForm;
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
import com.yiling.marketing.paypromotion.api.PayPromotionActivityApi;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionCalculateRuleDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionParticipateDTO;
import com.yiling.marketing.paypromotion.dto.request.AddPayPromotionActivityRequest;
import com.yiling.marketing.paypromotion.dto.request.QueryPayPromotionActivityPageRequest;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionActivityRequest;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.api.StrategyBuyerApi;
import com.yiling.marketing.strategy.api.StrategyEnterpriseGoodsApi;
import com.yiling.marketing.strategy.api.StrategyMemberApi;
import com.yiling.marketing.strategy.api.StrategyPlatformGoodsApi;
import com.yiling.marketing.strategy.api.StrategyPromoterMemberApi;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyCycleLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;
import com.yiling.marketing.strategy.enums.StrategyConditionBuyerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
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
@Api(tags = "支付促销-支付促销活动主表管理接口-运营后台")
@RestController
@RequestMapping("/payPromption/activity")
public class PayPromotionController extends BaseController {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    PayPromotionActivityApi payPromotionActivityApi;

    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;

    @DubboReference
    UserApi userApi;

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
    OrderApi orderApi;


    @ApiOperation(value = "查询支付促销详情-运营后台")
    @GetMapping("queryDetail")
    public Result<PayPromotionActivityDetailVO> queryDetail(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam("id") Long id) {
        //StrategyActivityDTO strategyActivityDTO = strategyActivityApi.queryById(id);
        log.info("queryDetail" + id);
        PayPromotionActivityDTO strategyActivityDTO = payPromotionActivityApi.getById(id);

        PayPromotionActivityDetailVO detailVO = PojoUtils.map(strategyActivityDTO, PayPromotionActivityDetailVO.class);

        {
            if (StringUtils.isNotBlank(strategyActivityDTO.getConditionEnterpriseTypeValue())) {
                List<Integer> conditionEnterpriseTypeValue = JSON.parseArray(strategyActivityDTO.getConditionEnterpriseTypeValue(), Integer.class);
                detailVO.setConditionEnterpriseTypeValue(conditionEnterpriseTypeValue);
            }


            if (StringUtils.isNotBlank(strategyActivityDTO.getPayType())) {
                List<Integer> conditionOther = JSON.parseArray(strategyActivityDTO.getPayType(), Integer.class);
                detailVO.setPayType(conditionOther);
            }

            if (StringUtils.isNotBlank(strategyActivityDTO.getConditionOther())) {
                List<Integer> conditionOther = JSON.parseArray(strategyActivityDTO.getConditionOther(), Integer.class);
                detailVO.setConditionOther(conditionOther);
            }

            if (StringUtils.isNotBlank(strategyActivityDTO.getConditionUserMemberType())) {
                List<Integer> conditionUserMemberType = JSON.parseArray(strategyActivityDTO.getConditionUserMemberType(), Integer.class);
                detailVO.setConditionUserMemberType(conditionUserMemberType);
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
                detailVO.setStrategySellerLimitCount(strategySellerApi.countSellerByActivityIdForPayPromotion(id));
            }
        }

        {
            // 商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
            if (StrategyConditionGoodsTypeEnum.getByType(detailVO.getConditionGoodsType()) == StrategyConditionGoodsTypeEnum.PLATFORM) {
                detailVO.setStrategyPlatformGoodsLimitCount(strategyPlatformGoodsApi.countPlatformGoodsByActivityIdForPayPromotion(id));
            }
            if (StrategyConditionGoodsTypeEnum.getByType(detailVO.getConditionGoodsType()) == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                detailVO.setStrategyEnterpriseGoodsLimitCount(strategyEnterpriseGoodsApi.countEnterpriseGoodsByActivityIdForPayPromotion(id));
            }
        }

        {
            // 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
            if (StrategyConditionBuyerTypeEnum.getByType(detailVO.getConditionBuyerType()) == StrategyConditionBuyerTypeEnum.ASSIGN) {
                detailVO.setStrategyBuyerLimitCount(strategyBuyerApi.countBuyerByActivityIdForPayPromotion(id));

            } else if (StrategyConditionBuyerTypeEnum.getByType(detailVO.getConditionBuyerType()) == StrategyConditionBuyerTypeEnum.RANGE) {
                if (StrategyConditionUserMemberTypeEnum.getByType(detailVO.getConditionUserType()) == StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER) {
                    detailVO.setStrategyMemberLimitCount(strategyBuyerApi.countMemberByActivityId(id));
                }
                if (StrategyConditionUserMemberTypeEnum.getByType(detailVO.getConditionUserType()) == StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER) {
                    detailVO.setStrategyPromoterMemberLimitCount(strategyBuyerApi.countPromoterMemberByActivityId(id));
                }
            }
        }

        {
            List<PayPromotionCalculateRuleDTO> giftDtoList = payPromotionActivityApi.getPayCalculateRuleList(id);
            // 生效条件&计算规则
            detailVO.setCalculateRules(PojoUtils.map(giftDtoList, PayPromotionCalculateVO.class));


        }
        return Result.success(detailVO);
    }


    @ApiOperation(value = "主信息保存--上面的保存按钮")
    @PostMapping("/saveBasic")
    public Result<Long> saveOrUpdateBasic(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddPayPromotionActivityForm form) {
        log.info("saveOrUpdateBasic" + form);
        AddPayPromotionActivityRequest request = PojoUtils.map(form, AddPayPromotionActivityRequest.class);
        request.setStatus(1);
        request.setOpUserId(staffInfo.getCurrentUserId());
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        request.setCreateTel(userDTO.getMobile());
        request.setCreateUserName(userDTO.getName());
        Long id = payPromotionActivityApi.saveBasic(request);
        return Result.success(id);
    }

    @ApiOperation(value = "分页列表支付促销活动-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PayPromotionActivityPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryPayPromotionActivityPageForm form) {
        log.info("pageList" + form);
        QueryPayPromotionActivityPageRequest request = PojoUtils.map(form, QueryPayPromotionActivityPageRequest.class);
        Page<PayPromotionActivityPageVO> result = new Page<>();
        // 创建人姓名，创建人手机号处理
        if (ObjectUtil.isNotNull(request.getStartTime())) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
            request.setStopTime(DateUtil.endOfDay(request.getStopTime()));
        }
        Page<PayPromotionActivityDTO> activityDtoPage = payPromotionActivityApi.pageList(request);
        Page<PayPromotionActivityPageVO> voPage = PojoUtils.map(activityDtoPage, PayPromotionActivityPageVO.class);
        if (CollectionUtils.isEmpty(voPage.getRecords())) {
            Result.success(result);
        }
        List<Long> ids = activityDtoPage.getRecords().stream().map(PayPromotionActivityDTO::getId).collect(Collectors.toList());
        for (PayPromotionActivityPageVO record : voPage.getRecords()) {
            {
                // 创建人名称    创建人手机号
                UserDTO userDTO = userApi.getById(record.getCreateUser());
                record.setCreateUserName(userDTO.getName());
                record.setCreateUserTel(userDTO.getMobile());
            }
            if(StringUtils.isNotEmpty(record.getPayType())){
                List<Long> conditionOther = JSON.parseArray(record.getPayType(), Long.class);
                String payType = conditionOther.stream().map(item -> PaymentMethodEnum.getByCode(item).getName()).collect(Collectors.joining(","));
                record.setPayType(payType);
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
                record.setLookFlag(true);
                record.setUpdateFlag(1 == record.getProgress() || 2 == record.getProgress());
                record.setCopyFlag(true);
                record.setStopFlag(1 == record.getProgress() || 2 == record.getProgress());
            }


        }
        return Result.success(voPage);
    }


    @ApiOperation(value = "支付促销-复制")
    @PostMapping("/copy")
    public Result<PayPromotionActivityDetailVO> copy(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid CopyStrategyForm form) {
        log.info("copy" + form);
        CopyStrategyRequest request = PojoUtils.map(form, CopyStrategyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        PayPromotionActivityDTO payPromotionActivityDTO = payPromotionActivityApi.copy(request);
        return Result.success(PojoUtils.map(payPromotionActivityDTO,PayPromotionActivityDetailVO.class));
    }

    @ApiOperation(value = "支付促销-停用")
    @PostMapping("/stop")
    public Result<Object> stop(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid StopStrategyForm form) {
        StopStrategyRequest request = PojoUtils.map(form, StopStrategyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = payPromotionActivityApi.stop(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("停用失败");
    }

    @ApiOperation(value = "信息保存--具体内容")
    @PostMapping("/saveRule")
    public Result<Object> saveAll(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddAllPayPromotionActivityForm form) {
        log.info("saveRule" + form);
        SavePayPromotionActivityRequest request = PojoUtils.map(form, SavePayPromotionActivityRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = payPromotionActivityApi.saveAll(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value = "参与次数分页列表")
    @PostMapping("/getPayPromotionParticipatePageById")
    public Result<Page<PayPromotionParticipatePageVO>> getPayPromotionParticipatePageById(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryPayPromotionActivityPageForm form) {
        log.info("saveRule" + form);
        QueryPayPromotionActivityPageRequest request = PojoUtils.map(form, QueryPayPromotionActivityPageRequest.class);
        Page<PayPromotionParticipateDTO> result = payPromotionActivityApi.getPayPromotionParticipateById(request);
        Page<PayPromotionParticipatePageVO> page = PojoUtils.map(result, PayPromotionParticipatePageVO.class);

        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            List<Long> orderIds = page.getRecords().stream().map(PayPromotionParticipatePageVO::getOrderId).collect(Collectors.toList());
            List<OrderDTO> orderPromotionActivityDTOS = orderApi.listByIds(orderIds);
            PayPromotionActivityDTO payPromotionActivityDTO = payPromotionActivityApi.getById(form.getId());
            Map<Long, OrderDTO> orderPromotionActivityDTOMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(orderPromotionActivityDTOS)) {
                orderPromotionActivityDTOMap = orderPromotionActivityDTOS.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
            }
            Map<Long, OrderDTO> finalOrderPromotionActivityDTOMap = orderPromotionActivityDTOMap;
            page.getRecords().forEach(item -> {
                OrderDTO orderDTO = finalOrderPromotionActivityDTOMap.get(item.getOrderId());
                item.setPayment(orderDTO.getPaymentAmount());
                item.setName(payPromotionActivityDTO.getName());
                item.setDiscountAmount(payPromotionActivityDTO.getSponsorType() == 1 ? orderDTO.getPlatformPaymentDiscountAmount() : orderDTO.getShopPaymentDiscountAmount());
                item.setOrderNo(orderDTO.getOrderNo());
                item.setStatus(OrderStatusEnum.getByCode(orderDTO.getOrderStatus()).getName());
            });
        }
        return Result.success(PojoUtils.map(page, PayPromotionParticipatePageVO.class));
    }
}
