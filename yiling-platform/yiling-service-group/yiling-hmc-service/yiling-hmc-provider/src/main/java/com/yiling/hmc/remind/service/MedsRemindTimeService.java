package com.yiling.hmc.remind.service;

import com.yiling.hmc.remind.entity.MedsRemindTimeDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 用药提醒时间表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
public interface MedsRemindTimeService extends BaseService<MedsRemindTimeDO> {

    /**
     * 通过药品管理id获取时间设置
     * @param medsRemindIdList
     * @return
     */
    List<MedsRemindTimeDO> selectByMedsRemindIdList(List<Long> medsRemindIdList);

    /**
     * 删除时间设置
     * @param id
     * @param opUserId
     */
    void deleteByMedsRemindId(Long id, Long opUserId);
}
