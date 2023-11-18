package com.yiling.sales.assistant.task.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.DistributorDTO;
import com.yiling.sales.assistant.task.dto.app.TaskDistributorDTO;
import com.yiling.sales.assistant.task.dto.request.DeleteTaskDistributorRequest;
import com.yiling.sales.assistant.task.dto.request.QueryDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorPageRequest;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorRequest;
import com.yiling.sales.assistant.task.entity.TaskDistributorDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gxl
 * @date 2021-10-11
 */
public interface TaskDistributorService extends BaseService<TaskDistributorDO> {

    /**
     * 选择配送商
     * @param queryDistributorPageRequest
     * @return
     */
    Page<DistributorDTO> listDistributorPage(QueryDistributorPageRequest queryDistributorPageRequest);

    /**
     * 编辑任务-配送商反显
     * @param queryTaskDistributorPageRequest
     * @return
     */
    Page<DistributorDTO> listTaskDistributorPage(QueryTaskDistributorPageRequest queryTaskDistributorPageRequest);

    /**
     * app端任务详情-查看配送商
     * @param queryTaskDistributorPageRequest
     * @return
     */
    Page<TaskDistributorDTO> listAppTaskDistributorPage(com.yiling.sales.assistant.task.dto.request.app.QueryTaskDistributorPageRequest queryTaskDistributorPageRequest);

    /**
     * 删除
     * @param deleteTaskDistributorRequest
     */
    void deleteById(DeleteTaskDistributorRequest deleteTaskDistributorRequest) ;

    /**
     * 根据配送商eid查询任务配送商eid
     * @param request
     * @return
     */
    List<Long> queryDistributorByEidList(QueryTaskDistributorRequest request);
}
