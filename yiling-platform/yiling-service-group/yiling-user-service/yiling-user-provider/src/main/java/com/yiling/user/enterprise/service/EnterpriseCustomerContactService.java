package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.request.ImportCustomerContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerContactPageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerContactDO;

/**
 * <p>
 * 企业客户商务联系人 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface EnterpriseCustomerContactService extends BaseService<EnterpriseCustomerContactDO> {

    /**
     * 查询客户商务联系人分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerContactDO> pageList(QueryCustomerContactPageListRequest request);

    /**
     * 统计客户商务联系人个数
     *
     * @param eid 企业ID
     * @param customerEids 客户ID列表
     * @return key：客户ID，value：商务联系人个数
     */
    Map<Long, Long> countCustomerContacts(Long eid, List<Long> customerEids);

    /**
     * 获取客户商务联系人列表
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    List<EnterpriseCustomerContactDO> listByEidAndCustomerEid(Long eid, Long customerEid);

    /**
     * 获取客户商务联系人列表
     * @param eids 企业IDs
     * @param customerEid 客户ID
     * @return
     */
    List<EnterpriseCustomerContactDO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid);

    /**
     * 查询商务联系人负责的企业列表
     *
     * @param eid 企业ID
     * @param contactUserIds 商务联系人ID
     * @return
     */
    Map<Long, List<EnterpriseCustomerContactDO>> listByEidAndContactUserIds(Long eid, List<Long> contactUserIds);

    /**
     * 保存企业客户联系人
     *
     * @param eid
     * @param customerEid
     * @param contactUserIds
     * @param opUserId
     * @return
     */
    boolean saveEnterpriseCustomerContactUserIds(Long eid, Long customerEid, List<Long> contactUserIds, Long opUserId);

    /**
     * 批量获取企业客户商务联系人
     *
     * @param eid
     * @param customerEidList
     * @return
     */
    List<EnterpriseCustomerContactDO> listByEidAndCustomerEidList(Long eid, List<Long> customerEidList);

    /**
     * 批量导入企业客户商务联系人
     *
     * @param customerContactRequestList
     * @return
     */
    boolean importCustomerContact(List<ImportCustomerContactRequest> customerContactRequestList);
}
