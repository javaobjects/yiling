package com.yiling.sjms.gb.service;

import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.dto.GbCompanyRelationDTO;
import com.yiling.sjms.gb.entity.GbCompanyRelationDO;


/**
 * 团购出库终端和商业关系
 *
 * @author: wei.wang
 * @date: 2023/03/07
 */
public interface GbCompanyRelationService extends BaseService<GbCompanyRelationDO> {

    /**
     * 根据FormId查询
     * @param formId
     * @return
     */
    List<GbCompanyRelationDO> listByFormId(Long formId);

    /**
     * 根据FormId删除
     * @param formId
     * @return
     */
    Boolean deleteByFormId(Long formId);

    /**
     * 根据FormIds查询
     * @param formIds
     * @return
     */
    List<GbCompanyRelationDO> listByFormIds(List<Long> formIds);
}
