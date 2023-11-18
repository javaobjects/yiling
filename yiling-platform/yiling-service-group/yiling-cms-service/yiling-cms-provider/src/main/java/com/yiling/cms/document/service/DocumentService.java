package com.yiling.cms.document.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.document.dto.request.AddDocumentRequest;
import com.yiling.cms.document.dto.request.QueryDocumentPageRequest;
import com.yiling.cms.document.dto.request.QueryDocumentRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentRequest;
import com.yiling.cms.document.entity.DocumentDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 文献 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
public interface DocumentService extends BaseService<DocumentDO> {

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
     * 文献列表
     * @param request
     * @return
     */
    List<DocumentDTO> list(QueryDocumentRequest request);

    /**
     * 查询批量
     * @param ids
     * @return
     */
    List<DocumentDTO> getDocumentByIds(List<Long> ids);

    /**
     * 更新阅读量
     * @param id
     */
    Integer updatePv(Long id);
}
