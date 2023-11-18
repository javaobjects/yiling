package com.yiling.open.erp.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.dto.ClientToolVersionDTO;
import com.yiling.open.erp.dto.request.ClientToolVersionQueryRequest;
import com.yiling.open.erp.dto.request.ClientToolVersionSaveRequest;

/**
 * 工具版本管理
 * @author shuan
 */
public interface ClientToolVersionApi {

    /**
     * 保存
     * @param request
     * @return
     */
    Boolean save(ClientToolVersionSaveRequest request);

    /**
     * 获取工具版本信息
     * @param request
     * @return
     */
    Page<ClientToolVersionDTO> getAppVersions(ClientToolVersionQueryRequest request);

    /**
     * 获取最后一次版本信息
     * @return
     */
    ClientToolVersionDTO getAppVersionByLast();
}
