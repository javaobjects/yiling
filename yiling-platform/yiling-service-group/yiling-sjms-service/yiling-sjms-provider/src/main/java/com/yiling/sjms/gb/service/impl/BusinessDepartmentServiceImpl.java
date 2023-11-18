package com.yiling.sjms.gb.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.service.BusinessDepartmentService;
import com.yiling.sjms.gb.service.GbOrgManagerService;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.bo.SimpleEsbOrgInfoBO;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 事业部 Service 实现
 *
 * @author: xuan.zhou
 * @date: 2023/3/3
 */
@Slf4j
@Service
public class BusinessDepartmentServiceImpl implements BusinessDepartmentService {

    @Autowired
    GbOrgManagerService gbOrgManagerService;
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Override
    public EsbOrganizationDTO getByEmpId(String empId) {
        log.info("查找员工对应的事业部信息 -> empId={}", empId);

        Map<String, EsbOrganizationDTO> map = this.listByEmpIds(ListUtil.toList(empId));
        EsbOrganizationDTO esbOrganizationDTO = map.get(empId);
        if (esbOrganizationDTO != null) {
            log.info("已找到员工对应的事业部信息 -> orgId={}, orgName={}", esbOrganizationDTO.getOrgId(), esbOrganizationDTO.getOrgName());
            return esbOrganizationDTO;
        }

        log.warn("未找到员工对应的事业部信息");
        return null;
    }

    @Override
    public Map<String, EsbOrganizationDTO> listByEmpIds(List<String> empIds) {
        log.info("查找员工对应的事业部信息 -> empIds={}", empIds);
        if (CollUtil.isEmpty(empIds)) {
            return MapUtil.empty();
        }

        List<EsbEmployeeDTO> list = esbEmployeeApi.listByEmpIds(empIds);
        if (CollUtil.isEmpty(list)) {
            log.error("未找到对应的员工信息");
            return MapUtil.empty();
        }

        List<Long> orgIds = list.stream().map(EsbEmployeeDTO::getDeptId).distinct().collect(Collectors.toList());
        Map<Long, EsbOrganizationDTO> orgBdDTOMap = this.listByOrgIds(orgIds);

        Map<String, EsbOrganizationDTO> empBdDTOMap = CollUtil.newHashMap();
        list.forEach(e -> {
            if (orgBdDTOMap.containsKey(e.getDeptId())) {
                empBdDTOMap.put(e.getEmpId(), orgBdDTOMap.get(e.getDeptId()));
            }
        });

        return empBdDTOMap;
    }

    @Override
    public EsbOrganizationDTO getByJobId(Long jobId) {
        log.info("查找岗位对应的事业部信息 -> jobId={}", jobId);

        Map<Long, EsbOrganizationDTO> map = this.listByJobIds(ListUtil.toList(jobId));
        EsbOrganizationDTO esbOrganizationDTO = map.get(jobId);
        if (esbOrganizationDTO != null) {
            log.info("已找到岗位对应的事业部信息 -> orgId={}, orgName={}", esbOrganizationDTO.getOrgId(), esbOrganizationDTO.getOrgName());
            return esbOrganizationDTO;
        }

        log.warn("未找到岗位对应的事业部信息");
        return null;
    }

    @Override
    public Map<Long, EsbOrganizationDTO> listByJobIds(List<Long> jobIds) {
        log.info("查找岗位对应的事业部信息 -> jobIds={}", jobIds);
        if (CollUtil.isEmpty(jobIds)) {
            return MapUtil.empty();
        }

        List<EsbEmployeeDTO> list = esbEmployeeApi.listByJobIds(jobIds);
        if (CollUtil.isEmpty(list)) {
            log.error("未找到对应的岗位信息");
            return MapUtil.empty();
        }

        List<Long> orgIds = list.stream().map(EsbEmployeeDTO::getDeptId).distinct().collect(Collectors.toList());
        Map<Long, EsbOrganizationDTO> orgBdDTOMap = this.listByOrgIds(orgIds);

        Map<Long, EsbOrganizationDTO> jobBdDTOMap = CollUtil.newHashMap();
        list.forEach(e -> {
            if (orgBdDTOMap.containsKey(e.getDeptId())) {
                jobBdDTOMap.put(e.getJobId(), orgBdDTOMap.get(e.getDeptId()));
            }
        });

        return jobBdDTOMap;
    }

    @Override
    public EsbOrganizationDTO getByOrgId(Long orgId) {
        log.info("查找部门对应的事业部信息 -> orgId={}", orgId);

        Map<Long, EsbOrganizationDTO> map = this.listByOrgIds(ListUtil.toList(orgId));
        EsbOrganizationDTO esbOrganizationDTO = map.get(orgId);
        if (esbOrganizationDTO != null) {
            log.info("已找到部门对应的事业部信息 -> orgId={}, orgName={}", esbOrganizationDTO.getOrgId(), esbOrganizationDTO.getOrgName());
            return esbOrganizationDTO;
        }

        log.warn("未找到部门对应的事业部信息");
        return null;
    }

    @Override
    public Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> orgIds) {
        log.info("批量查找部门对应的事业部信息 -> orgIds={}", orgIds);
        if (CollUtil.isEmpty(orgIds)) {
            return MapUtil.empty();
        }

        List<EsbOrganizationDTO> orgDTOList = esbOrganizationApi.listByOrgIds(orgIds);

        // 获取事业部部门ID列表
        List<Long> bdOrgIds = gbOrgManagerService.listOrgIds();
        // 获取所有部门简单信息列表
        List<SimpleEsbOrgInfoBO> allOrgBOList = esbOrganizationApi.listAll(true);
        Map<Long, SimpleEsbOrgInfoBO> allOrgBOMap = allOrgBOList.stream().collect(Collectors.toMap(SimpleEsbOrgInfoBO::getOrgId, Function.identity()));

        Map<Long, SimpleEsbOrgInfoBO> orgParentBOMap = CollUtil.newHashMap();
        orgDTOList.forEach(e -> {
            SimpleEsbOrgInfoBO bdOrgInfoBO = this.getParentIdWithin(allOrgBOMap, PojoUtils.map(e, SimpleEsbOrgInfoBO.class), bdOrgIds);
            if (bdOrgInfoBO != null) {
                orgParentBOMap.put(e.getOrgId(), bdOrgInfoBO);
            }
        });

        if (CollUtil.isEmpty(orgParentBOMap)) {
            log.info("未找到部门对应的事业部信息");
            return MapUtil.empty();
        }

        log.info("找到部门对应的是事业部信息 -> {}", JSONUtil.toJsonStr(orgParentBOMap));

        List<Long> selectedDbOrgIds = orgParentBOMap.values().stream().map(e -> e.getOrgId()).distinct().collect(Collectors.toList());
        List<EsbOrganizationDTO> selectedDbOrgDTOList = esbOrganizationApi.listByOrgIds(selectedDbOrgIds);
        Map<Long, EsbOrganizationDTO> selectedBdDTOMap = selectedDbOrgDTOList.stream().collect(Collectors.toMap(EsbOrganizationDTO::getOrgId, Function.identity()));

        Map<Long, EsbOrganizationDTO> orgBdDTOMap = CollUtil.newHashMap();
        orgParentBOMap.forEach((k, v) -> {
            if (selectedBdDTOMap.containsKey(v.getOrgId())) {
                orgBdDTOMap.put(k, selectedBdDTOMap.get(v.getOrgId()));
            }
        });

        return orgBdDTOMap;
    }

    private SimpleEsbOrgInfoBO getParentIdWithin(Map<Long, SimpleEsbOrgInfoBO> orgMap, SimpleEsbOrgInfoBO orgInfo, List<Long> orgIds) {
        Long orgId = orgInfo.getOrgId();
        if (orgIds.contains(orgId)) {
            return orgInfo;
        }

        Long orgPid = orgInfo.getOrgPid();
        if (orgPid == null || orgPid == 0L) {
            return null;
        }

        SimpleEsbOrgInfoBO parentOrgInfo = orgMap.get(orgPid);
        if (parentOrgInfo == null) {
            return null;
        }

        return this.getParentIdWithin(orgMap, parentOrgInfo, orgIds);
    }

}
