package com.yiling.b2b.app.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.order.form.OrderB2BAppPayAmountForm;
import com.yiling.b2b.app.order.form.OrderB2BAppPayAmountListForm;
import com.yiling.b2b.app.order.form.OrderB2BPaymentFrom;
import com.yiling.b2b.app.order.vo.OrderGoodsVO;
import com.yiling.b2b.app.order.vo.OrderPaymentListVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderB2BPaymentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.OrderB2BPaymentRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * B2B移动端订单账期
 *
 * @author:wei.wang
 * @date:2021/10/22
 */
@Slf4j
@RestController
@RequestMapping("/order/payment")
@Api(tags = "待还款")
public class OrderPaymentController extends BaseController {

    @DubboReference
    OrderB2BApi          orderB2BApi;
    @DubboReference
    OrderApi             orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    GoodsApi             goodsApi;
    @DubboReference
    PayApi               payApi;
    @DubboReference
    OrderDetailApi orderDetailApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "待还款列表")
    @PostMapping("/list")
    public Result<Page<OrderPaymentListVO>> listOrderPayment(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody OrderB2BPaymentFrom form) {
        OrderB2BPaymentRequest request = PojoUtils.map(form, OrderB2BPaymentRequest.class);
        request.setBuyerEid(staffInfo.getCurrentEid());
        List<Integer> list = new ArrayList<>();
        if(form.getStatus() == 2){
            list.add(3);
            list.add(4);
            request.setStatusList(list);
        }else{
            list.add(1);
            request.setStatusList(list);
        }
        Page<OrderB2BPaymentDTO> page = orderB2BApi.getOrderB2BPaymentList(request);
        Page<OrderPaymentListVO> result = PojoUtils.map(page, OrderPaymentListVO.class);
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
            List<OrderDetailChangeDTO> detailChangeList = orderDetailChangeApi.listByOrderIds(ids);
            Map<Long, List<OrderDetailChangeDTO>> detailChangeMap = new HashMap<>();

            List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailByOrderIds(ids);
            Map<Long, OrderDetailDTO> detailMap = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));

            for (OrderDetailChangeDTO changeOne : detailChangeList) {
                if (detailChangeMap.containsKey(changeOne.getOrderId())) {
                    List<OrderDetailChangeDTO> changeList = detailChangeMap.get(changeOne.getOrderId());
                    changeList.add(changeOne);
                } else {
                    detailChangeMap.put(changeOne.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {{
                        add(changeOne);
                    }});
                }
            }
            List<Long> goodIds = new ArrayList<>();
            for (OrderPaymentListVO one : result.getRecords()) {
                List<OrderDetailChangeDTO> orderDetailChangeList = detailChangeMap.get(one.getId());
                //数量
                Integer goodsNumber = 0;
                List<OrderGoodsVO> goodsList = new ArrayList<>();
                for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
                    OrderDetailDTO detailDTO = detailMap.get(changeOne.getDetailId());
                    goodsNumber = goodsNumber + changeOne.getGoodsQuantity();
                    OrderGoodsVO goodOne = new OrderGoodsVO();
                    goodOne.setGoodsSkuId(changeOne.getGoodsSkuId());
                    goodOne.setGoodsQuantity(changeOne.getGoodsQuantity());
                    goodOne.setGoodsId(changeOne.getGoodsId());
                    goodOne.setPromotionActivityType(detailDTO.getPromotionActivityType());
                    goodsList.add(goodOne);
                    goodIds.add(changeOne.getGoodsId());
                }
                one.setGoodsNumber(goodsNumber);
                one.setGoodsType(orderDetailChangeList.size());
                one.setGoodsList(goodsList);
            }
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodIds);
            for (OrderPaymentListVO one : result.getRecords()) {
                for (OrderGoodsVO goods : one.getGoodsList()) {
                    goods.setGoodPic(pictureUrlUtils.getGoodsPicUrl(map.get(goods.getGoodsId())));
                }
            }
        }

        return Result.success(result);
    }

    @ApiOperation(value = "去还款")
    @PostMapping("/repay")
    public Result<String> repaymentBatch(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody OrderB2BAppPayAmountListForm form) {
        StringBuilder builder = new StringBuilder();
        builder.append("共");
        builder.append(form.getList().size() + "笔订单 ");
        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = new ArrayList<>(form.getList().size());
        List<Long> ids = form.getList().stream().map(order -> order.getOrderId()).collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(ids);
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));
        for (OrderB2BAppPayAmountForm appPayAmountForm : form.getList()) {
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(staffInfo.getCurrentUserId());
            request.setAppOrderId(appPayAmountForm.getOrderId());
            OrderDTO order = orderMap.get(appPayAmountForm.getOrderId());
            if(order == null){
                throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
            }
            request.setAppOrderNo(order.getOrderNo());
            request.setAmount(order.getPaymentAmount());
            request.setBuyerEid(staffInfo.getCurrentEid());
            request.setSellerEid(appPayAmountForm.getSellerEid());
            appOrderList.add(request);
        }
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setTradeType(TradeTypeEnum.BACK);
        payOrderRequest.setContent(builder.toString());
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(staffInfo.getCurrentUserId());
        Result<String> createResult = payApi.createPayOrder(payOrderRequest);
        return createResult;
    }

}
