package com.yiling.basic.log.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.dto.SysLoginLogDTO;
import com.yiling.basic.log.dto.request.QuerySysLoginLogPageListRequest;

/**
 * 系统登录日志 API
 *
 * @author: lun.yu
 * @date: 2021/12/31
 */
public interface SysLoginLogApi {

    /**
     * 保存登录日志
     *
     * @param sysLoginLogDTO
     * @return
     */
    boolean save(SysLoginLogDTO sysLoginLogDTO);

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<SysLoginLogDTO> queryListPage(QuerySysLoginLogPageListRequest request);
}
