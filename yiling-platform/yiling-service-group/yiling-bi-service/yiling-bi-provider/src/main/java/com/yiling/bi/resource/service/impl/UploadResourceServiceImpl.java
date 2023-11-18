package com.yiling.bi.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.bi.resource.dao.UploadResourceMapper;
import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.entity.UploadResourceDO;
import com.yiling.bi.resource.entity.UploadResourceLogDO;
import com.yiling.bi.resource.service.UploadResourceService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
@Slf4j
@Service
public class UploadResourceServiceImpl extends BaseServiceImpl<UploadResourceMapper, UploadResourceDO> implements UploadResourceService {

    @Override
    public UploadResourceDTO getUploadResourceByDataId(String dataId) {
        QueryWrapper<UploadResourceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UploadResourceDO::getDataId, dataId);
        return PojoUtils.map(this.baseMapper.selectOne(queryWrapper),UploadResourceDTO.class);
    }

    @Override
    public Integer deleteUploadResourceById(Long id) {
        return this.baseMapper.deleteUploadResourceById(id);
    }
}
