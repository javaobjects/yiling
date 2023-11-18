package com.yiling.dataflow.crm.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPinchRunnerDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationPinchRunnerPageListRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipPinchRunnerRequest;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;

/**
 * @author: houjie.sun
 * @date: 2023/4/19
 */
public interface CrmEnterpriseRelationPinchRunnerApi {

    Page<CrmEnterpriseRelationPinchRunnerDTO> page(QueryCrmEnterpriseRelationPinchRunnerPageListRequest request);

    /**
     * 根据id查询代跑三者关系
     *
     * @param userDatascopeBO 数据权限
     * @param id 主键ID
     * @return
     */
    CrmEnterpriseRelationPinchRunnerDTO getById(SjmsUserDatascopeBO userDatascopeBO, Long id);

    /**
     * 根据机构ID、三者关系ID查询代跑三者关系信息
     *
     * @param crmEnterpriseId 机构id
     * @param crmRelationShipId 三者关系id
     * @return
     */
    CrmEnterpriseRelationPinchRunnerDTO getByCrmEnterpriseIdAndCrmRelationShipId(Long crmEnterpriseId, Long crmRelationShipId);

    /**
     * 根据机构ID、三者关系id列表查询代跑三者关系列表
     *
     * @param crmEnterpriseId 机构id，非必传（机构id、三者关系id 至少需要一个）
     * @param crmRelationShipIds 三者关系id列表，非必传（机构id、三者关系id 至少需要一个）
     * @param tableSuffix 表后缀 比如wash_202303
     * @return 代跑三者关系列表
     */
    List<CrmEnterpriseRelationPinchRunnerDTO> getByCrmEnterpriseIdAndRelationShipIds(Long crmEnterpriseId, List<Long> crmRelationShipIds, String tableSuffix);

    /**
     * 新增
     *
     * @param request
     * @return
     */
    Long add(SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request);

    /**
     * 编辑
     *
     * @param request
     * @return
     */
    Boolean edit(SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest request);

    /**
     * 删除
     *
     * @param request
     * @return
     */
    Boolean remove(RemoveCrmEnterpriseRelationPinchRunnerRequest request);

    /**
     * 根据三者关系id列表删除
     *
     * @param request enterpriseCersIdList、opUserId，必填
     * @return
     */
    Integer removeByEnterpriseCersId(RemoveCrmEnterpriseRelationPinchRunnerRequest request);

    /**
     * 代跑三者关系备份表，根据月份更新销量计入主管岗位信息
     *
     * @param request
     * @param orgIds
     * @return
     */
    Boolean relationShipPinchRunnerBackup(AgencyBackRequest request, List<Long> orgIds);

}
