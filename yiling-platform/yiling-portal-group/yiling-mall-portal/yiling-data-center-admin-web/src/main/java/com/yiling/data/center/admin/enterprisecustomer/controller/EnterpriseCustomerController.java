package com.yiling.data.center.admin.enterprisecustomer.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.data.center.admin.enterprisecustomer.form.QueryCustomerPageListForm;
import com.yiling.data.center.admin.enterprisecustomer.form.UpdateEnterpriseCustomerLineForm;
import com.yiling.data.center.admin.enterprisecustomer.vo.CustomerDetailVO;
import com.yiling.data.center.admin.enterprisecustomer.vo.CustomerVO;
import com.yiling.data.center.admin.enterprisecustomer.vo.EnterpriseCustomerListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseCustomerLineApi;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerLineDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerLineListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseCustomerLineRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业客户 Controller
 *
 * @author: lun.yu
 * @date: 2021/11/3
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "企业客户接口")
@Slf4j
public class EnterpriseCustomerController extends BaseController {

    @DubboReference(timeout = 1000 * 10)
    CustomerApi customerApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CustomerGroupApi customerGroupApi;
    @DubboReference
    CertificateApi certificateApi;
    @DubboReference
    EnterpriseCustomerLineApi enterpriseCustomerLineApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "客户分页列表")
    @PostMapping("/pageList")
    public Result<Page<EnterpriseCustomerListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryCustomerPageListForm form) {
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setCustomerGroupId((request.getCustomerGroupId()!=null && request.getCustomerGroupId() == 0 ) ? null : request.getCustomerGroupId());

        Page<EnterpriseCustomerDTO> page = customerApi.pageList(request);
        Page<EnterpriseCustomerListItemVO> pageVO = PojoUtils.map(page,EnterpriseCustomerListItemVO.class);

        List<Long> customerEidList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerEid).collect(Collectors.toList());
        List<Long> customerGroupList = pageVO.getRecords().stream().map(EnterpriseCustomerListItemVO::getCustomerGroupId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(customerEidList)){
            //企业信息
            List<EnterpriseDTO> list = enterpriseApi.listByIds(customerEidList);
            Map<Long, EnterpriseDTO> enterpriseDtoMap = list.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));

            //客户分组信息
            Map<Long, String> customerGroupMap = MapUtil.newHashMap();
            if(CollUtil.isNotEmpty(customerGroupList)){
                List<EnterpriseCustomerGroupDTO> customerGroupDtoList = customerGroupApi.listByIds(customerGroupList);
                customerGroupMap = customerGroupDtoList.stream().collect(Collectors.toMap(EnterpriseCustomerGroupDTO::getId, EnterpriseCustomerGroupDTO::getName));
            }

            Map<Long, String> finalCustomerGroupMap = customerGroupMap;
            pageVO.getRecords().forEach(enterpriseCustomerListItemVO -> {
                Integer source = enterpriseCustomerListItemVO.getSource();
                EnterpriseDTO enterpriseDTO = enterpriseDtoMap.getOrDefault(enterpriseCustomerListItemVO.getCustomerEid(), new EnterpriseDTO());
                PojoUtils.map(enterpriseDTO , enterpriseCustomerListItemVO);
                enterpriseCustomerListItemVO.setCustomerGroupName(finalCustomerGroupMap.get(enterpriseCustomerListItemVO.getCustomerGroupId()));
                enterpriseCustomerListItemVO.setCustomerErpName(enterpriseCustomerListItemVO.getCustomerName());
                enterpriseCustomerListItemVO.setCustomerName(enterpriseDTO.getName());
                enterpriseCustomerListItemVO.setSource(source);
                enterpriseCustomerListItemVO.setAddress(new StringJoiner(" ").add(enterpriseCustomerListItemVO.getProvinceName()).add(enterpriseCustomerListItemVO.getCityName())
                        .add(enterpriseCustomerListItemVO.getRegionName()).add(enterpriseCustomerListItemVO.getAddress()).toString());
            });
        }

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取客户信息")
    @GetMapping("/get")
    public Result<CustomerDetailVO> get(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("customerEid") Long customerEid) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = Optional.ofNullable(customerApi.get(staffInfo.getCurrentEid(), customerEid))
                .orElseThrow(()->new BusinessException(UserErrorCode.CUSTOMER_NOT_EXIST));

        // 获取客户企业信息
        EnterpriseDTO customerEnterpriseDTO = enterpriseApi.getById(customerEid);

        CustomerVO customerVO = new CustomerVO();
        PojoUtils.map(customerEnterpriseDTO, customerVO);
        PojoUtils.map(enterpriseCustomerDTO, customerVO);
        customerVO.setAddress(new StringJoiner(" ").add(customerEnterpriseDTO.getProvinceName()).add(customerEnterpriseDTO.getCityName())
                .add(customerEnterpriseDTO.getRegionName()).add(customerEnterpriseDTO.getAddress()).toString());

        //企业资质
        List<EnterpriseCertificateDTO> certificateDTOList = certificateApi.listByEid(customerEid);

        List<CustomerDetailVO.EnterpriseCertificateVO> certificateVoList = PojoUtils.map(certificateDTOList, CustomerDetailVO.EnterpriseCertificateVO.class);
        certificateVoList.forEach(enterpriseCertificateVO -> enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateVO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE)));

        CustomerDetailVO pageVO = new CustomerDetailVO();
        pageVO.setCustomerInfo(customerVO);
        pageVO.setErpCustomerInfo(PojoUtils.map(enterpriseCustomerDTO, CustomerDetailVO.ErpCustomerVO.class));
        pageVO.setCertificateList(certificateVoList);

        //使用产品线
        QueryCustomerLineListRequest request = new QueryCustomerLineListRequest();
        request.setCustomerId(enterpriseCustomerDTO.getId());
        List<Integer> useLineMap = enterpriseCustomerLineApi.queryList(request).stream().map(EnterpriseCustomerLineDTO::getUseLine).collect(Collectors.toList());

        List<CustomerDetailVO.EnterpriseCustomerLineVO> customerLineList = ListUtil.toList();
        for (EnterpriseCustomerLineEnum lineEnum : EnterpriseCustomerLineEnum.values()) {
            CustomerDetailVO.EnterpriseCustomerLineVO customerLineVO = new CustomerDetailVO.EnterpriseCustomerLineVO();
            customerLineVO.setUseLine(lineEnum.getCode());
            customerLineVO.setUseLineName(lineEnum.getName());
            customerLineVO.setChecked(useLineMap.contains(lineEnum.getCode()));
            customerLineList.add(customerLineVO);
        }
        pageVO.setCustomerLineList(customerLineList);

        return Result.success(pageVO);
    }

    @ApiOperation(value = "修改使用产品线")
    @PostMapping("/updateLine")
    @Log(title = "修改使用产品线", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateLine(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateEnterpriseCustomerLineForm form) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = Optional.ofNullable(customerApi.get(staffInfo.getCurrentEid(), form.getCustomerEid()))
                .orElseThrow(()->new BusinessException(UserErrorCode.CUSTOMER_NOT_EXIST));

        UpdateEnterpriseCustomerLineRequest request = PojoUtils.map(form,UpdateEnterpriseCustomerLineRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setCustomerId(enterpriseCustomerDTO.getId());
        request.setEid(staffInfo.getCurrentEid());
        request.setEname(enterpriseApi.getById(staffInfo.getCurrentEid()).getName());
        enterpriseCustomerLineApi.updateLine(request);

        return Result.success();
    }


}
