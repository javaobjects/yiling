package com.yiling.user.enterprise.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerPaymentMethodDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerEasInfoRequest;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.ImportEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListByCurrentRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListByContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerRequest;
import com.yiling.user.enterprise.dto.request.UpdateOpenPayRequest;

/**
 * 企业客户 API
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
public interface CustomerApi {

    /**
     * 客户管理分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerDTO> pageList(QueryCustomerPageListRequest request);

    /**
     * 查询客户管理列表
     *
     * @param request
     * @return
     */
    List<EnterpriseCustomerDTO> queryList(QueryCustomerPageListRequest request);

    /**
     * 获取企业客户信息
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    EnterpriseCustomerDTO get(Long eid, Long customerEid);

    /**
     * 获取企业客户信息
     *
     * @param eid 企业ID
     * @param customerEids 客户ID集合
     * @return
     */
    List<EnterpriseCustomerDTO> listByEidAndCustomerEids(Long eid, List<Long> customerEids);

    /**
     * 批量获取企业客户信息
     *
     * @param eids 企业ID列表
     * @param customerEid 客户ID
     * @return
     */
    List<EnterpriseCustomerDTO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid);

    /**
     * 添加客户
     *
     * @param request
     * @return
     */
    boolean add(AddCustomerRequest request);

    /**
     * B2B创建订单成功后，添加客户关系
     * @param request
     * @return
     */
    boolean addB2bCustomer(AddCustomerRequest request);

    /**
     * 统计客户分组下的用户数量
     *
     * @param groupIds 客户分组ID列表
     * @return
     */
    Map<Long, Long> countGroupCustomers(List<Long> groupIds);

    /**
     * 统计客户商务联系人个数
     *
     * @param eid 企业ID
     * @param customerEids 客户ID列表
     * @return key：客户ID，value：商务联系人个数
     */
    Map<Long, Long> countCustomerContacts(Long eid, List<Long> customerEids);

    /**
     * 保存客户信息
     *
     * @param request
     * @return
     */
    Boolean updateCustomerInfo(UpdateCustomerInfoRequest request);

    /**
     * 获取客户EAS信息列表
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    List<EnterpriseCustomerEasDTO> getCustomerEasInfos(Long eid, Long customerEid);

    /**
     * 批量获取客户EAS信息列表
     *
     * @param eid 企业ID
     * @param customerEids 客户ID列表
     * @return key：客户ID，value：客户EAS信息列表
     */
    Map<Long, List<EnterpriseCustomerEasDTO>> listCustomerEasInfos(Long eid, List<Long> customerEids);

    /**
     * 查询客户EAS信息分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerEasDTO> queryCustomerEasInfoPageList(QueryCustomerEasInfoPageListRequest request);

    /**
     * 获取商务联系人负责的企业EAS信息分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerEasDTO> queryCustomerEasInfoPageListByCurrent(QueryCustomerEasInfoPageListByCurrentRequest request);

    /**
     * 新增EAS信息
     *
     * @param request
     * @return
     */
    boolean addEasInfo(AddCustomerEasInfoRequest request);

    /**
     * 获取某个企业下的客户信息列表
     *
     * @param eid 企业ID
     * @return
     */
    List<EnterpriseCustomerDTO> listByEid(Long eid);

    /**
     * 通过企业ID和easCode查询对应的企业信息
     * @param eid 企业ID
     * @param easCode eas内码
     * @return
     */
    Long getCustomerEidByEasCode(Long eid,String easCode);

	/**
	 * 根据企业id及easCode更新兑付金额
	 *
	 * @param eid
	 * @param easCode
	 * @param amount
	 * @return
	 */
	Boolean updateAppliedAmount(Long eid, String easCode, BigDecimal amount);

    /**
     * 批量获取企业客户信息
     *
     * @param eid 企业ID
     * @param customerErpCode 客户ERP编码
     * @return
     */
    EnterpriseCustomerDTO listByEidAndCustomerErpCode(Long eid, String customerErpCode);

    /**
     * 更新客户信息
     *
     * @param request
     * @return
     */
    Boolean syncUpdateById(UpdateCustomerRequest request);

    /**
     * 获取商务联系人负责的企业信息分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseDTO> queryCustomerPageListByContact(QueryCustomerPageListByContactRequest request);

    /**
     * 获取某个客户的授信主体企业列表
     *
     * @param customerId 企业ID
     * @return
     */
    List<EnterpriseDTO> getEidListByCustomerId(Long customerId);

    /**
     * 删除Eas信息
     * @param id
     * @param currentUserId
     * @return
     */
    Boolean deleteEasInfo(Long id, Long currentUserId);

    /**
     * 开通线下支付
     *
     * @param request
     */
    Boolean openOfflinePay(UpdateOpenPayRequest request);

    /**
     * 获取批量客户支付方式
     *
     * @param eid 企业ID
     * @param customerEidList 客户企业ID
     * @param platformEnum 平台
     * @return key为客户企业ID，value为对应的支付方式
     */
    Map<Long, List<EnterpriseCustomerPaymentMethodDTO>> listMapCustomerPaymentMethods(Long eid, List<Long> customerEidList, PlatformEnum platformEnum);

    /**
     * 通过客户eid查询建采eid 通过最后采购时间排序
     * @param request
     * @return
     */
    List<Long> getEidListByCustomerEid(QueryCanBuyEidRequest request);

    /**
     * 删除企业客户关系
     *
     * @param customerId 企业客户表ID
     * @param opUserId 操作人ID
     * @return
     */
    boolean deleteEnterpriseCustomer(Long customerId, Long opUserId);
}
