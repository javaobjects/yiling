package com.yiling.basic.contract.service;

import com.yiling.basic.contract.dto.ContractCreateDTO;
import com.yiling.basic.contract.dto.request.CallBackRequest;

/**
 * @author fucheng.bai
 * @date 2022/11/17
 */
public interface QysContractService {

    /**
     * 创建合同
     */
    Long createByCategory(ContractCreateDTO contractCreateDTO);

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
     * 撤回合同
     * @param contractId 合同id
     * @param sealId 发起方的印章ID，用于签署作废声明
     * @param reason 作废原因或说明
     * @param removeContract 作废成功后是否删除合同正文文档，默认为false
     */
    void cancelContract(Long contractId, Long sealId, String reason, Boolean removeContract);

    /**
     * 删除合同
     * @param contractId
     */
    void deleteContract(Long contractId);

    /**
     * 下载合同
     * @param contractId
     */
    String downloadContract(Long contractId);

    /**
     * 合同回调
     * @param callBackRequest
     */
    void completeCallBack(CallBackRequest callBackRequest);

    /**
     * 获取合同状态
     * @param contractId
     * @return
     */
    String getContractStatus(Long contractId);
}
