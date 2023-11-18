package com.yiling.hmc.remind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.hmc.remind.entity.MedsRemindTimeDO;
import com.yiling.hmc.remind.dao.MedsRemindTimeMapper;
import com.yiling.hmc.remind.service.MedsRemindTimeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用药提醒时间表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Service
public class MedsRemindTimeServiceImpl extends BaseServiceImpl<MedsRemindTimeMapper, MedsRemindTimeDO> implements MedsRemindTimeService {

    @Override
    public List<MedsRemindTimeDO> selectByMedsRemindIdList(List<Long> medsRemindIdList) {
        QueryWrapper<MedsRemindTimeDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MedsRemindTimeDO::getMedsRemindId, medsRemindIdList);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void deleteByMedsRemindId(Long id, Long opUserId) {
        LambdaQueryWrapper<MedsRemindTimeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MedsRemindTimeDO::getMedsRemindId, id);

        MedsRemindTimeDO medsRemindTimeDO = new MedsRemindTimeDO();
        medsRemindTimeDO.setOpUserId(opUserId);
        medsRemindTimeDO.setOpTime(new Date());

        this.batchDeleteWithFill(medsRemindTimeDO, queryWrapper);

    }
}
