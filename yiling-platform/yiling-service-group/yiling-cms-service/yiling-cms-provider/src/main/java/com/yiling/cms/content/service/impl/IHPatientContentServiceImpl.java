package com.yiling.cms.content.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dao.IHPatientContentMapper;
import com.yiling.cms.content.dto.ContentDTO;
import com.yiling.cms.content.dto.IHPatientContentPageDTO;
import com.yiling.cms.content.dto.IHPatientHomeContentPageDTO;
import com.yiling.cms.content.dto.request.AddContentRequest;
import com.yiling.cms.content.dto.request.AddIHPatientContentRequest;
import com.yiling.cms.content.dto.request.AddOrUpdateIHPatientContentCategoryRequest;
import com.yiling.cms.content.dto.request.QueryIHPatientContentPageRequest;
import com.yiling.cms.content.dto.request.UpdateContentCategoryRankRequest;
import com.yiling.cms.content.dto.request.UpdateContentRequest;
import com.yiling.cms.content.dto.request.UpdateIHPatientContentRequest;
import com.yiling.cms.content.entity.IHPatientContentDO;
import com.yiling.cms.content.service.ContentService;
import com.yiling.cms.content.service.IHPatientContentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

/**
 * <p>
 * IH患者端内容表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-11
 */
@Service
public class IHPatientContentServiceImpl extends BaseServiceImpl<IHPatientContentMapper, IHPatientContentDO> implements IHPatientContentService {

    @Autowired
    private ContentService contentService;


    @Override
    public List<IHPatientContentDO> listByContentIdList(List<Long> contentIdList) {
        QueryWrapper<IHPatientContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(IHPatientContentDO::getContentId, contentIdList);
        return this.list(wrapper);
    }

    @Override
    public void addIHPatientContent(AddIHPatientContentRequest request) {
        AddContentRequest contentRequest = PojoUtils.map(request, AddContentRequest.class);
        contentRequest.setSourceContentType(1);
        Long contentId = contentService.addContent(contentRequest);
        List<IHPatientContentDO> contentDOS = PojoUtils.map(request.getCategoryList(), IHPatientContentDO.class);
        contentDOS.forEach(content -> {
            content.setCreateUser(request.getOpUserId());
            content.setUpdateUser(request.getOpUserId());
            content.setContentId(contentId);
        });
        this.saveBatch(contentDOS);
    }

    @Override
    public void updateIHPatientContent(UpdateIHPatientContentRequest request) {
        UpdateContentRequest contentRequest = PojoUtils.map(request, UpdateContentRequest.class);
        contentRequest.setId(request.getId());
        contentService.updateContent(contentRequest);
        List<AddOrUpdateIHPatientContentCategoryRequest> categoryList = request.getCategoryList();
        //全部删除
        QueryWrapper<IHPatientContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IHPatientContentDO::getContentId, request.getId());
        IHPatientContentDO contentDO = new IHPatientContentDO();
        contentDO.setOpUserId(request.getOpUserId());
        contentDO.setUpdateTime(request.getOpTime());
        this.batchDeleteWithFill(contentDO, wrapper);

        for (AddOrUpdateIHPatientContentCategoryRequest categoryRequest : categoryList) {
            IHPatientContentDO map = PojoUtils.map(categoryRequest, IHPatientContentDO.class);
            map.setContentId(request.getId());
            IHPatientContentDO ihPatientContentDO = this.baseMapper.selectIgnoreLogicDelete(map);
            if (Objects.nonNull(ihPatientContentDO)) {
                //修改
                ihPatientContentDO.setDelFlag(0);
                ihPatientContentDO.setUpdateTime(request.getOpTime());
                ihPatientContentDO.setUpdateUser(request.getOpUserId());
                int i = this.baseMapper.updateDelFlag(ihPatientContentDO);
            } else {
                //添加
                map.setContentId(request.getId());
                map.setCreateUser(request.getOpUserId());
                this.save(map);
            }
        }
    }

    @Override
    public Page<IHPatientContentPageDTO> listContentPageBySql(QueryIHPatientContentPageRequest request) {
        return this.baseMapper.listContentPageBySql(request.getPage(), request);
    }

    @Override
    public Boolean updateLikeCount(Long cmsId, Long likeCount) {
        IHPatientContentDO patientContentDO = this.getById(cmsId);
        patientContentDO.setLikeCount(likeCount);
        return this.updateById(patientContentDO);
    }

    @Override
    public void updateView(Long id) {
        QueryWrapper<IHPatientContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IHPatientContentDO::getId, id);
        IHPatientContentDO ihPatientContentDO = this.getOne(wrapper);
        ihPatientContentDO.setView(ihPatientContentDO.getView() + 1);
        this.updateById(ihPatientContentDO);
    }

    @Override
    public List<IHPatientContentDO> getIHPatientContentByContentIds(List<Long> contentIds) {
        LambdaQueryWrapper<IHPatientContentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IHPatientContentDO::getContentId, contentIds);
        return this.list(wrapper);
    }

    @Override
    public Boolean updateContentCategoryRank(UpdateContentCategoryRankRequest request) {
        List<AddOrUpdateIHPatientContentCategoryRequest> categoryList = request.getCategoryList();
        for (AddOrUpdateIHPatientContentCategoryRequest categoryRequest : categoryList) {
            QueryWrapper<IHPatientContentDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(IHPatientContentDO::getContentId, request.getId()).eq(IHPatientContentDO::getLineId, categoryRequest.getLineId()).eq(IHPatientContentDO::getModuleId, categoryRequest.getModuleId()).eq(IHPatientContentDO::getCategoryId, categoryRequest.getCategoryId());
            IHPatientContentDO contentDO = new IHPatientContentDO();
            contentDO.setCategoryRank(categoryRequest.getCategoryRank());
            contentDO.setUpdateUser(request.getOpUserId());
            contentDO.setUpdateTime(request.getOpTime());
            this.update(contentDO, wrapper);
        }
        return true;
    }

    @Override
    public Page<IHPatientHomeContentPageDTO> homeListContentPageBySql(QueryIHPatientContentPageRequest request) {
        return this.baseMapper.homeListContentPageBySql(request.getPage(), request);
    }

    @Override
    public ContentDTO getPatientContent(Long id, Long categoryId, Long moduleId) {
        ContentDTO content = contentService.getContentById(id);
        contentService.updatePv(id);
        if (Objects.nonNull(categoryId) && Objects.nonNull(moduleId)) {
            QueryWrapper<IHPatientContentDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(IHPatientContentDO::getContentId, id).eq(IHPatientContentDO::getCategoryId, categoryId).eq(IHPatientContentDO::getModuleId, moduleId);
            IHPatientContentDO ihPatientContentDO = this.getOne(wrapper);
            ihPatientContentDO.setView(ihPatientContentDO.getView() + 1);
            this.updateById(ihPatientContentDO);
        }
        return content;
    }
}
