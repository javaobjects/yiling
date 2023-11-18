package com.yiling.open.erp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.open.erp.dto.ClientToolVersionDTO;
import com.yiling.open.erp.dto.request.ClientToolVersionQueryRequest;
import com.yiling.open.erp.entity.ClientToolVersionDO;

/**
 * @author shuan
 */
public interface ClientToolVersionService extends BaseService<ClientToolVersionDO> {

    Page<ClientToolVersionDTO> getAppVersions(ClientToolVersionQueryRequest request);

    ClientToolVersionDTO getAppVersionByLast();
}
