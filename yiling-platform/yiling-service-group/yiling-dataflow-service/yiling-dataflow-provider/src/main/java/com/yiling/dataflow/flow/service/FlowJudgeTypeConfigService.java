package com.yiling.dataflow.flow.service;

import java.util.List;

import com.yiling.dataflow.flow.dto.FlowJudgeTypeConfigDTO;
import com.yiling.dataflow.flow.entity.FlowJudgeTypeConfigDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-01-12
 */
public interface FlowJudgeTypeConfigService extends BaseService<FlowJudgeTypeConfigDO> {

    List<FlowJudgeTypeConfigDTO> getAllFlowJudgeTypeConfig();
}
