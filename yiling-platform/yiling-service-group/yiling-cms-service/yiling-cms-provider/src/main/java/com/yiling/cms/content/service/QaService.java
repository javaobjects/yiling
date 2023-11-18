package com.yiling.cms.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.QaDTO;
import com.yiling.cms.content.dto.request.QueryQAPageRequest;
import com.yiling.cms.content.dto.request.SwitchQAStatusRequest;
import com.yiling.cms.content.entity.QaDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 问答表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
public interface QaService extends BaseService<QaDO> {

    /**
     * 根据contentID获取问答数量
     *
     * @param contentId
     * @return
     */
    Integer getQaCountByContentId(Long contentId);

    /**
     * 获取问答列表
     *
     * @param contentId
     * @return
     */
    List<QaDTO> getQaListByContentId(Long contentId);

    /**
     * QA列表分页
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
