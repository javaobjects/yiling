package com.yiling.b2b.admin.shop.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.admin.shop.form.SaveShopForm;
import com.yiling.b2b.admin.shop.form.UpdateAnnouncementForm;
import com.yiling.b2b.admin.shop.vo.ShopAnnouncementVO;
import com.yiling.b2b.admin.shop.vo.ShopVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.shop.dto.request.SaveShopRequest;
import com.yiling.user.shop.dto.request.UpdateAnnouncementRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 店铺设置 Controller
 *
 * @author: lun.yu
 * @date: 2021/10/15
 */
@RestController
@RequestMapping("/shop")
@Api(tags = "店铺设置接口")
@Slf4j
public class ShopController extends BaseController {

    @DubboReference
    ShopApi shopApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PaymentMethodApi paymentMethodApi;

    @ApiOperation(value = "设置店铺")
    @PostMapping("/setShop")
    public Result<Boolean> setShop(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveShopForm form) {
        SaveShopRequest request = PojoUtils.map(form,SaveShopRequest.class);
        request.setShopEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(staffInfo.getCurrentEid())).orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        request.setShopName(enterpriseDTO.getName());

        Boolean result = shopApi.setShop(request);
        return Result.success(result);
    }

    @ApiOperation(value = "获取店铺设置详情")
    @GetMapping("/getShop")
    public Result<ShopVO> getShop(@CurrentUser CurrentStaffInfo staffInfo) {
        ShopDTO shopDTO = shopApi.getShop(staffInfo.getCurrentEid());

        // 获取店铺可选支付方式列表
        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByPlatform(PlatformEnum.B2B);
        List<ShopVO.PaymentMethodVO> paymentMethodVOList = ListUtil.empty();
        if (CollUtil.isNotEmpty(paymentMethodDTOList)) {
            paymentMethodVOList = paymentMethodDTOList.stream().map(e -> {
                ShopVO.PaymentMethodVO paymentMethodVO = new ShopVO.PaymentMethodVO();
                paymentMethodVO.setId(e.getCode());
                paymentMethodVO.setName(e.getName());
                // 在线支付默认选中
                paymentMethodVO.setChecked(PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE);
                paymentMethodVO.setDisabled(PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE);
                return paymentMethodVO;
            }).collect(Collectors.toList());
        }

        ShopVO shopVO;
        if (shopDTO == null || shopDTO.getId() == null) {
            shopVO = new ShopVO();
            shopVO.setShopEid(staffInfo.getCurrentEid());
            shopVO.setPaymentMethodList(paymentMethodVOList);
            return Result.success(shopVO);
        }

        shopVO = PojoUtils.map(shopDTO, ShopVO.class);
        shopVO.setPaymentMethodList(paymentMethodVOList);

        EnterpriseSalesAreaDTO salesAreaDTO = Optional.ofNullable(shopApi.getShopAreaJson(shopVO.getId())).orElse(new EnterpriseSalesAreaDTO());
        shopVO.setAreaJsonString(salesAreaDTO.getJsonContent());
        shopVO.setDescription(salesAreaDTO.getDescription());

        List<PaymentMethodDTO> shopPaymentMethodDTOList = shopApi.listShopPaymentMethods(shopVO.getShopEid());
        if (CollUtil.isNotEmpty(shopPaymentMethodDTOList)) {
            List<Long> paymentMethodIds = shopPaymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).distinct().collect(Collectors.toList());
            shopVO.getPaymentMethodList().forEach(e -> {
                // 在线支付默认选中
                e.setChecked(paymentMethodIds.contains(e.getId()));
                e.setDisabled(PaymentMethodEnum.getByCode(e.getId()) == PaymentMethodEnum.ONLINE);
            });
        }

        return Result.success(shopVO);
    }

    @ApiOperation(value = "修改店铺公告")
    @PostMapping("/updateShopAnnouncement")
    public Result<Void> updateShopAnnouncement(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateAnnouncementForm form) {
        UpdateAnnouncementRequest request = PojoUtils.map(form,UpdateAnnouncementRequest.class);
        request.setShopEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());

        shopApi.updateShopAnnouncement(request);
        return Result.success();
    }

    @ApiOperation(value = "获取店铺公告")
    @GetMapping("/getShopAnnouncement")
    public Result<ShopAnnouncementVO> getShopAnnouncement(@CurrentUser CurrentStaffInfo staffInfo) {
        ShopDTO shopDTO = shopApi.getShopAnnouncement(staffInfo.getCurrentEid());
        return Result.success(PojoUtils.map(shopDTO,ShopAnnouncementVO.class));
    }


}
