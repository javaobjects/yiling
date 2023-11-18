package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.dto.UnlockSaleDepartmentDTO;
import com.yiling.dataflow.wash.entity.UnlockSaleDepartmentDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleDepartmentService extends BaseService<UnlockSaleDepartmentDO> {
    UnlockSaleDepartmentDO getUnlockSaleDepartmentByRuleId(long ruleId);
    List<UnlockSaleDepartmentDO> getUnlockSaleDepartmentByRuleIds(List<Long> ruleIds);
}
