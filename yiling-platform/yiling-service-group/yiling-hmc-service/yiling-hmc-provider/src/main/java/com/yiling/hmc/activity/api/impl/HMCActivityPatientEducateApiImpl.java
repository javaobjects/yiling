package com.yiling.hmc.activity.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.activity.api.HMCActivityPatientEducateApi;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.hmc.activity.dto.request.QueryActivityPatientEducateRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityPatientEducationRequest;
import com.yiling.hmc.activity.service.ActivityPatientEducateService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 患教活动API
 *
 * @author: fan.shen
 * @date: 2022/09/01
 */
@DubboService
public class HMCActivityPatientEducateApiImpl implements HMCActivityPatientEducateApi {

    @Autowired
    private ActivityPatientEducateService activityPatientEducateService;

    @Override
    public Page<ActivityPatientEducateDTO> pageList(QueryActivityPatientEducateRequest request) {
        return activityPatientEducateService.pageList(request);
    }

    @Override
    public ActivityPatientEducateDTO saveActivityPatientEducate(SaveActivityPatientEducationRequest request) {
        return activityPatientEducateService.saveActivityPatientEducate(request);
    }

    @Override
    public ActivityPatientEducateDTO queryActivityById(Long id) {
        return activityPatientEducateService.queryById(id);
    }

    @Override
    public List<ActivityPatientEducateDTO> queryActivity(QueryActivityRequest request) {
        return activityPatientEducateService.queryActivity(request);
    }

    @Override
    public boolean delActivityById(Long id) {
        return activityPatientEducateService.delActivityById(id);
    }

    @Override
    public List<ActivityPatientEducateDTO> getActivityPatientEducate(List<Long> idList) {
        return activityPatientEducateService.getActivityPatientEducate(idList);
    }
}
