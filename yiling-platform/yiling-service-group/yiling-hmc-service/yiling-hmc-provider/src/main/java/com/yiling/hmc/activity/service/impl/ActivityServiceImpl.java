package com.yiling.hmc.activity.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.activity.dto.ActivityDTO;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.hmc.activity.dto.request.BaseActivityRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityDocPatientRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityGoodsPromoteRequest;
import com.yiling.hmc.activity.entity.ActivityDO;
import com.yiling.hmc.activity.dao.ActivityMapper;
import com.yiling.hmc.activity.entity.ActivityDocToPatientDO;
import com.yiling.hmc.activity.enums.ActivityStatusEnum;
import com.yiling.hmc.activity.service.ActivityDocToPatientService;
import com.yiling.hmc.activity.service.ActivityService;
import com.yiling.framework.common.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * C端活动 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-01-13
 */
@Slf4j
@Service
public class ActivityServiceImpl extends BaseServiceImpl<ActivityMapper, ActivityDO> implements ActivityService {

    @Autowired
    ActivityDocToPatientService activityDocToPatientService;

    @Override
    public Page<ActivityDTO> pageList(QueryActivityRequest request) {
        Page<ActivityDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<ActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StrUtil.isNotBlank(request.getActivityName()), ActivityDO::getActivityName, request.getActivityName());
        queryWrapper.lambda().eq(Objects.nonNull(request.getActivityType()), ActivityDO::getActivityType, request.getActivityType());
        queryWrapper.lambda().orderByDesc(ActivityDO::getCreateTime);
        Page<ActivityDO> result = this.page(page, queryWrapper);
        return PojoUtils.map(result, ActivityDTO.class);
    }

    @Override
    public Long saveOrUpdateDocToPatient(SaveActivityDocPatientRequest request) {
        ActivityDO activityDO = PojoUtils.map(request, ActivityDO.class);
        this.saveOrUpdate(activityDO);
        if (Objects.isNull(request.getId())) {
            ActivityDocToPatientDO activityDocToPatientDO = PojoUtils.map(request, ActivityDocToPatientDO.class);
            activityDocToPatientDO.setActivityId(activityDO.getId());
            activityDocToPatientService.save(activityDocToPatientDO);
        } else {
            ActivityDocToPatientDO activityDocToPatientDO = activityDocToPatientService.getByActivityId(activityDO.getId());
            if (Objects.nonNull(activityDocToPatientDO)) {
                PojoUtils.map(request, activityDocToPatientDO);
                activityDocToPatientService.updateById(activityDocToPatientDO);
            } else {
                ActivityDocToPatientDO activityDocToPatientDO2 = PojoUtils.map(request, ActivityDocToPatientDO.class);
                activityDocToPatientDO2.setActivityId(activityDO.getId());
                activityDocToPatientService.save(activityDocToPatientDO2);
            }
        }
        return activityDO.getId();
    }

    @Override
    public ActivityDocToPatientDTO queryActivityById(Long id) {
        QueryWrapper<ActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ActivityDO::getId, id);
        ActivityDO activityDO = this.getOne(queryWrapper);
        if (Objects.isNull(activityDO)) {
            log.error("根据id未获取到活动信息");
            return null;
        }

        ActivityDocToPatientDO activityDocToPatientDO = activityDocToPatientService.getByActivityId(activityDO.getId());

        ActivityDocToPatientDTO activityDTO = PojoUtils.map(activityDO, ActivityDocToPatientDTO.class);

        if(Objects.nonNull(activityDocToPatientDO)) {
            PojoUtils.map(activityDocToPatientDO, activityDTO);
        }

        activityDTO.setId(activityDO.getId());
        return activityDTO;
    }

    @Override
    public List<ActivityDocToPatientDTO> queryActivityByIdList(List<Long> idList) {
        QueryWrapper<ActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ActivityDO::getId, idList);
        List<ActivityDO> activityDOList = this.list(queryWrapper);
        return PojoUtils.map(activityDOList, ActivityDocToPatientDTO.class);
    }

    @Override
    public List<ActivityDTO> queryActivity(QueryActivityRequest request) {
        QueryWrapper<ActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ActivityDO::getId, request.getActivityIdList());
        queryWrapper.lambda().like(ActivityDO::getActivityName, request.getActivityName());
        List<ActivityDO> activityDOList = this.list(queryWrapper);
        return PojoUtils.map(activityDOList, ActivityDTO.class);
    }

    @Override
    public ActivityDTO queryActivity(Long id) {
        QueryWrapper<ActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ActivityDO::getId, id);
        ActivityDO one = this.getOne(queryWrapper);
        return PojoUtils.map(one, ActivityDTO.class);
    }

    @Override
    public Boolean stopActivity(BaseActivityRequest request) {
        QueryWrapper<ActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ActivityDO::getId, request.getId());

        ActivityDO activityDO = PojoUtils.map(request, ActivityDO.class);
        activityDO.setActivityStatus(ActivityStatusEnum.UNABLE.getCode());
        return this.update(activityDO, wrapper);
    }

    @Override
    public Long saveOrUpdateGoodsPromote(SaveActivityGoodsPromoteRequest request) {
        ActivityDO activityDO = PojoUtils.map(request, ActivityDO.class);
        this.saveOrUpdate(activityDO);
        return activityDO.getId();
    }

    @Override
    public Boolean delActivity(BaseActivityRequest request) {
        ActivityDO activityDO = PojoUtils.map(request, ActivityDO.class);
        return this.deleteByIdWithFill(activityDO) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
