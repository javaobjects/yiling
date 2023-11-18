package com.yiling.f2b.admin.order.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.yiling.f2b.admin.order.form.QueryOrderInfoForm;
import com.yiling.f2b.admin.order.form.ReceiveReceiptForm;
import com.yiling.f2b.admin.order.form.SaveOrderDeliveryListInfoForm;
import com.yiling.f2b.admin.order.form.SaveOrderReceiveListInfoForm;
import com.yiling.f2b.admin.order.vo.EnterpriseInfoVO;
import com.yiling.f2b.admin.order.vo.OrderAddressVO;
import com.yiling.f2b.admin.order.vo.OrderCountNumberVO;
import com.yiling.f2b.admin.order.vo.OrderDeliveryVO;
import com.yiling.f2b.admin.order.vo.OrderDetailDeliveryVO;
import com.yiling.f2b.admin.order.vo.OrderDetailVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceApplyAllInfoVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceVo;
import com.yiling.f2b.admin.order.vo.OrderLogVO;
import com.yiling.f2b.admin.order.vo.OrderPageVO;
import com.yiling.f2b.admin.order.vo.OrderPurchaseDetailVO;
import com.yiling.f2b.admin.order.vo.OrderSelleDetailVO;
import com.yiling.f2b.admin.order.vo.OrderTicketDiscountVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderInvoiceApi;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderTicketDiscountApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderAttachmentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.dto.OrderNumberDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.dto.request.QueryOrderReturnPageRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderReceiveListInfoRequest;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderRangeTypeEnum;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.ErpSyncLevelEnum;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author:wei.wang
 * @date:2021/6/22
 */
@RestController
@Api(tags = "订单管理")
@RequestMapping("/order")
public class OrderController extends BaseController {
    @DubboReference
    OrderProcessApi        orderMallApi;
    @DubboReference
    OrderApi               orderApi;
    @DubboReference
    OrderDetailApi         orderDetailApi;
    @DubboReference
    OrderAddressApi        orderAddressApi;
    @DubboReference
    OrderLogApi            orderLogApi;
    @DubboReference
    OrderDeliveryApi       orderDeliveryApi;
    @DubboReference
    GoodsApi               goodsApi;
    @DubboReference
    OrderInvoiceApplyApi   orderInvoiceApplyApi;
    @DubboReference
    OrderInvoiceApi        orderInvoiceApi;
    @DubboReference
    UserApi                userApi;
    @DubboReference
    EnterpriseApi          enterpriseApi;
    @DubboReference
    OrderDetailChangeApi   orderDetailChangeApi;
    @DubboReference
    OrderTicketDiscountApi orderTicketDiscountApi;
    @DubboReference
    OrderReturnApi         orderReturnApi;
    @DubboReference
    DataPermissionsApi     dataPermissionsApi;
    @DubboReference
    DepartmentApi departmentApi;

    @Autowired
    FileService     fileService;
    @Autowired
    PictureUrlUtils pictureUrlUtils;


    @ApiOperation(value = "采购订单数量接口")
    @GetMapping("/get/number")
    public Result<OrderCountNumberVO> getOrderNumber(@CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eids = new ArrayList<Long>() {{
            add(staffInfo.getCurrentEid());
        }};
        OrderNumberDTO orderNumber = orderApi.getOrderNumber(OrderRangeTypeEnum.ORDER_PURCHASE_RANGE_TYPE.getCode(), eids, staffInfo.getCurrentUserId(), OrderTypeEnum.POP.getCode(),null);
        return Result.success(PojoUtils.map(orderNumber, OrderCountNumberVO.class));
    }

    @ApiOperation(value = "销售订单数量接口")
    @GetMapping("/get/sale/number")
    public Result<OrderCountNumberVO> getSaleOrderNumber(@CurrentUser CurrentStaffInfo staffInfo,
                                                         @RequestParam(value = "departmentType",required = false) Integer departmentType) {
        List<Long> eids = new ArrayList<Long>() {{
            add(staffInfo.getCurrentEid());
        }};
        OrderNumberDTO orderNumber = new OrderNumberDTO();
        List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        if (staffInfo.getYilingAdminFlag()) {
            //以岭管理员

            orderNumber = orderApi.getOrderNumber(OrderRangeTypeEnum.ORDER_SALE_ADMIN_RANGE_TYPE.getCode(), list, staffInfo.getCurrentUserId(), OrderTypeEnum.POP.getCode(),null);
        } else if (staffInfo.getYilingFlag()) {
            //以岭普通人员
            orderNumber = orderApi.getOrderNumber(OrderRangeTypeEnum.ORDER_SALE_YILINGH_RANGE_TYPE.getCode(), list, staffInfo.getCurrentUserId(), OrderTypeEnum.POP.getCode(),null);
        } else {

            Long departmentId = null;
            EnterpriseDepartmentDTO enterpriseDepartment = null;
            if(departmentType != null){
                if (departmentType == 3){
                    //大运河数拓部门
                    enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_ST_DEPARTMENT_CODE);
                }else if(departmentType == 4){
                    //大运河分销部门
                    enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_FX_DEPARTMENT_CODE);
                }
            }

            if(enterpriseDepartment != null){
                departmentId = enterpriseDepartment.getId();
            }
            //非以岭人员
            orderNumber = orderApi.getOrderNumber(OrderRangeTypeEnum.ORDER_SALE_NOT_YILINGH_RANGE_TYPE.getCode(), eids, staffInfo.getCurrentUserId(), OrderTypeEnum.POP.getCode(),departmentId);
        }

        return Result.success(PojoUtils.map(orderNumber, OrderCountNumberVO.class));
    }


    @ApiOperation(value = "订单列表")
    @PostMapping("/get/page")
    public Result<Page<OrderPageVO>> getOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOrderInfoForm form) {
        QueryOrderPageRequest request = PojoUtils.map(form, QueryOrderPageRequest.class);
        request.setOrderType(OrderTypeEnum.POP.getCode());
        List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);
        List<Long> longs = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);

        List<Long> list = new ArrayList<>();
        //request.setYiLingOrdinary(Boolean.FALSE);

        Map<Long, EnterpriseDTO> enterpriseMap = new HashMap<>();
        if (request.getType() == 1) {
            if (staffInfo.getYilingFlag()) {

                enterpriseMap = enterpriseApi.getMapByIds(subEidLists);

                list.addAll(subEidLists);
                List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
                request.setContacterIdList(contacterIdList);

            } else {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());
                //非以岭人员
                list.add(staffInfo.getCurrentEid());
                enterpriseMap.put(staffInfo.getCurrentEid(), enterpriseDTO);

                EnterpriseDepartmentDTO enterpriseDepartment = null;
                if(request.getDepartmentType() != null){
                    if (request.getDepartmentType() == 3){
                        //大运河数拓部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_ST_DEPARTMENT_CODE);
                    }else if(request.getDepartmentType() == 4){
                        //大运河分销部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_FX_DEPARTMENT_CODE);
                    }
                }

                if(enterpriseDepartment != null){
                    request.setDepartmentId(enterpriseDepartment.getId());
                }
            }


        } else {
            list.add(staffInfo.getCurrentEid());

        }
        request.setEidList(list);


        Page<OrderDTO> orderPage = orderApi.getOrderPage(request);
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
                if (subEidLists.contains(one.getSellerEid()) || longs.contains(one.getSellerEid())) {
                    one.setYilingSellerFlag(true);
                } else {
                    one.setYilingSellerFlag(false);
                }

                //判断是否有取消，发货按钮
                one.setJudgeDeliveryFlag(false);
                one.setJudgeCancelFlag(false);

                //取消订单按钮
                Boolean judgeCancelFlag = false;
                //发货订单按钮
                Boolean judgeDeliveryFlag = false;
                EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getSellerEid());
                if (enterpriseDTO != null) {
                    if (enterpriseDTO.getErpSyncLevel().compareTo(ErpSyncLevelEnum.ORDER_DOCKING.getCode()) <= 0) {
                        judgeDeliveryFlag = true;

                    }

                    if (enterpriseDTO.getErpSyncLevel().compareTo(ErpSyncLevelEnum.BASED_DOCKING.getCode()) <= 0) {
                        judgeCancelFlag = true;
                    }
                }

                if (OrderStatusEnum.getByCode(one.getOrderStatus()) == OrderStatusEnum.UNDELIVERED) {
                    if (judgeDeliveryFlag && one.getPaymentMethod().compareTo(0) != 0) {
                        one.setJudgeDeliveryFlag(true);
                    }

                    if (judgeCancelFlag && (OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.NOT_PUSH
                            || OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.PUSH_FAIL)) {

                        one.setJudgeCancelFlag(true);

                    }

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
                if (request.getType() == 1) {
                    one.setOrderReturnSubmitFlag(Boolean.FALSE);
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

    @ApiOperation(value = "销售订单详情")
    @GetMapping("/sale/get/detail")
    public Result<OrderSelleDetailVO> getOrderSaleDetailAllInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                                @RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderSelleDetailVO result = PojoUtils.map(orderInfo, OrderSelleDetailVO.class);
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());

        if (staffInfo.getYilingFlag() ||
                EnterpriseChannelEnum.INDUSTRY_DIRECT == EnterpriseChannelEnum.getByCode(enterpriseDTO.getChannelId())) {
            result.setYLFlag(true);
        } else {
            result.setYLFlag(false);
        }


        OrderInvoiceApplyAllInfoVO orderInvoiceApplyAllInfo = getOrderInvoiceApplyAllInfoVO(orderId, orderInfo.getInvoiceStatus());

        result.setOrderInvoiceApplyAllInfo(orderInvoiceApplyAllInfo);

        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderId);
        OrderAddressVO address = PojoUtils.map(addressInfo, OrderAddressVO.class);
        if (addressInfo != null) {
            address.setAddress(addressInfo.getAddress());
        }
        result.setOrderAddress(address);

        List<OrderAttachmentDTO> orderContractList = orderApi.listOrderAttachmentsByType(orderId, OrderAttachmentTypeEnum.RECEIPT_FILE);
        //组装回执单信息
        List<FileInfoVO> fileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderContractList)) {
            for (OrderAttachmentDTO one : orderContractList) {
                FileInfoVO file = new FileInfoVO();
                file.setFileKey(one.getFileKey());
                file.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.ORDER_RECEIVE_ONE_RECEIPT));
                fileList.add(file);
            }
        }
        result.setReceiveReceiptList(fileList);

        List<OrderLogDTO> orderLogInfo = orderLogApi.getOrderLogInfo(orderId);
        result.setOrderLogInfo(PojoUtils.map(orderLogInfo, OrderLogVO.class));

        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        List<OrderDetailDeliveryVO> orderDetailDeliveryList = new ArrayList<>();
        List<Long> listIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();
        Boolean flag = OrderStatusEnum.PARTDELIVERED.getCode() <= result.getOrderStatus();
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderId);
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
        //货款总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        //票折总金额
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
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
                            .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
            orderDetailDelivery.setRealAmount(orderDetailDelivery.getGoodsAmount()
                    .subtract(orderDetailDelivery.getDiscountAmount()));

            //ticketDiscountAmount = ticketDiscountAmount.add(orderDetailChangeOne.getTicketDiscountAmount()
            //.subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
            cashDiscountAmount = cashDiscountAmount.add(orderDetailChangeOne.getCashDiscountAmount()
                    .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount()));
            totalAmount = totalAmount.add(orderDetailDelivery.getGoodsAmount());

            orderDetailDeliveryList.add(orderDetailDelivery);
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

        List<OrderTicketDiscountDTO> ticketDiscountList = orderTicketDiscountApi.listByOrderId(orderId);
        List<OrderTicketDiscountVO> orderTicketDiscountList = new ArrayList<>();
        for (OrderTicketDiscountDTO one : ticketDiscountList) {
            OrderTicketDiscountVO ticketOne = new OrderTicketDiscountVO();
            ticketOne.setTicketDiscountAmount(one.getUseAmount());
            ticketOne.setTicketDiscountNo(one.getTicketDiscountNo());
            ticketOne.setApplyId(one.getApplyId());
            orderTicketDiscountList.add(ticketOne);
            ticketDiscountAmount = ticketDiscountAmount.add(one.getUseAmount());

        }
        result.setTicketDiscountAmount(ticketDiscountAmount);
        result.setPaymentAmount(totalAmount.subtract(cashDiscountAmount).subtract(ticketDiscountAmount));
        result.setTicketDiscountList(orderTicketDiscountList);
        result.setOrderDetailDelivery(orderDetailDeliveryList);
        return Result.success(result);
    }

    @ApiOperation(value = "采购订单详情列表")
    @GetMapping("/purchase/get/detail")
    public Result<OrderPurchaseDetailVO> getOrderPurchaseDetailAllInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                                       @RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderPurchaseDetailVO result = PojoUtils.map(orderInfo, OrderPurchaseDetailVO.class);

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

        List<OrderAttachmentDTO> orderContractList = orderApi.listOrderAttachmentsByType(orderId, OrderAttachmentTypeEnum.RECEIPT_FILE);
        //组装回执单信息
        List<FileInfoVO> fileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderContractList)) {
            for (OrderAttachmentDTO one : orderContractList) {
                FileInfoVO file = new FileInfoVO();
                file.setFileKey(one.getFileKey());
                file.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.ORDER_RECEIVE_ONE_RECEIPT));
                fileList.add(file);
            }
        }
        result.setReceiveReceiptList(fileList);

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


            //ticketDiscountAmount = ticketDiscountAmount.add(orderDetailChangeOne.getTicketDiscountAmount()
            //.subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));

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

        List<OrderTicketDiscountDTO> ticketDiscountList = orderTicketDiscountApi.listByOrderId(orderId);
        List<OrderTicketDiscountVO> orderTicketDiscountList = new ArrayList<>();
        for (OrderTicketDiscountDTO one : ticketDiscountList) {
            OrderTicketDiscountVO ticketOne = new OrderTicketDiscountVO();
            ticketOne.setTicketDiscountAmount(one.getUseAmount());
            ticketOne.setTicketDiscountNo(one.getTicketDiscountNo());
            ticketOne.setApplyId(one.getApplyId());
            orderTicketDiscountList.add(ticketOne);
            ticketDiscountAmount = ticketDiscountAmount.add(one.getUseAmount());
        }
        result.setTicketDiscountList(orderTicketDiscountList);
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

    @ApiOperation(value = "查看发票详情")
    @GetMapping("/invoice/get/detail")
    public Result<OrderInvoiceApplyAllInfoVO> getOrderInvoiceDetail(@RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderInvoiceApplyAllInfoVO orderInvoiceApplyAllInfoVO = getOrderInvoiceApplyAllInfoVO(orderId, orderInfo.getInvoiceStatus());
        return Result.success(orderInvoiceApplyAllInfoVO);
    }

    @ApiOperation(value = "销售订单发货")
    @PostMapping("/delivery")
    public Result<BoolObject> saveOrderDelivery(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody SaveOrderDeliveryListInfoForm form) {
        SaveOrderDeliveryListInfoRequest request = PojoUtils.map(form, SaveOrderDeliveryListInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean result = orderMallApi.frontDelivery(request);
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

    @ApiOperation(value = "上传回执单信息")
    @PostMapping("/upload/receipt")
    public Result<BoolObject> updateReceiveReceipt(@CurrentUser CurrentStaffInfo staffInfo,
                                                   @Valid @RequestBody ReceiveReceiptForm form) {

        Boolean result = orderApi.updateReceiveReceipt(form.getOrderId(), form.getReceiveReceiptList(), staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "取消订单")
    @GetMapping("/cancel")
    public Result<BoolObject> cancel(@CurrentUser CurrentStaffInfo staffInfo,
                                     @RequestParam(value = "orderId") Long orderId) {

        Boolean result = orderMallApi.cancel(orderId, staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }


    @GetMapping("/super/cancel")
    public Result<BoolObject> superCancel(@CurrentUser CurrentStaffInfo staffInfo,
                                          @RequestParam(value = "orderId") Long orderId) {


        Boolean result = orderMallApi.cancel(orderId, staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "分摊金额")
    @GetMapping("/share/amount")
    public Result<BoolObject> shareAllDeliveryErpCashAmount(@CurrentUser CurrentStaffInfo staffInfo) {
        Boolean result = orderApi.shareAllDeliveryErpCashAmount();
        return Result.success(new BoolObject(result));
    }

}
