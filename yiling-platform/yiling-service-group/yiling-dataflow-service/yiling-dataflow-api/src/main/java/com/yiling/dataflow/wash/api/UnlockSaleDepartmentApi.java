package com.yiling.dataflow.wash.api;

import java.util.List;

import com.yiling.dataflow.wash.dto.UnlockSaleDepartmentDTO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleDepartmentApi {
    UnlockSaleDepartmentDTO getUnlockSaleDepartmentByRuleId(long ruleId);
    List<UnlockSaleDepartmentDTO> getUnlockSaleDepartmentByRuleIds(List<Long> ruleIds);
}
