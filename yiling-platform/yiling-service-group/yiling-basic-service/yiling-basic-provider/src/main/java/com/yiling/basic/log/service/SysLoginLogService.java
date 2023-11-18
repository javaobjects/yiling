package com.yiling.basic.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.log.dto.request.QuerySysLoginLogPageListRequest;
import com.yiling.basic.log.entity.SysLoginLogDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 系统登录日志记录 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021-12-31
 */
public interface SysLoginLogService extends BaseService<SysLoginLogDO> {

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<SysLoginLogDO> queryListPage(QuerySysLoginLogPageListRequest request);
}
