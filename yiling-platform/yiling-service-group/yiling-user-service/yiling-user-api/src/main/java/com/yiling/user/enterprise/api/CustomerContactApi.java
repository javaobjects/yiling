package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerContactRequest;
import com.yiling.user.enterprise.dto.request.ImportCustomerContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerContactPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveCustomerContactRequest;
import com.yiling.user.system.dto.UserDTO;

/**
 * 客户商务联系人 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/4
 */
public interface CustomerContactApi {

    /**
     * 查询客户商务联系人分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseCustomerContactDTO> pageList(QueryCustomerContactPageListRequest request);

    /**
     * 添加客户商务联系人
     *
     * @param request
     * @return
     */
    boolean add(AddCustomerContactRequest request);

    /**
     * 移除客户商务联系人
     *
     * @param request
     * @return
     */
    boolean remove(RemoveCustomerContactRequest request);

    /**
     * 获取客户商务联系人列表
     *
     * @param eid 企业ID
     * @param customerEid 客户ID
     * @return
     */
    List<EnterpriseCustomerContactDTO> listByEidAndCustomerEid(Long eid, Long customerEid);

    /**
     * 获取客户上午联系人列表
     * @param eids  企业ID信息
     * @param customerEid 客户ID
     * @return
     */
    List<EnterpriseCustomerContactDTO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid);

    /**
     * 批量获取企业客户商务联系人
     *
     * @param eid 企业ID
     * @param customerEidList 客户ID集合
     * @return key为客户ID，value为客户对应的商务联系人信息
     */
    Map<Long, List<UserDTO>> listByEidAndCustomerEidList(Long eid, List<Long> customerEidList);

    /**
     * 批量导入企业客户商务联系人
     *
     * @param customerContactRequestList
     * @return
     */
    boolean importCustomerContact(List<ImportCustomerContactRequest> customerContactRequestList);
}
