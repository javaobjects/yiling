package com.yiling.open.erp.service.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.dao.ErpTaskInterfaceMapper;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.service.ErpTaskInterfaceService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/6/9
 */
@Slf4j
@CacheConfig(cacheNames = "open:erpTaskInterfaceService")
@Service(value = "erpTaskInterfaceService")
public class ErpTaskInterfaceServiceImpl implements ErpTaskInterfaceService {

    @Resource
    private ErpTaskInterfaceMapper erpTaskInterfaceMapper;

    @Override
    @Cacheable(key="#taskNo+'+findErpTaskInterfaceByTaskNo'")
    public ErpTaskInterfaceDTO findErpTaskInterfaceByTaskNo(String taskNo) {
        return PojoUtils.map(erpTaskInterfaceMapper.findErpTaskInterfaceByTaskNo(taskNo), ErpTaskInterfaceDTO.class);
    }
}
