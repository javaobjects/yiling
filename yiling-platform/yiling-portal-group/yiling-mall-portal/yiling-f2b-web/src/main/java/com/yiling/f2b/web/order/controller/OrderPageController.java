package com.yiling.f2b.web.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.web.order.form.OrderListPageForm;
import com.yiling.f2b.web.order.form.SaveOrderReceiveListInfoForm;
import com.yiling.f2b.web.order.vo.EnterpriseInfoVO;
import com.yiling.f2b.web.order.vo.OrderDeliveryVO;
import com.yiling.f2b.web.order.vo.OrderDetailDeliveryVO;
import com.yiling.f2b.web.order.vo.OrderDetailVO;
import com.yiling.f2b.web.order.vo.OrderInvoiceApplyAllInfoVO;
import com.yiling.f2b.web.order.vo.OrderInvoiceVo;
import com.yiling.f2b.web.order.vo.OrderLogVO;
import com.yiling.f2b.web.order.vo.OrderPageVO;
import com.yiling.f2b.web.order.vo.OrderPurchaseDetailVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderInvoiceApi;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.request.OrderPOPWebListPageRequest;
import com.yiling.order.order.dto.request.QueryOrderReturnPageRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单页面 Controller
 *
 * @author: wei.wang
 * @date: 2023/01/05
 */
@RestController
@RequestMapping("/order/page")
@Api(tags = "采购订单接口")
@Slf4j
public class OrderPageController  extends BaseController {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderProcessApi orderMallApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    OrderInvoiceApplyApi orderInvoiceApplyApi;
    @DubboReference
    OrderInvoiceApi orderInvoiceApi;
    @DubboReference
    UserApi userApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "订单列表")
    @PostMapping("/list")
    public Result<Page<OrderPageVO>> getOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderListPageForm form) {
        OrderPOPWebListPageRequest request = PojoUtils.map(form, OrderPOPWebListPageRequest.class);
        request.setOrderType(OrderTypeEnum.POP.getCode());
        List<Long> list = new ArrayList<>();
        list.add(staffInfo.getCurrentEid());
        request.setBuyerEidList(list);

        Page<OrderDTO> orderPage = orderApi.getPOPWebOrderListPage(request);
        Page<OrderPageVO> page = PojoUtils.map(orderPage, OrderPageVO.class);
        List<Long> goodsIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
            Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
            List<OrderDetailDTO> detailsList = orderDetailApi.getOrderDetailByOrderIds(ids);

            for (OrderPageVO one : page.getRecords()) {
                List<OrderDetailVO> goodOrderList = new ArrayList<>();
                //货款总额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //折扣金额
                BigDecimal discountAmount = BigDecimal.ZERO;
                for (OrderDetailDTO detail : detailsList) {
                    if (one.getId().equals(detail.getOrderId())) {
                        OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(detail.getId());
                        OrderDetailVO detailVO = PojoUtils.map(detail, OrderDetailVO.class);
                        detailVO.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                                .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                                .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                                .setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                                .setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount()
                                        .add(orderDetailChangeOne.getTicketDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
                        detailVO.setRealAmount(detailVO.getGoodsAmount().subtract(detailVO.getDiscountAmount()));
                        goodOrderList.add(detailVO);
                        totalAmount = totalAmount.add(detailVO.getGoodsAmount());
                        discountAmount = discountAmount.add(detailVO.getDiscountAmount());
                    }
                    goodsIds.add(detail.getGoodsId());
                    goodsSukIds.add(detail.getGoodsSkuId());
                }
                //设置金额
                one.setDiscountAmount(discountAmount)
                        .setTotalAmount(totalAmount)
                        .setPaymentAmount(totalAmount.subtract(discountAmount));



                one.setJudgeCancelFlag(false);
                one.setJudgePayFlag(false);

                if(OrderStatusEnum.getByCode(one.getOrderStatus()) != OrderStatusEnum.CANCELED  && OrderAuditStatusEnum.getByCode(one.getAuditStatus()) == OrderAuditStatusEnum.UNSUBMIT ){
                    one.setJudgePayFlag(true);
                }
                if((OrderAuditStatusEnum.getByCode(one.getAuditStatus()) == OrderAuditStatusEnum.UNSUBMIT || OrderAuditStatusEnum.getByCode(one.getAuditStatus()) == OrderAuditStatusEnum.UNAUDIT) && OrderStatusEnum.getByCode(one.getOrderStatus()) != OrderStatusEnum.CANCELED ){

                    one.setJudgeCancelFlag(true);
                }

                one.setGoodOrderList(goodOrderList);
                OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(one.getId());
                one.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum())
                        .setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum())
                        .setReceiveOrderNum(goodsTypeNumber.getReceiveOrderNum()).setReceiveOrderPieceNum(goodsTypeNumber.getReceiveOrderPieceNum())
                        .setDeliveryOrderNum(goodsTypeNumber.getDeliveryOrderNum()).setDeliveryOrderPieceNum(goodsTypeNumber.getDeliveryOrderPieceNum());
            }
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);
            List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
            Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
                skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
            }
            Map<Long, List<OrderReturnDetailDTO>> returnMap = orderReturnApi.queryByOrderIdList(ids);
            for (OrderPageVO one : page.getRecords()) {
                Integer returnNumber = 0;
                for (OrderDetailVO goods : one.getGoodOrderList()) {
                    goods.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(goods.getGoodsId())));
                    goods.setPackageNumber(skuGoodsMap.get(goods.getGoodsSkuId()) != null ? skuGoodsMap.get(goods.getGoodsSkuId()).getPackageNumber() : 1L);
                    goods.setGoodsRemark(skuGoodsMap.get(goods.getGoodsSkuId()) != null ? skuGoodsMap.get(goods.getGoodsSkuId()).getRemark() : "");

                }
                List<OrderReturnDetailDTO> orderReturnDetailDTOS = returnMap.get(one.getId());

                if (CollectionUtil.isNotEmpty(orderReturnDetailDTOS)) {
                    for (OrderReturnDetailDTO returnOne : orderReturnDetailDTOS) {
                        returnNumber = returnNumber + returnOne.getReturnQuantity();
                    }
                    if (returnNumber.compareTo(one.getGoodsOrderPieceNum()) == 0) {
                        one.setOrderReturnSubmitFlag(Boolean.FALSE);
                    }
                }
                QueryOrderReturnPageRequest returnPageRequest = new QueryOrderReturnPageRequest();
                returnPageRequest.setOrderId(one.getId());
                Page<OrderReturnDTO> orderReturnPage = orderReturnApi.queryOrderReturnPage(returnPageRequest);
                if (orderReturnPage != null && CollectionUtil.isNotEmpty(orderReturnPage.getRecords())) {
                    one.setOrderReturnFlag(Boolean.TRUE);
                } else {
                    one.setOrderReturnFlag(Boolean.FALSE);
                }
            }
        }

        return Result.success(page);
    }

    @ApiOperation(value = "预订单取消")
    @GetMapping("/cancel")
    public Result<BoolObject> cancelOrderExpect(@CurrentUser CurrentStaffInfo staffInfo,
                                                @RequestParam(value = "orderId") Long orderId) {
        Boolean result = orderMallApi.cancelOrderExpect(orderId, staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "采购订单收货")
    @PostMapping("/receive")
    public Result<BoolObject> saveOrderReceive(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody SaveOrderReceiveListInfoForm form) {
        SaveOrderReceiveListInfoRequest request = PojoUtils.map(form, SaveOrderReceiveListInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        Boolean result = orderMallApi.receive(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "采购订单详情列表")
    @GetMapping("/detail")
    public Result<OrderPurchaseDetailVO> getOrderPurchaseDetailAllInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                                       @RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderPurchaseDetailVO result = PojoUtils.map(orderInfo, OrderPurchaseDetailVO.class);

        UserDTO user = userApi.getById(result.getAuditUser());
        if(user != null){
            result.setAuditUserName(user.getName());
        }
        EnterpriseDTO enterprise = enterpriseApi.getById(result.getSellerEid());
        EnterpriseInfoVO enterpriseInfo = PojoUtils.map(enterprise, EnterpriseInfoVO.class);
        if (enterprise != null) {
            enterpriseInfo.setAddress(enterprise.getProvinceName() + enterprise.getCityName() + enterprise.getRegionName() + enterprise.getAddress());
        }
        result.setEnterpriseInfo(enterpriseInfo);

        List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(orderInfo.getSellerEid());

        if (subEidLists.contains(orderInfo.getSellerEid()) ||
                EnterpriseChannelEnum.INDUSTRY_DIRECT == EnterpriseChannelEnum.getByCode(enterpriseDTO.getChannelId())) {
            result.setYLFlag(true);
        } else {
            result.setYLFlag(false);
        }

        OrderInvoiceApplyAllInfoVO orderInvoiceApplyAllInfo = getOrderInvoiceApplyAllInfoVO(orderId, orderInfo.getInvoiceStatus());

        result.setOrderInvoiceApplyAllInfo(orderInvoiceApplyAllInfo);

        List<OrderLogDTO> orderLogInfo = orderLogApi.getOrderLogInfo(orderId);
        result.setOrderLogInfo(PojoUtils.map(orderLogInfo, OrderLogVO.class));

        //List<OrderAttachmentDTO> orderContractList = orderApi.listOrderAttachmentsByType(orderId, OrderAttachmentTypeEnum.RECEIPT_FILE);


        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderId);
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));

        List<OrderDetailDeliveryVO> orderDetailDeliveryList = new ArrayList<>();
        List<Long> listIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();
        Boolean flag = OrderStatusEnum.PARTDELIVERED.getCode() <= result.getOrderStatus();
        //货款总额
        BigDecimal totalAmount = BigDecimal.ZERO;

        //现折金额
        BigDecimal cashDiscountAmount = BigDecimal.ZERO;

        for (OrderDetailDTO one : orderDetailInfo) {
            OrderDetailDeliveryVO orderDetailDelivery = PojoUtils.map(one, OrderDetailDeliveryVO.class);
            OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(one.getId());
            if (flag) {
                List<OrderDeliveryDTO> deliveryList = orderDeliveryApi.getOrderDeliveryList(orderId);
                List<OrderDeliveryDTO> list = new ArrayList<>();
                for (OrderDeliveryDTO delivery : deliveryList) {
                    if (one.getId().equals(delivery.getDetailId()) && one.getGoodsId().equals(delivery.getGoodsId())) {
                        list.add(delivery);
                    }
                }
                orderDetailDelivery.setOrderDeliveryList(PojoUtils.map(list, OrderDeliveryVO.class));
            }

            orderDetailDelivery.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                    .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                    .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                    .setGoodsAmount(orderDetailChangeOne.getGoodsAmount()
                            .subtract(orderDetailChangeOne.getSellerReturnAmount()))
                    .setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount()
                            .add(orderDetailChangeOne.getTicketDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount()));
            orderDetailDelivery.setRealAmount(orderDetailDelivery.getGoodsAmount().subtract(orderDetailDelivery.getDiscountAmount()));
            orderDetailDeliveryList.add(orderDetailDelivery);

            cashDiscountAmount = cashDiscountAmount.add(orderDetailChangeOne.getCashDiscountAmount()
                    .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount()));

            totalAmount = totalAmount.add(orderDetailDelivery.getGoodsAmount());


            listIds.add(one.getGoodsId());
            goodsSukIds.add(one.getGoodsSkuId());
        }
        result.setTotalAmount(totalAmount);
        result.setCashDiscountAmount(cashDiscountAmount);

        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
        Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
            skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        }
        Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(listIds);
        for (OrderDetailDeliveryVO one : orderDetailDeliveryList) {
            one.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(one.getGoodsId())));
            one.setPackageNumber(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getPackageNumber() : 1L);
            one.setGoodsRemark(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getRemark() : "");
        }
        //票折总金额
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;

        result.setOrderDetailDelivery(orderDetailDeliveryList);
        result.setTicketDiscountAmount(ticketDiscountAmount);
        result.setPaymentAmount(totalAmount.subtract(cashDiscountAmount).subtract(ticketDiscountAmount));
        // 判断是否允许退货
        checkReturnOrder(orderInfo, result);
        return Result.success(result);
    }

    private OrderInvoiceApplyAllInfoVO getOrderInvoiceApplyAllInfoVO(Long orderId, Integer invoiceStatus) {
        OrderInvoiceApplyAllInfoVO result = new OrderInvoiceApplyAllInfoVO();
        List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyApi.listByOrderId(orderId);
        if (CollectionUtil.isNotEmpty(orderInvoiceApplyList)) {
            result.setStatus(invoiceStatus);
            List<Long> applyIdList = orderInvoiceApplyList.stream().map(o -> o.getId()).collect(Collectors.toList());

            BigDecimal invoiceFinishAmount = orderInvoiceApplyList.stream().map(OrderInvoiceApplyDTO::getInvoiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            List<OrderInvoiceDTO> orderInvoiceByApplyIdList = orderInvoiceApi.getOrderInvoiceByApplyIdList(applyIdList);
            if (CollectionUtil.isNotEmpty(orderInvoiceByApplyIdList)) {
                result.setInvoiceNumber(orderInvoiceByApplyIdList.size());
                Map<Long, List<OrderInvoiceDTO>> applyMap = orderInvoiceByApplyIdList.stream().collect(Collectors.groupingBy(s -> s.getApplyId()));
                List<OrderInvoiceVo> orderInvoiceInfo = new ArrayList<>();
                for (OrderInvoiceApplyDTO applyOne : orderInvoiceApplyList) {
                    OrderInvoiceVo orderInvoiceOne = new OrderInvoiceVo();
                    List<OrderInvoiceDTO> orderInvoiceList = applyMap.get(applyOne.getId());
                    if(CollectionUtil.isNotEmpty(orderInvoiceList)){
                        String invoiceNo = orderInvoiceList.stream().map(o -> o.getInvoiceNo()).collect(Collectors.joining(","));
                        orderInvoiceOne.setInvoiceNo(invoiceNo);
                    }
                    orderInvoiceOne.setApplyId(applyOne.getId());
                    orderInvoiceInfo.add(orderInvoiceOne);
                }
                result.setOrderInvoiceInfo(orderInvoiceInfo);
            }
            result.setInvoiceFinishAmount(invoiceFinishAmount);
        }
        return result;
    }

    /**
     * 订单不允许申请退货
     *
     * @param orderDTO
     */
    private void checkReturnOrder(OrderDTO orderDTO, OrderPurchaseDetailVO result) {
        // 1.订单不允许申请退货
        Date createTime = orderDTO.getCreateTime();
        int createYear = DateUtil.year(createTime);
        int nowYear = DateUtil.year(new Date());
        if (createYear != nowYear) {
            result.setIsAllowReturn(1);
            result.setRefuseReturnReason(OrderErrorCode.ORDER_RETURN_OTHER_YEAR.getMessage());
        }
        // 2.该订单存在未审核的退货单，不允许申请退货
        int count = orderReturnApi.countByOrderIdAndStatus(orderDTO.getId(), OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        if (count > 0) {
            result.setIsAllowReturn(1);
            result.setRefuseReturnReason(OrderErrorCode.ORDER_RETURN_STATUS_IS_AUDIT_EXIST.getMessage());
        }

    }

}
