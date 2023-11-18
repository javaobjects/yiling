package com.yiling.cms.document.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.yiling.cms.content.enums.CmsErrorCode;
import com.yiling.cms.content.enums.ContentStatusEnum;
import com.yiling.cms.document.dao.DocumentMapper;
import com.yiling.cms.document.dto.DocumentDTO;
import com.yiling.cms.document.dto.request.AddDocumentRequest;
import com.yiling.cms.document.dto.request.QueryDocumentPageRequest;
import com.yiling.cms.document.dto.request.QueryDocumentRequest;
import com.yiling.cms.document.dto.request.UpdateDocumentRequest;
import com.yiling.cms.document.entity.DocumentDO;
import com.yiling.cms.document.entity.DocumentGoodsDO;
import com.yiling.cms.document.service.DocumentGoodsService;
import com.yiling.cms.document.service.DocumentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 文献 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
@Service
public class DocumentServiceImpl extends BaseServiceImpl<DocumentMapper, DocumentDO> implements DocumentService {

    @Autowired
    private DocumentGoodsService documentGoodsService;

    @Autowired
    private FileService fileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDocument(AddDocumentRequest request) {
        DocumentDO documentDO = new DocumentDO();
        PojoUtils.map(request, documentDO);
        if (request.getStatus().equals(ContentStatusEnum.PUBLISHED.getCode())) {
            documentDO.setPublishTime(DateUtil.date());
        }
        documentDO.setDisplayLine(Joiner.on(",").join(request.getDisplayLines()));
        this.save(documentDO);
        List<Long> standardGoodsIdList = request.getStandardGoodsIdList();
        List<DocumentGoodsDO> documentGoodsDOS = Lists.newArrayListWithExpectedSize(standardGoodsIdList.size());
        standardGoodsIdList.forEach(goodsId->{
            DocumentGoodsDO documentGoodsDO = new DocumentGoodsDO();
            documentGoodsDO.setStandardGoodsId(goodsId).setDocumentId(documentDO.getId());
            documentGoodsDOS.add(documentGoodsDO);
        });
        documentGoodsService.saveBatch(documentGoodsDOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocument(UpdateDocumentRequest request) {
        DocumentDO documentDO = new DocumentDO();
        PojoUtils.map(request, documentDO);
        // 发布时间处理-只第一次发布的时候填充发布时间
        if (request.getStatus().equals(ContentStatusEnum.PUBLISHED.getCode())) {
            DocumentDO oldContent = this.getById(request.getId());
            if(oldContent.getPublishTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0){
                documentDO.setPublishTime(DateUtil.date());
            }
        }
        if(CollUtil.isNotEmpty(request.getDisplayLines())){
            documentDO.setDisplayLine(Joiner.on(",").join(request.getDisplayLines()));
        }

        this.updateById(documentDO);
        //编辑状态不走以下逻辑
        if(Objects.isNull(request.getCategoryId())){
            return;
        }
        LambdaQueryWrapper<DocumentGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DocumentGoodsDO::getDocumentId, request.getId());
        List<DocumentGoodsDO> list = documentGoodsService.list(wrapper);
        List<Long> goodsIds = list.stream().map(DocumentGoodsDO::getStandardGoodsId).collect(Collectors.toList());
        List<Long> intersection = CollUtil.intersection(request.getStandardGoodsIdList(), goodsIds).stream().collect(Collectors.toList());
        if(intersection.size()!=goodsIds.size() || request.getStandardGoodsIdList().size()!=goodsIds.size()){
            List<Long> lineList = list.stream().map(DocumentGoodsDO::getId).collect(Collectors.toList());
            documentGoodsService.removeByIds(lineList);
            List<DocumentGoodsDO> documentGoodsDOList = Lists.newArrayList();
            request.getStandardGoodsIdList().forEach(goodsId -> {
                DocumentGoodsDO documentGoodsDO = new DocumentGoodsDO();
                documentGoodsDO.setDocumentId(documentDO.getId()).setStandardGoodsId(goodsId);
                documentGoodsDO.setCreateUser(request.getOpUserId());
                documentGoodsDOList.add(documentGoodsDO);
            });
            documentGoodsService.saveBatch(documentGoodsDOList);
        }


    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        DocumentDO documentDO = this.getById(id);
        DocumentDTO documentDTO = new DocumentDTO();

        PojoUtils.map(documentDO,documentDTO);
        LambdaQueryWrapper<DocumentGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DocumentGoodsDO::getDocumentId,id).select(DocumentGoodsDO::getStandardGoodsId);
        List<DocumentGoodsDO> documentGoodsDOS = documentGoodsService.list(wrapper);
        documentDTO.setStandardGoodsIdList(documentGoodsDOS.stream().map(DocumentGoodsDO::getStandardGoodsId).collect(Collectors.toList()));
        return documentDTO;
    }

    @Override
    public DocumentDTO getAppDocumentById(Long id) {
        DocumentDO documentDO = this.getById(id);
        if(Objects.isNull(documentDO)){
            throw new BusinessException(CmsErrorCode.DOCUMENT_OFFLINE);
        }
        // 内容下架处理
        if(documentDO.getStatus().equals(ContentStatusEnum.UN_PUBLISH.getCode())){
            throw new BusinessException(CmsErrorCode.DOCUMENT_OFFLINE);
        }
        DocumentDTO documentDTO = new DocumentDTO();
        PojoUtils.map(documentDO,documentDTO);
        documentDTO.setDocumentFileUrl(fileService.getUrl(documentDO.getDocumentFileUrl(), FileTypeEnum.DOCUMENT_FILE_URL));
        Integer pageView =  this.updatePv(id);
        documentDTO.setPageView(pageView);
        return documentDTO;
    }

    @Override
    public Page<DocumentDTO> listPage(QueryDocumentPageRequest request) {
        LambdaQueryWrapper<DocumentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(Objects.nonNull(request.getStartTime()), DocumentDO::getCreateTime, request.getStartTime());
        wrapper.eq(Objects.nonNull(request.getCategoryId()), DocumentDO::getCategoryId, request.getCategoryId());
        wrapper.eq(Objects.nonNull(request.getStatus()), DocumentDO::getStatus, request.getStatus());
        wrapper.eq(Objects.nonNull(request.getIsOpen()), DocumentDO::getIsOpen, request.getIsOpen());
        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
            wrapper.le(DocumentDO::getCreateTime, request.getEndTime());
        }
        wrapper.like(StringUtils.isNotBlank(request.getDisplayLine()),DocumentDO::getDisplayLine,request.getDisplayLine());
        wrapper.like(StringUtils.isNotBlank(request.getTitle()), DocumentDO::getTitle, request.getTitle()).orderByDesc(DocumentDO::getId);
        Page<DocumentDO> documentDOPage = this.page(request.getPage(), wrapper);
        if (documentDOPage.getTotal() == 0) {
            return new Page<>();
        }
        Page<DocumentDTO> page = PojoUtils.map(documentDOPage,DocumentDTO.class);
        return page;
    }

    @Override
    public List<DocumentDTO> list(QueryDocumentRequest request) {
        LambdaQueryWrapper<DocumentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(request.getStatus()),DocumentDO::getStatus,request.getStatus());
        List<DocumentDO> list = this.list(wrapper);
        return PojoUtils.map(list,DocumentDTO.class);
    }

    @Override
    public List<DocumentDTO> getDocumentByIds(List<Long> ids) {
        LambdaQueryWrapper<DocumentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(DocumentDO :: getId,ids);
        List<DocumentDO> list = this.list(wrapper);
        return PojoUtils.map(list,DocumentDTO.class);
    }

    @Override
    public Integer updatePv(Long id) {
        LambdaQueryWrapper<DocumentDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DocumentDO::getId,id).select(DocumentDO::getPageView).last("limit 1");
        DocumentDO contentDO = this.getOne(wrapper);
        contentDO.setId(id);
        Integer pageView = contentDO.getPageView()+1;
        contentDO.setPageView(pageView);
        this.baseMapper.updatePv(contentDO);
        return pageView;
    }
}
