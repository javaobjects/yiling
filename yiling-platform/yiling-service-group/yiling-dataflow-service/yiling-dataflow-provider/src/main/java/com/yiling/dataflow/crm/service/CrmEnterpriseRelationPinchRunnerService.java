package com.yiling.dataflow.crm.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationPinchRunnerPageListRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmEnterpriseRelationPinchRunnerRequest;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipPinchRunnerRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-04-19
 */
public interface CrmEnterpriseRelationPinchRunnerService extends BaseService<CrmEnterpriseRelationPinchRunnerDO> {

    Page<CrmEnterpriseRelationPinchRunnerDO> page(QueryCrmEnterpriseRelationPinchRunnerPageListRequest request);

    /**
     * 根据id查询代跑三者关系
     *
     * @param userDatascopeBO 数据权限
     * @param id 主键ID
     * @return
     */
    CrmEnterpriseRelationPinchRunnerDO getById(SjmsUserDatascopeBO userDatascopeBO, Long id);

    /**
     * 根据机构ID、三者关系ID查询代跑三者关系信息
     *
     * @param crmEnterpriseId 机构id
     * @param crmRelationShipId 三者关系id
     * @return
     */
    CrmEnterpriseRelationPinchRunnerDO getByCrmEnterpriseIdAndCrmRelationShipId(Long crmEnterpriseId, Long crmRelationShipId);

    /**
     * 根据机构ID、三者关系id列表查询代跑三者关系列表
     *
     * @param crmEnterpriseId 机构id，非必传（机构id、三者关系id 至少需要一个）
     * @param crmRelationShipIds 三者关系id列表，非必传（机构id、三者关系id 至少需要一个）
     * @param tableSuffix 表后缀 比如wash_202303
     * @return 代跑三者关系列表
     */
    List<CrmEnterpriseRelationPinchRunnerDO> getByCrmEnterpriseIdAndRelationShipIds(Long crmEnterpriseId, List<Long> crmRelationShipIds, String tableSuffix);

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
     * 获取销量计入主管岗位代码列表
     *
     * @return
     */
    List<Long> getBusinessSuperiorPostCode();

    /**
     * 根据表名、主管岗位代码，更新主管岗位信息
     *
     * @param request
     * @param tableName
     * @return
     */
    boolean updateBackUpBatchByBusinessSuperiorPostCode(UpdateCrmEnterpriseRelationShipPinchRunnerRequest request, String tableName);

}
