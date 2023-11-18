package com.yiling.cms.content.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.HMCContentDTO;
import com.yiling.cms.content.dto.request.*;
import com.yiling.cms.content.entity.ContentDO;
import com.yiling.cms.content.entity.ContentDisplayLineDO;
import com.yiling.cms.content.entity.HmcContentDO;
import com.yiling.cms.content.dao.HmcContentMapper;
import com.yiling.cms.content.enums.CmsErrorCode;
import com.yiling.cms.content.service.HmcContentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * HMC端内容表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-07
 */
@Slf4j
@Service
public class HmcContentServiceImpl extends BaseServiceImpl<HmcContentMapper, HmcContentDO> implements HmcContentService {

    @Autowired
    private HmcContentService hmcContentService;

    @Override
    public Boolean addContent(AddHmcContentRequest addContentRequest) {
        List<HmcContentDO> hmcContentDOList = addContentRequest.getList().stream().map(item -> {
            HmcContentDO contentDO = PojoUtils.map(item, HmcContentDO.class);
            contentDO.setCreateUser(addContentRequest.getOpUserId());
            contentDO.setUpdateUser(addContentRequest.getOpUserId());
            contentDO.setReferStatus(1);
            return contentDO;
        }).collect(Collectors.toList());
        return this.saveBatch(hmcContentDOList);
    }

    @Override
    public Boolean contentRank(ContentRankRequest request) {
        HmcContentDO hmcContentDO = new HmcContentDO();
        if (Objects.nonNull(request.getCategoryRank())) {
            hmcContentDO.setCategoryRank(request.getCategoryRank());
        }
        if (Objects.nonNull(request.getTopFlag())) {
            hmcContentDO.setTopFlag(request.getTopFlag());
        }
        hmcContentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<HmcContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), HmcContentDO::getId, request.getId());
        return this.update(hmcContentDO, wrapper);
    }

    @Override
    public Page<HMCContentDTO> listPage(QueryHMCContentPageRequest request) {
        return this.getBaseMapper().listPage(request.getPage(), request);
    }

    @Override
    public Page<AppContentDTO> listAppContentPageBySql(QueryAppContentPageRequest request) {
        Page<AppContentDTO> page = this.getBaseMapper().listAppContentPageBySql(request.getPage(), request);
        List<Long> contentIdList = page.getRecords().stream().map(AppContentDTO::getContentId).collect(Collectors.toList());
        if(CollUtil.isEmpty(contentIdList)) {
            return request.getPage();
        }

        List<HmcContentDO> hmcContentDOList = hmcContentService.listByContentIdList(contentIdList);
        Map<Long, List<HmcContentDO>> hmcContentDOMap = hmcContentDOList.stream().collect(Collectors.groupingBy(HmcContentDO::getContentId));

        Page<AppContentDTO> result = PojoUtils.map(page, AppContentDTO.class);
        result.getRecords().forEach(contentDTO -> {
            if (hmcContentDOMap.containsKey(contentDTO.getContentId())) {
                contentDTO.setHmcView(hmcContentDOMap.get(contentDTO.getContentId()).stream().map(HmcContentDO::getView).reduce(Integer::sum).get());
            }
        });
        return result;
    }

    @Override
    public List<HmcContentDO> listByContentIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return Lists.newArrayList();
        }
        QueryWrapper<HmcContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HmcContentDO::getContentId, idList);
        return this.list(wrapper);
    }

    @Override
    public void updateView(Long id) {
        QueryWrapper<HmcContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HmcContentDO::getId, id);
        wrapper.last(" limit 1");
        HmcContentDO hmcContentDO = this.getOne(wrapper);
        if (Objects.isNull(hmcContentDO)) {
            log.warn("根据id未获取到文章，id:{}", id);
            throw new BusinessException(CmsErrorCode.CONTENT_NOT_FOUND);
        }
        hmcContentDO.setView(hmcContentDO.getView() + 1);
        this.updateById(hmcContentDO);
    }

    @Override
    public Boolean updateReferStatus(ContentReferStatusRequest request) {
        HmcContentDO hmcContentDO = new HmcContentDO();
        hmcContentDO.setReferStatus(request.getReferStatus());
        hmcContentDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<HmcContentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), HmcContentDO::getId, request.getId());
        return this.update(hmcContentDO, wrapper);
    }
}
