package com.yiling.cms.document.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.document.dto.request.AddDocumentRequest;
import com.yiling.cms.document.dto.request.QueryDocumentPageRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentRequest;

/**
 * @author: gxl
 * @date: 2022/6/2
 */
public interface DocumentApi {
    /**
     * 添加文献
     * @param request
     */
    void addDocument(AddDocumentRequest request);

    /**
     * 编辑
     * @param request
     */
    void updateDocument(UpdateDocumentRequest request);

    /**
     * 查询单个
     * @param id
     * @return
     */
    DocumentDTO getDocumentById(Long id);

    /**
     * 手机端查看详情
     * @param id
     * @return
     */
    DocumentDTO getAppDocumentById(Long id);
    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<DocumentDTO> listPage(QueryDocumentPageRequest request);

    /**
     * 查询批量
     * @param ids
     * @return
     */
    List<DocumentDTO> getDocumentByIds(List<Long> ids);

}
