package com.yiling.hmc.remind.api.impl;

import com.yiling.hmc.remind.api.MedsRemindApi;
import com.yiling.hmc.remind.dto.MedsRemindDTO;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import com.yiling.hmc.remind.dto.request.*;
import com.yiling.hmc.remind.service.MedsRemindService;
import com.yiling.hmc.remind.service.MedsRemindSubscribeService;
import com.yiling.hmc.remind.service.MedsRemindTaskDetailService;
import org.apache.dubbo.config.annotation.DubboService;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用药提醒API
 * @author: fan.shen
 * @date: 2022/5/31
 */
@DubboService
public class MedsRemindApiImpl implements MedsRemindApi {

    @Autowired
    private MedsRemindService medsRemindService;

    @Autowired
    private MedsRemindTaskDetailService medsRemindTaskDetailService;

    @Autowired
    private MedsRemindSubscribeService medsRemindSubscribeService;

    @Override
    public MedsRemindDTO saveOrUpdateMedsRemind(SaveMedsRemindRequest request) {
        return medsRemindService.saveOrUpdateMedsRemind(request);
    }

    @Override
    public List<MedsRemindDTO> getAllMedsRemind(Long currentUserId) {
        return medsRemindService.getAllMedsRemind(currentUserId);
    }

    @Override
    public boolean checkMedsRemind(CheckMedsRemindRequest request) {
        return medsRemindTaskDetailService.checkMedsRemind(request);
    }

    @Override
    public boolean stopMedsRemind(Long id) {
        return medsRemindService.stopMedsRemind(id);
    }

    @Override
    public boolean acceptRemind(AcceptMedsRemindRequest request) {
        return medsRemindService.acceptRemind(request);
    }

    @Override
    public MedsRemindDTO getMedsRemindDetail(Long currentUserId, Long id) {
        return medsRemindService.getMedsRemindDetail(currentUserId, id);
    }

    @Override
    public boolean cancelRemind(MedsRemindBaseRequest request) {
        return medsRemindService.cancelRemind(request);
    }

    @Override
    public List<MedsRemindTaskDetailDTO> todayRemind(Long currentUserId) {
        return medsRemindService.todayRemind(currentUserId);
    }

    @Override
    public void generateDailyMedsRemindTask() {
        medsRemindService.generateDailyMedsRemindTask();
    }

    @Override
    public void saveOrUpdateRemindSub(List<MedsRemindSubscribeRequest> requestList) {
        medsRemindSubscribeService.saveOrUpdateRemindSub(requestList);
    }

    @Override
    public List<MedsRemindDTO> medsHistory(Long currentUserId) {
        return medsRemindService.medsHistory(currentUserId);
    }

    @Override
    public boolean clearHistory(Long currentUserId) {
        return medsRemindService.clearHistory(currentUserId);
    }

    @Override
    public boolean updateMedsRemindSubscribe(Long currentUserId, String subscribeStr) {
        return medsRemindSubscribeService.updateMedsRemindSubscribe(currentUserId, subscribeStr);
    }

    @Override
    public void midAutumnFestivalPushWxTemplateMsg() {
        medsRemindSubscribeService.midAutumnFestivalPushWxTemplateMsg();
    }

    @Override
    public void fatherFestivalPushWxTemplateMsg() {
        medsRemindSubscribeService.fatherFestivalPushWxTemplateMsg();
    }
}