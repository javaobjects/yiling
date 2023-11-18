package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dao.EnterprisePlatformMapper;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.ImportEnterprisePlatformRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.entity.EnterprisePlatformDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseCustomerSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;
import com.yiling.user.enterprise.enums.EnterprisePlatformOpTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.enterprise.service.EnterprisePlatformLogService;
import com.yiling.user.enterprise.service.EnterprisePlatformService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.enums.PermissionAppEnum;
import com.yiling.user.system.enums.RoleTypeEnum;
import com.yiling.user.system.service.RoleMenuService;
import com.yiling.user.system.service.RoleService;
import com.yiling.user.system.service.UserRoleService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业开通平台表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-31
 */
@Slf4j
@Service
public class EnterprisePlatformServiceImpl extends BaseServiceImpl<EnterprisePlatformMapper, EnterprisePlatformDO> implements EnterprisePlatformService {

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private EnterprisePlatformLogService enterprisePlatformLogService;
    @Autowired
    private EnterpriseCustomerService enterpriseCustomerService;

    @Override
    public EnterprisePlatformDO getByEid(Long eid) {
        QueryWrapper<EnterprisePlatformDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterprisePlatformDO::getEid, eid).last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EnterprisePlatformDO> listByEids(List<Long> eids) {
        QueryWrapper<EnterprisePlatformDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterprisePlatformDO::getEid, eids);
        List<EnterprisePlatformDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean openPlatform(Long eid, List<PlatformEnum> platformEnumList, EnterpriseChannelEnum enterpriseChannelEnum, EnterpriseHmcTypeEnum enterpriseHmcTypeEnum, Long opUserId) {
        if (CollUtil.isEmpty(platformEnumList)) {
            return true;
        }

        EnterprisePlatformDO entity = this.getByEid(eid);
        if (entity == null) {
            entity = new EnterprisePlatformDO();
            entity.setMallFlag(0);
            entity.setPopFlag(0);
            entity.setSalesAssistFlag(0);
            entity.setInternetHospitalFlag(0);
            entity.setDataCenterFlag(0);
            entity.setHmcFlag(0);
        }

        // 企业信息
        EnterpriseDO enterpriseDO = enterpriseService.getById(eid);
        // 企业管理员信息
        List<EnterpriseEmployeeDO> enterpriseEmployeeDOList = enterpriseEmployeeService.listAdminsByEid(eid);

        if (entity.getMallFlag() == 0 && platformEnumList.contains(PlatformEnum.B2B)) {
            // 开通B2B子系统
            entity.setMallFlag(1);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.B2B, EnterprisePlatformOpTypeEnum.OPEN, opUserId);

            // 开通B2B时，根据企业类型获取管理员用户要绑定的角色编码
            List<String> roleCodeList = EnterpriseTypeEnum.getByCode(enterpriseDO.getType()).getCategory().getB2bRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户绑定子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.bindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });
        }

        if (entity.getPopFlag() == 0 && platformEnumList.contains(PlatformEnum.POP)) {
            if (enterpriseChannelEnum == null) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_OPEN_POP_PLATFORM_CHANNEL_NOT_NULL);
            }

            // 更新企业渠道类型
            enterpriseService.updateById(enterpriseDO.setChannelId(enterpriseChannelEnum.getCode()));

            // 开通POP子系统
            entity.setPopFlag(1);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.POP, EnterprisePlatformOpTypeEnum.OPEN, opUserId);

            // 将其生成为以岭的客户
            AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
            addCustomerRequest.setEid(Constants.YILING_EID);
            addCustomerRequest.setCustomerEid(enterpriseDO.getId());
            addCustomerRequest.setCustomerName(enterpriseDO.getName());
            addCustomerRequest.setSource(EnterpriseCustomerSourceEnum.OPEN_POP.getCode());
            addCustomerRequest.setAddPurchaseRelationFlag(false);
            addCustomerRequest.setOpUserId(opUserId);
            enterpriseCustomerService.add(addCustomerRequest);

            // 开通POP时，根据渠道类型获取管理员用户要绑定的角色ID
            Long roleId = enterpriseChannelEnum.getRoleId();
            // 给管理员用户绑定子系统角色
            enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                userRoleService.bindUserRoles(PermissionAppEnum.MALL_ADMIN_POP, eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleId), opUserId);
            });
        }

        if (entity.getSalesAssistFlag() == 0 && platformEnumList.contains(PlatformEnum.SALES_ASSIST)) {
            if (entity.getPopFlag() == 0 && entity.getMallFlag() == 0) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_CAN_NOT_OPEN_SALES_ASSISTANT);
            } else if (EnterpriseTypeEnum.getByCode(enterpriseDO.getType()).isTerminal()) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_TERMINAL_CAN_NOT_OPEN_SALES_ASSISTANT);
            }

            // 开通销售助手
            entity.setSalesAssistFlag(1);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.SALES_ASSIST, EnterprisePlatformOpTypeEnum.OPEN, opUserId);

            List<String> roleCodeList = PlatformEnum.SALES_ASSIST.getRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户绑定子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.bindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });
        }

        if (entity.getHmcFlag() == 0 && platformEnumList.contains(PlatformEnum.HMC)) {
            if (enterpriseHmcTypeEnum == null) {
                throw new BusinessException(ResultCode.FAILED, "开通HMC时，业务类型不能为空");
            }

            EnterpriseTypeEnum enterpriseTypeEnum = EnterpriseTypeEnum.getByCode(enterpriseDO.getType());
            if (enterpriseHmcTypeEnum == EnterpriseHmcTypeEnum.MEDICINE_INSURANCE || enterpriseHmcTypeEnum == EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK) {
                if (!enterpriseTypeEnum.isTerminal()) {
                    throw new BusinessException(ResultCode.FAILED, "只有终端才能开通此业务类型");
                }
            } else {
                if (!(enterpriseTypeEnum.isIndustry() || enterpriseTypeEnum.isBusiness())) {
                    throw new BusinessException(ResultCode.FAILED, "只有工业或商业才能开通此业务类型");
                }
            }

            // 更新企业HMC类型
            enterpriseService.updateById(enterpriseDO.setHmcType(enterpriseHmcTypeEnum.getCode()));

            // 开通HMC子系统
            entity.setHmcFlag(1);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.HMC, EnterprisePlatformOpTypeEnum.OPEN, opUserId);

            List<String> roleCodeList = enterpriseHmcTypeEnum.getRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户绑定子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.bindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });
        }

        // 无论开通那个子系统，都一并开通数据中台
        if (entity.getDataCenterFlag() == 0) {
            // 开通数据中台
            entity.setDataCenterFlag(1);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.DATA_CENTER, EnterprisePlatformOpTypeEnum.OPEN, opUserId);

            List<String> roleCodeList = EnterpriseTypeEnum.getByCode(enterpriseDO.getType()).getCategory().getDataCenterRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户绑定子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.bindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });
        }

        entity.setEid(eid);
        entity.setOpUserId(opUserId);
        return this.saveOrUpdate(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closePlatform(Long eid, List<PlatformEnum> platformEnumList, Long opUserId) {
        if (CollUtil.isEmpty(platformEnumList)) {
            return true;
        }

        EnterprisePlatformDO entity = this.getByEid(eid);
        if (entity == null) {
            return true;
        }

        // 企业信息
        EnterpriseDO enterpriseDO = enterpriseService.getById(eid);
        // 企业管理员信息
        List<EnterpriseEmployeeDO> enterpriseEmployeeDOList = enterpriseEmployeeService.listAdminsByEid(eid);

        if (entity.getMallFlag() == 1 && platformEnumList.contains(PlatformEnum.B2B)) {
            // 关闭B2B子系统
            entity.setMallFlag(0);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.B2B, EnterprisePlatformOpTypeEnum.CLOSE, opUserId);

            // 关闭B2B时，根据企业类型获取管理员用户要解绑的角色编码
            List<String> roleCodeList = EnterpriseTypeEnum.getByCode(enterpriseDO.getType()).getCategory().getB2bRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户解绑子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.unbindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });
        }

        if (entity.getPopFlag() == 1 && platformEnumList.contains(PlatformEnum.POP)) {
            // 关闭POP子系统
            entity.setPopFlag(0);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.POP, EnterprisePlatformOpTypeEnum.CLOSE, opUserId);

            // 关闭POP时，根据渠道类型获取管理员用户要解绑的角色ID
            Long roleId = EnterpriseChannelEnum.getByCode(enterpriseDO.getChannelId()).getRoleId();
            // 给管理员用户解绑子系统角色
            enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                userRoleService.unbindUserRoles(PermissionAppEnum.MALL_ADMIN_POP, eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleId), opUserId);
            });

            // 更新企业渠道类型
            enterpriseService.updateById(enterpriseDO.setChannelId(0L));
        }

        if (entity.getSalesAssistFlag() == 1 && platformEnumList.contains(PlatformEnum.SALES_ASSIST)) {
            // 关闭销售助手
            entity.setSalesAssistFlag(0);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.SALES_ASSIST, EnterprisePlatformOpTypeEnum.CLOSE, opUserId);

            List<String> roleCodeList = PlatformEnum.SALES_ASSIST.getRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户绑定子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.unbindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });
        }

        if (entity.getHmcFlag() == 1 && platformEnumList.contains(PlatformEnum.HMC)) {
            // 关闭HMC
            entity.setHmcFlag(0);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.HMC, EnterprisePlatformOpTypeEnum.CLOSE, opUserId);

            List<String> roleCodeList = EnterpriseHmcTypeEnum.getByCode(enterpriseDO.getHmcType()).getRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户解绑子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.unbindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });

            // 更新HMC道类型
            enterpriseService.updateById(enterpriseDO.setHmcType(0));
        }

        // 所有子系统都关闭后，就关闭数据中台
        boolean closeAllFlag = entity.getPopFlag() == 0 && entity.getMallFlag() == 0 && entity.getSalesAssistFlag() == 0 && entity.getHmcFlag() == 0;
        if (entity.getDataCenterFlag() == 1 && closeAllFlag) {
            // 关闭数据中台
            entity.setDataCenterFlag(0);
            // 记录日志
            enterprisePlatformLogService.saveOpPlatformLog(eid, PlatformEnum.DATA_CENTER, EnterprisePlatformOpTypeEnum.CLOSE, opUserId);

            List<String> roleCodeList = EnterpriseTypeEnum.getByCode(enterpriseDO.getType()).getCategory().getDataCenterRoleCodeList();
            List<RoleDO> roleDOList = roleService.getByCodeList(roleCodeList);
            roleDOList.forEach(roleDO -> {
                // 给管理员用户解绑子系统角色
                enterpriseEmployeeDOList.forEach(enterpriseEmployeeDO -> {
                    userRoleService.unbindUserRoles(PermissionAppEnum.getByCode(roleDO.getAppId()), eid, enterpriseEmployeeDO.getUserId(), ListUtil.toList(roleDO.getId()), opUserId);
                });
            });
        }

        // 清空企业商家后台自定义角色权限
        List<RoleDO> roleDOList = roleService.listByEid(PermissionAppEnum.MALL_ADMIN, eid);
        List<RoleDO> customRoleDOList = roleDOList.stream().filter(e -> RoleTypeEnum.getByCode(e.getType()) == RoleTypeEnum.CUSTOM).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(customRoleDOList)) {
            customRoleDOList.forEach(roleDO -> {
                log.info("清空企业自定义角色权限：roleId={}", roleDO.getId());
                roleMenuService.updateRoleMenus(roleDO.getId(), ListUtil.empty(), opUserId);
            });
        }

        entity.setEid(eid);
        entity.setOpUserId(opUserId);
        return this.saveOrUpdate(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importEnterprisePlatform(ImportEnterprisePlatformRequest request) {
        // 给企业开通平台
        List<PlatformEnum> platformEnumList = request.getPlatformEnumList();
        if (CollUtil.isNotEmpty(platformEnumList)) {
            EnterpriseChannelEnum enterpriseChannelEnum = null;
            if (Objects.nonNull(request.getChannelId())) {
                enterpriseChannelEnum = EnterpriseChannelEnum.getByCode(request.getChannelId());
            }

            this.openPlatform(request.getId(), platformEnumList, enterpriseChannelEnum, request.getHmcTypeEnum(), request.getOpUserId());

            // 如果开通POP（有渠道类型），则将其生成为以岭的客户
            if (platformEnumList.contains(PlatformEnum.POP)) {
                EnterpriseDO enterpriseDO = enterpriseService.getById(request.getId());

                AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
                addCustomerRequest.setEid(Constants.YILING_EID);
                addCustomerRequest.setCustomerEid(enterpriseDO.getId());
                addCustomerRequest.setCustomerName(enterpriseDO.getName());
                addCustomerRequest.setSource(EnterpriseSourceEnum.IMPORT.getCode());
                addCustomerRequest.setAddPurchaseRelationFlag(false);
                addCustomerRequest.setOpUserId(request.getOpUserId());
                enterpriseCustomerService.add(addCustomerRequest);
            }
        }

        return true;
    }
}
