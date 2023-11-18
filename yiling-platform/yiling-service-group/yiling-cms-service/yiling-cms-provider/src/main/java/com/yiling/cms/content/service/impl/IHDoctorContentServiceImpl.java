package com.yiling.cms.content.service.impl;

import java.util.List;
import java.util.Objects;

import com.yiling.cms.content.dto.request.*;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dao.IHDoctorContentMapper;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.IHDocContentDTO;
import com.yiling.cms.content.entity.IHDoctorContentDO;
import com.yiling.cms.content.service.IHDocContentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

/**
 * <p>
 * IH医生端内容表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-07
 */
@Service
public class IHDoctorContentServiceImpl extends BaseServiceImpl<IHDoctorContentMapper, IHDoctorContentDO> implements IHDocContentService {


    @Override
    public Boolean addContent(AddIHDocContentRequest addContentRequest) {
        List<IHDoctorContentDO> contentDOList = PojoUtils.map(addContentRequest.getContentList(), IHDoctorContentDO.class);
        contentDOList.forEach(content->{
            content.setCreateUser(addContentRequest.getOpUserId());
            content.setUpdateUser(addContentRequest.getOpUserId());
            content.setReferStatus(1);
        });
        return this.saveBatch(contentDOList);
    }

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        IHDoctorContentDO contentDO = new IHDoctorContentDO();
        if (Objects.nonNull(request.getCategoryRank())) {
            contentDO.setCategoryRank(request.getCategoryRank());
        }
        if (Objects.nonNull(request.getTopFlag())) {
            contentDO.setTopFlag(request.getTopFlag());
        }
        contentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<IHDoctorContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), IHDoctorContentDO::getId, request.getId());
        return this.update(contentDO, wrapper);
    }

    @Override
    public Page<IHDocContentDTO> listPage(QueryIHDocContentPageRequest request) {
        return this.getBaseMapper().listPage(request.getPage(), request);
    }

    @Override
    public Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request) {
        Page<AppContentDTO> page = this.getBaseMapper().listAppContentPageBySql(request.getPage(), request);
        return PojoUtils.map(page, AppContentDTO.class);
    }

    @Override
    public List<IHDoctorContentDO> listByContentIdList(List<Long> idList) {
        QueryWrapper<IHDoctorContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(IHDoctorContentDO::getContentId, idList);
        return this.list(wrapper);
    }

    @Override
    public void updateView(Long id) {
        QueryWrapper<IHDoctorContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(IHDoctorContentDO::getId, id);
        IHDoctorContentDO ihDocContentDO = this.getOne(wrapper);
        ihDocContentDO.setView(ihDocContentDO.getView() + 1);
        this.updateById(ihDocContentDO);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        IHDoctorContentDO contentDO = new IHDoctorContentDO();
        contentDO.setReferStatus(request.getReferStatus());
        contentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<IHDoctorContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), IHDoctorContentDO::getId, request.getId());
        return this.update(contentDO, wrapper);
    }

    @Override
    public Boolean updateIHDocContentAuth(UpdateContentAuthRequest request) {
        IHDoctorContentDO contentDO = new IHDoctorContentDO();
        contentDO.setContentAuth(request.getContentAuth());
        contentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<IHDoctorContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), IHDoctorContentDO::getId, request.getId());
        return this.update(contentDO, wrapper);
    }
}
