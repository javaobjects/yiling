package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportB2bSettlementOrderBO;
import com.yiling.export.export.bo.ExportB2bSettlementOrderDetailBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.dto.B2BSettlementDetailDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.api.SettlementOrderApi;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.SettlementOrderDTO;
import com.yiling.settlement.b2b.dto.request.ExportSettlementOrderPageListRequest;
import com.yiling.settlement.b2b.dto.request.QuerySettlementOrderPageListRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Service("b2bSettlementOrderExportServiceImpl")
@Slf4j
public class B2bSettlementOrderExportServiceImpl implements BaseExportQueryDataService<ExportSettlementOrderPageListRequest> {

    @DubboReference
    SettlementApi settlementApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderB2BApi orderB2BApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    SettlementOrderApi settlementOrderApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    PresaleActivityApi presaleActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;


    private static final LinkedHashMap<String, String> FIELD_SHEET1 = new LinkedHashMap<String, String>() {

        {
            put("code", "结算单号");
            put("orderNo", "订单号");
            put("buyerName", "采购商名称");
            put("customerErpCode", "客户ERP内码");
            put("sellerName", "供应商");
            put("orderCreateTime", "订单创建时间");
            put("paymentTime", "订单支付时间");
            put("totalGoodsAmount", "货款金额");
            put("refundGoodsAmount", "货款退款金额");
            put("goodsCreateTime", "货款结算单创建时间");
            put("goodsSettlementTime", "货款结算单结算时间");
            put("couponAmount", "平台承担券金额");
            put("refundCouponAmount", "平台承担券退款金额");
            put("promotionAmount", "平台承担秒杀/特价金额");
            put("refundPromotionAmount", "平台承担秒杀/特价退款金额");
            put("giftAmount", "平台承担满赠金额");
            put("refundGiftAmount", "平台承担满赠退款金额");
            put("comPacAmount", "平台承担套装金额");
            put("refundComPacAmount", "平台承担套装退款金额");
            put("presaleDiscountAmount", "平台承担预售优惠金额");
            put("refundPreAmount", "平台承担预售优惠退款金额");
            put("payDiscountAmount", "平台承担支付促销优惠金额");
            put("refundPayAmount", "平台承担支付促销退款金额");
            put("saleCreateTime", "促销结算单创建时间");
            put("saleSettlementTime", "促销结算单结算时间");
            put("presaleDefaultAmount", "预售违约金额");
            put("pdCreateTime", "预售违约金结算单创建时间");
            put("pdSettlementTime", "预售违约金结算单结算时间");
            put("totalAmount", "结算总金额");
        }
    };
    private static final LinkedHashMap<String, String> FIELD_SHEET2 = new LinkedHashMap<String, String>() {

        {
            put("code", "结算单号");
            put("orderNo", "订单号");
            put("buyerName", "采购商名称");
            put("customerErpCode", "客户ERP内码");
            put("paymentTime", "订单支付时间");
            put("goodsId", "商品ID");
            put("inSn", "ERP商品内码");
            put("goodsErpCode", "ERP商品编码");
            put("goodsName", "商品名称");
            put("goodsSpecification", "规格");
            put("goodsManufacturer", "生产厂家");
            //			put("canSplit", "是否拆零销售");
            //			put("middlePackage", "中包装数量");
            put("goodsQuantity", "购买数量");
            put("deliveryQuantity", "发货数量");
            put("sellerReturnQuantity", "卖家退货数量");
            put("receiveQuantity", "收货数量");
            put("returnQuantity", "买家退货数量");
            put("goodsPrice", "商品单价");
            put("goodsAmount", "货款金额");
            put("refundGoodsAmount", "货款退款金额");
            put("goodsSettleAmount", "货款结算金额");
            put("goodsCreateTime", "货款结算单创建时间");
            put("goodsSettlementTime", "货款结算单结算时间");
            put("couponAmount", "平台承担券金额");
            put("refundCouponAmount", "平台承担券退款金额");
            put("settleCouponAmount", "平台承担券结算金额");
            put("promotionAmount", "平台承担秒杀特价金额");
            put("refundPromotionAmount", "平台承担秒杀特价退款金额");
            put("settPromotionAmount", "平台承担秒杀特价结算金额");
            put("comPacAmount", "平台承担套装金额");
            put("refundComPacAmount", "平台承担套装退款金额");
            put("settComPacAmount", "平台承担套装结算金额");
            put("presaleDiscountAmount", "平台承担预售优惠金额");
            put("returnPresaleDiscountAmount", "平台承担预售优惠退款金额");
            put("settPreSaleAmount", "平台承担预售优惠结算金额");
            put("paymentPlatformDiscountAmount", "平台承担支付促销金额");
            put("refundPayAmount", "平台承担支付促销退款金额");
            put("settPaySaleAmount", "平台承担支付促销结算金额");
            put("saleCreateTime", "促销结算单创建时间");
            put("saleSettlementTime", "促销结算单结算时间");
            put("presaleDefaultAmount", "预售违约金额");
            put("pdSettCreateTime", "预售违约结算单创建时间");
            put("pdSettlementTime", " 预售违约结算单结算时间");
            put("amount", "结算总金额");

        }
    };

    @Override
    public QueryExportDataDTO queryData(ExportSettlementOrderPageListRequest request) {

        QuerySettlementOrderPageListRequest mainQueryRequest = PojoUtils.map(request, QuerySettlementOrderPageListRequest.class);

        //设置结算单导出范围--以岭查全部数据
        if (ObjectUtil.isNull(request.getEid())) {
            mainQueryRequest.setSellerEid(null);
        } else {
            mainQueryRequest.setSellerEid(request.getEid());
        }

        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //不同sheet数据
        List<Map<String, Object>> settlementData = new ArrayList<>();
        List<Map<String, Object>> settlementOrderData = new ArrayList<>();


        int mainCurrent = 1;
        Page<SettlementOrderDTO> mainPage;
        //分页查询结算单列表
        do {

            mainQueryRequest.setCurrent(mainCurrent);
            mainQueryRequest.setSize(50);
            //查询结算单
            mainPage = settlementOrderApi.querySettlementOrderPageList(mainQueryRequest);

            List<SettlementOrderDTO> mainRecords = mainPage.getRecords();

            if (CollUtil.isEmpty(mainRecords)) {
                break;
            }
            //查询结算单
            List<Long> settIdList = ListUtil.toList();
            settIdList.addAll(mainRecords.stream().map(SettlementOrderDTO::getGoodsSettlementId).collect(Collectors.toList()));
            settIdList.addAll(mainRecords.stream().map(SettlementOrderDTO::getSaleSettlementId).collect(Collectors.toList()));
            settIdList.addAll(mainRecords.stream().map(SettlementOrderDTO::getPdSettlementId).collect(Collectors.toList()));
            settIdList = settIdList.stream().filter(e -> ObjectUtil.notEqual(e, 0L)).distinct().collect(Collectors.toList());
            Map<Long, SettlementDTO> settlementDTOMap = settlementApi.getByIdList(settIdList).stream().collect(Collectors.toMap(SettlementDTO::getId, e -> e));

            //查询订单列表
            //根据结算单查询订单信息及供应商信息
            List<Long> orderIdList = mainRecords.stream().map(SettlementOrderDTO::getOrderId).distinct().collect(Collectors.toList());
            List<OrderDTO> orderDTOList = orderApi.listByIds(orderIdList);
            List<Long> eidIdList = orderDTOList.stream().map(OrderDTO::getBuyerEid).collect(Collectors.toList());
            eidIdList.addAll(orderDTOList.stream().map(OrderDTO::getSellerEid).collect(Collectors.toList()));
            eidIdList = eidIdList.stream().distinct().collect(Collectors.toList());

            //订单map
            Map<Long, OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
            //企业map
            Map<Long, String> entDTOMap = enterpriseApi.listByIds(eidIdList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));

            //生成sheet1
            mainRecords.forEach(settlementOrderDTO -> {
                ExportB2bSettlementOrderBO var1 = PojoUtils.map(settlementOrderDTO, ExportB2bSettlementOrderBO.class);

                var1.setBuyerName(entDTOMap.getOrDefault(settlementOrderDTO.getBuyerEid(), ""));
                var1.setSellerName(entDTOMap.getOrDefault(settlementOrderDTO.getSellerEid(), ""));
                OrderDTO orderDTO = orderDTOMap.get(settlementOrderDTO.getOrderId());
                var1.setCustomerErpCode(orderDTO.getCustomerErpCode());
                var1.setOrderCreateTime(orderDTO.getCreateTime());
                var1.setPaymentTime(DateUtil.compare(orderDTO.getPaymentTime(),DateUtil.parse("1970-01-01 00:00:00"))==0?null:orderDTO.getPaymentTime());
                if (ObjectUtil.notEqual(settlementOrderDTO.getGoodsSettlementId(), 0L)) {
                    var1.setCode(settlementOrderDTO.getGoodsSettlementNo());
                    SettlementDTO settlementDTO = settlementDTOMap.get(settlementOrderDTO.getGoodsSettlementId());
                    var1.setGoodsCreateTime(settlementDTO.getCreateTime());
                    if (DateUtil.compare(settlementDTO.getSettlementTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                        var1.setGoodsSettlementTime(settlementDTO.getSettlementTime());
                    }
                }
                if (ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementId(), 0L)) {
                    SettlementDTO settlementDTO = settlementDTOMap.get(settlementOrderDTO.getSaleSettlementId());
                    var1.setSaleCreateTime(settlementDTO.getCreateTime());
                    if (DateUtil.compare(settlementDTO.getSettlementTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                        var1.setSaleSettlementTime(settlementDTO.getSettlementTime());
                    }
                    String code = var1.getCode();
                    if (StrUtil.isNotBlank(code)) {
                        code = code + "," + settlementOrderDTO.getSaleSettlementNo();
                    } else {
                        code = settlementOrderDTO.getSaleSettlementNo();
                    }
                    var1.setCode(code);
                }
                if (ObjectUtil.notEqual(settlementOrderDTO.getPdSettlementId(), 0L)) {
                    SettlementDTO settlementDTO = settlementDTOMap.get(settlementOrderDTO.getPdSettlementId());
                    var1.setPdCreateTime(settlementDTO.getCreateTime());
                    if (DateUtil.compare(settlementDTO.getSettlementTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                        var1.setPdSettlementTime(settlementDTO.getSettlementTime());
                    }
                    String code = var1.getCode();
                    if (StrUtil.isNotBlank(code)) {
                        code = code + "," + settlementOrderDTO.getPdSettlementNo();
                    } else {
                        code = settlementOrderDTO.getPdSettlementNo();
                    }
                    var1.setCode(code);
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(var1);
                settlementData.add(dataPojo);
            });

            //生成sheet2
            mainRecords.forEach(settlementOrderDTO -> {
                //查询订单明细
                List<B2BSettlementDetailDTO> orderDetailList = orderB2BApi.listSettleOrderDetailByOrderId(settlementOrderDTO.getOrderId());
                //查询order__change
                Map<Long, List<OrderDetailChangeDTO>> orderChangeListMap = orderDetailChangeApi.listByOrderIds(orderIdList).stream().collect(Collectors.groupingBy(OrderDetailChangeDTO::getOrderId));

                //查询商品信息
                List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIdsAndStatus(orderDetailList.stream().map(B2BSettlementDetailDTO::getGoodsId).distinct().collect(Collectors.toList()),null);
                Map<Long, List<GoodsSkuDTO>> goodsInfoMap = goodsSkuDTOList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));

                //查询活动百分比
                List<Long> promotionActivityIds = orderDetailList.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.FULL_GIFT.getCode())||ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode())||ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode())||ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())).map(B2BSettlementDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());

                Map<Long, PromotionActivityDTO> activityDTOMap = MapUtil.newHashMap();
                if (CollUtil.isNotEmpty(promotionActivityIds)) {
                    activityDTOMap = promotionActivityApi.batchQueryByIdList(promotionActivityIds).stream().collect(Collectors.toMap(PromotionActivityDTO::getId, e -> e));
                }
                List<Long> preSaleActivityIds = orderDetailList.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.PRESALE.getCode())).map(B2BSettlementDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());
                Map<Long, PresaleActivityDTO> preSaleActivityDTOMap = MapUtil.newHashMap();
                if (CollUtil.isNotEmpty(preSaleActivityIds)){
                    preSaleActivityDTOMap = presaleActivityApi.batchQueryByIdList(preSaleActivityIds);
                }
                //查询订单的活动信息
                Map<Long, List<OrderPromotionActivityDTO>> orderActivityInfoMap= MapUtil.newHashMap();
                List<Long> orderIds = orderDetailList.stream().map(B2BSettlementDetailDTO::getOrderId).distinct().collect(Collectors.toList());
                if (CollUtil.isNotEmpty(orderIds)) {
                    orderActivityInfoMap = orderPromotionActivityApi.listByOrderIds(orderIds).stream().map(e->{e.setActivityPlatformPercent(e.getActivityPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN)); return e;}).collect(Collectors.groupingBy(OrderPromotionActivityDTO::getOrderId));
                }


                Map<Long, PromotionActivityDTO> finalActivityDTOMap = activityDTOMap;
                Map<Long, List<OrderPromotionActivityDTO>> finalOrderActivityInfoMap = orderActivityInfoMap;
                Map<Long, PresaleActivityDTO> finalPreSaleActivityDTOMap = preSaleActivityDTOMap;

                orderDetailList.forEach(orderDetail -> {

                    //查询订单下的订单明细
                    Map<Long, List<OrderDetailChangeDTO>> orderDetailMap = orderChangeListMap.get(orderDetail.getOrderId()).stream().collect(Collectors.groupingBy(OrderDetailChangeDTO::getDetailId));
                    OrderDetailChangeDTO detailChangeDTO = orderDetailMap.get(orderDetail.getDetailId()).get(0);
                    ExportB2bSettlementOrderDetailBO var2 = new ExportB2bSettlementOrderDetailBO();
                    var2.setOrderNo(orderDetail.getOrderNo());
                    var2.setBuyerName(entDTOMap.getOrDefault(settlementOrderDTO.getBuyerEid(), ""));
                    var2.setCustomerErpCode(orderDetail.getCustomerErpCode());
                    OrderDTO orderDTO = orderDTOMap.get(settlementOrderDTO.getOrderId());
                    var2.setCustomerErpCode(orderDTO.getCustomerErpCode());
                    var2.setPaymentTime(DateUtil.compare(orderDTO.getPaymentTime(),DateUtil.parse("1970-01-01 00:00:00"))==0?null:orderDTO.getPaymentTime());
                    //设置商品相关
                    var2.setGoodsId(orderDetail.getGoodsId());
                    var2.setGoodsName(orderDetail.getGoodsName());
                    var2.setGoodsManufacturer(orderDetail.getGoodsManufacturer());
                    var2.setGoodsSpecification(orderDetail.getGoodsSpecification());
                    var2.setGoodsPrice(orderDetail.getGoodsPrice());
                    var2.setGoodsQuantity(orderDetail.getGoodsQuantity());
                    var2.setDeliveryQuantity(orderDetail.getDeliveryQuantity());
                    var2.setReceiveQuantity(orderDetail.getReceiveQuantity());
                    var2.setGoodsErpCode(orderDetail.getGoodsErpCode());
                    //设置卖家退货数量&&买家退货数量
                    var2.setSellerReturnQuantity(detailChangeDTO.getSellerReturnQuantity());
                    var2.setReturnQuantity(detailChangeDTO.getReturnQuantity());


                    BigDecimal goodsSettAmount = BigDecimal.ZERO;
                    if (ObjectUtil.notEqual(settlementOrderDTO.getGoodsSettlementId(), 0L) && ObjectUtil.notEqual(settlementOrderDTO.getGoodsSettlementNo(), "")) {
                        var2.setCode(settlementOrderDTO.getGoodsSettlementNo());
                        //计算货款金额：货款金额=购买商品小计-平台优惠劵折扣金额-商家优惠劵折扣金额-预售促销金额-支付促销金额
                        BigDecimal goodsAmount = detailChangeDTO.getGoodsAmount()
                                .subtract(detailChangeDTO.getPlatformCouponDiscountAmount())
                                .subtract(detailChangeDTO.getCouponDiscountAmount())
                                .subtract(detailChangeDTO.getPresaleDiscountAmount())
                                .subtract(detailChangeDTO.getPlatformPaymentDiscountAmount().add(detailChangeDTO.getShopPaymentDiscountAmount()))
                                .setScale(2, BigDecimal.ROUND_HALF_UP);
                        var2.setGoodsAmount(goodsAmount);
                        //总退款金额
                        BigDecimal totalReturn = detailChangeDTO.getReturnAmount().add(detailChangeDTO.getSellerReturnAmount());
                        //卖家优惠券退款金额
                        BigDecimal sellerCouponReturnAmount = detailChangeDTO.getSellerPlatformCouponDiscountAmount().add(detailChangeDTO.getSellerCouponDiscountAmount());
                        //买家优惠券退款金额
                        BigDecimal buyerReturnAmount = detailChangeDTO.getReturnPlatformCouponDiscountAmount().add(detailChangeDTO.getReturnCouponDiscountAmount());
                        //预售促销退款金额
                        BigDecimal preSaleReturnAmount = detailChangeDTO.getReturnPresaleDiscountAmount().add(detailChangeDTO.getSellerPresaleDiscountAmount());
                        //支付促销退款金额
                        BigDecimal paySaleReturnAmount = detailChangeDTO.getReturnPlatformPaymentDiscountAmount().add(detailChangeDTO.getReturnShopPaymentDiscountAmount());

                        //计算货款退款金额:货款退款金额=总退款金额-卖家优惠券退款金额-买家优惠券退款金额-预售促销金额-支付促销退款金额
                        BigDecimal returnGoodsAmount = totalReturn
                                .subtract(sellerCouponReturnAmount)
                                .subtract(buyerReturnAmount)
                                .subtract(preSaleReturnAmount)
                                .subtract(paySaleReturnAmount)
                                .setScale(2, BigDecimal.ROUND_HALF_UP);
                        var2.setRefundGoodsAmount(returnGoodsAmount);
                        //货款应结算金额=货款金额-货款退款金额
                        goodsSettAmount = goodsAmount.subtract(returnGoodsAmount);
                        var2.setGoodsSettleAmount(goodsSettAmount);
                        //设置结算单时间
                        if (ObjectUtil.notEqual(settlementOrderDTO.getGoodsSettlementId(), 0L)) {
                            SettlementDTO settlementDTO = settlementDTOMap.get(settlementOrderDTO.getGoodsSettlementId());
                            var2.setGoodsCreateTime(settlementDTO.getCreateTime());
                            if (DateUtil.compare(settlementDTO.getSettlementTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                                var2.setGoodsSettlementTime(settlementDTO.getSettlementTime());
                            }
                        }
                    }

                    //应结算平台优惠券金额
                    BigDecimal settCouponAmount = BigDecimal.ZERO;
                    //应结算平台秒杀&特价金额
                    BigDecimal settPromotionAmount = BigDecimal.ZERO;
                    //应结组合促销金额
                    BigDecimal settComPacAmount = BigDecimal.ZERO;
                    if (ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementId(), 0L) && ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementNo(), "")) {
                        String code = var2.getCode();
                        if (StrUtil.isNotBlank(code)) {
                            code = code + "," + settlementOrderDTO.getSaleSettlementNo();
                        } else {
                            code = settlementOrderDTO.getSaleSettlementNo();
                        }
                        var2.setCode(code);
                        //计算平台补贴金额
                        //计算该条订单明细的平台券+商家券的平台补贴金额
                        //平台券补贴金额
                        BigDecimal subPlatformAmount = orderDetail.getPlatformCouponDiscountAmount().multiply(orderDetail.getPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                        //商家券补贴金额
                        BigDecimal subBusinessAmount = orderDetail.getCouponDiscountAmount().multiply(orderDetail.getShopPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getShopPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                        //平台优惠券补贴金额=平台券补贴金额+商家券补贴金额
                        BigDecimal totalPlatformAmount = subPlatformAmount.add(subBusinessAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                        var2.setCouponAmount(totalPlatformAmount);

                        //计算退款的平台补贴金额
                        //商家券的退款金额
                        BigDecimal subReturnPlatformAmount = orderDetail.getReturnCouponDiscountAmount().multiply(orderDetail.getShopPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getShopPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                        //平台券的退款金额
                        BigDecimal subReturnBusinessAmount = orderDetail.getReturnPlatformCouponDiscountAmount().multiply(orderDetail.getPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                        //该条明细的平台补贴退款金额=商家券的退款金额+平台券的退款金额
                        BigDecimal totalReturnPlatformAmount = subReturnPlatformAmount.add(subReturnBusinessAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                        var2.setRefundCouponAmount(totalReturnPlatformAmount);
                        //应结算平台补贴金额=平台补贴金额-退款的平台补贴金额
                        settCouponAmount = totalPlatformAmount.subtract(totalReturnPlatformAmount);
                        var2.setSettleCouponAmount(settCouponAmount);

                        //计算秒杀&特价金额
                        //秒杀特价金额
                        BigDecimal promotionSaleSubTotal = BigDecimal.ZERO;
                        //秒杀特价退货金额
                        BigDecimal returnPromotionSaleSubTotal = BigDecimal.ZERO;
                        if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode()) || ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode())) {
                            promotionSaleSubTotal = orderDetail.getPromotionSaleSubTotal();
                            returnPromotionSaleSubTotal = orderDetail.getReturnPromotionSaleSubTotal();
                            //如果本单的秒杀&特价结算金额大于0的时候，则计算实际的秒杀&特价金额，否则商品明细的秒杀特价结算金额记为0
                            if (promotionSaleSubTotal.compareTo(BigDecimal.ZERO) == 1) {
                                PromotionActivityDTO activityDTO = finalActivityDTOMap.get(orderDetail.getPromotionActivityId());
                                if (ObjectUtil.isNotNull(activityDTO)) {
                                    BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                                    promotionSaleSubTotal = promotionSaleSubTotal.multiply(platformPercentage);
                                    returnPromotionSaleSubTotal = returnPromotionSaleSubTotal.multiply(platformPercentage);
                                }
                            } else {
                                promotionSaleSubTotal = BigDecimal.ZERO;
                                returnPromotionSaleSubTotal = BigDecimal.ZERO;
                            }
                        }
                        var2.setPromotionAmount(promotionSaleSubTotal);
                        var2.setRefundPromotionAmount(returnPromotionSaleSubTotal);
                        //应结算秒杀金额=秒杀金额-退款的平秒杀金额
                        settPromotionAmount = promotionSaleSubTotal.subtract(returnPromotionSaleSubTotal);
                        var2.setSettPromotionAmount(settPromotionAmount);

                        //计算组合促销金额
                        //组合促销金额
                        BigDecimal comPacAmount = BigDecimal.ZERO;
                        //组合促销退货金额
                        BigDecimal refundComPacAmount = BigDecimal.ZERO;
                        if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())) {
                            comPacAmount = orderDetail.getComPacAmount();
                            refundComPacAmount = orderDetail.getReturnComPacAmount();
                            //如果本单的组合促销结算金额大于0的时候，则计算实际的组合促销金额，否则商品明细的组合促销结算金额记为0
                            if (comPacAmount.compareTo(BigDecimal.ZERO) == 1) {
                                PromotionActivityDTO activityDTO = finalActivityDTOMap.get(orderDetail.getPromotionActivityId());
                                if (ObjectUtil.isNotNull(activityDTO)) {
                                    BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                                    comPacAmount = comPacAmount.multiply(platformPercentage);
                                    refundComPacAmount = refundComPacAmount.multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                                }
                            } else {
                                comPacAmount = BigDecimal.ZERO;
                                refundComPacAmount = BigDecimal.ZERO;
                            }
                        }
                        var2.setComPacAmount(comPacAmount);
                        var2.setRefundComPacAmount(refundComPacAmount);
                        //应结算组合促销金额=组合促销金额-退款的组合促销金额
                        settComPacAmount = comPacAmount.subtract(refundComPacAmount);
                        var2.setSettComPacAmount(settComPacAmount);

                        //设置结算单时间
                        if (ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementId(), 0L)) {
                            SettlementDTO settlementDTO = settlementDTOMap.get(settlementOrderDTO.getSaleSettlementId());
                            var2.setSaleCreateTime(settlementDTO.getCreateTime());
                            if (DateUtil.compare(settlementDTO.getSettlementTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                                var2.setSaleSettlementTime(settlementDTO.getSettlementTime());
                            }
                        }
                    }

                    //预售促销优惠金额
                    BigDecimal preSaleAmount= BigDecimal.ZERO;
                    //退回预售促销优惠的金额
                    BigDecimal refundPreSaleAmount= BigDecimal.ZERO;
                    //应结预售促销金额
                    BigDecimal settPreSaleAmount= BigDecimal.ZERO;

                    //计算预售促销金额
                    if (ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementId(), 0L) && ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementNo(), "")) {
                        //如果商品的结算金额小于等于0 则忽略该商品
                        if (BigDecimal.ZERO.compareTo(orderDetail.getPresaleDiscountAmount()) == -1){
                            PresaleActivityDTO activityDTO = finalPreSaleActivityDTOMap.get(orderDetail.getPromotionActivityId());
                            BigDecimal platformPercentage;
                            if (ObjectUtil.equal(activityDTO.getBear(), CouponBearTypeEnum.BUSINESS.getCode())){
                                platformPercentage=BigDecimal.ZERO;
                            }else {
                                platformPercentage = activityDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                            }

                            preSaleAmount = orderDetail.getPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //退款的组合包促销金额
                            refundPreSaleAmount = orderDetail.getReturnPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //应结算预售促销金额=预售促销金额-退款的预售促销金额
                            settPreSaleAmount = preSaleAmount.subtract(refundPreSaleAmount);
                        }

                    }
                    //支付促销优惠金额
                    BigDecimal paySaleAmount = BigDecimal.ZERO;
                    //退回支付促销优惠的金额
                    BigDecimal refundPaySaleAmount = BigDecimal.ZERO;
                    //应结算的支付促销优惠的金额
                    BigDecimal settPaySaleAmount = BigDecimal.ZERO;

                    //计算支付促销金额
                    //order_detail的promotion_activity_type字段，支付促销不会打标记的
                    BigDecimal paymentDiscountAmount = orderDetail.getPaymentPlatformDiscountAmount().add(orderDetail.getPaymentShopDiscountAmount());
                    //平台承担活动百分比
                    BigDecimal payPlatformPercent = finalOrderActivityInfoMap.getOrDefault(orderDetail.getOrderId(),ListUtil.toList()).stream().filter(e -> ObjectUtil.equal(e.getActivityType(), PromotionActivityTypeEnum.PAYMENT.getCode())).map(OrderPromotionActivityDTO::getActivityPlatformPercent).reduce(BigDecimal.ZERO, BigDecimal::add);

                    //如果商品的支付结算金额大于0 且 平台承担百分比大于0 才计算支付促销金额
                    if (BigDecimal.ZERO.compareTo(paymentDiscountAmount) == -1 && BigDecimal.ZERO.compareTo(payPlatformPercent) == -1) {

                        Map<Integer, BigDecimal> sponsorPercentMap = finalOrderActivityInfoMap.getOrDefault(orderDetail.getOrderId(),ListUtil.toList()).stream().filter(e -> ObjectUtil.equal(e.getActivityType(), PromotionActivityTypeEnum.PAYMENT.getCode())).collect(Collectors.toMap(OrderPromotionActivityDTO::getSponsorType, OrderPromotionActivityDTO::getActivityPlatformPercent));
                        paySaleAmount = orderDetail.getPaymentPlatformDiscountAmount().multiply(sponsorPercentMap.getOrDefault(1,BigDecimal.ZERO)).add(orderDetail.getPaymentShopDiscountAmount().multiply(sponsorPercentMap.getOrDefault(2,BigDecimal.ZERO))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        //退款的支付促销金额
                        refundPaySaleAmount = orderDetail.getReturnPlatformPaymentDiscountAmount().multiply(sponsorPercentMap.getOrDefault(1,BigDecimal.ZERO)).add(orderDetail.getReturnShopPaymentDiscountAmount().multiply(sponsorPercentMap.getOrDefault(2,BigDecimal.ZERO))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        settPaySaleAmount=paySaleAmount.subtract(refundPaySaleAmount);
                    }

                    var2.setPresaleDiscountAmount(preSaleAmount);
                    var2.setReturnPresaleDiscountAmount(refundPreSaleAmount);
                    var2.setSettPreSaleAmount(settPreSaleAmount);
                    var2.setPaymentPlatformDiscountAmount(paySaleAmount);
                    var2.setRefundPayAmount(refundPaySaleAmount);
                    var2.setSettPaySaleAmount(settPaySaleAmount);


                    //预售违约金额
                    BigDecimal settPreDefAmount=BigDecimal.ZERO;
                    //如果存在预售违约金则强制把订单的违约金放入到第一个商品明细中
                    if (settlementOrderDTO.getPresaleDefaultAmount().compareTo(BigDecimal.ZERO)>0){
                        String code = var2.getCode();
                        if (StrUtil.isNotBlank(code)) {
                            code = code + "," + settlementOrderDTO.getPdSettlementNo();
                        } else {
                            code = settlementOrderDTO.getPdSettlementNo();
                        }
                        var2.setCode(code);
                        settPreDefAmount=settlementOrderDTO.getPresaleDefaultAmount();
                        var2.setPresaleDefaultAmount(settlementOrderDTO.getPresaleDefaultAmount());
                        var2.setRefundPresaleDefaultAmount(BigDecimal.ZERO);
                        var2.setPreDefSettlementAmount(settlementOrderDTO.getPresaleDefaultAmount());

                    }
                    //设置结算单时间
                    if (ObjectUtil.notEqual(settlementOrderDTO.getPdSettlementId(), 0L)) {
                        SettlementDTO settlementDTO = settlementDTOMap.get(settlementOrderDTO.getPdSettlementId());
                        var2.setPdSettCreateTime(settlementDTO.getCreateTime());
                        if (DateUtil.compare(settlementDTO.getSettlementTime(), DateUtil.parseDate("1970-01-01 00:00:00")) > 0) {
                            var2.setPdSettlementTime(settlementDTO.getSettlementTime());
                        }
                    }

                    //结算总金额=货款应结算金额+应结算平台优惠券金额+应结算平台秒杀&特价金额+应结算的组合促销金额+应结算的预售促销金额+应结算的支付促销金额+应结算的预售违约金额
                    BigDecimal amount = goodsSettAmount.add(settCouponAmount).add(settPromotionAmount).add(settComPacAmount).add(settPreSaleAmount).add(settPaySaleAmount).add(settPreDefAmount);
                    var2.setAmount(amount);

                    //todo 设置商品信息
                    List<GoodsSkuDTO> goodsSkuDTO = goodsInfoMap.get(orderDetail.getGoodsId());
                    if (CollUtil.isNotEmpty(goodsSkuDTO)) {
                        List<GoodsSkuDTO> collect = goodsSkuDTO.stream().filter(e -> ObjectUtil.equal(e.getGoodsLine(), GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
                        if (CollUtil.isNotEmpty(collect)) {
                            //设置商品信息
                            var2.setGoodsErpCode(collect.stream().findAny().get().getSn());
                            var2.setInSn(collect.stream().findAny().get().getInSn());
                        }
                    }
                    Map<String, Object> detailPojo = BeanUtil.beanToMap(var2);
                    settlementOrderData.add(detailPojo);
                });
            });
            mainCurrent = mainCurrent + 1;
        } while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));

        ExportDataDTO exportApplyDTO = new ExportDataDTO();
        exportApplyDTO.setSheetName("结算单明细");
        // 页签字段
        exportApplyDTO.setFieldMap(FIELD_SHEET1);
        // 页签数据
        exportApplyDTO.setData(settlementData);

        ExportDataDTO exportApplyDetailDTO = new ExportDataDTO();
        exportApplyDetailDTO.setSheetName("结算单商品明细");
        // 页签字段
        exportApplyDetailDTO.setFieldMap(FIELD_SHEET2);
        // 页签数据
        exportApplyDetailDTO.setData(settlementOrderData);

        //封装excel
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportApplyDTO);
        sheets.add(exportApplyDetailDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public ExportSettlementOrderPageListRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, ExportSettlementOrderPageListRequest.class);
    }
}
