package com.yiling.basic.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.dto.request.QuerySysOperLogPageListRequest;
import com.yiling.basic.log.entity.SysOperLogDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-11
 */
public interface SysOperLogService extends BaseService<SysOperLogDO> {

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<SysOperLogDO> queryListPage(QuerySysOperLogPageListRequest request);
}
