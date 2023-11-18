package com.yiling.cms.content.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.QaDTO;
import com.yiling.cms.content.dto.request.QueryQAPageRequest;
import com.yiling.cms.content.dto.request.SwitchQAStatusRequest;
import com.yiling.cms.content.entity.QaDO;
import com.yiling.cms.content.dao.QaMapper;
import com.yiling.cms.content.enums.QaShowStatusEnum;
import com.yiling.cms.content.service.QaService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 问答表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Service
public class QaServiceImpl extends BaseServiceImpl<QaMapper, QaDO> implements QaService {


    @Override
    public Integer getQaCountByContentId(Long contentId) {
        return this.baseMapper.getQaCount(contentId);
    }

    @Override
    public List<QaDTO> getQaListByContentId(Long contentId) {
        QueryWrapper<QaDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(QaDO::getContentId, contentId);
        wrapper.lambda().eq(QaDO::getShowStatus, QaShowStatusEnum.show.getCode());
        wrapper.lambda().orderByDesc(QaDO::getCreateTime);
        List<QaDO> list = this.list(wrapper);
        return PojoUtils.map(list, QaDTO.class);
    }

    @Override
    public Page<QaDTO> listPage(QueryQAPageRequest request) {
        Page<QaDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<QaDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(request.getId()), QaDO::getId, request.getId());
        wrapper.lambda().eq(Objects.nonNull(request.getQaId()), QaDO::getQaId, request.getQaId());
        wrapper.lambda().eq(Objects.nonNull(request.getUserType()), QaDO::getQaType, request.getUserType());
        wrapper.lambda().in(CollUtil.isNotEmpty(request.getContentIdList()), QaDO::getContentId, request.getContentIdList());
        if(Objects.nonNull(request.getBeginDate())) {
            wrapper.lambda().ge(Objects.nonNull(request.getBeginDate()), QaDO::getCreateTime, DateUtil.beginOfDay(request.getBeginDate()));
        }
        if (Objects.nonNull(request.getEndDate())) {
            wrapper.lambda().le(Objects.nonNull(request.getEndDate()), QaDO::getCreateTime, DateUtil.endOfDay(request.getEndDate()));
        }
        wrapper.lambda().eq(Objects.nonNull(request.getLineId()), QaDO::getLineId, request.getLineId());
        wrapper.lambda().orderByDesc(QaDO::getCreateTime);
        Page<QaDO> result = this.page(page, wrapper);
        return PojoUtils.map(result, QaDTO.class);
    }

    @Override
    public Boolean switchShowStatus(SwitchQAStatusRequest request) {
        QaDO qaDO = PojoUtils.map(request, QaDO.class);
        return this.updateById(qaDO);
    }
}
