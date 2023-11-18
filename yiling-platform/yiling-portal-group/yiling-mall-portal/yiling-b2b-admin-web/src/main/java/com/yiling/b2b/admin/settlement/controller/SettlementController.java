package com.yiling.b2b.admin.settlement.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.settlement.form.QueryOrderDetailPageListForm;
import com.yiling.b2b.admin.settlement.form.QuerySettlementDetailPageListForm;
import com.yiling.b2b.admin.settlement.form.QuerySettlementOrderDetailPageListForm;
import com.yiling.b2b.admin.settlement.form.QuerySettlementOrderPageListForm;
import com.yiling.b2b.admin.settlement.form.QuerySettlementPageListForm;
import com.yiling.b2b.admin.settlement.vo.OrderDetailListItemVO;
import com.yiling.b2b.admin.settlement.vo.OrderDetailPageVO;
import com.yiling.b2b.admin.settlement.vo.SettlementDetailPageListItemVO;
import com.yiling.b2b.admin.settlement.vo.SettlementDetailPageVO;
import com.yiling.b2b.admin.settlement.vo.SettlementOrderDetailPageVO;
import com.yiling.b2b.admin.settlement.vo.SettlementOrderVO;
import com.yiling.b2b.admin.settlement.vo.SettlementPageListItemVO;
import com.yiling.b2b.admin.settlement.vo.SettlementPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.dto.B2BSettlementDetailDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.api.SettlementDetailApi;
import com.yiling.settlement.b2b.api.SettlementOrderApi;
import com.yiling.settlement.b2b.api.SettlementOrderSyncApi;
import com.yiling.settlement.b2b.dto.SettlementAmountInfoDTO;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.SettlementDetailDTO;
import com.yiling.settlement.b2b.dto.SettlementOrderDTO;
import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementDetailPageListRequest;
import com.yiling.settlement.b2b.dto.request.QuerySettlementOrderPageListRequest;
import com.yiling.settlement.b2b.dto.request.QuerySettlementPageListRequest;
import com.yiling.settlement.b2b.enums.SettlementTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-10-27
 */
@Api(tags = "结算单模块")
@RestController
@RequestMapping("/settlement")
@Slf4j
public class SettlementController extends BaseController {

	@DubboReference
	SettlementApi       settlementApi;
	@DubboReference
	SettlementDetailApi settlementDetailApi;
	@DubboReference
	EnterpriseApi       enterpriseApi;
	@DubboReference
	OrderB2BApi         orderB2BApi;
    @DubboReference
    OrderApi orderApi;
	@DubboReference
	SettlementOrderApi  settlementOrderApi;
	@DubboReference
	PromotionActivityApi promotionActivityApi;
    @DubboReference
    SettlementOrderSyncApi settlementOrderSyncApi;
    @DubboReference
    PresaleActivityApi presaleActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;


	@ApiOperation("查询结算单列表")
	@PostMapping("/querySettlementPageList")
	public Result<SettlementPageVO<SettlementPageListItemVO>> querySettlementPageList(
			@RequestBody @Valid QuerySettlementPageListForm form, @CurrentUser CurrentStaffInfo staffInfo) {

		SettlementPageVO<SettlementPageListItemVO> result;
		//查询结算统计
		SettlementAmountInfoDTO amountInfoDTO = settlementApi.querySettlementAmountInfo(staffInfo.getCurrentEid());
		result = PojoUtils.map(amountInfoDTO, SettlementPageVO.class);

		QuerySettlementPageListRequest request = PojoUtils.map(form, QuerySettlementPageListRequest.class);
		request.setEid(staffInfo.getCurrentEid());

		//分页查询结算单
		Page<SettlementDTO> page = settlementApi.querySettlementPageList(request);
		List<SettlementDTO> records = page.getRecords();
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}
		PojoUtils.map(page, result);
		List<SettlementPageListItemVO> pageItemList = PojoUtils.map(records, SettlementPageListItemVO.class);
		result.setRecords(pageItemList);

		return Result.success(result);
	}

	@ApiOperation("查询结算单明细列表")
	@PostMapping("/querySettlementDetailPageList")
	public Result<SettlementDetailPageVO<SettlementDetailPageListItemVO>> querySettlementDetailPageList(
			@RequestBody @Valid QuerySettlementDetailPageListForm form) {
		SettlementDetailPageVO result;

		SettlementDTO settlementDTO = settlementApi.getById(form.getSettlementId());
		if (ObjectUtil.isNull(settlementDTO)) {
			return Result.failed("结算单不存在");
		}
		result = PojoUtils.map(settlementDTO, SettlementDetailPageVO.class);

		//查询结算单明细
		QuerySettlementDetailPageListRequest request = PojoUtils.map(form, QuerySettlementDetailPageListRequest.class);
		Page<SettlementDetailDTO> page = settlementDetailApi.querySettlementDetailPageList(request);
		List<SettlementDetailDTO> records = page.getRecords();
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}
		PojoUtils.map(page, result);
		List<SettlementDetailPageListItemVO> pageList = PojoUtils.map(records, SettlementDetailPageListItemVO.class);
		List<Long> sellerEidList = pageList.stream().map(SettlementDetailPageListItemVO::getBuyerEid).collect(Collectors.toList());
		//补全采购商
		Map<Long, String> entMap = enterpriseApi.listByIds(sellerEidList).stream()
				.collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
		pageList.forEach(e -> e.setBuyerName(entMap.getOrDefault(e.getBuyerEid(), "")));
		result.setRecords(pageList);
		return Result.success(result);
	}


	@ApiOperation("结算单---查询订单明细列表")
	@PostMapping("/queryOrderDetailPageList")
	public Result<OrderDetailPageVO> queryOrderDetailPageList(
			@RequestBody @Valid QueryOrderDetailPageListForm form) {

		OrderDetailPageVO result = new OrderDetailPageVO();

		//查询结算单明细
		SettlementDetailDTO settlementDetail = settlementDetailApi.querySettlementDetailByOrderId(form.getOrderId())
				.stream().filter(e -> ObjectUtil.equal(e.getSettlementId(), form.getSettlementId())).collect(Collectors.toList()).stream().findFirst().get();
		if (ObjectUtil.isNull(settlementDetail)) {
			return Result.failed("结算单明细不存在");
		}
		//查询结算单
		SettlementDTO settlementDTO = settlementApi.getById(settlementDetail.getSettlementId());

		if (ObjectUtil.isNull(settlementDTO)) {
			return Result.failed("结算单不存在");
		}
		result.setType(settlementDTO.getType());
		result.setStatus(settlementDTO.getStatus());
		result.setCreateTime(settlementDTO.getCreateTime());
		result.setSettlementTime(settlementDTO.getSettlementTime());
		//设置结算金额明细
		if (ObjectUtil.equal(settlementDTO.getType(),SettlementTypeEnum.GOODS.getCode())){
			result.setGoodsAmount(settlementDetail.getGoodsAmount());
			result.setRefundGoodsAmount(settlementDetail.getRefundGoodsAmount());
			result.setGoodsSettlementAmount(settlementDetail.getAmount());
		}else if (ObjectUtil.equal(settlementDTO.getType(), SettlementTypeEnum.SALE.getCode())){
			result.setCouponAmount(settlementDetail.getCouponAmount());
			result.setRefundCouponAmount(settlementDetail.getRefundCouponAmount());
			result.setPromotionAmount(settlementDetail.getPromotionAmount());
			result.setRefundPromotionAmount(settlementDetail.getRefundPromotionAmount());
			result.setGiftAmount(settlementDetail.getGiftAmount());
			result.setRefundGiftAmount(settlementDetail.getRefundGiftAmount());
            result.setComPacAmount(settlementDetail.getComPacAmount());
            result.setRefundComPacAmount(settlementDetail.getRefundComPacAmount());
            result.setPresaleDiscountAmount(settlementDetail.getPresaleDiscountAmount());
            result.setRefundPreAmount(settlementDetail.getRefundPreAmount());
            result.setPayDiscountAmount(settlementDetail.getPayDiscountAmount());
            result.setRefundPayAmount(settlementDetail.getRefundPayAmount());
			result.setSaleSettlementAmount(settlementDetail.getAmount());
		}else {
            result.setPresaleDefaultAmount(settlementDTO.getPresaleDefaultAmount());
            result.setPreDefSettlementAmount(settlementDetail.getAmount());
        }
		//查询订单详情
		List<B2BSettlementDetailDTO> orderDetailList = orderB2BApi.listSettleOrderDetailByOrderId(form.getOrderId());
		List<OrderDetailListItemVO> detailVOS = PojoUtils.map(orderDetailList, OrderDetailListItemVO.class);
        //查询同步订单
//        SettlementOrderSyncDTO syncDTO = settlementOrderSyncApi.querySettOrderSyncByOrderCode(orderDetailList.get(0).getOrderNo());
		OrderDTO order = orderApi.getOrderInfo(orderDetailList.get(0).getOrderId());
		//补全订单明细结算金额
		supplementOrderDetail(detailVOS,order);
        //如果存在预售违约金则强制把订单的违约金放入到第一个商品明细中
        if (result.getPresaleDefaultAmount().compareTo(BigDecimal.ZERO)>0){
            OrderDetailListItemVO itemVO = detailVOS.get(0);
            if (ObjectUtil.isNotNull(itemVO)){
                itemVO.setPresaleDefaultAmount(settlementDetail.getPresaleDefaultAmount());
                itemVO.setRefundPresaleDefaultAmount(BigDecimal.ZERO);
                itemVO.setPreDefSettlementAmount(settlementDetail.getPresaleDefaultAmount());
            }
        }

		result.setOrderDetailList(detailVOS);
		result.setOrderNo(orderDetailList.get(0).getOrderNo());
		result.setPaymentMethod(orderDetailList.get(0).getPaymentMethod());
		result.setOrderCreateTime(orderDetailList.get(0).getCreateTime());
		result.setPayChannel(orderDetailList.get(0).getPayChannel());

		return Result.success(result);
	}

	@ApiOperation("订单对账---查询订单明细列表")
	@PostMapping("/querySettlementOrderDetailPageList")
	public Result<SettlementOrderDetailPageVO> querySettlementOrderDetailPageList(
			@RequestBody @Valid QuerySettlementOrderDetailPageListForm form) {

		SettlementOrderDetailPageVO result = new SettlementOrderDetailPageVO();

		//查询结算单明细
		List<SettlementDetailDTO> settlementDetailList = settlementDetailApi.querySettlementDetailByOrderId(form.getOrderId());
		if (CollUtil.isEmpty(settlementDetailList)) {
			return Result.failed("结算单明细不存在");
		}
		Map<Long, SettlementDTO> settlementMap = settlementApi.getByIdList(settlementDetailList.stream()
				.map(SettlementDetailDTO::getSettlementId).collect(Collectors.toList())).stream()
				.collect(Collectors.toMap(SettlementDTO::getId, e -> e));


		//设置结算金额明细
		settlementDetailList.forEach(e -> {
			SettlementDTO settlement = settlementMap.get(e.getSettlementId());
			if (ObjectUtil.equal(settlement.getType(), SettlementTypeEnum.GOODS.getCode())) {
				result.setGoodsAmount(e.getGoodsAmount());
				result.setRefundGoodsAmount(e.getRefundGoodsAmount());
				result.setGoodsSettlementAmount(e.getAmount());
				result.setGoodsStatus(settlement.getStatus());
				result.setGoodsCreateTime(settlement.getCreateTime());
				result.setGoodsSettlementTime(settlement.getSettlementTime());
			} else if (ObjectUtil.equal(settlement.getType(), SettlementTypeEnum.SALE.getCode())){
				result.setCouponAmount(e.getCouponAmount());
				result.setRefundCouponAmount(e.getRefundCouponAmount());
				result.setPromotionAmount(e.getPromotionAmount());
				result.setRefundPromotionAmount(e.getRefundPromotionAmount());
				result.setGiftAmount(e.getGiftAmount());
				result.setRefundGiftAmount(e.getRefundGiftAmount());
                result.setComPacAmount(e.getComPacAmount());
                result.setReturnComPacAmount(e.getRefundComPacAmount());
                result.setPresaleDiscountAmount(e.getPresaleDiscountAmount());
                result.setRefundPreAmount(e.getRefundPreAmount());
                result.setPayDiscountAmount(e.getPayDiscountAmount());
                result.setRefundPayAmount(e.getRefundPayAmount());
				result.setSaleSettlementAmount(e.getAmount());
				result.setSaleStatus(settlement.getStatus());
				result.setSaleCreateTime(settlement.getCreateTime());
				result.setSaleSettlementTime(settlement.getSettlementTime());
			}else {
                result.setPresaleDefaultAmount(e.getPresaleDefaultAmount());
                result.setPresaleDefaultStatus(settlement.getStatus());
                result.setPreSaleCreateTime(settlement.getCreateTime());
                result.setPreSaleSettlementTime(settlement.getSettlementTime());
            }
		});
		//查询订单详情
		List<B2BSettlementDetailDTO> orderDetailList = orderB2BApi.listSettleOrderDetailByOrderId(form.getOrderId());
		List<OrderDetailListItemVO> detailVOS = PojoUtils.map(orderDetailList, OrderDetailListItemVO.class);
        //查询同步订单
//        SettlementOrderSyncDTO syncDTO = settlementOrderSyncApi.querySettOrderSyncByOrderCode(orderDetailList.get(0).getOrderNo());
		OrderDTO order = orderApi.getOrderInfo(orderDetailList.get(0).getOrderId());
		//补全订单明细结算金额
		supplementOrderDetail(detailVOS,order);
        //如果存在预售违约金则强制把订单的违约金放入到第一个商品明细中
        if (result.getPresaleDefaultAmount().compareTo(BigDecimal.ZERO)>0){
            OrderDetailListItemVO itemVO = detailVOS.get(0);
            if (ObjectUtil.isNotNull(itemVO)){
                SettlementOrderDTO orderDTO = settlementOrderApi.querySettOrderByOrderId(form.getOrderId());
                if (ObjectUtil.isNotNull(orderDTO)){
                    itemVO.setPresaleDefaultAmount(orderDTO.getPresaleDefaultAmount());
                    itemVO.setRefundPresaleDefaultAmount(BigDecimal.ZERO);
                    itemVO.setPreDefSettlementAmount(orderDTO.getPresaleDefaultAmount());
                }
            }
        }

		result.setOrderDetailList(detailVOS);
		result.setOrderNo(orderDetailList.get(0).getOrderNo());
		result.setPaymentMethod(orderDetailList.get(0).getPaymentMethod());
        OrderDTO orderInfo = orderApi.getOrderInfo(orderDetailList.get(0).getOrderId());
        result.setSellerEname(orderInfo.getSellerEname());
        result.setBuyerEname(orderInfo.getBuyerEname());
		result.setPayChannel(orderDetailList.get(0).getPayChannel());
        result.setOrderCreateTime(orderDetailList.get(0).getCreateTime());

		return Result.success(result);
	}

	@ApiOperation("查询订单对账列表")
	@PostMapping("/querySettlementOrderPageList")
	public Result<SettlementPageVO<SettlementOrderVO>> querySettlementOrderPageList(
			@RequestBody @Valid QuerySettlementOrderPageListForm form, @CurrentUser CurrentStaffInfo staffInfo) {
		SettlementPageVO<SettlementOrderVO> result;

		//查询结算统计
		SettlementAmountInfoDTO amountInfoDTO = settlementApi.querySettlementAmountInfo(staffInfo.getCurrentEid());
		result = PojoUtils.map(amountInfoDTO, SettlementPageVO.class);

		QuerySettlementOrderPageListRequest request = PojoUtils.map(form, QuerySettlementOrderPageListRequest.class);
		request.setSellerEid(staffInfo.getCurrentEid());
		Page<SettlementOrderDTO> page = settlementOrderApi.querySettlementOrderPageList(request);

		PojoUtils.map(page, result);

		if (CollUtil.isNotEmpty(page.getRecords())) {
			List<SettlementOrderVO> records = PojoUtils.map(page.getRecords(), SettlementOrderVO.class);
			List<Long> entIdList = ListUtil.toList();
			entIdList.addAll(records.stream().map(SettlementOrderVO::getBuyerEid).collect(Collectors.toList()));
			entIdList = entIdList.stream().distinct().collect(Collectors.toList());
			//查询商家名称
			Map<Long, String> entMap = enterpriseApi.listByIds(entIdList).stream()
					.collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
			records.forEach(e -> {
				e.setBuyerName(entMap.getOrDefault(e.getBuyerEid(), ""));
			});
			result.setRecords(records);
		}

		return Result.success(result);
	}

	/**
	 * 补充订单明细的结算金额
	 *
	 * @param request
	 * @param order
	 * @return
	 */
	private void supplementOrderDetail(List<OrderDetailListItemVO> request, OrderDTO order) {
		//查询秒杀特价活动百分比
		List<Long> promotionActivityIds = request.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.FULL_GIFT.getCode())||ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode())||ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode())||ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode()))
				.map(OrderDetailListItemVO::getPromotionActivityId).distinct().collect(Collectors.toList());

        Map<Long, PromotionActivityDTO> activityDTOMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(promotionActivityIds)){
            activityDTOMap=promotionActivityApi.batchQueryByIdList(promotionActivityIds).stream().collect(Collectors.toMap(PromotionActivityDTO::getId, e -> e));
        }
        //查询订单的活动信息
        Map<Long, List<OrderPromotionActivityDTO>> orderActivityInfoMap= MapUtil.newHashMap();
        List<Long> orderIds = request.stream().map(OrderDetailListItemVO::getOrderId).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(orderIds)) {
            orderActivityInfoMap = orderPromotionActivityApi.listByOrderIds(orderIds).stream().map(e->{e.setActivityPlatformPercent(e.getActivityPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN)); return e;}).collect(Collectors.groupingBy(OrderPromotionActivityDTO::getOrderId));
        }
        List<Long> preSaleActivityIds = request.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.PRESALE.getCode())).map(OrderDetailListItemVO::getPromotionActivityId).distinct().collect(Collectors.toList());
        Map<Long, PresaleActivityDTO> preSaleActivityDTOMap;
        if (CollUtil.isNotEmpty(preSaleActivityIds)){
            preSaleActivityDTOMap = presaleActivityApi.batchQueryByIdList(preSaleActivityIds);
        }else {
            preSaleActivityDTOMap= MapUtil.newHashMap();
        }
        Map<Long, PromotionActivityDTO> finalActivityDTOMap = activityDTOMap;
        Map<Long, List<OrderPromotionActivityDTO>> finalOrderActivityInfoMap = orderActivityInfoMap;
        request.forEach(orderDetail -> {
			//统计货款金额相关
			//线下支付不计算货款金额
			if (ObjectUtil.notEqual(orderDetail.getPaymentMethod().toString(), PaymentMethodEnum.OFFLINE.getCode().toString())&&ObjectUtil.notEqual(order.getOrderStatus(), OrderStatusEnum.CANCELED.getCode())) {
				//货款金额=商品小计金额-平台优惠-优惠券优惠-预售促销金额-支付促销金额
				BigDecimal goodsAmount = orderDetail.getGoodsAmount()
                        .subtract(orderDetail.getPlatformCouponDiscountAmount())
						.subtract(orderDetail.getCouponDiscountAmount())
                        .subtract(orderDetail.getPresaleDiscountAmount())
                        .subtract(orderDetail.getPaymentPlatformDiscountAmount().add(orderDetail.getPaymentShopDiscountAmount()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
				orderDetail.setGoodsTotalAmount(goodsAmount);
				//货款退款金额=退款金额-平台券退款金额-商家券退款金额-预售退款金额-支付促销平台退款金额-支付促销商家退款金额
				BigDecimal returnGoodsAmount = orderDetail.getReturnAmount()
						.subtract(orderDetail.getReturnPlatformCouponDiscountAmount())
                        .subtract(orderDetail.getReturnCouponDiscountAmount())
                        .subtract(orderDetail.getReturnPresaleDiscountAmount())
                        .subtract(orderDetail.getReturnPlatformPaymentDiscountAmount().add(orderDetail.getReturnShopPaymentDiscountAmount()))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				orderDetail.setGoodsRefundAmount(returnGoodsAmount);
				//货款应结算金额=货款金额-货款退款金额
				orderDetail.setGoodsSettlementAmount(goodsAmount.subtract(returnGoodsAmount));
			}
			//统计优惠券金额
            //优惠券金额
            BigDecimal couponSalesAmount;
            //优惠券退款金额
            BigDecimal couponReturnSalesAmount = BigDecimal.ZERO;

			//平台券补贴额度=平台券使用额度*平台券平台占比
			BigDecimal platformAmount = orderDetail.getPlatformCouponDiscountAmount()
					.multiply(orderDetail.getPlatformRatio() == null ? BigDecimal.ZERO :
							orderDetail.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
			//商家券平台补贴额度=商家券使用额度*商家券平台占比
			BigDecimal shopAmount = orderDetail.getCouponDiscountAmount()
					.multiply(orderDetail.getShopPlatformRatio() == null ? BigDecimal.ZERO :
							orderDetail.getShopPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
			//促销金额=平台券补贴额度+商家券平台补贴额度
			couponSalesAmount = platformAmount.add(shopAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

			//如果优惠券总额大于0则计算优惠券退款金额
			if (couponSalesAmount.compareTo(BigDecimal.ZERO)==1){
                //统计优惠券退款金额
                //退回平台券补贴额度=退回平台券使用额度*平台券平台占比
                BigDecimal returnPlatformAmount = orderDetail.getReturnPlatformCouponDiscountAmount()
                        .multiply(orderDetail.getPlatformRatio() == null ? BigDecimal.ZERO
                                : orderDetail.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                //退回商家券平台补贴额度=退回商家券使用额度*商家券平台占比
                BigDecimal returnShopAmount = orderDetail.getReturnCouponDiscountAmount()
                        .multiply(orderDetail.getShopPlatformRatio() == null ? BigDecimal.ZERO :
                                orderDetail.getShopPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                //退回优惠券金额=退回平台券补贴额度+退回商家券平台补贴额度
                couponReturnSalesAmount = returnPlatformAmount.add(returnShopAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            orderDetail.setCouponAmount(couponSalesAmount);
            orderDetail.setCouponRefundAmount(couponReturnSalesAmount);;

			//秒杀特价金额
			BigDecimal promotionSaleSubTotal=BigDecimal.ZERO;
			//秒杀特价退货金额
			BigDecimal returnPromotionSaleSubTotal=BigDecimal.ZERO;

            //如果涉及秒杀&特价结算
            if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode()) || ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode())) {
                promotionSaleSubTotal=orderDetail.getPromotionSaleSubTotal();
                returnPromotionSaleSubTotal=orderDetail.getReturnPromotionSaleSubTotal();
                //如果本单的秒杀&特价结算金额大于0的时候，则计算实际的秒杀&特价金额，否则商品明细的秒杀特价结算金额记为0
                if (promotionSaleSubTotal.compareTo(BigDecimal.ZERO)==1){
                    PromotionActivityDTO activityDTO = finalActivityDTOMap.get(orderDetail.getPromotionActivityId());
                    if (ObjectUtil.isNotNull(activityDTO)) {
                        BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                        promotionSaleSubTotal = promotionSaleSubTotal.multiply(platformPercentage);
                        returnPromotionSaleSubTotal = returnPromotionSaleSubTotal.multiply(platformPercentage);
                    }
                }else {
                    promotionSaleSubTotal = BigDecimal.ZERO;
                    returnPromotionSaleSubTotal = BigDecimal.ZERO;
                }
            }
			orderDetail.setPromotionSaleSubTotal(promotionSaleSubTotal);
			orderDetail.setReturnPromotionSaleSubTotal(returnPromotionSaleSubTotal);

            //组合促销优惠金额
            BigDecimal comPacAmount= BigDecimal.ZERO;
            //退回组合促销优惠的金额
            BigDecimal refundComPacAmount= BigDecimal.ZERO;
            //如果涉及组合包结算
            if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())) {
                comPacAmount=orderDetail.getComPacAmount();
                refundComPacAmount=orderDetail.getReturnComPacAmount();
                //如果本单的组合包结算金额大于0的时候，则计算实际的组合包金额，否则商品明细的组合包结算金额记为0
                if (comPacAmount.compareTo(BigDecimal.ZERO)==1){
                    PromotionActivityDTO activityDTO = finalActivityDTOMap.get(orderDetail.getPromotionActivityId());
                    if (ObjectUtil.isNotNull(activityDTO)) {
                        BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                        comPacAmount = comPacAmount.multiply(platformPercentage);
                        refundComPacAmount = refundComPacAmount.multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }else {
                    comPacAmount = BigDecimal.ZERO;
                    refundComPacAmount = BigDecimal.ZERO;
                }
            }

            orderDetail.setComPacAmount(comPacAmount);
            orderDetail.setReturnComPacAmount(refundComPacAmount);

            //预售促销优惠金额
            BigDecimal preSaleAmount= BigDecimal.ZERO;
            //退回预售促销优惠的金额
            BigDecimal refundPreSaleAmount= BigDecimal.ZERO;
            //支付促销优惠金额
            BigDecimal paySaleAmount = BigDecimal.ZERO;
            //退回支付促销优惠的金额
            BigDecimal refundPaySaleAmount = BigDecimal.ZERO;

            SettlementOrderDTO settlementOrderDTO = settlementOrderApi.querySettOrderByOrderId(orderDetail.getOrderId());
            orderDetail.setTotalAmount(settlementOrderDTO.getTotalAmount());

            //计算预售促销金额
            if (ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementId(), 0L) && ObjectUtil.notEqual(settlementOrderDTO.getSaleSettlementNo(), "")) {
                //如果商品的结算金额小于等于0 则忽略该商品
                if (BigDecimal.ZERO.compareTo(orderDetail.getPresaleDiscountAmount()) == -1){
                    PresaleActivityDTO activityDTO = preSaleActivityDTOMap.get(orderDetail.getPromotionActivityId());
                    BigDecimal platformPercentage;
                    if (ObjectUtil.equal(activityDTO.getBear(), CouponBearTypeEnum.BUSINESS.getCode())){
                        platformPercentage=BigDecimal.ZERO;
                    }else {
                        platformPercentage = activityDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                    }

                    preSaleAmount = orderDetail.getPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //退款的组合包促销金额
                    refundPreSaleAmount = orderDetail.getReturnPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                }else {
                    preSaleAmount = BigDecimal.ZERO;
                    refundPreSaleAmount = BigDecimal.ZERO;
                }
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
                }else {
                    paySaleAmount = BigDecimal.ZERO;
                    refundPaySaleAmount = BigDecimal.ZERO;
                }
            }
            orderDetail.setPresaleDiscountAmount(preSaleAmount);
            orderDetail.setReturnPresaleDiscountAmount(refundPreSaleAmount);
            orderDetail.setPaymentPlatformDiscountAmount(paySaleAmount);
            orderDetail.setReturnPlatformPaymentDiscountAmount(refundPaySaleAmount);

			//促销结算金额=（优惠券金额-优惠券退款金额）+（秒杀特价金额-秒杀特价退款金额）+（满赠金额-满赠退款金额）+（组合促金额-组合退款金额）+（预售促销金额-预售促销退款金额）+（支付促销优惠金额-退回支付促销优惠的金额）
			orderDetail.setSaleSettlementAmount(couponSalesAmount.subtract(couponReturnSalesAmount)
					.add(promotionSaleSubTotal.subtract(returnPromotionSaleSubTotal))
                    .add(comPacAmount.subtract(refundComPacAmount))
                    .add(preSaleAmount.subtract(refundPreSaleAmount))
                    .add(paySaleAmount.subtract(refundPaySaleAmount)));
		});
	}


}
