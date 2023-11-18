package com.yiling.dataflow.agency.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.agency.entity.CrmHospitalDO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.request.ChangeRelationShipRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 医疗机构拓展表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
public interface CrmHospitalService extends BaseService<CrmHospitalDO> {

    /**
     * 查询档案医疗扩展信息
     * @param ids
     * @return
     */
    List<CrmHospitalDO> getByCrmEnterpriseIds(List<Long> ids);

    CrmHospitalDO getByCrmEnterpriseId(Long id);


    Page<CrmEnterpriseRelationShipDTO> getCrmRelationPage(QueryCrmEnterpriseRelationShipPageListRequest request);


    Boolean saveOrUpdateRelationShip(SaveCrmEnterpriseRelationShipRequest request);

    Boolean changeRelationShip(ChangeRelationShipRequest srcRelationShipIps);

    /**
     * 备份表根据医疗机构id查询机构扩展信息
     *
     * @param crmEnterpriseIdList  机构id
     * @param tableSuffix   表名
     * @return  医疗机构扩展信息
     */
    List<CrmHospitalDO> listSuffixByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList, String tableSuffix);

    Page<CrmEnterpriseRelationShipDTO> getCrmRelationBackUpPage(QueryCrmEnterpriseRelationShipPageListRequest request,String tableSuffix);
}
