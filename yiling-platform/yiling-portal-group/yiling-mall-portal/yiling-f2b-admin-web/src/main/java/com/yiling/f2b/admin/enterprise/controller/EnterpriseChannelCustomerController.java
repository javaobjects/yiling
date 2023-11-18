package com.yiling.f2b.admin.enterprise.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.enterprise.form.AddEasInfoForm;
import com.yiling.f2b.admin.enterprise.form.DeleteEasInfoForm;
import com.yiling.f2b.admin.enterprise.form.QueryChannelCustomerPageListForm;
import com.yiling.f2b.admin.enterprise.form.SaveChannelCustomerForm;
import com.yiling.f2b.admin.enterprise.vo.ChannelCustomerDetailVO;
import com.yiling.f2b.admin.enterprise.vo.CustomerContactVO;
import com.yiling.f2b.admin.enterprise.vo.CustomerGroupVO;
import com.yiling.f2b.admin.enterprise.vo.EnterpriseChannelCustomerItemVO;
import com.yiling.f2b.admin.payment.vo.PaymentMethodVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.ChannelCustomerApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseChannelCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerEasInfoRequest;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerContactPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerInfoRequest;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 渠道商模块 Controller
 *
 * @author: yuecheng.chen
 * @date: 2021/6/4
 */
@RestController
@RequestMapping("/channel")
@Api(tags = "渠道商模块接口")
@Slf4j
public class EnterpriseChannelCustomerController {

    @DubboReference(timeout = 1000 * 10)
    ChannelCustomerApi channelCustomerApi;
    @DubboReference
    CustomerContactApi customerContactApi;
    @DubboReference
    CustomerGroupApi customerGroupApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    EmployeeApi      employeeApi;
    @DubboReference
    DepartmentApi    departmentApi;
    @DubboReference
    PaymentMethodApi paymentMethodApi;
    @DubboReference
    CustomerApi customerApi;

    @ApiOperation(value = "客户分页列表")
    @PostMapping("/pageList")
    public Result<Page<EnterpriseChannelCustomerItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryChannelCustomerPageListForm form) {
        QueryChannelCustomerPageListRequest request = PojoUtils.map(form, QueryChannelCustomerPageListRequest.class);
        request.setEid(Constants.YILING_EID);
        Page<EnterpriseChannelCustomerDTO> page = channelCustomerApi.pageList(request);
        List<EnterpriseChannelCustomerDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(PojoUtils.map(page, EnterpriseChannelCustomerItemVO.class));
        }

        List<EnterpriseChannelCustomerItemVO> list = records.stream().map(enterpriseChannelCustomerDto -> {
            EnterpriseChannelCustomerItemVO item = PojoUtils.map(enterpriseChannelCustomerDto, EnterpriseChannelCustomerItemVO.class);
            if (Objects.nonNull(enterpriseChannelCustomerDto.getStatus())) {
                item.setStatus(EnterpriseStatusEnum.getByCode(enterpriseChannelCustomerDto.getStatus()).getName());
            }
            // 处理客户支付方式
            item.setCustomerPaymentMethods(this.doCustomerPaymentMethod(item.getEid(), item.getCustomerEid()));
            return item;
        }).collect(Collectors.toList());

        Page<EnterpriseChannelCustomerItemVO> pageVO = PojoUtils.map(page, EnterpriseChannelCustomerItemVO.class);
        pageVO.setRecords(list);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取单个渠道商信息")
    @GetMapping("/get")
    public Result<ChannelCustomerDetailVO> get(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("customerEid") Long customerEid) {
        Long eid = staffInfo.getCurrentEid();

        EnterpriseChannelCustomerDTO enterpriseChannelCustomerDTO = Optional.ofNullable(channelCustomerApi.get(eid, customerEid))
                .orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_CHANNEL_CUSTOMER_NOT_EXIST));

        ChannelCustomerDetailVO detailVO = PojoUtils.map(enterpriseChannelCustomerDTO, ChannelCustomerDetailVO.class);

        // 商务联系人；
        QueryCustomerContactPageListRequest queryCustomerContactPageListRequest = new QueryCustomerContactPageListRequest().setEid(eid).setCustomerEid(customerEid);
        Page<EnterpriseCustomerContactDTO> customerContactDtoPage = customerContactApi.pageList(queryCustomerContactPageListRequest);
        if (CollUtil.isNotEmpty(customerContactDtoPage.getRecords())) {
            List<Long> userIds = customerContactDtoPage.getRecords().stream().map(EnterpriseCustomerContactDTO::getContactUserId).collect(Collectors.toList());
            List<UserDTO> userDTOList = userApi.listByIds(userIds);
            Map<Long,UserDTO> userMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(),(key1, key2)->key2));
            // 筛选出部门信息
            List<EnterpriseEmployeeDTO> employeeDTOList = employeeApi.listByEidUserIds(eid, userIds);
            Map<Long,EnterpriseEmployeeDTO> employeeMap = employeeDTOList.stream().collect(Collectors.toMap(EnterpriseEmployeeDTO::getUserId, Function.identity(),(key1, key2)->key2));

            // 获取部门字典
            List<Long> employeeIds = employeeDTOList.stream().map(EnterpriseEmployeeDTO::getId).distinct().collect(Collectors.toList());
            Map<Long, List<Long>> employeeDepartmentIdsMap = employeeApi.listDepartmentIdsByEmployeeIds(employeeIds);
            List<Long> departmentIds = employeeDepartmentIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            List<EnterpriseDepartmentDTO> departmentDTOList = departmentApi.listByIds(departmentIds);
            Map<Long, EnterpriseDepartmentDTO> departmentDTOMap = departmentDTOList.stream().collect(Collectors.toMap(EnterpriseDepartmentDTO::getId, Function.identity()));

            List<CustomerContactVO> customerContacts = customerContactDtoPage.getRecords().stream().map(e -> {
                CustomerContactVO customerContactVO = new CustomerContactVO();
                UserDTO userDTO = userMap.get(e.getContactUserId());
                customerContactVO.setId(userDTO.getId());
                customerContactVO.setMobile(userDTO.getMobile());
                customerContactVO.setName(userDTO.getName());

                EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeMap.getOrDefault(e.getContactUserId(),new EnterpriseEmployeeDTO());
                List<Long> userDepartmentIds = employeeDepartmentIdsMap.get(enterpriseEmployeeDTO.getId());
                if (CollUtil.isNotEmpty(userDepartmentIds)) {
                    StringBuilder departmentNameBuilder = new StringBuilder();
                    userDepartmentIds.forEach(departmentId -> {
                        EnterpriseDepartmentDTO enterpriseDepartmentDTO = departmentDTOMap.get(departmentId);
                        if (enterpriseDepartmentDTO != null) {
                            departmentNameBuilder.append(enterpriseDepartmentDTO.getName()).append("/");
                        }
                    });
                    customerContactVO.setDepartmentName(StrUtil.removeSuffix(departmentNameBuilder.toString(), "/"));
                }
                return customerContactVO;
            }).collect(Collectors.toList());

            detailVO.setCustomerContacts(customerContacts);
        }

        // 客户支付方式列表
        List<PaymentMethodDTO> customerPaymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(eid, customerEid, PlatformEnum.POP);
        List<Long> customerPaymentMethodIds = customerPaymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).distinct().collect(Collectors.toList());
        // 预定义的支付方式列表
        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByChannelId(staffInfo.getEnterpriseChannel().getCode());

        // 客户支付方式
        List<ChannelCustomerDetailVO.PaymentMethodVO> paymentMethodVOList = paymentMethodDTOList.stream().map(e -> {
            ChannelCustomerDetailVO.PaymentMethodVO paymentMethodVO = new ChannelCustomerDetailVO.PaymentMethodVO();
            paymentMethodVO.setId(e.getCode());
            paymentMethodVO.setName(e.getName());
            paymentMethodVO.setRemark(PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.PAYMENT_DAYS ? "提示：开通账期支付时请确定已设置账期额度" : null);
            paymentMethodVO.setChecked(customerPaymentMethodIds.contains(e.getCode()));
            return paymentMethodVO;
        }).collect(Collectors.toList());
        detailVO.setCustomerPaymentMethods(paymentMethodVOList);

        // 客户分组
        if (Objects.nonNull(enterpriseChannelCustomerDTO.getCustomerGroupId())) {
            EnterpriseCustomerGroupDTO customerGroupDTO = customerGroupApi.getById(enterpriseChannelCustomerDTO.getCustomerGroupId());
            CustomerGroupVO customerGroupVO = PojoUtils.map(customerGroupDTO, CustomerGroupVO.class);
            detailVO.setCustomerGroup(customerGroupVO);
        }

        // EAS信息
        List<EnterpriseCustomerEasDTO> enterpriseCustomerEasDTOList = customerApi.getCustomerEasInfos(eid, customerEid);
        detailVO.setEasInfoList(PojoUtils.map(enterpriseCustomerEasDTOList, ChannelCustomerDetailVO.EasInfoVO.class));

        return Result.success(detailVO);
    }

    @ApiOperation(value = "保存渠道商信息")
    @PostMapping("/save")
    @Log(title = "保存渠道商信息", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> save(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveChannelCustomerForm form) {
        if (staffInfo.getCurrentEid() == null) {
            return Result.failed("保存渠道商信息：当前用户企业ID为空！");
        }
        UpdateCustomerInfoRequest request = PojoUtils.map(form, UpdateCustomerInfoRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPlatformEnum(PlatformEnum.POP);
        return Result.success(new BoolObject(customerApi.updateCustomerInfo(request)));
    }

    @ApiOperation(value = "新增EAS信息")
    @PostMapping("/addEasInfo")
    @Log(title = "新增EAS信息", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> addEasInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddEasInfoForm form) {
        AddCustomerEasInfoRequest request = PojoUtils.map(form, AddCustomerEasInfoRequest.class);
        request.setEasCode(this.clearSpace(request.getEasCode()));
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(customerApi.addEasInfo(request)));
    }

    @ApiOperation(value = "删除EAS信息")
    @PostMapping("/deleteEasInfo")
    @Log(title = "删除EAS信息", businessType = BusinessTypeEnum.DELETE)
    public Result<Void> deleteEasInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid DeleteEasInfoForm form) {
        customerApi.deleteEasInfo(form.getId(),staffInfo.getCurrentUserId());
        return Result.success();
    }

    /**
     * 处理客户支付方式JSON
     *
     * @param eid           企业ID
     * @param customerEid   客户ID
     * @return
     */
    private List<PaymentMethodVO> doCustomerPaymentMethod(Long eid, Long customerEid) {
        Map<Long, List<PaymentMethodDTO>> longListMap = paymentMethodApi.listByEidAndCustomerEids(eid, ListUtil.list(false, customerEid), PlatformEnum.POP);
        return PojoUtils.map(longListMap.get(customerEid), PaymentMethodVO.class);
    }

    /**
     * 清除空格等特殊字符
     * @param str
     * @return
     */
    private String clearSpace(String str){
        String pattern = "\\s*|\t|\r|\n";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

}
