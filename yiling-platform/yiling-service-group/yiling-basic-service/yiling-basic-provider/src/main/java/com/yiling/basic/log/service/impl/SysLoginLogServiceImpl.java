package com.yiling.basic.log.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.dao.SysLoginLogMapper;
import com.yiling.basic.log.dto.request.QuerySysLoginLogPageListRequest;
import com.yiling.basic.log.entity.SysLoginLogDO;
import com.yiling.basic.log.service.SysLoginLogService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 系统登录日志记录 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021-12-31
 */
@Service
public class SysLoginLogServiceImpl extends BaseServiceImpl<SysLoginLogMapper, SysLoginLogDO> implements SysLoginLogService {

    @Override
    public Page<SysLoginLogDO> queryListPage(QuerySysLoginLogPageListRequest request) {
        LambdaQueryWrapper<SysLoginLogDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(request.getAppId())) {
            queryWrapper.eq(SysLoginLogDO::getAppId, request.getAppId());
        }
        if (Objects.nonNull(request.getUserId()) && request.getUserId() != 0) {
            queryWrapper.like(SysLoginLogDO::getUserId, request.getUserId());
        }
        if (StrUtil.isNotEmpty(request.getLoginAccount())) {
            queryWrapper.eq(SysLoginLogDO::getLoginAccount, request.getLoginAccount());
        }
        if (Objects.nonNull(request.getStartLoginTime())) {
            queryWrapper.ge(SysLoginLogDO::getLoginTime, request.getStartLoginTime());
        }
        if (Objects.nonNull(request.getEndLoginTime())) {
            queryWrapper.le(SysLoginLogDO::getLoginTime, request.getEndLoginTime());
        }
        queryWrapper.orderByDesc(SysLoginLogDO::getLoginTime);

        return this.page(request.getPage(), queryWrapper);
    }


}
