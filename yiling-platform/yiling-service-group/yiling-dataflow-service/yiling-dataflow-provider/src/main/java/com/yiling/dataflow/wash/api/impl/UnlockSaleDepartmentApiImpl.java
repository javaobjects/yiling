package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.yiling.dataflow.wash.api.UnlockSaleDepartmentApi;
import com.yiling.dataflow.wash.dao.UnlockSaleDepartmentMapper;
import com.yiling.dataflow.wash.dto.UnlockSaleDepartmentDTO;
import com.yiling.dataflow.wash.entity.UnlockSaleDepartmentDO;
import com.yiling.dataflow.wash.service.UnlockSaleDepartmentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@DubboService
public class UnlockSaleDepartmentApiImpl implements UnlockSaleDepartmentApi {

    @Autowired
    private UnlockSaleDepartmentService unlockSaleDepartmentService;

    @Override
    public UnlockSaleDepartmentDTO getUnlockSaleDepartmentByRuleId(long ruleId) {
        return PojoUtils.map(unlockSaleDepartmentService.getUnlockSaleDepartmentByRuleId(ruleId),UnlockSaleDepartmentDTO.class);
    }

    @Override
    public List<UnlockSaleDepartmentDTO> getUnlockSaleDepartmentByRuleIds(List<Long> ruleIds) {
        return PojoUtils.map(unlockSaleDepartmentService.getUnlockSaleDepartmentByRuleIds(ruleIds),UnlockSaleDepartmentDTO.class);
    }
}
