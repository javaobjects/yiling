package com.yiling.dataflow.agency.api;

import java.util.List;

import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.request.RemoveCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmSupplierRequest;

/**
 * 零售机构档案扩展表 API
 *
 * @author: yong.zhang
 * @date: 2023/2/14 0014
 */
public interface CrmPharmacyApi {

    /**
     * 根据机构基本信息id查询零售机构档案信息
     *
     * @param crmEnterpriseIdList 机构基本信息id集合
     * @return 零售机构档案扩展信息
     */
    List<CrmPharmacyDTO> listByCrmEnterpriseId(List<Long> crmEnterpriseIdList);

    /**
     * 根据机构基本信息id查询零售机构档案扩展信息
     *
     * @param crmEnterpriseId 机构基本信息id
     * @return 零售机构档案扩展信息
     */
    CrmPharmacyDTO queryByEnterpriseId(Long crmEnterpriseId);

    /**
     * 保存零售机构档案信息(基础信息和扩展信息)(新增、编辑)
     *
     * @param request 保存的信息
     * @return 成功/失败
     */
    boolean saveCrmPharmacy(SaveCrmPharmacyRequest request);

    /**
     * 删除零售机构档案扩展信息
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    boolean removeByEnterpriseId(RemoveCrmPharmacyRequest request);

    /**
     * 批量更新扩展信息
     *
     * @param requests
     * @return
     */
    boolean updateCrmPharmacyBatch(List<UpdateCrmPharmacyRequest> requests);
}
