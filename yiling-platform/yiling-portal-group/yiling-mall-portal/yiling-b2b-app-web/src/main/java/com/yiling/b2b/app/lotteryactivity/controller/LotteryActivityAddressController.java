package com.yiling.b2b.app.lotteryactivity.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.lotteryactivity.form.DeleteDeliveryAddressForm;
import com.yiling.b2b.app.lotteryactivity.form.SaveDeliveryAddressForm;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityDeliveryAddressVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityDeliveryAddressApi;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDeliveryAddressDTO;
import com.yiling.marketing.lotteryactivity.dto.request.SaveDeliveryAddressRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * b2b-app抽奖活动收货地址 Controller
 *
 * @author: lun.yu
 * @date: 2022-12-06
 */
@Slf4j
@RestController
@RequestMapping("/lotteryDeliveryAddress")
@Api(tags = "抽奖活动收货地址接口")
public class LotteryActivityAddressController extends BaseController {

    @DubboReference
    LotteryActivityDeliveryAddressApi lotteryActivityDeliveryAddressApi;

    @ApiOperation(value = "查询奖品收货地址列表")
    @PostMapping("/queryList")
    public Result<CollectionObject<LotteryActivityDeliveryAddressVO>> queryList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<LotteryActivityDeliveryAddressDTO> addressDTOList = lotteryActivityDeliveryAddressApi.queryList(staffInfo.getCurrentEid());
        List<LotteryActivityDeliveryAddressVO> addressVOList = PojoUtils.map(addressDTOList, LotteryActivityDeliveryAddressVO.class);
        return Result.success(new CollectionObject<>(addressVOList));
    }

    @ApiOperation(value = "保存抽奖活动收货地址")
    @PostMapping("/save")
    @Log(title = "保存抽奖活动收货地址", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> save(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveDeliveryAddressForm form) {
        SaveDeliveryAddressRequest request = PojoUtils.map(form, SaveDeliveryAddressRequest.class);
        request.setUid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(lotteryActivityDeliveryAddressApi.saveDeliveryAddress(request));
    }

    @ApiOperation(value = "删除抽奖活动收货地址")
    @PostMapping("/delete")
    @Log(title = "删除抽奖活动收货地址", businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> delete(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid DeleteDeliveryAddressForm form) {
        boolean result = lotteryActivityDeliveryAddressApi.deleteById(form.getId(), staffInfo.getCurrentUserId());
        return Result.success(result);
    }

}
