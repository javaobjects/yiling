package com.yiling.cms.content.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dao.SaContentMapper;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.AddHmcContentRequest;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import com.yiling.cms.content.dto.request.ContentReferStatusRequest;
import com.yiling.cms.content.dto.request.QueryHMCContentPageRequest;
import com.yiling.cms.content.entity.SaContentDO;
import com.yiling.cms.content.enums.CmsErrorCode;
import com.yiling.cms.content.service.SaContentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

/**
 * <p>
 * 销售助手内容表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-06-25
 */
@Service
public class SaContentServiceImpl extends BaseServiceImpl<SaContentMapper, SaContentDO> implements SaContentService {

    @Override
    public boolean addContent(AddHmcContentRequest request) {
        List<SaContentDO> saContentDOList = request.getList().stream().map(e -> {
            SaContentDO saContentDO = PojoUtils.map(e, SaContentDO.class);
            saContentDO.setOpUserId(request.getOpUserId());
            saContentDO.setReferStatus(1);
            return saContentDO;
        }).collect(Collectors.toList());
        return this.saveBatch(saContentDOList);
    }

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        SaContentDO contentDO = new SaContentDO();
        if (Objects.nonNull(request.getCategoryRank())) {
            contentDO.setCategoryRank(request.getCategoryRank());
        }
        if (Objects.nonNull(request.getTopFlag())) {
            contentDO.setTopFlag(request.getTopFlag());
        }
        contentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<SaContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), SaContentDO::getId, request.getId());
        return this.update(contentDO, wrapper);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        SaContentDO contentDO = new SaContentDO();
        contentDO.setReferStatus(request.getReferStatus());
        contentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<SaContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), SaContentDO::getId, request.getId());
        return this.update(contentDO, wrapper);
    }

    @Override
    public Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request) {
        return this.getBaseMapper().listPage(request.getPage(), request);
    }

    @Override
    public void updateView(Long id) {
        QueryWrapper<SaContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SaContentDO::getId, id);
        wrapper.last(" limit 1");
        SaContentDO saContentDO = this.getOne(wrapper);
        if (Objects.isNull(saContentDO)) {
            throw new BusinessException(CmsErrorCode.CONTENT_NOT_FOUND);
        }
        saContentDO.setView(saContentDO.getView() + 1);
        this.updateById(saContentDO);
    }

    @Override
    public List<SaContentDO> listByContentIdList(List<Long> contentIdList) {
        QueryWrapper<SaContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(SaContentDO::getContentId, contentIdList);
        return this.list(wrapper);
    }
}
