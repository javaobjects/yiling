package com.yiling.dataflow.spda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.spda.dto.SpdaEnterpriseDataDTO;
import com.yiling.dataflow.spda.entity.SpdaEnterpriseDataDO;
import com.yiling.dataflow.spda.dao.SpdaEnterpriseDataMapper;
import com.yiling.dataflow.spda.service.SpdaEnterpriseDataService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 药监局企业数据 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-01-06
 */
@Service
@CacheConfig(cacheNames = "dataflow:SpdaEnterpriseData")
public class SpdaEnterpriseDataServiceImpl extends BaseServiceImpl<SpdaEnterpriseDataMapper, SpdaEnterpriseDataDO> implements SpdaEnterpriseDataService {

    @Override
    @Cacheable(key = "#name+'+getSpdaEnterpriseDataByName'")
    public SpdaEnterpriseDataDTO getSpdaEnterpriseDataByName(String name) {
        QueryWrapper<SpdaEnterpriseDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SpdaEnterpriseDataDO::getName, name);
        queryWrapper.last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),SpdaEnterpriseDataDTO.class);
    }
}
