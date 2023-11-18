package com.yiling.f2b.admin.enterprise.controller;

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
import com.yiling.f2b.admin.enterprise.form.AddCustomerGroupForm;
import com.yiling.f2b.admin.enterprise.form.AddGroupCustomersForm;
import com.yiling.f2b.admin.enterprise.form.AddResultGroupCustomersForm;
import com.yiling.f2b.admin.enterprise.form.MoveGroupCustomersForm;
import com.yiling.f2b.admin.enterprise.form.QueryCustomerGroupPageListForm;
import com.yiling.f2b.admin.enterprise.form.RemoveGroupCustomersForm;
import com.yiling.f2b.admin.enterprise.form.UpdateCustomerGroupForm;
import com.yiling.f2b.admin.enterprise.vo.CustomerGroupListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.IdObject;
import com.yiling.pricing.goods.api.GoodsPriceCustomerGroupApi;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.user.enterprise.api.ChannelCustomerApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerGroupApi;
import com.yiling.user.enterprise.dto.EnterpriseChannelCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerGroupDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerGroupRequest;
import com.yiling.user.enterprise.dto.request.AddResultGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.MoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.QueryChannelCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerGroupPageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.SaveGroupCustomersRequest;
import com.yiling.user.enterprise.dto.request.UpdateCustomerGroupRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户分组模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@RestController
@RequestMapping("/customer/group")
@Api(tags = "客户分组模块接口")
@Slf4j
public class CustomerGroupController extends BaseController {

    @DubboReference
    CustomerGroupApi customerGroupApi;
    @DubboReference
    CustomerApi      customerApi;
    @DubboReference
    GoodsPriceCustomerGroupApi goodsPriceCustomerGroupApi;
    @DubboReference
    ChannelCustomerApi channelCustomerApi;

    @ApiOperation(value = "客户分组列表")
    @PostMapping("/pageList")
    public Result<Page<CustomerGroupListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCustomerGroupPageListForm form) {
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
    @Log(title = "添加客户分组", businessType = BusinessTypeEnum.INSERT)
    public Result<IdObject> add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddCustomerGroupForm form) {
        AddCustomerGroupRequest request = PojoUtils.map(form, AddCustomerGroupRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        Long id = customerGroupApi.add(request);
        return Result.success(new IdObject(id));
    }

    @ApiOperation(value = "修改客户分组")
    @PostMapping("/update")
    @Log(title = "修改客户分组", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateCustomerGroupForm form) {
        UpdateCustomerGroupRequest request = PojoUtils.map(form, UpdateCustomerGroupRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = customerGroupApi.update(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "删除客户分组")
    @GetMapping("/remove")
    @Log(title = "删除客户分组", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> remove(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam @ApiParam(value = "客户分组ID", required = true) Long id) {
        boolean result = customerGroupApi.remove(id, staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "移除分组中的客户")
    @PostMapping("/removeCustomers")
    @Log(title = "移除分组中的客户", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> removeCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid RemoveGroupCustomersForm form) {
        RemoveGroupCustomersRequest request = PojoUtils.map(form, RemoveGroupCustomersRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());

        boolean result = customerGroupApi.removeGroupCustomers(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "向分组中添加客户")
    @PostMapping("/addCustomers")
    @Log(title = "向分组中添加客户", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> addCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddGroupCustomersForm form) {
        SaveGroupCustomersRequest request = PojoUtils.map(form, SaveGroupCustomersRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());

        boolean result = customerGroupApi.addGroupCustomers(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "向分组中添加查询结果")
    @PostMapping("/addResultCustomers")
    @Log(title = "向分组中添加查询结果", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> addResultCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddResultGroupCustomersForm form) {
        // 根据查询条件获取所有客户
        QueryChannelCustomerPageListRequest request = PojoUtils.map(form, QueryChannelCustomerPageListRequest.class);
        request.setEid(Constants.YILING_EID);
        List<EnterpriseChannelCustomerDTO> list = channelCustomerApi.queryChannelCustomerList(request);

        // 过滤客户
        List<Long> customerEidList = list.stream().map(channelCustomerDTO -> {
            if (channelCustomerDTO.getCustomerGroupId() != null && channelCustomerDTO.getCustomerGroupId() != 0) {
                return null;
            }
            return channelCustomerDTO.getCustomerEid();

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
    @Log(title = "移动分组客户", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> moveCustomers(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid MoveGroupCustomersForm form) {
        MoveGroupCustomersRequest request = PojoUtils.map(form, MoveGroupCustomersRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = customerGroupApi.moveGroupCustomers(request);
        return Result.success(new BoolObject(result));
    }
}
