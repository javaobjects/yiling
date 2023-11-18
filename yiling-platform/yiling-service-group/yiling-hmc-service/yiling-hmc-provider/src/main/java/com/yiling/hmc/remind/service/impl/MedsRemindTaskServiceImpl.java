package com.yiling.hmc.remind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.remind.dao.MedsRemindTaskMapper;
import com.yiling.hmc.remind.entity.MedsRemindTaskDO;
import com.yiling.hmc.remind.service.MedsRemindTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用药提醒任务表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Service
public class MedsRemindTaskServiceImpl extends BaseServiceImpl<MedsRemindTaskMapper, MedsRemindTaskDO> implements MedsRemindTaskService {

    @Override
    public List<MedsRemindTaskDO> batchQueryByIdList(List<Long> remindTaskIdList) {
        QueryWrapper<MedsRemindTaskDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MedsRemindTaskDO::getId, remindTaskIdList);
        return this.getBaseMapper().selectList(wrapper);
    }
}
