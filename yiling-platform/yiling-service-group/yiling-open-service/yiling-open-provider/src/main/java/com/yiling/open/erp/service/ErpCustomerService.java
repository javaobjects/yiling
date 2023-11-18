package com.yiling.open.erp.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.dto.request.SaveEnterpriseCustomerRequest;

/**
 * erp客户同步
 * @author: houjie.sun
 * @date: 2021/8/30
 */
public interface ErpCustomerService {

    /**
     * 上线引擎
     * @param baseErpEntity
     * @return
     */
     boolean onlineData(BaseErpEntity baseErpEntity);

    /**
     * 客户调度
     */
    void syncCustomer();

    /**
     * 手动匹配
     * @param request
     * @return
     */
    Boolean relationCustomer(SaveEnterpriseCustomerRequest request);

    /**
     * 手动维护
     * @param request
     * @return
     */
    Boolean maintain(SaveEnterpriseCustomerRequest request);

    /**
     * 天眼查手动匹配
     * @param request
     * @return
     */
    Boolean relationCustomerByTyc(SaveEnterpriseCustomerRequest request);

    /**
     * 根据同步状态分页查询Erp客户列表
     * @param request
     * @return
     */
    Page<ErpCustomerDTO> QueryErpCustomerPageBySyncStatus(ErpCustomerQueryRequest request);

    /**
     * 根据同步状态查询Erp客户列表
     * @param request
     * @return
     */
    List<ErpCustomerDTO> getErpCustomerListBySyncStatus(ErpCustomerQueryRequest request);

    ErpCustomerDTO findById(Long id);

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
