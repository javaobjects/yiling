package com.yiling.sales.assistant.app.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.ValidateUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.request.ListCartRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.mall.customer.api.CustomerSearchApi;
import com.yiling.mall.customer.dto.request.CustomerVerificationRequest;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.api.SaOrderApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderConfirmRequest;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.dto.request.PopOrderConfirmRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListDetailRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetGoodsDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderTicketDiscountApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderAttachmentDTO;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.request.QueryAssistanOrderPageRequest;
import com.yiling.order.order.dto.request.UpdateCustomerConfirmStatusRequest;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.SaOrderStatusEnum;
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.sales.assistant.app.cart.util.SimpleGoodInfoUtils;
import com.yiling.sales.assistant.app.cart.util.StockUtils;
import com.yiling.sales.assistant.app.deliveryAddress.vo.DeliveryAddressVO;
import com.yiling.sales.assistant.app.order.form.CheckCouponForm;
import com.yiling.sales.assistant.app.order.form.GetConfirmVerifyCodeForm;
import com.yiling.sales.assistant.app.order.form.OrderConfirmForm;
import com.yiling.sales.assistant.app.order.form.OrderSubmitForm;
import com.yiling.sales.assistant.app.order.form.QueryOrderDetailForm;
import com.yiling.sales.assistant.app.order.form.QueryOrderPageForm;
import com.yiling.sales.assistant.app.order.form.ShareOrderProductInfoForm;
import com.yiling.sales.assistant.app.order.vo.AssistanQueryOrderVO;
import com.yiling.sales.assistant.app.order.vo.B2bOrderInfoVO;
import com.yiling.sales.assistant.app.order.vo.ConfirmOrderResultVO;
import com.yiling.sales.assistant.app.order.vo.CouponActivityCanUseDetailVO;
import com.yiling.sales.assistant.app.order.vo.CustomerSendOrderVO;
import com.yiling.sales.assistant.app.order.vo.OrderAddressVO;
import com.yiling.sales.assistant.app.order.vo.OrderConfirmVO;
import com.yiling.sales.assistant.app.order.vo.OrderDeliveryBatchVO;
import com.yiling.sales.assistant.app.order.vo.OrderDetailVO;
import com.yiling.sales.assistant.app.order.vo.OrderGoodsVO;
import com.yiling.sales.assistant.app.order.vo.OrderInfoVO;
import com.yiling.sales.assistant.app.order.vo.OrderPayVO;
import com.yiling.sales.assistant.app.order.vo.OrderSettlementPageVO;
import com.yiling.sales.assistant.app.order.vo.OrderTicketDiscountVO;
import com.yiling.sales.assistant.app.order.vo.PopOrderInfoVO;
import com.yiling.sales.assistant.app.order.vo.ShareOrderResultVO;
import com.yiling.sales.assistant.app.order.vo.SubmitResultVO;
import com.yiling.sales.assistant.app.payment.form.PaymentForm;
import com.yiling.sales.assistant.app.payment.form.QueryReceiptDeskOrderListForm;
import com.yiling.sales.assistant.app.rebate.vo.OrderDistributorVO;
import com.yiling.sales.assistant.app.system.enums.LoginErrorCode;
import com.yiling.sales.assistant.app.util.PictureUrlUtils;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.request.QueryTaskGoodsMatchRequest;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchListDTO;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserDeregisterAccountDTO;
import com.yiling.user.system.enums.UserDeregisterAccountStatusEnum;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手订单模块
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.controller
 * @date: 2021/9/16
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块")
@Slf4j
public class SaOrderController extends BaseController {
    @DubboReference
    SaOrderApi                          saOrderApi;
    @DubboReference
    OrderApi                            orderApi;
    @DubboReference
    CustomerSearchApi                   customerSearchApi;
    @DubboReference
    OrderAddressApi                     orderAddressApi;
    @DubboReference
    OrderDetailApi                      orderDetailApi;
    @DubboReference
    OrderDetailChangeApi                orderDetailChangeApi;
    @DubboReference
    EnterprisePurchaseRelationApi       enterprisePurchaseRelationApi;
    @DubboReference
    AgreementBusinessApi                agreementBusinessApi;
    @DubboReference
    EnterpriseApi                       enterpriseApi;
    @DubboReference
    CartApi                             cartApi;
    @DubboReference
    GoodsApi                            goodsApi;
    @DubboReference
    InventoryApi                        inventoryApi;
    @DubboReference
    GoodsPriceApi                       goodsPriceApi;
    @DubboReference
    DeliveryAddressApi                  deliveryAddressApi;
    @DubboReference
    CustomerApi                         customerApi;
    @DubboReference
    OrderProcessApi                     orderProcessApi;
    @DubboReference
    UserApi                             userApi;
    @DubboReference
    PaymentMethodApi                    paymentMethodApi;
    @DubboReference
    CouponActivityApi                   couponActivityApi;
    @DubboReference
    PaymentDaysAccountApi               paymentDaysAccountApi;
    @DubboReference
    ShopApi                             shopApi;
    @DubboReference
    OrderTicketDiscountApi              orderTicketDiscountApi;
    @DubboReference
    OrderDeliveryApi                    orderDeliveryApi;
    @DubboReference
    PayApi                              payApi;
    @DubboReference
    OrderCouponUseApi                   orderCouponUseApi;
    @DubboReference
    DepartmentApi                       departmentApi;
    @DubboReference
    TaskApi                             taskApi;
    @DubboReference
    SmsApi                              smsApi;
    @DubboReference
    StaffApi                            staffApi;
    @DubboReference
    UserDeregisterAccountApi            userDeregisterAccountApi;
    @DubboReference
    EmployeeApi                         employeeApi;
    @DubboReference
    ProcurementRelationGoodsApi         procurementRelationGoodsApi;
    @Autowired
    private FileService                 fileService;
    @Autowired
    private PictureUrlUtils             pictureUrlUtils;
    @Autowired
    private RedisService                redisService;
    @Autowired
    HttpServletRequest                  httpRequest;


    /**
     * 查询企业收货地址信息，
     *
     * @param eid 企业ID
     * @return
     */
    private DeliveryAddressVO selectDeliveryAddressInfoList(Long eid) {

        QueryDeliveryAddressRequest deliveryAddressRequest = new QueryDeliveryAddressRequest();
        deliveryAddressRequest.setEid(eid);

        List<DeliveryAddressDTO> deliverAddressList = deliveryAddressApi.selectDeliveryAddressList(deliveryAddressRequest);

        if (CollUtil.isEmpty(deliverAddressList)) {

            return new DeliveryAddressVO();
        }
        List<DeliveryAddressDTO> collect = deliverAddressList.stream().filter(e -> e.getDefaultFlag() == 1).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(collect)) {
            return new DeliveryAddressVO();
        }
        return PojoUtils.map(collect.get(0), DeliveryAddressVO.class);
    }

    /**
     * 以岭直购，以及一级商向工业直属采购需要展示EAS企业账号
     * 查询是否展示企业信息
     *
     * @param yilingSubEids   以岭企业ID信息
     * @param distributorEids 配送商企业信息ID
     * @param isYilingCutomer 是否为以岭客户代客下单
     * @return
     */
    private boolean showEasAccountInfoFlag(Boolean isYilingCutomer, List<Long> yilingSubEids, List<Long> distributorEids) {
        if (!isYilingCutomer) {
            return false;
        }
        if (yilingSubEids.stream().anyMatch(eid -> distributorEids.contains(eid))) {

            return true;
        }
        // 查询工业直属的企业ID信息
        List<Long> industryDirectEids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);

        return industryDirectEids.stream().anyMatch(eid -> distributorEids.contains(eid));
    }

    /**
     * 查询企业账号信息
     *
     * @param showEasAccountInfoFlag 是否展示企业信息
     * @param currentEid             当前登录企业ID
     * @param isYilingCutomer        是否为以岭客户代客下单
     * @return
     */
    private List<OrderSettlementPageVO.EasAccountVO> selectEasAccountList(Boolean isYilingCutomer, Boolean showEasAccountInfoFlag, Long currentEid) {

        if (!isYilingCutomer || !showEasAccountInfoFlag) {

            return ListUtil.empty();
        }
        List<EnterpriseCustomerEasDTO> enterpriseCustomerEasDTOList = customerApi.getCustomerEasInfos(Constants.YILING_EID, currentEid);
        return enterpriseCustomerEasDTOList.stream().map(e -> {
            OrderSettlementPageVO.EasAccountVO easAccountDto = new OrderSettlementPageVO.EasAccountVO();
            easAccountDto.setEname(e.getEasName());
            easAccountDto.setAccount(e.getEasCode());
            return easAccountDto;
        }).collect(Collectors.toList());
    }

    /**
     * 校验库存是否充足
     * @param allCartDOList
     */
    private void validateGoodsInventory(List<CartDTO> allCartDOList) {
        List<Long> goodSkuIds = allCartDOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        Map<Long,InventoryDTO> distributorGoodsInventoryDTOMap = inventoryApi.getMapBySkuIds(goodSkuIds);
        for (CartDTO cartDO : allCartDOList) {
            InventoryDTO inventoryDTO = distributorGoodsInventoryDTOMap.get(cartDO.getGoodsSkuId());
            // 超卖商品,无需校验库存
            if (inventoryDTO != null && ObjectUtil.equal(1,inventoryDTO.getOverSoldType())) {
                continue;
            }
            if (CompareUtil.compare(cartDO.getQuantity(),0) <= 0 ) {

                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
            }

            if (inventoryDTO == null || inventoryDTO.getQty() - inventoryDTO.getFrozenQty() < cartDO.getQuantity()) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
            }
        }
    }

    /**
     * 校验管控商品
     * @param buyerEid
     * @param gidList
     */
    private void checkB2BGoodsByGids(Long buyerEid,List<Long> gidList) {
        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(gidList,buyerEid);
        if (log.isDebugEnabled()) {
            log.debug("checkB2BGoodsByGids request->{},result->{}",JSON.toJSON(gidList),JSON.toJSON(goodsListResult));
        }
        // 控销商品无法结算
        if (MapUtil.isEmpty(goodsListResult)) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否管控商品
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(value))){
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否建立采购关系
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.NOT_RELATION_SHIP == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.NOT_RELATION_SHIP_ERROR);
        }
    }


    /**
     * 结算商品校验
     * @param yilingFlag 是否以为以岭客户
     * @param customerEid 客户EID
     * @param cartDTOList 购物车信息
     */
    private void settlementCheck(Boolean yilingFlag,Long customerEid,List<CartDTO> cartDTOList) {
        // 校验采购关系
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        // 配送商商品ID
        List<Long> distributorGoodsIds = cartDTOList.stream().map(CartDTO::getDistributorGoodsId).distinct().collect(Collectors.toList());

        // 校验库存是否充足
        this.validateGoodsInventory(cartDTOList);

        // 如果是以岭内部需要校验采购关系
        if (!yilingFlag) {
            // 校验管控商品
            this.checkB2BGoodsByGids(customerEid,distributorGoodsIds);
            return;
        }
        // 表示为POP订单,需要校验采购关系
        boolean checkPurchaseRelationResult = enterprisePurchaseRelationApi.checkPurchaseRelation(customerEid, distributorEids);
        if (!checkPurchaseRelationResult) {
            throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_RELATION);
        }

        // 商品是否建采判断
        this.validatePurchaseAuthority(cartDTOList,customerEid);
    }

    /**
     * 校验商品建采权限
     * @param allCartDOList
     * @param buyerEid
     */
    private void validatePurchaseAuthority(List<CartDTO> allCartDOList, Long buyerEid) {

        Map<Long, List<CartDTO>> goodsAndDistributorMap = allCartDOList.stream().collect(Collectors.groupingBy(CartDTO::getGoodsId));
        List<Long> goodIdList = allCartDOList.stream().map(t -> t.getGoodsId()).distinct().collect(Collectors.toList());

        Map<Long, List<DistributorGoodsBO>> resultMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(goodIdList, buyerEid);

        if (MapUtil.isEmpty(resultMap)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
        }

        goodsAndDistributorMap.entrySet().forEach(t -> {

            List<DistributorGoodsBO> distributorGoodsBOS = resultMap.get(t.getKey());

            if (CollectionUtil.isEmpty(distributorGoodsBOS)) {

                throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
            }

            List<Long> distributorEidList = distributorGoodsBOS.stream().map(z -> z.getDistributorEid()).collect(Collectors.toList());
            List<Long> cartDistributorEidList =  t.getValue().stream().map(h -> h.getDistributorEid()).distinct().collect(Collectors.toList());

            if (!distributorEidList.containsAll(cartDistributorEidList)) {

                throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
            }
        });
    }

    @ApiOperation(value = "购物车\"去结算\"生成结算页信息")
    @RequestMapping(path = "/settlement", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<OrderSettlementPageVO> settlement(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "选择的客户ID", required = true) @RequestParam(value = "customerEid", required = true) Long customerEid) {
        CustomerVerificationRequest customerVerificationRequest = new CustomerVerificationRequest();
        customerVerificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        customerVerificationRequest.setCustomerEid(customerEid);
        customerVerificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        customerVerificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        if (!customerSearchApi.checkIsMyCustomer(customerVerificationRequest)) {
            return Result.failed("请确认,是否为当前登录人的用户");
        }

        CartGoodsSourceEnum cartGoodsSourceEnum = staffInfo.getYilingFlag() ? CartGoodsSourceEnum.POP : CartGoodsSourceEnum.B2B;
        ListCartRequest request = new ListCartRequest();
        request.setBuyerEid(customerEid);
        request.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        request.setGoodsSourceEnum(cartGoodsSourceEnum);
        request.setCreateUser(staffInfo.getCurrentUserId());
        request.setCartIncludeEnum(CartIncludeEnum.SELECTED);
        List<CartDTO> cartDTOList = cartApi.listByCreateUser(request);

        if (CollUtil.isEmpty(cartDTOList)) {
            return Result.failed("进货单中无选中商品");
        }
        // 校验采购关系
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        // 校验商品采购权限
        Map<Long, List<CartDTO>> distributorCartDTOMap = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getDistributorEid));
        // 校验商品状态
        List<Long> allGoodsSkuIds = cartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allGoodsSkuIds);
        Optional<GoodsSkuStandardBasicDTO> optional = allDistributorGoodsDTOList.stream().filter(e -> GoodsStatusEnum.getByCode(e.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF).findAny();
        if (optional.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
        }
        Optional<GoodsSkuStandardBasicDTO>  checkDisable = allDistributorGoodsDTOList.stream().filter(e -> !GoodsSkuStatusEnum.NORMAL.getCode().equals(e.getStatus())).findAny();
        if (checkDisable.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_DISABLE);
        }
        // 结算数据校验
        this.settlementCheck(staffInfo.getYilingFlag(),customerEid,cartDTOList);
        // 商品信息字典
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));
        //获取购物车最新的配送商商品Id
        cartDTOList.stream().forEach(cartDTO -> {
            GoodsSkuStandardBasicDTO goodsSkuStandardBasicDTO = allDistributorGoodsDTOMap.get(cartDTO.getGoodsSkuId());
            cartDTO.setDistributorGoodsId(goodsSkuStandardBasicDTO.getGoodsId());
        });
        // 配送商字典
        List<EnterpriseDTO> distributorDTOList = enterpriseApi.listByIds(distributorEids);
        Map<Long, EnterpriseDTO> distributorDTOMap = distributorDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        // 以岭企业列表
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 工业直属企业信息
        List<Long> industryDirectEids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
        //商品信息
        List<Long> distributorGoodsIds = cartDTOList.stream().map(e -> e.getDistributorGoodsId()).distinct().collect(Collectors.toList());

        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(customerEid);
        queryGoodsPriceRequest.setGoodsIds(distributorGoodsIds);
        // 商品价格字典
        Map<Long, BigDecimal> goodsPriceMap;
        if (staffInfo.getYilingFlag()) {
            goodsPriceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);
        } else {
            goodsPriceMap = Maps.newHashMap();
            goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest).forEach((k,v)  ->  goodsPriceMap.put(k,v.getLinePrice()));
        }

        // 以岭任务Id
        Map<Long,TaskGoodsMatchListDTO> taskGoodsMatchMap = this.selectDistributorTaskInfo(!staffInfo.getYilingFlag(),staffInfo.getCurrentUserId(),distributorEids);

        List<ShopDTO> shopList = shopApi.listShopByEidList(distributorEids);
        Map<Long, ShopDTO> shopMap = shopList.stream().collect(Collectors.toMap(ShopDTO::getShopEid, o -> o, (k1, k2) -> k1));
        List<OrderDistributorVO> orderDistributorVOList = CollUtil.newArrayList();
        for (Long distributorEid : distributorCartDTOMap.keySet()) {
            List<CartDTO> distributorCartDTOList = distributorCartDTOMap.get(distributorEid);
            List<OrderGoodsVO> orderGoodsVOList = CollUtil.newArrayList();
            for (CartDTO cartInfo : distributorCartDTOList) {
                GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
                OrderGoodsVO orderGoodsVO = new OrderGoodsVO();
                // 拷贝商品标准库信息
                PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), orderGoodsVO);
                orderGoodsVO.setRelationId(cartInfo.getId());
                orderGoodsVO.setGoodsSkuId(cartInfo.getGoodsSkuId());
                orderGoodsVO.setDistributorGoodsId(cartInfo.getDistributorGoodsId());
                orderGoodsVO.setPrice(goodsPriceMap.getOrDefault(cartInfo.getDistributorGoodsId(), standardGoodsBasicDTO.getStandardGoodsBasic().getPrice()));
                orderGoodsVO.setQuantity(cartInfo.getQuantity());
                orderGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(orderGoodsVO.getPrice(), orderGoodsVO.getQuantity()), 2));
                // 匹配商品是否参与任务
                orderGoodsVO.setIsHasTask(this.matchTask(taskGoodsMatchMap.get(cartInfo.getDistributorEid()),cartInfo.getGoodsId()));

                orderGoodsVOList.add(orderGoodsVO);
            }
            OrderDistributorVO orderDistributorVO = new OrderDistributorVO();
            orderDistributorVO.setDistributorEid(distributorEid);
            orderDistributorVO.setDistributorName(distributorDTOMap.get(distributorEid).getName());
            orderDistributorVO.setOrderGoodsList(orderGoodsVOList);
            orderDistributorVO.setGoodsSpeciesNum(orderGoodsVOList.stream().count());
            orderDistributorVO.setGoodsNum(orderGoodsVOList.stream().mapToLong(OrderGoodsVO::getQuantity).sum());
            orderDistributorVO.setTotalAmount(orderGoodsVOList.stream().map(OrderGoodsVO::getAmount).reduce(BigDecimal::add).get());
            orderDistributorVO.setFreightAmount(BigDecimal.ZERO);
            orderDistributorVO.setPaymentAmount(NumberUtil.sub(orderDistributorVO.getTotalAmount(), orderDistributorVO.getFreightAmount()));
            orderDistributorVO.setYilingFlag(yilingSubEids.contains(distributorEid));
            orderDistributorVO.setShowContractFile(staffInfo.getYilingFlag()&&(yilingSubEids.contains(distributorEid) || industryDirectEids.contains(distributorEid)));
            // 是否有参与的任务
            orderDistributorVO.setIsHasTask(orderGoodsVOList.stream().filter(e -> e.getIsHasTask()).findAny().isPresent());
            orderDistributorVOList.add(orderDistributorVO);
            ShopDTO shopOne = shopMap.get(distributorEid);
            if (!staffInfo.getYilingFlag() && shopOne != null && shopOne.getStartAmount().compareTo(orderDistributorVO.getTotalAmount()) > 0) {
                // 部分企业未满足起配金额，请满足后重新提交
                throw new BusinessException(OrderErrorCode.ORDER_SHOP_START_ERROR);
            }
        }
        OrderSettlementPageVO pageVO = new OrderSettlementPageVO();
        pageVO.setCustomerEid(customerEid);
        pageVO.setOrderDistributorList(orderDistributorVOList);
        pageVO.setGoodsSpeciesNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsSpeciesNum).sum());
        pageVO.setGoodsNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsNum).sum());
        pageVO.setTotalAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getTotalAmount).reduce(BigDecimal::add).get());
        pageVO.setFreightAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getFreightAmount).reduce(BigDecimal::add).get());
        pageVO.setPaymentAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getPaymentAmount).reduce(BigDecimal::add).get());
        // 配送地址
        pageVO.setDeliveryAddress(this.selectDeliveryAddressInfoList(customerEid));
        pageVO.setOrderType(customerVerificationRequest.getOrderType());
        // 企业账号信息
        pageVO.setShowEasAccountInfoFlag(this.showEasAccountInfoFlag(staffInfo.getYilingFlag(), yilingSubEids, distributorEids));
        pageVO.setEasAccountList(this.selectEasAccountList(staffInfo.getYilingFlag(), pageVO.getShowEasAccountInfoFlag(), customerEid));
        return Result.success(pageVO);
    }

    /**
     * 匹配商品是否参与运河
     * @param goodsMatchListDTO 任务信息
     * @param goodsId 匹配商品Id
     * @return
     */
    private Boolean matchTask(TaskGoodsMatchListDTO goodsMatchListDTO,Long goodsId) {

        Boolean isHasTask = Optional.ofNullable(goodsMatchListDTO).map(t -> {
            if (CollectionUtil.isEmpty(t.getGoodsMatchList())) {
                return false;
            }
            List<Long> taskGoodIds = t.getGoodsMatchList().stream().map(z -> z.getGoodsId()).collect(Collectors.toList());
            if (taskGoodIds.contains(goodsId)) {
                return true;
            }
            return false;
        }).orElse(false);

        return isHasTask;
    }


    /**
     * 查询配送商是否参与
     * @param isB2bOrder 是否b2b订单
     * @param userId 用户Id
     * @param distributorEids 配送商Ids
     * @return
     */
    private  Map<Long,TaskGoodsMatchListDTO> selectDistributorTaskInfo(Boolean isB2bOrder,Long userId,List<Long> distributorEids) {

        // 非b2b订单,没有参与任务
        if (!isB2bOrder) {

            return MapUtil.empty();
        }

        List<QueryTaskGoodsMatchRequest> queryTaskGoodsMatchRequestList = distributorEids.stream().map(t -> {

            QueryTaskGoodsMatchRequest queryTaskGoodsMatchRequest = new QueryTaskGoodsMatchRequest();
            queryTaskGoodsMatchRequest.setUserId(userId);
            queryTaskGoodsMatchRequest.setEid(t);
            return queryTaskGoodsMatchRequest;
        }).collect(Collectors.toList());

        // 查询当前用户参与的任务品
        List<TaskGoodsMatchListDTO> taskGoodsMatchDTOS = taskApi.queryTaskGoodsList(queryTaskGoodsMatchRequestList);

        if (log.isDebugEnabled()) {
            log.debug("调用任务接口:queryTaskGoodsList..入参:{},返回参数:{}",queryTaskGoodsMatchRequestList,taskGoodsMatchDTOS);
        }

        if (CollectionUtil.isEmpty(taskGoodsMatchDTOS)) {

            return MapUtil.empty();
        }

        return  taskGoodsMatchDTOS.stream().collect(Collectors.toMap(t -> t.getEid(),Function.identity(),(u,u1) -> u));
    }

    /**
     * 订单状态转换
     * @param e 订单信息
     */
    private void transferOrderStatus(OrderDTO e) {
        if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(e.getOrderStatus())) {
            e.setOrderStatus(SaOrderStatusEnum.CANCELED.getCode());
            return;
        }
        if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(e.getOrderType())
                && (CustomerConfirmEnum.NOTCONFIRM.getCode().equals(e.getCustomerConfirmStatus())
                || CustomerConfirmEnum.TRANSFER.getCode().equals(e.getCustomerConfirmStatus()))
                && PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(e.getPaymentStatus())
                && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(e.getOrderStatus())
        ) {
            e.setOrderStatus(SaOrderStatusEnum.NOTCONFIRM.getCode());
            return;
        }
        if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(e.getOrderType())
                && CustomerConfirmEnum.CONFIRMED.getCode().equals(e.getCustomerConfirmStatus()) && PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(e.getPaymentStatus())
                && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(e.getOrderStatus())
        ) {
            e.setOrderStatus(SaOrderStatusEnum.WAIT_PAY.getCode());
        }
        if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(e.getOrderType()) &&
                (OrderStatusEnum.FINISHED == OrderStatusEnum.getByCode(e.getOrderStatus())
                        || OrderStatusEnum.RECEIVED == OrderStatusEnum.getByCode(e.getOrderStatus()))) {
            e.setOrderStatus(SaOrderStatusEnum.FINISHED.getCode());
        }
        if (OrderAuditStatusEnum.AUDIT_REJECT.getCode().equals(e.getAuditStatus())) {
            e.setOrderStatus(SaOrderStatusEnum.ORDER_RETURN_REJECT.getCode());
        }
        if (OrderAuditStatusEnum.UNAUDIT.getCode().equals(e.getAuditStatus()) && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(e.getOrderStatus())) {
            e.setOrderStatus(SaOrderStatusEnum.UNAUDITED.getCode());
        }
        if (OrderTypeEnum.POP == OrderTypeEnum.getByCode(e.getOrderType()) &&  e.getPaymentMethod() == 0) {
            e.setOrderStatus(SaOrderStatusEnum.UNSUBMIT.getCode());
        }
    }
    @ApiOperation(value = "代客下单订单查看列表")
    @RequestMapping(path = "/list", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication
    public Result<AssistanQueryOrderVO> queryList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOrderPageForm queryOrderPageForm) {
        CustomerVerificationRequest customerVerificationRequest = new CustomerVerificationRequest();
        customerVerificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        customerVerificationRequest.setCustomerEid(queryOrderPageForm.getCustomerEid());
        customerVerificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        customerVerificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());

        if (!customerSearchApi.checkIsMyCustomer(customerVerificationRequest)) {
            return Result.failed("请确认,是否为当前登录人的用户");
        }
        QueryAssistanOrderPageRequest queryOrderPageRequest = PojoUtils.map(queryOrderPageForm, QueryAssistanOrderPageRequest.class);
        queryOrderPageRequest.setOrderType(staffInfo.getYilingFlag()? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        queryOrderPageRequest.setContacterIds(ListUtil.toList(staffInfo.getCurrentUserId()));
        Page<OrderDTO> pageResult = orderApi.getSaOrderPage(queryOrderPageRequest);
        if (CollectionUtil.isEmpty(pageResult.getRecords())) {
            AssistanQueryOrderVO assistanQueryOrderVO = new AssistanQueryOrderVO();
            assistanQueryOrderVO.setOrderInfoList(new Page<>(pageResult.getCurrent(),0));
            assistanQueryOrderVO.setIsYilingFlag(staffInfo.getYilingFlag());
            return Result.success(assistanQueryOrderVO);
        }
        // 订单状态转换，便于前台展示
        pageResult.getRecords().forEach(e -> {
            transferOrderStatus(e);
        });
        Page<OrderInfoVO> orderInfoList = PojoUtils.map(pageResult, OrderInfoVO.class);
        List<Long> ids = orderInfoList.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
        List<OrderDetailDTO> details = orderDetailApi.getOrderDetailByOrderIds(ids);
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
        List<Long> goodsIds = details.stream().map(e -> e.getGoodsId()).distinct().collect(Collectors.toList());
        // 图片地址
        Map<Long, String> pictureMap = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);
        Map<Long,List<OrderDetailDTO>> detailMapList = details.stream().collect(Collectors.groupingBy(t -> t.getOrderId()));

        for (OrderInfoVO e : orderInfoList.getRecords()) {
            //货款总额
            BigDecimal totalAmount = BigDecimal.ZERO;
            //折扣金额
            BigDecimal discountAmount = BigDecimal.ZERO;
            List<OrderDetailVO> goodDetailList = new ArrayList<>();
            List<OrderDetailDTO> orderDetails = detailMapList.getOrDefault(e.getId(),ListUtil.empty());
            for (OrderDetailDTO detail : orderDetails) {
                OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(detail.getId());
                // 明细优惠金额
                BigDecimal detailDiscountAmount = orderDetailChangeOne.getCashDiscountAmount().add(orderDetailChangeOne.getTicketDiscountAmount());
                detailDiscountAmount = detailDiscountAmount.add(orderDetailChangeOne.getPlatformCouponDiscountAmount()).add(orderDetailChangeOne.getCouponDiscountAmount());
                detailDiscountAmount = detailDiscountAmount.subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount());
                detailDiscountAmount = detailDiscountAmount.subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount());
                detailDiscountAmount = detailDiscountAmount.subtract(orderDetailChangeOne.getSellerPlatformCouponDiscountAmount());
                detailDiscountAmount = detailDiscountAmount.subtract(orderDetailChangeOne.getSellerCouponDiscountAmount());

                OrderDetailVO orderDetailOne = PojoUtils.map(detail, OrderDetailVO.class);
                orderDetailOne.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                        .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                        .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                        .setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                        .setDiscountAmount(detailDiscountAmount)
                        .setRealAmount(orderDetailOne.getGoodsAmount().subtract(orderDetailOne.getDiscountAmount()));
                orderDetailOne.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(pictureMap.get(detail.getGoodsId())));
                totalAmount = totalAmount.add(orderDetailOne.getGoodsAmount());
                discountAmount = discountAmount.add(orderDetailOne.getDiscountAmount());
                goodDetailList.add(orderDetailOne);
            }
            //设置金额
            e.setDiscountAmount(discountAmount).setTotalAmount(totalAmount);
            e.setPaymentAmount(totalAmount.subtract(discountAmount));
            OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(e.getId());
            e.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum());
            e.setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum());
            e.setReceiveOrderNum(goodsTypeNumber.getReceiveOrderNum()).setReceiveOrderPieceNum(goodsTypeNumber.getReceiveOrderPieceNum());
            e.setDeliveryOrderNum(goodsTypeNumber.getDeliveryOrderNum()).setDeliveryOrderPieceNum(goodsTypeNumber.getDeliveryOrderPieceNum());
            e.setGoodDetailList(goodDetailList);
        }

        AssistanQueryOrderVO assistanQueryOrderVO = new AssistanQueryOrderVO();
        assistanQueryOrderVO.setOrderInfoList(orderInfoList);
        assistanQueryOrderVO.setIsYilingFlag(staffInfo.getYilingFlag());
        return Result.success(assistanQueryOrderVO);
    }


    @ApiOperation(value = "b2b代客下单订单查看详情")
    @RequestMapping(path = "/b2bOrder/detail", method = {RequestMethod.GET})
    public Result<B2bOrderInfoVO> b2bDetail(@ApiParam(value = "订单ID", required = true) @RequestParam(value = "orderId", required = true) Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        if (ObjectUtil.isNull(orderInfo)) {
            return Result.failed("订单不存在!");
        }
        // 转换状态
        this.transferOrderStatus(orderInfo);

        B2bOrderInfoVO infoVO = PojoUtils.map(orderInfo, B2bOrderInfoVO.class);
        // 设置收货地址信息
        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderInfo.getId());
        OrderAddressVO address = PojoUtils.map(addressInfo, OrderAddressVO.class);
        address.setAddress(Optional.ofNullable(addressInfo).map(e -> e.getProvinceName() + e.getCityName() + e.getRegionName() + e.getAddress()).orElse(null));
        infoVO.setOrderAddress(address);

        List<OrderDetailDTO> details = orderDetailApi.getOrderDetailInfo(orderInfo.getId());
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderInfo.getId());
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
        List<Long> goodsIds = details.stream().map(e -> e.getGoodsId()).distinct().collect(Collectors.toList());
        // 图片地址
        Map<Long, String> pictureMap = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);
        //货款总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        //折扣金额
        BigDecimal discountAmountAmount = BigDecimal.ZERO;
        //平台优惠总金额
        BigDecimal platformCouponDiscountAmount = BigDecimal.ZERO;
        //商家优惠总金额
        BigDecimal couponDiscountAmount = BigDecimal.ZERO;
        List<OrderDetailVO> goodDetailList = new ArrayList<>();
        for (OrderDetailDTO detail : details) {
            OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(detail.getId());
            // 明细优惠金额
            BigDecimal detailDiscountAmount = orderDetailChangeOne.getPlatformCouponDiscountAmount().add(orderDetailChangeOne.getCouponDiscountAmount());
            detailDiscountAmount = detailDiscountAmount.subtract(orderDetailChangeOne.getSellerPlatformCouponDiscountAmount());
            detailDiscountAmount = detailDiscountAmount.subtract(orderDetailChangeOne.getSellerCouponDiscountAmount());

            OrderDetailVO orderDetailOne = PojoUtils.map(detail, OrderDetailVO.class);
            orderDetailOne.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                    .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                    .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                    .setGoodsAmount(detail.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                    .setDiscountAmount(detailDiscountAmount)
                    .setRealAmount(orderDetailOne.getGoodsAmount().subtract(orderDetailOne.getDiscountAmount()));
            orderDetailOne.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(pictureMap.get(detail.getGoodsId())));
            totalAmount = totalAmount.add(orderDetailOne.getGoodsAmount());
            discountAmountAmount = discountAmountAmount.add(orderDetailOne.getDiscountAmount());
            platformCouponDiscountAmount = platformCouponDiscountAmount.add(orderDetailChangeOne.getPlatformCouponDiscountAmount().subtract(orderDetailChangeOne.getSellerPlatformCouponDiscountAmount()));
            couponDiscountAmount = couponDiscountAmount.add(orderDetailChangeOne.getCouponDiscountAmount().subtract(orderDetailChangeOne.getSellerCouponDiscountAmount()));

            goodDetailList.add(orderDetailOne);
        }
        //设置金额
        infoVO.setDiscountAmount(discountAmountAmount).setTotalAmount(totalAmount);
        infoVO.setPaymentAmount(totalAmount.subtract(discountAmountAmount));
        infoVO.setPlatformCouponDiscountAmount(platformCouponDiscountAmount);
        infoVO.setCouponDiscountAmount(couponDiscountAmount);
        OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(orderId);
        infoVO.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum());
        infoVO.setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum());
        infoVO.setReceiveOrderNum(goodsTypeNumber.getReceiveOrderNum()).setReceiveOrderPieceNum(goodsTypeNumber.getReceiveOrderPieceNum());
        infoVO.setDeliveryOrderNum(goodsTypeNumber.getDeliveryOrderNum()).setDeliveryOrderPieceNum(goodsTypeNumber.getDeliveryOrderPieceNum());
        infoVO.setOrderDetailDelivery(goodDetailList);

        return Result.success(infoVO);

    }



    /**
     * 构建订单完整信息
     *
     * @param orderInfo
     * @param isYilingFlag
     * @return
     */
    private PopOrderInfoVO buildOrderFullDetailInfoVo(OrderDTO orderInfo, Boolean isYilingFlag) {
        // 转换状态
        this.transferOrderStatus(orderInfo);

        PopOrderInfoVO result = PojoUtils.map(orderInfo, PopOrderInfoVO.class);
        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderInfo.getId());
        OrderAddressVO address = PojoUtils.map(addressInfo, OrderAddressVO.class);
        address.setAddress(Optional.ofNullable(addressInfo).map(e -> e.getProvinceName() + e.getCityName() + e.getRegionName() + e.getAddress()).orElse(null));
        result.setOrderAddress(address);
        // 查询票折信息
        List<OrderTicketDiscountDTO> ticketDiscountDTOList = orderTicketDiscountApi.listByOrderId(orderInfo.getId());

        if (CollectionUtil.isNotEmpty(ticketDiscountDTOList)) {
            result.setOrderTicketDiscountVoList(PojoUtils.map(ticketDiscountDTOList, OrderTicketDiscountVO.class));
        } else {
            result.setOrderTicketDiscountVoList(ListUtil.empty());
        }
        // 合同信息
        List<OrderAttachmentDTO> orderContractList = orderApi.listOrderAttachmentsByType(orderInfo.getId(), OrderAttachmentTypeEnum.SALES_CONTRACT_FILE);
        //购销合同信息
        List<FileInfoVO> fileList = orderContractList.stream().map(one -> {
            FileInfoVO file = new FileInfoVO();
            file.setFileKey(one.getFileKey());
            file.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.ORDER_SALES_CONTRACT));
            return file;

        }).collect(Collectors.toList());
        result.setContractList(fileList);
        result.setIsYilingFlag(isYilingFlag);
        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderInfo.getId());
        List<OrderDetailVO> orderDetailDelivery = new ArrayList<>();
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderInfo.getId());
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
        //货款总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        //票折总金额
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
        //现折金额
        BigDecimal cashDiscountAmount = BigDecimal.ZERO;
        //折扣金额
        BigDecimal discountAmountAmount = BigDecimal.ZERO;
        // 图片地址
        List<Long> goodsIds = orderDetailInfo.stream().map(e -> e.getGoodsId()).distinct().collect(Collectors.toList());
        Map<Long, String> pictureMap = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);

        for (OrderDetailDTO one : orderDetailInfo) {
            OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(one.getId());
            OrderDetailVO detail = PojoUtils.map(one, OrderDetailVO.class);
            // 订单优惠金额
            BigDecimal discountAmount = orderDetailChangeOne.getCashDiscountAmount().add(orderDetailChangeOne.getTicketDiscountAmount());
            discountAmount = discountAmount.subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount()).subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount());
            detail.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(pictureMap.get(one.getGoodsId())));
            detail.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                    .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                    .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                    .setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                    .setDiscountAmount(discountAmount)
                    .setReceiveQuantity(orderDetailChangeOne.getReceiveQuantity())
                    .setRealAmount(detail.getGoodsAmount().subtract(detail.getDiscountAmount()));
            ticketDiscountAmount = ticketDiscountAmount.add(orderDetailChangeOne.getTicketDiscountAmount().subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
            cashDiscountAmount = cashDiscountAmount.add(orderDetailChangeOne.getCashDiscountAmount().subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount()));
            totalAmount = totalAmount.add(detail.getGoodsAmount());
            discountAmountAmount = discountAmountAmount.add(discountAmount);
            orderDetailDelivery.add(detail);
        }
        result.setTotalAmount(totalAmount);
        result.setCashDiscountAmount(cashDiscountAmount);
        result.setTicketDiscountAmount(ticketDiscountAmount);
        result.setDiscountAmount(discountAmountAmount);
        result.setPaymentAmount(totalAmount.subtract(cashDiscountAmount).subtract(ticketDiscountAmount));
        OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(orderInfo.getId());
        result.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum());
        result.setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum());
        result.setReceiveOrderNum(goodsTypeNumber.getReceiveOrderNum()).setReceiveOrderPieceNum(goodsTypeNumber.getReceiveOrderPieceNum());
        result.setDeliveryOrderNum(goodsTypeNumber.getDeliveryOrderNum()).setDeliveryOrderPieceNum(goodsTypeNumber.getDeliveryOrderPieceNum());
        // 按照实际支付金额倒序排序
        orderDetailDelivery = orderDetailDelivery.stream().sorted(Comparator.comparing(OrderDetailVO::getRealAmount).reversed()).collect(Collectors.toList());
        result.setOrderDetailDelivery(orderDetailDelivery);
        return result;
    }

    @ApiOperation(value = "pop代客下单订单查看详情")
    @RequestMapping(path = "/popOrder/detail", method = {RequestMethod.GET})
    @UserAccessAuthentication
    public Result<PopOrderInfoVO> orderDetailInfo(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "订单ID", required = true) @RequestParam(value = "orderId", required = true) Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);

        if (ObjectUtil.isNull(orderInfo)) {
            return Result.failed("订单不存在!");
        }
        return Result.success(this.buildOrderFullDetailInfoVo(orderInfo, staffInfo.getYilingFlag()));
    }

    @ApiOperation(value = "分享订单")
    @PostMapping(path = "/share/order")
    public Result<ShareOrderResultVO> shareOrder(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm orderListForm) {

        if (staffInfo.getYilingFlag()) {
            return Result.failed("以岭内部人员无此操作权限!");
        }
        // 订单列表
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(orderListForm.getOrderNos());
        if (CollUtil.isEmpty(orderDTOList)) {
            return Result.failed("未找到相关订单信息");
        }
        Boolean checkOrderType = orderDTOList.stream().filter(e -> OrderTypeEnum.B2B != OrderTypeEnum.getByCode(e.getOrderType())).findAny().isPresent();
        if (checkOrderType) {
            return Result.failed("选择的订单不属于B2B类型订单");
        }
        List<String> orderList = orderDTOList.stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());
        // 确认转发订单
        this.toConfirmedOrder(orderDTOList.stream().filter(e -> CustomerConfirmEnum.TRANSFER.getCode().equals(e.getCustomerConfirmStatus())).collect(Collectors.toList()),staffInfo.getCurrentUserId());
        String keyStr = StockUtils.getEncryptHexStr(StringUtils.join(orderList.toArray(), ","));
        Long buyerEid = orderDTOList.stream().findFirst().get().getBuyerEid();
        String buyerName = orderDTOList.stream().findFirst().get().getBuyerEname();
        ShareOrderResultVO orderResultVO = ShareOrderResultVO.builder().keyStr(keyStr).buyerEid(buyerEid).buyerName(buyerName).build();
        this.clearShareLink(orderListForm.getOrderNos(),keyStr);
        return Result.success(orderResultVO);
    }
    /**
     * 清除链接分享
     * @param orderNos
     * @param keyStr
     */
    private void clearShareLink(List<String> orderNos,String keyStr) {
        orderNos.forEach(orderNo -> {
            redisService.del(RedisKey.generate("sa","order","share","link",orderNo));
            redisService.set(RedisKey.generate("sa","order","share","link",orderNo),keyStr,24*60*60L);
        });
    }
    /**
     * 修改转发订单，确认订单状态
     * @param waitOrderDTOList
     * @param currentUserId
     */
    private void toConfirmedOrder(List<OrderDTO> waitOrderDTOList,Long currentUserId) {
        if (CollectionUtil.isEmpty(waitOrderDTOList)) {
            return;
        }
        UpdateCustomerConfirmStatusRequest request = new UpdateCustomerConfirmStatusRequest();
        List<UpdateCustomerConfirmStatusRequest.UpdateConfirmStatusDetailRequest> detailRequestList = Lists.newArrayList();
        for (OrderDTO orderDTO : waitOrderDTOList) {
            UpdateCustomerConfirmStatusRequest.UpdateConfirmStatusDetailRequest detailRequest = new UpdateCustomerConfirmStatusRequest.UpdateConfirmStatusDetailRequest();
            detailRequest.setOrderId(orderDTO.getId());
            detailRequest.setOriginalStatus(CustomerConfirmEnum.getByCode(orderDTO.getCustomerConfirmStatus()));
            detailRequest.setNewStatus(CustomerConfirmEnum.NOTCONFIRM);
            detailRequest.setUpdateUserId(currentUserId);
            detailRequest.setRemark("");
            detailRequestList.add(detailRequest);
        }
        request.setConfirmRequestList(detailRequestList);
        // 设置转发状态为“待确认”
        orderApi.updateOrderCustomerConfirmStatus(request);
    }

    /**
     * 校验订单支付方式
     *
     * @param paymentMethodDTOList
     * @param paymentMethodId
     */
    private void checkPaymentMethod(List<PaymentMethodDTO> paymentMethodDTOList, Long paymentMethodId) {

        List<Long> paymentMethodIds = paymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).collect(Collectors.toList());
        if (!paymentMethodIds.contains(paymentMethodId)) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_METHOD_UNUSABLE);
        }
    }

    /**
     * 设置订单支付方式
     *
     * @param paymentMethodDTOList
     * @param orderDistributorVO
     */
    private void setPaymentMethod(OrderDTO orderDTO,List<PaymentMethodDTO> paymentMethodDTOList, Long choosePaymentMethodId, OrderConfirmVO.OrderSellerVO orderDistributorVO) {
        orderDistributorVO.setPaymentMethodList(ListUtil.empty());
        List<OrderConfirmVO.PaymentMethodVO> paymentMethodVOList = Lists.newArrayList();
        // 如果用户确认，按照用户确认的支付状态
        if (CustomerConfirmEnum.CONFIRMED == CustomerConfirmEnum.getByCode(orderDTO.getCustomerConfirmStatus())) {
            paymentMethodDTOList.stream().filter(t -> orderDTO.getPaymentMethod().equals(t.getCode().intValue())).findFirst().ifPresent(e -> {
                OrderConfirmVO.PaymentMethodVO paymentMethodVO = new OrderConfirmVO.PaymentMethodVO();
                paymentMethodVO.setId(e.getCode());
                paymentMethodVO.setName(e.getName());
                paymentMethodVO.setEnabled(1 == e.getStatus());
                paymentMethodVO.setSelected(true);
                paymentMethodVOList.add(paymentMethodVO);
            });
            // 默认在线支付
            if (CollectionUtil.isEmpty(paymentMethodVOList)) {
                OrderConfirmVO.PaymentMethodVO paymentMethodVO = new OrderConfirmVO.PaymentMethodVO();
                paymentMethodVO.setId(4l);
                paymentMethodVO.setName("在线支付");
                paymentMethodVO.setEnabled(true);
                paymentMethodVO.setSelected(true);
                paymentMethodVOList.add(paymentMethodVO);
            }
            orderDistributorVO.setPaymentMethodList(paymentMethodVOList);
            return;
        }
        for (PaymentMethodDTO e : paymentMethodDTOList) {
            OrderConfirmVO.PaymentMethodVO paymentMethodVO = new OrderConfirmVO.PaymentMethodVO();
            paymentMethodVO.setId(e.getCode());
            paymentMethodVO.setName(e.getName());
            paymentMethodVO.setEnabled(1 == e.getStatus());
            paymentMethodVO.setSelected(false);
            // 如果有企业方式，按照用户选择的企业方式
            if (choosePaymentMethodId != null && choosePaymentMethodId.equals(e.getCode().longValue())) {
                paymentMethodVO.setSelected(true);
                paymentMethodVOList.add(paymentMethodVO);
                continue;
            }
            // 默认在线支付
            if (choosePaymentMethodId == null && PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getCode())) {
                paymentMethodVO.setSelected(true);
                paymentMethodVOList.add(paymentMethodVO);
                continue;
            }

            paymentMethodVOList.add(paymentMethodVO);
        }
        // 如果企业不存线上支付方式,从新设置一个线下默认方式
        if (!paymentMethodVOList.stream().filter(t -> t.getSelected()).findFirst().isPresent()) {
            paymentMethodVOList.stream().findFirst().ifPresent(
                    e -> e.setSelected(true)
            );
        }
        orderDistributorVO.setPaymentMethodList(paymentMethodVOList);
    }


    /**
     * 查询订单可使用的优惠劵
     * @param orderDistributorVOList
     * @param shopCustomerCouponMap
     * @param currentEid
     * @param pageVO
     * @param goodsDetailList
     */
    private void selectCouponCanUseList(
            List<OrderConfirmVO.OrderSellerVO> orderDistributorVOList,
            Map<Long, Long> shopCustomerCouponMap,
            Long currentEid,
            OrderConfirmVO pageVO,
            List<QueryCouponCanUseListDetailRequest> goodsDetailList
    ) {
        if (log.isDebugEnabled()) {
            log.debug("..orderDistributorVOList...request -> {}", JSON.toJSON(goodsDetailList));
        }
        Map<Long,List<QueryCouponCanUseListDetailRequest>> goodIdMap = goodsDetailList.stream().collect(Collectors.groupingBy(QueryCouponCanUseListDetailRequest::getGoodsId));
        List<QueryCouponCanUseListDetailRequest> groupDetailList = Lists.newArrayList();
        for (Long goodId : goodIdMap.keySet()) {
            QueryCouponCanUseListDetailRequest detailRequest = PojoUtils.map(goodIdMap.get(goodId).get(0),QueryCouponCanUseListDetailRequest.class);
            detailRequest.setGoodsAmount(goodIdMap.get(goodId).stream().map(QueryCouponCanUseListDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get());
            detailRequest.setShopCouponId(shopCustomerCouponMap.get(detailRequest.getEid()));
            groupDetailList.add(detailRequest);
        }
        QueryCouponCanUseListRequest canUseListRequest = new QueryCouponCanUseListRequest();
        canUseListRequest.setCurrentEid(currentEid);
        canUseListRequest.setPlatform(PlatformEnum.SALES_ASSIST.getCode());
        canUseListRequest.setGoodsDetailList(groupDetailList);
        canUseListRequest.setIsUseBestCoupon(false);
        CouponActivityCanUseDTO activityCanUseDTO = couponActivityApi.getCouponCanUseList(canUseListRequest);
        log.info("..orderDistributorVOList...request -> {},result -> {}",JSON.toJSON(canUseListRequest),JSON.toJSON(activityCanUseDTO));
        if (activityCanUseDTO == null) {
            return;
        }
        List<CouponActivityCanUseDetailDTO> platformCouponList = activityCanUseDTO.getPlatformList();
        if (CollectionUtil.isNotEmpty(platformCouponList)) {
            Optional.ofNullable(platformCouponList).ifPresent(e -> {
                pageVO.setPlatformCouponActivity(platformCouponList.stream().map(couponActivityCanUseDetailDTO -> {
                    CouponActivityCanUseDetailVO detailVO = PojoUtils.map(couponActivityCanUseDetailDTO, CouponActivityCanUseDetailVO.class);
                    detailVO.setIsSelected(false);
                    if (ObjectUtil.equal(detailVO.getId(), pageVO.getCustomerPlatformCouponId())) {
                        detailVO.setIsSelected(true);
                    }
                    return detailVO;
                }).collect(Collectors.toList()));
                pageVO.setPlatformCouponCount(e.size());
                pageVO.setIsUsePlatformCoupon(true);
            });
        }

        List<CouponActivityCanUseDetailDTO> shopCouponList = activityCanUseDTO.getBusinessList();
        if (CollectionUtil.isEmpty(shopCouponList)) {
            return;
        }
        Map<Long, List<CouponActivityCanUseDetailDTO>> shopCanUseMap = shopCouponList.stream().collect(Collectors.groupingBy(CouponActivityCanUseDetailDTO::getShopEid));
        for (OrderConfirmVO.OrderSellerVO orderDistributorVO : orderDistributorVOList) {
            List<CouponActivityCanUseDetailDTO> shopCanUserList = shopCanUseMap.get(orderDistributorVO.getDistributorEid());
            Optional.ofNullable(shopCanUserList).ifPresent(e -> {
                orderDistributorVO.setShopCouponActivity((shopCanUserList.stream().map(couponActivityCanUseDto -> {
                    CouponActivityCanUseDetailVO detailVO = PojoUtils.map(couponActivityCanUseDto, CouponActivityCanUseDetailVO.class);
                    detailVO.setIsSelected(false);
                    if (ObjectUtil.equal(detailVO.getId(), orderDistributorVO.getCustomerShopCouponId())) {
                        detailVO.setIsSelected(true);
                    }
                    return detailVO;
                }).collect(Collectors.toList())));
                orderDistributorVO.setShopCouponCount(e.size());
                orderDistributorVO.setIsUseShopCoupon(true);
            });
        }
    }


    /**
     * 计算优惠劵信息并返回优惠金额
     *
     * @param orderDistributorVOList
     * @param shopCustomerCouponMap
     * @param customerPlatformCouponId
     * @param currentEid
     * @param pageVO
     */
    private void setCouponInfo(
            List<OrderConfirmVO.OrderSellerVO> orderDistributorVOList,
            Map<Long, Long> shopCustomerCouponMap,
            Long customerPlatformCouponId,
            Long currentEid,
            OrderConfirmVO pageVO
    ) {
        List<QueryCouponCanUseListDetailRequest> goodsDetailList = Lists.newArrayList();
        List<OrderUseCouponBudgetGoodsDetailRequest> goodsDetailRequests = Lists.newArrayList();
        if (log.isDebugEnabled()) {
            log.debug("..orderDistributorVOList...request -> {}", JSON.toJSON(orderDistributorVOList));
        }
        List<OrderConfirmVO.OrderSellerVO> canUseCouponActivityList = orderDistributorVOList.stream().filter( vo -> {
            OrderConfirmVO.PaymentMethodVO paymentMethodVO = vo.getPaymentMethodList().stream().filter(e -> e.getSelected()).findFirst().get();
            if (paymentMethodVO == null) {
                return false;
            }
            if (!vo.getSelected()) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(canUseCouponActivityList)){
            return;
        }
        canUseCouponActivityList.stream().forEach(vo -> {
            OrderConfirmVO.PaymentMethodVO paymentMethodVO = vo.getPaymentMethodList().stream().filter(e -> e.getSelected()).findFirst().get();
            for (OrderGoodsVO goodsVO : vo.getOrderGoodsList()) {
                QueryCouponCanUseListDetailRequest detailRequest = new QueryCouponCanUseListDetailRequest();
                detailRequest.setEid(vo.getDistributorEid());
                detailRequest.setPaymentMethod(paymentMethodVO.getId().intValue());
                detailRequest.setGoodsAmount(NumberUtil.mul(goodsVO.getPrice(), goodsVO.getQuantity()));
                detailRequest.setGoodsId(goodsVO.getDistributorGoodsId());
                goodsDetailList.add(detailRequest);
                if (customerPlatformCouponId != null) {
                    OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest = new OrderUseCouponBudgetGoodsDetailRequest();
                    goodsDetailRequest.setEid(vo.getDistributorEid());
                    goodsDetailRequest.setPayMethod(paymentMethodVO.getId().intValue());
                    goodsDetailRequest.setGoodsSkuAmount(NumberUtil.mul(goodsVO.getPrice(), goodsVO.getQuantity()));
                    goodsDetailRequest.setGoodsId(goodsVO.getDistributorGoodsId());
                    goodsDetailRequest.setGoodsSkuId(goodsVO.getGoodsSkuId());
                    goodsDetailRequest.setPlatformCouponId(customerPlatformCouponId);
                    goodsDetailRequest.setCouponId(shopCustomerCouponMap.get(vo.getDistributorEid()));
                    goodsDetailRequests.add(goodsDetailRequest);
                } else if (shopCustomerCouponMap.get(vo.getDistributorEid()) != null) {
                    OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest = new OrderUseCouponBudgetGoodsDetailRequest();
                    goodsDetailRequest.setEid(vo.getDistributorEid());
                    goodsDetailRequest.setPayMethod(paymentMethodVO.getId().intValue());
                    goodsDetailRequest.setGoodsSkuAmount(NumberUtil.mul(goodsVO.getPrice(), goodsVO.getQuantity()));
                    goodsDetailRequest.setGoodsId(goodsVO.getDistributorGoodsId());
                    goodsDetailRequest.setGoodsSkuId(goodsVO.getGoodsSkuId());
                    goodsDetailRequest.setPlatformCouponId(customerPlatformCouponId);
                    goodsDetailRequest.setCouponId(shopCustomerCouponMap.get(vo.getDistributorEid()));
                    goodsDetailRequests.add(goodsDetailRequest);
                }
            }

        });
        if (CollectionUtil.isNotEmpty(goodsDetailRequests)) {
            // 查询优惠劵使用金额
            OrderUseCouponBudgetRequest useCouponRequest = new OrderUseCouponBudgetRequest();
            useCouponRequest.setCurrentEid(currentEid);
            useCouponRequest.setPlatform(PlatformEnum.SALES_ASSIST.getCode());
            useCouponRequest.setGoodsSkuDetailList(goodsDetailRequests);
            OrderUseCouponBudgetDTO budgetDTO = couponActivityApi.orderUseCouponShareAmountBudget(useCouponRequest);
            log.info("..orderUseCouponShareAmountBudget...request ->{},result ->{}", JSON.toJSON(useCouponRequest),JSON.toJSON(budgetDTO));

            Map<Long, OrderUseCouponBudgetEnterpriseDTO> shopUseMap = budgetDTO.getEnterpriseGoodsList().stream().collect(Collectors.toMap(OrderUseCouponBudgetEnterpriseDTO::getEid, Function.identity()));
            for (OrderConfirmVO.OrderSellerVO orderDistributorVO : orderDistributorVOList) {
                OrderUseCouponBudgetEnterpriseDTO enterpriseDTO = shopUseMap.get(orderDistributorVO.getDistributorEid());
                Optional.ofNullable(enterpriseDTO).ifPresent(e -> {
                    orderDistributorVO.setPaymentAmount(enterpriseDTO.getTotalAmount().subtract(enterpriseDTO.getPlatformDiscountAmount()).subtract(enterpriseDTO.getBusinessDiscountAmount()));
                    orderDistributorVO.setShopCouponDiscountMoney(enterpriseDTO.getBusinessDiscountAmount());
                    orderDistributorVO.setPlatformCouponDiscountMoney(enterpriseDTO.getPlatformDiscountAmount());
                });
            }
            pageVO.setCustomerPlatformCouponId(budgetDTO.getPlatformCouponId());
            pageVO.setPlatformCouponDiscountAmount(budgetDTO.getTotalPlatformDiscountAmount());
            pageVO.setShopCouponDiscountAmount(budgetDTO.getTotalBusinessDiscountAmount());
        }

        // 查询订单可用优惠劵活动，并设置优惠劵
        if (CollectionUtil.isNotEmpty(goodsDetailList)) {

            this.selectCouponCanUseList(orderDistributorVOList,shopCustomerCouponMap,currentEid,pageVO,goodsDetailList);
        }
    }

    /**
     * @param orderDTO           配送商名称
     * @param paymentMethodDTOList      支付集合
     * @param shopCustomerCouponId      店铺优惠劵ID
     * @return
     */
    private OrderConfirmVO.OrderSellerVO buildOrderDistributorVo(
            OrderDTO orderDTO,
            List<PaymentMethodDTO> paymentMethodDTOList,
            Integer getPaymentMethod,
            Long shopCustomerCouponId,
            ShopDTO shopOne,
            String buyerMessage,
            List<OrderCouponUseDTO> orderCouponUseDTOList,
            Boolean isSelected
    ) {

        // 校验支付方式
        if (CollUtil.isEmpty(paymentMethodDTOList)) {
            // 如果未查询到支付方式，默认为在线支付
            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
            paymentMethodDTO.setCode(4l);
            paymentMethodDTO.setName("在线支付");
            paymentMethodDTO.setStatus(1);
            paymentMethodDTO.setType(1);
            paymentMethodDTOList = Collections.singletonList(paymentMethodDTO);
        } else {
            boolean result = paymentMethodDTOList.stream().anyMatch(t -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(t.getCode()));

            if (!result) {
                PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
                paymentMethodDTO.setCode(4l);
                paymentMethodDTO.setName("在线支付");
                paymentMethodDTO.setStatus(1);
                paymentMethodDTO.setType(1);
                paymentMethodDTOList.add(paymentMethodDTO);
            }
        }
        // 订单是否已确认
        Boolean isConfirmed = CustomerConfirmEnum.CONFIRMED == CustomerConfirmEnum.getByCode(orderDTO.getCustomerConfirmStatus());
        // 校验选择的支付方式
        if (getPaymentMethod != null) {
            this.checkPaymentMethod(paymentMethodDTOList, getPaymentMethod.longValue());
        }
        OrderConfirmVO.OrderSellerVO orderDistributorVO = new OrderConfirmVO.OrderSellerVO();
        // 设置支付方式
        this.setPaymentMethod(orderDTO,paymentMethodDTOList, Optional.ofNullable(getPaymentMethod).map(e -> e.longValue()).orElse(null), orderDistributorVO);
        // 当前支付方式
        OrderConfirmVO.PaymentMethodVO currentPaymentPayment = orderDistributorVO.getPaymentMethodList().stream().filter(t -> t.getSelected()).findFirst().get();
        // 获取账期浮动值
        BigDecimal upPoint = new BigDecimal(1);
        // 未确认需要计算账期
        if (!isConfirmed && currentPaymentPayment != null && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(currentPaymentPayment.getId().longValue())) {
            PaymentDaysAccountDTO paymentDaysAccountDTO = paymentDaysAccountApi.getByCustomerEid(orderDTO.getDistributorEid(), orderDTO.getBuyerEid());
            if (paymentDaysAccountDTO == null) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS);
            }
            if (paymentDaysAccountDTO.getUpPoint() != null) {
                upPoint = NumberUtil.add(upPoint, NumberUtil.div(paymentDaysAccountDTO.getUpPoint(),100));
            }
        }

        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());
        List<GoodsSkuStandardBasicDTO> distributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(orderDetailDTOList.stream().map(t -> t.getGoodsSkuId()).collect(Collectors.toList()));
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = distributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));
        List<OrderGoodsVO> orderGoodsVOList = CollUtil.newArrayList();
        BigDecimal amount = BigDecimal.ZERO;
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(orderDetailDTO.getGoodsSkuId());
            OrderGoodsVO orderGoodsVO = new OrderGoodsVO();
            // 拷贝商品标准库信息
            PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), orderGoodsVO);
            orderGoodsVO.setGoodsSkuId(orderDetailDTO.getGoodsSkuId());
            //设置配送商商品ID
            orderGoodsVO.setDistributorGoodsId(orderDetailDTO.getDistributorGoodsId());
            orderGoodsVO.setQuantity(orderDetailDTO.getGoodsQuantity());
            orderGoodsVO.setRelationId(orderDetailDTO.getId());
            orderGoodsVO.setPrice(orderDetailDTO.getGoodsPrice());

            // 未确认
            if (!isConfirmed && currentPaymentPayment.getId() != null && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(currentPaymentPayment.getId()) && orderDetailDTO.getDistributorGoodsId().equals(orderDetailDTO.getGoodsId())) {
                // 账期浮动商品价格
                if (CompareUtil.compare(new BigDecimal("1"),upPoint) == 0) {
                    orderGoodsVO.setPrice(orderDetailDTO.getGoodsPrice());
                } else {
                    orderGoodsVO.setPrice(NumberUtil.round(NumberUtil.mul(orderDetailDTO.getGoodsPrice(), upPoint), 2));
                }
            }

            orderGoodsVO.setAmount(orderGoodsVO.getPrice().multiply(BigDecimal.valueOf(orderDetailDTO.getGoodsQuantity())));

            amount = amount.add(orderGoodsVO.getAmount());

            orderGoodsVOList.add(orderGoodsVO);
        }
        orderDistributorVO.setDistributorEid(orderDTO.getDistributorEid());
        orderDistributorVO.setDistributorName(orderDTO.getDistributorEname());
        orderDistributorVO.setOrderGoodsList(orderGoodsVOList);
        orderDistributorVO.setGoodsSpeciesNum(orderGoodsVOList.stream().count());
        orderDistributorVO.setGoodsNum(orderGoodsVOList.stream().mapToLong(OrderGoodsVO::getQuantity).sum());
        orderDistributorVO.setTotalAmount(amount);
        orderDistributorVO.setFreightAmount(BigDecimal.ZERO);
        orderDistributorVO.setPaymentAmount(orderDTO.getPaymentAmount());

        // 确认订单后,订单金额不发生编号
        if (!isConfirmed){
            if (shopOne != null && shopOne.getStartAmount().compareTo(amount) > 0) {
                // 部分企业未满足起配金额，请满足后重新提交
                throw new BusinessException(OrderErrorCode.ORDER_SHOP_START_ERROR);
            }
            orderDistributorVO.setPaymentAmount(amount);
        }

        orderDistributorVO.setShopCouponCount(0);
        orderDistributorVO.setIsUseShopCoupon(false);
        orderDistributorVO.setCustomerShopCouponId(shopCustomerCouponId);
        orderDistributorVO.setShopCouponActivity(Collections.emptyList());
        orderDistributorVO.setBuyerMessage(buyerMessage);
        orderDistributorVO.setOrderId(orderDTO.getId());
        orderDistributorVO.setOrderNo(orderDTO.getOrderNo());
        orderDistributorVO.setShopCouponDiscountMoney(orderDTO.getCouponDiscountAmount());
        orderDistributorVO.setPlatformCouponDiscountMoney(orderDTO.getPlatformCouponDiscountAmount());
        orderDistributorVO.setSelected(isSelected);
        orderDistributorVO.setShopCouponCount(0);
        // 如果订单已经确认，需要查询使用的优惠劵记录
        if (CustomerConfirmEnum.CONFIRMED == CustomerConfirmEnum.getByCode(orderDTO.getCustomerConfirmStatus())
                && CollectionUtil.isNotEmpty(orderCouponUseDTOList)) {
            List<OrderCouponUseDTO>   platformCouponList =  orderCouponUseDTOList.stream().filter(t -> 2 == t.getCouponType()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(platformCouponList)) {
                orderDistributorVO.setShopCouponActivity(this.buildCouponActivitys(platformCouponList));
                orderDistributorVO.setCustomerShopCouponId(platformCouponList.stream().findFirst().get().getCouponId());
                orderDistributorVO.setShopCouponCount(1);
            }
        }

        return orderDistributorVO;
    }


    /**
     *  设置订单使用的优惠劵信息
     * @param couponList
     * @return
     */
    private  List<CouponActivityCanUseDetailVO> buildCouponActivitys( List<OrderCouponUseDTO>   couponList) {
        if (CollectionUtil.isEmpty(couponList)) {
            return ListUtil.empty();
        }
        List<Long> couponActivityIdList = couponList.stream().map(t -> t.getCouponActivityId()).collect(Collectors.toList());
        List<CouponActivityCanUseDetailDTO> couponActivityCanUseDetailDTOList = couponActivityApi.queryByCouponActivityIdList(couponActivityIdList);
        return  couponActivityCanUseDetailDTOList.stream().map(couponActivityCanUseDetailDTO -> {
            CouponActivityCanUseDetailVO detailVO = PojoUtils.map(couponActivityCanUseDetailDTO, CouponActivityCanUseDetailVO.class);
            detailVO.setIsSelected(true);
            return detailVO;
        }).collect(Collectors.toList());
    }


    /**
     * 校验分享链接是否失效
     * @param keyStr
     * @param orderNoList
     * @return
     */
    private boolean checkShareLink(String keyStr,List<String> orderNoList) {
        Boolean successFlag = true;
        for (String orderNo : orderNoList) {
            Object keyStrValue = redisService.get(RedisKey.generate("sa","order","share","link",orderNo));
            if (keyStrValue == null) {
                successFlag = false;
                break;
            }
            if (!keyStrValue.toString().equals(keyStr)) {
                successFlag = false;
                break;
            }
        }
        return successFlag;
    }

    @ApiOperation(value = "客户确认订单-分享订单详情")
    @RequestMapping(path = "/customer/orderProduct", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Result<OrderConfirmVO> selectOrderProductDetails(@Valid @RequestBody ShareOrderProductInfoForm form) {
        String decryptStr = StockUtils.getDecryptStr(form.getKeyStr());
        if ("".equals(decryptStr)) {
            return Result.failed("分享链接无效!");
        }
        if (StringUtils.isBlank(decryptStr)) {
            return Result.failed("订单已过时效性禁止操作，请联系您的专属业务人员处理");
        }
        List<String> orderNoList = Arrays.asList(decryptStr.split(","));

        if (!this.checkShareLink(form.getKeyStr(),orderNoList)) {
            return Result.failed("分享链接已失效,请联系您的商务负责人重新确认支付订单");
        }
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(orderNoList);
        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("订单信息不存在");
        }
        // 校验订单类型
        if (orderDTOList.stream().anyMatch(orderDTO -> OrderTypeEnum.B2B != OrderTypeEnum.getByCode(orderDTO.getOrderType()) || OrderSourceEnum.SA != OrderSourceEnum.getByCode(orderDTO.getOrderSource()))) {
            return Result.failed("订单类型不对!");
        }
        orderDTOList = orderDTOList.stream().filter(orderDTO -> OrderStatusEnum.CANCELED != OrderStatusEnum.getByCode(orderDTO.getOrderStatus())).collect(Collectors.toList());
        // 订单已取消
        if (CollectionUtil.isEmpty(orderDTOList)) {

            return Result.failed("订单已作废，请联系您的商务代表重新下单");
        }
        List<OrderCouponUseDTO> orderCouponUseList = Lists.newArrayList();
        if (orderDTOList.stream().allMatch(t -> CustomerConfirmEnum.CONFIRMED == CustomerConfirmEnum.getByCode(t.getCustomerConfirmStatus()))) {
            orderCouponUseList = orderCouponUseApi.listByOrderIds(orderDTOList.stream().map(t -> t.getId()).collect(Collectors.toList()));
        }
        Map<Long, List<OrderCouponUseDTO>> orderCouponUseMap = orderCouponUseList.stream().collect(Collectors.groupingBy(OrderCouponUseDTO::getOrderId));
        List<Long> sellerEidList = orderDTOList.stream().map(t -> t.getSellerEid()).collect(Collectors.toList());
        List<ShopDTO> shopList = shopApi.listShopByEidList(sellerEidList);
        Map<Long, ShopDTO> shopMap = shopList.stream().collect(Collectors.toMap(ShopDTO::getShopEid, o -> o, (k1, k2) -> k1));
        Map<Long, List<PaymentMethodDTO>> paymentMethodDTOMap = paymentMethodApi.listByCustomerEidAndEids(orderDTOList.get(0).getBuyerEid(), sellerEidList, PlatformEnum.B2B);
        List<OrderConfirmVO.OrderSellerVO> orderSellerVOList = Lists.newArrayList();
        for (OrderDTO orderDTO : orderDTOList) {
            List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodDTOMap.get(orderDTO.getDistributorEid());
            OrderConfirmVO.OrderSellerVO orderSellerVO = this.buildOrderDistributorVo(
                    orderDTO,
                    paymentMethodDTOList,
                    null,
                    null,
                    shopMap.get(orderDTO.getDistributorEid()),
                    "",
                    orderCouponUseMap.get(orderDTO.getId()),
                    true
            );
            orderSellerVOList.add(orderSellerVO);
        }
        OrderDTO orderDto = orderDTOList.get(0);
        OrderAddressDTO orderAddressDto = orderAddressApi.getOrderAddressInfo(orderDto.getId());
        OrderConfirmVO confirmVO = new OrderConfirmVO();
        confirmVO.setOrderDistributorList(orderSellerVOList);
        confirmVO.setName(orderAddressDto.getName());
        confirmVO.setAddress(orderAddressDto.getAddress());
        confirmVO.setMobile(orderAddressDto.getMobile());
        confirmVO.setBuyerName(orderDto.getBuyerEname());
        confirmVO.setOrderTime(orderDto.getCreateTime());
        confirmVO.setIsUsePlatformCoupon(false);
        confirmVO.setShopCouponDiscountAmount(orderSellerVOList.stream().map(t -> t.getShopCouponDiscountMoney()).reduce(BigDecimal::add).get());
        confirmVO.setPlatformCouponDiscountAmount(orderSellerVOList.stream().map(t -> t.getPlatformCouponDiscountMoney()).reduce(BigDecimal::add).get());
        // 订单确认状态
        confirmVO.setOrderConfirmStatus(orderDTOList.stream().allMatch(t -> CustomerConfirmEnum.CONFIRMED == CustomerConfirmEnum.getByCode(t.getCustomerConfirmStatus())));
        // 设置是否需要支付,如果未确认一定需要支付
        if (confirmVO.getOrderConfirmStatus()) {
            List<OrderDTO> onlineOrderList = orderDTOList.stream().filter(t -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(t.getPaymentMethod().longValue())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(onlineOrderList)) {
                confirmVO.setPaymentStatus(false);
            } else {
                confirmVO.setPaymentStatus(onlineOrderList.stream().anyMatch(t -> PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(t.getPaymentStatus())));
            }

        } else {
            confirmVO.setPaymentStatus(true);
        }
        confirmVO.setPlatformCouponActivity(ListUtil.empty());
        // 优惠券信息
        if (!confirmVO.getOrderConfirmStatus()) {
            this.setCouponInfo(orderSellerVOList,Collections.emptyMap(),null,orderDto.getBuyerEid(),confirmVO);
        } else {
            if (CollectionUtil.isNotEmpty(orderCouponUseList)) {
                List<OrderCouponUseDTO>   platformCouponList =  orderCouponUseList.stream().filter(t -> 1 == t.getCouponType()).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(platformCouponList)) {
                    confirmVO.setPlatformCouponActivity(this.buildCouponActivitys(platformCouponList));
                    confirmVO.setCustomerPlatformCouponId(platformCouponList.stream().findFirst().get().getCouponId());
                    confirmVO.setPlatformCouponCount(1);
                }
            }
        }
        confirmVO.setGoodsSpeciesNum(orderSellerVOList.stream().mapToLong(OrderConfirmVO.OrderSellerVO::getGoodsSpeciesNum).sum());
        confirmVO.setGoodsNum(orderSellerVOList.stream().mapToLong(OrderConfirmVO.OrderSellerVO::getGoodsNum).sum());
        confirmVO.setTotalAmount(orderSellerVOList.stream().map(OrderConfirmVO.OrderSellerVO::getTotalAmount).reduce(BigDecimal::add).get());
        confirmVO.setFreightAmount(orderSellerVOList.stream().map(OrderConfirmVO.OrderSellerVO::getFreightAmount).reduce(BigDecimal::add).get());
        confirmVO.setPaymentAmount(orderSellerVOList.stream().map(OrderConfirmVO.OrderSellerVO::getPaymentAmount).reduce(BigDecimal::add).get());
        Optional.ofNullable(orderDto.getCreateUser()).ifPresent(t -> {
            UserDTO userDTO = userApi.getById(t);
            confirmVO.setContacterName(userDTO.getName());
            confirmVO.setContacterPhone(userDTO.getMobile());
        });

        return Result.success(confirmVO);
    }

    /**
     * 构建前台传递参数
     * @param orderDTOMap
     * @param checkCouponForm
     * @param shopCustomerCouponMap
     * @param buyerMessageMap
     * @param paymentMethodMap
     */
    private void buildCustomerParam(
            Map<Long,OrderDTO> orderDTOMap,
            CheckCouponForm checkCouponForm,
            Map<Long, Long> shopCustomerCouponMap,
            Map<Long, String> buyerMessageMap,
            Map<Long, Integer> paymentMethodMap,
            Map<Long,Boolean> selectedMap
    ) {
        if (checkCouponForm == null) {
            return;
        }
        if (CollectionUtil.isEmpty(checkCouponForm.getOrderList())) {
            return;
        }
        for (CheckCouponForm.OrderForm distributorForm : checkCouponForm.getOrderList()) {
            selectedMap.put(distributorForm.getOrderId(),distributorForm.getSelected());
            OrderDTO OrderDto = orderDTOMap.get(distributorForm.getOrderId());
            if (distributorForm.getShopCustomerCouponId() != null && distributorForm.getShopCustomerCouponId() != 0l) {
                shopCustomerCouponMap.put(OrderDto.getDistributorEid(), distributorForm.getShopCustomerCouponId());
            }
            if (StringUtils.isNotBlank(distributorForm.getBuyerMessage())) {
                buyerMessageMap.put(OrderDto.getDistributorEid(), distributorForm.getBuyerMessage());
            }
            if (distributorForm.getPaymentMethod() != null) {
                paymentMethodMap.put(OrderDto.getDistributorEid(), distributorForm.getPaymentMethod());
            }
        }
    }

    @ApiOperation(value = "客户确认订单-选择支付方式/选择优惠劵")
    @RequestMapping(path = "/customer/checkCoupon", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Result<OrderConfirmVO> checkCoupon(@Valid @RequestBody CheckCouponForm form) {
        String decryptStr = StockUtils.getDecryptStr(form.getKeyStr());
        if ("".equals(decryptStr)) {

            return Result.failed("订单不存在");
        }
        if (StringUtils.isBlank(decryptStr)) {

            return Result.failed("订单已过时效性禁止操作，请联系您的专属业务人员处理");
        }
        List<String> orderNoList = Arrays.asList(decryptStr.split(","));
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(orderNoList);
        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("订单信息不存在");
        }
        // 校验订单类型
        if (orderDTOList.stream().anyMatch(orderDTO -> OrderTypeEnum.B2B != OrderTypeEnum.getByCode(orderDTO.getOrderType()) || OrderSourceEnum.SA != OrderSourceEnum.getByCode(orderDTO.getOrderSource()))) {
            return Result.failed("订单类型不对!");
        }
        if (orderDTOList.stream().allMatch(t -> CustomerConfirmEnum.CONFIRMED == CustomerConfirmEnum.getByCode(t.getCustomerConfirmStatus()))) {

            return Result.failed("订单已确认，请联系您的商务代表重新下单!");
        }

        Map<Long,OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId,Function.identity()));

        Map<Long, Long> shopCustomerCouponMap = Maps.newHashMap();
        // 卖家留言
        Map<Long, String> buyerMessageMap = Maps.newHashMap();
        // 支付方式map
        Map<Long, Integer> paymentMethodMap = Maps.newHashMap();
        Map<Long,Boolean> selectedMap = Maps.newHashMap();
        this.buildCustomerParam(orderDTOMap,form,shopCustomerCouponMap,buyerMessageMap,paymentMethodMap,selectedMap);
        // 平台优惠劵
        Long customerPlatformCouponId = Optional.ofNullable(form).map(e -> { return ObjectUtil.isNull(e.getPlatformCustomerCouponId()) || e.getPlatformCustomerCouponId() == 0l ? null : e.getPlatformCustomerCouponId();}).orElse(null);
        List<Long> sellerEidList = orderDTOList.stream().map(t -> t.getSellerEid()).collect(Collectors.toList());
        List<ShopDTO> shopList = shopApi.listShopByEidList(sellerEidList);
        Map<Long, ShopDTO> shopMap = shopList.stream().collect(Collectors.toMap(ShopDTO::getShopEid, o -> o, (k1, k2) -> k1));
        Map<Long, List<PaymentMethodDTO>> paymentMethodDTOMap = paymentMethodApi.listByCustomerEidAndEids(orderDTOList.get(0).getBuyerEid(), sellerEidList, PlatformEnum.B2B);
        List<OrderConfirmVO.OrderSellerVO> orderSellerVOList = Lists.newArrayList();
        for (OrderDTO orderDTO : orderDTOList) {
            List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodDTOMap.get(orderDTO.getDistributorEid());
            OrderConfirmVO.OrderSellerVO orderSellerVO = this.buildOrderDistributorVo(
                    orderDTO,
                    paymentMethodDTOList,
                    paymentMethodMap.get(orderDTO.getDistributorEid()),
                    shopCustomerCouponMap.get(orderDTO.getDistributorEid()),
                    shopMap.get(orderDTO.getDistributorEid()),
                    buyerMessageMap.get(orderDTO.getDistributorEid()),
                    ListUtil.empty(),
                    selectedMap.get(orderDTO.getId())
            );
            orderSellerVOList.add(orderSellerVO);
        }
        OrderDTO orderDto = orderDTOList.get(0);
        OrderAddressDTO orderAddressDto = orderAddressApi.getOrderAddressInfo(orderDto.getId());
        OrderConfirmVO confirmVO = new OrderConfirmVO();
        confirmVO.setOrderDistributorList(orderSellerVOList);
        confirmVO.setName(orderAddressDto.getName());
        confirmVO.setAddress(orderAddressDto.getAddress());
        confirmVO.setMobile(orderAddressDto.getMobile());
        confirmVO.setBuyerName(orderDto.getBuyerEname());
        confirmVO.setOrderTime(orderDto.getCreateTime());
        confirmVO.setIsUsePlatformCoupon(false);
        confirmVO.setShopCouponDiscountAmount(BigDecimal.ZERO);
        confirmVO.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        confirmVO.setTotalAmount(BigDecimal.ZERO);
        confirmVO.setFreightAmount(BigDecimal.ZERO);
        confirmVO.setPaymentAmount(BigDecimal.ZERO);
        // 订单确认状态
        confirmVO.setOrderConfirmStatus(orderDTOList.stream().allMatch(t -> CustomerConfirmEnum.CONFIRMED == CustomerConfirmEnum.getByCode(t.getCustomerConfirmStatus())));
        if (confirmVO.getOrderConfirmStatus()) {
            List<OrderDTO> onlineOrderList = orderDTOList.stream().filter(t -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(t.getPaymentMethod().longValue())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(onlineOrderList)) {
                confirmVO.setPaymentStatus(false);
            } else {
                confirmVO.setPaymentStatus(onlineOrderList.stream().anyMatch(t -> PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(t.getPaymentStatus())));
            }
        } else {
            confirmVO.setPaymentStatus(true);
        }
        confirmVO.setPlatformCouponActivity(ListUtil.empty());
        // 优惠券信息
        if (!confirmVO.getOrderConfirmStatus()) {
            this.setCouponInfo(orderSellerVOList,shopCustomerCouponMap,customerPlatformCouponId,orderDto.getBuyerEid(),confirmVO);
        }
        confirmVO.setGoodsSpeciesNum(orderSellerVOList.stream().mapToLong(OrderConfirmVO.OrderSellerVO::getGoodsSpeciesNum).sum());
        confirmVO.setGoodsNum(orderSellerVOList.stream().mapToLong(OrderConfirmVO.OrderSellerVO::getGoodsNum).sum());
        List<OrderConfirmVO.OrderSellerVO> selectOrderList = orderSellerVOList.stream().filter(t -> t.getSelected()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(selectOrderList)) {
            confirmVO.setTotalAmount(selectOrderList.stream().map(OrderConfirmVO.OrderSellerVO::getTotalAmount).reduce(BigDecimal::add).get());
            confirmVO.setFreightAmount(selectOrderList.stream().map(OrderConfirmVO.OrderSellerVO::getFreightAmount).reduce(BigDecimal::add).get());
            confirmVO.setPaymentAmount(selectOrderList.stream().map(OrderConfirmVO.OrderSellerVO::getPaymentAmount).reduce(BigDecimal::add).get());
        }
        Optional.ofNullable(orderDto.getCreateUser()).ifPresent(t -> {
            UserDTO userDTO = userApi.getById(t);
            confirmVO.setContacterName(userDTO.getName());
            confirmVO.setContacterPhone(userDTO.getMobile());
        });

        if (confirmVO.getOrderConfirmStatus()) {
            return Result.success(confirmVO);
        }
        // 账期支付的配送订单
        List<OrderConfirmVO.OrderSellerVO> paymentDayDistributorList = this.selectPaymentDaysOrder(orderSellerVOList);
        if (CollectionUtil.isEmpty(paymentDayDistributorList)) {
            return Result.success(confirmVO);
        }

        // 账期支付金额校验
        for (OrderConfirmVO.OrderSellerVO t : paymentDayDistributorList) {
            BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getB2bAvailableAmountByCustomerEid(t.getDistributorEid(), orderDto.getBuyerEid());
            // 订单应付金额
            if (paymentDaysAvailableAmount.compareTo(t.getPaymentAmount()) == -1) {

                return Result.failed("采购商可以选择其它支付方式或者补足额度后再选择该支付方式");
            }
        }
        return Result.success(confirmVO);
    }

    /**
     * 找出账期支付的订单
     *
     * @param paymentDayDistributorList
     * @return
     */
    private List<OrderConfirmVO.OrderSellerVO> selectPaymentDaysOrder(List<OrderConfirmVO.OrderSellerVO> paymentDayDistributorList) {

        return paymentDayDistributorList.stream().filter(e -> {
            if (CollectionUtil.isEmpty(e.getPaymentMethodList())) {
                return false;
            }
            return e.getPaymentMethodList().stream().filter(t -> t.getSelected() && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(t.getId().longValue())).findFirst().isPresent();

        }).collect(Collectors.toList());

    }

    @ApiOperation(value = "订单列表取消订单")
    @RequestMapping(path = "/orderList/cancel", method = {RequestMethod.GET})
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Void> cancelOrder(@CurrentUser CurrentStaffInfo staffInfo,@ApiParam(value = "订单ID", required = true) @RequestParam(value = "orderId", required = true) Long orderId) {
        OrderDTO orderDto = orderApi.getOrderInfo(orderId);
        if (orderDto == null) {
            return Result.failed("订单不存在");
        }
        if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderDto.getOrderStatus())) {
            return Result.failed("订单已取消,请勿重复操作!");
        }
        boolean result =  orderProcessApi.cancel(orderId,staffInfo.getCurrentUserId());

        if (!result) {
            return Result.failed("订单取消失败!");
        }
        return Result.success();
    }

    @ApiOperation(value = "分享页面客户取消订单")
    @RequestMapping(path = "/customer/cancelOrder", method = {RequestMethod.POST})
    public Result<Void> customerCancelOrder(@Valid @RequestBody ShareOrderProductInfoForm form) {
        String decryptStr = StockUtils.getDecryptStr(form.getKeyStr());
        if ("".equals(decryptStr)) {

            return Result.failed("订单不存在");
        }
        if (StringUtils.isBlank(decryptStr)) {

            return Result.failed("订单已过时效性禁止操作，请联系您的专属业务人员处理");
        }
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(Arrays.asList(decryptStr.split(",")));
        if (CollectionUtil.isEmpty(orderDTOList)) {

            return Result.failed("订单信息不存在");
        }
        boolean checkOrderType = orderDTOList.stream().filter(e -> OrderTypeEnum.B2B != OrderTypeEnum.getByCode(e.getOrderType())
                || OrderSourceEnum.SA != OrderSourceEnum.getByCode(e.getOrderSource())).findAny().isPresent();
        if (checkOrderType) {

            return Result.failed("订单类型不对!");
        }
        boolean checkOrderStatus = orderDTOList.stream().allMatch(e -> OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(e.getOrderStatus()));
        if (checkOrderStatus) {

            return Result.failed("订单已作废，请联系您的商务代表重新下单!");
        }
        boolean isPaid = orderDTOList.stream().allMatch(e -> OrderStatusEnum.UNAUDITED != OrderStatusEnum.getByCode(e.getOrderStatus())
                || PaymentStatusEnum.PAID  == PaymentStatusEnum.getByCode(e.getPaymentStatus()));

        if (isPaid) {
            return Result.failed("订单待发货，请联系您的商务代表取消下单!");
        }
        saOrderApi.userCancelB2BOrder(Arrays.asList(decryptStr.split(",")));
        return Result.success();
    }

    @ApiOperation(value = "客户确认订单")
    @RequestMapping(path = "/customer/confirmOrder", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public Result<ConfirmOrderResultVO> customerConfirmOrder(@Valid @RequestBody OrderConfirmForm form) {
       /* String mobile = form.getMobile();
        String verifyCode = form.getVerifyCode();*/

        String decryptStr = StockUtils.getDecryptStr(form.getKeyStr());
        if ("".equals(decryptStr)) {
            return Result.failed("订单不存在");
        }
        if (StringUtils.isBlank(decryptStr)) {
            return Result.failed("订单已过时效性禁止操作，请联系您的专属业务人员处理");
        }
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(Arrays.asList(decryptStr.split(",")));
        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("订单信息不存在");
        }
        List<Long> checkOrderIdList =  form.getOrderList().stream().map(t -> t.getOrderId()).collect(Collectors.toList());
        List<OrderDTO> checkOrderList = orderApi.listByIds(checkOrderIdList);
        if (checkOrderIdList.size() != checkOrderList.size()) {
            return Result.failed("部分订单不存在");
        }
        boolean checkOrderType = checkOrderList.stream().filter(e -> OrderTypeEnum.B2B != OrderTypeEnum.getByCode(e.getOrderType())
                || OrderSourceEnum.SA != OrderSourceEnum.getByCode(e.getOrderSource())).findAny().isPresent();
        if (checkOrderType) {
            return Result.failed("订单类型不对!");
        }
        boolean checkCustomerConfirmStatus = checkOrderList.stream().filter(
                e -> CustomerConfirmEnum.NOTCONFIRM != CustomerConfirmEnum.getByCode(e.getCustomerConfirmStatus())
                        ||  OrderStatusEnum.UNAUDITED !=  OrderStatusEnum.getByCode(e.getOrderStatus()))
                .findAny().isPresent();

        if (checkCustomerConfirmStatus) {
            return Result.failed("客户确认状态已变更!");
        }

     /*   // 短信验证码校验
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(mobile, verifyCode, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_ORDER_CONFIRM);

        if (!checkVerifyCodeResult) {

            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }*/

        // 分享订单
        List<Long> shareOrderIdList = orderDTOList.stream().map(t -> t.getId()).collect(Collectors.toList());
        // 确认取消订单
        List<Long> cancelList = shareOrderIdList.stream().filter(t -> !checkOrderIdList.contains(t)).collect(Collectors.toList());

        Map<Long,OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(t -> t.getId(),Function.identity()));
        OrderConfirmRequest confirmRequest = new OrderConfirmRequest();
        confirmRequest.setPlatformCustomerCouponId(form.getPlatformCustomerCouponId());
        confirmRequest.setOpUserId(0l);
        confirmRequest.setBuyerEid(orderDTOList.get(0).getBuyerEid());
        List<OrderConfirmRequest.DistributorOrderDTO> distributorOrderList = form.getOrderList().stream().map(t -> {
            OrderConfirmRequest.DistributorOrderDTO distributorOrderDTO = new OrderConfirmRequest.DistributorOrderDTO();
            distributorOrderDTO.setOrderId(t.getOrderId());
            distributorOrderDTO.setOrderNo(orderDTOMap.get(t.getOrderId()).getOrderNo());
            distributorOrderDTO.setShopCustomerCouponId(t.getShopCustomerCouponId());
            distributorOrderDTO.setPaymentMethod(t.getPaymentMethod());
            distributorOrderDTO.setBuyerMessage(t.getBuyerMessage());
            distributorOrderDTO.setSellerEid(orderDTOMap.get(t.getOrderId()).getSellerEid());
            distributorOrderDTO.setDistributorEid(orderDTOMap.get(t.getOrderId()).getDistributorEid());
            return distributorOrderDTO;
        }).collect(Collectors.toList());
        confirmRequest.setDistributorOrderList(distributorOrderList);
        confirmRequest.setCancelOrderIds(cancelList);
        OrderSubmitBO submitBO = saOrderApi.b2bConfirmCustomerOrder(confirmRequest);
        // 在线支付订单
        List<OrderDTO> onlineOrderList = submitBO.getOrderDTOList().stream().filter(
                e -> PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.UNPAID
                        && PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())
        ).collect(Collectors.toList());

        Boolean hasOnlinePay = false;
        BigDecimal payMoney = BigDecimal.ZERO;

        if (CollectionUtil.isNotEmpty(onlineOrderList)) {
            hasOnlinePay = true;
            payMoney = onlineOrderList.stream().map(OrderDTO::getPaymentAmount).reduce(BigDecimal::add).get();
        }

        Long buyerEid = orderDTOList.stream().findFirst().get().getBuyerEid();
        String buyerName = orderDTOList.stream().findFirst().get().getBuyerEname();
        List<String> orderNos = submitBO.getOrderDTOList().stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());
        ConfirmOrderResultVO resultVO = new ConfirmOrderResultVO();
        resultVO.setOrderCodeList(orderNos);
        resultVO.setPayMoney(payMoney);
        resultVO.setHasOnlinePay(hasOnlinePay);
        resultVO.setPayId(submitBO.getPayId());
        resultVO.setBuyerEid(buyerEid);
        resultVO.setBuyerName(buyerName);

        // 清理短信验证码
        // smsApi.cleanVerifyCode(mobile, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_ORDER_CONFIRM);

        return Result.success(resultVO);
    }

    /**
     * 待发送确认订单列表
     * @param staffInfo
     * @param form
     * @return
     */
    @ApiOperation(value = "B2B提交订单-发给客户")
    @RequestMapping(path = "/customer/sendCustomerList", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CustomerSendOrderVO> sendCustomerList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm form) {
        if (staffInfo.getYilingFlag()) {
            return Result.failed("以岭内部人员无此操作权限!");
        }
        List<OrderDTO> resultList = orderApi.listByOrderNos(form.getOrderNos());
        if (CollectionUtil.isEmpty(resultList)) {
            return Result.success();
        }
        List<CustomerSendOrderVO.SendOrderVO> sendOrderVOResultList = resultList.stream().map(t -> {
            CustomerSendOrderVO.SendOrderVO sendOrderVO = new CustomerSendOrderVO.SendOrderVO();
            sendOrderVO.setOrderNo(t.getOrderNo());
            sendOrderVO.setAmount(t.getTotalAmount());
            sendOrderVO.setPaymentAmount(t.getPaymentAmount());
            sendOrderVO.setSellerEname(t.getSellerEname());
            sendOrderVO.setId(t.getId());
            return sendOrderVO;
        }).collect(Collectors.toList());
        BigDecimal totalAmount = sendOrderVOResultList.stream().map(CustomerSendOrderVO.SendOrderVO::getAmount).reduce(BigDecimal::add).get();
        BigDecimal totalPayAmount = sendOrderVOResultList.stream().map(CustomerSendOrderVO.SendOrderVO::getPaymentAmount).reduce(BigDecimal::add).get();

        CustomerSendOrderVO vo = new CustomerSendOrderVO();
        vo.setTotalAmount(totalAmount);
        vo.setTotalPayAmount(totalPayAmount);
        vo.setSendOrderVoList(sendOrderVOResultList);

        return Result.success(vo);
    }


    /**
     * POP需要设置部门ID
     * @param request
     */
    private void setDepartmentId(OrderSubmitRequest request) {

        if (OrderTypeEnum.POP != request.getOrderTypeEnum()) {

            return;
        }

        Map<Long, List<EnterpriseDepartmentDTO>> departmentByEidUserMap = departmentApi.getDepartmentByEidUser(Constants.YILING_EID, ListUtil.toList(request.getContacterId()));

        if (log.isDebugEnabled()) {

            log.debug("getDepartmentByEidUser...query..param:{},...result:{}",request.getContacterId(),departmentByEidUserMap);
        }

        List<EnterpriseDepartmentDTO> enterpriseDepartmentDTOS = departmentByEidUserMap.get(request.getContacterId());

        if (CollectionUtil.isNotEmpty(enterpriseDepartmentDTOS)) {

            request.setDepartmentId(enterpriseDepartmentDTOS.stream().findFirst().get().getId());
        }
    }


    @ApiOperation(value = "提交订单")
    @PostMapping("/submit")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<SubmitResultVO> submit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody OrderSubmitForm form) {
        // 获取以岭子企业ID列表
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 工业直属企业信息
        List<Long> industryDirectEids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
        // 配送商ID信息
        List<Long> distributorEids = form.getDistributorOrderList().stream().map(e -> e.getDistributorEid()).collect(Collectors.toList());
        Class[] groups;
        // pop订单并且是直采或者供应直属的需要上传劳动合同
        if (staffInfo.getYilingFlag() && this.showEasAccountInfoFlag(staffInfo.getYilingFlag(), yilingSubEids, distributorEids)) {
            groups = new Class[]{OrderSubmitForm.YilingOrderSubmitValidateGroup.class};
        } else {
            groups = new Class[]{OrderSubmitForm.CommonOrderSubmitValidateGroup.class};
        }
        String errorMessage = ValidateUtils.failFastValidate(form, groups);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return Result.validateFailed(errorMessage);
        }

        for (OrderSubmitForm.DistributorOrderForm distributorOrderForm : form.getDistributorOrderList()) {
            if (staffInfo.getYilingFlag() && (yilingSubEids.contains(distributorOrderForm.getDistributorEid()) || industryDirectEids.contains(distributorOrderForm.getDistributorEid()))) {
                groups = new Class[]{OrderSubmitForm.YilingOrderSubmitValidateGroup.class};
            } else {
                groups = new Class[]{OrderSubmitForm.CommonOrderSubmitValidateGroup.class};
            }

            errorMessage = ValidateUtils.failFastValidate(distributorOrderForm, groups);
            if (StrUtil.isNotEmpty(errorMessage)) {
                return Result.validateFailed(errorMessage);
            }
        }
        OrderSubmitRequest request = PojoUtils.map(form, OrderSubmitRequest.class);
        if (staffInfo.getYilingFlag()) {
            request.setOrderTypeEnum(OrderTypeEnum.POP);
        } else {
            request.setOrderTypeEnum(OrderTypeEnum.B2B);
        }
        request.setOrderSourceEnum(OrderSourceEnum.SA);
        request.setBuyerEid(form.getCustomerEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setContacterId(staffInfo.getCurrentUserId());
        // 设置部门ID
        this.setDepartmentId(request);
        request.setIpAddress(IPUtils.getIp(httpRequest));
        UserAgent userAgent = UserAgentUtil.parse(httpRequest.getHeader("User-Agent"));
        request.setUserAgent(JSONObject.toJSONString(userAgent));

        OrderSubmitBO orderSubmitBO = orderProcessApi.submit(request);
        List<String> orderNos = orderSubmitBO.getOrderDTOList().stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());
        Long customerEid = orderSubmitBO.getOrderDTOList().stream().findFirst().get().getBuyerEid();
        String customerName = orderSubmitBO.getOrderDTOList().stream().findFirst().get().getBuyerEname();
        SubmitResultVO submitResultVO =  SubmitResultVO.builder().orderCodeList(orderNos).customerEid(customerEid).customerName(customerName).build();

        return Result.success(submitResultVO);
    }


    @ApiOperation(value = "订单查看-查询订单发货批次信息")
    @PostMapping("/delivery/batchList")
    @UserAccessAuthentication
    public Result<OrderDeliveryBatchVO> findDeliveryBatchList(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody QueryOrderDetailForm orderDetailForm) {
        OrderDetailDTO orderDetailDto = orderDetailApi.getOrderDetailById(orderDetailForm.getOrderDetailId());
        if (orderDetailDto == null) {
            return Result.failed("未查询到订单明细信息!");
        }
        List<OrderDeliveryDTO> resultList = orderDeliveryApi.getOrderDeliveryList(orderDetailForm.getOrderId(),orderDetailForm.getOrderDetailId());
        if (CollectionUtil.isEmpty(resultList)) {
            return Result.failed("未查询发货信息!");
        }
        List<StandardGoodsBasicDTO> distributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(Collections.singletonList(orderDetailDto.getGoodsId()));
        if (CollectionUtil.isEmpty(distributorGoodsDTOList)) {
            return Result.failed("未查询到商品标准库信息");
        }
        OrderDeliveryBatchVO vo = new OrderDeliveryBatchVO();
        PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(distributorGoodsDTOList.stream().findFirst().get()), vo);
        List<OrderDeliveryBatchVO.BatchVO> batchVOList =
                resultList.stream().map(dto -> {
                    OrderDeliveryBatchVO.BatchVO batchVO = PojoUtils.map(dto,OrderDeliveryBatchVO.BatchVO.class);
                    return batchVO;
                }).filter(t -> ObjectUtil.isNotNull(t)).collect(Collectors.toList());
        vo.setBatchVOList(batchVOList);
        vo.setGoodsId(orderDetailDto.getGoodsId());

        return  Result.success(vo);
    }

    @ApiOperation(value = "在线订单客户去支付")
    @PostMapping("/toPay")
    public Result<OrderPayVO> toPay(@Valid @RequestBody ShareOrderProductInfoForm form) {
        String decryptStr = StockUtils.getDecryptStr(form.getKeyStr());
        if ("".equals(decryptStr)) {
            return Result.failed("订单不存在");
        }
        if (StringUtils.isBlank(decryptStr)) {

            return Result.failed("订单已过时效性禁止操作，请联系您的专属业务人员处理");
        }
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(Arrays.asList(decryptStr.split(",")));
        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("订单信息不存在");
        }
        boolean checkOrderType = orderDTOList.stream().filter(e -> OrderTypeEnum.B2B != OrderTypeEnum.getByCode(e.getOrderType())
                || OrderSourceEnum.SA != OrderSourceEnum.getByCode(e.getOrderSource())).findAny().isPresent();
        if (checkOrderType) {

            return Result.failed("订单类型不对!");
        }
        orderDTOList = orderDTOList.stream().filter(t -> OrderStatusEnum.CANCELED != OrderStatusEnum.getByCode(t.getOrderStatus())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("订单已作废，请联系您的商务代表重新下单!");
        }
        if (orderDTOList.stream().allMatch(t -> CustomerConfirmEnum.CONFIRMED != CustomerConfirmEnum.getByCode(t.getCustomerConfirmStatus()))) {
            return Result.failed("请选择已确认未支付订单!");
        }
        orderDTOList = orderDTOList.stream().filter(t -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(t.getPaymentMethod().longValue())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("请选择在线支付订单!");
        }
        boolean needPay = orderDTOList.stream().anyMatch(t -> PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(t.getPaymentStatus()) && PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(t.getPaymentMethod().longValue()));
        if (!needPay) {
            return Result.failed("请选择已确认未支付订单");
        }
        // 过滤出，未支付，并且需要在线支付的订单记录
        orderDTOList = orderDTOList.stream().filter(t -> PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(t.getPaymentStatus())).collect(Collectors.toList());
        String payId = this.initPaymentOrder(orderDTOList);
        if (StringUtils.isBlank(payId)) {
            return Result.failed("请选择未支付的在线支付订单!");
        }
        OrderPayVO orderPayVO = new OrderPayVO();
        orderPayVO.setPayId(payId);

        return Result.success(orderPayVO);
    }


    /**
     * 在线支付，创建支付交易流水
     * @param orderDTOList
     * @return
     */
    private String initPaymentOrder(List<OrderDTO> orderDTOList) {
        // 在线支付订单
        List<OrderDTO> onlineOrderList = orderDTOList.stream().filter(
                e -> PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.UNPAID
                        && PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())
        ).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(onlineOrderList)) {
            return "";
        }
        List<OrderDTO> paymentDayList = orderDTOList.stream().filter(
                e -> PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())
        ).collect(Collectors.toList());
        List<OrderDTO> offlineOrderList = orderDTOList.stream().filter(
                e -> PaymentMethodEnum.OFFLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())
        ).collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        builder.append("订单包含");
        Integer paymentDayNum = Optional.ofNullable(paymentDayList).map(e-> e.size()).orElse(0);
        Integer offlineOrderNum = Optional.ofNullable(offlineOrderList).map(e-> e.size()).orElse(0);

        if (onlineOrderList.size() > 0) {
            builder.append(onlineOrderList.size() + "笔在线支付 ");
        }
        if (offlineOrderNum > 0) {
            builder.append(offlineOrderNum + "笔线下支付 ");
        }
        if (paymentDayNum > 0) {
            builder.append(paymentDayNum + "笔账期支付 ");
        }

        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = new ArrayList<>(onlineOrderList.size());
        for (OrderDTO orderDto : onlineOrderList) {
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(orderDto.getCreateUser());
            request.setAppOrderId(orderDto.getId());
            request.setAppOrderNo(orderDto.getOrderNo());
            request.setAmount(orderDto.getPaymentAmount());
            request.setBuyerEid(orderDto.getBuyerEid());
            request.setSellerEid(orderDto.getSellerEid());
            appOrderList.add(request);
        }
        payOrderRequest.setTradeType(TradeTypeEnum.PAY);
        payOrderRequest.setContent(builder.toString());
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(onlineOrderList.get(0).getCreateUser());
        Result<String> createResult =  payApi.createPayOrder(payOrderRequest);

        if (HttpStatus.HTTP_OK != createResult.getCode()) {
            throw new BusinessException(com.yiling.payment.enums.PaymentErrorCode.ORDER_PAID_ERROR);
        }
        return createResult.getData();
    }

    @ApiOperation(value = "POP确认订单")
    @PostMapping("/pop/confirm")
    @UserAccessAuthentication
    public Result<SubmitResultVO> pay(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid PaymentForm form) {
        if (!staffInfo.getYilingFlag()) {
            return Result.failed("非以岭内部人员，无此操作权限!");
        }
        List<PopOrderConfirmRequest.DistributorOrderDTO> orderDTOs = Lists.newArrayList();
        for (PaymentForm.OrderPaymentForm orderPaymentForm : form.getOrderPaymentList()) {
            PopOrderConfirmRequest.DistributorOrderDTO orderDTO = new  PopOrderConfirmRequest.DistributorOrderDTO();
            orderDTO.setOrderId(orderPaymentForm.getOrderId());
            orderDTO.setPaymentMethodId(orderPaymentForm.getPaymentMethodId());
            orderDTO.setBuyerMessage(orderPaymentForm.getBuyerMessage());
            orderDTOs.add(orderDTO);
        }
        boolean result = saOrderApi.popConfirmOrder(new PopOrderConfirmRequest().setOrderPaymentList(orderDTOs));
        if (result) {
            List<Long> orderIds = form.getOrderPaymentList().stream().map(t -> t.getOrderId()).collect(Collectors.toList());
            List<OrderDTO> orderDTOList = orderApi.listByIds(orderIds);
            SubmitResultVO resultVo = SubmitResultVO.builder().orderCodeList(orderDTOList.stream().map(t -> t.getOrderNo()).collect(Collectors.toList()))
                    .customerName(orderDTOList.stream().findFirst().get().getBuyerEname())
                    .customerEid(orderDTOList.stream().findFirst().get().getBuyerEid())
                    .build();

            return Result.success(resultVo);
        }
        return Result.failed("确认失败!");
    }


    @ApiOperation(value = "获取确认订单验证码")
    @PostMapping("/getConfirmVerifyCode")
    public Result getConfirmVerifyCode(@RequestBody @Valid GetConfirmVerifyCodeForm form) {

        String decryptStr = StockUtils.getDecryptStr(form.getKeyStr());
        if ("".equals(decryptStr)) {
            return Result.failed("订单不存在");
        }

        if (StringUtils.isBlank(decryptStr)) {

            return Result.failed("订单已过时效性禁止操作，请联系您的专属业务人员处理");
        }

        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(Arrays.asList(decryptStr.split(",")));

        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("订单信息不存在");
        }

        // 订单购买企业Id
        Long buyerEid =  orderDTOList.stream().findFirst().get().getBuyerEid();

        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);

        if (staff == null) {

            return Result.failed("您的手机号尚未注册或未加入企业");
        }

        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return Result.failed(LoginErrorCode.ACCOUNT_DISABLED);
        }

        // 校验注销账号不可登录
        if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DEREGISTER) {
            return Result.failed(LoginErrorCode.ACCOUNT_HAD_LOGOUT);
        }

        UserDeregisterAccountDTO deregisterAccountDTO = userDeregisterAccountApi.getByUserId(staff.getId());
        if (Objects.nonNull(deregisterAccountDTO)) {
            if (deregisterAccountDTO.getStatus().equals(UserDeregisterAccountStatusEnum.WAITING_DEREGISTER.getCode()) ) {
                return Result.failed(LoginErrorCode.ACCOUNT_DEREGISTERING);
            }
        }

        EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(buyerEid, staff.getId());

        if (employeeDTO == null) {

            return Result.failed("非用户本人登录账号,请重新填写");
        }

        if (EnableStatusEnum.DISABLED == EnableStatusEnum.getByCode(employeeDTO.getStatus())) {

            return Result.failed(LoginErrorCode.ACCOUNT_DISABLED);
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_ORDER_CONFIRM);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }
}
