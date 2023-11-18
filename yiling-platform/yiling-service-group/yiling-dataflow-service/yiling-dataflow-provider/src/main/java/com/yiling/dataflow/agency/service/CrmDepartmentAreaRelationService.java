package com.yiling.dataflow.agency.service;

import java.util.List;

import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.entity.CrmDepartmentAreaRelationDO;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 省区与业务省区对应关系表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-02-16
 */
public interface CrmDepartmentAreaRelationService extends BaseService<CrmDepartmentAreaRelationDO> {

    String getProvinceAreaByThreeParms(String yxDept, String yxProvince);

    List<CrmDepartmentAreaRelationDTO> getGoodsGroup(String ywbm,Integer supplyChainRole);

    String getBackUpProvinceAreaByThreeParms(String yxDept, String yxProvince,String backUpTableName);
}
