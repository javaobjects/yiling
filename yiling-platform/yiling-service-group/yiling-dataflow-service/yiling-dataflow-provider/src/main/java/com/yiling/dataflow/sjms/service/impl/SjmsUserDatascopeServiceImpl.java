package com.yiling.dataflow.sjms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.entity.SjmsProvinceDO;
import com.yiling.dataflow.sjms.entity.SjmsRoleDatascopeDO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.sjms.service.SjmsProvinceService;
import com.yiling.dataflow.sjms.service.SjmsRoleDatascopeProvinceService;
import com.yiling.dataflow.sjms.service.SjmsRoleDatascopeService;
import com.yiling.dataflow.sjms.service.SjmsUserDatascopeService;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.api.SjmsUserApi;
import com.yiling.user.system.bo.SjmsUser;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向数据权限-用户权限配置 服务类
 *
 * @author: xuan.zhou
 * @date: 2023/3/27
 */
@Slf4j
@Service
public class SjmsUserDatascopeServiceImpl implements SjmsUserDatascopeService {

    @DubboReference
    SjmsUserApi sjmsUserApi;
    @DubboReference
    RoleApi roleApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Autowired
    SjmsRoleDatascopeService sjmsRoleDatascopeService;
    @Autowired
    SjmsRoleDatascopeProvinceService sjmsRoleDatascopeProvinceService;
    @Autowired
    SjmsProvinceService sjmsProvinceService;
    @Autowired
    CrmEnterpriseService crmEnterpriseService;
    @Autowired
    CrmSupplierService crmSupplierService;
    @Autowired
    CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public List<Long> listAuthorizedEids(String empId) {
        log.info("准备获取员工对应的数据权限的企业ID列表 -> empId={}", empId);

        SjmsUser sjmsUser = sjmsUserApi.getByEmpId(empId);
        if (sjmsUser == null) {
            log.info("未找到员工号对应的SjmsUser信息 -> empId={}", empId);
            return null;
        }
        log.info("获取到员工对应的SjmsUser信息 -> sjmsUser={}", JSONUtil.toJsonStr(sjmsUser));

        List<RoleDTO> userRoleDTOList = roleApi.listByEidUserId(PermissionAppEnum.SJMS, 0L, sjmsUser.getId());
        if (CollUtil.isEmpty(userRoleDTOList)) {
            log.info("用户未绑定任何角色信息 -> userId={}", sjmsUser.getId());
            return null;
        }
        log.info("获取到员工绑定的角色信息 -> userRoleDOList={}", JSONUtil.toJsonStr(userRoleDTOList));

        List<Long> userRoleIds = userRoleDTOList.stream().map(RoleDTO::getId).distinct().collect(Collectors.toList());
        List<SjmsRoleDatascopeDO> sjmsRoleDatascopeDOList = sjmsRoleDatascopeService.listByRoleIds(userRoleIds);
        if (CollUtil.isEmpty(sjmsRoleDatascopeDOList)) {
            log.info("角色对应的数据权限配置为空");
            return null;
        }

        List<Long> list = CollUtil.newArrayList();
        for (SjmsRoleDatascopeDO sjmsRoleDatascopeDO : sjmsRoleDatascopeDOList) {
            List<Long> ids = this.listAuthorizedEids(sjmsRoleDatascopeDO, empId);
            if (ids != null && ids.size() == 0) {
                log.info("判定数据权限为全国");
                return ListUtil.empty();
            }

            if (CollUtil.isNotEmpty(ids)) {
                list = CollUtil.addAllIfNotContains(list, ids);
            }
        }

        if (CollUtil.isEmpty(list)) {
            log.info("判定数据权限为无");
            return null;
        }

        log.info("判定数据权限为 -> list={}", JSONUtil.toJsonStr(list));
        return list;
    }

    @Override
    public SjmsUserDatascopeBO getByEmpId(String empId) {
        log.info("准备获取员工对应的数据权限信息 -> empId={}", empId);
        SjmsUser sjmsUser = sjmsUserApi.getByEmpId(empId);
        if (sjmsUser == null) {
            log.info("未找到员工号对应的SjmsUser信息 -> empId={}", empId);
            return SjmsUserDatascopeBO.noPermission(empId);
        }
        log.info("获取到员工对应的SjmsUser信息 -> sjmsUser={}", JSONUtil.toJsonStr(sjmsUser));

        List<RoleDTO> userRoleDTOList = roleApi.listByEidUserId(PermissionAppEnum.SJMS, 0L, sjmsUser.getId());
        if (CollUtil.isEmpty(userRoleDTOList)) {
            log.info("用户未绑定任何角色信息 -> userId={}", sjmsUser.getId());
            return SjmsUserDatascopeBO.noPermission(empId);
        }
        log.info("获取到员工绑定的角色信息 -> userRoleDOList={}", JSONUtil.toJsonStr(userRoleDTOList));

        List<Long> userRoleIds = userRoleDTOList.stream().map(RoleDTO::getId).distinct().collect(Collectors.toList());
        List<SjmsRoleDatascopeDO> sjmsRoleDatascopeDOList = sjmsRoleDatascopeService.listByRoleIds(userRoleIds);
        if (CollUtil.isEmpty(sjmsRoleDatascopeDOList)) {
            log.info("角色对应的数据权限配置为空");
            return SjmsUserDatascopeBO.noPermission(empId);
        }

        boolean hasAllOrgDatascopeFlag = sjmsRoleDatascopeDOList.stream().anyMatch(e -> e.getOrgDatascope() == 100);
        if (hasAllOrgDatascopeFlag) {
            log.info("部分角色拥有所有机构权限");
            return SjmsUserDatascopeBO.allPermission(empId);
        }

        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(empId);

        List<String> provinceCodes = CollUtil.newArrayList();
        List<String> provinceNames = CollUtil.newArrayList();
        List<Long> crmEids1 = CollUtil.newArrayList();
        List<Long> crmEids2 = CollUtil.newArrayList();;
        for (SjmsRoleDatascopeDO sjmsRoleDatascopeDO : sjmsRoleDatascopeDOList) {
            Integer datascope = sjmsRoleDatascopeDO.getOrgDatascope();
            if (datascope == 0) {
                continue;
            } else if (datascope == 10 || datascope == 20) {
                List<SjmsProvinceDO> sjmsProvinceDOList = this.listAuthorizedProvinces(empId, sjmsRoleDatascopeDO.getRoleId());
                if (CollUtil.isEmpty(sjmsProvinceDOList)) {
                    log.info("未找到省区对应的省份信息");
                    continue;
                }

                provinceCodes = CollUtil.addAllIfNotContains(provinceCodes, sjmsProvinceDOList.stream().map(SjmsProvinceDO::getProvinceCode).distinct().collect(Collectors.toList()));
                provinceNames = CollUtil.addAllIfNotContains(provinceNames, sjmsProvinceDOList.stream().map(SjmsProvinceDO::getProvinceName).distinct().collect(Collectors.toList()));
            } else if (datascope == 30) {
                if (CollUtil.isEmpty(crmEids1)) {
                    List<Long> ids = this.list1(esbEmployeeDTO.getJobId().toString());
                    if (CollUtil.isEmpty(ids)) {
                        crmEids1.add(0L);
                    } else {
                        crmEids1 = ids;
                    }
                }
            } else if (datascope == 40) {
                if (CollUtil.isEmpty(crmEids2)) {
                    List<Long> ids = this.list2(empId, esbEmployeeDTO.getJobId().toString());
                    if (CollUtil.isEmpty(ids)) {
                        crmEids2.add(0L);
                    } else {
                        crmEids2 = ids;
                    }
                }
            }
        }

        // 合并crmEids
        List<Long> crmEids = CollUtil.newArrayList();
        crmEids = CollUtil.addAllIfNotContains(crmEids, crmEids1);
        crmEids = CollUtil.addAllIfNotContains(crmEids, crmEids2);
        crmEids.remove(0L);

        if (CollUtil.isEmpty(provinceCodes) && CollUtil.isEmpty(crmEids)) {
            log.info("判定数据权限为无");
            return SjmsUserDatascopeBO.noPermission(empId);
        }

        SjmsUserDatascopeBO sjmsUserDatascopeBO = new SjmsUserDatascopeBO();
        sjmsUserDatascopeBO.setEmpId(empId);
        sjmsUserDatascopeBO.setOrgDatascope(OrgDatascopeEnum.PORTION.getCode());

        SjmsUserDatascopeBO.OrgPartDatascopeBO orgPartDatascopeBO = new SjmsUserDatascopeBO.OrgPartDatascopeBO();
        orgPartDatascopeBO.setProvinceCodes(provinceCodes);
        orgPartDatascopeBO.setProvinceNames(provinceNames);
        orgPartDatascopeBO.setCrmEids(crmEids);

        sjmsUserDatascopeBO.setOrgPartDatascopeBO(orgPartDatascopeBO);
        log.info("判定数据权限为 -> partDatascopeBO={}", JSONUtil.toJsonStr(orgPartDatascopeBO));
        return sjmsUserDatascopeBO;
    }

    private List<SjmsProvinceDO> listAuthorizedProvinces(String empId, Long roleId) {
        List<String> bizProvinceNames = sjmsRoleDatascopeProvinceService.listProvinceNamesByRoleIdEmpId(roleId, empId);
        if (CollUtil.isEmpty(bizProvinceNames)) {
            log.info("未找到角色和人员对应的省区配置信息 -> empId={}, roleId={}", empId, roleId);
            return ListUtil.empty();
        }
        log.info("已找到角色和人员对应的省区配置信息 -> empId={}, roleId={}, bizProvinceNames={}", empId, roleId, JSONUtil.toJsonStr(bizProvinceNames));

        List<SjmsProvinceDO> sjmsProvinceDOList = sjmsProvinceService.listByBizProvinceName(bizProvinceNames);
        return sjmsProvinceDOList;
    }

    /**
     * 取三者关系中业务代表为自己的
     *
     * @param jobId 岗位ID
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2023/4/10
     **/
    private List<Long> list1(String jobId) {
        List<Long> ids = crmEnterpriseRelationShipService.getCrmEnterIdListByPostCode(jobId);
        log.info("取三者关系中业务代表为自己的 -> ids={}", JSONUtil.toJsonStr(ids));
        return ids;
    }

    /**
     * 取机构档案中商务负责人为自己的 + 取三者关系中业务代表为自己的
     *
     * @param empId
     * @param jobId
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2023/4/10
     **/
    private List<Long> list2(String empId, String jobId) {
        List<Long> ids1 = crmSupplierService.getCrmEnterIdListByCommerceJobNumber(empId);
        log.info("取机构档案中商务负责人为自己的 -> ids={}", JSONUtil.toJsonStr(ids1));
        // 取三者关系中业务代表为自己的
        List<Long> ids2 = crmEnterpriseRelationShipService.getCrmEnterIdListByPostCode(jobId);
        log.info("取三者关系中业务代表为自己的 -> ids={}", JSONUtil.toJsonStr(ids2));
        // 合并去重
        List<Long> ids = CollUtil.addAllIfNotContains(ids1, ids2);
        log.info("最终的 -> ids={}", JSONUtil.toJsonStr(ids));
        return ids;
    }

    private List<Long> listAuthorizedEids(SjmsRoleDatascopeDO sjmsRoleDatascopeDO, String empId) {
        log.info("角色数据权限范围 -> orgDatascope={}, empId={}", sjmsRoleDatascopeDO.getOrgDatascope(), empId);

        Integer datascope = sjmsRoleDatascopeDO.getOrgDatascope();
        if (datascope == 0) {
            return null;
        } else if (datascope == 10 || datascope == 20) {
            List<SjmsProvinceDO> sjmsProvinceDOList = this.listAuthorizedProvinces(empId, sjmsRoleDatascopeDO.getRoleId());
            if (CollUtil.isEmpty(sjmsProvinceDOList)) {
                log.info("未找到省区对应的省份信息");
                return ListUtil.empty();
            }

            List<String> provinceNames = sjmsProvinceDOList.stream().map(SjmsProvinceDO::getProvinceName).collect(Collectors.toList());
            log.info("已找到省区对应的省区名称信息 -> provinceNames={}", JSONUtil.toJsonStr(provinceNames));

            List<Long> ids = crmEnterpriseService.listIdsByProvinceNames(provinceNames);
            if (CollUtil.isEmpty(ids)) {
                log.info("未找到省份名称对应企业信息");
                return null;
            }
            log.info("已找到省份名称对应的企业信息 -> size={}", ids.size());

            return ids;
        } else if (datascope == 30) {
            EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(empId);
            List<Long> ids = this.list1(esbEmployeeDTO.getJobId().toString());
            if (CollUtil.isEmpty(ids)) {
                return null;
            }
            return ids;
        } else if (datascope == 40) {
            EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(empId);
            List<Long> ids = this.list2(empId, esbEmployeeDTO.getJobId().toString());
            if (CollUtil.isEmpty(ids)) {
                return null;
            }
            return ids;
        } else if (datascope == 100) {
            return ListUtil.empty();
        }

        return null;
    }
}
