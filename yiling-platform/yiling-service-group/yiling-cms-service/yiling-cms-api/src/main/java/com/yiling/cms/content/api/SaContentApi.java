package com.yiling.cms.content.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.AddHmcContentRequest;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import com.yiling.cms.content.dto.request.ContentReferStatusRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;

/**
 * 销售助手内容管理API
 *
 * @author: yong.zhang
 * @date: 2023/6/26 0026
 */
public interface SaContentApi {

    /**
     * 添加内容
     *
     * @param request 新增内容
     */
    void addContent(AddHmcContentRequest request);

    /**
     * 内容排序
     *
     * @param request 操作条件
     * @return 成功/失败
     */
    Boolean contentRank(ContentRankRequest request);

    /**
     * 修改引用状态
     *
     * @param request 操作条件
     * @return 成功/失败
     */
    Boolean updateReferStatus(ContentReferStatusRequest request);

    /**
     * 内容分页
     *
     * @param request 分页查询条件
     * @return 销售助手内容列表信息
     */
    Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request);
}
