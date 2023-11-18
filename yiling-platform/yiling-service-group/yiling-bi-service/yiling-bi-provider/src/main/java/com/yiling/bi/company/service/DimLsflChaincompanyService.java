package com.yiling.bi.company.service;

import java.util.List;

import com.yiling.bi.company.entity.DimLsflChaincompanyDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-30
 */
public interface DimLsflChaincompanyService extends BaseService<DimLsflChaincompanyDO> {

    List<DimLsflChaincompanyDO> getByDbCodeAndChainCode(String dbCode, String chainCode);

    List<DimLsflChaincompanyDO> getByChainCode(String chainCode);
}
