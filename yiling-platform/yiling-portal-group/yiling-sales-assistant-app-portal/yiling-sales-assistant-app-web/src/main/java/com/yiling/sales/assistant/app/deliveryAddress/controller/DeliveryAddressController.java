package com.yiling.sales.assistant.app.deliveryAddress.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.sales.assistant.app.deliveryAddress.form.AddDeliveryAddressForm;
import com.yiling.sales.assistant.app.deliveryAddress.form.UpdateDeliveryAddressForm;
import com.yiling.sales.assistant.app.deliveryAddress.vo.DeliveryAddressVO;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 收货地址 Controller
 * 
 * @author zhigang.guo
 * @date 2021/09/14
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
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<DeliveryAddressVO> getMyDefaultAddress(@CurrentUser CurrentStaffInfo staffInfo,@ApiParam(value = "客户EID", required = true) @RequestParam(value = "customerEid", required = true) Long customerEid) {
        DeliveryAddressDTO deliveryAddressDTO = deliveryAddressApi.getDefaultAddressByEid(customerEid);
        return Result.success(PojoUtils.map(deliveryAddressDTO, DeliveryAddressVO.class));
    }

    @ApiOperation(value = "添加收货地址")
    @PostMapping("/add")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<BoolObject> add(@CurrentUser CurrentStaffInfo staffInfo,
                                  @RequestBody @Valid AddDeliveryAddressForm form) {
        AddDeliveryAddressRequest request = PojoUtils.map(form, AddDeliveryAddressRequest.class);
        request.setEid(form.getCustomerEid());
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


    @ApiOperation(value = "地址列表")
    @PostMapping("/list")
    @UserAccessAuthentication
    public Result<CollectionObject<DeliveryAddressVO>> selectList(@CurrentUser CurrentStaffInfo staffInfo,@ApiParam(value = "客户EID", required = true) @RequestParam(value = "customerEid", required = true) Long customerEid) {

        QueryDeliveryAddressRequest queryDeliveryAddressRequest = new QueryDeliveryAddressRequest();
        queryDeliveryAddressRequest.setEid(customerEid);
        List<DeliveryAddressDTO> resultList = deliveryAddressApi.selectDeliveryAddressList(queryDeliveryAddressRequest);
        List<DeliveryAddressVO> deliveryAddressVOS = PojoUtils.map(resultList,DeliveryAddressVO.class);

        return Result.success(new CollectionObject(deliveryAddressVOS));
    }
}
