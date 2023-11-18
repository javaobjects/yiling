package com.yiling.sjms.wash.service.impl;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.wash.api.UnlockFlowWashSaleApi;
import com.yiling.dataflow.wash.dto.UnlockFlowWashSaleDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleDepartmentDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.dto.request.UpdateUnlockFlowWashSaleDistributionRequest;
import com.yiling.sjms.wash.enums.SjmsUnlockWashErrorEnum;
import com.yiling.sjms.wash.handler.UnFlowWashHandler;
import com.yiling.sjms.wash.service.SjmsUnlockSaleRuleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
@Service
public class SjmsUnlockSaleRuleServiceImpl implements SjmsUnlockSaleRuleService {

    @Autowired
    private UnFlowWashHandler     unFlowWashHandler;
    @DubboReference
    private UnlockFlowWashSaleApi unlockFlowWashSaleApi;

    @Override
    public boolean updateUnlockSaleRuleDistribution(UpdateUnlockFlowWashSaleDistributionRequest request) {
        List<UnlockFlowWashSaleDTO> list = unlockFlowWashSaleApi.getByIds(request.getIds());
        for (UnlockFlowWashSaleDTO unlockFlowWashSaleDTO : list) {
            //先清空计入数据
            unlockFlowWashSaleDTO.setOperateStatus(1);
            unlockFlowWashSaleDTO.setUnlockSaleRuleId(0L);
            unlockFlowWashSaleDTO.setRuleNotes("");
            unlockFlowWashSaleDTO.setJudgment(0);
            unlockFlowWashSaleDTO.setDistributionStatus(1);
            unlockFlowWashSaleDTO.setDistributionSource(0);
            unlockFlowWashSaleDTO.setDepartment("");
            unlockFlowWashSaleDTO.setBusinessDepartment("");
            unlockFlowWashSaleDTO.setProvincialArea("");
            unlockFlowWashSaleDTO.setBusinessProvince("");
            unlockFlowWashSaleDTO.setRepresentativeCode("");
            unlockFlowWashSaleDTO.setRepresentativeName("");
            unlockFlowWashSaleDTO.setSuperiorSupervisorCode("");
            unlockFlowWashSaleDTO.setSuperiorSupervisorName("");
            unlockFlowWashSaleDTO.setDistrictCounty("");
            unlockFlowWashSaleDTO.setDistrictCountyCode("");
            unlockFlowWashSaleDTO.setPostCode(0L);
            unlockFlowWashSaleDTO.setPostName("");
            unlockFlowWashSaleDTO.setRemark("");

            UnlockSaleRuleDTO unlockSaleRuleDTO = new UnlockSaleRuleDTO();
            unlockSaleRuleDTO.setSource(1);
            unlockSaleRuleDTO.setJudgment(request.getJudgment());
            unlockSaleRuleDTO.setNotes(request.getNotes());
            unlockSaleRuleDTO.setSaleRange(request.getSaleRange());
            UnlockSaleDepartmentDTO unlockSaleDepartmentDTO = PojoUtils.map(request.getSaveUnlockSaleDepartmentRequest(),UnlockSaleDepartmentDTO.class);
            unFlowWashHandler.fillUnlockFlowWashSale(unlockSaleRuleDTO, unlockSaleDepartmentDTO, unlockFlowWashSaleDTO);
            unlockFlowWashSaleDTO.setDistributionSource(2);
            unlockFlowWashSaleDTO.setDistributionStatus(2);
            unlockFlowWashSaleDTO.setUpdateTime(new Date());
            unlockFlowWashSaleDTO.setUpdateUser(request.getOpUserId());
        }
        unFlowWashHandler.updateUnlockFlowWashSale(list);
        return false;
    }
}
