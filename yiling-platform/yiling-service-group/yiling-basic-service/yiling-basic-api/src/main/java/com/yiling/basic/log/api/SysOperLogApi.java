package com.yiling.basic.log.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.dto.SysOperLogDTO;
import com.yiling.basic.log.dto.request.QuerySysOperLogPageListRequest;

/**
 * 系统操作日志 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
public interface SysOperLogApi {

    /**
     * 保存操作日志
     *
     * @param sysOperLogDTO
     * @return
     */
    boolean save(SysOperLogDTO sysOperLogDTO);

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<SysOperLogDTO> queryListPage(QuerySysOperLogPageListRequest request);
}
