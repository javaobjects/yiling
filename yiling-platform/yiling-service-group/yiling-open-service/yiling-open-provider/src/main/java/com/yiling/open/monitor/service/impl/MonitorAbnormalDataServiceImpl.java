package com.yiling.open.monitor.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.open.monitor.dao.MonitorAbnormalDataMapper;
import com.yiling.open.monitor.dto.request.QueryErpMonitorSaleExceptionPageRequest;
import com.yiling.open.monitor.entity.MonitorAbnormalDataDO;
import com.yiling.open.monitor.service.MonitorAbnormalDataService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-08
 */
@Service
public class MonitorAbnormalDataServiceImpl extends BaseServiceImpl<MonitorAbnormalDataMapper, MonitorAbnormalDataDO> implements MonitorAbnormalDataService {

    @Override
    public boolean insertMonitorAbnormalData(MonitorAbnormalDataDO monitorAbnormalDataDO) {
        return this.save(monitorAbnormalDataDO);
    }

    @Override
    public Page<MonitorAbnormalDataDO> page(QueryErpMonitorSaleExceptionPageRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getFlowTimeStart(), "参数 flowTimeStart 不能为空");
        Assert.notNull(request.getFlowTimeEnd(), "参数 flowTimeEnd 不能为空");
        Page<MonitorAbnormalDataDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, pageQueryWrapper(request));
    }

    @Override
    public Long getSaleExceptionCount(Date startTime, Date endTime) {
        Long exceptionCount = this.baseMapper.getSaleExceptionCount(startTime, endTime);
        if(ObjectUtil.isNotNull(exceptionCount)){
            return exceptionCount;
        }
        return 0L;
    }

    @Override
    public List<MonitorAbnormalDataDO> getByIdList(List<Long> idList) {
        if(CollUtil.isEmpty(idList)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<MonitorAbnormalDataDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(MonitorAbnormalDataDO::getId, idList);
        return this.list(lambdaQueryWrapper);
    }

    private LambdaQueryWrapper<MonitorAbnormalDataDO> pageQueryWrapper(QueryErpMonitorSaleExceptionPageRequest request) {
        LambdaQueryWrapper<MonitorAbnormalDataDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        Long eid = request.getEid();
        if (ObjectUtil.isNotNull(eid) && 0 != eid.intValue()) {
            lambdaQueryWrapper.eq(MonitorAbnormalDataDO::getEid, eid);
        }

        Long parentId = request.getParentId();
        if (ObjectUtil.isNotNull(parentId) && 0 != parentId.intValue()) {
            lambdaQueryWrapper.eq(MonitorAbnormalDataDO::getId, parentId);
        }

        String ename = request.getEname();
        if (StrUtil.isNotBlank(ename)) {
            lambdaQueryWrapper.like(MonitorAbnormalDataDO::getEname, ename);
        }

        String soId = request.getSoId();
        if (StrUtil.isNotBlank(soId)) {
            lambdaQueryWrapper.like(MonitorAbnormalDataDO::getSoId, soId);
        }

        String soNo = request.getSoNo();
        if (StrUtil.isNotBlank(soNo)) {
            lambdaQueryWrapper.like(MonitorAbnormalDataDO::getSoNo, soNo);
        }

        Long controlId = request.getControlId();
        if (ObjectUtil.isNotNull(controlId)) {
            lambdaQueryWrapper.like(MonitorAbnormalDataDO::getControlId, controlId);
        }

        Date flowTimeStart = request.getFlowTimeStart();
        if (ObjectUtil.isNotNull(flowTimeStart)) {
            lambdaQueryWrapper.ge(MonitorAbnormalDataDO::getFlowTime, DateUtil.beginOfDay(flowTimeStart));
        }

        Date flowTimeEnd = request.getFlowTimeEnd();
        if (ObjectUtil.isNotNull(flowTimeEnd)) {
            lambdaQueryWrapper.le(MonitorAbnormalDataDO::getFlowTime, DateUtil.endOfDay(flowTimeEnd));
        }

        // 异常类型：1超过3天以后上传的数据
        lambdaQueryWrapper.eq(MonitorAbnormalDataDO::getDataType, 1);
        lambdaQueryWrapper.orderByDesc(MonitorAbnormalDataDO::getId);
        return lambdaQueryWrapper;
    }

}
