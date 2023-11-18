package com.yiling.bi.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.bi.order.dao.BiOrderBackupTaskMapper;
import com.yiling.bi.order.entity.BiOrderBackupTaskDO;
import com.yiling.bi.order.service.BiOrderBackupTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.date.DateUtil;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
@Service
public class BiOrderBackupTaskServiceImpl extends BaseServiceImpl<BiOrderBackupTaskMapper, BiOrderBackupTaskDO> implements BiOrderBackupTaskService {

    @Override
    public void updateBackupStatus(Long taskId, String endTime, Integer backupStatus) {
        BiOrderBackupTaskDO biOrderBackupTaskDO = new BiOrderBackupTaskDO();
        biOrderBackupTaskDO.setId(taskId);
        biOrderBackupTaskDO.setBackupStatus(backupStatus);
        biOrderBackupTaskDO.setEndTime(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss"));
        biOrderBackupTaskDO.setUpdateTime(biOrderBackupTaskDO.getEndTime());

        baseMapper.updateById(biOrderBackupTaskDO);
    }

    @Override
    public List<BiOrderBackupTaskDO> getTaskByMonth(String month) {
        LambdaQueryWrapper<BiOrderBackupTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BiOrderBackupTaskDO::getBackupMonth, month);
        return baseMapper.selectList(wrapper);
    }
}
