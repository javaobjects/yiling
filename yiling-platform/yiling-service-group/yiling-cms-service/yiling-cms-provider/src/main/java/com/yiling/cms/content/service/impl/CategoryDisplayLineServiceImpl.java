package com.yiling.cms.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.content.dto.CategoryDisplayLineDTO;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import com.yiling.cms.content.dao.CategoryDisplayLineMapper;
import com.yiling.cms.content.service.CategoryDisplayLineService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.cms.content.entity.CategoryDisplayLineDO;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 栏目引用业务线 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Service
public class CategoryDisplayLineServiceImpl extends BaseServiceImpl<CategoryDisplayLineMapper, CategoryDisplayLineDO> implements CategoryDisplayLineService {

    @Override
    public List<CategoryDisplayLineDTO> getCategoryByLineIdAndModuleId(Long lineId, Long moduleId) {
        QueryWrapper<CategoryDisplayLineDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(lineId), CategoryDisplayLineDO::getLineId, lineId);
        wrapper.lambda().eq(Objects.nonNull(moduleId), CategoryDisplayLineDO::getModuleId, moduleId);
        List<CategoryDisplayLineDO> list = this.list(wrapper);
        return PojoUtils.map(list, CategoryDisplayLineDTO.class);
    }

    @Override
    public List<CategoryDisplayLineDTO> getCategoryByLineId(Long lineId) {
        QueryWrapper<CategoryDisplayLineDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Objects.nonNull(lineId), CategoryDisplayLineDO::getLineId, lineId);
        List<CategoryDisplayLineDO> list = this.list(wrapper);
        return PojoUtils.map(list, CategoryDisplayLineDTO.class);
    }

    @Override
    public List<CategoryDisplayLineDTO> getCategoryList() {
        List<CategoryDisplayLineDO> list = this.list();
        return PojoUtils.map(list, CategoryDisplayLineDTO.class);
    }
}
