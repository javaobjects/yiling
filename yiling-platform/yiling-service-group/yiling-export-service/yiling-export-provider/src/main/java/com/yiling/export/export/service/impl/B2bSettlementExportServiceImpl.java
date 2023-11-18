package com.yiling.export.export.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.B2bSettDetailExcelModel;
import com.yiling.export.export.model.B2bSettExcelModel;
import com.yiling.export.export.model.B2bSettOrderInfoExcelModel;
import com.yiling.export.export.model.B2bSettUserExcelModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
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
import com.yiling.settlement.b2b.api.SettlementDetailApi;
import com.yiling.settlement.b2b.api.SettlementOrderApi;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.SettlementDetailDTO;
import com.yiling.settlement.b2b.dto.SettlementOrderDTO;
import com.yiling.settlement.b2b.dto.request.ExportSettlementSimpleInfoPageListRequest;
import com.yiling.settlement.b2b.dto.request.QuerySettlementPageListRequest;
import com.yiling.settlement.b2b.enums.SettlementStatusEnum;
import com.yiling.settlement.b2b.enums.SettlementTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 采购关系导出
 *
 * @author dexi.yao
 * @date: 2023/3/17
 */
@Slf4j
@Service("b2bSettlementExportServiceImpl")
public class B2bSettlementExportServiceImpl implements BaseExportQueryDataService<ExportSettlementSimpleInfoPageListRequest> {

    @DubboReference
    DictApi dictApi;
    @DubboReference
    SettlementApi settlementApi;
    @DubboReference
    SettlementDetailApi settlementDetailApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference(timeout = 1000 * 60)
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderB2BApi orderB2BApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    SettlementOrderApi settlementOrderApi;
    @DubboReference
    PresaleActivityApi presaleActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;

    private ThreadLocal<List<DictBO>> dictThreadLocal = new ThreadLocal<>();

    /**
     * 获取数据字典
     *
     * @return
     */
    private List<DictBO> getDictDataList() {

        List<DictBO> dictBOList = dictThreadLocal.get();

        if (dictBOList == null) {
            List<DictBO> dictApiEnabledList = dictApi.getEnabledList();
            dictThreadLocal.set(dictApiEnabledList);
            dictBOList = dictApiEnabledList;
        }

        return dictBOList;
    }


    /**
     * 获取数据字典值
     *
     * @param key
     * @param data
     * @return
     */
    private String getDictDataValue(String key, Integer data) {

        if (data == null || data == 0) {

            return "--";
        }

        List<DictBO> dataList = this.getDictDataList();
        if (CollectionUtil.isEmpty(dataList)) {
            return "--";
        }

        Map<String, DictBO> dictBOMap = dataList.stream().collect(Collectors.toMap(DictBO::getName, Function.identity()));
        DictBO dictBO = dictBOMap.get(key);
        if (dictBO == null) {
            return "--";
        }

        List<DictBO.DictData> dictBODataList = dictBO.getDataList();
        if (CollectionUtil.isEmpty(dictBODataList)) {

            return "--";
        }

        Map<String, DictBO.DictData> dictDataMap = dictBODataList.stream().collect(Collectors.toMap(DictBO.DictData::getValue, Function.identity(), (k1, k2) -> k1));

        return Optional.ofNullable(dictDataMap.get(data + "")).map(t -> t.getLabel()).orElse("--");
    }

    @Override
    public ExportSettlementSimpleInfoPageListRequest getParam(Map<String, Object> map) {
        ExportSettlementSimpleInfoPageListRequest request = PojoUtils.map(map, ExportSettlementSimpleInfoPageListRequest.class);
        return request;
    }


    public List<B2bSettExcelModel> querySett(QuerySettlementPageListRequest request) {

        Page<SettlementDTO> page = settlementApi.querySettlementPageList(request);
        List<SettlementDTO> settList = page.getRecords();

        if (CollUtil.isEmpty(settList)) {
            return Collections.emptyList();
        }

        //查询企业信息
        List<Long> eidList = settList.stream().map(SettlementDTO::getEid).collect(Collectors.toList());
        eidList = eidList.stream().distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);

        Map<Long, String> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));

        List<B2bSettExcelModel> result = settList.stream().map(t -> {

            B2bSettExcelModel var = PojoUtils.map(t, B2bSettExcelModel.class);

            var.setEname(enterpriseMap.getOrDefault(t.getEid(), ""));
            var.setTypeStr(SettlementTypeEnum.getByCode(t.getType()).getName());
            var.setStatusStr(SettlementStatusEnum.getByCode(t.getStatus()).getName());

            return var;

        }).collect(Collectors.toList());

        return result;
    }

    public List<B2bSettUserExcelModel> querySettByUser(QuerySettlementPageListRequest request) {

        Page<SettlementDTO> page = settlementApi.querySettlementPageList(request);
        List<SettlementDTO> settList = page.getRecords();

        if (CollUtil.isEmpty(settList)) {
            return Collections.emptyList();
        }

        //查询企业信息
        List<Long> eidList = settList.stream().map(SettlementDTO::getEid).collect(Collectors.toList());
        eidList = eidList.stream().distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);

        Map<Long, String> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));

        List<B2bSettUserExcelModel> result = settList.stream().map(t -> {

            B2bSettUserExcelModel var = PojoUtils.map(t, B2bSettUserExcelModel.class);

            var.setEname(enterpriseMap.getOrDefault(t.getEid(), ""));
            var.setTypeStr(SettlementTypeEnum.getByCode(t.getType()).getName());
            var.setStatusStr(SettlementStatusEnum.getByCode(t.getStatus()).getName());

            return var;

        }).collect(Collectors.toList());

        return result;
    }

    public List<B2bSettDetailExcelModel> querySettDetail(List<Long> settIds) {

        List<SettlementDetailDTO> settlementDetailDTOS = settlementDetailApi.querySettlementDetailBySettlementId(settIds);

        List<SettlementDTO> settlementDTOList = settlementApi.getByIdList(settIds);
        Map<Long, SettlementDTO> settlementDTOMap = settlementDTOList.stream().collect(Collectors.toMap(SettlementDTO::getId, e -> e));

        //查询企业信息
        List<Long> eidIdList = settlementDetailDTOS.stream().map(SettlementDetailDTO::getBuyerEid).collect(Collectors.toList());
        eidIdList.addAll(settlementDetailDTOS.stream().map(SettlementDetailDTO::getSellerEid).collect(Collectors.toList()));
        eidIdList = eidIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, String> entDTOMap = enterpriseApi.listByIds(eidIdList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));

        //查询订单信息
        List<Long> orderIdList = settlementDetailDTOS.stream().map(SettlementDetailDTO::getOrderId).collect(Collectors.toList());
        Map<Long, OrderDTO> orderDTOMap = orderApi.listByIds(orderIdList).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));


        List<B2bSettDetailExcelModel> result = settlementDetailDTOS.stream().map(t -> {

            B2bSettDetailExcelModel var = PojoUtils.map(t, B2bSettDetailExcelModel.class);

            SettlementDTO settlementDTO = settlementDTOMap.get(t.getSettlementId());

            var.setCode(settlementDTO.getCode());
            var.setBuyerName(entDTOMap.getOrDefault(t.getBuyerEid(), ""));
            var.setSellerName(entDTOMap.getOrDefault(t.getSellerEid(), ""));
            //设置退款金额
            //设置结算单类型
            var.setTypeStr(SettlementTypeEnum.getByCode(settlementDTO.getType()).getName());
            OrderDTO orderDTO = orderDTOMap.get(t.getOrderId());
            if (ObjectUtil.isNotNull(orderDTO)) {
                var.setOrderCreateTime(orderDTO.getCreateTime());
                var.setPaymentTime(orderDTO.getPaymentTime());
                var.setCustomerErpCode(orderDTO.getCustomerErpCode());
            }

            return var;

        }).collect(Collectors.toList());
        return result;
    }

    public List<B2bSettOrderInfoExcelModel> queryOrderDetail(List<B2bSettDetailExcelModel> settDetailList) {
        List<B2bSettOrderInfoExcelModel> result = ListUtil.toList();
        //查询结算单信息
        List<SettlementDTO> settlementDTOList = settlementApi.getByIdList(settDetailList.stream().map(B2bSettDetailExcelModel::getSettlementId).distinct().collect(Collectors.toList()));
        Map<Long, SettlementDTO> settlementDTOMap = settlementDTOList.stream().collect(Collectors.toMap(SettlementDTO::getId, e -> e));
        //查询企业信息
        List<Long> eidIdList = settDetailList.stream().map(B2bSettDetailExcelModel::getBuyerEid).collect(Collectors.toList());
        eidIdList.addAll(settDetailList.stream().map(B2bSettDetailExcelModel::getSellerEid).collect(Collectors.toList()));
        eidIdList = eidIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, String> entDTOMap = enterpriseApi.listByIds(eidIdList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        //查询订单信息
        List<Long> orderIdList = settDetailList.stream().map(B2bSettDetailExcelModel::getOrderId).distinct().collect(Collectors.toList());
        Map<Long, OrderDTO> orderDTOMap = orderApi.listByIds(orderIdList).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
        //查询order__change
        Map<Long, List<OrderDetailChangeDTO>> orderChangeListMap = orderDetailChangeApi.listByOrderIds(orderIdList).stream().collect(Collectors.groupingBy(OrderDetailChangeDTO::getOrderId));


        settDetailList.forEach(settlementDetailDTO -> {
            SettlementDTO settlementDTO = settlementDTOMap.get(settlementDetailDTO.getSettlementId());
            OrderDTO orderDTO = orderDTOMap.get(settlementDetailDTO.getOrderId());

            //生成sheet3
            List<B2BSettlementDetailDTO> orderDetailList = orderB2BApi.listSettleOrderDetailByOrderId(settlementDetailDTO.getOrderId());

            //查询商品信息
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIdsAndStatus(orderDetailList.stream().map(B2BSettlementDetailDTO::getGoodsId).distinct().collect(Collectors.toList()), null);
            Map<Long, List<GoodsSkuDTO>> goodsInfoMap = goodsSkuDTOList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));

            orderDetailList.forEach(orderDetail -> {
                B2bSettOrderInfoExcelModel var2 = new B2bSettOrderInfoExcelModel();
                var2.setCode(settlementDTO.getCode());
                var2.setOrderNo(settlementDetailDTO.getOrderNo());
                var2.setBuyerName(entDTOMap.getOrDefault(settlementDetailDTO.getBuyerEid(), ""));
                var2.setCustomerErpCode(orderDTO.getCustomerErpCode());
                var2.setPaymentTime(DateUtil.compare(settlementDetailDTO.getPaymentTime(), DateUtil.parse("1970-01-01 00:00:00")) == 0 ? null : settlementDetailDTO.getPaymentTime());
                var2.setSettlementTime(settlementDTO.getSettlementTime());
                var2.setTypeStr(SettlementTypeEnum.getByCode(settlementDTO.getType()).getName());
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
                var2.setCustomerErpCode(orderDetail.getCustomerErpCode());


                //查询订单下的订单明细
                Map<Long, List<OrderDetailChangeDTO>> orderDetailMap = orderChangeListMap.get(orderDetail.getOrderId()).stream().collect(Collectors.groupingBy(OrderDetailChangeDTO::getDetailId));
                OrderDetailChangeDTO detailChangeDTO = orderDetailMap.get(orderDetail.getDetailId()).get(0);
                //设置卖家退货数量&&买家退货数量
                var2.setSellerReturnQuantity(detailChangeDTO.getSellerReturnQuantity());
                var2.setReturnQuantity(detailChangeDTO.getReturnQuantity());

                BigDecimal goodsSettAmount = BigDecimal.ZERO;
                if (ObjectUtil.equal(SettlementTypeEnum.getByCode(settlementDTO.getType()).getCode(), SettlementTypeEnum.GOODS.getCode())) {
                    //计算货款金额：货款金额=购买商品小计-平台优惠劵折扣金额-商家优惠劵折扣金额-预售促销金额-支付促销金额
                    BigDecimal goodsAmount = detailChangeDTO.getGoodsAmount().subtract(detailChangeDTO.getPlatformCouponDiscountAmount()).subtract(detailChangeDTO.getCouponDiscountAmount()).subtract(detailChangeDTO.getPresaleDiscountAmount()).subtract(detailChangeDTO.getPlatformPaymentDiscountAmount().add(detailChangeDTO.getShopPaymentDiscountAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
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
                    //计算货款退款金额:货款退款金额=总退款金额-卖家优惠券退款金额-买家优惠券退款金额-预售退款金额-支付促销退款金额
                    BigDecimal returnGoodsAmount = totalReturn.subtract(sellerCouponReturnAmount).subtract(buyerReturnAmount).subtract(preSaleReturnAmount).subtract(paySaleReturnAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                    var2.setRefundGoodsAmount(returnGoodsAmount);
                    //货款应结算金额=货款金额-货款退款金额
                    goodsSettAmount = goodsAmount.subtract(returnGoodsAmount);
                    var2.setGoodsSettleAmount(goodsSettAmount);
                }

                //查询秒杀特价活动百分比
                List<Long> promotionActivityIds = orderDetailList.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.FULL_GIFT.getCode()) || ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode()) || ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode()) || ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())).map(B2BSettlementDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());

                Map<Long, PromotionActivityDTO> activityDTOMap = MapUtil.newHashMap();
                if (CollUtil.isNotEmpty(promotionActivityIds)) {
                    activityDTOMap = promotionActivityApi.batchQueryByIdList(promotionActivityIds).stream().collect(Collectors.toMap(PromotionActivityDTO::getId, e -> e));
                }

                List<Long> preSaleActivityIds = orderDetailList.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.PRESALE.getCode())).map(B2BSettlementDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());
                Map<Long, PresaleActivityDTO> preSaleActivityDTOMap = MapUtil.newHashMap();
                if (CollUtil.isNotEmpty(preSaleActivityIds)) {
                    preSaleActivityDTOMap = presaleActivityApi.batchQueryByIdList(preSaleActivityIds);
                }
                //查询订单的活动信息
                Map<Long, List<OrderPromotionActivityDTO>> orderActivityInfoMap = MapUtil.newHashMap();
                List<Long> orderIds = orderDetailList.stream().map(B2BSettlementDetailDTO::getOrderId).distinct().collect(Collectors.toList());
                if (CollUtil.isNotEmpty(orderIds)) {
                    orderActivityInfoMap = orderPromotionActivityApi.listByOrderIds(orderIds).stream().map(e -> {
                        e.setActivityPlatformPercent(e.getActivityPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                        return e;
                    }).collect(Collectors.groupingBy(OrderPromotionActivityDTO::getOrderId));
                }

                Map<Long, PromotionActivityDTO> finalActivityDTOMap = activityDTOMap;
                Map<Long, List<OrderPromotionActivityDTO>> finalOrderActivityInfoMap = orderActivityInfoMap;

                //应结算平台优惠券金额
                BigDecimal settCouponAmount = BigDecimal.ZERO;
                //应结算平台秒杀&特价金额
                BigDecimal settPromotionAmount = BigDecimal.ZERO;
                //应结组合促销金额
                BigDecimal settComPacAmount = BigDecimal.ZERO;
                //预售促销优惠金额
                BigDecimal preSaleAmount = BigDecimal.ZERO;
                //退回预售促销优惠的金额
                BigDecimal refundPreSaleAmount = BigDecimal.ZERO;
                //应结预售促销金额
                BigDecimal settPreSaleAmount = BigDecimal.ZERO;
                //支付促销优惠金额
                BigDecimal paySaleAmount = BigDecimal.ZERO;
                //退回支付促销优惠的金额
                BigDecimal refundPaySaleAmount = BigDecimal.ZERO;
                //应结算的支付促销优惠的金额
                BigDecimal settPaySaleAmount = BigDecimal.ZERO;


                SettlementOrderDTO settlementOrderDTO = settlementOrderApi.querySettOrderByOrderId(orderDetail.getOrderId());
                if (ObjectUtil.equal(SettlementTypeEnum.getByCode(settlementDTO.getType()).getCode(), SettlementTypeEnum.SALE.getCode())) {
                    //计算平台补贴金额
                    //计算该条订单明细的平台券+商家券的平台补贴金额
                    //平台券补贴金额
                    BigDecimal subPlatformAmount = orderDetail.getPlatformCouponDiscountAmount().multiply(orderDetail.getPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                    //商家券补贴金额
                    BigDecimal subBusinessAmount = orderDetail.getCouponDiscountAmount().multiply(orderDetail.getShopPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getShopPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                    //平台补贴金额=平台券补贴金额+商家券补贴金额
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
                            //应结算组合促销金额=组合促销金额-退款的组合促销金额
                            settComPacAmount = comPacAmount.subtract(refundComPacAmount);
                        } else {
                            comPacAmount = BigDecimal.ZERO;
                            refundComPacAmount = BigDecimal.ZERO;
                        }
                    }
                    var2.setComPacAmount(comPacAmount);
                    var2.setRefundComPacAmount(refundComPacAmount);

                    //如果商品的结算金额小于等于0 则忽略该商品
                    if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.PRESALE.getCode())) {
                        PresaleActivityDTO activityDTO = preSaleActivityDTOMap.get(orderDetail.getPromotionActivityId());
                        BigDecimal platformPercentage;
                        if (ObjectUtil.equal(activityDTO.getBear(), CouponBearTypeEnum.BUSINESS.getCode())) {
                            platformPercentage = BigDecimal.ZERO;
                        } else {
                            platformPercentage = activityDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                        }

                        preSaleAmount = orderDetail.getPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                        //退款的预售金额
                        refundPreSaleAmount = orderDetail.getReturnPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                        settPreSaleAmount = preSaleAmount.subtract(refundPreSaleAmount);
                    }
                    //计算支付促销金额
                    //order_detail的promotion_activity_type字段，支付促销不会打标记的
                    BigDecimal paymentDiscountAmount = orderDetail.getPaymentPlatformDiscountAmount().add(orderDetail.getPaymentShopDiscountAmount());
                    //平台承担活动百分比
                    BigDecimal payPlatformPercent = finalOrderActivityInfoMap.getOrDefault(orderDetail.getOrderId(), ListUtil.toList()).stream().filter(e -> ObjectUtil.equal(e.getActivityType(), PromotionActivityTypeEnum.PAYMENT.getCode())).map(OrderPromotionActivityDTO::getActivityPlatformPercent).reduce(BigDecimal.ZERO, BigDecimal::add);

                    //如果商品的支付结算金额大于0 且 平台承担百分比大于0 才计算支付促销金额
                    if (BigDecimal.ZERO.compareTo(paymentDiscountAmount) == -1 && BigDecimal.ZERO.compareTo(payPlatformPercent) == -1) {

                        Map<Integer, BigDecimal> sponsorPercentMap = finalOrderActivityInfoMap.getOrDefault(orderDetail.getOrderId(), ListUtil.toList()).stream().filter(e -> ObjectUtil.equal(e.getActivityType(), PromotionActivityTypeEnum.PAYMENT.getCode())).collect(Collectors.toMap(OrderPromotionActivityDTO::getSponsorType, OrderPromotionActivityDTO::getActivityPlatformPercent));
                        paySaleAmount = orderDetail.getPaymentPlatformDiscountAmount().multiply(sponsorPercentMap.getOrDefault(1, BigDecimal.ZERO)).add(orderDetail.getPaymentShopDiscountAmount().multiply(sponsorPercentMap.getOrDefault(2, BigDecimal.ZERO))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        //退款的支付促销金额
                        refundPaySaleAmount = orderDetail.getReturnPlatformPaymentDiscountAmount().multiply(sponsorPercentMap.getOrDefault(1, BigDecimal.ZERO)).add(orderDetail.getReturnShopPaymentDiscountAmount().multiply(sponsorPercentMap.getOrDefault(2, BigDecimal.ZERO))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        settPaySaleAmount = paySaleAmount.subtract(refundPaySaleAmount);
                    }


                }
                var2.setSettComPacAmount(settComPacAmount);
                var2.setPresaleDiscountAmount(preSaleAmount);
                var2.setReturnPresaleDiscountAmount(refundPreSaleAmount);
                var2.setSettPreSaleAmount(settPreSaleAmount);

                var2.setPaymentPlatformDiscountAmount(paySaleAmount);
                var2.setRefundPayAmount(refundPaySaleAmount);
                var2.setSettPaySaleAmount(settPaySaleAmount);

                //预售违约金额
                BigDecimal settPreDefAmount = BigDecimal.ZERO;
                //如果存在预售违约金则强制把订单的违约金放入到第一个商品明细中

                if (ObjectUtil.equal(SettlementTypeEnum.getByCode(settlementDTO.getType()).getCode(), SettlementTypeEnum.PRESALE_DEFAULT.getCode())) {
                    if (settlementOrderDTO.getPresaleDefaultAmount().compareTo(BigDecimal.ZERO) > 0) {
                        settPreDefAmount = settlementOrderDTO.getPresaleDefaultAmount();
                        var2.setPresaleDefaultAmount(settlementOrderDTO.getPresaleDefaultAmount());
                        var2.setRefundPresaleDefaultAmount(BigDecimal.ZERO);
                        var2.setPreDefSettlementAmount(settlementOrderDTO.getPresaleDefaultAmount());
                    }
                }

                //结算总金额=货款应结算金额+应结算平台优惠券金额+应结算平台秒杀&特价金额+应结算的组合促销金额+应结算的预售促销金额+支付促销金额+预售违约金额
                BigDecimal amount = goodsSettAmount.add(settCouponAmount).add(settPromotionAmount).add(settComPacAmount).add(settPreSaleAmount).add(settPaySaleAmount).add(settPreDefAmount);
                var2.setAmount(amount);

                List<GoodsSkuDTO> goodsSkuDTO = goodsInfoMap.get(orderDetail.getGoodsId());
                if (CollUtil.isNotEmpty(goodsSkuDTO)) {
                    List<GoodsSkuDTO> collect = goodsSkuDTO.stream().filter(e -> ObjectUtil.equal(e.getGoodsLine(), GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(collect)) {
                        //设置商品信息
                        var2.setGoodsErpCode(collect.stream().findAny().get().getSn());
                        var2.setInSn(collect.stream().findAny().get().getInSn());
                    }
                }
                result.add(var2);
            });
        });
        return result;
    }


    @Override
    @SneakyThrows
    public byte[] getExportByte(ExportSettlementSimpleInfoPageListRequest request, String file) {
        if (request == null) {
            return null;
        }
        String tmpDirPath = FileUtil.getTmpDirPath() + File.separator + "B2bSettlementInfoExport";
        File tmpExcelDir = FileUtil.newFile(tmpDirPath + File.separator + "excel");

        if (!tmpExcelDir.isDirectory()) {
            tmpExcelDir.mkdirs();
        }
        file = file.substring(0, file.lastIndexOf(".xlsx"));
        String fileName = file + Constants.SEPARATOR_UNDERLINE + "结算单" + DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + ".xlsx";

        QuerySettlementPageListRequest mainQueryRequest = PojoUtils.map(request, QuerySettlementPageListRequest.class);

        //        //设置结算单导出范围
        //        if (ObjectUtil.isNull(request.getEid())) {
        //            mainQueryRequest.setEid(null);
        //        } else {
        //            mainQueryRequest.setEid(request.getEid());
        //        }
        //
        //根据供应商名字查eid
        if (StrUtil.isNotBlank(request.getEntName())) {
            List<Long> eidList = ListUtil.toList();
            int current = 1;
            Page<EnterpriseDTO> entPage;
            QueryEnterprisePageListRequest queryRequest = new QueryEnterprisePageListRequest();
            queryRequest.setSize(100);
            queryRequest.setName(request.getEntName());
            //分页查询申请单列表
            do {
                queryRequest.setCurrent(current);
                //分页查询符合结算条件的订单
                entPage = enterpriseApi.pageList(queryRequest);
                if (CollUtil.isNotEmpty(entPage.getRecords())) {
                    List<Long> eids = entPage.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                    eidList.addAll(eids);
                }
                current = current + 1;
            } while (entPage != null && CollUtil.isNotEmpty(entPage.getRecords()));
            mainQueryRequest.setEidList(eidList);
        }

        int current = 1;
        int settSheetNum = 1;
        int settDetailSheetNum = 1;
        int size = 200;
        List<Long> settIdList = ListUtil.toList();
        List<B2bSettDetailExcelModel> settDetailList = ListUtil.toList();
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        try {
            //结算单
            settSheet:
            do {
                WriteSheet writeSheet;
                if (ObjectUtil.isNull(request.getEid())) {
                    writeSheet = EasyExcel.writerSheet("结算单" + settSheetNum).head(B2bSettExcelModel.class).build();
                } else {
                    writeSheet = EasyExcel.writerSheet("结算单" + settSheetNum).head(B2bSettUserExcelModel.class).build();
                }

                while (true) {
                    mainQueryRequest.setCurrent(current);
                    mainQueryRequest.setSize(size);

                    if (ObjectUtil.isNull(request.getEid())) {
                        List<B2bSettExcelModel> list = querySett(mainQueryRequest);
                        // 写文件
                        excelWriter.write(list, writeSheet);

                        if (CollUtil.isEmpty(list)) {
                            break settSheet;
                        }
                        settIdList.addAll(list.stream().map(B2bSettExcelModel::getId).collect(Collectors.toList()));
                    } else {
                        List<B2bSettUserExcelModel> list = querySettByUser(mainQueryRequest);
                        // 写文件
                        excelWriter.write(list, writeSheet);

                        if (CollUtil.isEmpty(list)) {
                            break settSheet;
                        }
                        settIdList.addAll(list.stream().map(B2bSettUserExcelModel::getId).collect(Collectors.toList()));
                    }

                    if (current % 5000 == 0) {   // 10w数据做文件切割
                        settSheetNum++;
                        current++;
                        break;
                    }
                    current++;
                }

            } while (true);

            //结算单明细
            List<Long> tempSettIdList;
            int querySettDetailCurrent = 0;
            settDetailSheet:
            do {
                WriteSheet writeSheet = EasyExcel.writerSheet("结算单明细" + settDetailSheetNum).head(B2bSettDetailExcelModel.class).build();

                while (true) {
                    tempSettIdList = CollUtil.page(querySettDetailCurrent, 10, settIdList);
                    if (CollUtil.isEmpty(tempSettIdList)) {
                        break settDetailSheet;
                    }
                    List<B2bSettDetailExcelModel> list = querySettDetail(tempSettIdList);
                    // 写文件
                    excelWriter.write(list, writeSheet);

                    if (CollUtil.isEmpty(list)) {
                        break settDetailSheet;
                    }
                    settDetailList.addAll(list);

                    if (querySettDetailCurrent + 1 % 10000 == 0) {   // 10w数据做文件切割
                        settDetailSheetNum++;
                        querySettDetailCurrent++;
                        break;
                    }
                    querySettDetailCurrent++;
                }

            } while (true);
            //订单明细
            List<B2bSettDetailExcelModel> tempSettDetailList;
            int queryOrderDetailCurrent = 0;
            orderDetailSheet:
            do {
                WriteSheet writeSheet = EasyExcel.writerSheet("结算单商品明细" + settDetailSheetNum).head(B2bSettOrderInfoExcelModel.class).build();

                while (true) {
                    tempSettDetailList = CollUtil.page(queryOrderDetailCurrent, 10, settDetailList);
                    if (CollUtil.isEmpty(tempSettDetailList)) {
                        break orderDetailSheet;
                    }
                    List<B2bSettOrderInfoExcelModel> list = queryOrderDetail(tempSettDetailList);
                    // 写文件
                    excelWriter.write(list, writeSheet);

                    if (CollUtil.isEmpty(list)) {
                        break orderDetailSheet;
                    }

                    if (queryOrderDetailCurrent + 1 % 10000 == 0) {   // 10w数据做文件切割
                        settDetailSheetNum++;
                        queryOrderDetailCurrent++;
                        break;
                    }
                    queryOrderDetailCurrent++;
                }

            } while (true);
        } catch (Exception e) {
            log.error("导出结算单报错，异常原因={}", e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        return getFileByte(tmpDirPath, fileName);
    }


    @Override
    public QueryExportDataDTO queryData(ExportSettlementSimpleInfoPageListRequest request) {
        return null;
    }

    @Override
    public boolean isReturnData() {

        return false;
    }

    private byte[] getFileByte(String dir, String fileName) {
        //  压缩文件
        try {
            File zipFile = FileUtil.newFile(fileName);
            if (null != zipFile) {
                return FileUtil.readBytes(zipFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(dir);
        }
        return new byte[0];
    }
}
