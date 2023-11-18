package com.yiling.cms.collect.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectPageRequest;
import com.yiling.cms.collect.dto.request.QueryCollectRequest;
import com.yiling.cms.collect.dto.request.SaveCollectRequest;
import com.yiling.cms.collect.entity.MyCollectDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 我的收藏 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-07-29
 */
public interface MyCollectService extends BaseService<MyCollectDO> {

    /**
     * 保存收藏
     * @param request
     */
    void save(SaveCollectRequest request);

    /**
     * 我的收藏
     * @param request
     * @return
     */
    Page<MyCollectDTO> queryPage(QueryCollectPageRequest request);

    /**
     * 条件查询单个
     * @param request
     * @return
     */
    MyCollectDTO getOneCollect(QueryCollectRequest request);
}
