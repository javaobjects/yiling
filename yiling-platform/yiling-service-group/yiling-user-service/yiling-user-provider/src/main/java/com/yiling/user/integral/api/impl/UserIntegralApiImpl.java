package com.yiling.user.integral.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.bo.GenerateUserSignRecordBO;
import com.yiling.user.integral.bo.UserSignRecordDetailBO;
import com.yiling.user.integral.dto.request.AddIntegralOrderGiveRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordTurnPageRequest;
import com.yiling.user.integral.dto.request.UpdateIUserIntegralRequest;
import com.yiling.user.integral.service.IntegralUserSignRecordService;
import com.yiling.user.integral.service.UserIntegralService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分发放/扣减记录 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-10
 */
@Slf4j
@DubboService
public class UserIntegralApiImpl implements UserIntegralApi {

    @Autowired
    UserIntegralService userIntegralService;
    @Autowired
    IntegralUserSignRecordService integralUserSignRecordService;

    @Override
    public Integer getUserIntegralByUid(Long uid, Integer platform) {
        return userIntegralService.getUserIntegralByUid(uid, platform);
    }

    @Override
    public Integer updateIntegral(UpdateIUserIntegralRequest request) {
        return userIntegralService.updateIntegral(request);
    }

    @Override
    public List<GenerateUserSignRecordBO> generateUserSignData(QueryUserSignRecordRequest request) {
        return integralUserSignRecordService.generateUserSignData(request);
    }

    @Override
    public List<GenerateUserSignRecordBO> userSignRecordTurnPage(QueryUserSignRecordTurnPageRequest request) {
        return integralUserSignRecordService.userSignRecordTurnPage(request);
    }

    @Override
    public UserSignRecordDetailBO getSignDetail(Long giveRuleId, Integer platform, Long uid) {
        return integralUserSignRecordService.getSignDetail(giveRuleId, platform, uid);
    }

    @Override
    public boolean clearIntegral(Integer platform) {
        return userIntegralService.clearIntegral(platform);
    }

    @Override
    public boolean cleanDirectionalGiveIntegral() {
        return userIntegralService.cleanDirectionalGiveIntegral();
    }

    @Override
    public boolean giveIntegralByOrder(AddIntegralOrderGiveRequest request) {
        return userIntegralService.giveIntegralByOrder(request);
    }

}
