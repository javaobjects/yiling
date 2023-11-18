package com.yiling.open.erp.api.impl;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpTaskInterfaceApi;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.service.ErpTaskInterfaceService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ErpTaskInterfaceApiImpl implements ErpTaskInterfaceApi {

    @Resource
    private ErpTaskInterfaceService erpTaskInterfaceService;

    @Override
    public ErpTaskInterfaceDTO findErpTaskInterfaceByTaskNo(String taskNo) {
        return erpTaskInterfaceService.findErpTaskInterfaceByTaskNo(taskNo);
    }
}
