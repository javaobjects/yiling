package com.yiling.b2b.admin.enterprisecustomer.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import com.yiling.b2b.admin.enterprisecustomer.form.AddCustomerGroupForm;
import com.yiling.b2b.admin.enterprisecustomer.form.AddGroupCustomersForm;
import com.yiling.b2b.admin.enterprisecustomer.form.AddResultGroupCustomersForm;
import com.yiling.b2b.admin.enterprisecustomer.form.MoveGroupCustomersForm;
import com.yiling.b2b.admin.enterprisecustomer.form.QueryCustomerGroupPageForm;
import com.yiling.b2b.admin.enterprisecustomer.form.RemoveGroupCustomersForm;
import com.yiling.b2b.admin.enterprisecustomer.form.UpdateCustomerGroupForm;
import com.yiling.b2b.admin.enterprisecustomer.vo.CustomerGroupListItemVO;
import com.yiling.b2b.admin.enterprisecustomer.vo.CustomerGroupVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.common.web.rest.IdObject;
import com.yiling.pricing.goods.api.GoodsPriceCustomerGroupApi;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerGroupRequest;
import com.yiling.user.enterprise.dto.request.MoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerGroupPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.SaveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerGroupRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家后台B2B-客户分组 Controller
 *
 * @author: lun.yu
 * @date: 2021/11/1
 */
@RestController
@RequestMapping("/customerGroup")
@Api(tags = "客户分组接口")
@Slf4j
public class CustomerGroupController extends BaseController {

    @DubboReference
    CustomerGroupApi customerGroupApi;
    @DubboReference
    CustomerApi customerApi;
    @DubboReference
    GoodsPriceCustomerGroupApi goodsPriceCustomerGroupApi;

    @ApiOperation(value = "客户分组列表")
    @PostMapping("/pageList")
    public Result<Page<CustomerGroupListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryCustomerGroupPageForm form) {
        QueryCustomerGroupPageListRequest request = PojoUtils.map(form, QueryCustomerGroupPageListRequest.class);
        request.setEid(staffInfo.getCurrentEid());

        Page<EnterpriseCustomerGroupDTO> page = customerGroupApi.pageList(request);
        List<EnterpriseCustomerGroupDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }

        List<Long> groupIds = records.stream().map(EnterpriseCustomerGroupDTO::getId).collect(Collectors.toList());
        Map<Long, Long> groupCustomersMap = customerApi.countGroupCustomers(groupIds);

        // 根据商品id查询客户分组定价
        Map<Long, List<GoodsPriceCustomerGroupDTO>> goodsPriceCustomerGroupMap = goodsPriceCustomerGroupApi.getByGoodsId(form.getGoodsId()).stream()
                .collect(Collectors.groupingBy(GoodsPriceCustomerGroupDTO::getCustomerGroupId));

        List<CustomerGroupListItemVO> list = Lists.newArrayList();
        records.forEach(e -> {
            CustomerGroupListItemVO item = PojoUtils.map(e, CustomerGroupListItemVO.class);
            item.setCustomerNum(groupCustomersMap.getOrDefault(e.getId(), 0L));
            // 已添加过的分组
            List<GoodsPriceCustomerGroupDTO> goodsPriceCustomerGroupDtos = goodsPriceCustomerGroupMap.get(e.getId());
            if (CollUtil.isNotEmpty(goodsPriceCustomerGroupDtos)) {
                item.setSelectedFlag(true);
            }
            list.add(item);
        });

        Page<CustomerGroupListItemVO> pageVO = PojoUtils.map(page, CustomerGroupListItemVO.class);
        pageVO.setRecords(list);

        return Result.success(pageVO);
    }

    @ApiOperation(value = "添加客户分组")
    @PostMapping("/add")
    public Result<IdObject> add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddCustomerGroupForm form) {
        AddCustomerGroupRequest request = PojoUtils.map(form, AddCustomerGroupRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        Long id = customerGroupApi.add(request);
        return Result.success(new IdObject(id));
    }

    @ApiOperation(value = "修改客户分组")
    @PostMapping("/update")
    public Result<Void> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateCustomerGroupForm form) {
        UpdateCustomerGroupRequest request = PojoUtils.map(form, UpdateCustomerGroupRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        customerGroupApi.update(request);
        return Result.success();
    }

    @ApiOperation(value = "删除客户分组")
    @GetMapping("/remove")
    public Result<Void> remove(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam @ApiParam(value = "客户分组ID", required = true) Long id) {
        customerGroupApi.remove(id, staffInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "移除分组中的客户")
    @PostMapping("/removeCustomers")
    public Result<Void> removeCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid RemoveGroupCustomersForm form) {
        RemoveGroupCustomersRequest request = PojoUtils.map(form, RemoveGroupCustomersRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());

        customerGroupApi.removeGroupCustomers(request);
        return Result.success();
    }

    @ApiOperation(value = "向分组中添加客户")
    @PostMapping("/addCustomers")
    public Result<Void> addCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddGroupCustomersForm form) {
        SaveGroupCustomersRequest request = PojoUtils.map(form, SaveGroupCustomersRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());

        customerGroupApi.addGroupCustomers(request);
        return Result.success();
    }

    @ApiOperation(value = "向分组中添加查询结果")
    @PostMapping("/addResultCustomers")
    public Result<Void> addResultCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddResultGroupCustomersForm form) {
        // 根据查询条件获取到所有客户
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        request.setEids(ListUtil.toList(staffInfo.getCurrentEid()));
        request.setUseLine(EnterpriseCustomerLineEnum.B2B.getCode());
        List<EnterpriseCustomerDTO> list = customerApi.queryList(request);

        // 过滤客户
        List<Long> customerEidList = list.stream().map(enterpriseCustomerDTO -> {
            if (enterpriseCustomerDTO.getCustomerGroupId() != null && enterpriseCustomerDTO.getCustomerGroupId() != 0) {
                return null;
            }
            return enterpriseCustomerDTO.getCustomerEid();

        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (customerEidList.size() == 0) {
            return Result.success();
        }

        // 添加客户
        SaveGroupCustomersRequest customersRequest = new SaveGroupCustomersRequest();
        customersRequest.setGroupId(form.getGroupId());
        customersRequest.setCustomerEids(customerEidList);
        customersRequest.setOpUserId(staffInfo.getCurrentUserId());
        customersRequest.setEid(staffInfo.getCurrentEid());

        customerGroupApi.addGroupCustomers(customersRequest);
        return Result.success();
    }

    @ApiOperation(value = "移动分组客户")
    @PostMapping("/moveCustomers")
    public Result<Void> moveCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid MoveGroupCustomersForm form) {
        MoveGroupCustomersRequest request = PojoUtils.map(form, MoveGroupCustomersRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        customerGroupApi.moveGroupCustomers(request);
        return Result.success();
    }

    @ApiOperation(value = "获取客户分组下拉信息")
    @GetMapping("/getCustomerGroupList")
    public Result<CollectionObject<CustomerGroupVO>> getCustomerGroupList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<EnterpriseCustomerGroupDTO> enterpriseCustomerGroupDTOS=customerGroupApi.listByEid(staffInfo.getCurrentEid());
        return Result.success(new CollectionObject<>(PojoUtils.map(enterpriseCustomerGroupDTOS,CustomerGroupVO.class)));
    }
}
