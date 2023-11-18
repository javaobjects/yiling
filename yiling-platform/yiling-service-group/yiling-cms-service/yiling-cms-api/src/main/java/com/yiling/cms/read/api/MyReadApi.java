package com.yiling.cms.read.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.read.dto.MyReadDTO;
import com.yiling.cms.read.dto.request.AddMyReadRequest;
import com.yiling.cms.read.dto.request.QueryMyReadPageRequest;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
public interface MyReadApi {
    /**
     * 保存
     * @param request
     */
    void save(AddMyReadRequest request);

    /**
     * 分页
     *
     * @param queryMyReadPageRequest
     * @return
     */
    Page<MyReadDTO> queryPage(QueryMyReadPageRequest queryMyReadPageRequest);

}
