package com.yiling.user.enterprise.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.tianyancha.api.TycEnterpriseApi;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;
import com.yiling.basic.tianyancha.enums.TycErrorCode;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.bo.EnterpriseStatisticsBO;
import com.yiling.user.enterprise.dao.EnterpriseMapper;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.CreateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseCertificateRequest;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.ImportEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.QueryContactorEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.RegistEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseCertificateRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseChannelRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseHmcTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseStatusRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateErpStatusRequest;
import com.yiling.user.enterprise.dto.request.UpdateManagerMobileRequest;
import com.yiling.user.enterprise.entity.EnterpriseCertificateDO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerContactDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.entity.EnterpriseManagerChangeLogDO;
import com.yiling.user.enterprise.enums.EnterpriseAuthSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.service.DeliveryAddressService;
import com.yiling.user.enterprise.service.EnterpriseAuthInfoService;
import com.yiling.user.enterprise.service.EnterpriseCertificateAuthInfoService;
import com.yiling.user.enterprise.service.EnterpriseCertificateService;
import com.yiling.user.enterprise.service.EnterpriseCustomerContactService;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.enterprise.service.EnterpriseManagerChangeLogService;
import com.yiling.user.enterprise.service.EnterprisePlatformService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.CreateStaffRequest;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.dto.request.UpdateUserStatusRequest;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.entity.UserRoleDO;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.enums.RoleTypeEnum;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.system.service.RoleMenuService;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserAuthsService;
import com.yiling.user.system.service.UserRoleService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CreditCodeUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业信息表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Slf4j
@Service
public class EnterpriseServiceImpl extends BaseServiceImpl<EnterpriseMapper, EnterpriseDO> implements EnterpriseService {

    @Value("${user.staff.defaultPwd}")
    private String defaultPwd;

    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    private EnterpriseManagerChangeLogService enterpriseManagerChangeLogService;
    @Autowired
    private EnterpriseCertificateService enterpriseCertificateService;
    @Autowired
    private EnterprisePlatformService enterprisePlatformService;
    @Autowired
    private DeliveryAddressService deliveryAddressService;
    @Autowired
    private EnterpriseCustomerService enterpriseCustomerService;
    @Autowired
    private EnterpriseCustomerContactService enterpriseCustomerContactService;
    @Autowired
    private EnterpriseAuthInfoService enterpriseAuthInfoService;
    @Autowired
    private EnterpriseCertificateAuthInfoService enterpriseCertificateAuthInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthsService userAuthsService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @DubboReference
    private TycEnterpriseApi tycEnterpriseApi;

    @Autowired
    private RedisDistributedLock redisDistributedLock;
    @Resource
    private DataSourceTransactionManager transactionManager;

    @Override
    public EnterpriseDO getByName(String name) {
        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseDO::getName, name).last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public EnterpriseDO getByLicenseNumber(String licenseNumber) {
        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseDO::getLicenseNumber, licenseNumber).last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public EnterpriseDO getByErpCode(String erpCode) {
        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseDO::getErpCode, erpCode).last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EnterpriseDO> listByUserId(Long userId, EnableStatusEnum statusEnum) {
        List<EnterpriseEmployeeDO> list = enterpriseEmployeeService.listByUserId(userId, statusEnum);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        List<Long> eids = list.stream().map(EnterpriseEmployeeDO::getEid).distinct().collect(Collectors.toList());
        List<EnterpriseDO> enterpriseDOList = this.listByIds(eids);

        if (statusEnum == EnableStatusEnum.ENABLED) {
            return enterpriseDOList.stream().filter(e -> e.getStatus().equals(EnterpriseStatusEnum.ENABLED.getCode())).collect(Collectors.toList());
        } else if (statusEnum == EnableStatusEnum.DISABLED) {
            return enterpriseDOList.stream().filter(e -> e.getStatus().equals(EnterpriseStatusEnum.DISABLED.getCode())).collect(Collectors.toList());
        } else {
            return enterpriseDOList;
        }
    }

    @Override
    public List<EnterpriseDO> listByParentId(Long parentId) {
        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseDO::getParentId, parentId);
        List<EnterpriseDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<Long> listSubEids(Long eid) {
        List<EnterpriseDO> list = this.listByParentId(eid);
        return list.stream().map(EnterpriseDO::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listEidsByChannel(EnterpriseChannelEnum enterpriseChannelEnum) {
        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseDO::getChannelId, enterpriseChannelEnum.getCode());

        List<EnterpriseDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(EnterpriseDO::getId).distinct().collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<EnterpriseDO>> listByUserIds(List<Long> userIds) {
        List<EnterpriseEmployeeDO> list = enterpriseEmployeeService.listByUserIds(userIds);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        List<Long> eids = list.stream().map(EnterpriseEmployeeDO::getEid).distinct().collect(Collectors.toList());
        List<EnterpriseDO> enterpriseDOList = this.listByIds(eids);
        if (CollUtil.isEmpty(enterpriseDOList)) {
            return MapUtil.empty();
        }

        Map<Long, List<EnterpriseEmployeeDO>> userEnterpriseEmployeeMap = list.stream().collect(Collectors.groupingBy(EnterpriseEmployeeDO::getUserId));

        Map<Long, List<EnterpriseDO>> userEnterpriseMap = MapUtil.newHashMap();
        for (Long userId : userEnterpriseEmployeeMap.keySet()) {
            List<Long> userEids = userEnterpriseEmployeeMap.get(userId).stream().map(EnterpriseEmployeeDO::getEid).distinct().collect(Collectors.toList());
            List<EnterpriseDO> userEnterpriseDOList = enterpriseDOList.stream().filter(e -> userEids.contains(e.getId())).collect(Collectors.toList());
            userEnterpriseMap.put(userId, userEnterpriseDOList);
        }
        return userEnterpriseMap;
    }

    @Override
    public Map<Long, List<EnterpriseDO>> listByContactUserIds(Long eid, List<Long> contactUserIds) {
        Map<Long, List<EnterpriseCustomerContactDO>> map = enterpriseCustomerContactService.listByEidAndContactUserIds(eid, contactUserIds);
        if (CollUtil.isEmpty(map)) {
            return MapUtil.empty();
        }

        List<Long> customerEids = CollUtil.newArrayList();
        for (Long key : map.keySet()) {
            customerEids.addAll(map.get(key).stream().map(EnterpriseCustomerContactDO::getCustomerEid).collect(Collectors.toList()));
        }

        List<EnterpriseDO> enterpriseDOList = this.listByIds(customerEids);
        Map<Long, EnterpriseDO> enterpriseDOMap = enterpriseDOList.stream().collect(Collectors.toMap(EnterpriseDO::getId, Function.identity()));

        Map<Long, List<EnterpriseDO>> result = CollUtil.newHashMap();
        for (Long key : map.keySet()) {
            List<EnterpriseDO> list = CollUtil.newArrayList();
            for (EnterpriseCustomerContactDO customerContactDO : map.get(key)) {
                 list.add(enterpriseDOMap.get(customerContactDO.getCustomerEid()));
            }
            result.put(key, list);
        }
        return result;
    }

    @Override
    public Page<EnterpriseDO> pageList(QueryEnterprisePageListRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public List<EnterpriseDO> queryList(QueryEnterprisePageListRequest request) {
        return this.baseMapper.pageList(request);
    }

    @Override
    @GlobalTransactional
    public Long regist(RegistEnterpriseRequest request) {
        CreateEnterpriseRequest request1 = new CreateEnterpriseRequest();
        request1.setName(request.getName());
        request1.setContactor(request.getContactor());
        request1.setContactorPhone(request.getContactorPhone());
        request1.setLicenseNumber(request.getLicenseNumber());
        request1.setProvinceCode(request.getProvinceCode());
        request1.setProvinceName(request.getProvinceName());
        request1.setCityCode(request.getCityCode());
        request1.setCityName(request.getCityName());
        request1.setRegionCode(request.getRegionCode());
        request1.setRegionName(request.getRegionName());
        request1.setAddress(request.getAddress());
        request1.setType(request.getType());
        request1.setSource(request.getEnterpriseSourceEnum().getCode());
        request1.setAuthStatus(EnterpriseAuthStatusEnum.UNAUTH.getCode());
        request1.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        request1.setCertificateList(Lists.newArrayList());
        request1.setPassword(request.getPassword());
        request1.setOpUserId(0L);
        Long eid = this.create(request1);

        // 生成审核任务
        {
            EnterpriseAuthInfoRequest enterpriseAuthInfoRequest = new EnterpriseAuthInfoRequest();
            enterpriseAuthInfoRequest.setEid(eid);
            enterpriseAuthInfoRequest.setName(request.getName());
            enterpriseAuthInfoRequest.setLicenseNumber(request.getLicenseNumber());
            enterpriseAuthInfoRequest.setContactor(request.getContactor());
            enterpriseAuthInfoRequest.setContactorPhone(request.getContactorPhone());
            enterpriseAuthInfoRequest.setProvinceCode(request.getProvinceCode());
            enterpriseAuthInfoRequest.setProvinceName(request.getProvinceName());
            enterpriseAuthInfoRequest.setCityCode(request.getCityCode());
            enterpriseAuthInfoRequest.setCityName(request.getCityName());
            enterpriseAuthInfoRequest.setRegionCode(request.getRegionCode());
            enterpriseAuthInfoRequest.setRegionName(request.getRegionName());
            enterpriseAuthInfoRequest.setAddress(request.getAddress());
            enterpriseAuthInfoRequest.setOpUserId(request.getOpUserId());
            if (request.getEnterpriseSourceEnum() == EnterpriseSourceEnum.B2B_APP_REGIST) {
                enterpriseAuthInfoRequest.setSource(EnterpriseAuthSourceEnum.B2B.getCode());
            }
            Long enterpriseAuthId = enterpriseAuthInfoService.addEnterpriseAuth(enterpriseAuthInfoRequest);

            List<EnterpriseCertificateAuthInfoRequest> authInfoRequests = PojoUtils.map(request.getCertificateList(), EnterpriseCertificateAuthInfoRequest.class);
            authInfoRequests.forEach(enterpriseCertificateAuthInfoRequest ->{
                enterpriseCertificateAuthInfoRequest.setEnterpriseAuthId(enterpriseAuthId);
                enterpriseCertificateAuthInfoRequest.setEid(eid);
            });
            enterpriseCertificateAuthInfoService.addEnterpriseCertificateAuth(authInfoRequests);
        }

        return eid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(CreateEnterpriseRequest request) {
        String licenseNumber = request.getLicenseNumber();
        EnterpriseDO enterpriseDO = this.getByLicenseNumber(licenseNumber);
        if (enterpriseDO != null) {
            throw new BusinessException(UserErrorCode.ENTERPRISE_LICENSE_NUMBER_EXISTS);
        }

        EnterpriseDO entity = new EnterpriseDO();
        entity.setName(request.getName());
        entity.setContactor(request.getContactor());
        entity.setContactorPhone(request.getContactorPhone());
        entity.setLicenseNumber(request.getLicenseNumber());
        entity.setProvinceCode(request.getProvinceCode());
        entity.setProvinceName(request.getProvinceName());
        entity.setCityCode(request.getCityCode());
        entity.setCityName(request.getCityName());
        entity.setRegionCode(request.getRegionCode());
        entity.setRegionName(request.getRegionName());
        entity.setAddress(request.getAddress());
        entity.setParentId(request.getParentId());
        entity.setType(request.getType());
        entity.setSource(request.getSource());
        entity.setAuthStatus(request.getAuthStatus());
        entity.setAuthUser(request.getAuthUser());
        entity.setAuthTime(request.getAuthTime());
        entity.setStatus(request.getStatus());
        entity.setRemark(request.getRemark());
        entity.setOpUserId(request.getOpUserId());
        // 终端类型的客户，如果没有联系人手机号，则生成虚拟号
        String contactorPhone = request.getContactorPhone();
        EnterpriseTypeEnum enterpriseTypeEnum = EnterpriseTypeEnum.getByCode(request.getType());
        if (enterpriseTypeEnum.isTerminal()) {
            if (StrUtil.isEmpty(contactorPhone) || !ReUtil.isMatch(Constants.REGEXP_MOBILE, contactorPhone)) {
                entity.setContactorPhone(userService.generateSpecialPhone());
            }
        } else if (StrUtil.isEmpty(contactorPhone)) {
            throw new BusinessException(ResultCode.FAILED, "联系人手机号不能为空");
        }
        // 保存
        this.save(entity);

        // 企业账号
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setEid(entity.getId());
        createEmployeeRequest.setName(entity.getContactor());
        createEmployeeRequest.setMobile(entity.getContactorPhone());
        createEmployeeRequest.setPassword(request.getPassword());
        createEmployeeRequest.setAdminFlag(1);
        createEmployeeRequest.setOpUserId(request.getOpUserId());
        enterpriseEmployeeService.create(createEmployeeRequest);

        // 企业资质
        List<CreateEnterpriseCertificateRequest> certificateList = request.getCertificateList();
        if (CollUtil.isNotEmpty(certificateList)) {
            certificateList.forEach(e -> {
                EnterpriseCertificateDO enterpriseCertificateEntity = PojoUtils.map(e, EnterpriseCertificateDO.class);
                enterpriseCertificateEntity.setOpUserId(request.getOpUserId());
                enterpriseCertificateEntity.setEid(entity.getId());
                enterpriseCertificateService.save(enterpriseCertificateEntity);
            });
        }

        // 创建收货地址
        this.createDeliveryAddress(entity, request.getOpUserId());

        // 给企业开通平台
        List<PlatformEnum> platformEnumList = request.getPlatformEnumList();
        if (CollUtil.isNotEmpty(platformEnumList)) {
            enterprisePlatformService.openPlatform(entity.getId(), platformEnumList, EnterpriseChannelEnum.getByCode(request.getChannelId()), request.getHmcTypeEnum(), request.getOpUserId());
        }

        return entity.getId();
    }

    /**
     * 创建收货地址
     *
     * @param entity
     * @param opUserId
     * @return
     */
    private boolean createDeliveryAddress(EnterpriseDO entity, Long opUserId) {
        AddDeliveryAddressRequest addDeliveryAddressRequest = new AddDeliveryAddressRequest();
        addDeliveryAddressRequest.setEid(entity.getId());
        addDeliveryAddressRequest.setReceiver(entity.getContactor());
        addDeliveryAddressRequest.setMobile(entity.getContactorPhone());
        addDeliveryAddressRequest.setProvinceCode(entity.getProvinceCode());
        addDeliveryAddressRequest.setProvinceName(entity.getProvinceName());
        addDeliveryAddressRequest.setCityCode(entity.getCityCode());
        addDeliveryAddressRequest.setCityName(entity.getCityName());
        addDeliveryAddressRequest.setRegionCode(entity.getRegionCode());
        addDeliveryAddressRequest.setRegionName(entity.getRegionName());
        addDeliveryAddressRequest.setAddress(entity.getAddress());
        addDeliveryAddressRequest.setDefaultFlag(1);
        addDeliveryAddressRequest.setOpUserId(opUserId);
        return deliveryAddressService.add(addDeliveryAddressRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UpdateEnterpriseRequest request) {
        String licenseNumber = request.getLicenseNumber();
        EnterpriseDO enterpriseDO = this.getByLicenseNumber(licenseNumber);
        if (enterpriseDO != null && !enterpriseDO.getId().equals(request.getId())) {
            throw new BusinessException(UserErrorCode.ENTERPRISE_LICENSE_NUMBER_EXISTS);
        }

        EnterpriseDO entity = PojoUtils.map(request, EnterpriseDO.class);
        this.updateById(entity);

        List<UpdateEnterpriseCertificateRequest> certificateList = request.getCertificateList();
        if (CollUtil.isNotEmpty(certificateList)) {
            certificateList.forEach(e -> {
                if (StrUtil.isNotEmpty(e.getFileKey())) {
                    EnterpriseCertificateDO enterpriseCertificateEntity = PojoUtils.map(e, EnterpriseCertificateDO.class);
                    enterpriseCertificateEntity.setOpUserId(request.getOpUserId());
                    enterpriseCertificateService.saveOrUpdate(enterpriseCertificateEntity);
                } else {
                    EnterpriseCertificateDO enterpriseCertificateEntity = new EnterpriseCertificateDO();
                    enterpriseCertificateEntity.setId(e.getId());
                    enterpriseCertificateEntity.setOpUserId(request.getOpUserId());
                    enterpriseCertificateService.deleteByIdWithFill(enterpriseCertificateEntity);
                }
            });
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateType(UpdateEnterpriseTypeRequest request) {
        EnterpriseDO enterpriseDO = this.getById(request.getEid());
        if (request.getEnterpriseTypeEnum() == EnterpriseTypeEnum.getByCode(enterpriseDO.getType())) {
            log.info("企业类型未做更改。eid={}, type={}", enterpriseDO.getId(), enterpriseDO.getType());
            return true;
        }

        log.info("准备修改企业类型：eid={}, type={}, newType={}", enterpriseDO.getId(), enterpriseDO.getType(), request.getEnterpriseTypeEnum().getCode());

        // 关闭所有平台
        enterprisePlatformService.closePlatform(request.getEid(), ListUtil.toList(PlatformEnum.B2B, PlatformEnum.POP, PlatformEnum.SALES_ASSIST, PlatformEnum.HMC), request.getOpUserId());

        // 修改企业类型
        EnterpriseDO entity = new EnterpriseDO();
        entity.setId(request.getEid());
        entity.setType(request.getEnterpriseTypeEnum().getCode());
        entity.setOpUserId(request.getOpUserId());
        this.updateById(entity);

        log.info("企业类型修改完毕");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateChannel(UpdateEnterpriseChannelRequest request) {
        EnterpriseDO enterpriseDO = this.getById(request.getEid());
        EnterpriseChannelEnum enterpriseChannelEnum = EnterpriseChannelEnum.getByCode(enterpriseDO.getChannelId());
        if (enterpriseChannelEnum == null) {
            throw new BusinessException(ResultCode.FAILED, "原渠道类型不能为空");
        } else if (request.getEnterpriseChannelEnum() == enterpriseChannelEnum) {
            log.info("企业渠道未做更改。eid={}, channelId={}", enterpriseDO.getId(), enterpriseDO.getChannelId());
            return true;
        }

        log.info("准备修改企业渠道：eid={}, channelId={}, newChannelId={}", enterpriseDO.getId(), enterpriseDO.getChannelId(), request.getEnterpriseChannelEnum().getCode());

        // 管理员换绑角色
        {
            // 管理员信息
            List<EnterpriseEmployeeDO> enterpriseEmployeeDOList = enterpriseEmployeeService.listAdminsByEid(request.getEid());
            // 给管理员绑定新角色
            final Long newRoleId = request.getEnterpriseChannelEnum().getRoleId();
            enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                log.info("绑定管理员角色：appId={}, eid={}, userId={}, roleId={}", PermissionAppEnum.MALL_ADMIN_POP.getCode(), request.getEid(), enterpriseEmployeeDO.getUserId(), newRoleId);
                userRoleService.updateUserRoles(PermissionAppEnum.MALL_ADMIN_POP, request.getEid(), enterpriseEmployeeDO.getUserId(), ListUtil.toList(newRoleId), request.getOpUserId());
            });
        }

        // 清空企业商家后台自定义角色权限
        List<RoleDO> roleDOList = roleService.listByEid(PermissionAppEnum.MALL_ADMIN, request.getEid());
        List<RoleDO> customRoleDOList = roleDOList.stream().filter(e -> RoleTypeEnum.getByCode(e.getType()) == RoleTypeEnum.CUSTOM).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(customRoleDOList)) {
            customRoleDOList.forEach(roleDO -> {
                log.info("清空企业自定义角色权限：roleId={}", roleDO.getId());
                roleMenuService.updateRoleMenus(roleDO.getId(), ListUtil.empty(), request.getOpUserId());
            });
        }

        // 修改企业渠道
        EnterpriseDO entity = new EnterpriseDO();
        entity.setId(request.getEid());
        entity.setChannelId(request.getEnterpriseChannelEnum().getCode());
        entity.setOpUserId(request.getOpUserId());
        this.updateById(entity);

        log.info("企业渠道修改完毕");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHmcType(UpdateEnterpriseHmcTypeRequest request) {
        EnterpriseDO enterpriseDO = this.getById(request.getEid());
        EnterpriseHmcTypeEnum enterpriseHmcTypeEnum = EnterpriseHmcTypeEnum.getByCode(enterpriseDO.getHmcType());
        if (enterpriseHmcTypeEnum == null) {
            throw new BusinessException(ResultCode.FAILED, "原HMC类型不能为空");
        } else if (request.getEnterpriseHmcTypeEnum() == enterpriseHmcTypeEnum) {
            log.info("企业HMC类型未做更改。eid={}, hmcType={}", enterpriseDO.getId(), enterpriseDO.getHmcType());
            return true;
        }

        EnterpriseTypeEnum enterpriseTypeEnum = EnterpriseTypeEnum.getByCode(enterpriseDO.getType());
        if (request.getEnterpriseHmcTypeEnum() == EnterpriseHmcTypeEnum.MEDICINE_INSURANCE || request.getEnterpriseHmcTypeEnum() == EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK) {
            if (!enterpriseTypeEnum.isTerminal()) {
                throw new BusinessException(ResultCode.FAILED, "只有终端才能开通此业务类型");
            }
        } else {
            if (!(enterpriseTypeEnum.isIndustry() || enterpriseTypeEnum.isBusiness())) {
                throw new BusinessException(ResultCode.FAILED, "只有工业或商业才能开通此业务类型");
            }
        }

        log.info("准备修改企业HMC类型：eid={}, hmcType={}, newHmcType={}", enterpriseDO.getId(), enterpriseDO.getHmcType(), request.getEnterpriseHmcTypeEnum().getCode());

        // 管理员换绑角色
        {
            // 管理员信息
            List<EnterpriseEmployeeDO> enterpriseEmployeeDOList = enterpriseEmployeeService.listAdminsByEid(request.getEid());
            // 给管理员绑定新角色
            List<String> newRoleCodeList = request.getEnterpriseHmcTypeEnum().getRoleCodeList();
            List<RoleDO> newRoleDOList = roleService.getByCodeList(newRoleCodeList);
            List<Long> newRoleIdList = newRoleDOList.stream().map(RoleDO::getId).distinct().collect(Collectors.toList());
            enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                log.info("绑定管理员角色：appId={}, eid={}, userId={}, roleIdList={}", PermissionAppEnum.MALL_ADMIN_HMC.getCode(), request.getEid(), enterpriseEmployeeDO.getUserId(), newRoleIdList);
                userRoleService.updateUserRoles(PermissionAppEnum.MALL_ADMIN_HMC, request.getEid(), enterpriseEmployeeDO.getUserId(), newRoleIdList, request.getOpUserId());
            });
        }

        // 清空企业商家后台自定义角色权限
        List<RoleDO> roleDOList = roleService.listByEid(PermissionAppEnum.MALL_ADMIN, request.getEid());
        List<RoleDO> customRoleDOList = roleDOList.stream().filter(e -> RoleTypeEnum.getByCode(e.getType()) == RoleTypeEnum.CUSTOM).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(customRoleDOList)) {
            customRoleDOList.forEach(roleDO -> {
                log.info("清空企业自定义角色权限：roleId={}", roleDO.getId());
                roleMenuService.updateRoleMenus(roleDO.getId(), ListUtil.empty(), request.getOpUserId());
            });
        }

        EnterpriseDO entity = new EnterpriseDO();
        entity.setId(request.getEid());
        entity.setHmcType(request.getEnterpriseHmcTypeEnum().getCode());
        entity.setOpUserId(request.getOpUserId());
        this.updateById(entity);

        log.info("企业HMC类型修改完毕");
        return true;
    }

    @Override
    public boolean updateStatus(UpdateEnterpriseStatusRequest request) {
        EnterpriseDO entity = new EnterpriseDO();
        entity.setId(request.getId());
        entity.setStatus(request.getStatus());
        entity.setOpUserId(request.getOpUserId());
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importData(ImportEnterpriseRequest request) {
        CreateEnterpriseRequest createEnterpriseRequest = new CreateEnterpriseRequest();
        createEnterpriseRequest.setName(request.getName());
        createEnterpriseRequest.setContactor(request.getContactor());
        createEnterpriseRequest.setContactorPhone(request.getContactorPhone());
        createEnterpriseRequest.setLicenseNumber(request.getLicenseNumber());
        createEnterpriseRequest.setProvinceCode(request.getProvinceCode());
        createEnterpriseRequest.setProvinceName(request.getProvinceName());
        createEnterpriseRequest.setCityCode(request.getCityCode());
        createEnterpriseRequest.setCityName(request.getCityName());
        createEnterpriseRequest.setRegionCode(request.getRegionCode());
        createEnterpriseRequest.setRegionName(request.getRegionName());
        createEnterpriseRequest.setAddress(request.getAddress());
        createEnterpriseRequest.setParentId(request.getParentId());
        createEnterpriseRequest.setType(request.getType());
        createEnterpriseRequest.setSource(EnterpriseSourceEnum.IMPORT.getCode());
        createEnterpriseRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        createEnterpriseRequest.setAuthUser(request.getOpUserId());
        createEnterpriseRequest.setAuthTime(new Date());
        createEnterpriseRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        createEnterpriseRequest.setOpUserId(request.getOpUserId());
        // 创建企业信息
        Long eid = this.create(createEnterpriseRequest);

        // 给企业开通平台
        List<PlatformEnum> platformEnumList = request.getPlatformEnumList();
        if (CollUtil.isNotEmpty(platformEnumList)) {
            enterprisePlatformService.openPlatform(eid, platformEnumList, EnterpriseChannelEnum.getByCode(request.getChannelId()), request.getHmcTypeEnum(), request.getOpUserId());

            // 如果开通POP（有渠道类型），则将其生成为以岭的客户
            if (platformEnumList.contains(PlatformEnum.POP)) {
                AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
                addCustomerRequest.setEid(Constants.YILING_EID);
                addCustomerRequest.setCustomerEid(eid);
                addCustomerRequest.setCustomerName(request.getName());
                addCustomerRequest.setSource(EnterpriseSourceEnum.IMPORT.getCode());
                addCustomerRequest.setAddPurchaseRelationFlag(false);
                addCustomerRequest.setOpUserId(request.getOpUserId());
                enterpriseCustomerService.add(addCustomerRequest);
            }
        }

        return true;
    }

    @Override
    public EnterpriseStatisticsBO quantityStatistics() {
        EnterpriseStatisticsBO enterpriseStatisticsBO = this.baseMapper.quantityStatistics();
        if (enterpriseStatisticsBO == null) {
            enterpriseStatisticsBO = new EnterpriseStatisticsBO();
            enterpriseStatisticsBO.setTotalCount(0L);
            enterpriseStatisticsBO.setIndustryCount(0L);
            enterpriseStatisticsBO.setBusinessCount(0L);
            enterpriseStatisticsBO.setEnabledCount(0L);
            enterpriseStatisticsBO.setDisabledCount(0L);
            enterpriseStatisticsBO.setTerminalCount(0L);
            enterpriseStatisticsBO.setHospitalCount(0L);
            enterpriseStatisticsBO.setChainBaseCount(0L);
            enterpriseStatisticsBO.setChainDirectCount(0L);
            enterpriseStatisticsBO.setChainJoinCount(0L);
            enterpriseStatisticsBO.setPharmacyCount(0L);
            enterpriseStatisticsBO.setClinicCount(0L);
            enterpriseStatisticsBO.setPrivateHospitalCount(0L);
            enterpriseStatisticsBO.setLevel3HospitalCount(0L);
            enterpriseStatisticsBO.setLevel2HospitalCount(0L);
            enterpriseStatisticsBO.setCommunityCenterCount(0L);
            enterpriseStatisticsBO.setHealthCenterCount(0L);
            enterpriseStatisticsBO.setCommunityStationCount(0L);
            enterpriseStatisticsBO.setPeopleHospitalCount(0L);
        }
        return enterpriseStatisticsBO;
    }

    @Override
    public List<EnterpriseDTO> queryListByArea(QueryEnterpriseListRequest request) {
        QueryWrapper<EnterpriseDO> queryWrapper = WrapperUtils.getWrapper(request);
        // 当前维度查询数据只需要返回企业ID
        queryWrapper.lambda().select(EnterpriseDO::getId);

        return PojoUtils.map(list(queryWrapper),EnterpriseDTO.class);
    }

    @Override
    public Page<EnterpriseDO> myCustomerPageList(QueryContactorEnterprisePageListRequest request) {

        return this.getBaseMapper().myCustomerPageList(request.getPage(),request);
    }

    @Override
    public List<EnterpriseDO> listMainPart() {
        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnterpriseDO::getChannelId, EnterpriseChannelEnum.INDUSTRY.getCode())
                .ne(EnterpriseDO::getParentId, 0);

        return this.list(queryWrapper);
    }

    @Override
    public boolean updateAuthStatus(UpdateEnterpriseAuthRequest request) {
        EnterpriseDO enterpriseDO = Optional.ofNullable(getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        if(EnterpriseAuthStatusEnum.UNAUTH == EnterpriseAuthStatusEnum.getByCode(request.getAuthStatus())){
            throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_STATUS_ERROR);

        }

        EnterpriseDO enterprise = new EnterpriseDO();
        enterprise.setId(enterpriseDO.getId());
        enterprise.setOpUserId(request.getOpUserId());

        if(EnterpriseAuthStatusEnum.AUTH_SUCCESS == EnterpriseAuthStatusEnum.getByCode(request.getAuthStatus())){
            enterprise.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            enterprise.setAuthRejectReason("");
        } else {
            enterprise.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_FAIL.getCode());
            enterprise.setAuthRejectReason(request.getAuthRejectReason());
        }
        enterprise.setAuthTime(new Date());
        enterprise.setAuthUser(request.getOpUserId());

        return this.updateById(enterprise);
    }

    @Override
    public List<EnterpriseDTO> getEnterpriseListByName(QueryEnterpriseByNameRequest request) {
        LambdaQueryWrapper<EnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        // 默认查审核通过的企业
        queryWrapper.eq(EnterpriseDO::getAuthStatus,EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());

        if (Objects.nonNull(request.getId()) && request.getId() != 0) {
            queryWrapper.eq(EnterpriseDO::getId, request.getId());
        }
        if (Objects.nonNull(request.getType()) && request.getType() != 0){
            queryWrapper.eq(EnterpriseDO::getType,request.getType());
        } else if (CollUtil.isNotEmpty(request.getTypeList())) {
            queryWrapper.in(EnterpriseDO::getType, request.getTypeList());
        }
        if (CollUtil.isNotEmpty(request.getErpSyncLevelList())) {
            queryWrapper.in(EnterpriseDO::getErpSyncLevel,request.getErpSyncLevelList());
        }
        if (StrUtil.isNotEmpty(request.getName())) {
            queryWrapper.like(EnterpriseDO::getName,request.getName());
        }
        if (Objects.nonNull(request.getChannelId()) && request.getChannelId() != 0) {
            queryWrapper.eq(EnterpriseDO::getChannelId, request.getChannelId());
        }
        if (Objects.nonNull(request.getStatus()) && request.getStatus() != 0) {
            queryWrapper.eq(EnterpriseDO::getStatus, request.getStatus());
        }
        if (Objects.nonNull(request.getAuthStatus()) && request.getAuthStatus() != 0) {
            queryWrapper.eq(EnterpriseDO::getAuthStatus, request.getAuthStatus());
        }
        if (Objects.isNull(request.getLimit()) || request.getLimit() == 0) {
            queryWrapper.last("limit 100");
        } else {
            queryWrapper.last("limit " + request.getLimit());
        }

        return PojoUtils.map(this.list(queryWrapper),EnterpriseDTO.class);
    }

    @Override
    public boolean updateErpStatus(UpdateErpStatusRequest request) {
        EnterpriseDO enterpriseDO = PojoUtils.map(request,EnterpriseDO.class);
        return this.updateById(enterpriseDO);
    }

    @Override
    public boolean updateManagerMobile(UpdateManagerMobileRequest request) {
        log.info("准备更换企业管理员账号：{}", JSONUtil.toJsonStr(request));

        // 获取锁，lockName=updateManagerMobile_eid_userId
        String lockName = StrUtil.format("updateManagerMobile_{}_{}", request.getEid(), request.getUserId());
        String lockId = redisDistributedLock.lock2(lockName, 3, 3, TimeUnit.SECONDS);
        try {
            boolean isAdmin = enterpriseEmployeeService.isAdmin(request.getEid(), request.getUserId());
            if (!isAdmin) {
                log.error("用户不是企业的管理员：eid={}, userId={}", request.getEid(), request.getUserId());
                throw new BusinessException(ResultCode.FAILED, "用户不是企业的管理员");
            }

            // 原账号用户信息
            Staff originStaff = staffService.getById(request.getUserId());
            // 原账号对应的企业数量
            int originStaffEnterpriseCount = 0;
            if (UserStatusEnum.getByCode(originStaff.getStatus()) != UserStatusEnum.DEREGISTER) {
                originStaffEnterpriseCount = enterpriseEmployeeService.listByUserIds(ListUtil.toList(request.getUserId())).size();
            }
            // 判断原账号手机号是否为虚拟号
            boolean isVirtualMobile = !ReUtil.isMatch(Constants.REGEXP_MOBILE, originStaff.getMobile());
            // 新账号用户信息
            Staff newStaff = staffService.getByMobile(request.getNewMobile());

            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus status = transactionManager.getTransaction(def);

            try {
                if (newStaff == null) {
                    log.info("新账号{}不在本系统中，为新账号。", request.getNewMobile());
                    // 如果原账号只绑定一个企业，则直接修改手机号，否则新建账号
                    if (originStaffEnterpriseCount == 1 || isVirtualMobile) {
                        if (isVirtualMobile) {
                            log.info("当前账号为虚拟账号，直接更换为手机号{}", request.getNewMobile());
                        } else {
                            log.info("当前账号只归属于一个企业，直接更换为手机号{}", request.getNewMobile());
                        }

                        // 修改姓名及手机号
                        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
                        updateUserRequest.setAppId(UserAuthsAppIdEnum.MALL.getCode());
                        updateUserRequest.setId(request.getUserId());
                        updateUserRequest.setName(request.getName());
                        updateUserRequest.setMobile(request.getNewMobile());
                        userService.update(updateUserRequest);

                        // 如果原账号是虚拟账号，则最后需要更换用户关联的企业的联系人信息
                        if (isVirtualMobile) {
                            // 虚拟账号换绑时修改用户密码
                            if (StrUtil.isNotEmpty(request.getPassword())) {
                                userAuthsService.updatePassword(request.getUserId(), UserAuthsAppIdEnum.MALL.getCode(), request.getPassword(), request.getOpUserId());
                            }
                            this.changeContactorByUserId(request.getUserId(), request.getName(), request.getNewMobile(), request.getOpUserId());
                        }

                        // 记录企业管理员变更日志
                        this.saveChangeAdminLog(request.getEid(), originStaff.getId(), originStaff.getMobile(), originStaff.getId(), request.getNewMobile(), request.getOpUserId());
                    } else {
                        // 创建员工信息
                        CreateStaffRequest createStaffRequest = new CreateStaffRequest();
                        createStaffRequest.setName(request.getName());
                        createStaffRequest.setMobile(request.getNewMobile());
                        createStaffRequest.setPassword(request.getPassword());
                        createStaffRequest.setIgnoreExists(true);
                        createStaffRequest.setOpUserId(request.getOpUserId());
                        Long newUserId = staffService.create(createStaffRequest);

                        // 更换管理员
                        this.changeAdmin(request.getEid(), originStaff.getId(), originStaff.getMobile(), newUserId, request.getNewMobile(), request.getOpUserId());
                    }
                } else {
                    if (newStaff.getId().equals(request.getUserId())) {
                        log.info("更换的账号与原账号一样，无需更换。newMobile={}", request.getNewMobile());
                        throw new BusinessException(UserErrorCode.ENTERPRISE_UPDATE_MANAGER_MOBILE_SAME);
                    }

                    if (!newStaff.getName().equals(request.getName())) {
                        // 修改姓名
                        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
                        updateUserRequest.setAppId(UserAuthsAppIdEnum.MALL.getCode());
                        updateUserRequest.setId(newStaff.getId());
                        updateUserRequest.setName(request.getName());
                        userService.update(updateUserRequest);
                    }

                    // 如果原账号是虚拟账号，则最后需要更换用户关联的企业的联系人信息
                    if (isVirtualMobile) {
                        // 虚拟账号换绑时修改用户密码
                        if (StrUtil.isNotEmpty(request.getPassword())) {
                            userAuthsService.updatePassword(newStaff.getId(), UserAuthsAppIdEnum.MALL.getCode(), request.getPassword(), request.getOpUserId());
                        }
                        this.changeContactorByUserId(request.getUserId(), request.getName(), request.getNewMobile(), request.getOpUserId());
                    }

                    // 更换管理员
                    this.changeAdmin(request.getEid(), originStaff.getId(), originStaff.getMobile(), newStaff.getId(), request.getNewMobile(), request.getOpUserId());

                    if (isVirtualMobile) {
                        // 注销原始用户账号
                        UpdateUserStatusRequest updateUserStatusRequest = new UpdateUserStatusRequest();
                        updateUserStatusRequest.setId(request.getUserId());
                        updateUserStatusRequest.setStatus(UserStatusEnum.DEREGISTER.getCode());
                        updateUserStatusRequest.setRemark("因更换管理员账号而注销");
                        userService.updateStatus(updateUserStatusRequest);
                    }
                }

                transactionManager.commit(status);
            } catch (Exception e) {
                transactionManager.rollback(status);
                throw e;
            }

        } finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockName, lockId);
        }

        return true;
    }

    /**
     * 批量修改用户关联企业的联系人信息
     *
     * @param userId 用户ID
     * @param contactor 联系人姓名
     * @param contactorPhone 联系人手机号
     * @author xuan.zhou
     * @date 2022/11/10
     **/
    private void changeContactorByUserId(Long userId, String contactor, String contactorPhone, Long opUserId) {
        // 用户关联的企业列表（可用状态）
        List<EnterpriseDO> userEnterpriseList = this.listByUserId(userId, EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return;
        }

        // 用户关联的企业ID列表
        List<Long> eids = userEnterpriseList.stream().map(EnterpriseDO::getId).collect(Collectors.toList());

        QueryWrapper<EnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseDO::getId, eids);

        EnterpriseDO entity = new EnterpriseDO();
        entity.setContactor(contactor);
        entity.setContactorPhone(contactorPhone);
        entity.setOpUserId(opUserId);

        this.update(entity, queryWrapper);
    }

    /**
     * 保存更换管理员日志
     *
     * @param eid 企业ID
     * @param originUserId 原用户ID
     * @param originMobile 原用户手机号
     * @param newUserId 新用户ID
     * @param newMobile 新用户手机号
     * @param opUserId 操作人ID
     * @author xuan.zhou
     * @date 2022/11/10
     **/
    private void saveChangeAdminLog(Long eid, Long originUserId, String originMobile, Long newUserId, String newMobile, Long opUserId) {
        EnterpriseManagerChangeLogDO enterpriseManagerChangeLogDO = new EnterpriseManagerChangeLogDO();
        enterpriseManagerChangeLogDO.setEid(eid);
        enterpriseManagerChangeLogDO.setOriginUserId(originUserId);
        enterpriseManagerChangeLogDO.setOriginMobile(originMobile);
        enterpriseManagerChangeLogDO.setNewUserId(newUserId);
        enterpriseManagerChangeLogDO.setNewMobile(newMobile);
        enterpriseManagerChangeLogDO.setOpUserId(opUserId);
        enterpriseManagerChangeLogService.save(enterpriseManagerChangeLogDO);
    }

    /**
     * 更换企业管理员
     *
     * @param eid 企业ID
     * @param originUserId 原用户ID
     * @param originMobile 原用户手机号
     * @param newUserId 新用户ID
     * @param newMobile 新用户手机号
     * @param opUserId 操作人ID
     */
    private void changeAdmin(Long eid, Long originUserId, String originMobile, Long newUserId, String newMobile, Long opUserId) {
        // 更换管理员信息
        // 如果企业下存在新用户ID对应的员工信息，则直接升级为管理员，如果不存在则新增
        EnterpriseEmployeeDO enterpriseEmployeeDO = enterpriseEmployeeService.getByEidUserId(eid, newUserId);
        if (enterpriseEmployeeDO == null) {
            EnterpriseEmployeeDO originEnterpriseEmployeeDO = enterpriseEmployeeService.getByEidUserId(eid, originUserId);
            originEnterpriseEmployeeDO.setOpUserId(opUserId);
            enterpriseEmployeeService.deleteByIdWithFill(originEnterpriseEmployeeDO);

            enterpriseEmployeeDO = new EnterpriseEmployeeDO();
            enterpriseEmployeeDO.setEid(eid);
            enterpriseEmployeeDO.setUserId(newUserId);
            enterpriseEmployeeDO.setAdminFlag(1);
            enterpriseEmployeeDO.setOpUserId(opUserId);
            enterpriseEmployeeService.save(enterpriseEmployeeDO);
        } else if (enterpriseEmployeeDO.getAdminFlag() == 1) {
            log.info("已是该企业管理员账号，无需更换。eid={}, userId={}", eid, newUserId);
            throw new BusinessException(UserErrorCode.ENTERPRISE_UPDATE_MANAGER_MOBILE_MANAGER_YET);
        } else {
            EnterpriseEmployeeDO originEnterpriseEmployeeDO = enterpriseEmployeeService.getByEidUserId(eid, originUserId);
            originEnterpriseEmployeeDO.setOpUserId(opUserId);
            enterpriseEmployeeService.deleteByIdWithFill(originEnterpriseEmployeeDO);

            enterpriseEmployeeDO.setAdminFlag(1);
            enterpriseEmployeeDO.setOpUserId(opUserId);
            enterpriseEmployeeService.updateById(enterpriseEmployeeDO);
        }

        // 记录企业管理员变更日志
        this.saveChangeAdminLog(eid, originUserId, originMobile, newUserId, newMobile, opUserId);

        // 重新绑定角色信息
        {
            if (enterpriseEmployeeDO != null) {
                // 如果企业下存在新用户ID对应的员工信息，则删除新用户在该企业下的角色信息
                List<UserRoleDO> userRoleDOList = userRoleService.listByEidUserId(eid, newUserId);
                userRoleDOList.forEach(e -> {
                    e.setOpUserId(opUserId);
                    userRoleService.deleteByIdWithFill(e);
                });
            }

            // 把原用户角色绑给新用户，删除原用户角色
            List<UserRoleDO> userRoleDOList = userRoleService.listByEidUserId(eid, originUserId);
            userRoleDOList.forEach(e -> {
                UserRoleDO userRoleDO = new UserRoleDO();
                userRoleDO.setAppId(e.getAppId());
                userRoleDO.setEid(e.getEid());
                userRoleDO.setUserId(newUserId);
                userRoleDO.setRoleId(e.getRoleId());
                userRoleDO.setOpUserId(opUserId);
                userRoleService.save(userRoleDO);

                e.setOpUserId(opUserId);
                userRoleService.deleteByIdWithFill(e);
            });
        }
    }

    /**
     * 1、找出不符合企业统一信用代码企业信息 。打上标记 （ 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房）
     * <p>
     * 2、通过企业统一信用代码找天眼查表，把名称不正确的给打上标记和没有找到企业名称打上标记。（ 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房）
     * <p>
     * 3、通过企业名称查找天眼查没有匹配企业名称。（7-医院 8-诊所）打上标记。
     * <p>
     * 4、通过证照号找出相同的企业信息。打上重复标记
     * <p>
     * 5、把所有打上标记的企业判断其业务是否可以软删除。
     *
     * @return
     */
    @Override
    public boolean syncHandlerFlag() {
        //找出不符合企业统一信用代码企业信息 。打上标记 （ 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房）
        QueryEnterprisePageListRequest pageListRequest = new QueryEnterprisePageListRequest();
        pageListRequest.setInTypeList(Arrays.asList(3,4,5,6,7,8));
        pageListRequest.setHandleType(1);
        Page<EnterpriseDO> page = null;
        int current = 1;
        do {
            pageListRequest.setCurrent(current);
            pageListRequest.setSize(500);
            page = this.pageList(pageListRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            for (EnterpriseDO e : page.getRecords()) {
                if (Arrays.asList(3, 4, 5, 6).contains(e.getType())) {
                    //社会唯一信用代码效验
                    boolean lengthResult = CreditCodeUtil.isCreditCode(e.getLicenseNumber());
                    if (!lengthResult) {
                        e.setHandleType(3);
                        this.updateById(e);
                        continue;
                    }

                    //通过企业统一信用代码找天眼查表
                    TycEnterpriseQueryRequest request = new TycEnterpriseQueryRequest();
                    request.setKeyword(e.getLicenseNumber());
                    TycResultDTO<TycEnterpriseInfoDTO> tycResultDTO = tycEnterpriseApi.findEnterpriseByKeyword(request);
                    //天眼查功能是否关闭
                    if (!TycErrorCode.OK.getCode().equals(tycResultDTO.getError_code())) {
                        e.setHandleType(4);
                        this.updateById(e);
                        continue;
                    }

                    TycEnterpriseInfoDTO tycEnterpriseInfoDTO = tycResultDTO.getResult();
                    if (!tycEnterpriseInfoDTO.getName().equals(e.getName())) {
                        double similar2 = StrUtil.similar(tycEnterpriseInfoDTO.getName(), e.getName());
                        e.setHandleType(4);
                        e.setLikeValue(new BigDecimal(similar2*100).intValue());
                        this.updateById(e);
                        continue;
                    }
                    if(!tycEnterpriseInfoDTO.getCreditCode().equals(e.getLicenseNumber())){
                        e.setHandleType(4);
                        this.updateById(e);
                        continue;
                    }
                }
                if (Arrays.asList(7, 8).contains(e.getType())) {
                    if (!(matchPracticeLicenseOfMedicalInstitution(e.getLicenseNumber()) || matchTempMedicalInstitution(e.getLicenseNumber()))) {
                        e.setHandleType(3);
                        this.updateById(e);
                        continue;
                    }

                    TycEnterpriseQueryRequest request = new TycEnterpriseQueryRequest();
                    request.setKeyword(e.getName());
                    TycResultDTO<TycEnterpriseInfoDTO> tycResultDTO = tycEnterpriseApi.findEnterpriseByKeyword(request);
                    if (!TycErrorCode.OK.getCode().equals(tycResultDTO.getError_code())) {
                        e.setHandleType(4);
                        this.updateById(e);
                        continue;
                    }

                    TycEnterpriseInfoDTO tycEnterpriseInfoDTO = tycResultDTO.getResult();
                    if (!tycEnterpriseInfoDTO.getName().equals(e.getName())) {
                        double similar2 = StrUtil.similar(tycEnterpriseInfoDTO.getName(), e.getName());
                        e.setLikeValue(new BigDecimal(similar2*100).intValue());
                        e.setHandleType(4);
                        this.updateById(e);
                        continue;
                    }
                }

                LambdaQueryWrapper<EnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(EnterpriseDO::getLicenseNumber,e.getLicenseNumber()).eq(EnterpriseDO::getHandleType,2);
                EnterpriseDO enterpriseDO=this.getOne(queryWrapper);
                if (enterpriseDO != null) {
                    e.setHandleType(5);
                    this.updateById(e);
                }else{
                    e.setHandleType(2);
                    this.updateById(e);
                }
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return true;
    }

    public static boolean matchPracticeLicenseOfMedicalInstitution(String str) {
        String pattern = "[0-9A-Z]{8}[-]{0,1}[0-9A-Z]\\d{8}[A-Z]\\d{3}(1|2|9)";
        Matcher m = Pattern.compile(pattern).matcher(str);
        return m.matches();
    }

    /**
     * 部分地方提供临时医疗许可证由16位大写字母和数字组成
     *
     * @param str
     * @return
     */
    public static boolean matchTempMedicalInstitution(String str) {
        String pattern = "[0-9A-Z]{16}";
        Matcher m = Pattern.compile(pattern).matcher(str);
        return m.matches();
    }
}
