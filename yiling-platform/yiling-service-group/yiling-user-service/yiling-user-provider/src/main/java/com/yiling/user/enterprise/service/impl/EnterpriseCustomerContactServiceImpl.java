package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dao.EnterpriseCustomerContactMapper;
import com.yiling.user.enterprise.dto.request.ImportCustomerContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerContactPageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseCustomerContactDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerPaymentMethodDO;
import com.yiling.user.enterprise.service.EnterpriseCustomerContactService;
import com.yiling.user.enterprise.service.EnterpriseService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 企业客户商务联系人 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Service
public class EnterpriseCustomerContactServiceImpl extends BaseServiceImpl<EnterpriseCustomerContactMapper, EnterpriseCustomerContactDO> implements EnterpriseCustomerContactService {

    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public Page<EnterpriseCustomerContactDO> pageList(QueryCustomerContactPageListRequest request) {
        QueryWrapper<EnterpriseCustomerContactDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerContactDO::getEid, request.getEid())
                .eq(EnterpriseCustomerContactDO::getCustomerEid, request.getCustomerEid());
        return this.page(request.getPage(), queryWrapper);
    }

    @Override
    public Map<Long, Long> countCustomerContacts(Long eid, List<Long> customerEids) {
        QueryWrapper<EnterpriseCustomerContactDO> queryWrapper = new QueryWrapper<>();
        // 针对以岭分公司的特殊处理
        List<Long> yilingSubEids = enterpriseService.listSubEids(Constants.YILING_EID);
        if (yilingSubEids.contains(eid)) {
            queryWrapper.lambda().eq(EnterpriseCustomerContactDO::getEid, Constants.YILING_EID);
        } else {
            queryWrapper.lambda().eq(EnterpriseCustomerContactDO::getEid, eid);
        }
        queryWrapper.lambda().in(EnterpriseCustomerContactDO::getCustomerEid, customerEids);

        List<EnterpriseCustomerContactDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        Map<Long, Long> map = list.stream().collect(Collectors.groupingBy(EnterpriseCustomerContactDO::getCustomerEid, Collectors.counting()));
        customerEids.forEach(e -> {
            if (!map.containsKey(e)) {
                map.put(e, 0L);
            }
        });

        return map;
    }

    @Override
    public List<EnterpriseCustomerContactDO> listByEidAndCustomerEid(Long eid, Long customerEid) {
        QueryWrapper<EnterpriseCustomerContactDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerContactDO::getEid, eid)
                .eq(EnterpriseCustomerContactDO::getCustomerEid, customerEid);

        List<EnterpriseCustomerContactDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<EnterpriseCustomerContactDO> listByEidsAndCustomerEid(List<Long> eids, Long customerEid) {

        QueryWrapper<EnterpriseCustomerContactDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseCustomerContactDO::getEid, eids)
                .eq(EnterpriseCustomerContactDO::getCustomerEid, customerEid);

        List<EnterpriseCustomerContactDO> list = this.list(queryWrapper);

        if (CollUtil.isEmpty(list)) {

            return ListUtil.empty();
        }

        return list;
    }

    @Override
    public Map<Long, List<EnterpriseCustomerContactDO>> listByEidAndContactUserIds(Long eid, List<Long> contactUserIds) {
        QueryWrapper<EnterpriseCustomerContactDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseCustomerContactDO::getEid, eid)
                .in(EnterpriseCustomerContactDO::getContactUserId, contactUserIds);

        List<EnterpriseCustomerContactDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        Map<Long, List<EnterpriseCustomerContactDO>> map = list.stream().collect(Collectors.groupingBy(EnterpriseCustomerContactDO::getContactUserId));
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEnterpriseCustomerContactUserIds(Long eid, Long customerEid, List<Long> contactUserIds, Long opUserId) {
        List<EnterpriseCustomerContactDO> enterpriseCustomerContactDOList = this.listByEidAndCustomerEid(eid, customerEid);
        if (CollUtil.isEmpty(enterpriseCustomerContactDOList)) {
            return this.addEnterpriseCustomerContact(eid, customerEid, contactUserIds, opUserId);
        }

        // 客户原有商务联系人ID列表
        List<Long> originalContactUserIds = enterpriseCustomerContactDOList.stream().map(EnterpriseCustomerContactDO::getContactUserId).distinct().collect(Collectors.toList());

        if (CollUtil.isEmpty(contactUserIds)) {
            return this.removeEnterpriseCustomerContact(eid, customerEid, originalContactUserIds, opUserId);
        }

        // 本次需要移除的商务联系人列表
        List<Long> removeIds = originalContactUserIds.stream().filter(e -> !contactUserIds.contains(e)).collect(Collectors.toList());
        this.removeEnterpriseCustomerContact(eid, customerEid, removeIds, opUserId);

        // 本次需要新增的支付方式ID列表
        List<Long> addIds = contactUserIds.stream().filter(e -> !originalContactUserIds.contains(e)).collect(Collectors.toList());
        this.addEnterpriseCustomerContact(eid, customerEid, addIds, opUserId);

        return true;
    }

    @Override
    public List<EnterpriseCustomerContactDO> listByEidAndCustomerEidList(Long eid, List<Long> customerEidList) {
        LambdaQueryWrapper<EnterpriseCustomerContactDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseCustomerContactDO::getEid, eid);
        queryWrapper.in(EnterpriseCustomerContactDO::getCustomerEid, customerEidList);

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importCustomerContact(List<ImportCustomerContactRequest> customerContactRequestList) {
        if (CollUtil.isEmpty(customerContactRequestList)) {
            return true;
        }

        List<EnterpriseCustomerContactDO> customerContactDOList = PojoUtils.map(customerContactRequestList, EnterpriseCustomerContactDO.class);
        return this.saveBatch(customerContactDOList);
    }

    /**
     * 批量添加企业客户商务联系人
     *
     * @param eid
     * @param customerEid
     * @param contactUserIds
     * @param opUserId
     * @return
     */
    private boolean addEnterpriseCustomerContact(Long eid, Long customerEid, List<Long> contactUserIds, Long opUserId) {
        if (CollUtil.isEmpty(contactUserIds)) {
            return true;
        }

        List<EnterpriseCustomerContactDO> contactDOList = contactUserIds.stream().map(contactUserId -> {
            EnterpriseCustomerContactDO customerContactDO = new EnterpriseCustomerContactDO();
            customerContactDO.setEid(eid);
            customerContactDO.setCustomerEid(customerEid);
            customerContactDO.setContactUserId(contactUserId);
            customerContactDO.setOpUserId(opUserId);
            return customerContactDO;
        }).collect(Collectors.toList());

        return this.saveBatch(contactDOList);
    }

    /**
     * 删除企业客户绑定的商务联系人
     *
     * @param eid
     * @param customerEid
     * @param originalContactUserIds
     * @param opUserId
     * @return
     */
    private boolean removeEnterpriseCustomerContact(Long eid, Long customerEid, List<Long> originalContactUserIds, Long opUserId) {
        if (CollUtil.isEmpty(originalContactUserIds)) {
            return true;
        }

        LambdaQueryWrapper<EnterpriseCustomerContactDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseCustomerContactDO::getEid, eid);
        wrapper.eq(EnterpriseCustomerContactDO::getCustomerEid, customerEid);
        wrapper.in(EnterpriseCustomerContactDO::getContactUserId, originalContactUserIds);

        EnterpriseCustomerContactDO entity = new EnterpriseCustomerContactDO();
        entity.setOpUserId(opUserId);

        return this.batchDeleteWithFill(entity, wrapper) > 0;
    }
}
