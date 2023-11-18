package com.yiling.open.erp.api.impl;

import java.util.Date;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ClientToolVersionApi;
import com.yiling.open.erp.dto.ClientToolVersionDTO;
import com.yiling.open.erp.dto.request.ClientToolVersionQueryRequest;
import com.yiling.open.erp.dto.request.ClientToolVersionSaveRequest;
import com.yiling.open.erp.entity.ClientToolVersionDO;
import com.yiling.open.erp.service.ClientToolVersionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ClientToolVersionApiImpl implements ClientToolVersionApi {

    @Autowired
    private ClientToolVersionService clientToolVersionService;

    @Override
    public Boolean save(ClientToolVersionSaveRequest request) {
        try {
            ClientToolVersionDO clientToolVersionDO = PojoUtils.map(request, ClientToolVersionDO.class);

            Integer id = request.getId();
            if (id != null && id.intValue() != 0) {
                clientToolVersionDO.setUpdateTime(new Date());
            } else {
                clientToolVersionDO.setCreateTime(new Date());
            }

            return clientToolVersionService.saveOrUpdate(clientToolVersionDO);
        } catch (Exception e) {
            log.error("[ClientToolVersionApiImpl][save] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }


    @Override
    public Page<ClientToolVersionDTO> getAppVersions(ClientToolVersionQueryRequest request) {
        try {
            return clientToolVersionService.getAppVersions(request);
        } catch (Exception e) {
            log.error("[ClientToolVersionApiImpl][getAppVersions] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public ClientToolVersionDTO getAppVersionByLast() {
        try {
            return clientToolVersionService.getAppVersionByLast();
        } catch (Exception e) {
            log.error("[ClientToolVersionApiImpl][getAppVersionByLast] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }
}
