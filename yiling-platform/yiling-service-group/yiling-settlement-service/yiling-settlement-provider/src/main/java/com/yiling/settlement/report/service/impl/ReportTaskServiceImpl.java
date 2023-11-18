package com.yiling.settlement.report.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.settlement.report.entity.ReportTaskDO;
import com.yiling.settlement.report.dao.ReportTaskMapper;
import com.yiling.settlement.report.enums.ReportTaskStatusEnum;
import com.yiling.settlement.report.service.ReportTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 返利报表生成任务表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-09
 */
@Service
public class ReportTaskServiceImpl extends BaseServiceImpl<ReportTaskMapper, ReportTaskDO> implements ReportTaskService {

    @Override
    public List<ReportTaskDO> queryInProductionTask(Long eid) {
        if (ObjectUtil.isNull(eid)||ObjectUtil.equal(eid,0L)){
            return ListUtil.toList();
        }
        LambdaQueryWrapper<ReportTaskDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReportTaskDO::getEid,eid);
        wrapper.eq(ReportTaskDO::getStatus, ReportTaskStatusEnum.IN_PRODUCTION.getCode());
        return list(wrapper);
    }
}
