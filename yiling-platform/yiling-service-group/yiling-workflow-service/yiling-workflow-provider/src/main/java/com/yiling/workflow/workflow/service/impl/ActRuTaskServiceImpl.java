package com.yiling.workflow.workflow.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.workflow.workflow.dao.ActRuTaskMapper;
import com.yiling.workflow.workflow.entity.ActHiTaskinstDO;
import com.yiling.workflow.workflow.entity.ActRuExecutionDO;
import com.yiling.workflow.workflow.entity.ActRuTaskDO;
import com.yiling.workflow.workflow.service.ActHiTaskinstService;
import com.yiling.workflow.workflow.service.ActRuExecutionService;
import com.yiling.workflow.workflow.service.ActRuTaskService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-05-23
 */
@Service
public class ActRuTaskServiceImpl extends BaseServiceImpl<ActRuTaskMapper, ActRuTaskDO> implements ActRuTaskService {
    @Autowired
    private ActHiTaskinstService actHiTaskinstService;
    @Autowired
    private ActRuExecutionService actRuExecutionService;

    @Override
    @Transactional
    public void batchInsert(List<ActRuTaskDO> list) {
        List<ActRuExecutionDO> actRuExecutionDOS = Lists.newArrayList();
        list.forEach(actRuTaskDO -> {
            ActRuExecutionDO actRuExecutionDO = new ActRuExecutionDO();
            actRuExecutionDO.setId(actRuTaskDO.getExecutionId()).setProcDefId(actRuTaskDO.getProcDefId()).setProcInstId(actRuTaskDO.getProcInstId()).setParentId(actRuTaskDO.getExecutionId()).setIsActive(0)
                    .setStartTime(new Date());
            actRuExecutionDOS.add(actRuExecutionDO);
        });
        actRuExecutionService.saveBatch(actRuExecutionDOS);
        this.saveBatch(list);
        List<ActHiTaskinstDO> taskinstDOS = PojoUtils.map(list, ActHiTaskinstDO.class);
        taskinstDOS.forEach(taskins->{
            taskins.setStartTime(new Date());

        });
        actHiTaskinstService.saveBatch(taskinstDOS);

    }
}
