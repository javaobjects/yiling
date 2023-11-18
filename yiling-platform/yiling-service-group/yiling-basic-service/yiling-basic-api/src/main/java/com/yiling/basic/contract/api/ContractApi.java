package com.yiling.basic.contract.api;

import com.yiling.basic.contract.dto.request.CallBackRequest;
import com.yiling.basic.contract.dto.request.ContractCancelRequest;
import com.yiling.basic.contract.dto.request.ContractCreateRequest;

/**
 * @author fucheng.bai
 * @date 2022/11/16
 */
public interface ContractApi {

    /**
     * 创建合同
     * @param contractCreateRequest
     */
    Long createByCategory(ContractCreateRequest contractCreateRequest);

    /**
     * 查看合同
     * @param contractId
     * @return
     */
    String viewUrl(Long contractId);

    /**
     * 发起合同
     * @param contractId
     */
    void sendContract(Long contractId);


    /**
     * 撤回合同
     * @param contractId
     */
    void recallContract(Long contractId, String reason);

    /**
     * 作废合同
     * @param contractId 合同id
     * @param sealId 发起方的印章ID，用于签署作废声明
     * @param reason 作废原因或说明
     * @param removeContract 作废成功后是否删除合同正文文档，默认为false
     */
    void cancelContract(ContractCancelRequest contractCancelRequest);

    /**
     * 删除合同
     * @param contractId
     */
    void deleteContract(Long contractId);


    void completeCallBack(CallBackRequest callBackRequest);


    /**
     * 下载合同
     * @param contractId
     * @return
     */
    String downloadContract(Long contractId);

    /**
     * 获取合同状态
     * @param contractId
     * @return
     */
    String getContractStatus(Long contractId);
}
