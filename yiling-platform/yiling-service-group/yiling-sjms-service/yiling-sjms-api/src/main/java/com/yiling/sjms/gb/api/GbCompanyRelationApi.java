package com.yiling.sjms.gb.api;

import java.util.List;

import com.yiling.sjms.gb.dto.GbCompanyRelationDTO;

import cn.hutool.core.collection.ListUtil;

/**
 * 团购出库终端和商业关系
 *
 * @author: wei.wang
 * @date: 2023/03/07
 */
public interface GbCompanyRelationApi {

    /**
     * 根据FormId查询
     * @param formId
     * @return
     */
    List<GbCompanyRelationDTO> listByFormId(Long formId);

    /**
     * 根据FormIds查询
     * @param formIds
     * @return
     */
    List<GbCompanyRelationDTO> listByFormIds(List<Long> formIds);
}
