package com.yiling.cms.content.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.QaDTO;
import com.yiling.cms.content.dto.request.AddQaRequest;
import com.yiling.cms.content.dto.request.QueryContentPageRequest;
import com.yiling.cms.content.dto.request.QueryQAPageRequest;
import com.yiling.cms.content.dto.request.SwitchQAStatusRequest;

import java.util.List;

/**
 * 内容问答API
 *
 * @author: fan.shen
 * @date: 2023/3/23
 */
public interface QaApi {

    /**
     * 添加问答
     *
     * @param request
     */
    Long add(AddQaRequest request);

    /**
     * 根据contentID获取问答数量
     *
     * @param contentId
     */
    Integer getQaCountByContentId(Long contentId);

    /**
     * 根据内容id获取所有问答列表
     *
     * @param contentId
     * @return
     */
    List<QaDTO> getQaListByContentId(Long contentId);

    /**
     * QA分页
     *
     * @param request
     * @return
     */
    Page<QaDTO> listPage(QueryQAPageRequest request);

    /**
     * 切换显示状态
     * @param request
     * @return
     */
    Boolean switchShowStatus(SwitchQAStatusRequest request);
}
