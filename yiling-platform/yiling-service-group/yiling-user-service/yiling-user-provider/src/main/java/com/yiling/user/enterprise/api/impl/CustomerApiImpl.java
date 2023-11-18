package com.yiling.user.enterprise.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.CustomerApi;
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
import com.yiling.user.enterprise.entity.EnterpriseCustomerDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerEasDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerContactService;
import com.yiling.user.enterprise.service.EnterpriseCustomerEasService;
import com.yiling.user.enterprise.service.EnterpriseCustomerPaymentMethodService;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterpriseService;

import io.jsonwebtoken.lang.Assert;

/**
 * 企业客户 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@DubboService
public class CustomerApiImpl implements CustomerApi {

    @Autowired
    EnterpriseCustomerService enterpriseCustomerService;
    @Autowired
    EnterpriseCustomerContactService enterpriseCustomerContactService;
    @Autowired
    EnterpriseCustomerPaymentMethodService enterpriseCustomerPaymentMethodService;
    @Autowired
    EnterpriseCustomerEasService enterpriseCustomerEasService;
    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public Page<EnterpriseCustomerDTO> pageList(QueryCustomerPageListRequest request) {
        Page<EnterpriseCustomerDO> page = enterpriseCustomerService.pageList(request);
        return PojoUtils.map(page, EnterpriseCustomerDTO.class);
    }

    @Override
    public List<EnterpriseCustomerDTO> queryList(QueryCustomerPageListRequest request) {
        List<EnterpriseCustomerDO> list = enterpriseCustomerService.queryList(request);
        return PojoUtils.map(list, EnterpriseCustomerDTO.class);
    }

    @Override
    public EnterpriseCustomerDTO get(Long eid, Long customerEid) {
        return PojoUtils.map(enterpriseCustomerService.get(eid, customerEid), EnterpriseCustomerDTO.class);
    }

    @Override
    public List<EnterpriseCustomerDTO> listByEidAndCustomerEids(Long eid, List<Long> customerEids) {
        return PojoUtils.map(enterpriseCustomerService.listByEidAndCustomerEids(eid, customerEids), EnterpriseCustomerDTO.class);
    }

    @Override
    public List<EnterpriseCustomerDTO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid) {
        return PojoUtils.map(enterpriseCustomerService.listByEidsAndCustomerEid(eids, customerEid), EnterpriseCustomerDTO.class);
    }

    @Override
    public boolean add(AddCustomerRequest request) {
        return enterpriseCustomerService.add(request);
    }

    @Override
    public boolean addB2bCustomer(AddCustomerRequest request) {
        return enterpriseCustomerService.addB2bCustomer(request);
    }

    @Override
    public Map<Long, Long> countGroupCustomers(List<Long> groupIds) {
        return enterpriseCustomerService.countGroupCustomers(groupIds);
    }

    @Override
    public Map<Long, Long> countCustomerContacts(Long eid, List<Long> customerEids) {
        return enterpriseCustomerContactService.countCustomerContacts(eid, customerEids);
    }

    @Override
    public Boolean updateCustomerInfo(UpdateCustomerInfoRequest request) {
        return enterpriseCustomerService.saveChannelCustomer(request);
    }

    @Override
    public List<EnterpriseCustomerEasDTO> getCustomerEasInfos(Long eid, Long customerEid) {
        Assert.notNull(eid, "参数eid不能为空");
        Assert.notNull(customerEid, "参数customerEid不能为空");
        return PojoUtils.map(enterpriseCustomerEasService.listByCustomer(eid, customerEid), EnterpriseCustomerEasDTO.class);
    }

    @Override
    public Map<Long, List<EnterpriseCustomerEasDTO>> listCustomerEasInfos(Long eid, List<Long> customerEids) {
        Assert.notNull(eid, "参数eid不能为空");
        Assert.notEmpty(customerEids, "参数customerEids不能为空");
        return PojoUtils.map(enterpriseCustomerEasService.listCustomerEasInfos(eid, customerEids), EnterpriseCustomerEasDTO.class);
    }

    @Override
    public Page<EnterpriseCustomerEasDTO> queryCustomerEasInfoPageList(QueryCustomerEasInfoPageListRequest request) {
        Assert.notNull(request, "参数request不能为空");
        return PojoUtils.map(enterpriseCustomerEasService.pageList(request), EnterpriseCustomerEasDTO.class);
    }

    @Override
    public Page<EnterpriseCustomerEasDTO> queryCustomerEasInfoPageListByCurrent(QueryCustomerEasInfoPageListByCurrentRequest request) {
        Assert.notNull(request, "参数request不能为空");
        Assert.notNull(request.getOpUserId(), "参数request不能为空");
        return PojoUtils.map(enterpriseCustomerEasService.pageListByCurrent(request), EnterpriseCustomerEasDTO.class);
    }

    @Override
    public boolean addEasInfo(AddCustomerEasInfoRequest request) {
        Assert.notNull(request, "参数request不能为空");
        return enterpriseCustomerEasService.add(request);
    }

    @Override
    public List<EnterpriseCustomerDTO> listByEid(Long eid) {
        Assert.notNull(eid, "参数eid不能为空");
        return PojoUtils.map(enterpriseCustomerService.listByEid(eid), EnterpriseCustomerDTO.class);
    }

    @Override
    public Long getCustomerEidByEasCode(Long eid, String easCode) {
        return enterpriseCustomerEasService.getCustomerEidByEasCode(eid,easCode);
    }

	@Override
	public Boolean updateAppliedAmount(Long eid, String easCode, BigDecimal amount) {
		return enterpriseCustomerEasService.updateAppliedAmount(eid,easCode,amount);
	}

    @Override
    public EnterpriseCustomerDTO listByEidAndCustomerErpCode(Long eid, String customerCode) {
        return enterpriseCustomerService.listByEidAndCustomerErpCode(eid, customerCode);
    }

    @Override
    public Boolean syncUpdateById(UpdateCustomerRequest request) {
        return enterpriseCustomerService.syncUpdateById(request);
    }

    @Override
    public Page<EnterpriseDTO> queryCustomerPageListByContact(QueryCustomerPageListByContactRequest request) {
        return enterpriseCustomerService.queryCustomerPageListByContact(request);
    }

    @Override
    public List<EnterpriseDTO> getEidListByCustomerId(Long customerId) {
        return enterpriseCustomerService.getEidListByCustomerId(customerId);
    }

    @Override
    public Boolean deleteEasInfo(Long id, Long currentUserId) {
        EnterpriseCustomerEasDO customerEasDO = new EnterpriseCustomerEasDO();
        customerEasDO.setId(id);
        customerEasDO.setOpUserId(currentUserId);

        enterpriseCustomerEasService.deleteByIdWithFill(customerEasDO);
        return true;
    }

    @Override
    public Boolean openOfflinePay(UpdateOpenPayRequest request) {
        return enterpriseCustomerPaymentMethodService.openOfflinePay(request);
    }

    @Override
    public Map<Long, List<EnterpriseCustomerPaymentMethodDTO>> listMapCustomerPaymentMethods(Long eid, List<Long> customerEidList, PlatformEnum platformEnum) {
        return PojoUtils.map(enterpriseCustomerPaymentMethodService.listMapCustomerPaymentMethods(eid, customerEidList, platformEnum), EnterpriseCustomerPaymentMethodDTO.class);
    }

    @Override
    public List<Long> getEidListByCustomerEid(QueryCanBuyEidRequest request) {
        return enterpriseCustomerService.getEidListByCustomerEid(request);
    }

    @Override
    public boolean deleteEnterpriseCustomer(Long customerId, Long opUserId) {
        return enterpriseCustomerService.deleteEnterpriseCustomer(customerId, opUserId);
    }


}
