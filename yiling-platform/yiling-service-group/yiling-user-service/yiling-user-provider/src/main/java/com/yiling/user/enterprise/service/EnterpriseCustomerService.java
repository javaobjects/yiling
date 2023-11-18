package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.bo.ChannelCustomerBO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.AddResultGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.MoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListByContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.SaveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;

/**
 * <p>
 * 企业客户信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-21
 */
public interface EnterpriseCustomerService extends BaseService<EnterpriseCustomerDO> {

    /**
     * 客户分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerDO> pageList(QueryCustomerPageListRequest request);

    /**
     * 查询客户分页列表
     *
     * @param request
     * @return
     */
    List<EnterpriseCustomerDO> queryList(QueryCustomerPageListRequest request);

    /**
     * 获取客户信息
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    EnterpriseCustomerDO get(Long eid, Long customerEid);

    /**
     * 批量获取企业客户信息
     * @param eid
     * @param customerEids
     * @return
     */
    List<EnterpriseCustomerDO> listByEidAndCustomerEids(Long eid, List<Long> customerEids);

    /**
     * 批量获取企业客户信息
     *
     * @param eids 企业ID列表
     * @param customerEid 客户ID
     * @return
     */
    List<EnterpriseCustomerDO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid);

    /**
     * 添加客户
     *
     * @param request
     * @return
     */
    boolean add(AddCustomerRequest request);

    /**
     * 添加客户
     * @param request
     * @return 返回企业客户ID
     */
    Long addCustomer(AddCustomerRequest request);

    /**
     * 统计客户分组下的客户数量
     *
     * @param groupIds 客户分组ID列表
     * @return
     */
    Map<Long, Long> countGroupCustomers(List<Long> groupIds);

    /**
     * 向分组中添加客户
     *
     * @param request
     * @return
     */
    boolean addGroupCustomers(SaveGroupCustomersRequest request);

    /**
     * 从分组中移除客户
     *
     * @param request
     * @return
     */
    boolean removeGroupCustomers(RemoveGroupCustomersRequest request);

    /**
     * 移动分组客户
     *
     * @param request
     * @return
     */
    boolean moveGroupCustomers(MoveGroupCustomersRequest request);

    /**
     * 查询企业渠道商分页列表信息(同查询客户)
     * @param request
     * @return
     */
    Page<ChannelCustomerBO> pageChannelCustomerList(QueryChannelCustomerPageListRequest request);

    /**
     * 查询企业渠道商列表信息(同查询客户)
     *
     * @param request
     * @return
     */
    List<ChannelCustomerBO> queryChannelCustomerList(QueryChannelCustomerPageListRequest request);

    /**
     * 获取渠道商详情
     * @param eid
     * @param customerEid
     * @return
     */
    ChannelCustomerBO getChannelCustomer(Long eid, Long customerEid);

    /**
     * 统计企业下客户数量
     *
     * @param eids 企业ID列表
     * @return key：企业ID value：客户数据
     */
    Map<Long, Long> countCustomersByEids(List<Long> eids);

    /**
     * 保存渠道商信息（商务联系人、支付方式、客户分组）
     *
     * @param request
     * @return
     */
    Boolean saveChannelCustomer(UpdateCustomerInfoRequest request);

    /**
     * 获取某个企业下的客户信息列表
     *
     * @param eid 企业ID
     * @return
     */
    List<EnterpriseCustomerDO> listByEid(Long eid);

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
     * @param request
     * @return
     */
    Page<EnterpriseDTO> queryCustomerPageListByContact(QueryCustomerPageListByContactRequest request);

    /**
     * 获取某个客户的授信主体企业列表
     * @param customerId
     * @return
     */
    List<EnterpriseDTO> getEidListByCustomerId(Long customerId);

    /**
     * B2B下单成功后，添加客户关系
     * @param request
     * @return
     */
    boolean addB2bCustomer(AddCustomerRequest request);

    /**
     * 通过客户eid查询建采eid 通过最后采购时间排序
     * @param request
     * @return
     */
    List<Long> getEidListByCustomerEid(QueryCanBuyEidRequest request);

    /**
     * 删除企业客户关系
     *
     * @param customerId
     * @param opUserId
     * @return
     */
    boolean deleteEnterpriseCustomer(Long customerId, Long opUserId);
}
