package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.bo.SplitOrderResultBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.order.order.dto.request.CreateOrderAddressRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderGoodsTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单拆单
 *
 * @author zhigang.guo
 * @date: 2022/4/26
 */
@Slf4j
public abstract class AbstractOrderSplitHandler {
    /**
     * 订单拆单
     *
     * @param t
     * @return
     */
    public SplitOrderResultBO splitOrder(SplitOrderContextBO t) {
        // 获取拆单购物车数据
        List<CartDO> cartDOList = this.getSplitCartList(t);

        if (CollectionUtil.isEmpty(cartDOList)) {

            return SplitOrderResultBO.builder().createOrderRequestList(Collections.emptyList()).build();
        }

        // copy 数据
        List<OrderSubmitRequest.DistributorOrderDTO> splitCartList = SplitOrderEnum.filterData(t.getRequest(), cartDOList);
        SplitOrderContextBO splitOrderContextBO = SplitOrderContextBO.builder().build();
        splitOrderContextBO.setAllEnterpriseList(t.getAllEnterpriseList());
        splitOrderContextBO.setCustomerDTOList(t.getCustomerDTOList());
        splitOrderContextBO.setAllGoodsDTOList(t.getAllGoodsDTOList());
        splitOrderContextBO.setYilingSubEids(t.getYilingSubEids());
        splitOrderContextBO.setIndustryDirectEids(t.getIndustryDirectEids());
        splitOrderContextBO.setOrderBatchNo(t.getOrderBatchNo());
        splitOrderContextBO.setContacterFunction(t.getContacterFunction());
        splitOrderContextBO.setOrderNoFunction(t.getOrderNoFunction());
        splitOrderContextBO.setPriceFunction(t.getPriceFunction());
        splitOrderContextBO.setInitOrderFunction(t.getInitOrderFunction());
        splitOrderContextBO.setDeliveryAddressDTO(t.getDeliveryAddressDTO());
        splitOrderContextBO.setProvinceManagerFunction(t.getProvinceManagerFunction());
        splitOrderContextBO.setAllCartDOList(cartDOList);
        splitOrderContextBO.setPromotionGoodsLimitDTOList(t.getPromotionGoodsLimitDTOList());
        splitOrderContextBO.setPresaleActivityGoodsDTOList(t.getPresaleActivityGoodsDTOList());
        OrderSubmitRequest request = this.copy(t.getRequest(), splitCartList);
        splitOrderContextBO.setRequest(request);

        SplitOrderResultBO splitOrderResultBO =  split(splitOrderContextBO);
        // 计算订单金额
        calculateOrderMoney(t.getPriceFunction(),splitOrderResultBO.getCreateOrderRequestList(),t.getRequest().getBuyerEid());
        // 校验订单金额是否出现异常
        checkOrderMoney(t.getRequest().getOrderTypeEnum(),splitOrderResultBO.getCreateOrderRequestList());

        return splitOrderResultBO;
    }

    /**
     * 订单拆单
     *
     * @param t
     * @return
     */
    protected abstract SplitOrderResultBO split(SplitOrderContextBO t);


    /**
     * 获取对应订单类型的购物车数据
     *
     * @return
     */
    protected abstract List<CartDO> getSplitCartList(SplitOrderContextBO t);


    /**
     * 计算订单金额
     *
     * @param priceFunction 获取价格函数
     * @param createOrderRequestList 订单信息
     * @param buyerEid 买家企业Eid
     */
    protected abstract void calculateOrderMoney(Function<QueryGoodsPriceRequest,Map<Long, GoodsPriceDTO>> priceFunction,List<CreateOrderRequest> createOrderRequestList, Long buyerEid);


    /**
     * 校验订单金额是否异常
     * @param orderTypeEnum 订单类型
     * @param createOrderRequestList 请求结果
     */
    protected  void checkOrderMoney(OrderTypeEnum orderTypeEnum, List<CreateOrderRequest> createOrderRequestList){
        Boolean orderMoneyCheckError = createOrderRequestList.stream().anyMatch(createOrderRequest -> CompareUtil.compare(createOrderRequest.getPaymentAmount(), BigDecimal.ZERO) <= 0);

        if (orderMoneyCheckError) {
            log.error("拆单订单金额异常,出现零元订单");
            throw new BusinessException(OrderErrorCode.ORDER_MONEY_ERROR);
        }
    }


    /**
     * 拷贝订单创建数据
     *
     * @param request
     * @param splitCartList
     * @return
     */
    private OrderSubmitRequest copy(OrderSubmitRequest request, List<OrderSubmitRequest.DistributorOrderDTO> splitCartList) {

        OrderSubmitRequest copyRequest = new OrderSubmitRequest();
        copyRequest.setOpUserId(request.getOpUserId());
        copyRequest.setOpTime(request.getOpTime());
        copyRequest.setDistributorOrderList(splitCartList);
        copyRequest.setAddressId(request.getAddressId());
        copyRequest.setContacterId(request.getContacterId());
        copyRequest.setDepartmentId(request.getDepartmentId());
        copyRequest.setEasAccount(request.getEasAccount());
        copyRequest.setBuyerEid(request.getBuyerEid());
        copyRequest.setOrderTypeEnum(request.getOrderTypeEnum());
        copyRequest.setOrderSourceEnum(request.getOrderSourceEnum());
        copyRequest.setPlatformCustomerCouponId(request.getPlatformCustomerCouponId());

        return copyRequest;
    }


    /**
     * 创建订单收货地址信息
     *
     * @param deliveryAddressDTO
     * @return
     */
    protected CreateOrderAddressRequest buildOrderAddressRequest(DeliveryAddressDTO deliveryAddressDTO) {

        CreateOrderAddressRequest createOrderAddressRequest = new CreateOrderAddressRequest();
        createOrderAddressRequest.setName(StringUtils.isBlank(deliveryAddressDTO.getReceiver()) ? "未填写收货人" : deliveryAddressDTO.getReceiver());
        createOrderAddressRequest.setMobile(deliveryAddressDTO.getMobile());
        createOrderAddressRequest.setAddressId(deliveryAddressDTO.getId());
        createOrderAddressRequest.setProvinceCode(deliveryAddressDTO.getProvinceCode());
        createOrderAddressRequest.setProvinceName(deliveryAddressDTO.getProvinceName());
        createOrderAddressRequest.setCityCode(deliveryAddressDTO.getCityCode());
        createOrderAddressRequest.setCityName(deliveryAddressDTO.getCityName());
        createOrderAddressRequest.setRegionCode(deliveryAddressDTO.getRegionCode());
        createOrderAddressRequest.setRegionName(deliveryAddressDTO.getRegionName());
        createOrderAddressRequest.setAddress(deliveryAddressDTO.getAddress());

        return createOrderAddressRequest;
    }

    /**
     * 构建订单明细基本请求request
     *
     * @param orderNo
     * @param cartDO
     * @param goodsDTO
     * @return
     */
    protected CreateOrderDetailRequest buildOrderDetailRequest(String orderNo, CartDO cartDO, GoodsSkuInfoDTO goodsDTO) {

        CreateOrderDetailRequest createOrderDetailRequest = new CreateOrderDetailRequest();
        createOrderDetailRequest.setOrderNo(orderNo);
        createOrderDetailRequest.setStandardId(goodsDTO.getGoodsInfo().getStandardId());
        createOrderDetailRequest.setDistributorGoodsId(cartDO.getDistributorGoodsId());
        createOrderDetailRequest.setGoodsId(cartDO.getGoodsId());
        createOrderDetailRequest.setGoodsCode(goodsDTO.getGoodsInfo().getGoodsCode());
        createOrderDetailRequest.setGoodsErpCode(goodsDTO.getInSn());
        createOrderDetailRequest.setGoodsName(goodsDTO.getGoodsInfo().getName());
        createOrderDetailRequest.setGoodsCommonName(goodsDTO.getGoodsInfo().getCommonName());
        createOrderDetailRequest.setGoodsLicenseNo(goodsDTO.getGoodsInfo().getLicenseNo());
        createOrderDetailRequest.setGoodsSpecification(goodsDTO.getGoodsInfo().getSellSpecifications());
        createOrderDetailRequest.setGoodsManufacturer(goodsDTO.getGoodsInfo().getManufacturer());
        createOrderDetailRequest.setGoodsQuantity(cartDO.getQuantity());
        createOrderDetailRequest.setGoodsSkuId(cartDO.getGoodsSkuId());
        createOrderDetailRequest.setCouponDiscountAmount(BigDecimal.ZERO);
        createOrderDetailRequest.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        createOrderDetailRequest.setDepositAmount(BigDecimal.ZERO);
        createOrderDetailRequest.setCashDiscountAmount(BigDecimal.ZERO);
        createOrderDetailRequest.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        createOrderDetailRequest.setShopPaymentDiscountAmount(BigDecimal.ZERO);
        // 赋值商品类型
        createOrderDetailRequest.setGoodsType(createOrderDetailRequest.getDistributorGoodsId().equals(createOrderDetailRequest.getGoodsId()) ? OrderGoodsTypeEnum.NORMAL.getCode() : OrderGoodsTypeEnum.YLGOODS.getCode());
        return createOrderDetailRequest;
    }

    /**
     * 获取订单商品价格
     * @param priceFunction 获取价格函数
     * @param buyerEid 买家eid
     * @param distributorGoodsIds 商品Id
     * @return
     */
    protected Map<Long, GoodsPriceDTO> getGoodsPriceDto(Function<QueryGoodsPriceRequest,Map<Long, GoodsPriceDTO>> priceFunction,Long buyerEid, List<Long> distributorGoodsIds) {
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(distributorGoodsIds);
        if (log.isDebugEnabled()) {
            log.debug("getGoodsPriceDto..queryGoodsPriceRequest:{}",queryGoodsPriceRequest);
        }
        return priceFunction.apply(queryGoodsPriceRequest);
    }

}
