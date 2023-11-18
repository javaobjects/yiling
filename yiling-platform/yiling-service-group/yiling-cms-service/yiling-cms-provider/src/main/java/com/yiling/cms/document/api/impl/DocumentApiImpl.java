package com.yiling.cms.document.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.document.api.DocumentApi;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.document.dto.request.AddDocumentRequest;
import com.yiling.cms.document.dto.request.QueryDocumentPageRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentRequest;
import com.yiling.cms.document.service.DocumentService;

/**
 * 文献
 * @author: gxl
 * @date: 2022/6/2
 */
@DubboService
public class DocumentApiImpl implements DocumentApi {
    @Autowired
    private DocumentService documentService;

    @Override
    public void addDocument(AddDocumentRequest request) {
        documentService.addDocument(request);
    }

    @Override
    public void updateDocument(UpdateDocumentRequest request) {
        documentService.updateDocument(request);
    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        return documentService.getDocumentById(id);
    }

    @Override
    public DocumentDTO getAppDocumentById(Long id) {
        return documentService.getAppDocumentById(id);
    }

    @Override
    public Page<DocumentDTO> listPage(QueryDocumentPageRequest request) {
        return documentService.listPage(request);
    }

    @Override
    public List<DocumentDTO> getDocumentByIds(List<Long> ids) {

        return documentService.getDocumentByIds(ids);
    }

}