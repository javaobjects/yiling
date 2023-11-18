package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.wash.dao.UnlockFlowWashTaskMapper;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerWashTaskRequest;
import com.yiling.dataflow.wash.entity.UnlockFlowWashTaskDO;
import com.yiling.dataflow.wash.service.UnlockFlowWashTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-06
 */
@Service
public class UnlockFlowWashTaskServiceImpl extends BaseServiceImpl<UnlockFlowWashTaskMapper, UnlockFlowWashTaskDO> implements UnlockFlowWashTaskService {

    @Override
    public Boolean saveBatch(List<SaveOrUpdateUnlockCustomerWashTaskRequest> requestList) {
        List<UnlockFlowWashTaskDO> list = new ArrayList<>();
        for (SaveOrUpdateUnlockCustomerWashTaskRequest saveOrUpdateUnlockCustomerWashTaskRequest : requestList) {
            UnlockFlowWashTaskDO unlockFlowWashTaskDO = PojoUtils.map(saveOrUpdateUnlockCustomerWashTaskRequest, UnlockFlowWashTaskDO.class);
            unlockFlowWashTaskDO.setCreateTime(saveOrUpdateUnlockCustomerWashTaskRequest.getOpTime());
            unlockFlowWashTaskDO.setUpdateTime(saveOrUpdateUnlockCustomerWashTaskRequest.getOpTime());
            unlockFlowWashTaskDO.setCreateUser(saveOrUpdateUnlockCustomerWashTaskRequest.getOpUserId());
            unlockFlowWashTaskDO.setUpdateUser(saveOrUpdateUnlockCustomerWashTaskRequest.getOpUserId());
            unlockFlowWashTaskDO.setRemark("");
            unlockFlowWashTaskDO.setDelFlag(0);
            list.add(unlockFlowWashTaskDO);
        }
        this.baseMapper.insertBatchSomeColumn(list);
        return true;
    }

    @Override
    public Long saveUnlockCustomerWashTask(SaveOrUpdateUnlockCustomerWashTaskRequest request) {
        UnlockFlowWashTaskDO unlockFlowWashTaskDO = PojoUtils.map(request, UnlockFlowWashTaskDO.class);
        if (this.saveOrUpdate(unlockFlowWashTaskDO)) {
            return unlockFlowWashTaskDO.getId();
        }
        return 0L;
    }

    @Override
    public List<UnlockFlowWashTaskDO> getListByFmwcId(Long fmwcId) {
        LambdaQueryWrapper<UnlockFlowWashTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockFlowWashTaskDO::getFmwcId, fmwcId);
        return this.list(wrapper);
    }
}
