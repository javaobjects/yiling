package com.yiling.admin.b2b.integral.controller;

import java.util.List;
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
import com.yiling.admin.b2b.integral.form.QueryIntegralExchangeOrderPageForm;
import com.yiling.admin.b2b.integral.form.UpdateExpressForm;
import com.yiling.admin.b2b.integral.form.UpdateIntegralExchangeOrderForm;
import com.yiling.admin.b2b.integral.form.UpdateIntegralExchangeOrderReceiptForm;
import com.yiling.admin.b2b.integral.form.UpdateReceiptAddressForm;
import com.yiling.admin.b2b.integral.vo.IntegralExchangeOrderDetailVO;
import com.yiling.admin.b2b.integral.vo.IntegralExchangeOrderItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.goodsgift.api.GoodsGiftApi;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dto.request.QueryIntegralExchangeOrderPageRequest;
import com.yiling.user.integral.api.IntegralExchangeOrderApi;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;
import com.yiling.user.integral.dto.IntegralExchangeOrderReceiptInfoDTO;
import com.yiling.user.integral.dto.request.UpdateExpressRequest;
import com.yiling.user.integral.dto.request.UpdateReceiptAddressRequest;
import com.yiling.user.integral.dto.request.UpdateReceiptInfoRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeOrderRequest;
import com.yiling.user.integral.enums.IntegralExchangeOrderStatusEnum;
import com.yiling.user.integral.enums.IntegralGoodsTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换订单 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-11
 */
@Slf4j
@RestController
@RequestMapping("/integralExchangeOrder")
@Api(tags = "积分兑换订单接口")
public class IntegralExchangeOrderController extends BaseController {

    @DubboReference
    IntegralExchangeOrderApi integralExchangeOrderApi;
    @DubboReference
    GoodsGiftApi goodsGiftApi;

    @ApiOperation(value = "积分兑换订单分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<IntegralExchangeOrderItemVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryIntegralExchangeOrderPageForm form) {
        QueryIntegralExchangeOrderPageRequest request = PojoUtils.map(form, QueryIntegralExchangeOrderPageRequest.class);
        Page<IntegralExchangeOrderItemVO> page = PojoUtils.map(integralExchangeOrderApi.queryListPage(request), IntegralExchangeOrderItemVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "兑付")
    @PostMapping("/exchange")
    @Log(title = "兑付", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> exchange(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateIntegralExchangeOrderForm form) {
        UpdateIntegralExchangeOrderRequest request = PojoUtils.map(form, UpdateIntegralExchangeOrderRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeOrderApi.exchange(request));
    }

    @ApiOperation(value = "兑付详情")
    @GetMapping("/exchangeDetail")
    public Result<IntegralExchangeOrderDetailVO> exchangeDetail(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        IntegralExchangeOrderDTO exchangeOrderDTO = integralExchangeOrderApi.getById(id);
        if (exchangeOrderDTO.getStatus().equals(IntegralExchangeOrderStatusEnum.UN_CASH.getCode())) {
            throw new BusinessException(UserErrorCode.ORDER_UN_EXCHANGE);
        }

        IntegralExchangeOrderDetailVO exchangeOrderDetailVO = new IntegralExchangeOrderDetailVO();
        exchangeOrderDetailVO.setId(exchangeOrderDTO.getId());
        exchangeOrderDetailVO.setGoodsType(exchangeOrderDTO.getGoodsType());

        if (IntegralGoodsTypeEnum.getByCode(exchangeOrderDTO.getGoodsType()) == IntegralGoodsTypeEnum.REAL_GOODS) {
            IntegralExchangeOrderReceiptInfoDTO orderReceiptInfoDTO = integralExchangeOrderApi.getReceiptInfoByOrderId(id);
            if (Objects.nonNull(orderReceiptInfoDTO)) {
                IntegralExchangeOrderDetailVO.ReceiptInfoVO receiptInfoVO = PojoUtils.map(orderReceiptInfoDTO, IntegralExchangeOrderDetailVO.ReceiptInfoVO.class);
                exchangeOrderDetailVO.setReceiptInfo(receiptInfoVO);
            }

        } else if (IntegralGoodsTypeEnum.getByCode(exchangeOrderDTO.getGoodsType()) == IntegralGoodsTypeEnum.VIRTUAL_GOODS) {
            GoodsGiftDetailDTO goodsGifDetail = goodsGiftApi.getGoodsGifDetail(exchangeOrderDTO.getGoodsId());
            String cardNo = goodsGifDetail.getCardNo();
            String password = goodsGifDetail.getPassword();
            if (StrUtil.isNotEmpty(cardNo)) {
                String[] cardArr = cardNo.split(",");
                String[] passwordArr = new String[cardArr.length];
                if (StrUtil.isNotEmpty(password)) {
                    passwordArr = password.split(",");
                }

                List<IntegralExchangeOrderDetailVO.CardInfo> cardInfoList = ListUtil.toList();
                for (int i=0; i< cardArr.length; i++) {
                    IntegralExchangeOrderDetailVO.CardInfo cardInfo = new IntegralExchangeOrderDetailVO.CardInfo();
                    cardInfo.setCardNo(cardArr[i]);
                    cardInfo.setPassword(passwordArr.length >= i + 1 ? passwordArr[i] : null);
                    cardInfoList.add(cardInfo);
                }
                exchangeOrderDetailVO.setCardInfoList(cardInfoList);
            }

        } else if (IntegralGoodsTypeEnum.getByCode(exchangeOrderDTO.getGoodsType()) == IntegralGoodsTypeEnum.GOODS_COUPON
                || IntegralGoodsTypeEnum.getByCode(exchangeOrderDTO.getGoodsType()) == IntegralGoodsTypeEnum.MEMBER_COUPON) {
            // 优惠券ID
            exchangeOrderDetailVO.setCouponId(exchangeOrderDTO.getGoodsId());
        }

        return Result.success(exchangeOrderDetailVO);
    }

    @ApiOperation(value = "修改兑付", notes = "真实物品已兑付状态可以进行修改")
    @PostMapping("/update")
    @Log(title = "修改兑付", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> update(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateIntegralExchangeOrderReceiptForm form) {
        UpdateIntegralExchangeOrderRequest request = new UpdateIntegralExchangeOrderRequest();
        request.setExchangeType(3);
        request.setId(form.getId());
        request.setOrderReceiptInfo(PojoUtils.map(form, UpdateReceiptInfoRequest.class));
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeOrderApi.exchange(request));
    }

    @ApiOperation(value = "修改地址")
    @PostMapping("/updateAddress")
    @Log(title = "修改地址", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateAddress(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateReceiptAddressForm form) {
        UpdateReceiptAddressRequest request = PojoUtils.map(form, UpdateReceiptAddressRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeOrderApi.updateAddress(request));
    }

    @ApiOperation(value = "立即兑付", notes = "真实物品未兑付状态使用此接口")
    @PostMapping("/atOnceExchange")
    @Log(title = "立即兑付", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> atOnceExchange(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateExpressForm form) {
        UpdateExpressRequest request = PojoUtils.map(form, UpdateExpressRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(integralExchangeOrderApi.atOnceExchange(request));
    }


}
