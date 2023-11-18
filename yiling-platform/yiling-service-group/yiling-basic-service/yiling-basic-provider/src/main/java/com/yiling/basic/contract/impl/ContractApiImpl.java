package com.yiling.basic.contract.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.contract.api.ContractApi;
import com.yiling.basic.contract.dto.ContractCreateDTO;
import com.yiling.basic.contract.dto.request.CallBackRequest;
import com.yiling.basic.contract.dto.request.ContractCancelRequest;
import com.yiling.basic.contract.dto.request.ContractCreateRequest;
import com.yiling.basic.contract.service.QysContractService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/11/16
 */
@DubboService
@Slf4j
public class ContractApiImpl implements ContractApi {

    @Autowired
    private QysContractService qysContractService;

    @Override
    public Long createByCategory(ContractCreateRequest contractCreateRequest) {
        return qysContractService.createByCategory(PojoUtils.map(contractCreateRequest, ContractCreateDTO.class));
    }

    @Override
    public String viewUrl(Long contractId) {
        return qysContractService.viewUrl(contractId);
    }

    @Override
    public void sendContract(Long contractId) {
        qysContractService.sendContract(contractId);
    }

    @Override
    public void recallContract(Long contractId, String reason) {
        qysContractService.recallContract(contractId, reason);
    }

    @Override
    public void cancelContract(ContractCancelRequest contractCancelRequest) {
        Long contractId = contractCancelRequest.getContractId();
        Long sealId = contractCancelRequest.getSealId();
        String reason = contractCancelRequest.getReason();
        Boolean removeContract = contractCancelRequest.getRemoveContract();
        qysContractService.cancelContract(contractId, sealId, reason, removeContract);
    }

    @Override
    public void deleteContract(Long contractId) {
        qysContractService.deleteContract(contractId);
    }

    @Override
    public void completeCallBack(CallBackRequest callBackRequest) {
        qysContractService.completeCallBack(callBackRequest);
    }

    @Override
    public String downloadContract(Long contractId) {
        return qysContractService.downloadContract(contractId);
    }

    @Override
    public String getContractStatus(Long contractId) {
        return qysContractService.getContractStatus(contractId);
    }
}
