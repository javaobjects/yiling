package com.yiling.hmc.settlement.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.pojo.Result;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageResultBO;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementCallbackRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementRequest;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDO;

/**
 * <p>
 * 保司结账表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
public interface InsuranceSettlementService extends BaseService<InsuranceSettlementDO> {

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
