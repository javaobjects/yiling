package com.yiling.b2b.app.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.order.form.B2BOrderReturnApplyForm;
import com.yiling.b2b.app.order.form.B2BOrderReturnDetailForm;
import com.yiling.b2b.app.order.form.B2BOrderReturnPageForm;
import com.yiling.b2b.app.order.vo.B2BOrderReturnDetailBatchVO;
import com.yiling.b2b.app.order.vo.B2BOrderReturnDetailVO;
import com.yiling.b2b.app.order.vo.B2BOrderReturnLogQueryVO;
import com.yiling.b2b.app.order.vo.B2BOrderReturnQueryVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderReturnDetailApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.request.B2BOrderReturnApplyRequest;
import com.yiling.order.order.dto.request.B2BOrderReturnDetailApplyRequest;
import com.yiling.order.order.dto.request.B2BOrderReturnDetailBatchApplyRequest;
import com.yiling.order.order.dto.request.OrderDeliveryRequest;
import com.yiling.order.order.dto.request.OrderDetailRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnPageRequest;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
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
    OrderReturnDetailApi orderReturnDetailApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderProcessApi orderProcessApi;
    @DubboReference
    OrderApi orderApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "退货单申请")
    @PostMapping("/apply")
    public Result<Object> apply(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid B2BOrderReturnApplyForm form) {
        B2BOrderReturnApplyRequest request = PojoUtils.map(form, B2BOrderReturnApplyRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
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

    @ApiOperation(value = "退货列表查询--包含详情")
    @PostMapping("/queryByStatus")
    public Result<Page<B2BOrderReturnQueryVO>> queryByStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody B2BOrderReturnPageForm form) {
        OrderReturnPageRequest request = PojoUtils.map(form, OrderReturnPageRequest.class);
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setOrderReturnType(OrderTypeEnum.B2B.getCode());
        Page<OrderReturnDTO> orderReturnDTOPage = orderReturnApi.pageByCondition(request);
        List<OrderReturnDTO> orderReturnDTOList = orderReturnDTOPage.getRecords();
        Page<B2BOrderReturnQueryVO> b2BOrderReturnVOPage = PojoUtils.map(orderReturnDTOPage, B2BOrderReturnQueryVO.class);
        List<Long> returnIdList = orderReturnDTOList.stream().map(OrderReturnDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(returnIdList)) {
            return Result.success();
        }
        Map<Long, OrderReturnDTO> orderReturnDTOMap = orderReturnDTOList.stream().collect(Collectors.toMap(OrderReturnDTO::getId, o -> o, (k1, k2) -> k1));

        List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnDetailApi.getOrderReturnDetailByReturnIds(returnIdList);
        Map<Long, List<OrderReturnDetailDTO>> orderReturnDetailDTOListMap = orderReturnDetailDTOList.stream().collect(Collectors.groupingBy(OrderReturnDetailDTO::getReturnId));
        List<OrderReturnDetailBatchDTO> orderReturnDetailBatchDTOList = orderReturnDetailBatchApi.getOrderReturnDetailBatchByReturnIds(returnIdList);
        Map<Long, List<OrderReturnDetailBatchDTO>> returnDetailBatchListMap = orderReturnDetailBatchDTOList.stream().collect(Collectors.groupingBy(OrderReturnDetailBatchDTO::getReturnId));
        for (B2BOrderReturnQueryVO b2BOrderReturnQueryVO : b2BOrderReturnVOPage.getRecords()) {
            OrderReturnDTO orderReturnDTO = orderReturnDTOMap.get(b2BOrderReturnQueryVO.getId());
            b2BOrderReturnQueryVO.setReturnGoodsAmount(orderReturnDTO.getReturnAmount());
            b2BOrderReturnQueryVO.setReturnB2BCouponAmount(orderReturnDTO.getPlatformCouponDiscountAmount());
            b2BOrderReturnQueryVO.setReturnSellerCouponAmount(orderReturnDTO.getCouponDiscountAmount());
            b2BOrderReturnQueryVO.setReturnSellerPresaleDiscountAmount(orderReturnDTO.getPresaleDiscountAmount());
            b2BOrderReturnQueryVO.setReturnSellerPlatformPaymentDiscountAmount(orderReturnDTO.getPlatformPaymentDiscountAmount());
            b2BOrderReturnQueryVO.setReturnSellerShopPaymentDiscountAmount(orderReturnDTO.getShopPaymentDiscountAmount());
            b2BOrderReturnQueryVO.setReturnAmount(orderReturnDTO.getReturnAmount()
                    .subtract(orderReturnDTO.getPlatformCouponDiscountAmount())
                    .subtract(orderReturnDTO.getCouponDiscountAmount())
                    .subtract(orderReturnDTO.getPresaleDiscountAmount())
                    .subtract(orderReturnDTO.getShopPaymentDiscountAmount())
                    .subtract(orderReturnDTO.getPlatformPaymentDiscountAmount()));
            // 流转信息
            List<OrderLogDTO> orderLogDTOList = orderLogApi.getOrderLogInfo(b2BOrderReturnQueryVO.getId(), 2);
            List<B2BOrderReturnLogQueryVO> b2bOrderLogList = PojoUtils.map(orderLogDTOList, B2BOrderReturnLogQueryVO.class);
            b2BOrderReturnQueryVO.setOrderReturnLogList(b2bOrderLogList);
            // 退货单明细
            List<B2BOrderReturnDetailVO> b2bOrderReturnDetailList = new ArrayList<>();
            List<OrderReturnDetailDTO> orderReturnDetailDTOS = orderReturnDetailDTOListMap.get(b2BOrderReturnQueryVO.getId());
            b2BOrderReturnQueryVO.setReturnKind(orderReturnDetailDTOS.size());
            int returnQuality = orderReturnDetailDTOS.stream().mapToInt(OrderReturnDetailDTO::getReturnQuantity).sum();
            b2BOrderReturnQueryVO.setReturnQuality(returnQuality);
            List<OrderReturnDetailBatchDTO> orderReturnDetailBatchDTOS = returnDetailBatchListMap.get(b2BOrderReturnQueryVO.getId());
            for (OrderReturnDetailBatchDTO orderReturnDetailBatchDTO : orderReturnDetailBatchDTOS) {
                B2BOrderReturnDetailVO b2BOrderReturnDetailVO = PojoUtils.map(orderReturnDetailBatchDTO, B2BOrderReturnDetailVO.class);
                OrderDetailDTO orderDetailDTO = orderDetailApi.getOrderDetailById(orderReturnDetailBatchDTO.getDetailId());
                b2BOrderReturnDetailVO.setGoodsId(orderDetailDTO.getGoodsId()).setGoodsSkuId(orderDetailDTO.getGoodsSkuId()).setGoodsName(orderDetailDTO.getGoodsName()).setGoodsSpecification(orderDetailDTO.getGoodsSpecification()).setGoodsManufacturer(orderDetailDTO.getGoodsManufacturer()).setReturnAmount(orderDetailDTO.getGoodsPrice().multiply(new BigDecimal(orderReturnDetailBatchDTO.getReturnQuantity()))).setPromotionActivityType(orderDetailDTO.getPromotionActivityType());
                b2bOrderReturnDetailList.add(b2BOrderReturnDetailVO);
            }
            List<Long> goodsIdList = b2bOrderReturnDetailList.stream().map(B2BOrderReturnDetailVO::getGoodsId).collect(Collectors.toList());
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsIdList);
            b2bOrderReturnDetailList.forEach(e -> {
                e.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(e.getGoodsId())));
            });
            b2BOrderReturnQueryVO.setReturnDetailList(b2bOrderReturnDetailList);
        }
        return Result.success(b2BOrderReturnVOPage);
    }

    @ApiOperation(value = "退货详情")
    @PostMapping("/queryDeatil")
    public Result<B2BOrderReturnQueryVO> queryDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody B2BOrderReturnDetailForm form) {
        Long returnId = form.getReturnId();
        OrderReturnDTO orderReturnDTO = orderReturnApi.selectById(returnId);
        List<OrderReturnDetailDTO> returnDetailDTOList = orderReturnDetailApi.getOrderReturnDetailByReturnId(returnId);
        List<OrderReturnDetailBatchDTO> returnDetailBatchDTOList = orderReturnDetailBatchApi.getOrderReturnDetailBatch(returnId, null, null);
        B2BOrderReturnQueryVO b2BOrderReturnQueryVO = PojoUtils.map(orderReturnDTO, B2BOrderReturnQueryVO.class);
        OrderDTO orderInfo = orderApi.getOrderInfo(orderReturnDTO.getOrderId());
        b2BOrderReturnQueryVO.setOrderCategory(orderInfo.getOrderCategory());
        b2BOrderReturnQueryVO.setReturnGoodsAmount(orderReturnDTO.getReturnAmount());
        b2BOrderReturnQueryVO.setReturnB2BCouponAmount(orderReturnDTO.getPlatformCouponDiscountAmount());
        b2BOrderReturnQueryVO.setReturnSellerCouponAmount(orderReturnDTO.getCouponDiscountAmount());
        b2BOrderReturnQueryVO.setReturnSellerPresaleDiscountAmount(orderReturnDTO.getPresaleDiscountAmount());
        b2BOrderReturnQueryVO.setReturnSellerPlatformPaymentDiscountAmount(orderReturnDTO.getPlatformPaymentDiscountAmount());
        b2BOrderReturnQueryVO.setReturnSellerShopPaymentDiscountAmount(orderReturnDTO.getShopPaymentDiscountAmount());
        b2BOrderReturnQueryVO.setReturnAmount(orderReturnDTO.getReturnAmount()
                .subtract(orderReturnDTO.getPlatformCouponDiscountAmount())
                .subtract(orderReturnDTO.getCouponDiscountAmount())
                .subtract(orderReturnDTO.getPresaleDiscountAmount())
                .subtract(orderReturnDTO.getShopPaymentDiscountAmount())
                .subtract(orderReturnDTO.getPlatformPaymentDiscountAmount()));
        // 流转信息
        List<OrderLogDTO> orderLogDTOList = orderLogApi.getOrderLogInfo(b2BOrderReturnQueryVO.getId(), 2);
        List<B2BOrderReturnLogQueryVO> b2bOrderLogList = PojoUtils.map(orderLogDTOList, B2BOrderReturnLogQueryVO.class);
        b2BOrderReturnQueryVO.setOrderReturnLogList(b2bOrderLogList);
        // 退货单明细
        List<B2BOrderReturnDetailVO> b2bOrderReturnDetailList = new ArrayList<>();
        b2BOrderReturnQueryVO.setReturnKind(returnDetailDTOList.size());
        int returnQuality = returnDetailDTOList.stream().mapToInt(OrderReturnDetailDTO::getReturnQuantity).sum();
        b2BOrderReturnQueryVO.setReturnQuality(returnQuality);
        Map<Long, List<OrderReturnDetailBatchDTO>> detailBatchListMap = returnDetailBatchDTOList.stream().collect(Collectors.groupingBy(OrderReturnDetailBatchDTO::getDetailId));

        for (OrderReturnDetailDTO returnDetailDTO : returnDetailDTOList) {
            B2BOrderReturnDetailVO returnDetailVO = PojoUtils.map(returnDetailDTO, B2BOrderReturnDetailVO.class);
            OrderDetailDTO orderDetailDTO = orderDetailApi.getOrderDetailById(returnDetailDTO.getDetailId());
            returnDetailVO.setGoodsId(orderDetailDTO.getGoodsId()).setGoodsSkuId(orderDetailDTO.getGoodsSkuId()).setGoodsName(orderDetailDTO.getGoodsName()).setGoodsSpecification(orderDetailDTO.getGoodsSpecification()).setGoodsManufacturer(orderDetailDTO.getGoodsManufacturer()).setReturnGoodsAmount(returnDetailDTO.getReturnAmount()).setGoodsQuantity(orderDetailDTO.getGoodsQuantity()).setReturnQuantity(returnDetailDTO.getReturnQuantity())
                    .setDiscountAmount(returnDetailDTO.getReturnCouponDiscountAmount()
                            .add(returnDetailDTO.getReturnPlatformCouponDiscountAmount())
                            .add(returnDetailDTO.getReturnShopPaymentDiscountAmount())
                            .add(returnDetailDTO.getReturnPlatformPaymentDiscountAmount())
                            .add(returnDetailDTO.getReturnPresaleDiscountAmount()))
                    .setReturnAmount(returnDetailDTO.getReturnAmount()
                            .subtract(returnDetailDTO.getReturnCouponDiscountAmount())
                            .subtract(returnDetailDTO.getReturnPlatformPaymentDiscountAmount())
                            .subtract(returnDetailDTO.getReturnShopPaymentDiscountAmount())
                            .subtract(returnDetailDTO.getReturnPresaleDiscountAmount())
                            .subtract(returnDetailDTO.getReturnPlatformCouponDiscountAmount()))
                    .setPromotionActivityType(orderDetailDTO.getPromotionActivityType());
            List<OrderReturnDetailBatchDTO> batchDTOList = detailBatchListMap.get(returnDetailDTO.getDetailId());
            List<B2BOrderReturnDetailBatchVO> detailBatchVOS = PojoUtils.map(batchDTOList, B2BOrderReturnDetailBatchVO.class);
            returnDetailVO.setReturnDetailBatchList(detailBatchVOS);
            b2bOrderReturnDetailList.add(returnDetailVO);
        }

        List<Long> goodsIdList = b2bOrderReturnDetailList.stream().map(B2BOrderReturnDetailVO::getGoodsId).collect(Collectors.toList());
        Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsIdList);
        b2bOrderReturnDetailList.forEach(e -> e.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(e.getGoodsId()))));
        b2BOrderReturnQueryVO.setReturnDetailList(b2bOrderReturnDetailList);
        return Result.success(b2BOrderReturnQueryVO);
    }
}
