package com.yiling.b2b.admin.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.order.form.B2BOrderReturnApplyForm;
import com.yiling.b2b.admin.order.form.B2BOrderReturnVerifyForm;
import com.yiling.b2b.admin.order.form.QuerySellerOrderReturnInfoForm;
import com.yiling.b2b.admin.order.vo.EnterpriseInfoVO;
import com.yiling.b2b.admin.order.vo.OrderLogVO;
import com.yiling.b2b.admin.order.vo.OrderPurchaseReturnVO;
import com.yiling.b2b.admin.order.vo.OrderReturnGoodsBatchVO;
import com.yiling.b2b.admin.order.vo.OrderReturnGoodsDetailVO;
import com.yiling.b2b.admin.order.vo.OrderReturnVO;
import com.yiling.b2b.admin.order.vo.ReturnOrderNumberVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderReturnDetailApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.ReturnOrderNumberDTO;
import com.yiling.order.order.dto.request.B2BOrderReturnApplyRequest;
import com.yiling.order.order.dto.request.B2BOrderReturnDetailApplyRequest;
import com.yiling.order.order.dto.request.B2BOrderReturnDetailBatchApplyRequest;
import com.yiling.order.order.dto.request.OrderDeliveryRequest;
import com.yiling.order.order.dto.request.OrderDetailRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.dto.request.OrderReturnVerifyRequest;
import com.yiling.order.order.dto.request.ReturnNumberRequest;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 售后退货接口
 *
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Slf4j
@RestController
@RequestMapping("/return")
@Api(tags = "售后退货接口")
public class OrderReturnController extends BaseController {
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderProcessApi orderProcessApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "退货单审核")
    @PostMapping("/verify")
    public Result<BoolObject> verify(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid B2BOrderReturnVerifyForm form) {
        OrderReturnVerifyRequest request = PojoUtils.map(form, OrderReturnVerifyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Boolean result = orderProcessApi.verifyOrderReturn(request);
        return Result.success(new BoolObject(result));
    }

    /**
     * 根据申请的用户获得需要查询的用户id
     *
     * @param staffInfo 用户登录信息
     * @return 用户id集合
     */
    private List<Long> getRequestUserIdList(CurrentStaffInfo staffInfo) {
        if (staffInfo.getYilingFlag()) {
            return dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_B2B, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        }
        return null;
    }

    /**
     * 根据申请的用户获得需要查询的企业id
     *
     * @param staffInfo 用户登录信息
     * @return 企业id集合
     */
    private List<Long> getRequestEidList(CurrentStaffInfo staffInfo) {
        List<Long> eidList = new ArrayList<>();
        if (staffInfo.getYilingFlag()) {
            eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        } else {
            eidList.add(staffInfo.getCurrentEid());
        }
        return eidList;
    }


    @ApiOperation(value = "销售退货单数量接口")
    @GetMapping("/get/number/seller")
    public Result<ReturnOrderNumberVO> getOrderNumber(@CurrentUser CurrentStaffInfo staffInfo) {
        ReturnNumberRequest request = new ReturnNumberRequest();
        List<Long> userIdList = getRequestUserIdList(staffInfo);
        if (null != userIdList) {
            request.setUserIdList(userIdList);
        }
        request.setSellerIdList(getRequestEidList(staffInfo));
        request.setOrderTypeEnum(OrderTypeEnum.B2B);
        ReturnOrderNumberDTO returnOrderNumberDTO = orderReturnApi.getOrderNumber(request);
        return Result.success(PojoUtils.map(returnOrderNumberDTO, ReturnOrderNumberVO.class));
    }

    @ApiOperation(value = "销售退货单列表")
    @PostMapping("/sellerOrderReturnPageList")
    public Result<Page<OrderReturnVO>> sellerPageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QuerySellerOrderReturnInfoForm form) {
        OrderReturnPageListRequest request = PojoUtils.map(form, OrderReturnPageListRequest.class);
        List<Long> userIdList = getRequestUserIdList(staffInfo);
        if (null != userIdList) {
            request.setUserIdList(userIdList);
        }
        request.setSellerEidList(getRequestEidList(staffInfo));
        request.setSortType(1);
        request.setOrderTypeEnum(OrderTypeEnum.B2B);
        Page<OrderReturnDTO> page = orderReturnApi.pageList(request);
        Page<OrderReturnVO> pageVO = PojoUtils.map(page, OrderReturnVO.class);
        List<OrderReturnVO> records = pageVO.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return Result.success(pageVO);
        }
        List<Long> orderIdList = records.stream().map(OrderReturnVO::getOrderId).collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(orderIdList);
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
        records.forEach(e -> {
            e.setTotalReturnAmount(e.getReturnAmount());
            e.setTotalDiscountAmount(e.getPlatformCouponDiscountAmount()
                    .add(e.getCashDiscountAmount())
                    .add(e.getTicketDiscountAmount())
                    .add(e.getCouponDiscountAmount())
                    .add(e.getPresaleDiscountAmount())
                    .add(e.getPlatformPaymentDiscountAmount())
                    .add(e.getShopPaymentDiscountAmount()));
            e.setReturnAmount(e.getReturnAmount()
                    .subtract(e.getTicketDiscountAmount())
                    .subtract(e.getCashDiscountAmount()).subtract(e.getPlatformCouponDiscountAmount())
                    .subtract(e.getCouponDiscountAmount()).subtract(e.getPresaleDiscountAmount())
                    .subtract(e.getPlatformPaymentDiscountAmount())
                    .subtract(e.getShopPaymentDiscountAmount()));
            OrderDTO orderDTO = orderMap.get(e.getOrderId());
            e.setPaymentMethod(orderDTO.getPaymentMethod());
            e.setPaymentStatus(orderDTO.getPaymentStatus());
            List<OrderReturnDetailDTO> orderReturnDetailList = orderReturnDetailApi.getOrderReturnDetailByReturnId(e.getId());
            Integer returnGoodsNum = orderReturnDetailList.stream().mapToInt(OrderReturnDetailDTO::getReturnQuantity).sum();
            e.setReturnGoodsNum(returnGoodsNum);
            e.setReturnGoods(orderReturnDetailList.size());
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "销售退货单详情")
    @GetMapping("/sellerOrderReturnDetail")
    public Result<OrderReturnVO> sellerOrderReturnDetail(Long returnOrderId) {
        OrderReturnVO orderReturnVo = getOrderReturnVo(returnOrderId);
        return Result.success(orderReturnVo);
    }

    /**
     * 查询退货单详情
     *
     * @param returnOrderId 退货单id
     * @return 退货单详情
     */
    private OrderReturnVO getOrderReturnVo(Long returnOrderId) {
        //根据订单编号查出退货单订单
        OrderReturnDTO orderReturnDTO = orderReturnApi.selectById(returnOrderId);
        if (null == orderReturnDTO) {
            return null;
        }
        OrderReturnVO orderReturnVO = PojoUtils.map(orderReturnDTO, OrderReturnVO.class);
        //根据订单编号查出订单
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderReturnDTO.getOrderNo());
        orderReturnVO.setTotalDiscountAmount(orderReturnDTO.getPlatformCouponDiscountAmount()
                .add(orderReturnDTO.getCouponDiscountAmount())
                .add(orderReturnDTO.getPresaleDiscountAmount())
                .add(orderReturnDTO.getPlatformPaymentDiscountAmount())
                .add(orderReturnDTO.getShopPaymentDiscountAmount()));
        orderReturnVO.setReturnAmount(orderReturnDTO.getReturnAmount()
                .subtract(orderReturnDTO.getPlatformCouponDiscountAmount())
                .subtract(orderReturnDTO.getCouponDiscountAmount())
                .subtract(orderReturnDTO.getPresaleDiscountAmount())
                .subtract(orderReturnDTO.getPlatformPaymentDiscountAmount())
                .subtract(orderReturnDTO.getShopPaymentDiscountAmount()));
        orderReturnVO.setReturnAllAmount(orderReturnDTO.getReturnAmount());
        orderReturnVO.setOrderId(orderDTO.getId());
        orderReturnVO.setPaymentAmount(PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.PAID ? orderDTO.getPaymentAmount() : BigDecimal.ZERO);
        orderReturnVO.setTotalAmount(orderDTO.getTotalAmount());
        orderReturnVO.setPaymentMethod(orderDTO.getPaymentMethod());
        orderReturnVO.setPaymentStatus(orderDTO.getPaymentStatus());
        // 采购商备注
        orderReturnVO.setOrderRemark(orderDTO.getOrderNote());
        // 驳回原因
        orderReturnVO.setRefuseReason(orderReturnDTO.getFailReason());
        OrderAddressDTO orderAddressDTO = orderAddressApi.getOrderAddressInfo(orderDTO.getId());
        if (orderAddressDTO != null) {
            orderReturnVO.setReceiveUserName(orderAddressDTO.getName());
            orderReturnVO.setReceiveUserMobile(orderAddressDTO.getMobile());
            orderReturnVO.setReceiveUserAdress(orderAddressDTO.getAddress());
        }

        // 只有退货单状态为待审核的，才有可能有审核按钮
        if (OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode().equals(orderReturnVO.getReturnStatus())) {
            orderReturnVO.setIsAllowCheck(1);
        }
        // 设置供应商信息
        EnterpriseDTO enterpriseDistributor = enterpriseApi.getById(orderReturnDTO.getSellerEid());
        EnterpriseInfoVO enterpriseInfo = PojoUtils.map(enterpriseDistributor, EnterpriseInfoVO.class);
        if (enterpriseDistributor != null) {
            enterpriseInfo.setAddress(enterpriseDistributor.getProvinceName() + enterpriseDistributor.getCityName() + enterpriseDistributor.getRegionName() + enterpriseDistributor.getAddress());
        }
        orderReturnVO.setSellerEnterpriseInfo(enterpriseInfo);

        //查出退款单商品
        List<OrderReturnDetailDTO> orderReturnDetailList = orderReturnDetailApi.getOrderReturnDetailByReturnId(orderReturnVO.getId());
        List<Long> goodsIdList = orderReturnDetailList.stream().map(OrderReturnDetailDTO::getGoodsId).collect(Collectors.toList());
        List<Long> goodsSukIds = orderReturnDetailList.stream().map(OrderReturnDetailDTO::getGoodsSkuId).collect(Collectors.toList());
        List<Long> detailIdList = orderReturnDetailList.stream().map(OrderReturnDetailDTO::getDetailId).collect(Collectors.toList());
        Map<Long, OrderReturnDetailDTO> returnDetailMap = orderReturnDetailList.stream().collect(Collectors.toMap(OrderReturnDetailDTO::getDetailId, e -> e, (k1, k2) -> k1));

        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.listByIdList(detailIdList);
        List<OrderReturnGoodsDetailVO> orderReturnGoodsDetailVOList = PojoUtils.map(orderDetailDTOList, OrderReturnGoodsDetailVO.class);
        for (OrderReturnGoodsDetailVO orderReturnGoodsDetailVO : orderReturnGoodsDetailVOList) {
            OrderReturnDetailDTO orderReturnDetailDTO = returnDetailMap.get(orderReturnGoodsDetailVO.getId());
            // 注意：供应商退货单没有存储批次，也没有发货信息
            if (OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode().equals(orderReturnVO.getReturnType())) {
                OrderReturnGoodsBatchVO orderReturnGoodsBatchVO = PojoUtils.map(orderReturnDetailDTO, OrderReturnGoodsBatchVO.class);
                List<OrderReturnGoodsBatchVO> orderReturnGoodsBatchVOList = new ArrayList<>();
                orderReturnGoodsBatchVOList.add(orderReturnGoodsBatchVO);
                orderReturnGoodsDetailVO.setOrderDeliveryList(orderReturnGoodsBatchVOList);
            } else {
                // 查询出有退货商品的发货信息
                List<OrderDeliveryDTO> orderDeliveryList = orderDeliveryApi.getOrderDeliveryList(orderReturnGoodsDetailVO.getOrderId(), orderReturnGoodsDetailVO.getId());
                List<OrderReturnGoodsBatchVO> orderReturnGoodsBatchVOS = PojoUtils.map(orderDeliveryList, OrderReturnGoodsBatchVO.class);
                // 对于发货信息遍历，查询出对应的批次信息，获得对应明细的退货数量
                for (OrderReturnGoodsBatchVO orderReturnGoodsBatchVO : orderReturnGoodsBatchVOS) {
                    List<OrderReturnDetailBatchDTO> orderReturnDetailBatchDTOS = orderReturnDetailBatchApi.getOrderReturnDetailBatch(orderReturnVO.getId(), orderReturnGoodsBatchVO.getDetailId(), orderReturnGoodsBatchVO.getBatchNo());
                    if (orderReturnDetailBatchDTOS.size() > 0) {
                        orderReturnGoodsBatchVO.setReturnQuantity(orderReturnDetailBatchDTOS.get(0).getReturnQuantity());
                        orderReturnGoodsBatchVO.setOrderReturnDetailId(orderReturnDetailBatchDTOS.get(0).getId());
                    }
                }
                orderReturnGoodsDetailVO.setOrderDeliveryList(orderReturnGoodsBatchVOS);
            }

            OrderDetailChangeDTO orderDetailChangeDTO = orderDetailChangeApi.getByDetailId(orderReturnGoodsDetailVO.getId());
            if (null != orderDetailChangeDTO) {
                orderReturnGoodsDetailVO.setDeliveryQuantity(orderDetailChangeDTO.getDeliveryQuantity());
                orderReturnGoodsDetailVO.setReceiveQuantity(orderDetailChangeDTO.getReceiveQuantity());
            }
            orderReturnGoodsDetailVO.setReturnQuantity(orderReturnDetailDTO.getReturnQuantity());
            BigDecimal discountAmount = orderReturnDetailDTO.getReturnPlatformCouponDiscountAmount()
                    .add(orderReturnDetailDTO.getReturnCouponDiscountAmount())
                    .add(orderReturnDetailDTO.getReturnPresaleDiscountAmount())
                    .add(orderReturnDetailDTO.getReturnPlatformPaymentDiscountAmount())
                    .add(orderReturnDetailDTO.getReturnShopPaymentDiscountAmount());
            orderReturnGoodsDetailVO.setReturnAmount(orderReturnDetailDTO.getReturnAmount().subtract(discountAmount));
            orderReturnGoodsDetailVO.setDiscountAmount(discountAmount);
        }

        //加入包装信息
        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
        Map<Long, GoodsSkuDTO> finalSkuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsIdList);
        orderReturnGoodsDetailVOList.forEach(e -> {
            e.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(e.getGoodsId())));
            e.setPackageNumber(finalSkuGoodsMap.get(e.getGoodsSkuId()) != null ? finalSkuGoodsMap.get(e.getGoodsSkuId()).getPackageNumber() : 1L);
            e.setGoodsRemark(finalSkuGoodsMap.get(e.getGoodsSkuId()) != null ? finalSkuGoodsMap.get(e.getGoodsSkuId()).getRemark() : "");
        });
        orderReturnVO.setOrderDetailVOList(orderReturnGoodsDetailVOList);
        List<OrderLogVO> logList = PojoUtils.map(orderLogApi.getOrderLogInfo(returnOrderId, 2), OrderLogVO.class);
        orderReturnVO.setLogList(logList);

        return orderReturnVO;
    }

    @ApiOperation(value = "采购退货单列表")
    @PostMapping("/purchaseOrderReturnPageList")
    public Result<Page<OrderPurchaseReturnVO>> purchasePageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QuerySellerOrderReturnInfoForm form) {
        OrderReturnPageListRequest request = PojoUtils.map(form, OrderReturnPageListRequest.class);

        request.setBuyerEidList(new ArrayList<Long>() {{
            add(staffInfo.getCurrentEid());
        }});
        request.setOrderTypeEnum(OrderTypeEnum.B2B);
        Page<OrderReturnDTO> page = orderReturnApi.pageList(request);
        Page<OrderPurchaseReturnVO> pageVO = PojoUtils.map(page, OrderPurchaseReturnVO.class);
        List<OrderPurchaseReturnVO> records = pageVO.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return Result.success(pageVO);
        }
        List<Long> orderIdList = records.stream().map(OrderPurchaseReturnVO::getOrderId).collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(orderIdList);

        Map<Long, OrderReturnDTO> orderReturnDTOMap = page.getRecords().stream().collect(Collectors.toMap(OrderReturnDTO::getId, o -> o, (k1, k2) -> k1));

        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
        records.forEach(e -> {
            OrderReturnDTO orderReturnDTO = orderReturnDTOMap.get(e.getId());
            e.setReturnGoodsAmount(orderReturnDTO.getReturnAmount());
            e.setOrderReturnNo(orderReturnDTO.getReturnNo());
            e.setTotalDiscountAmount(orderReturnDTO.getPlatformCouponDiscountAmount().add(orderReturnDTO.getCouponDiscountAmount()).add(orderReturnDTO.getPresaleDiscountAmount())
                    .add(orderReturnDTO.getPlatformPaymentDiscountAmount())
                    .add(orderReturnDTO.getShopPaymentDiscountAmount()));
            e.setReturnAmount(orderReturnDTO.getReturnAmount().subtract(orderReturnDTO.getPlatformCouponDiscountAmount()).subtract(orderReturnDTO.getCouponDiscountAmount()).subtract(orderReturnDTO.getPresaleDiscountAmount())
                    .subtract(orderReturnDTO.getPlatformPaymentDiscountAmount())
                    .subtract(orderReturnDTO.getShopPaymentDiscountAmount()));
            OrderDTO orderDTO = orderMap.get(e.getOrderId());
            e.setPaymentMethod(orderDTO.getPaymentMethod());
            List<OrderReturnDetailDTO> orderReturnDetailList = orderReturnDetailApi.getOrderReturnDetailByReturnId(e.getId());
            Integer returnGoodsNum = orderReturnDetailList.stream().mapToInt(OrderReturnDetailDTO::getReturnQuantity).sum();
            e.setReturnGoodsNum(returnGoodsNum);
            e.setReturnGoods(orderReturnDetailList.size());
        });

        return Result.success(pageVO);
    }

    @ApiOperation(value = "采购退货单申请")
    @PostMapping("/apply")
    public Result<Object> apply(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid B2BOrderReturnApplyForm form) {
        B2BOrderReturnApplyRequest request = PojoUtils.map(form, B2BOrderReturnApplyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setReturnSource(ReturnSourceEnum.B2B_APP.getCode());
        OrderReturnApplyRequest returnApplyRequest = buildRequest(request);
        Boolean result = orderProcessApi.applyOrderReturn(returnApplyRequest);
        return Result.success(result);
    }

    /**
     * 构建申请退货单请求数据
     *
     * @param request 请求数据
     * @return 构造后的请求数据
     */
    private OrderReturnApplyRequest buildRequest(B2BOrderReturnApplyRequest request) {
        OrderReturnApplyRequest returnApplyRequest = PojoUtils.map(request, OrderReturnApplyRequest.class);
        //        returnApplyRequest.setReturnSourceEnum(ReturnSourceEnum.getByCode(request.getReturnSource()));
        //        returnApplyRequest.setOrderType(OrderTypeEnum.B2B.getCode());
        returnApplyRequest.setReturnType(OrderReturnTypeEnum.BUYER_RETURN_ORDER.getCode());
        List<B2BOrderReturnDetailApplyRequest> orderReturnDetailList = request.getOrderReturnDetailList();
        List<OrderDetailRequest> orderDetailRequestList = new ArrayList<>();
        for (B2BOrderReturnDetailApplyRequest b2BOrderReturnDetailApplyRequest : orderReturnDetailList) {
            OrderDetailRequest orderDetailRequest = PojoUtils.map(b2BOrderReturnDetailApplyRequest, OrderDetailRequest.class);
            List<B2BOrderReturnDetailBatchApplyRequest> orderReturnDetailBatchList = b2BOrderReturnDetailApplyRequest.getOrderReturnDetailBatchList();
            List<OrderDeliveryRequest> orderDeliveryRequests = PojoUtils.map(orderReturnDetailBatchList, OrderDeliveryRequest.class);
            orderDetailRequest.setOrderDeliveryList(orderDeliveryRequests);
            orderDetailRequestList.add(orderDetailRequest);
        }
        returnApplyRequest.setOrderDetailList(orderDetailRequestList);
        return returnApplyRequest;
    }
}
