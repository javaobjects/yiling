package com.yiling.hmc.remind.api;

import com.yiling.hmc.remind.dto.MedsRemindDTO;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import com.yiling.hmc.remind.dto.request.*;

import java.util.List;


/**
 * 用药提醒API
 *
 * @Author fan.shen
 * @Date 2022/5/30
 */
public interface MedsRemindApi {

    /**
     * 保存用药提醒设置
     * @param request
     * @return
     */
    MedsRemindDTO saveOrUpdateMedsRemind(SaveMedsRemindRequest request);

    /**
     * 全部用药
     * @param currentUserId
     * @return
     */
    List<MedsRemindDTO> getAllMedsRemind(Long currentUserId);

    /**
     * 设为已用/未用
     * @param request
     * @return
     */
    boolean checkMedsRemind(CheckMedsRemindRequest request);

    /**
     * 停止提醒
     * @param id
     * @return
     */
    boolean stopMedsRemind(Long id);

    /**
     * 接受提醒
     * @param request
     * @return
     */
    boolean acceptRemind(AcceptMedsRemindRequest request);

    /**
     * 获取用药详情
     * @param currentUserId
     * @param id
     * @return
     */
    MedsRemindDTO getMedsRemindDetail(Long currentUserId, Long id);

    /**
     * 取消用药提醒
     * @param request
     * @return
     */
    boolean cancelRemind(MedsRemindBaseRequest request);

    /**
     * 今日提醒
     * @param currentUserId
     * @return
     */
    List<MedsRemindTaskDetailDTO> todayRemind(Long currentUserId);

    /**
     * 生成每日用药提醒任务开始
     */
    void generateDailyMedsRemindTask();

    /**
     * 准备保存消息订阅
     * @param requestList
     */
    void saveOrUpdateRemindSub(List<MedsRemindSubscribeRequest> requestList);

    /**
     * 历史记录
     * @param currentUserId
     * @return
     */
    List<MedsRemindDTO> medsHistory(Long currentUserId);

    /**
     * 清除历史记录
     * @param currentUserId
     * @return
     */
    boolean clearHistory(Long currentUserId);

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
