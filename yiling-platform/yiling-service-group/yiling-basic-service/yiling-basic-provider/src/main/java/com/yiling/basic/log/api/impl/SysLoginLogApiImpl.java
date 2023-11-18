package com.yiling.basic.log.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.api.SysLoginLogApi;
import com.yiling.basic.log.dto.SysLoginLogDTO;
import com.yiling.basic.log.dto.request.QuerySysLoginLogPageListRequest;
import com.yiling.basic.log.entity.SysLoginLogDO;
import com.yiling.basic.log.service.SysLoginLogService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * 系统登录日志 API 实现
 *
 * @author: lun.yu
 * @date: 2021/12/31
 */
@DubboService
public class SysLoginLogApiImpl implements SysLoginLogApi {

    @Autowired
    SysLoginLogService sysLoginLogService;

    @Override
    public boolean save(SysLoginLogDTO sysLoginLogDTO) {
        SysLoginLogDO entity = PojoUtils.map(sysLoginLogDTO, SysLoginLogDO.class);
        return sysLoginLogService.save(entity);
    }

    @Override
    public Page<SysLoginLogDTO> queryListPage(QuerySysLoginLogPageListRequest request) {
        return PojoUtils.map(sysLoginLogService.queryListPage(request),SysLoginLogDTO.class);
    }
}
