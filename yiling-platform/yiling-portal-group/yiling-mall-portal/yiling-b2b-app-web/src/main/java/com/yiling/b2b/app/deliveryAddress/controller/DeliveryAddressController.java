package com.yiling.b2b.app.deliveryAddress.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.deliveryAddress.form.AddDeliveryAddressForm;
import com.yiling.b2b.app.deliveryAddress.form.UpdateDeliveryAddressForm;
import com.yiling.b2b.app.deliveryAddress.vo.DeliveryAddressVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.request.AddDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.dto.request.UpdateDeliveryAddressRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public Result<DeliveryAddressVO> getMyDefaultAddress(@CurrentUser CurrentStaffInfo staffInfo) {
        DeliveryAddressDTO deliveryAddressDTO = deliveryAddressApi.getDefaultAddressByEid(staffInfo.getCurrentEid());
        return Result.success(PojoUtils.map(deliveryAddressDTO, DeliveryAddressVO.class));
    }

    @ApiOperation(value = "添加收货地址")
    @PostMapping("/add")
    public Result<Void> add(@CurrentUser CurrentStaffInfo staffInfo,
                                  @RequestBody @Valid AddDeliveryAddressForm form) {
        AddDeliveryAddressRequest request = PojoUtils.map(form, AddDeliveryAddressRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = deliveryAddressApi.add(request);

        if (result) {

            return Result.success();
        }

        return Result.failed("添加收货地址失败");
    }

    @ApiOperation(value = "修改收货地址")
    @PostMapping("/update")
    public Result<Void> update(@CurrentUser CurrentStaffInfo staffInfo,
                                     @RequestBody @Valid UpdateDeliveryAddressForm form) {
        UpdateDeliveryAddressRequest request = PojoUtils.map(form, UpdateDeliveryAddressRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = deliveryAddressApi.update(request);
        if (result) {

            return Result.success();
        }
        return Result.failed("修改收货地址失败");
    }


    @ApiOperation(value = "地址列表")
    @PostMapping("/list")
    public Result<CollectionObject<DeliveryAddressVO>> selectList(@CurrentUser CurrentStaffInfo staffInfo) {
        QueryDeliveryAddressRequest queryDeliveryAddressRequest = new QueryDeliveryAddressRequest();
        queryDeliveryAddressRequest.setEid(staffInfo.getCurrentEid());
        List<DeliveryAddressDTO> resultList = deliveryAddressApi.selectDeliveryAddressList(queryDeliveryAddressRequest);
        return Result.success(new CollectionObject<>(PojoUtils.map(resultList,DeliveryAddressVO.class)));
    }
}
