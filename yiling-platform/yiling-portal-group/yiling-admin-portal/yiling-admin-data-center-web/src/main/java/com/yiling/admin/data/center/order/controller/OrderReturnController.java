package com.yiling.admin.data.center.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.data.center.order.form.QueryOperatorOrderReturnInfoForm;
import com.yiling.admin.data.center.order.vo.EnterpriseInfoVO;
import com.yiling.admin.data.center.order.vo.OrderLogVO;
import com.yiling.admin.data.center.order.vo.OrderReturnGoodsBatchVO;
import com.yiling.admin.data.center.order.vo.OrderReturnGoodsDetailVO;
import com.yiling.admin.data.center.order.vo.OrderReturnVO;
import com.yiling.admin.data.center.order.vo.ReturnOrderNumberVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
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
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.dto.request.ReturnNumberRequest;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 退货单 Controller
 *
 * @author: yong.zhang
 * @date: 2021/10/26
 */
@RestController
@RequestMapping("/refund")
@Api(tags = "退货单模块接口")
@Slf4j
public class OrderReturnController extends BaseController {
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "退货单数量接口")
    @GetMapping("/get/number")
    public Result<ReturnOrderNumberVO> getOrderNumber() {
        ReturnNumberRequest request = new ReturnNumberRequest();
        ReturnOrderNumberDTO returnOrderNumberDTO = orderReturnApi.getOrderNumber(request);
        return Result.success(PojoUtils.map(returnOrderNumberDTO, ReturnOrderNumberVO.class));
    }

    @ApiOperation(value = "运营后台退货单列表")
    @PostMapping("/operatorOrderReturnPageList")
    public Result<Page<OrderReturnVO>> operatorPageList(@RequestBody QueryOperatorOrderReturnInfoForm form) {
        OrderReturnPageListRequest request = PojoUtils.map(form, OrderReturnPageListRequest.class);
        if (null != form.getReturnSource() && 0 != form.getReturnSource()) {
            request.setReturnSourceEnum(ReturnSourceEnum.getByCode(form.getReturnSource()));
        }
        Page<OrderReturnDTO> page = orderReturnApi.pageList(request);
        Page<OrderReturnVO> pageVO = PojoUtils.map(page, OrderReturnVO.class);
        List<OrderReturnVO> returnList = pageVO.getRecords();
        if (CollectionUtil.isEmpty(returnList)) {
            return Result.success(pageVO);
        }
        List<Long> returnIdList = returnList.stream().map(OrderReturnVO::getId).collect(Collectors.toList());
        List<Long> orderIdList = returnList.stream().map(OrderReturnVO::getOrderId).collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(orderIdList);
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
        List<OrderReturnDetailDTO> returnDetailList = orderReturnDetailApi.getOrderReturnDetailByReturnIds(returnIdList);
        Map<Long, List<OrderReturnDetailDTO>> listMap = returnDetailList.stream().collect(Collectors.groupingBy(OrderReturnDetailDTO::getReturnId));
        returnList.forEach(e -> {
            e.setReturnAmount(e.getReturnAmount().subtract(e.getTicketDiscountAmount()).subtract(e.getCashDiscountAmount()).subtract(e.getPlatformCouponDiscountAmount()).subtract(e.getCouponDiscountAmount()).subtract(e.getPresaleDiscountAmount()).subtract(e.getPlatformPaymentDiscountAmount()).subtract(e.getShopPaymentDiscountAmount()));
            OrderDTO orderDTO = orderMap.get(e.getOrderId());
            e.setPaymentMethod(orderDTO.getPaymentMethod());
            e.setPaymentStatus(orderDTO.getPaymentStatus());
            List<OrderReturnDetailDTO> orderReturnDetailList = listMap.get(e.getId());
            Integer returnGoodsNum = orderReturnDetailList.stream().mapToInt(OrderReturnDetailDTO::getReturnQuantity).sum();
            e.setReturnGoodsNum(returnGoodsNum);
            e.setReturnGoods(orderReturnDetailList.size());
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "运营后台退货单详情")
    @GetMapping("/operatorOrderReturnDetail")
    public Result<OrderReturnVO> operatorReturnOrderDetail(@RequestParam(value = "returnOrderId") Long returnOrderId) {
        //根据订单编号查出退货单订单buyerOrderReturnPageList
        OrderReturnDTO orderReturnDTO = orderReturnApi.selectById(returnOrderId);
        if (null == orderReturnDTO) {
            return null;
        }
        OrderReturnVO orderReturnVO = PojoUtils.map(orderReturnDTO, OrderReturnVO.class);
        //根据订单编号查出退货单订单
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderReturnDTO.getOrderNo());
        orderReturnVO.setReturnAmount(orderReturnDTO.getReturnAmount()
                .subtract(orderReturnDTO.getCashDiscountAmount())
                .subtract(orderReturnDTO.getTicketDiscountAmount())
                .subtract(orderReturnDTO.getPlatformCouponDiscountAmount())
                .subtract(orderReturnDTO.getCouponDiscountAmount())
                .subtract(orderReturnDTO.getPresaleDiscountAmount())
                .subtract(orderReturnDTO.getPlatformPaymentDiscountAmount())
                .subtract(orderReturnDTO.getShopPaymentDiscountAmount()));
        orderReturnVO.setDiscountAmount(orderReturnDTO.getCashDiscountAmount()
                .add(orderReturnDTO.getTicketDiscountAmount())
                .add(orderReturnDTO.getPlatformCouponDiscountAmount())
                .add(orderReturnDTO.getCouponDiscountAmount())
                .subtract(orderReturnDTO.getPlatformPaymentDiscountAmount()).subtract(orderReturnDTO.getShopPaymentDiscountAmount())
                .subtract(orderReturnDTO.getPresaleDiscountAmount()));
        orderReturnVO.setOrderId(orderDTO.getId());
        orderReturnVO.setPaymentAmount(PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.PAID ? orderDTO.getPaymentAmount() : BigDecimal.ZERO);
        orderReturnVO.setTotalAmount(orderDTO.getTotalAmount());
        orderReturnVO.setPaymentMethod(orderDTO.getPaymentMethod());
        orderReturnVO.setPaymentStatus(orderDTO.getPaymentStatus());
        // 采购商备注
        orderReturnVO.setOrderRemark(orderDTO.getOrderNote());
        // 驳回原因
        orderReturnVO.setRefuseReason(orderReturnDTO.getFailReason());
        orderReturnVO.setDeliveryCompany(orderDTO.getDeliveryCompany());
        orderReturnVO.setDeliveryNo(orderDTO.getDeliveryNo());

        OrderAddressDTO orderAddressDTO = orderAddressApi.getOrderAddressInfo(orderDTO.getId());
        if (orderAddressDTO != null) {
            orderReturnVO.setReceiveUserName(orderAddressDTO.getName());
            orderReturnVO.setReceiveUserMobile(orderAddressDTO.getMobile());
            orderReturnVO.setReceiveUserAdress(orderAddressDTO.getAddress());
        }
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
                List<OrderReturnGoodsBatchVO> orderReturnGoodsBatchS = PojoUtils.map(orderDeliveryList, OrderReturnGoodsBatchVO.class);
                // 对于发货信息遍历，查询出对应的批次信息，获得对应明细的退货数量
                for (OrderReturnGoodsBatchVO orderReturnGoodsBatchVO : orderReturnGoodsBatchS) {
                    List<OrderReturnDetailBatchDTO> orderReturnDetailBatchS = orderReturnDetailBatchApi.getOrderReturnDetailBatch(orderReturnVO.getId(), orderReturnGoodsBatchVO.getDetailId(), orderReturnGoodsBatchVO.getBatchNo());
                    if (orderReturnDetailBatchS.size() > 0) {
                        orderReturnGoodsBatchVO.setReturnQuantity(orderReturnDetailBatchS.get(0).getReturnQuantity());
                        orderReturnGoodsBatchVO.setOrderReturnDetailId(orderReturnDetailBatchS.get(0).getId());
                    }
                }
                orderReturnGoodsDetailVO.setOrderDeliveryList(orderReturnGoodsBatchS);
            }

            OrderDetailChangeDTO orderDetailChangeDTO = orderDetailChangeApi.getByDetailId(orderReturnGoodsDetailVO.getId());
            if (null != orderDetailChangeDTO) {
                orderReturnGoodsDetailVO.setDeliveryQuantity(orderDetailChangeDTO.getDeliveryQuantity());
                orderReturnGoodsDetailVO.setReceiveQuantity(orderDetailChangeDTO.getReceiveQuantity());
            }

            orderReturnGoodsDetailVO.setReturnQuantity(orderReturnDetailDTO.getReturnQuantity());
            BigDecimal discountAmount = orderReturnDetailDTO.getReturnCashDiscountAmount().add(orderReturnDetailDTO.getReturnTicketDiscountAmount())
                    .add(orderReturnDetailDTO.getReturnPlatformCouponDiscountAmount()).add(orderReturnDetailDTO.getReturnCouponDiscountAmount()
                            .add(orderReturnDetailDTO.getReturnShopPaymentDiscountAmount()).add(orderReturnDetailDTO.getReturnPlatformPaymentDiscountAmount()));
            orderReturnGoodsDetailVO.setTotalReturnAmount(orderReturnDetailDTO.getReturnAmount());
            orderReturnGoodsDetailVO.setReturnAmount(orderReturnDetailDTO.getReturnAmount().subtract(discountAmount));
            orderReturnGoodsDetailVO.setDiscountAmount(discountAmount);
        }
        Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsIdList);
        //加入包装信息
        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
        Map<Long, GoodsSkuDTO> finalSkuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));

        orderReturnGoodsDetailVOList.forEach(e -> {
            e.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(e.getGoodsId())));
            e.setPackageNumber(finalSkuGoodsMap.get(e.getGoodsSkuId()) != null ? finalSkuGoodsMap.get(e.getGoodsSkuId()).getPackageNumber() : 1L);
            e.setGoodsRemark(finalSkuGoodsMap.get(e.getGoodsSkuId()) != null ? finalSkuGoodsMap.get(e.getGoodsSkuId()).getRemark() : "");
        });
        orderReturnVO.setOrderDetailVOList(orderReturnGoodsDetailVOList);
        //供应商信息
        EnterpriseDTO buyEnterprise = enterpriseApi.getById(orderDTO.getBuyerEid());
        EnterpriseDTO sellerEnterprise = enterpriseApi.getById(orderDTO.getSellerEid());
        EnterpriseInfoVO buyEnterpriseInfo = PojoUtils.map(buyEnterprise, EnterpriseInfoVO.class);
        EnterpriseInfoVO sellerEnterpriseInfo = PojoUtils.map(sellerEnterprise, EnterpriseInfoVO.class);
        buyEnterpriseInfo.setAddress(buyEnterprise.getProvinceName() + buyEnterprise.getCityName() + buyEnterprise.getRegionName() + buyEnterprise.getAddress());
        sellerEnterpriseInfo.setAddress(sellerEnterprise.getProvinceName() + sellerEnterprise.getCityName() + sellerEnterprise.getRegionName() + sellerEnterprise.getAddress());
        orderReturnVO.setSellerEnterpriseInfo(sellerEnterpriseInfo);
        orderReturnVO.setBuyerEnterpriseInfo(buyEnterpriseInfo);

        List<OrderLogVO> logList = PojoUtils.map(orderLogApi.getOrderLogInfo(returnOrderId, 2), OrderLogVO.class);
        orderReturnVO.setLogList(logList);

        return Result.success(orderReturnVO);
    }
}
