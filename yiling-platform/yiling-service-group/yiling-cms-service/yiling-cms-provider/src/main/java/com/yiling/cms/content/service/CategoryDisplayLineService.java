package com.yiling.cms.content.service;

import com.yiling.cms.content.dto.CategoryDisplayLineDTO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.cms.content.entity.CategoryDisplayLineDO;

import java.util.List;

/**
 * <p>
 * 栏目引用业务线 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
public interface CategoryDisplayLineService extends BaseService<CategoryDisplayLineDO> {

    /**
     * 获取栏目
     * @param lineId
     * @param moduleId
     * @return
     */
    List<CategoryDisplayLineDTO> getCategoryByLineIdAndModuleId(Long lineId, Long moduleId);

    /**
     * 获取业务线下信息
     * @param lineId
     * @return
     */
    List<CategoryDisplayLineDTO> getCategoryByLineId(Long lineId);

    /**
     * 获取栏目信息
     * @return
     */
    List<CategoryDisplayLineDTO> getCategoryList();
}
