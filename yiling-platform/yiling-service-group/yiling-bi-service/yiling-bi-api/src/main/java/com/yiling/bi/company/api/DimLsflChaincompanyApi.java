package com.yiling.bi.company.api;

import java.util.List;

import com.yiling.bi.company.dto.DimLsflChaincompanyDTO;

/**
 * @author fucheng.bai
 * @date 2023/2/10
 */
public interface DimLsflChaincompanyApi {

    List<DimLsflChaincompanyDTO> getByDbCodeAndChainCode(String dbCode, String chainCode);

    List<DimLsflChaincompanyDTO> getByChainCode(String chainCode);
}
