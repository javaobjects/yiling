package com.yiling.dataflow.flow.service.impl;

import java.util.List;

import com.yiling.dataflow.flow.dao.FlowJudgeTypeConfigMapper;
import com.yiling.dataflow.flow.dto.FlowJudgeTypeConfigDTO;
import com.yiling.dataflow.flow.entity.FlowJudgeTypeConfigDO;
import com.yiling.dataflow.flow.service.FlowJudgeTypeConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-01-12
 */
@Service
@CacheConfig(cacheNames = "dataflow:FlowJudgeTypeConfig")
public class FlowJudgeTypeConfigServiceImpl extends BaseServiceImpl<FlowJudgeTypeConfigMapper, FlowJudgeTypeConfigDO> implements FlowJudgeTypeConfigService {

    @Override
    @Cacheable(key = "#name+'+getAllFlowJudgeTypeConfig'")
    public List<FlowJudgeTypeConfigDTO> getAllFlowJudgeTypeConfig() {
        List<FlowJudgeTypeConfigDTO> flowJudgeTypeConfigDTOList = PojoUtils.map(this.list(), FlowJudgeTypeConfigDTO.class);
        return flowJudgeTypeConfigDTOList;
    }
}
