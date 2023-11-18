package com.yiling.f2b.admin.enterprise.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.enterprise.form.AddCustomerContactForm;
import com.yiling.f2b.admin.enterprise.form.QueryCustomerContactPageListForm;
import com.yiling.f2b.admin.enterprise.form.RemoveCustomerContactForm;
import com.yiling.f2b.admin.enterprise.vo.CustomerContactVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerContactRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerContactPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveCustomerContactRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户商务联系人模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/4
 */
@RestController
@RequestMapping("/customer/contact")
@Api(tags = "客户商务联系人模块接口")
@Slf4j
public class CustomerContactController extends BaseController {

    @DubboReference
    CustomerContactApi      customerContactApi;
    @DubboReference
    EmployeeApi             employeeApi;
    @DubboReference
    UserApi       userApi;
    @DubboReference
    DepartmentApi departmentApi;

    @ApiOperation(value = "查询商务联系人分页列表")
    @PostMapping("/pageList")
    public Result<Page<CustomerContactVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryCustomerContactPageListForm form) {
        QueryCustomerContactPageListRequest request = PojoUtils.map(form, QueryCustomerContactPageListRequest.class);
        request.setEid(staffInfo.getCurrentEid());

        Page<EnterpriseCustomerContactDTO> page = customerContactApi.pageList(request);
        List<EnterpriseCustomerContactDTO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(request.getPage());
        }

        List<Long> userIds = list.stream().map(EnterpriseCustomerContactDTO::getContactUserId).collect(Collectors.toList());
        List<EnterpriseEmployeeDTO> enterpriseEmployeeDTOList = employeeApi.listByEidUserIds(staffInfo.getCurrentEid(), userIds);
        Map<Long, EnterpriseEmployeeDTO> enterpriseEmployeeDTOMap = enterpriseEmployeeDTOList.stream().collect(Collectors.toMap(EnterpriseEmployeeDTO::getUserId, Function.identity()));

        // 获取用户字典
        List<UserDTO> userDTOList = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        // 获取部门字典
        List<Long> employeeIds = enterpriseEmployeeDTOList.stream().map(EnterpriseEmployeeDTO::getId).distinct().collect(Collectors.toList());
        Map<Long, List<Long>> employeeDepartmentIdsMap = employeeApi.listDepartmentIdsByEmployeeIds(employeeIds);
        List<Long> departmentIds = employeeDepartmentIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<EnterpriseDepartmentDTO> departmentDTOList = departmentApi.listByIds(departmentIds);
        Map<Long, EnterpriseDepartmentDTO> departmentDTOMap = departmentDTOList.stream().collect(Collectors.toMap(EnterpriseDepartmentDTO::getId, Function.identity()));

        List<CustomerContactVO> voList = ListUtil.toList();
        list.forEach(e -> {
            CustomerContactVO vo = new CustomerContactVO();

            EnterpriseEmployeeDTO enterpriseEmployeeDTO = enterpriseEmployeeDTOMap.get(e.getContactUserId());
            if (enterpriseEmployeeDTO != null) {
                if (userDTOMap.containsKey(enterpriseEmployeeDTO.getUserId())) {
                    vo.setId(enterpriseEmployeeDTO.getUserId());
                    vo.setName(userDTOMap.get(enterpriseEmployeeDTO.getUserId()).getName());
                    vo.setMobile(userDTOMap.get(enterpriseEmployeeDTO.getUserId()).getMobile());
                }

                List<Long> userDepartmentIds = employeeDepartmentIdsMap.get(enterpriseEmployeeDTO.getId());
                if (CollUtil.isNotEmpty(userDepartmentIds)) {
                    StringBuilder departmentNameBuilder = new StringBuilder();
                    userDepartmentIds.forEach(departmentId -> {
                        EnterpriseDepartmentDTO enterpriseDepartmentDTO = departmentDTOMap.get(departmentId);
                        if (enterpriseDepartmentDTO != null) {
                            departmentNameBuilder.append(enterpriseDepartmentDTO.getName()).append("/");
                        }
                    });
                    vo.setDepartmentName(StrUtil.removeSuffix(departmentNameBuilder.toString(), "/"));
                }

                voList.add(vo);
            }
        });

        Page<CustomerContactVO> voPage = request.getPage();
        voPage.setRecords(voList);

        return Result.success(voPage);
    }

    @ApiOperation(value = "添加商务联系人")
    @PostMapping("/add")
    @Log(title = "添加商务联系人", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddCustomerContactForm form) {
        AddCustomerContactRequest request = PojoUtils.map(form, AddCustomerContactRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = customerContactApi.add(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "移除商务联系人")
    @PostMapping("/remove")
    @Log(title = "移除商务联系人", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> remove(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid RemoveCustomerContactForm form) {
        RemoveCustomerContactRequest request = PojoUtils.map(form, RemoveCustomerContactRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = customerContactApi.remove(request);
        return Result.success(new BoolObject(result));
    }

}
