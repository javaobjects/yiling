package com.yiling.cms.content.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.AddHmcContentRequest;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import com.yiling.cms.content.dto.request.ContentReferStatusRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.cms.content.entity.B2bContentDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 大运河内容表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-06-25
 */
public interface B2bContentService extends BaseService<B2bContentDO> {

    /**
     * 添加内容
     *
     * @param request 新增内容
     */
    boolean addContent(AddHmcContentRequest request);

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

    /**
     * 更新浏览量
     *
     * @param id
     */
    void updateView(Long id);

    /**
     * 根据contentID批量查询
     *
     * @param contentIdList
     * @return
     */
    List<B2bContentDO> listByContentIdList(List<Long> contentIdList);
}
