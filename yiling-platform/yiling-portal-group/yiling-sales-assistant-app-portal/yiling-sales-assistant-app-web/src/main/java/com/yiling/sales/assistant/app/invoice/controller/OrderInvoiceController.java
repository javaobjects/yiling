package com.yiling.sales.assistant.app.invoice.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.mall.order.api.OrderInvoiceApplyProcessApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderInvoiceApi;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.api.OrderInvoiceDetailApi;
import com.yiling.order.order.api.OrderTicketDiscountApi;
import com.yiling.order.order.api.TicketDiscountRecordApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.dto.OrderInvoiceDetailDTO;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.QueryInvoicePageRequest;
import com.yiling.order.order.dto.request.SaveOrderTicketApplyInfoRequest;
import com.yiling.order.order.enums.OrderInvoiceDiscountTypeEnum;
import com.yiling.sales.assistant.app.invoice.form.OrderApplyTicketComputeForm;
import com.yiling.sales.assistant.app.invoice.form.QueryInvoicePageForm;
import com.yiling.sales.assistant.app.invoice.form.SaveOrderDetailTicketDiscountForm;
import com.yiling.sales.assistant.app.invoice.vo.OrderApplyInvoiceDetailVO;
import com.yiling.sales.assistant.app.invoice.vo.OrderApplyInvoiceVO;
import com.yiling.sales.assistant.app.invoice.vo.OrderApplyTicketComputeVO;
import com.yiling.sales.assistant.app.invoice.vo.OrderChoiceTicketInfoVO;
import com.yiling.sales.assistant.app.invoice.vo.OrderInvoiceListPageVO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 发票管理模块
 *
 * @author:wei.wang
 * @date:2021/9/15
 */
@RestController
@RequestMapping("/invoice")
@Api(tags = "发票管理模块")
@Slf4j
public class OrderInvoiceController extends BaseController {

    @DubboReference
    OrderApi                orderApi;
    @DubboReference
    OrderInvoiceApplyApi        orderInvoiceApplyApi;
    @DubboReference
    OrderDetailChangeApi        orderDetailChangeApi;
    @DubboReference
    OrderInvoiceApi             orderInvoiceApi;
    @DubboReference
    OrderDetailApi              orderDetailApi;
    @DubboReference
    TicketDiscountRecordApi     ticketDiscountRecordApi;
    @DubboReference
    OrderInvoiceDetailApi       orderInvoiceDetailApi;
    @DubboReference
    GoodsApi                    goodsApi;
    @DubboReference
    OrderTicketDiscountApi      orderTicketDiscountApi;
    @DubboReference
    OrderInvoiceApplyProcessApi orderInvoiceApplyProcessApi;

    @Autowired
    FileService     fileService;


    @ApiOperation(value = "发票列表")
    @GetMapping("/get/list")
    @UserAccessAuthentication
    public Result<Page<OrderInvoiceListPageVO>> getInvoiceList(@CurrentUser CurrentStaffInfo staffInfo,
                                                         @RequestBody @Valid QueryInvoicePageForm form) {

        QueryInvoicePageRequest request = PojoUtils.map(form, QueryInvoicePageRequest.class);
        Page<OrderDTO> page = orderApi.getOrderInvoiceInfoPage(request);
        Page<OrderInvoiceListPageVO> result = PojoUtils.map(page, OrderInvoiceListPageVO.class);
        if (page != null && CollectionUtil.isNotEmpty(page.getRecords())) {
            List<Long> orderList = page.getRecords().stream().map(o -> o.getId()).collect(Collectors.toList());
            List<OrderInvoiceApplyDTO> invoiceApplyList = orderInvoiceApplyApi.getOrderInvoiceApplyByList(orderList);

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(orderList);
            Map<Long, List<OrderDetailChangeDTO>> changeMap = new HashMap<>();
            for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
                if (changeMap.containsKey(changeOne.getOrderId())) {
                    List<OrderDetailChangeDTO> orderChangeList = changeMap.get(changeOne.getOrderId());
                    orderChangeList.add(changeOne);
                } else {
                    changeMap.put(changeOne.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {{
                        add(changeOne);
                    }});
                }
            }

            for (OrderInvoiceListPageVO one : result.getRecords()) {
                //货款总金额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //票折总金额
                BigDecimal ticketAmount = BigDecimal.ZERO;
                //现折总金额
                BigDecimal cashAmount = BigDecimal.ZERO;
                //购买数量
                Integer purchaseGoodsNumber = 0;

                List<OrderDetailChangeDTO> orderDetailChange = changeMap.get(one.getId());
                for (OrderDetailChangeDTO detailChangeOne : orderDetailChange) {
                    totalAmount = totalAmount.add(detailChangeOne.getGoodsAmount()
                            .subtract(detailChangeOne.getSellerReturnAmount()));
                    ticketAmount = ticketAmount.add(detailChangeOne.getTicketDiscountAmount()
                            .subtract(detailChangeOne.getSellerReturnTicketDiscountAmount()));
                    cashAmount = cashAmount.add(detailChangeOne.getCashDiscountAmount()
                            .subtract(detailChangeOne.getSellerReturnCashDiscountAmount()));
                    purchaseGoodsNumber = purchaseGoodsNumber + detailChangeOne.getGoodsQuantity();
                }
                one.setTotalAmount(totalAmount)
                        .setDiscountAmount(ticketAmount.add(cashAmount))
                        .setPaymentAmount(totalAmount.subtract(ticketAmount).subtract(cashAmount))
                        .setPurchaseGoodsNumber(purchaseGoodsNumber);
            }

            if (CollectionUtil.isNotEmpty(invoiceApplyList)) {

                List<Long> applyIds = invoiceApplyList.stream().map(order -> order.getId()).collect(Collectors.toList());

                List<OrderInvoiceDTO> applyList = orderInvoiceApi.getOrderInvoiceByApplyIdList(applyIds);
                Map<Long, List<OrderInvoiceDTO>> map = new HashMap<>();
                if (CollectionUtil.isNotEmpty(applyList)) {
                    for (OrderInvoiceDTO one : applyList) {
                        if (map.containsKey(one.getOrderId())) {
                            List<OrderInvoiceDTO> invoiceLists = map.get(one.getApplyId());
                            invoiceLists.add(one);
                        } else {
                            List<OrderInvoiceDTO> invoiceLists = new ArrayList<>();
                            invoiceLists.add(one);
                            map.put(one.getOrderId(), invoiceLists);
                        }
                    }
                }
                for (OrderInvoiceListPageVO one : result.getRecords()) {
                    one.setInvoiceNumber(map.get(one.getId()) != null ? map.get(one.getId()).size() : 0);
                }
            }

        }
        return Result.success(result);
    }

    @ApiOperation(value = "选择票折")
    @GetMapping("/choice/invoice")
    public Result<CollectionObject<OrderChoiceTicketInfoVO>> getTicketDiscount(@CurrentUser CurrentStaffInfo staffInfo,
                                                                               @ApiParam(value = "订单ID", required = true) @RequestParam("orderId") Long orderId) {
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);

        String sellerErpCode = orderDTO.getSellerErpCode();
        String customerErpCode = orderDTO.getCustomerErpCode();
        if (StrUtil.isEmpty(sellerErpCode) || StrUtil.isEmpty(customerErpCode)) {
            return Result.failed("订单数据不完整");
        }
        List<TicketDiscountRecordDTO> list = ticketDiscountRecordApi.listCustomerTicketDiscounts(sellerErpCode, customerErpCode);
        List<OrderChoiceTicketInfoVO> result = PojoUtils.map(list, OrderChoiceTicketInfoVO.class);
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> nos = result.stream().map(order -> order.getTicketDiscountNo()).collect(Collectors.toList());
            List<OrderTicketDiscountDTO> orderTicketDiscountList = orderTicketDiscountApi.getOrderTicketDiscountByListNos(nos);
            Map<String, List<Long>> map = new HashMap<>();
            for (OrderTicketDiscountDTO one : orderTicketDiscountList) {
                if (map.containsKey(one.getTicketDiscountNo())) {
                    List<Long> longs = map.get(one.getTicketDiscountNo());
                    if (!longs.contains(one.getOrderId())) {
                        longs.add(one.getOrderId());
                    }
                } else {
                    map.put(one.getTicketDiscountNo(), new ArrayList<Long>() {{
                        add(one.getOrderId());
                    }});
                }
            }
            for (OrderChoiceTicketInfoVO one : result) {
                one.setUsedOrder(map.get(one.getTicketDiscountNo()) == null ? 0 : map.get(one.getTicketDiscountNo()).size());
            }
        }
        return Result.success(new CollectionObject(result));
    }


    @ApiOperation(value = "申请发票提交")
    @PostMapping("/apply/submit")
    public Result<BoolObject> saveTicketDiscountInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                     @Valid @RequestBody SaveOrderDetailTicketDiscountForm form) {
        SaveOrderTicketApplyInfoRequest request = PojoUtils.map(form, SaveOrderTicketApplyInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        Boolean result = orderInvoiceApplyProcessApi.apply(request);
        return Result.success(new BoolObject(result));
    }



    @ApiOperation(value = "选择票折计算金额")
    @PostMapping("/compute/amount")
    public Result<OrderApplyTicketComputeVO> computeAmount(@CurrentUser CurrentStaffInfo staffInfo,
                                                           @Valid @RequestBody OrderApplyTicketComputeForm form) {

        OrderApplyTicketComputeVO result = new OrderApplyTicketComputeVO();

        BigDecimal ticketDiscountAllAmount = BigDecimal.ZERO;
        BigDecimal cashDiscountAllAmount = BigDecimal.ZERO;
        BigDecimal allAmount = BigDecimal.ZERO;
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(form.getOrderId());
        for (OrderDetailChangeDTO one : orderDetailChangeList) {
            if (one.getDetailId().equals(form.getDetailId())) {
                if (OrderInvoiceDiscountTypeEnum.USE_RATE_TYPE.getCode().equals(form.getInvoiceDiscountType())) {
                    //使用比率
                    //票折金额
                    BigDecimal ticketDiscountAmount = one.getDeliveryAmount().multiply(form.getValue()).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal ticketDiscountRate = form.getValue();
                    ticketDiscountAllAmount = ticketDiscountAllAmount.add(ticketDiscountAmount);
                    result.setTicketDiscountRate(ticketDiscountRate);
                    result.setTicketDiscountAmount(ticketDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                    result.setGoodsDiscountAmount(result.getTicketDiscountAmount().add(one.getDeliveryCashDiscountAmount()));
                    BigDecimal goodsDiscountRate = result.getGoodsDiscountAmount().multiply(BigDecimal.valueOf(100)).divide(one.getDeliveryAmount(), 2, BigDecimal.ROUND_HALF_UP);
                    result.setGoodsDiscountRate(goodsDiscountRate);
                } else if (OrderInvoiceDiscountTypeEnum.USE_AMOUNT_TYPE.getCode().equals(form.getInvoiceDiscountType())) {
                    //使用金额
                    ticketDiscountAllAmount = ticketDiscountAllAmount.add(form.getValue());
                    BigDecimal ticketDiscountRate = form.getValue().multiply(BigDecimal.valueOf(100))
                            .divide(one.getDeliveryAmount(), 2, BigDecimal.ROUND_HALF_UP);
                    result.setTicketDiscountRate(ticketDiscountRate);
                    result.setTicketDiscountAmount(form.getValue());
                    result.setGoodsDiscountAmount(result.getTicketDiscountAmount().add(one.getDeliveryCashDiscountAmount()));
                    BigDecimal goodsDiscountRate = result.getGoodsDiscountAmount().multiply(BigDecimal.valueOf(100)).divide(one.getDeliveryAmount(), 2, BigDecimal.ROUND_HALF_UP);
                    result.setGoodsDiscountRate(goodsDiscountRate);
                }
                cashDiscountAllAmount = cashDiscountAllAmount.add(one.getDeliveryCashDiscountAmount());
                allAmount = allAmount.add(one.getDeliveryAmount());
            } else {
                ticketDiscountAllAmount = ticketDiscountAllAmount.add(one.getTicketDiscountAmount());
                cashDiscountAllAmount = cashDiscountAllAmount.add(one.getDeliveryCashDiscountAmount());
                allAmount = allAmount.add(one.getDeliveryAmount());
            }

        }

        result.setInvoiceAmount(allAmount.subtract(cashDiscountAllAmount)
                .subtract(ticketDiscountAllAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
        result.setTicketDiscountAllAmount(ticketDiscountAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        return Result.success(result);
    }


}
