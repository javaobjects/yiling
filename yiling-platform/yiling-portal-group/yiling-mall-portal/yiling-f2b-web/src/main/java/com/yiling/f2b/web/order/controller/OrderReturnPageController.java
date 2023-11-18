package com.yiling.f2b.web.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.web.order.form.OrderReturnForm;
import com.yiling.f2b.web.order.form.QueryBuyerOrderReturnInfoForm;
import com.yiling.f2b.web.order.vo.EnterpriseInfoVO;
import com.yiling.f2b.web.order.vo.OrderLogVO;
import com.yiling.f2b.web.order.vo.OrderReturnGoodsBatchVO;
import com.yiling.f2b.web.order.vo.OrderReturnGoodsDetailVO;
import com.yiling.f2b.web.order.vo.OrderReturnVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
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
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单页面 退货单 Controller
 *
 * @author: wei.wang
 * @date: 2023/01/05
 */
@RestController
@RequestMapping("/order/page/return")
@Api(tags = "订单中心-订单退货接口")
@Slf4j
public class OrderReturnPageController extends BaseController {

    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderProcessApi orderProcessApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;


    @ApiOperation(value = "采购退货单列表")
    @PostMapping("/list")
    public Result<Page<OrderReturnVO>> buyerPageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryBuyerOrderReturnInfoForm form) {
        OrderReturnPageListRequest request = PojoUtils.map(form, OrderReturnPageListRequest.class);
        request.setBuyerEidList(new ArrayList<Long>() {{
            add(staffInfo.getCurrentEid());
        }});
        Page<OrderReturnDTO> page = orderReturnApi.pageList(request);
        Page<OrderReturnVO> pageVO = PojoUtils.map(page, OrderReturnVO.class);
        List<OrderReturnVO> orderReturnList = pageVO.getRecords();
        if (CollectionUtil.isEmpty(orderReturnList)) {
            return Result.success(pageVO);
        }
        List<Long> orderIdList = orderReturnList.stream().map(OrderReturnVO::getOrderId).collect(Collectors.toList());
        Map<Long, OrderDTO> orderMap = orderApi.listByIds(orderIdList).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
        orderReturnList.forEach(e -> {
            e.setReturnAmount(e.getReturnAmount().subtract(e.getTicketDiscountAmount()).subtract(e.getCashDiscountAmount()).subtract(e.getPlatformCouponDiscountAmount()).subtract(e.getCouponDiscountAmount()));
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

    @ApiOperation(value = "退货单详情")
    @GetMapping("/detail")
    public Result<OrderReturnVO> purchaseReturnOrderDetail(Long returnOrderId) {
        OrderReturnVO orderReturnVo = getOrderReturnVo(returnOrderId, 1);
        return Result.success(orderReturnVo);
    }

    @ApiOperation(value = "退货单申请:采购商主动退货  2.退回退货单,3.采购退货单")
    @PostMapping("/apply")
    public Result<Object> purchaseApplyReturnOrder(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OrderReturnForm orderReturnForm) {
        OrderReturnApplyRequest orderReturnApplyRequest = PojoUtils.map(orderReturnForm, OrderReturnApplyRequest.class);
        orderReturnApplyRequest.setOpUserId(staffInfo.getCurrentUserId());
        Boolean res = false;
        if (OrderReturnTypeEnum.BUYER_RETURN_ORDER.getCode().equals(orderReturnApplyRequest.getReturnType())) {
            log.info("【退货单申请】采购商主动申请退货,请求参数为:[{}]", orderReturnApplyRequest);
            res = orderProcessApi.purchaseApplyReturnOrder(orderReturnApplyRequest);
        } else if (OrderReturnTypeEnum.DAMAGE_RETURN_ORDER.getCode().equals(orderReturnApplyRequest.getReturnType())) {
            log.info("【退货单申请】采购商申请破损退货,请求参数为:[{}]", orderReturnApplyRequest);
            res = orderProcessApi.damageOrderReturn(orderReturnApplyRequest, 1, null);
        }
        if (res) {
            return Result.success(new BoolObject(true));
        } else {
            return Result.failed("申请失败！");
        }
    }


    /**
     * 查询退货单详情
     *
     * @param returnOrderId 退货单id
     * @param type 0/1 销售退订单详情/采购退货单详情
     * @return 退货单详情
     */
    private OrderReturnVO getOrderReturnVo(Long returnOrderId, int type) {
        //根据订单编号查出退货单订单
        OrderReturnDTO orderReturnDTO = orderReturnApi.selectById(returnOrderId);
        if (null == orderReturnDTO) {
            return null;
        }
        OrderReturnVO orderReturnVO = PojoUtils.map(orderReturnDTO, OrderReturnVO.class);
        //根据订单编号查出订单
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderReturnDTO.getOrderNo());
        orderReturnVO.setReturnAmount(orderReturnDTO.getReturnAmount().subtract(orderReturnDTO.getCashDiscountAmount()).subtract(orderReturnDTO.getTicketDiscountAmount()));
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
        if (OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode().equals(orderReturnVO.getReturnStatus()) && 0 == type) {
            // 如果退货单对应订单状态为已开票或者不开票，才可以审核------------以岭的和工业直属才有开票
            orderReturnVO.setIsAllowCheck(isAllowCheck(orderDTO));
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
                List<OrderReturnGoodsBatchVO> orderReturnGoodsBatchVOS = PojoUtils.map(orderDeliveryList, OrderReturnGoodsBatchVO.class);
                // 对于发货信息遍历，查询出对应的批次信息，获得对应明细的退货数量
                for (OrderReturnGoodsBatchVO orderReturnGoodsBatchVO : orderReturnGoodsBatchVOS) {
                    List<OrderReturnDetailBatchDTO> orderReturnDetailBatchDTOS = orderReturnDetailBatchApi.getOrderReturnDetailBatch(orderReturnVO.getId(), orderReturnGoodsBatchVO.getDetailId(), orderReturnGoodsBatchVO.getBatchNo());
                    if (orderReturnDetailBatchDTOS.size() > 0) {
                        orderReturnGoodsBatchVO.setReturnQuantity(orderReturnDetailBatchDTOS.get(0).getReturnQuantity());
                        orderReturnGoodsBatchVO.setOrderReturnDetailId(orderReturnDetailBatchDTOS.get(0).getId());
                        orderReturnGoodsBatchVO.setAllowReturnQuantity(orderReturnGoodsBatchVO.getReceiveQuantity());
                        if (OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode().equals(orderReturnDTO.getReturnStatus())) {
                            orderReturnGoodsBatchVO.setAllowReturnQuantity(orderReturnGoodsBatchVO.getReceiveQuantity() + orderReturnGoodsBatchVO.getReturnQuantity());
                        }
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
            BigDecimal discountAmount = orderReturnDetailDTO.getReturnCashDiscountAmount()
                    .add(orderReturnDetailDTO.getReturnTicketDiscountAmount());
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
        if (1 == type) {
            //供应商信息
            EnterpriseDTO enterprise = enterpriseApi.getById(orderDTO.getSellerEid());
            EnterpriseInfoVO enterpriseInfo = PojoUtils.map(enterprise, EnterpriseInfoVO.class);
            enterpriseInfo.setAddress(enterprise.getProvinceName() + enterprise.getCityName() + enterprise.getRegionName() + enterprise.getAddress());
            orderReturnVO.setSellerEnterpriseInfo(enterpriseInfo);
        }
        List<OrderLogVO> logList = PojoUtils.map(orderLogApi.getOrderLogInfo(returnOrderId, 2), OrderLogVO.class);
        orderReturnVO.setLogList(logList);
        return orderReturnVO;
    }

    /**
     * 以岭和工业直属的只有已开票和不需要开票的才运行审核
     *
     * @param orderDTO 订单信息
     * @return 1/0 允许审核/不允许审核
     */
    private int isAllowCheck(OrderDTO orderDTO) {
        if (OrderInvoiceApplyStatusEnum.INVOICED.getCode().equals(orderDTO.getInvoiceStatus()) || OrderInvoiceApplyStatusEnum.NOT_NEED.getCode().equals(orderDTO.getInvoiceStatus())) {
            return 1;
        }
        // 如果是非以岭的同时也是非工业直属的,都可以审核
        boolean isYiLing = enterpriseApi.isYilingSubEid(orderDTO.getSellerEid());
        List<Long> longList = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
        boolean contains = longList.contains(orderDTO.getSellerEid());
        if (!isYiLing && !contains) {
            return 1;
        }
        return 0;
    }
}
