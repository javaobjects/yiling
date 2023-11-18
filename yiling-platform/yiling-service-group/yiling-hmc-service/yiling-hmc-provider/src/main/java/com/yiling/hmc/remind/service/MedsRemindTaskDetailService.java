package com.yiling.hmc.remind.service;

import com.yiling.hmc.remind.dto.request.CheckMedsRemindRequest;
import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.hmc.remind.entity.MedsRemindTaskDetailDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 用药提醒任务详情表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
public interface MedsRemindTaskDetailService extends BaseService<MedsRemindTaskDetailDO> {

    /**
     * 删除历史用药
     * @param id
     */
    void cancelHistoryMedsRemindTask(Long currentUserId, List<Long> userIdList, MedsRemindDO id);

    /**
     * 设为已用/未用
     * @param request
     * @return
     */
    boolean checkMedsRemind(CheckMedsRemindRequest request);

    /**
     * 停止所有提醒任务
     * @param id
     */
    void stopAllRemindTask(Long id);

    /**
     * 获取今日任务
     * @param currentUserId
     * @return
     */
    List<MedsRemindTaskDetailDO> getTodayRemindTaskDetail(Long currentUserId);

    /**
     * 获取已确认任务
     * @param medsRemindIdList
     * @return
     */
    List<MedsRemindTaskDetailDO> getConfirmedTaskByMedsIds(List<Long> medsRemindIdList);

    /**
     * 获取所有同一时间提醒任务
     * @param taskDetailDO
     * @return
     */
    List<MedsRemindTaskDetailDO> getAllSameTimeRemindTask(MedsRemindTaskDetailDO taskDetailDO);

    /**
     * 获取最近一次提醒
     * @param id
     * @return
     */
    MedsRemindTaskDetailDO getLatestRemindTaskDetail(Long id);
}
