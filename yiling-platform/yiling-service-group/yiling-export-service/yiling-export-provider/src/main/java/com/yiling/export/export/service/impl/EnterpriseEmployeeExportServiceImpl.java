package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeExportDTO;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.member.api.MemberReturnApi;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.MemberReturnExportDTO;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.enums.MemberReturnAuthStatusEnum;
import com.yiling.user.member.enums.MemberReturnStatusEnum;
import com.yiling.user.system.api.RoleApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 商家后台-中台-员工管理导出
 *
 * @author: lun.yu
 * @date: 2023-06-06
 */
@Service("enterpriseEmployeeExportService")
public class EnterpriseEmployeeExportServiceImpl implements BaseExportQueryDataService<QueryEmployeePageListRequest> {

    @DubboReference
    UserApi userApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    RoleApi roleApi;
    @DubboReference
    DepartmentApi departmentApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("userId", "账号ID");
        FIELD.put("name", "姓名");
        FIELD.put("mobile", "手机号");
        FIELD.put("code", "工号");
        FIELD.put("departmentName", "部门");
        FIELD.put("roleName", "角色");
        FIELD.put("typeName", "用户类型");
        FIELD.put("parentName", "上级领导");
        FIELD.put("statusName", "状态");
        FIELD.put("createInfo", "创建信息");
    }

    @Override
    public QueryExportDataDTO queryData(QueryEmployeePageListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<EnterpriseEmployeeDTO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = employeeApi.pageList(request);
            if ( Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            Page<EnterpriseEmployeeExportDTO> exportDTOPage = PojoUtils.map(page, EnterpriseEmployeeExportDTO.class);

            List<EnterpriseEmployeeDTO> list = page.getRecords();
            // 获取用户字典
            List<Long> userIds = list.stream().map(EnterpriseEmployeeDTO::getUserId).collect(Collectors.toList());
            List<Long> createUserIds = list.stream().map(EnterpriseEmployeeDTO::getCreateUser).distinct().collect(Collectors.toList());
            List<Long> leaderUserIds = list.stream().map(EnterpriseEmployeeDTO::getParentId).distinct().collect(Collectors.toList());
            List<Long> allUserIds = CollUtil.union(userIds, createUserIds, leaderUserIds).stream().distinct().collect(Collectors.toList());
            List<UserDTO> userDTOList = userApi.listByIds(allUserIds);
            Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

            // 获取部门字典
            List<Long> employeeIds = list.stream().map(EnterpriseEmployeeDTO::getId).distinct().collect(Collectors.toList());
            Map<Long, List<Long>> employeeDepartmentIdsMap = employeeApi.listDepartmentIdsByEmployeeIds(employeeIds);
            List<Long> departmentIds = employeeDepartmentIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            List<EnterpriseDepartmentDTO> departmentDTOList = departmentApi.listByIds(departmentIds);
            Map<Long, EnterpriseDepartmentDTO> departmentDTOMap = departmentDTOList.stream().collect(Collectors.toMap(EnterpriseDepartmentDTO::getId, Function.identity()));

            // 获取角色字典
            Long eid = list.stream().map(EnterpriseEmployeeDTO::getEid).findFirst().orElse(0L);
            Map<Long, List<RoleDTO>> userRolesMap = roleApi.listByEidUserIds(PermissionAppEnum.MALL_ADMIN, eid, userIds);

            exportDTOPage.getRecords().forEach(employeeExportDTO -> {

                // 用户信息
                UserDTO userDTO = userDTOMap.get(employeeExportDTO.getUserId());
                if (userDTO != null) {
                    employeeExportDTO.setName(userDTO.getName());
                    employeeExportDTO.setMobile(userDTO.getMobile());
                }
                employeeExportDTO.setParentName(userDTOMap.getOrDefault(employeeExportDTO.getParentId(), new UserDTO()).getName());
                // 部门名
                List<Long> userDepartmentIds = employeeDepartmentIdsMap.get(employeeExportDTO.getId());
                if (CollUtil.isNotEmpty(userDepartmentIds)) {
                    StringBuilder departmentNameBuilder = new StringBuilder();
                    userDepartmentIds.forEach(departmentId -> {
                        EnterpriseDepartmentDTO enterpriseDepartmentDTO = departmentDTOMap.get(departmentId);
                        if (enterpriseDepartmentDTO != null) {
                            departmentNameBuilder.append(enterpriseDepartmentDTO.getName()).append("/");
                        }
                    });
                    employeeExportDTO.setDepartmentName(StrUtil.removeSuffix(departmentNameBuilder.toString(), "/"));
                }

                // 角色名
                List<RoleDTO> userRoles = userRolesMap.get(employeeExportDTO.getUserId());
                if (CollUtil.isNotEmpty(userRoles)) {
                    employeeExportDTO.setRoleName(userRoles.stream().map(RoleDTO::getName).collect(Collectors.joining("/")));
                }
                // 员工类型
                employeeExportDTO.setTypeName(Objects.requireNonNull(EmployeeTypeEnum.getByCode(employeeExportDTO.getType())).getName());
                // 状态
                employeeExportDTO.setStatusName(Objects.requireNonNull(EnableStatusEnum.getByCode(employeeExportDTO.getStatus())).getName());
                // 创建信息
                String time = DateUtil.format(employeeExportDTO.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
                UserDTO createUser = userDTOMap.get(employeeExportDTO.getCreateUser());
                if (Objects.nonNull(createUser)) {
                    employeeExportDTO.setCreateInfo(createUser.getName() + " " + time);
                }

                data.add(BeanUtil.beanToMap(employeeExportDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("员工列表导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public QueryEmployeePageListRequest getParam(Map<String, Object> map) {
        QueryEmployeePageListRequest request = PojoUtils.map(map, QueryEmployeePageListRequest.class);
        request.setEid(Long.parseLong(map.getOrDefault("eid", 0L).toString()));
        return request;
    }

}
