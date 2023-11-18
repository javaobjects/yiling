package com.yiling.hmc.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import com.yiling.hmc.diagnosis.vo.HmcPrescriptionGoodsInfoVO;
import com.yiling.hmc.order.enums.*;
import com.yiling.ih.patient.api.HmcPrescriptionApi;
import com.yiling.ih.patient.dto.HmcPrescriptionGoodsInfoDTO;
import com.yiling.ih.patient.dto.request.HmcPrescriptionOrderReceiveRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.hmc.address.api.AddressApi;
import com.yiling.hmc.address.dto.AddressDTO;
import com.yiling.hmc.address.vo.QueryAddressVO;
import com.yiling.hmc.config.HmcWebProperties;
import com.yiling.hmc.order.api.MarketOrderAddressApi;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.api.MarketOrderDetailApi;
import com.yiling.hmc.order.dto.MarketOrderAddressDTO;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDetailDTO;
import com.yiling.hmc.order.dto.request.QueryMarketOrderPageRequest;
import com.yiling.hmc.order.dto.request.UpdateMarketOrderRequest;
import com.yiling.hmc.order.form.QueryMarketOrderPageForm;
import com.yiling.hmc.order.form.UpdateMarketOrderForm;
import com.yiling.hmc.order.vo.CreateMarketOrderVO;
import com.yiling.hmc.order.vo.MarketOrderDetailVO;
import com.yiling.hmc.order.vo.QueryConfirmOrderInfoVO;
import com.yiling.hmc.order.vo.QueryMarketOrderPageVO;
import com.yiling.hmc.order.vo.QueryMarketOrderVO;
import com.yiling.hmc.usercenter.form.CreateMarketOrderForm;
import com.yiling.hmc.wechat.dto.request.MarketOrderSubmitRequest;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CloseOrderRequest;
import com.yiling.user.system.bo.CurrentUserInfo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 市场订单 Controller
 *
 * @author: fan.shen
 * @date: 2023-02-16
 */
@RestController
@RequestMapping("/marketOrder/")
@Api(tags = "市场订单")
@Slf4j
public class MarketOrderController extends BaseController {

    @DubboReference
    MarketOrderApi marketOrderApi;

    @DubboReference
    HmcPrescriptionApi prescriptionApi;

    @Autowired
    FileService fileService;

    @Autowired
    HmcWebProperties hmcWebProperties;

    @DubboReference
    MarketOrderDetailApi marketOrderDetailApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    MarketOrderAddressApi marketOrderAddressApi;

    @DubboReference
    PayApi payApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    AddressApi addressApi;

    @Autowired
    private SpringAsyncConfig asyncConfig;


    /**
     * 创建订单
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/createOrder")
    @Log(title = "创建订单", businessType = BusinessTypeEnum.OTHER)
    public Result<CreateMarketOrderVO> createOrder(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid CreateMarketOrderForm form) {
        MarketOrderSubmitRequest request = PojoUtils.map(form, MarketOrderSubmitRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        request.setHmcOrderStatus(HmcOrderStatusEnum.UN_PAY);
        request.setPaymentStatusEnum(HmcPaymentStatusEnum.UN_PAY);
        request.setPaymentMethodEnum(HmcPaymentMethodEnum.WECHAT_PAY);
        request.setDeliveryType(HmcDeliveryTypeEnum.FREIGHT);
        request.setCreateSource(HmcCreateSourceEnum.HMC_MP);
        request.setEid(hmcWebProperties.getEid());
        request.setGoodsId(hmcWebProperties.getGoodsId());

        // 创建订单
        try {
            MarketOrderDTO marketOrderDTO = marketOrderApi.createOrder(request);
            log.info("创建订单完成：{}", marketOrderDTO);
            return Result.success(PojoUtils.map(marketOrderDTO, CreateMarketOrderVO.class));
        } catch (IllegalArgumentException e) {
            return Result.failed(e.getMessage());
        } catch (Exception e) {
            return Result.failed("创建订单失败");
        }
    }

    @ApiOperation(value = "列表")
    @PostMapping("/queryOrderPage")
    public Result<Page<QueryMarketOrderPageVO>> queryOrderPage(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryMarketOrderPageForm form) {
        QueryMarketOrderPageRequest request = PojoUtils.map(form, QueryMarketOrderPageRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        Page<MarketOrderDTO> page = marketOrderApi.queryMarketOrderPage(request);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Result.success(request.getPage());
        }
        Page<QueryMarketOrderPageVO> voPage = PojoUtils.map(page, QueryMarketOrderPageVO.class);
        List<Long> orderIds = voPage.getRecords().stream().map(QueryMarketOrderPageVO::getId).distinct().collect(Collectors.toList());
        List<MarketOrderDetailDTO> detailDTOS = marketOrderDetailApi.queryByOrderIdList(orderIds);

        List<Long> goodsIds = detailDTOS.stream().map(MarketOrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> dtoMap = goodsDTOS.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        List<MarketOrderDetailVO> orderDetailVOS = PojoUtils.map(detailDTOS, MarketOrderDetailVO.class);
        orderDetailVOS.forEach(e -> {
            if (dtoMap.containsKey(e.getGoodsId())) {
                e.setPic(fileService.getUrl(dtoMap.get(e.getGoodsId()).getPic(), FileTypeEnum.GOODS_PICTURE));
            }
        });
        Map<Long, List<MarketOrderDetailVO>> couponMap = orderDetailVOS.stream().collect(Collectors.groupingBy(MarketOrderDetailVO::getOrderId));
        voPage.getRecords().forEach(e -> {
            if (couponMap.containsKey(e.getId())) {
                e.setDetailVOS(couponMap.get(e.getId()));
            }
            // 处方订单 -> 获取商品
            if (HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(e.getMarketOrderType())) {
                HmcPrescriptionGoodsInfoDTO prescriptionGoodsById = prescriptionApi.getPrescriptionGoodsById(e.getPrescriptionId().intValue());
                HmcPrescriptionGoodsInfoVO prescriptionGoodsInfoVO = PojoUtils.map(prescriptionGoodsById, HmcPrescriptionGoodsInfoVO.class);
                if (CollUtil.isNotEmpty(prescriptionGoodsInfoVO.getGoodsList())) {
                    String hmcPrescriptionGoodsGoodsDesc = prescriptionGoodsInfoVO.getGoodsList().stream().map(item -> item.getGoodsName() + " " + item.getNum() + "g").collect(Collectors.joining("、"));
                    e.setHmcPrescriptionGoodsGoodsDesc(hmcPrescriptionGoodsGoodsDesc);
                }
                e.setPrescriptionGoodsInfoVO(prescriptionGoodsInfoVO);
            }
        });

        return Result.success(voPage);
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/queryOrder")
    public Result<QueryMarketOrderVO> queryMarketOrder(@CurrentUser CurrentUserInfo currentUser, @ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id) {
        MarketOrderDTO marketOrderDTO = marketOrderApi.queryById(id);
        if (Objects.isNull(marketOrderDTO)) {
            return Result.failed("未查询到订单信息");
        }
        QueryMarketOrderVO marketOrderVO = PojoUtils.map(marketOrderDTO, QueryMarketOrderVO.class);
        MarketOrderAddressDTO addressDTO = marketOrderAddressApi.getAddressByOrderId(marketOrderVO.getId());
        PojoUtils.map(addressDTO, marketOrderVO);
        marketOrderVO.setId(id);
        List<MarketOrderDetailDTO> marketOrderDetailDTOS = marketOrderDetailApi.queryByOrderIdList(new ArrayList<Long>() {{
            add(marketOrderVO.getId());
        }});
        List<Long> goodsIds = marketOrderDetailDTOS.stream().map(MarketOrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> dtoMap = goodsDTOS.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        List<MarketOrderDetailVO> orderDetailVOS = PojoUtils.map(marketOrderDetailDTOS, MarketOrderDetailVO.class);
        orderDetailVOS.forEach(e -> {
            if (dtoMap.containsKey(e.getGoodsId())) {
                e.setPic(fileService.getUrl(dtoMap.get(e.getGoodsId()).getPic(), FileTypeEnum.GOODS_PICTURE));
            }
        });
        marketOrderVO.setDetailVOS(orderDetailVOS);
        if (marketOrderVO.getPayTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setPayTime(null);
        }
        if (marketOrderVO.getDeliverTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setDeliverTime(null);
        }
        if (marketOrderVO.getCancelTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setCancelTime(null);
        }
        if (marketOrderVO.getReceiveTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setReceiveTime(null);
        }
        marketOrderVO.setCurrentTime(DateUtil.date());
        if (marketOrderVO.getOrderStatus().equals(HmcOrderStatusEnum.UN_PAY.getCode())) {
            //订单未支付
            marketOrderVO.setCancelCutoffTime(DateUtil.offsetMinute(marketOrderVO.getCreateTime(), 30));
        }
        if (marketOrderVO.getOrderStatus().equals(HmcOrderStatusEnum.UN_RECEIVED.getCode())) {
            //待收货
            marketOrderVO.setReceiveCutoffTime(DateUtil.offsetDay(marketOrderVO.getDeliverTime(), 10));
        }
        // 处方订单 -> 获取商品
        if (HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(marketOrderDTO.getMarketOrderType())) {
            HmcPrescriptionGoodsInfoDTO prescriptionGoodsById = prescriptionApi.getPrescriptionGoodsById(marketOrderDTO.getPrescriptionId().intValue());
            HmcPrescriptionGoodsInfoVO prescriptionGoodsInfoVO = PojoUtils.map(prescriptionGoodsById, HmcPrescriptionGoodsInfoVO.class);
            marketOrderVO.setPrescriptionGoodsInfoVO(prescriptionGoodsInfoVO);
        }
        return Result.success(marketOrderVO);
    }

    @ApiOperation(value = "订单收货")
    @PostMapping("/receiveOrder")
    @Log(title = "HMC订单收货", businessType = BusinessTypeEnum.UPDATE)
    public Result receiveOrder(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid UpdateMarketOrderForm form) {
        if (!form.getOrderStatus().equals(HmcOrderStatusEnum.RECEIVED.getCode())) {
            return Result.failed("订单状态错误");
        }
        UpdateMarketOrderRequest request = PojoUtils.map(form, UpdateMarketOrderRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        request.setReceiveTime(DateUtil.date());
        Boolean b = marketOrderApi.updateMarketOrder(request);
        return Result.success(b);
    }

    @ApiOperation(value = "订单取消")
    @PostMapping("/cancelOrder")
    @Log(title = "HMC订单取消", businessType = BusinessTypeEnum.UPDATE)
    public Result cancelOrder(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid UpdateMarketOrderForm form) {
        if (!form.getOrderStatus().equals(HmcOrderStatusEnum.CANCELED.getCode())) {
            return Result.failed("订单状态错误");
        }

        MarketOrderDTO marketOrderDTO = marketOrderApi.queryById(form.getId());
        if (!HmcOrderStatusEnum.UN_PAY.getCode().equals(marketOrderDTO.getOrderStatus())) {
            log.info("订单状态已经变更，当前状态：{}", marketOrderDTO.getOrderStatus());
            return Result.success(true);
        }

        // 查询订单支付状态
        Result<Boolean> payResult = payApi.orderQueryByOrderNo(OrderPlatformEnum.HMC, TradeTypeEnum.PAY, marketOrderDTO.getOrderNo());
        if (Objects.nonNull(payResult) && !payResult.getData()) {
            // 更新订单状态 -> 已取消
            UpdateMarketOrderRequest request = PojoUtils.map(form, UpdateMarketOrderRequest.class);
            request.setOpUserId(currentUser.getCurrentUserId());
            request.setCancelTime(DateUtil.date());
            Boolean b = marketOrderApi.updateMarketOrder(request);
            // 发送订单关闭消息
            if (b) {
                this.sendClosePayOrderMessage(OrderPlatformEnum.HMC, marketOrderDTO.getOrderNo());
            }
            return Result.success(b);
        } else {
            return Result.failed("订单已支付成功，取消失败");
        }
    }

    /**
     * 发送订单关闭消息
     *
     * @param orderPlatformEnum
     * @param orderNo
     */
    private void sendClosePayOrderMessage(OrderPlatformEnum orderPlatformEnum, String orderNo) {
        CloseOrderRequest request = new CloseOrderRequest();
        request.setAppOrderNo(orderNo);
        request.setOrderPlatform(orderPlatformEnum.getCode());
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY, Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY, JSON.toJSONString(request));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageSendApi.send(mqMessageBO);
    }

    @ApiOperation(value = "提交订单信息回显")
    @GetMapping("/queryConfirmOrderInfo")
    public Result<QueryConfirmOrderInfoVO> queryConfirmOrderInfo(@CurrentUser CurrentUserInfo currentUser, @ApiParam(value = "商品数量", required = true) @RequestParam(value = "goodsQuantity") Long goodsQuantity) {
        QueryConfirmOrderInfoVO vo = new QueryConfirmOrderInfoVO();
        GoodsDTO goodsDTO = goodsApi.queryInfo(hmcWebProperties.getGoodsId());
        MarketOrderDetailVO detailVO = new MarketOrderDetailVO();
        detailVO.setPic(fileService.getUrl(goodsDTO.getPic(), FileTypeEnum.GOODS_PICTURE)).setGoodsId(hmcWebProperties.getGoodsId()).setGoodsName(goodsDTO.getName()).setGoodsPrice(goodsDTO.getPrice()).setGoodsSpecification(goodsDTO.getSellSpecifications()).setGoodsQuantity(goodsQuantity.intValue());
        vo.setDetailVO(detailVO);
        vo.setEid(goodsDTO.getEid());
        vo.setEname(goodsDTO.getEname());
        BigDecimal goodsTotalAmount = goodsDTO.getPrice().multiply(BigDecimal.valueOf(goodsQuantity));
        // 订单总额 = 运费 + 商品总额
        BigDecimal freightAmount = BigDecimal.ZERO;
        vo.setOrderTotalAmount(goodsTotalAmount.add(freightAmount));
        QueryPageListRequest request = new QueryPageListRequest();
        request.setCurrent(1).setSize(1).setOpUserId(currentUser.getCurrentUserId());
        Page<AddressDTO> addressDTOPage = addressApi.queryAddressPage(request);
        if (addressDTOPage.getTotal() > 0) {
            AddressDTO addressDTO = addressDTOPage.getRecords().get(0);
            vo.setAddressVO(PojoUtils.map(addressDTO, QueryAddressVO.class));
        }
        return Result.success(vo);
    }

}
