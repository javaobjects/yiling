package com.yiling.open.ftp.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.open.ftp.dao.FlowFtpFileTaskMapper;
import com.yiling.open.ftp.entity.FlowFtpFileTaskDO;
import com.yiling.open.ftp.service.FlowFtpFileTaskService;

/**
 * @author fucheng.bai
 * @date 2023/3/21
 */
@Service
public class FlowFtpFileTaskServiceImpl extends BaseServiceImpl<FlowFtpFileTaskMapper, FlowFtpFileTaskDO> implements FlowFtpFileTaskService {

    @Override
    public void updateTaskStatusById(Integer status, Long id) {
        FlowFtpFileTaskDO flowFtpFileTaskDO = new FlowFtpFileTaskDO();
        flowFtpFileTaskDO.setId(id);
        flowFtpFileTaskDO.setAnalyticStatus(status);
        flowFtpFileTaskDO.setUpdateTime(new Date());
        this.updateById(flowFtpFileTaskDO);
    }
}
