package com.yiling.hmc.remind.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.hmc.remind.entity.MedsRemindUserDO;
import com.yiling.hmc.remind.dao.MedsRemindUserMapper;
import com.yiling.hmc.remind.enums.HmcRemindStatusEnum;
import com.yiling.hmc.remind.service.MedsRemindUserService;
import com.yiling.framework.common.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用药提醒用户表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Slf4j
@Service
public class MedsRemindUserServiceImpl extends BaseServiceImpl<MedsRemindUserMapper, MedsRemindUserDO> implements MedsRemindUserService {

    @Override
    public void saveMedsRemindUser(Long id, Long userId) {
        QueryWrapper<MedsRemindUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindUserDO::getMedsRemindId, id);
        wrapper.lambda().eq(MedsRemindUserDO::getCreateUser, userId);
        MedsRemindUserDO user = this.getBaseMapper().selectOne(wrapper);
        if (Objects.nonNull(user)) {
            if (user.getRemindStatus().equals(HmcRemindStatusEnum.VALID.getType())) {
                log.info("当前用户已经关注提醒，跳过处理");
                return;
            }
            user.setRemindStatus(HmcRemindStatusEnum.VALID.getType());
            this.updateById(user);
            return;
        }

        // 保存用户
        Date now = new Date();
        user = new MedsRemindUserDO();
        user.setMedsRemindId(id);
        user.setCreateUser(userId);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setUpdateUser(userId);
        user.setRemindStatus(HmcRemindStatusEnum.VALID.getType());
        this.save(user);

    }

    @Override
    public List<MedsRemindUserDO> getAllByMedsId(Long medsId) {
        QueryWrapper<MedsRemindUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindUserDO::getMedsRemindId, medsId);
        wrapper.lambda().eq(MedsRemindUserDO::getRemindStatus, HmcRemindStatusEnum.VALID.getType());
        List<MedsRemindUserDO> userDOList = this.getBaseMapper().selectList(wrapper);
        return userDOList;
    }

    @Override
    public List<MedsRemindUserDO> getAllByMedsIdList(List<Long> medsIdList) {
        QueryWrapper<MedsRemindUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MedsRemindUserDO::getMedsRemindId, medsIdList);
        List<MedsRemindUserDO> userDOList = this.getBaseMapper().selectList(wrapper);
        return userDOList;
    }

    @Override
    public List<MedsRemindUserDO> getAllMedsRemindByUserId(Long currentUserId) {
        QueryWrapper<MedsRemindUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindUserDO::getCreateUser, currentUserId);
        List<MedsRemindUserDO> userDOList = this.getBaseMapper().selectList(wrapper);
        return userDOList;
    }

    @Override
    public void updateMedsRemindUser(Long medsRemindId, Long userId) {
        QueryWrapper<MedsRemindUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindUserDO::getMedsRemindId, medsRemindId);
        if (Objects.nonNull(userId)) {
            wrapper.lambda().eq(MedsRemindUserDO::getCreateUser, userId);
        }

        MedsRemindUserDO user = new MedsRemindUserDO();
        user.setRemindStatus(HmcRemindStatusEnum.INVALID.getType());
        user.setUpdateUser(userId);

        this.update(user, wrapper);
    }

    @Override
    public void deleteMedsRemindUser(Long medsRemindId, Long userId) {
        QueryWrapper<MedsRemindUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindUserDO::getMedsRemindId, medsRemindId);
        wrapper.lambda().eq(MedsRemindUserDO::getCreateUser, userId);

        MedsRemindUserDO remindUserDO = this.getOne(wrapper, false);
        this.deleteByIdWithFill(remindUserDO);
    }

    @Override
    public List<MedsRemindUserDO> getAllValidateRemindByMedsIdList(Long receiveUserId, List<Long> medsIdList) {
        QueryWrapper<MedsRemindUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindUserDO::getCreateUser, receiveUserId);
        wrapper.lambda().eq(MedsRemindUserDO::getRemindStatus, HmcRemindStatusEnum.VALID.getType());
        wrapper.lambda().in(MedsRemindUserDO::getMedsRemindId, medsIdList);

        List<MedsRemindUserDO> userDOList = this.getBaseMapper().selectList(wrapper);
        return userDOList;
    }
}
