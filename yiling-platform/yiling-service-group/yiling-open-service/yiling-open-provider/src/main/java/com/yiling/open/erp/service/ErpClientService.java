package com.yiling.open.erp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.open.erp.bo.SjmsFlowMonitorNoDataSyncEnterpriseBO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientParentQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.open.erp.dto.request.ErpClientSaveRequest;
import com.yiling.open.erp.dto.request.ErpMonitorQueryRequest;
import com.yiling.open.erp.dto.request.QueryClientFlowEnterpriseRequest;
import com.yiling.open.erp.dto.request.UpdateClientStatusRequest;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.dto.request.UpdateMonitorStatusRequest;
import com.yiling.open.erp.entity.ErpClientDO;

/**
 * <p>
 * 客户端抽取工具实例表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2020-08-12
 */
public interface ErpClientService {

    /**
     * 查询对接客户信息
     * @param suId
     * @param suDeptNo
     * @return
     */
    ErpClientDTO getErpClientBySuIdAndSuDeptNo(Long suId, String suDeptNo);

    /**
     * 通过key查询对接客户信息
     * @param key
     * @return
     */
    ErpClientDTO selectByClientKey(String key);

    /**
     * 通过客户编码查询对接客户集合
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
     * 修改远程操作状态和时间
     * @param erpClientSaveRequest
     * @return
     */
     Boolean updateCommandBySuId(ErpClientSaveRequest erpClientSaveRequest);

    /**
     * 保存/更新
     * @param erpClientSaveRequest
     * @return
     */
    Long saveOrUpdateErpClient(ErpClientSaveRequest erpClientSaveRequest);

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<ErpClientDO> page(ErpClientQueryRequest request);

    /**
     * 通过rksuid列表获取对接商业公司信息列表
     * @param rkSuIdList
     * @return
     */
    List<ErpClientDO> selectByRkSuIdList(List<Long> rkSuIdList);

    /**
     * 通过secret查询对接客户信息
     * @param secret
     * @return
     */
    ErpClientDTO selectByClientSecret(String secret);

    /**
     * 查询父级企业列表分页
     * @param request
     * @return
     */
    Page<ErpClientDO> parentPage(ErpClientParentQueryRequest request);

    /**
     * 开启/关闭监控状态
     * @param request
     * @return
     */
    Boolean updateMonitorStatus(UpdateMonitorStatusRequest request);

    /**
     * 开启/关闭对接状态
     * @param request
     * @return
     */
    Boolean updateClientStatus(UpdateClientStatusRequest request);

    /**
     * 根据suid更新最后心跳时间
     * @param request
     * @return
     */
    Boolean updateHeartBeatTimeBySuid(UpdateHeartBeatTimeRequest request);

    /**
     * erp监控信息列表分页
     */
    Page<ErpClientDO> getErpMonitorListPage(ErpMonitorQueryRequest request);

    /**
     * 查询流向公司列表分页
     *
     * @param request
     * @return
     */
    Page<ErpClientDO> flowEnterprisePage(QueryClientFlowEnterpriseRequest request);

//
//    ErpClientDO get(Long id);
//
//    Boolean saveOrUpdate(ErpClientSaveRequest request);
//
//    List<Integer> findErpClientByDepthTimeNotNull();

    /**
     * 根据商业公司名称查询已开启流向对接的
     *
     * @param name
     * @return
     */
    List<ErpClientDO> getFlowEnterpriseListByName(String name);

    /**
     * 通过rksuid列表获取对接商业公司信息列表
     *
     * @param request
     * @return
     */
    List<ErpClientDO> getAllFlowEnterpriseList(ErpClientQueryRequest request);


    /**
     * 根据id列表更新数据初始化状态为已完成
     *
     * @param idList
     * @return
     */
    boolean updateDdataInitStatusByIdList(List<Long> idList);

    /**
     * 根据企业id列表更新最新采集日期、最新流向日期
     *
     * @param list
     * @return
     */
    boolean updateCollectAndFlowDateBatch(List<ErpClientDO> list);

    /**
     * 已部署接口数量
     *
     * @return
     */
    Integer getDeployInterfaceCountByRkSuIdList(List<String> licenseNumberList);

    /**
     * 未开启同步数量
     *
     * @return
     */
    Integer getSyncStatusOffCountByRkSuIdList(List<String> licenseNumberList);

    /**
     * 终端未激活数量
     *
     * @return
     */
    Integer getClientStatusOffCountByRkSuIdList(List<String> licenseNumberList);

    /**
     * 终端未激活数量
     *
     * @return
     */
    Integer getRunningCountByRkSuIdList(List<String> licenseNumberList);

    /**
     * 终端未激活数量，分页列表
     *
     * @return
     */
    Integer getRunningCountByRkSuIdListForPage(ErpClientQuerySjmsRequest request);

    /**
     * 超x天未上传流向数量，业务日期
     *
     * @return
     */
    Integer getNoDatCountByRkSuIdListAndFlowDate(List<String> licenseNumberList, Date lastestFlowDateEnd);

    /**
     * 超x天未上传流向数量，上传日期
     *
     * @param datascopeBO 数据权限
     * @param lastestCollectDateEnd 上传日期结束时间
     * @return
     */
    Integer getNoDatCountByRkSuIdListAndFlowDateByCrmIds(SjmsUserDatascopeBO datascopeBO, Date lastestCollectDateEnd);

    /**
     * 超x天未上传流向数量，分页列表
     *
     * @return
     */
    Integer getNoDatCountByRkSuIdListAndFlowDateForPage(ErpClientQuerySjmsRequest request);

    /**
     * 根据企业id查询未上传流向天数最大的前10个企业的列表
     * 已激活、已开启同步、流向已对接
     *
     * @param licenseNumberList
     * @return
     */
    Integer getNoDataSyncCountByRkSuIdListAndLastestFlowDate(List<String> licenseNumberList, Date lastestFlowDateEnd);

    /**
     * 根据企业id查询未上传流向天数最大的前10个企业的列表
     * 已激活、已开启同步、流向已对接
     *
     * @param licenseNumberList
     * @return
     */
    List<SjmsFlowMonitorNoDataSyncEnterpriseBO> getNoDataSyncEnterpriseListByRkSuIdListAndEndDate(List<String> licenseNumberList, Date yesterday);

    /**
     * 查询列表分页
     * 企业id列表参数 rkSuIdList 不能为空
     *
     * @param request
     * @return
     */
    Page<ErpClientDO> sjmsPage(ErpClientQuerySjmsRequest request);

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
    List<ErpClientDO> getByLicenseNumberList(List<String> licenseNumberList);

    /**
     * 根据crmID列表查询
     *
     * @param crmEnterpriseIdList
     * @return
     */
    List<ErpClientDO> getByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList);

    /**
     * 根据crmID列表查询，有数据权限，crmEnterpriseIdList.size = 0 时查所有
     *
     * @param crmEnterpriseIdList
     * @return
     */
    List<ErpClientDO> getWithDatascopeByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList);

    /**
     * 根据crm企业id查询
     *
     * @param crmEnterpriseId
     * @return
     */
    ErpClientDO getByCrmEnterpriseId(Long crmEnterpriseId);

}
