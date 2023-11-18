package com.yiling.dataflow.agency.service;

import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import java.util.List;

import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 商业公司扩展表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
public interface CrmSupplierService extends BaseService<CrmSupplierDO> {

    /**
     * 获取商业公司扩展信息
     * @param crmId
     * @return
     */
    CrmSupplierDO getCrmSupplierByCrmId(Long crmId);
    /**
     * 获取商业公司扩展信息
     * @param crmIds
     * @return
     */
    List<CrmSupplierDO> getCrmSupplierByCrmIds(List<Long> crmIds);

    /**
     * 备份表根据商业机构id查询机构扩展信息
     *
     * @param crmEnterpriseIdList  机构id
     * @param tableSuffix   表名
     * @return  商业机构扩展信息
     */
    List<CrmSupplierDO> listSuffixByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList, String tableSuffix);
    List<Long> listByLevelAndGroup(Integer supplierLevel, Integer businessSystem);

    List<CrmSupplierDTO> getSupplierInfoByCrmEnterId(List<Long> crmEnterIds);

    List<Long> getCrmEnterIdListByCommerceJobNumber(String empId);
}
