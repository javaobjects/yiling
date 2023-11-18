package com.yiling.marketing.lotteryactivity.api.impl;

import java.util.List;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityDeliveryAddressApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDeliveryAddressDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityHitRandomGenerateDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityRulePageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveDeliveryAddressRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityBasicRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivitySettingRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDeliveryAddressDO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityAccessRecordService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityDeliveryAddressService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityHitRandomGenerateService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivitySignRecordService;

/**
 * <p>
 * 抽奖活动收货地址 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
@DubboService
public class LotteryActivityDeliveryAddressApiImpl implements LotteryActivityDeliveryAddressApi {

    @Autowired
    LotteryActivityDeliveryAddressService activityDeliveryAddressService;

    @Override
    public List<LotteryActivityDeliveryAddressDTO> queryList(Long uid) {
        return activityDeliveryAddressService.queryList(uid);
    }

    @Override
    public LotteryActivityDeliveryAddressDTO getById(Long id) {
        return PojoUtils.map(activityDeliveryAddressService.getById(id), LotteryActivityDeliveryAddressDTO.class);
    }

    @Override
    public boolean saveDeliveryAddress(SaveDeliveryAddressRequest request) {
        return activityDeliveryAddressService.saveDeliveryAddress(request);
    }

    @Override
    public boolean deleteById(Long id, Long opUserId) {
        LotteryActivityDeliveryAddressDO deliveryAddressDO = new LotteryActivityDeliveryAddressDO();
        deliveryAddressDO.setId(id);
        deliveryAddressDO.setOpUserId(opUserId);
        return activityDeliveryAddressService.deleteByIdWithFill(deliveryAddressDO) > 0;
    }
}
