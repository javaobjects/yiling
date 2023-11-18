package com.yiling.bi.resource.service;


import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.entity.UploadResourceDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
public interface UploadResourceService extends BaseService<UploadResourceDO> {

    UploadResourceDTO getUploadResourceByDataId(String dataId);

    Integer deleteUploadResourceById(Long id);

}
