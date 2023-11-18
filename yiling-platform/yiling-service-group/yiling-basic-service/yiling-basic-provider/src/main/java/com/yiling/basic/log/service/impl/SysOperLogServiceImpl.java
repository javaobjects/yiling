package com.yiling.basic.log.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.dao.SysOperLogMapper;
import com.yiling.basic.log.dto.request.QuerySysOperLogPageListRequest;
import com.yiling.basic.log.entity.SysOperLogDO;
import com.yiling.basic.log.service.SysOperLogService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-11
 */
@Service
public class SysOperLogServiceImpl extends BaseServiceImpl<SysOperLogMapper, SysOperLogDO> implements SysOperLogService {

    @Override
    public Page<SysOperLogDO> queryListPage(QuerySysOperLogPageListRequest request) {
        LambdaQueryWrapper<SysOperLogDO> queryWrapper = getLogQueryWrapper(request);

        return this.page(request.getPage(),queryWrapper);
    }

    private LambdaQueryWrapper<SysOperLogDO> getLogQueryWrapper(QuerySysOperLogPageListRequest request) {
        LambdaQueryWrapper<SysOperLogDO> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(request.getSystemId())){
            queryWrapper.eq(SysOperLogDO::getSystemId, request.getSystemId());
        }
        if(StrUtil.isNotEmpty(request.getRequestId())){
            queryWrapper.eq(SysOperLogDO::getRequestId, request.getRequestId());
        }
        if(StrUtil.isNotEmpty(request.getRequestMethod())){
            queryWrapper.eq(SysOperLogDO::getRequestMethod, request.getRequestMethod());
        }
        if(StrUtil.isNotEmpty(request.getBusinessType())){
            queryWrapper.eq(SysOperLogDO::getBusinessType, request.getBusinessType());
        }
        if(StrUtil.isNotEmpty(request.getTitle())){
            queryWrapper.like(SysOperLogDO::getTitle, request.getTitle());
        }
        if(StrUtil.isNotEmpty(request.getRequestData())){
            queryWrapper.like(SysOperLogDO::getRequestData, request.getRequestData());
        }
        if(StrUtil.isNotEmpty(request.getRequestUrl())){
            queryWrapper.eq(SysOperLogDO::getRequestUrl, request.getRequestUrl());
        }
        if(StrUtil.isNotEmpty(request.getResponseData())){
            queryWrapper.like(SysOperLogDO::getResponseData, request.getResponseData());
        }
        if(StrUtil.isNotEmpty(request.getErrorMsg())){
            queryWrapper.like(SysOperLogDO::getErrorMsg, request.getErrorMsg());
        }
        if(Objects.nonNull(request.getStatus()) && request.getStatus() != 0){
            queryWrapper.eq(SysOperLogDO::getStatus, request.getStatus());
        }
        if (Objects.nonNull(request.getOperId()) && request.getOperId() != 0) {
            queryWrapper.eq(SysOperLogDO::getOperId, request.getOperId());
        }
        if (Objects.nonNull(request.getStartOpTime())) {
            queryWrapper.ge(SysOperLogDO::getOpTime, request.getStartOpTime());
        }
        if (Objects.nonNull(request.getEndOpTime())) {
            queryWrapper.le(SysOperLogDO::getOpTime, request.getEndOpTime());
        }

        queryWrapper.orderByDesc(SysOperLogDO::getOpTime);

        return queryWrapper;
    }
}
