package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.bo.EnterpriseStatisticsBO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDetailDTO;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.ImportEnterprisePlatformRequest;
import com.yiling.user.enterprise.dto.request.ImportEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.OpenPlatformRequest;
import com.yiling.user.enterprise.dto.request.QueryContactorEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.RegistEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseChannelRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseHmcTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseStatusRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateErpStatusRequest;
import com.yiling.user.enterprise.dto.request.UpdateManagerMobileRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

/**
 * 企业 API
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
public interface EnterpriseApi {

    /**
     * 根据ID获取企业信息
     *
     * @param id 企业ID
     * @return
     */
    EnterpriseDTO getById(Long id);

    /**
     * 根据名称获取企业信息
     *
     * @param name 企业名称
     * @return
     */
    EnterpriseDTO getByName(String name);

    /**
     * 根据执业许可证号/社会信用统一代码获取企业信息
     *
     * @param licenseNumber 执业许可证号/社会信用统一代码
     * @return
     */
    EnterpriseDTO getByLicenseNumber(String licenseNumber);

    /**
     * 根据ERP编码查询企业信息
     *
     * @param erpCode ERP编码
     * @return
     */
    EnterpriseDTO getByErpCode(String erpCode);

    /**
     * 根据ID列表批量获取企业信息
     *
     * @param ids 企业ID列表
     * @return
     */
    List<EnterpriseDTO> listByIds(List<Long> ids);

    /**
     * 获取用户所属企业信息列表
     *
     * @param userId 用户ID
     * @param statusEnum 状态枚举
     * @return
     */
    List<EnterpriseDTO> listByUserId(Long userId, EnableStatusEnum statusEnum);

    /**
     * 批量获取商务联系人负责的客户企业信息
     *
     * @param eid 企业ID
     * @param contactUserIds 商务联系人用户ID
     * @return
     */
    Map<Long, List<EnterpriseDTO>> listByContactUserIds(Long eid, List<Long> contactUserIds);

    /**
     * 根据上级企业ID获取下级企业信息列表
     *
     * @param parentId 上级企业ID
     * @return
     */
    List<EnterpriseDTO> listByParentId(Long parentId);

    /**
     * 获取下级企业ID列表
     *
     * @param eid 企业ID
     * @return
     */
    List<Long> listSubEids(Long eid);

    /**
     * 获取指定渠道类型的企业ID列表
     *
     * @param enterpriseChannelEnum 企业渠道枚举
     * @return
     */
    List<Long> listEidsByChannel(EnterpriseChannelEnum enterpriseChannelEnum);

    /**
     * 是否为以岭下级企业
     *
     * @param eid 企业ID
     * @return
     */
    boolean isYilingSubEid(Long eid);

    /**
     * 批量获取用户所属企业信息列表
     *
     * @param userIds 用户ID列表
     * @return key：用户ID，value：用户所属企业信息列表
     */
    Map<Long, List<EnterpriseDTO>> listByUserIds(List<Long> userIds);

    /**
     * 查询企业分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseDTO> pageList(QueryEnterprisePageListRequest request);

    /**
     * 销售助手，根据商务代表查询我的客户信息
     * @param request 查询商务联系人参数
     * @return
     */
    Page<EnterpriseDTO> myCustomerPageList(QueryContactorEnterprisePageListRequest request);

    /**
     * 注册企业信息
     *
     * @param request
     * @return 企业ID
     */
    Long regist(RegistEnterpriseRequest request);

    /**
     * 创建企业信息
     *
     * @param request
     * @return 企业ID
     */
    Long create(CreateEnterpriseRequest request);

    /**
     * 修改企业信息
     *
     * @param request
     * @return
     */
    boolean update(UpdateEnterpriseRequest request);

    /**
     * 修改企业类型
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/15
     **/
    boolean updateType(UpdateEnterpriseTypeRequest request);

    /**
     * 修改企业渠道
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/15
     **/
    boolean updateChannel(UpdateEnterpriseChannelRequest request);

    /**
     * 修改企业HMC业务类型
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/4/15
     **/
    boolean updateHmcType(UpdateEnterpriseHmcTypeRequest request);

    /**
     * 更新企业状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateEnterpriseStatusRequest request);

    /**
     * 更新审核状态
     *
     * @param request
     * @return
     */
    boolean updateAuthStatus(UpdateEnterpriseAuthRequest request);

    /**
     * 导入企业信息
     *
     * @param request
     * @return
     */
    boolean importData(ImportEnterpriseRequest request);

    /**
     * 导入企业开通平台
     *
     * @param request
     * @return
     */
    boolean importEnterprisePlatform(ImportEnterprisePlatformRequest request);

    /**
     * 获取企业开通的平台信息
     *
     * @param eid 企业ID
     * @return
     */
    EnterprisePlatformDTO getEnterprisePlatform(Long eid);

    /**
     * 企业开通平台
     *
     * @param request
     * @return
     */
    boolean openPlatform(OpenPlatformRequest request);

    /**
     * 批量获取企业开通的平台信息
     *
     * @param eids 企业ID列表
     * @return
     */
    List<EnterprisePlatformDTO> getEnterprisePlatforms(List<Long> eids);

    /**
     * 企业数量统计
     *
     * @return
     */
    EnterpriseStatisticsBO quantityStatistics();

    /**
     * 根据省市区查询企业列表
     *
     * @param request
     * @return 结合业务场景：为了查询性能，只返回企业ID字段
     */
    List<EnterpriseDTO> queryListByArea(QueryEnterpriseListRequest request);

    /**
     * 获取企业的销售区域
     *
     * @param eid 企业ID
     * @return
     */
    EnterpriseSalesAreaDTO getEnterpriseSalesArea(Long eid);

    /**
     * 批量获取企业的销售区域
     *
     * @param eids 企业ID列表
     * @return
     */
    List<EnterpriseSalesAreaDTO> listEnterpriseSalesArea(List<Long> eids);

    /**
     * 获取企业销售区域详情
     * @param eid
     * @return
     */
    List<EnterpriseSalesAreaDetailDTO> getEnterpriseSaleAreaDetail(Long eid);

    /**
     * 获取协议主体列表
     *
     * @return
     */
    List<EnterpriseDTO> listMainPart();

    /**
     * 根据条件查询审核通过的企业列表
     *
     * @param request
     * @return
     */
    List<EnterpriseDTO> getEnterpriseListByName(QueryEnterpriseByNameRequest request);

    /**
     * 根据ID列表批量获取企业信息
     *
     * @param ids 企业ID列表
     * @return
     */
    Map<Long,EnterpriseDTO> getMapByIds(List<Long> ids);

    /**
     * 更新ERP对接级别
     *
     * @param request
     * @return
     */
    boolean updateErpStatus(UpdateErpStatusRequest request);

    /**
     * 修改企业管理员手机号
     *
     * @param request
     * @return boolean
     * @author xuan.zhou
     * @date 2022/3/8
     **/
    boolean updateManagerMobile(UpdateManagerMobileRequest request);

    /**
     * 企业同步打标识
     *
     * @return
     */
    boolean syncHandlerFlag();
}
