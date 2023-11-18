package com.yiling.dataflow.relation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.relation.dto.FlowSupplierMappingDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowSupplierMappingRequest;
import com.yiling.dataflow.relation.entity.FlowSupplierMappingDO;
import com.yiling.dataflow.relation.dao.FlowSupplierMappingMapper;
import com.yiling.dataflow.relation.service.FlowSupplierMappingService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-08-10
 */
@Service
@CacheConfig(cacheNames = "dataflow:flowSupplierMappingService")
public class FlowSupplierMappingServiceImpl extends BaseServiceImpl<FlowSupplierMappingMapper, FlowSupplierMappingDO> implements FlowSupplierMappingService {

    @Override
    @Cacheable(key="#enterpriseName+'+getByEnterpriseName'")
    public FlowSupplierMappingDO getByEnterpriseName(String enterpriseName) {
        QueryWrapper<FlowSupplierMappingDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowSupplierMappingDO::getFlowEnterpriseName, enterpriseName);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveFlowSupplierMapping(SaveFlowSupplierMappingRequest request) {
        return this.save(PojoUtils.map(request,FlowSupplierMappingDO.class));
    }
}
