package com.yiling.cms.content.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dao.B2bContentMapper;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.AddHmcContentRequest;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import com.yiling.cms.content.dto.request.ContentReferStatusRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.cms.content.entity.B2bContentDO;
import com.yiling.cms.content.enums.CmsErrorCode;
import com.yiling.cms.content.service.B2bContentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

/**
 * <p>
 * 大运河内容表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-06-25
 */
@Service
public class B2bContentServiceImpl extends BaseServiceImpl<B2bContentMapper, B2bContentDO> implements B2bContentService {

    @Override
    public boolean addContent(AddHmcContentRequest request) {
        List<B2bContentDO> contentDOList = request.getList().stream().map(e -> {
            B2bContentDO b2bContentDO = PojoUtils.map(e, B2bContentDO.class);
            b2bContentDO.setOpUserId(request.getOpUserId());
            b2bContentDO.setReferStatus(1);
            return b2bContentDO;
        }).collect(Collectors.toList());
        return this.saveBatch(contentDOList);
    }

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        B2bContentDO contentDO = new B2bContentDO();
        if (Objects.nonNull(request.getCategoryRank())) {
            contentDO.setCategoryRank(request.getCategoryRank());
        }
        if (Objects.nonNull(request.getTopFlag())) {
            contentDO.setTopFlag(request.getTopFlag());
        }
        contentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<B2bContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), B2bContentDO::getId, request.getId());
        return this.update(contentDO, wrapper);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        B2bContentDO contentDO = new B2bContentDO();
        contentDO.setReferStatus(request.getReferStatus());
        contentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<B2bContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), B2bContentDO::getId, request.getId());
        return this.update(contentDO, wrapper);
    }

    @Override
    public Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request) {
        return this.getBaseMapper().listPage(request.getPage(), request);
    }

    @Override
    public void updateView(Long id) {
        QueryWrapper<B2bContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(B2bContentDO::getId, id);
        wrapper.last(" limit 1");
        B2bContentDO b2bContentDO = this.getOne(wrapper);
        if (Objects.isNull(b2bContentDO)) {
            throw new BusinessException(CmsErrorCode.CONTENT_NOT_FOUND);
        }
        b2bContentDO.setView(b2bContentDO.getView() + 1);
        this.updateById(b2bContentDO);
    }

    @Override
    public List<B2bContentDO> listByContentIdList(List<Long> contentIdList) {
        QueryWrapper<B2bContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(B2bContentDO::getContentId, contentIdList);
        return this.list(wrapper);
    }
}
