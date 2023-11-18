package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
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
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.service.EnterprisePlatformService;
import com.yiling.user.enterprise.service.EnterpriseSalesAreaDetailService;
import com.yiling.user.enterprise.service.EnterpriseSalesAreaService;
import com.yiling.user.enterprise.service.EnterpriseService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * 企业 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@DubboService
public class EnterpriseApiImpl implements EnterpriseApi {

    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    EnterprisePlatformService enterprisePlatformService;
    @Autowired
    EnterpriseSalesAreaService enterpriseSalesAreaService;
    @Autowired
    EnterpriseSalesAreaDetailService enterpriseSalesAreaDetailService;

    @Override
    public EnterpriseDTO getById(Long id) {
        Assert.notNull(id, "参数id不能为空");

        EnterpriseDO enterpriseDO = enterpriseService.getById(id);
        return PojoUtils.map(enterpriseDO, EnterpriseDTO.class);
    }

    @Override
    public EnterpriseDTO getByName(String name) {
        Assert.notNull(name, "参数name不能为空");

        EnterpriseDO enterpriseDO = enterpriseService.getByName(name);
        return PojoUtils.map(enterpriseDO, EnterpriseDTO.class);
    }

    @Override
    public EnterpriseDTO getByLicenseNumber(String licenseNumber) {
        Assert.notNull(licenseNumber, "参数licenseNumber不能为空");

        EnterpriseDO enterpriseDO = enterpriseService.getByLicenseNumber(licenseNumber);
        return PojoUtils.map(enterpriseDO, EnterpriseDTO.class);
    }

    @Override
    public EnterpriseDTO getByErpCode(String erpCode) {
        Assert.notNull(erpCode, "参数erpCode不能为空");

        EnterpriseDO enterpriseDO = enterpriseService.getByErpCode(erpCode);
        return PojoUtils.map(enterpriseDO, EnterpriseDTO.class);
    }

    @Override
    public List<EnterpriseDTO> listByIds(List<Long> ids) {
        Assert.notEmpty(ids, "参数ids不能为空");

        List<EnterpriseDO> enterpriseDOList = enterpriseService.listByIds(ids);
        if (CollUtil.isEmpty(enterpriseDOList)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(enterpriseDOList, EnterpriseDTO.class);
    }

    @Override
    public List<EnterpriseDTO> listByUserId(Long userId, EnableStatusEnum statusEnum) {
        Assert.notNull(userId, "参数userId不能为空");
        Assert.notNull(statusEnum, "参数statusEnum不能为空");

        List<EnterpriseDO> list = enterpriseService.listByUserId(userId, statusEnum);
        return PojoUtils.map(list, EnterpriseDTO.class);
    }

    @Override
    public Map<Long, List<EnterpriseDTO>> listByContactUserIds(Long eid, List<Long> contactUserIds) {
        Assert.notNull(eid, "参数eid不能为空");
        Assert.notEmpty(contactUserIds, "参数contactUserIds不能为空");
        Map<Long, List<EnterpriseDO>> map = enterpriseService.listByContactUserIds(eid, contactUserIds);
        return PojoUtils.map(map, EnterpriseDTO.class);
    }

    @Override
    public List<EnterpriseDTO> listByParentId(Long parentId) {
        Assert.notNull(parentId, "参数parentId不能为空");

        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseDO::getParentId, parentId);
        return PojoUtils.map(enterpriseService.listByParentId(parentId), EnterpriseDTO.class);
    }

    @Override
    public List<Long> listSubEids(Long eid) {
        return enterpriseService.listSubEids(eid);
    }

    @Override
    public List<Long> listEidsByChannel(EnterpriseChannelEnum enterpriseChannelEnum) {
        return enterpriseService.listEidsByChannel(enterpriseChannelEnum);
    }

    @Override
    public boolean isYilingSubEid(Long eid) {
        return this.listSubEids(Constants.YILING_EID).contains(eid);
    }

    @Override
    public Map<Long, List<EnterpriseDTO>> listByUserIds(List<Long> userIds) {
        Assert.notEmpty(userIds, "参数userIds不能为空");

        Map<Long, List<EnterpriseDO>> userEnterpriseDOList = enterpriseService.listByUserIds(userIds);

        Map<Long, List<EnterpriseDTO>> userEnterpriseDTOList = MapUtil.newHashMap();
        for (Long key : userEnterpriseDOList.keySet()) {
            userEnterpriseDTOList.put(key, PojoUtils.map(userEnterpriseDOList.get(key), EnterpriseDTO.class));
        }
        return userEnterpriseDTOList;
    }

    @Override
    public Page<EnterpriseDTO> pageList(QueryEnterprisePageListRequest request) {
        Page<EnterpriseDO> page = enterpriseService.pageList(request);
        return PojoUtils.map(page, EnterpriseDTO.class);
    }

    @Override
    public Long regist(RegistEnterpriseRequest request) {
        return enterpriseService.regist(request);
    }

    @Override
    public Long create(CreateEnterpriseRequest request) {
        return enterpriseService.create(request);
    }

    @Override
    public boolean update(UpdateEnterpriseRequest request) {
        return enterpriseService.update(request);
    }

    @Override
    public boolean updateType(UpdateEnterpriseTypeRequest request) {
        return enterpriseService.updateType(request);
    }

    @Override
    public boolean updateChannel(UpdateEnterpriseChannelRequest request) {
        return enterpriseService.updateChannel(request);
    }

    @Override
    public boolean updateHmcType(UpdateEnterpriseHmcTypeRequest request) {
        return enterpriseService.updateHmcType(request);
    }

    @Override
    public boolean updateStatus(UpdateEnterpriseStatusRequest request) {
        return enterpriseService.updateStatus(request);
    }

    @Override
    public boolean updateAuthStatus(UpdateEnterpriseAuthRequest request) {
        return enterpriseService.updateAuthStatus(request);
    }

    @Override
    public boolean importData(ImportEnterpriseRequest request) {
        return enterpriseService.importData(request);
    }

    @Override
    public boolean importEnterprisePlatform(ImportEnterprisePlatformRequest request) {
        return enterprisePlatformService.importEnterprisePlatform(request);
    }

    @Override
    public EnterprisePlatformDTO getEnterprisePlatform(Long eid) {
        Assert.notNull(eid, "参数eid不能为空");
        return PojoUtils.map(enterprisePlatformService.getByEid(eid), EnterprisePlatformDTO.class);
    }

    @Override
    public boolean openPlatform(OpenPlatformRequest request) {
        return enterprisePlatformService.openPlatform(request.getEid(), request.getPlatformEnumList(), request.getEnterpriseChannelEnum(), request.getEnterpriseHmcTypeEnum(), request.getOpUserId());
    }

    @Override
    public List<EnterprisePlatformDTO> getEnterprisePlatforms(List<Long> eids) {
        Assert.notEmpty(eids, "参数eids不能为空");
        return PojoUtils.map(enterprisePlatformService.listByEids(eids), EnterprisePlatformDTO.class);
    }

    @Override
    public EnterpriseStatisticsBO quantityStatistics() {
        return enterpriseService.quantityStatistics();
    }

    @Override
    public List<EnterpriseDTO> queryListByArea(QueryEnterpriseListRequest request) {
        return enterpriseService.queryListByArea(request);
    }

    @Override
    public EnterpriseSalesAreaDTO getEnterpriseSalesArea(Long eid) {
        Assert.notNull(eid, "参数eid不能为空");
        return PojoUtils.map(enterpriseSalesAreaService.getByEid(eid), EnterpriseSalesAreaDTO.class);
    }

    @Override
    public List<EnterpriseSalesAreaDTO> listEnterpriseSalesArea(List<Long> eids) {
        Assert.notEmpty(eids, "参数eids不能为空");
        return PojoUtils.map(enterpriseSalesAreaService.listByEids(eids), EnterpriseSalesAreaDTO.class);
    }

    @Override
    public List<EnterpriseSalesAreaDetailDTO> getEnterpriseSaleAreaDetail(Long eid) {
        Assert.notNull(eid, "参数eid不能为空");
        return PojoUtils.map(enterpriseSalesAreaDetailService.getEnterpriseSaleAreaDetail(eid),EnterpriseSalesAreaDetailDTO.class);
    }

    @Override
    public List<EnterpriseDTO> listMainPart() {
        List<EnterpriseDO> list = enterpriseService.listMainPart();
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(list, EnterpriseDTO.class);
    }

    /**
     * 商务局联系人查询我的客户
     * @param request 查询商务联系人参数
     * @return
     */
    @Override
    public Page<EnterpriseDTO> myCustomerPageList(QueryContactorEnterprisePageListRequest request) {

        Assert.notNull(request.getContactUserId(), "商务联系ID不能为空");

        return PojoUtils.map(enterpriseService.myCustomerPageList(request), EnterpriseDTO.class);
    }

    @Override
    public List<EnterpriseDTO> getEnterpriseListByName(QueryEnterpriseByNameRequest request) {
        return enterpriseService.getEnterpriseListByName(request);
    }

    @Override
    public Map<Long, EnterpriseDTO> getMapByIds(List<Long> ids) {
        Assert.notEmpty(ids, "参数ids不能为空");
        List<EnterpriseDO> enterpriseDOList = enterpriseService.listByIds(ids);
        if (CollUtil.isEmpty(enterpriseDOList)) {
            return MapUtil.newHashMap();
        }
        List<EnterpriseDTO> list = PojoUtils.map(enterpriseDOList,EnterpriseDTO.class);

        return list.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity(),(k1,k2)->k2));
    }

    @Override
    public boolean updateErpStatus(UpdateErpStatusRequest request) {
        return enterpriseService.updateErpStatus(request);
    }

    @Override
    public boolean updateManagerMobile(UpdateManagerMobileRequest request) {
        return enterpriseService.updateManagerMobile(request);
    }

    @Override
    public boolean syncHandlerFlag() {
        return enterpriseService.syncHandlerFlag();
    }
}
