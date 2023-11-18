package com.yiling.hmc.remind.service;

import com.yiling.hmc.remind.dto.MedsRemindDTO;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import com.yiling.hmc.remind.dto.request.AcceptMedsRemindRequest;
import com.yiling.hmc.remind.dto.request.MedsRemindBaseRequest;
import com.yiling.hmc.remind.dto.request.SaveMedsRemindRequest;
import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 用药提醒主表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
public interface MedsRemindService extends BaseService<MedsRemindDO> {

    /**
     * 保存更新用药提醒
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
     * 取消提醒
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
     * 生成每日用药提醒任务
     */
    void generateDailyMedsRemindTask();

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
}
