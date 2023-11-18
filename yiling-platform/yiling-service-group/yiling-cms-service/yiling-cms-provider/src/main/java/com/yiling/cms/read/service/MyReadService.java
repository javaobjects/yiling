package com.yiling.cms.read.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.read.dto.MyReadDTO;
import com.yiling.cms.read.dto.request.AddMyReadRequest;
import com.yiling.cms.read.dto.request.QueryMyReadPageRequest;
import com.yiling.cms.read.entity.MyReadDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 我的阅读 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-07-28
 */
public interface MyReadService extends BaseService<MyReadDO> {

    /**
     * 新增
     * @param request
     */
    void add(AddMyReadRequest request);

    /**
   * 分页+
     *
     * @param queryMyReadPageRequest
     * @return
     */
    Page<MyReadDTO> queryPage(QueryMyReadPageRequest queryMyReadPageRequest);
}
