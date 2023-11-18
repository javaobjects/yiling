package com.yiling.hmc.address.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.address.api.AddressApi;
import com.yiling.hmc.address.dto.AddressDTO;
import com.yiling.hmc.address.dto.request.AddressSaveOrUpdateRequest;
import com.yiling.hmc.address.dto.request.DeleteAddressRequest;
import com.yiling.hmc.address.form.AddressSaveOrUpdateForm;
import com.yiling.hmc.address.form.DeleteAddressForm;
import com.yiling.hmc.address.vo.QueryAddressVO;
import com.yiling.user.system.bo.CurrentUserInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/16
 */
@Api(tags = "收货地址")
@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController extends BaseController {

    @DubboReference
    AddressApi addressApi;


    @ApiOperation(value = "收货地址添加或修改")
    @PostMapping("/saveOrUpdateAddress")
    @Log(title = "HMC收货地址添加或修改", businessType = BusinessTypeEnum.OTHER)
    public Result saveOrUpdateAddress(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid AddressSaveOrUpdateForm form) {
        AddressSaveOrUpdateRequest request = PojoUtils.map(form, AddressSaveOrUpdateRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        Boolean b = addressApi.saveOrUpdateAddress(request);
        return Result.success(b);
    }


    @ApiOperation(value = "收货地址删除")
    @PostMapping("/deleteAddress")
    @Log(title = "HMC收货地址删除", businessType = BusinessTypeEnum.DELETE)
    public Result deleteAddress(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid DeleteAddressForm form) {
        DeleteAddressRequest request = PojoUtils.map(form, DeleteAddressRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        Boolean b = addressApi.deleteAddress(request);
        return Result.success(b);
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/queryAddress")
    public Result<QueryAddressVO> queryAddress(@CurrentUser CurrentUserInfo currentUser, @ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id) {
        AddressDTO dto = addressApi.getAddressById(id);
        if (Objects.isNull(dto)) {
            return Result.failed("未查询到收货地址信息");
        }
        QueryAddressVO addressVO = PojoUtils.map(dto, QueryAddressVO.class);
        return Result.success(addressVO);
    }

    @ApiOperation(value = "列表")
    @PostMapping("/queryAddressPage")
    public Result<Page<QueryAddressVO>> queryAddressPage(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryPageListForm form) {
        QueryPageListRequest request = PojoUtils.map(form, QueryPageListRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        Page<AddressDTO> page = addressApi.queryAddressPage(request);
        if (page.getTotal() == 0) {
            return Result.success(request.getPage());
        }
        Page<QueryAddressVO> voPage = PojoUtils.map(page, QueryAddressVO.class);
        return Result.success(voPage);
    }
}
