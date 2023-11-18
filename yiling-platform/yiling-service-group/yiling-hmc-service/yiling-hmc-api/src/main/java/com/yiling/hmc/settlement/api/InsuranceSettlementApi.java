package com.yiling.hmc.settlement.api;

import java.util.List;

import com.yiling.framework.common.pojo.Result;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageResultBO;
import com.yiling.hmc.settlement.dto.InsuranceSettlementDTO;
import com.yiling.hmc.settlement.dto.InsuranceSettlementDetailDTO;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementCallbackRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementRequest;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;

/**
 * 保司结算Api
 *
 * @author: yong.zhang
 * @date: 2022/3/31
 */
public interface InsuranceSettlementApi {

    /**
     * 根据id查询保司结账信息
     *
     * @param id id
     * @return 保司结账信息
     */
    InsuranceSettlementDTO queryById(Long id);

    /**
     * 保司结账单分页查询
     *
     * @param request 查询条件
     * @return 保司结账表信息
     */
    InsuranceSettlementPageResultBO pageList(InsuranceSettlementPageRequest request);

    /**
     * 保司导入结算数据
     *
     * @param request 结算导入信息
     * @return 成功/失败
     */
    boolean importSettlementData(InsuranceSettlementRequest request);

    /**
     * 保司结算回调以岭接口
     *
     * @param request 回调数据
     * @return 成功/失败
     */
    boolean callback(InsuranceSettlementCallbackRequest request);

    /**
     * 根据保司结账主id查询保司结账明细
     *
     * @param insuranceSettlementId 保司结账主表id
     * @return 保司结账明细
     */
    List<InsuranceSettlementDetailDTO> listByInsuranceSettlementId(Long insuranceSettlementId);

    /**
     * 同步订单到保司（泰康）
     *
     * @param request 请求参数
     * @return 返回
     */
    boolean syncOrder(SyncOrderRequest request);

    /**
     * 同步订单到保司（泰康）结果获取
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    Result<Object> getSyncOrderResult(SyncOrderRequest request);

}
