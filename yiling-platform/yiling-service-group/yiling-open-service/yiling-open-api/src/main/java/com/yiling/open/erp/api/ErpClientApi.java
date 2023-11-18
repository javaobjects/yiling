package com.yiling.open.erp.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.open.erp.bo.ErpMonitorCountInfoDetailBO;
import com.yiling.open.erp.bo.ErpMonitorCountStatisticsBO;
import com.yiling.open.erp.bo.SjmsFlowCollectStatisticsCountBO;
import com.yiling.open.erp.bo.SjmsFlowMonitorNoDataSyncEnterpriseBO;
import com.yiling.open.erp.bo.SjmsFlowMonitorStatisticsCountBO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientParentQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.open.erp.dto.request.ErpClientSaveRequest;
import com.yiling.open.erp.dto.request.ErpMonitorQueryRequest;
import com.yiling.open.erp.dto.request.QueryClientFlowEnterpriseRequest;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.dto.request.UpdateMonitorStatusRequest;

/**
 * 对接商业公司管理
 * @author shuan
 */
public interface ErpClientApi {

    /**
     * 通过key获取客户信息
     * @param key
     * @return
     */
    ErpClientDTO findErpClientByKey(String key);


    /**
     * 通过企业编码查询企业信息
     * @param suId
     * @return
     */
    List<ErpClientDTO> selectBySuId(Long suId);

    /**
     * 通过rksuid获取suid和分部门
     * @param rkSuId
     * @return
     */
    ErpClientDTO selectByRkSuId(Long rkSuId);

    /**
     * 修改指令
     * @param erpClientSaveRequest
     * @return
     */
    Boolean updateCommandBySuId(ErpClientSaveRequest erpClientSaveRequest);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<ErpClientDTO> page(ErpClientQueryRequest request);

    /**
     * 通过rksuid列表获取对接商业公司信息列表
     * @param rkSuIdList
     * @return
     */
    List<ErpClientDTO> selectByRkSuIdList(List<Long> rkSuIdList);

    /**
     * 保存/更新
     * @param erpClientSaveRequest
     * @return
     */
    Long saveOrUpdateErpClient(ErpClientSaveRequest erpClientSaveRequest);

    /**
     * 查询父级企业列表分页
     * @param request
     * @return
     */
    Page<ErpClientDTO> parentPage(ErpClientParentQueryRequest request);

    /**
     * 开启/关闭监控状态
     * @param request
     * @return
     */
    Boolean updateMonitorStatus(UpdateMonitorStatusRequest request);

    /**
     * 根据suid更新最后心跳时间
     * @param request
     * @return
     */
    Boolean updateHeartBeatTimeBySuid(UpdateHeartBeatTimeRequest request);

    /**
     * erp监控信息列表分页
     */
    Page<ErpClientDTO> getErpMonitorListPage(ErpMonitorQueryRequest request);

    /**
     * 监控统计
     * 超过请求次数关闭对接数量、1小时内无心跳对接数量
     *
     * @return
     */
    ErpMonitorCountStatisticsBO getErpMonitorCountStatistics();

    /**
     * 查询流向公司列表分页
     *
     * @param request
     * @return
     */
    Page<ErpClientDTO> flowEnterprisePage(QueryClientFlowEnterpriseRequest request);


    /**
     * 根据商业公司名称查询已开启流向对接的
     *
     * @param name
     * @return
     */
    List<ErpClientDTO> getFlowEnterpriseListByName(String name);

    /**
     * 当月未上传销售列表分页
     */
    Page<ErpClientDTO> getNoSaleListPage(ErpClientQueryRequest request);

    /**
     * 统计当月没有销售数量的企业eid，并且保存到redis里面
     */
    void statisticsNoFlowSaleEidList();

    /**
     * 获取erp对接企业请求次数阈值信息
     *
     * @return
     */
    List<ErpMonitorCountInfoDetailBO> getErpMonitorCountInfoDetail();

    /**
     * 昨天无心跳的企业信息
     */
    void handleErpClientsNoHeartBetween24h();

    /**
     * 数据初始化状态更新
     */
    void erpClientDataInitStatusUpdate();


    /**
     * * 监控统计：
     *      * 已部署接口数量
     *      * 未开启同步数量
     *      * 终端未激活数量
     *      * 超7天未上传流向数量
     *      * 超15天未上传流向数量
     *
     * @return
     */
    SjmsFlowMonitorStatisticsCountBO getSjmsFlowMonitorStatisticsCount(List<String> licenseNumberList);

    /**
     * 监控统计：超xx天未上传流向数量
     *
     * @param datascopeBO 数据权限
     * @return
     */
    SjmsFlowMonitorStatisticsCountBO getSjmsFlowMonitorStatisticsCountByCrmIds(SjmsUserDatascopeBO datascopeBO);

    /**
     * 监控统计：分页列表
     * 运行中数量
     * 未上传昨日流向数量
     * 超7天未上传流向数量
     * 超15天未上传流向数量
     *
     * @return
     */
    SjmsFlowCollectStatisticsCountBO getSjmsFlowCollectStatisticsCount(ErpClientQuerySjmsRequest request);

    /**
     * 根据企业id、上传时间查询未上传流向天数最大的前10个企业的列表
     *
     * @param licenseNumberList
     * @return
     */
    List<SjmsFlowMonitorNoDataSyncEnterpriseBO> getSjmsNoDataSyncEnterpriseList(List<String> licenseNumberList);

    /**
     * 查询列表分页
     *
     * @param request
     * @return
     */
    Page<ErpClientDTO> sjmsPage(ErpClientQuerySjmsRequest request);

    /**
     * 根据企业id列表查询实施负责人姓名
     *
     * @param eidList 企业id
     * @return key-eid, value-installEmployeeName
     */
    Map<Long, String> getInstallEmployeeByEidList(List<Long> eidList);

    /**
     * 根据crm机构id列表查询实施负责人姓名
     *
     * @param crmEnterpriseIdList crm机构id
     * @return key-crmEnterpriseId, value-installEmployeeName
     */
    Map<Long, String> getInstallEmployeeByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList);

    /**
     * 根据社会统一信用代码列表查询
     *
     * @param licenseNumberList
     * @return
     */
    List<ErpClientDTO> getByLicenseNumberList(List<String> licenseNumberList);

    /**
     * 根据crm企业id查询
     *
     * @param crmEnterpriseId
     * @return
     */
    ErpClientDTO getByCrmEnterpriseId(Long crmEnterpriseId);

    /**
     * 根据crmID列表查询
     *
     * @param crmEnterpriseIdList
     * @return
     */
    List<ErpClientDTO> getByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList);

    /**
     * 根据crmID列表查询，有数据权限，crmEnterpriseIdList.size = 0 时查所有
     *
     * @param crmEnterpriseIdList
     * @return
     */
    List<ErpClientDTO> getWithDatascopeByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList);

}
