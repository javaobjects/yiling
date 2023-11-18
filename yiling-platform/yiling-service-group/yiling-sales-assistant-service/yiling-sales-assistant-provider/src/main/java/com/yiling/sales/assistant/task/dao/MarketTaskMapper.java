package com.yiling.sales.assistant.task.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sales.assistant.task.dto.request.QueryTaskPageRequest;
import com.yiling.sales.assistant.task.entity.MarketTaskDO;

/**
 * <p>
 * 任务基本信息表  Dao 接口
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Repository
public interface MarketTaskMapper extends BaseMapper<MarketTaskDO> {
    /**
     * 任务分页列表
     * @param page
     * @param queryTaskPageRequest
     * @return
     */
    IPage<MarketTaskDO> queryTaskListPage(Page page, @Param("t") QueryTaskPageRequest queryTaskPageRequest);

}
