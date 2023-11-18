package com.yiling.basic.log.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.api.SysOperLogApi;
import com.yiling.basic.log.dto.SysOperLogDTO;
import com.yiling.basic.log.dto.request.QuerySysOperLogPageListRequest;
import com.yiling.basic.log.entity.SysOperLogDO;
import com.yiling.basic.log.service.SysOperLogService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.util.StrUtil;

/**
 * 系统操作日志 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
@DubboService
public class SysOperLogApiImpl implements SysOperLogApi {

    @Autowired
    SysOperLogService sysOperLogService;

    @Override
    public boolean save(SysOperLogDTO sysOperLogDTO) {
        SysOperLogDO entity = PojoUtils.map(sysOperLogDTO, SysOperLogDO.class);
        // 最大2000个字符
        entity.setRequestData(StrUtil.sub(entity.getRequestData(), 0, 2000));
        entity.setResponseData(StrUtil.sub(entity.getResponseData(), 0, 2000));
        entity.setErrorMsg(StrUtil.sub(entity.getErrorMsg(), 0, 2000));
        return sysOperLogService.save(entity);
    }

    @Override
    public Page<SysOperLogDTO> queryListPage(QuerySysOperLogPageListRequest request) {
        return PojoUtils.map(sysOperLogService.queryListPage(request),SysOperLogDTO.class);
    }
}
