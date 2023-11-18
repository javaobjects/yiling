package com.yiling.dataflow.crm.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleRegionInfoDTO;
import com.yiling.dataflow.crm.dto.request.PermitAgencyLockApplyRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.dto.request.QueryDataScopeRequest;
import com.yiling.dataflow.crm.dto.request.SaveAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-21
 */
public interface CrmEnterpriseService extends BaseService<CrmEnterpriseDO> {

    /**
     * 通过企业名称模糊获取crm企业信息
     *
     * @param request
     * @return
     */
    Page<CrmEnterpriseSimpleDTO> getCrmEnterpriseSimplePage(QueryCrmEnterprisePageRequest request);

    /**
     * 新增crm档案表
     *
     * @param request
     * @return
     */
    Long saveCrmEnterpriseSimple(SaveCrmEnterpriseRequest request);

    /**
     * 修改企业档案表
     *
     * @param request 修改参数
     * @return 成功/失败
     */
    Integer updateCrmEnterpriseSimple(UpdateAgencyEnterpriseRequest request);

    /**
     * 新增机构档案基础信息
     *
     * @param requestList 新增机构信息
     */
    void saveAgencyEnterpriseList(List<SaveAgencyEnterpriseRequest> requestList);

    /**
     * 修改机构档案基础信息
     *
     * @param requestList 机构信息
     */
    void updateAgencyEnterpriseList(List<UpdateAgencyEnterpriseRequest> requestList);

    /**
     * 通过企业名称获取crm企业code
     *
     * @param name     企业名称
     * @param isEffect 是否生效 （true 查询生效企业 false查询全部企业）
     * @return
     */
    CrmEnterpriseDTO getCrmEnterpriseCodeByName(String name, boolean isEffect);

    /**
     * 根据统一信用代码获取crm企业信息
     *
     * @param licenseNumber 统一信用代码
     * @param isEffect      是否生效 （true 查询生效企业 false查询全部企业）
     * @return
     */
    CrmEnterpriseDTO getCrmEnterpriseByLicenseNumber(String licenseNumber, boolean isEffect);

    /**
     * 获取所有的企业信息
     *
     * @return
     */
    Page<CrmEnterpriseDTO> getCrmEnterprisePage(QueryCrmEnterprisePageRequest request);

    /**
     * 根据名称批量查询
     *
     * @param nameList
     * @return
     */
    List<CrmEnterpriseDTO> getCrmEnterpriseCodeByNameList(List<String> nameList);

    /**
     * 获取企业信息
     *
     * @return
     */
    Page<CrmEnterpriseDTO> getCrmEnterpriseInfoPage(QueryCrmAgencyPageListRequest request);

    /**
     * 根据条件查询符合条件的数据数量
     *
     * @param request 查询条件
     * @return 数量
     */
    CrmEnterpriseDO getFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request);

    CrmEnterpriseDO getBakFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request, String tableSuffix);

    /**
     * 根据机构名称模糊搜索获得机构id和机构名称,提供备份表查询
     *
     * @param request     机构名称查询条件
     * @param tableSuffix
     * @return 机构id和机构名称
     */
    Page<CrmEnterpriseIdAndNameBO> getCrmEnterpriseIdAndNameByName(QueryCrmEnterpriseByNamePageListRequest request, String tableSuffix);

    /**
     * 根据机构名称模糊搜索获得机构部分基础信息
     *
     * @param request     机构名称查询条件
     * @param tableSuffix
     * @return 机构部分基础信息
     */
    Page<CrmEnterprisePartBO> getCrmEnterprisePartInfoByName(QueryCrmEnterpriseByNamePageListRequest request, String tableSuffix);

    /**
     * 根据名称前匹配查询机构主信息
     *
     * @param request 查询条件
     * @return 机构主信息
     */
    Page<CrmEnterpriseDO> getCrmEnterpriseByNameLikeRight(QueryCrmEnterpriseByNamePageListRequest request);

    /**
     * 根据code批量查询
     *
     * @param codeList
     * @return
     */
    List<CrmEnterpriseDTO> getCrmEnterpriseByCodeList(List<String> codeList);

    /**
     * 根据省区查询对应企业信息
     *
     * @param provincialAreas
     * @return
     */
    @Deprecated
    List<CrmEnterpriseDTO> listByProvincialAreas(List<String> provincialAreas);

    /**
     * 根据省份查询对应企业信息ID列表
     *
     * @param provinceNames
     * @return
     */
    List<Long> listIdsByProvinceNames(List<String> provinceNames);

    /**
     * 根据企业id列表查询对应企业信息
     *
     * @param ids
     * @return
     */
    List<CrmEnterpriseDTO> listByIdsAndName(List<Long> ids, String name);

    /**
     * 根据终端类型查询对应企业信息数量
     *
     * @param supplyChainRole
     * @return
     */
    int getCountBySupplyChainRole(Integer supplyChainRole);

    List<CrmEnterpriseDTO> getCrmEnterpriseListById(List<Long> crmEnterPriseIds);

    /**
     * 机构锁定流程通过后设置相关信息
     *
     * @param request
     * @return
     */
    Boolean permitAgencyLockApply(List<PermitAgencyLockApplyRequest> request);

    /**
     * 根据企业id列表查询对应企业信息
     *
     * @param eidList
     * @return
     */
    List<CrmEnterpriseDTO> listByEidList(List<Long> eidList);

    /**
     * 根据d列表查询对应企业信息
     *
     * @return
     */
    List<CrmEnterpriseDTO> getDistributorEnterpriseByIds(SjmsUserDatascopeBO datascopeBO);

    List<Long> listById(List<Long> crmEnterIdList);

    Page<CrmEnterpriseDTO> getCrmEnterpriseByName(QueryCrmEnterpriseByNamePageListRequest request);

    /**
     * 根据id、企业名称、erp供应链角色类型查询列表，限制50个
     *
     * @param id                  机构编码（非必填，id、name 至少填一个）
     * @param name                机构名称（非必填，id、name 至少填一个）
     * @param supplyChainRoleList erp供应链角色（必填）：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role。枚举类：CrmSupplyChainRoleEnum
     * @return
     */
    List<CrmEnterpriseSimpleRegionInfoDTO> listByNameAndSupplyChainRoleWithLimit(Long id, String name, List<Integer> supplyChainRoleList);

    /**
     * 备份表根据机构id查询机构主信息
     *
     * @param crmEnterpriseIdList 机构id
     * @param tableSuffix         表名
     * @return 机构主档案信息
     */
    List<CrmEnterpriseDO> listSuffixByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList, String tableSuffix);

    /**
     * 根据条件查询备份表机构基础信息
     *
     * @param request     查询条件
     * @param tableSuffix 表名
     * @return 机构基础信息
     */
    Page<CrmEnterpriseDO> pageListSuffixBackUpInfo(QueryCrmEnterpriseBackUpPageRequest request, String tableSuffix);

    /**
     * 备份表根据机构id查询机构主信息
     *
     * @param crmId       机构id
     * @param tableSuffix 表名
     * @return 机构主档案信息
     */
    CrmEnterpriseDO getSuffixByCrmEnterpriseId(Long crmId, String tableSuffix);

    List<Long> getCrmEnterpriseListByEidsAndProvinceCode(List<Long> eids, List<String> provinceCodes);

    List<CrmEnterpriseDTO> getCrmEnterpriseListByDataScope(QueryDataScopeRequest request);

    Page<CrmEnterpriseIdAndNameBO> getIdAndNameListPage(Integer current, Integer size);
}
