package com.yiling.open.erp.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.dto.request.SaveEnterpriseCustomerRequest;

/**
 * 客户信息同步
 * @author shuan
 */
public interface ErpCustomerApi {

    /**
     * 同步商品信息
     */
    void syncCustomer();

    /**
     * 绑定客户逻辑
     */
    Boolean relationCustomer(SaveEnterpriseCustomerRequest request);

    /**
     * 天眼查绑定客户逻辑
     */
    Boolean relationCustomerByTyc(SaveEnterpriseCustomerRequest request);

    /**
     * 手动维护
     * @param request
     * @return
     */
    Boolean maintain(SaveEnterpriseCustomerRequest request);

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

    /**
     * id查询
     * @param id
     * @return
     */
    ErpCustomerDTO findById(Long id);

    /**
     * 根据suid软删除
     * @param suId
     * @return
     */
    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
