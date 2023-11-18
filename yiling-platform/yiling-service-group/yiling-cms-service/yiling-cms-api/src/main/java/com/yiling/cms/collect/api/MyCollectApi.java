package com.yiling.cms.collect.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectPageRequest;
import com.yiling.cms.collect.dto.request.QueryCollectRequest;
import com.yiling.cms.collect.dto.request.SaveCollectRequest;

/**
 * 收藏
 * @author: gxl
 * @date: 2022/7/29
 */
public interface MyCollectApi {

    /**
     * 保存收藏
     * @param request
     */
    void save(SaveCollectRequest request);

    /**
     * 我的收藏列表
     * @param request
     * @return
     */
    Page<MyCollectDTO> queryPage(QueryCollectPageRequest request);

    /**
     * 查询收藏状态
     * @param request
     * @return
     */
    MyCollectDTO getOne(QueryCollectRequest request);
}
