package com.yiling.hmc.remind.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.remind.dto.request.MedsRemindSubscribeRequest;
import com.yiling.hmc.remind.entity.MedsRemindSubscribeDO;

import java.util.List;

/**
 * <p>
 * 用药提醒消息订阅表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
public interface MedsRemindSubscribeService extends BaseService<MedsRemindSubscribeDO> {

    /**
     * 保存订阅
     * @param requestList
     */
    void saveOrUpdateRemindSub(List<MedsRemindSubscribeRequest> requestList);

    /**
     * 获取订阅记录 -> 取消订阅状态
     * @param receiveUserId
     * @param templateId
     * @return
     */
    MedsRemindSubscribeDO getByUserIdAndTemplateId(Long receiveUserId, String templateId);

    /**
     * 更新订阅状态
     * @param currentUserId
     * @param subscribeStr
     * @return
     */
    boolean updateMedsRemindSubscribe(Long currentUserId, String subscribeStr);

    /**
     * 中秋节活动-推送模板消息
     */
    void midAutumnFestivalPushWxTemplateMsg();

    /**
     * 父亲节活动-推送模板消息
     */
    void fatherFestivalPushWxTemplateMsg();
}
