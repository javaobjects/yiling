package com.yiling.dataflow.flow.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.bo.FlowEnterpriseConnectStatisticBO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseConnectMonitorDTO;
import com.yiling.dataflow.flow.dto.request.QueryEnterpriseConnectMonitorPageRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseConnectMonitorRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseConnectMonitorDO;
import com.yiling.dataflow.flow.enums.ConnectStatusEnum;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectMonitorService
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
public interface FlowEnterpriseConnectMonitorService extends BaseService<FlowEnterpriseConnectMonitorDO> {

    /**
     * 保存流向直连监控数据
     * @param request
     * @return
     */
    Long saveOrUpdate(SaveFlowEnterpriseConnectMonitorRequest request);

    /**
     * crm企业id查询流向直连监控
     * @param crmEnterpriseId
     * @return
     */
    FlowEnterpriseConnectMonitorDTO findByCrmEid(Long crmEnterpriseId);

    /**
     * erp客户端查询流向直连监控
     * @param erpClientId
     * @return
     */
    FlowEnterpriseConnectMonitorDTO findByErpClientId(Long erpClientId);

    /**
     * 分页查询流向直连监控数据
     * @param request
     * @return
     */
    Page<FlowEnterpriseConnectMonitorDTO> page(QueryEnterpriseConnectMonitorPageRequest request);

    /**
     * 列表条件查询流向直连监控数据
     * @param request
     * @return
     */
    List<FlowEnterpriseConnectMonitorDTO> listByQuery(QueryEnterpriseConnectMonitorPageRequest request);

    /**
     * 统计各省份有效直连和无效直连数量
     * @param supplierLevel
     * @return
     */
    List<FlowEnterpriseConnectStatisticBO> countConnectionStatusByProvince(Integer supplierLevel);

    /**
     * 有效经销商计数
     * @param connectStatus 非必传参数 0 无效，1 有效 null计数全部
     * @param userDatascopeBO 权限对象
     */
    Integer countMonitorEnterprise(SjmsUserDatascopeBO userDatascopeBO, ConnectStatusEnum connectStatus);

    /**
     * 根据erpClientId逻辑删除监控
     * @param clientIds
     * @param msg
     * @param opUserId
     * @return
     */
    Integer deleteByClientId(List<Long> clientIds,String msg,Long opUserId);

    /**
     * 实施人名称查询流向直连监控
     * @param installEmployee
     * @return
     */
    FlowEnterpriseConnectMonitorDTO findByInstallEmployee(String installEmployee);

    List<FlowEnterpriseConnectMonitorDO> findFlowEnterpriseConnectMonitorDTOList();
}
