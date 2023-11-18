package com.yiling.workflow.workflow.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.workflow.workflow.entity.ActRuTaskDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gxl
 * @date 2023-05-23
 */
public interface ActRuTaskService extends BaseService<ActRuTaskDO> {

    /**
     * 添加待办并初始化已办数据
     * @param list
     */
    void batchInsert(List<ActRuTaskDO> list);
}
