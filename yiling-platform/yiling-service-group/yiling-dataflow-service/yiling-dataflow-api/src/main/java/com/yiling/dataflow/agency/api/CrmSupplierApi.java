package com.yiling.dataflow.agency.api;

import java.util.List;

import com.yiling.dataflow.agency.dto.CrmEnterpriseNameDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.RemoveCrmSupplierRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmSupplierRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmSupplierRequest;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;

public interface CrmSupplierApi  {
    /**
     * 根据基本信息系统编码获取商业扩展列表
     * @param crmEnterIds
     * @return
     */
    public List<CrmSupplierDTO> getSupplierInfoByCrmEnterId(List<Long> crmEnterIds) ;

    /**
     * 根据系统编码逻辑删除
     * @param request
     */
    void removeCrmSupplierById(RemoveCrmSupplierRequest request);

    /**
     * 根据基本信息系统编码获取商业扩展信
     * @param id
     * @return
     */
    CrmSupplierDTO getCrmSupplierByCrmEnterId(Long id);

    /**
     * 根据基本信息系统编码获取商业扩展信
     * @param id
     * @return
     */
    CrmSupplierDTO getBakCrmSupplierByCrmEnterId(Long id,String tableSuffix);

    /**
     * 保存扩展信息
     * @param request
     * @return
     */
    int saveCrmSupplierInfo(SaveCrmSupplierRequest request);

    /**
     * 批量更新扩展信息
     * @param requests
     * @return
     * @author gxl
     */
    boolean updateCrmSupplierBatch(List<UpdateCrmSupplierRequest> requests);

    int updateCrmSupplierInfo(UpdateCrmSupplierRequest request);

    List<CrmEnterpriseRelationShipDTO> getRelationByCrmEnterpriseId(Long id, String name);

    List<CrmEnterpriseNameDTO> getCrmEnterpriseNameListById(List<String> parentCrmEenterIds);

    List<Long> listByLevelAndGroup(Integer supplierLevel, Integer businessSystem);

    /**
     * 根据商务负责人工号查询机构编码列表
     * @return
     */
    List<Long>  getCrmEnterIdListByCommerceJobNumber(String empId);
}
