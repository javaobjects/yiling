package com.yiling.hmc.remind.service;

import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.hmc.remind.entity.MedsRemindUserDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 用药提醒用户表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
public interface MedsRemindUserService extends BaseService<MedsRemindUserDO> {

    /**
     * 保存药品关联用户
     * @param id
     * @param userId
     */
    void saveMedsRemindUser(Long id, Long userId);

    /**
     * 根据提醒设置id获取所有用户
     * @return
     */
    List<MedsRemindUserDO> getAllByMedsId(Long medsId);

    /**
     *
     * @return
     */
    List<MedsRemindUserDO> getAllByMedsIdList(List<Long> medsIdList);

    /**
     * 根据用户获取所有用药提醒
     * @param currentUserId
     * @return
     */
    List<MedsRemindUserDO> getAllMedsRemindByUserId(Long currentUserId);

    /**
     * 更新提醒状态
     * @param medsRemindId
     * @param userId
     */
    void updateMedsRemindUser(Long medsRemindId, Long userId);

    /**
     * 删除提醒状态
     * @param medsRemindId
     * @param userId
     */
    void deleteMedsRemindUser(Long medsRemindId, Long userId);

    /**
     * 获取用户所有关注的提醒
     * @param receiveUserId
     * @param medsIdList
     * @return
     */
    List<MedsRemindUserDO> getAllValidateRemindByMedsIdList(Long receiveUserId, List<Long> medsIdList);
}
