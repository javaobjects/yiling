package com.yiling.hmc.activity.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.hmc.activity.dto.request.QueryActivityPatientEducateRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityPatientEducationRequest;
import com.yiling.hmc.activity.entity.ActivityPatientEducateDO;
import com.yiling.hmc.activity.dao.ActivityPatientEducateMapper;
import com.yiling.hmc.activity.service.ActivityPatientEducateService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 患教活动 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-09-01
 */
@Service
public class ActivityPatientEducateServiceImpl extends BaseServiceImpl<ActivityPatientEducateMapper, ActivityPatientEducateDO> implements ActivityPatientEducateService {

    @Override
    public Page<ActivityPatientEducateDTO> pageList(QueryActivityPatientEducateRequest request) {
        Page<ActivityPatientEducateDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<ActivityPatientEducateDO> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(request.getActivityName())) {
            queryWrapper.lambda().like(ActivityPatientEducateDO::getActivityName, request.getActivityName());
        }
        queryWrapper.lambda().orderByDesc(ActivityPatientEducateDO::getUpdateTime);
        Page<ActivityPatientEducateDO> result = this.page(page, queryWrapper);
        return PojoUtils.map(result, ActivityPatientEducateDTO.class);
    }

    @Override
    public ActivityPatientEducateDTO saveActivityPatientEducate(SaveActivityPatientEducationRequest request) {
        ActivityPatientEducateDO patientEducateDO = PojoUtils.map(request, ActivityPatientEducateDO.class);
        this.saveOrUpdate(patientEducateDO);
        return PojoUtils.map(patientEducateDO, ActivityPatientEducateDTO.class);
    }

    @Override
    public ActivityPatientEducateDTO queryById(Long id) {
        ActivityPatientEducateDO patientEducateDO = this.getById(id);
        return PojoUtils.map(patientEducateDO, ActivityPatientEducateDTO.class);
    }

    @Override
    public boolean delActivityById(Long id) {
        ActivityPatientEducateDO patientEducateDO = new ActivityPatientEducateDO();
        patientEducateDO.setId(id);
        int i = this.deleteByIdWithFill(patientEducateDO);
        return i > 0;
    }

    @Override
    public List<ActivityPatientEducateDTO> getActivityPatientEducate(List<Long> idList) {
        QueryWrapper<ActivityPatientEducateDO> queryWrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(idList)) {
            queryWrapper.lambda().in(ActivityPatientEducateDO::getId, idList);
        }
        List<ActivityPatientEducateDO> list = this.list(queryWrapper);
        return PojoUtils.map(list, ActivityPatientEducateDTO.class);
    }

    @Override
    public List<ActivityPatientEducateDTO> queryActivity(QueryActivityRequest request) {
        QueryWrapper<ActivityPatientEducateDO> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(request.getActivityIdList())) {
            wrapper.lambda().in(ActivityPatientEducateDO::getId, request.getActivityIdList());
        }
        if (StrUtil.isNotBlank(request.getActivityName())) {
            wrapper.lambda().like(ActivityPatientEducateDO::getActivityName, request.getActivityName());
        }
        List<ActivityPatientEducateDO> list = this.list(wrapper);
        return PojoUtils.map(list, ActivityPatientEducateDTO.class);
    }
}
