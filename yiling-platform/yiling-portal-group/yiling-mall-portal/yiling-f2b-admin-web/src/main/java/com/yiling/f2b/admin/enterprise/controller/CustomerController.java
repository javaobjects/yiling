package com.yiling.f2b.admin.enterprise.controller;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
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
import com.google.common.collect.Lists;
import com.yiling.f2b.admin.enterprise.form.QueryCustomerPageListForm;
import com.yiling.f2b.admin.enterprise.form.UpdateCustomerInfoForm;
import com.yiling.f2b.admin.enterprise.vo.CustomerDetailPageVO;
import com.yiling.f2b.admin.enterprise.vo.CustomerListItemVO;
import com.yiling.f2b.admin.enterprise.vo.CustomerVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.pricing.goods.api.GoodsPriceCustomerApi;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerInfoRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "客户模块接口")
@Slf4j
public class CustomerController extends BaseController {

    @DubboReference(timeout = 1000 * 10)
    CustomerApi   customerApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PaymentMethodApi paymentMethodApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    GoodsPriceCustomerApi goodsPriceCustomerApi;

    @ApiOperation(value = "客户分页列表")
    @PostMapping("/pageList")
    public Result<Page<CustomerListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCustomerPageListForm form) {
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.list(false, staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.POP.getCode());

        Page<EnterpriseCustomerDTO> page = customerApi.pageList(request);
        List<EnterpriseCustomerDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(PojoUtils.map(page,CustomerListItemVO.class));
        }

        List<Long> customerEids = records.stream().map(EnterpriseCustomerDTO::getCustomerEid).collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(customerEids);
        Map<Long, EnterpriseDTO> enterpriseDtoMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

        // 客户支付方式字典
        Map<Long, List<PaymentMethodDTO>> customerPaymentMethodMap = paymentMethodApi.listByEidAndCustomerEids(staffInfo.getCurrentEid(), customerEids, PlatformEnum.POP);

        // 根据商品id查询客户定价
        Map<Long, List<GoodsPriceCustomerDTO>> goodsPriceCustomerMap = goodsPriceCustomerApi.getByGoodsId(form.getGoodsId()).stream()
            .collect(Collectors.groupingBy(GoodsPriceCustomerDTO::getCustomerEid));

        List<CustomerListItemVO> list = Lists.newArrayList();
        records.forEach(e -> {
            CustomerListItemVO item = PojoUtils.map(e, CustomerListItemVO.class);

            EnterpriseDTO customerEnterpriseDTO = enterpriseDtoMap.get(e.getCustomerEid());
            if (customerEnterpriseDTO != null) {
                item.setCustomerName(customerEnterpriseDTO.getName());
                item.setCustomerType(EnterpriseTypeEnum.getByCode(customerEnterpriseDTO.getType()).getName());
                item.setContactor(customerEnterpriseDTO.getContactor());
                item.setContactorPhone(customerEnterpriseDTO.getContactorPhone());
                item.setAddress(new StringJoiner(" ")
                        .add(customerEnterpriseDTO.getProvinceName())
                        .add(customerEnterpriseDTO.getCityName())
                        .add(customerEnterpriseDTO.getRegionName())
                        .add(customerEnterpriseDTO.getAddress())
                        .toString());

                // 支付方式集合
                List<PaymentMethodDTO> customerPaymentMethodDTOList = customerPaymentMethodMap.get(e.getCustomerEid());
                if (CollUtil.isNotEmpty(customerPaymentMethodDTOList)) {
                    item.setPaymentMethods(customerPaymentMethodDTOList.stream().map(PaymentMethodDTO::getName).collect(Collectors.toList()));
                }
                // 已添加过的客户
                List<GoodsPriceCustomerDTO> goodsPriceCustomer = goodsPriceCustomerMap.get(e.getCustomerEid());
                if (CollUtil.isNotEmpty(goodsPriceCustomer)) {
                    item.setSelectedFlag(true);
                }
            }

            list.add(item);
        });

        Page<CustomerListItemVO> pageVO = PojoUtils.map(page, CustomerListItemVO.class);
        pageVO.setRecords(list);

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取单个客户信息")
    @GetMapping("/get")
    public Result<CustomerDetailPageVO> get(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("customerEid") Long customerEid) {
        EnterpriseCustomerDTO enterpriseCustomerDTO = customerApi.get(staffInfo.getCurrentEid(), customerEid);
        if (enterpriseCustomerDTO == null) {
            return Result.failed("客户信息不存在");
        }

        // 获取客户企业信息
        EnterpriseDTO customerEnterpriseDTO = enterpriseApi.getById(customerEid);

        CustomerVO customerVO = new CustomerVO();
        PojoUtils.map(customerEnterpriseDTO, customerVO);
        PojoUtils.map(enterpriseCustomerDTO, customerVO);
        customerVO.setAddress(new StringJoiner(" ")
                .add(customerEnterpriseDTO.getProvinceName())
                .add(customerEnterpriseDTO.getCityName())
                .add(customerEnterpriseDTO.getRegionName())
                .add(customerEnterpriseDTO.getAddress())
                .toString());

        // 客户支付方式列表
        List<PaymentMethodDTO> customerPaymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(staffInfo.getCurrentEid(), customerEid, PlatformEnum.POP);
        List<Long> customerPaymentMethodIds = customerPaymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).distinct().collect(Collectors.toList());
        // 预定义的支付方式列表
        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByChannelId(staffInfo.getEnterpriseChannel().getCode());
        List<CustomerDetailPageVO.PaymentMethodVO> paymentMethodVOList = CollUtil.newArrayList();
        paymentMethodDTOList.forEach(e -> {
            CustomerDetailPageVO.PaymentMethodVO paymentMethodVO = new CustomerDetailPageVO.PaymentMethodVO();
            paymentMethodVO.setId(e.getCode());
            paymentMethodVO.setName(e.getName());
            if (PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.PAYMENT_DAYS) {
                paymentMethodVO.setRemark("提示：开通账期支付时请确定已设置账期额度");
            }
            paymentMethodVO.setChecked(customerPaymentMethodIds.contains(e.getCode()));
            paymentMethodVOList.add(paymentMethodVO);
        });

        // 账期信息
        PaymentDaysAccountDTO paymentDaysAccountDTO = paymentDaysAccountApi.getByCustomerEid(staffInfo.getCurrentEid(), customerEid);

        CustomerDetailPageVO pageVO = new CustomerDetailPageVO();
        pageVO.setCustomerInfo(customerVO);
        pageVO.setPaymentMethodList(paymentMethodVOList);
        pageVO.setPaymentDaysInfo(PojoUtils.map(paymentDaysAccountDTO, CustomerDetailPageVO.PaymentDaysVO.class));
        pageVO.setErpCustomerInfo(PojoUtils.map(enterpriseCustomerDTO, CustomerDetailPageVO.ErpCustomerVO.class));

        // 工业直属类型的企业的客户EAS信息（工业直属企业的客户信息与以岭的客户信息一致）
        if (staffInfo.getEnterpriseChannel() == EnterpriseChannelEnum.INDUSTRY_DIRECT) {
            List<EnterpriseCustomerEasDTO> enterpriseCustomerEasDTOList = customerApi.getCustomerEasInfos(Constants.YILING_EID, customerEid);
            pageVO.setEasInfoList(PojoUtils.map(enterpriseCustomerEasDTOList, CustomerDetailPageVO.EasInfoVO.class));
            pageVO.setShowEasInfoFlag(true);
        }

        return Result.success(pageVO);
    }

    @ApiOperation(value = "修改客户信息")
    @PostMapping("/update")
    @Log(title = "修改客户信息", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateCustomerInfoForm form) {
        UpdateCustomerInfoRequest request = PojoUtils.map(form, UpdateCustomerInfoRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPlatformEnum(PlatformEnum.POP);

        boolean result = customerApi.updateCustomerInfo(request);
        return Result.success(new BoolObject(result));
    }

}
