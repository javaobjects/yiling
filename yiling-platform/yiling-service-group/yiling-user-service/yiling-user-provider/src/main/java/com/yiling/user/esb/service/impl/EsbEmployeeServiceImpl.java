package com.yiling.user.esb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.framework.common.annotations.DynamicName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.config.EsbConfig;
import com.yiling.user.esb.bo.SimpleEsbOrgInfoBO;
import com.yiling.user.esb.dao.EsbEmployeeMapper;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.QueryEsbEmployeePageListRequest;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbEmployeeRequest;
import com.yiling.user.esb.entity.EsbEmployeeDO;
import com.yiling.user.esb.service.EsbEmployeeService;
import com.yiling.user.esb.service.EsbOrganizationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * esb人员 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-24
 */
@Service
public class EsbEmployeeServiceImpl extends BaseServiceImpl<EsbEmployeeMapper, EsbEmployeeDO> implements EsbEmployeeService {

    @Autowired
    EsbConfig esbConfig;

    @Autowired
    EsbOrganizationService esbOrganizationService;

    @Override
    public Page<EsbEmployeeDO> pageList(QueryEsbEmployeePageListRequest request) {
        LambdaQueryWrapper<EsbEmployeeDO> queryWrapper = Wrappers.lambdaQuery();

        String empIdOrName = request.getEmpIdOrName();
        if (StrUtil.isNotEmpty(empIdOrName)) {
            queryWrapper.and(query -> {
                query.eq(EsbEmployeeDO::getEmpId, empIdOrName).or().eq(EsbEmployeeDO::getEmpName, empIdOrName);
            });
        }

        return this.page(request.getPage(), queryWrapper);
    }

    @Override
    public EsbEmployeeDO getByEmpId(String empId) {
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbEmployeeDO::getEmpId, empId)
                .eq(EsbEmployeeDO::getState, EMP_STATE_NORMAL)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<EsbEmployeeDO> listByEmpIds(List<String> empIds) {
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .in(EsbEmployeeDO::getEmpId, empIds)
                .eq(EsbEmployeeDO::getState, EMP_STATE_NORMAL);
        return this.list(queryWrapper);
    }

    @Override
    public List<EsbEmployeeDO> listByDeptId(Long deptId) {
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbEmployeeDO::getDeptId, deptId)
                .eq(EsbEmployeeDO::getState, EMP_STATE_NORMAL)
                .orderByAsc(EsbEmployeeDO::getEmpUid);
        return this.list(queryWrapper);
    }

    @Override
    public EsbEmployeeDO getByDeptIdAndJobName(Long deptId, String jobName) {
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbEmployeeDO::getDeptId, deptId)
                .eq(EsbEmployeeDO::getJobName, jobName)
                .eq(EsbEmployeeDO::getState, EMP_STATE_NORMAL)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(SaveOrUpdateEsbEmployeeRequest request) {
        Assert.notNull(request, "参数request不能为空");

        String empId = request.getEmpId();
        EsbEmployeeDO entity = this.getByEmpId(empId);
        if (entity == null) {
            entity = PojoUtils.map(request, EsbEmployeeDO.class);
            return this.save(entity);
        } else {
            PojoUtils.map(request, entity);
            return this.updateById(entity);
        }
    }

    @Override
    public boolean isProvinceManager(String empId) {
        EsbEmployeeDO entity = this.getByEmpId(empId);
        if (entity == null) {
            return false;
        }

        return esbConfig.getProvinceManager().getJobNames().contains(entity.getJobNames());
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public EsbEmployeeDO getByEmpIdOrJobId(String empId, String jobId, String tableSuffix) {
        if (StringUtils.isEmpty(empId) && StringUtils.isEmpty(jobId)) {
            return null;
        }
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(empId)) {
            queryWrapper.lambda().eq(EsbEmployeeDO::getEmpId, empId);
        }
        if (StringUtils.isNotEmpty(jobId)) {
            queryWrapper.lambda().eq(EsbEmployeeDO::getJobId, jobId);
        }
        queryWrapper.lambda().orderByDesc(EsbEmployeeDO::getId);

        List<EsbEmployeeDO> list = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        if (CollectionUtil.isNotEmpty(list)) {
            List<EsbEmployeeDO> use = list.stream().filter(item -> item.getStatus().equals("正式") || item.getStatus().equals("试用")).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(use)) {
                return use.get(0);
            }
            EsbEmployeeDO esbEmployeeDO = list.get(0);
            esbEmployeeDO.setEmpName(null);
            esbEmployeeDO.setEmpId(null);
            return esbEmployeeDO;

        }
        return null;
    }

    @Override
    public List<EsbEmployeeDO> listByJobIds(List<Long> jobIds) {
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EsbEmployeeDO::getJobId, jobIds);
        return this.list(queryWrapper);
    }

    @Override
    public List<EsbEmployeeDO> getEmpInfoByJobIds(List<Long> postIds) {
        if (CollectionUtil.isEmpty(postIds)) {
            return new ArrayList<>();
        }
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EsbEmployeeDO::getJobId, postIds);
        return this.list(queryWrapper);
    }

    @Override
    public List<EsbEmployeeDO> getEmpInfoByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return new ArrayList<>();
        }
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(EsbEmployeeDO::getEmpName, name).last("limit 50");
        return this.list(queryWrapper);
    }

    @Override
    public List<EsbEmployeeDO> listByJobIdsForAgency(List<Long> jobIds) {
        // 当一个岗位对应多个人员时候，只返回一个，优先去在职的，如果多个在职去第一个。多个离职的取第一个。
        if (CollectionUtil.isEmpty(jobIds)) {
            return new ArrayList<>();
        }
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EsbEmployeeDO::getJobId, jobIds);
        queryWrapper.lambda().orderByDesc(EsbEmployeeDO::getId);
        List<EsbEmployeeDO> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            List<EsbEmployeeDO> result = new ArrayList<>();
            Map<Long, List<EsbEmployeeDO>> esbEmpMap = list.stream().collect(Collectors.groupingBy(EsbEmployeeDO::getJobId));
            for (Long jobId : esbEmpMap.keySet()) {
                List<EsbEmployeeDO> esbEmployeeDOS = esbEmpMap.get(jobId);
                List<EsbEmployeeDO> use = esbEmployeeDOS.stream().filter(item -> item.getStatus().equals("正式") || item.getStatus().equals("试用")).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(use)) {
                    result.add(use.get(0));
                    continue;
                }
                EsbEmployeeDO esbEmployeeDO = esbEmployeeDOS.get(0);
                esbEmployeeDO.setEmpName(null);
                esbEmployeeDO.setEmpId(null);
                result.add(esbEmployeeDO);
            }
            return result;
        }
        return new ArrayList<>();
    }

    @Override
    public Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> longs, List<Long> bdOrgIds, String tableSuffix) {
        if (CollUtil.isEmpty(longs)) {
            return MapUtil.empty();
        }

        List<EsbOrganizationDTO> orgDTOList = PojoUtils.map(esbOrganizationService.listSuffixByOrgIds(longs,tableSuffix), EsbOrganizationDTO.class);

        // 获取事业部部门ID列表

        // 获取所有部门简单信息列表
        List<SimpleEsbOrgInfoBO> allOrgBOList = esbOrganizationService.listSuffixAll(true,tableSuffix);
        Map<Long, SimpleEsbOrgInfoBO> allOrgBOMap = allOrgBOList.stream().collect(Collectors.toMap(SimpleEsbOrgInfoBO::getOrgId, Function.identity()));

        Map<Long, SimpleEsbOrgInfoBO> orgParentBOMap = CollUtil.newHashMap();
        orgDTOList.forEach(e -> {
            SimpleEsbOrgInfoBO bdOrgInfoBO = this.getParentIdWithin(allOrgBOMap, PojoUtils.map(e, SimpleEsbOrgInfoBO.class), bdOrgIds);
            if (bdOrgInfoBO != null) {
                orgParentBOMap.put(e.getOrgId(), bdOrgInfoBO);
            }
        });

        if (CollUtil.isEmpty(orgParentBOMap)) {
            return MapUtil.empty();
        }


        List<Long> selectedDbOrgIds = orgParentBOMap.values().stream().map(e -> e.getOrgId()).distinct().collect(Collectors.toList());
        List<EsbOrganizationDTO> selectedDbOrgDTOList = PojoUtils.map(esbOrganizationService.listSuffixByOrgIds(selectedDbOrgIds,tableSuffix),EsbOrganizationDTO.class);
        Map<Long, EsbOrganizationDTO> selectedBdDTOMap = selectedDbOrgDTOList.stream().collect(Collectors.toMap(EsbOrganizationDTO::getOrgId, Function.identity()));

        Map<Long, EsbOrganizationDTO> orgBdDTOMap = CollUtil.newHashMap();
        orgParentBOMap.forEach((k, v) -> {
            if (selectedBdDTOMap.containsKey(v.getOrgId())) {
                orgBdDTOMap.put(k, selectedBdDTOMap.get(v.getOrgId()));
            }
        });

        return orgBdDTOMap;
    }

    @Override
    public List<EsbEmployeeDO> getProvincialManagerByYxDeptAndYxProvinceAndJobNames(String yxDept, String yxProvince, String yxArea, String jobNames){
        if (StringUtils.isEmpty(yxDept) || StringUtils.isEmpty(yxProvince)) {
            return ListUtil.empty();
        }
        cn.hutool.core.lang.Assert.notBlank(jobNames, "参数 jobName 不能为空");
        LambdaQueryWrapper<EsbEmployeeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EsbEmployeeDO::getStatus, ListUtil.toList("正式", "试用"));
        queryWrapper.eq(EsbEmployeeDO::getJobNames, jobNames);
        queryWrapper.eq(EsbEmployeeDO::getYxDept, yxDept);
        queryWrapper.eq(EsbEmployeeDO::getYxProvince, yxProvince);
        if (StrUtil.isNotBlank(yxArea)) {
            queryWrapper.eq(EsbEmployeeDO::getYxArea, yxArea);
        }
        List<EsbEmployeeDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<EsbEmployeeDO> getEsbEmployeeDTOList() {
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbEmployeeDO::getState, EMP_STATE_NORMAL);
        return this.list(queryWrapper);
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

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<EsbEmployeeDO> listSuffixByJobIds(List<Long> jobIds, String tableSuffix) {
        QueryWrapper<EsbEmployeeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EsbEmployeeDO::getJobId, jobIds);
        return this.list(queryWrapper);
    }
}
