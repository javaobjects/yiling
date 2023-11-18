package com.yiling.cms.content.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.IHDocContentDTO;
import com.yiling.cms.content.dto.request.*;

import java.util.List;

/**
 * IHDoc内容管理
 * @author: fan.shen
 * @date: 2022/3/23
 */
public interface IHDocContentApi {

    /**
     * 添加内容
     * @param addContentRequest
     */
     void addContent(AddIHDocContentRequest addContentRequest);

    /**
     * 内容分页
     * @param request
     * @return
     */
    Page<IHDocContentDTO> listPage(QueryIHDocContentPageRequest request);

    /**
     * 内容排序
     * @param request
     * @return
     */
    Boolean contentRank(ContentRankRequest request);


    /**
     * 分页查询内容
     *
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request);

    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    IHDocContentDTO getIhDocContentById(Long id);

    /**
     * 批量查询
     * @param contentIdList
     * @return
     */
    List<IHDocContentDTO> getContentByContentIdList(List<Long> contentIdList);

    /**
     * 修改引用状态
     * @param request
     * @return
     */
    Boolean updateReferStatus(ContentReferStatusRequest request);

    /**
     * 更新IHDoc文章权限
     * @param request
     * @return
     */
    Boolean updateIHDocContentAuth(UpdateContentAuthRequest request);
}
