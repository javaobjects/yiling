package com.yiling.f2b.web.enterprise.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.f2b.web.enterprise.form.AddDeliveryAddressForm;
import com.yiling.f2b.web.enterprise.form.UpdateDeliveryAddressForm;
import com.yiling.f2b.web.enterprise.form.UpdateDeliveryForm;
import com.yiling.f2b.web.enterprise.vo.DeliveryAddressVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.SetDefaultDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 收货地址 Controller
 * 
 * @author xuan.zhou
 * @date 2021/6/23
 */
@RestController
@RequestMapping("/deliveryAddress")
@Api(tags = "收货地址接口")
@Slf4j
public class DeliveryAddressController extends BaseController {

    @DubboReference
    DeliveryAddressApi deliveryAddressApi;

    @ApiOperation(value = "获取我的默认收货地址")
    @GetMapping("/getMyDefaultAddress")
    @UserAccessAuthentication
    public Result<DeliveryAddressVO> getMyDefaultAddress(@CurrentUser CurrentStaffInfo staffInfo) {
        DeliveryAddressDTO deliveryAddressDTO = deliveryAddressApi.getDefaultAddressByEid(staffInfo.getCurrentEid());
        return Result.success(PojoUtils.map(deliveryAddressDTO, DeliveryAddressVO.class));
    }

    @ApiOperation(value = "添加收货地址")
    @PostMapping("/add")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<BoolObject> add(@CurrentUser CurrentStaffInfo staffInfo,
                                  @RequestBody @Valid AddDeliveryAddressForm form) {
        AddDeliveryAddressRequest request = PojoUtils.map(form, AddDeliveryAddressRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = deliveryAddressApi.add(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改收货地址")
    @PostMapping("/update")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<BoolObject> update(@CurrentUser CurrentStaffInfo staffInfo,
                                     @RequestBody @Valid UpdateDeliveryAddressForm form) {
        UpdateDeliveryAddressRequest request = PojoUtils.map(form, UpdateDeliveryAddressRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = deliveryAddressApi.update(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "设置默认收货地址")
    @PostMapping("/setDefault")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Boolean> setDefault(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateDeliveryForm form) {
        SetDefaultDeliveryAddressRequest request = PojoUtils.map(form, SetDefaultDeliveryAddressRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(deliveryAddressApi.setDefault(request));
    }

    @ApiOperation(value = "删除收货地址")
    @PostMapping("/delete")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Boolean> delete(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateDeliveryForm form) {
        return Result.success(deliveryAddressApi.delete(staffInfo.getCurrentUserId(), form.getId()));
    }


    @ApiOperation(value = "收货地址列表")
    @PostMapping("/list")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CollectionObject<DeliveryAddressVO>> list(@CurrentUser CurrentStaffInfo staffInfo) {
        QueryDeliveryAddressRequest request = new QueryDeliveryAddressRequest();
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        List<DeliveryAddressDTO> resultList = deliveryAddressApi.selectDeliveryAddressList(request);

        List<DeliveryAddressVO>  deliveryAddressVOList = PojoUtils.map(resultList, DeliveryAddressVO.class);
        return  Result.success(new CollectionObject<>(deliveryAddressVOList));
    }
}
