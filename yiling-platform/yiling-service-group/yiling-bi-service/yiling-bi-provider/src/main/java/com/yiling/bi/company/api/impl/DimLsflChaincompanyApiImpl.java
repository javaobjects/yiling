package com.yiling.bi.company.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.company.api.DimLsflChaincompanyApi;
import com.yiling.bi.company.dto.DimLsflChaincompanyDTO;
import com.yiling.bi.company.service.DimLsflChaincompanyService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/2/10
 */
@DubboService
@Slf4j
public class DimLsflChaincompanyApiImpl implements DimLsflChaincompanyApi {

    @Autowired
    private DimLsflChaincompanyService dimLsflChaincompanyService;

    @Override
    public List<DimLsflChaincompanyDTO> getByDbCodeAndChainCode(String dbCode, String chainCode) {
        return PojoUtils.map(dimLsflChaincompanyService.getByDbCodeAndChainCode(dbCode, chainCode), DimLsflChaincompanyDTO.class);
    }

    @Override
    public List<DimLsflChaincompanyDTO> getByChainCode(String chainCode) {
        return PojoUtils.map(dimLsflChaincompanyService.getByChainCode(chainCode), DimLsflChaincompanyDTO.class);
    }
}
