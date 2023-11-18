package com.yiling.dataflow.crm.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.request.PermitAgencyLockApplyRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.dto.request.QueryDataScopeRequest;
import com.yiling.dataflow.crm.dto.request.SaveAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
public interface CrmEnterpriseApi {

    /**
     * 通过企业名称模糊获取crm企业信息
     *
     * @param request
     * @return
     */
    Page<CrmEnterpriseSimpleDTO> getCrmEnterpriseSimplePage(QueryCrmEnterprisePageRequest request);

    /**
     * 新增crm企业档案表
     *
     * @param request
     * @return
     */
    Long saveCrmEnterpriseSimple(SaveCrmEnterpriseRequest request);

    /**
     * 新增机构档案基础信息
     *
     * @param requestList   新增机构信息
     */
    void saveAgencyEnterpriseList(List<SaveAgencyEnterpriseRequest> requestList);

    /**
     * 修改机构档案基础信息
     *
     * @param requestList   机构信息
     */
    void updateAgencyEnterpriseList(List<UpdateAgencyEnterpriseRequest> requestList);

    /**
     * 修改企业档案表
     *
     * @param request 修改参数
     * @return 成功/失败
     */
    Integer updateCrmEnterpriseSimple(UpdateAgencyEnterpriseRequest request);

    /**
     * 通过商业名称获取crm企业信息
     *
     * @param name
     * @return
     */
    CrmEnterpriseDTO getCrmEnterpriseCodeByName(String name,boolean isEffect);

    /**
     * 根据名称批量查询
     *
     * @param nameList
     * @return
     */
    List<CrmEnterpriseDTO> getCrmEnterpriseCodeByNameList(List<String> nameList);

    /**
     * 根据code批量查询
     *
     * @param codeList
     * @return
     */
    List<CrmEnterpriseDTO> getCrmEnterpriseByCodeList(List<String> codeList);

    /**
     * 根据条件批量查询
     *
     * @param request
     * @return
     */
    Page<CrmEnterpriseDTO> getCrmEnterpriseInfoPage(QueryCrmAgencyPageListRequest request);

    /**
     * 根据条件查询备份表机构基础信息
     *
     * @param request   查询条件
     * @return  机构基础信息
     */
    Page<CrmEnterpriseDTO> pageListSuffixBackUpInfo(QueryCrmEnterpriseBackUpPageRequest request);

    /**
     * 根据企业id列表查询对应企业信息
     * @param ids
     * @return
     */
    List<CrmEnterpriseDTO> listByIdsAndName(List<Long> ids, String name);

    /**
     * 根据终端类型查询对应企业信息数量
     * @param supplyChainRole
     * @return
     */
    int getCountBySupplyChainRole(Integer supplyChainRole);

    CrmEnterpriseDTO getCrmEnterpriseById(Long id);

    /**
     * 根据条件查询符合条件的数据数量
     *
     * @param request 查询条件
     * @return 数量
     */
    CrmEnterpriseDTO getFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request);

    CrmEnterpriseDTO getBakFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request, String tableSuffix);

    /**
     * 根据机构名称模糊搜索获得机构id和机构名称，提供备份表查询
     *
     * @param request  机构名称查询条件
     * @return  机构id和机构名称
     */
    Page<CrmEnterpriseIdAndNameBO> getCrmEnterpriseIdAndNameByName(QueryCrmEnterpriseByNamePageListRequest request);

    /**
     * 根据机构名称模糊搜索获得机构部分基础信息，提供备份表查询
     *
     * @param request  机构名称查询条件
     * @return  机构部分基础信息
     */
    Page<CrmEnterprisePartBO> getCrmEnterprisePartInfoByName(QueryCrmEnterpriseByNamePageListRequest request);

    Page<CrmEnterpriseDTO> getCrmEnterpriseByName(QueryCrmEnterpriseByNamePageListRequest name);

    /**
     * 根据名称前匹配查询机构主信息
     *
     * @param request 查询条件
     * @return  机构主信息
     */
    Page<CrmEnterpriseDTO> getCrmEnterpriseByNameLikeRight(QueryCrmEnterpriseByNamePageListRequest request);

    List<CrmEnterpriseDTO> getCrmEnterpriseListById(List<Long> crmEnterPriseIds);

    /**
     * 机构锁定流程通过后设置相关信息
     *
     * @param request
     */
    Boolean permitAgencyLockApply(List<PermitAgencyLockApplyRequest> request);
    /**
     * 根据企业id列表查询对应企业信息
     * @param eidList
     * @return
     */
    List<CrmEnterpriseDTO> listByEidList(List<Long> eidList);

    /**
     * 根据d列表查询对应企业信息，仅查询类型为 经销商 的
     * @param datascopeBO
     * @return
     */
    List<CrmEnterpriseDTO> getDistributorEnterpriseByIds(SjmsUserDatascopeBO datascopeBO);


    /**
     * 根据企业id列表查询对应备份表企业信息
     * @param id
     * @return
     */
    CrmEnterpriseDTO getCrmEnterpriseBackById(Long id,String tableSuffix);

    /**
     * 根据省区权限和部门企业权限获取机构编码和popEid
     * @param request
     * @return
     */
    List<CrmEnterpriseDTO> getCrmEnterpriseListByDataScope(QueryDataScopeRequest request);
}
