package com.yiling.f2b.web.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yiling.f2b.web.common.utils.VoUtils;
import com.yiling.f2b.web.enterprise.vo.DeliveryAddressVO;
import com.yiling.f2b.web.order.form.CalculateDiscountForm;
import com.yiling.f2b.web.order.form.OrderSubmitForm;
import com.yiling.f2b.web.order.function.CashDiscountFunction;
import com.yiling.f2b.web.order.vo.OrderDiscountVO;
import com.yiling.f2b.web.order.vo.OrderDistributorVO;
import com.yiling.f2b.web.order.vo.OrderGoodsVO;
import com.yiling.f2b.web.order.vo.OrderSettlementPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.ValidateUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.request.BatchAddToCartRequest;
import com.yiling.mall.cart.dto.request.GetCartGoodsInfoRequest;
import com.yiling.mall.cart.dto.request.RemoveCartGoodsRequest;
import com.yiling.mall.cart.dto.request.SelectCartGoodsRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysCompanyDTO;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/23
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块接口")
@Slf4j
public class OrderController extends BaseController {

    @DubboReference
    DeliveryAddressApi            deliveryAddressApi;
    @DubboReference
    CartApi                       cartApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    EnterpriseApi                 enterpriseApi;
    @DubboReference
    InventoryApi                  inventoryApi;
    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;
    @DubboReference
    OrderProcessApi               orderProcessApi;
    @DubboReference
    CustomerContactApi            customerContactApi;
    @DubboReference
    CustomerApi                   customerApi;
    @DubboReference
    UserApi                       userApi;
    @DubboReference
    OrderApi                      orderApi;
    @DubboReference
    OrderDetailApi                orderDetailApi;
    @DubboReference
    GoodsPriceApi                 goodsPriceApi;
    @DubboReference
    DepartmentApi                 departmentApi;
    @DubboReference
    PaymentMethodApi              paymentMethodApi;
    @DubboReference
    PaymentDaysAccountApi         paymentDaysAccountApi;
    @DubboReference
    ProcurementRelationGoodsApi   procurementRelationGoodsApi;
    @Autowired
    CashDiscountFunction          cashDiscountFunction;
    @Autowired
    VoUtils voUtils;
    @Autowired
    FileService fileService;
    @Autowired
    HttpServletRequest httpRequest;


    /**
     * 获取商品合并之后，由于商品Id发生变化后的最新Id
     * @param allCartDOList 购物车商品信息
     * @param allGoodsDTOMap 查询出来的商品集合信息
     * @param yilingSubEids  以岭子企业信息
     */
    protected void setLastMergeGoodId(List<CartDTO> allCartDOList, Map<Long, GoodsSkuStandardBasicDTO> allGoodsDTOMap, List<Long> yilingSubEids) {
        // 配送商商品Ids
        List<Long> allDistributorGoodsIds = allCartDOList.stream().map(CartDTO::getDistributorGoodsId).distinct().collect(Collectors.toList());
        // 获取以岭goodId
        Map<Long,Long> ylGoodIdMap =  goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(allDistributorGoodsIds,yilingSubEids);

        for (CartDTO cartDTO : allCartDOList) {
            // 配送商商品Id
            Long distributorGoodsId = Optional.ofNullable(allGoodsDTOMap.get(cartDTO.getGoodsSkuId())).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            // 获取以岭最新商品Id
            Long ylGoodId = ylGoodIdMap.getOrDefault(cartDTO.getDistributorGoodsId(),cartDTO.getDistributorGoodsId());
            ylGoodId = Long.valueOf(0).equals(ylGoodId) ? cartDTO.getDistributorGoodsId() : ylGoodId;
            // 配送商商品Id
            cartDTO.setDistributorGoodsId(distributorGoodsId);
            // 以岭商品Id
            cartDTO.setGoodsId(ylGoodId);
        }
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
    @GetMapping("/settlement")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<OrderSettlementPageVO> settlement(@CurrentUser CurrentStaffInfo staffInfo) {
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(staffInfo.getCurrentEid(), PlatformEnum.POP, CartGoodsSourceEnum.POP,CartIncludeEnum.SELECTED);
        if (CollUtil.isEmpty(cartDTOList)) {
            return Result.failed("进货单中无选中商品");
        }

        // 校验商品状态
        List<Long> allSkuGoodsIds = cartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allSkuGoodsIds);
        Optional<GoodsSkuStandardBasicDTO>  optional = allDistributorGoodsDTOList.stream().filter(e -> GoodsStatusEnum.getByCode(e.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF).findAny();
        if (optional.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
        }
        Optional<GoodsSkuStandardBasicDTO>  checkDisable = allDistributorGoodsDTOList.stream().filter(e -> !GoodsSkuStatusEnum.NORMAL.getCode().equals(e.getStatus())).findAny();
        if (checkDisable.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_DISABLE);
        }
        // 商品信息字典
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));

        if (allSkuGoodsIds.size() != allDistributorGoodsDTOList.size()) {

            log.error("{}:商品信息查询不全!",allSkuGoodsIds);
        }

        // 设置获取最新的商品Id
        this.setLastMergeGoodId(cartDTOList,allDistributorGoodsDTOMap,enterpriseApi.listSubEids(Constants.YILING_EID));

        // 校验采购关系
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        boolean checkPurchaseRelationResult = enterprisePurchaseRelationApi.checkPurchaseRelation(staffInfo.getCurrentEid(), distributorEids);
        if (!checkPurchaseRelationResult) {
            return Result.failed(OrderErrorCode.SUBMIT_NO_PURCHASE_RELATION);
        }

        // 校验商品采购权限
        this.validatePurchaseAuthority(cartDTOList,staffInfo.getCurrentEid());

        Map<Long, List<CartDTO>> distributorCartDTOMap = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getDistributorEid));
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
        queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryGoodsPriceRequest.setGoodsIds(distributorGoodsIds);
        // 商品价格字典
        Map<Long, BigDecimal> goodsPriceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);
        // 企业支付方式
        Map<Long, List<PaymentMethodDTO>> paymentMethodDTOMap = paymentMethodApi.listByCustomerEidAndEids(staffInfo.getCurrentEid(), distributorEids, PlatformEnum.POP);
        List<OrderDistributorVO> orderDistributorVOList = CollUtil.newArrayList();
        for (Long distributorEid : distributorCartDTOMap.keySet()) {
            List<CartDTO> distributorCartDTOList = distributorCartDTOMap.get(distributorEid);
            List<Long> goodSkuIds = distributorCartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
            Map<Long, InventoryDTO> distributorGoodsInventoryDTOMap = inventoryApi.getMapBySkuIds(goodSkuIds);
            // 校验商品库存
            for (CartDTO cartInfo : distributorCartDTOList) {
                InventoryDTO inventoryDTO = distributorGoodsInventoryDTOMap.get(cartInfo.getGoodsSkuId());
                // 超卖商品,无需校验库存
                if (inventoryDTO != null && ObjectUtil.equal(1,inventoryDTO.getOverSoldType())) {
                    continue;
                }
                Long stockNum = inventoryDTO != null ? inventoryDTO.getQty() - inventoryDTO.getFrozenQty() : 0L;
                if (stockNum < cartInfo.getQuantity()) {
                    return Result.failed(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
                }
            }

            List<OrderGoodsVO> orderGoodsVOList = CollUtil.newArrayList();
            distributorCartDTOList.forEach(cartInfo -> {
                GoodsSkuStandardBasicDTO goodsSkuStandardBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());

                OrderGoodsVO orderGoodsVO = new OrderGoodsVO();
                // 拷贝商品标准库信息
                PojoUtils.map(voUtils.toSimpleGoodsVO(goodsSkuStandardBasicDTO.getStandardGoodsBasic()), orderGoodsVO);
                orderGoodsVO.setCartId(cartInfo.getId());
                orderGoodsVO.setDistributorGoodsId(cartInfo.getDistributorGoodsId());
                orderGoodsVO.setPrice(goodsPriceMap.getOrDefault(cartInfo.getDistributorGoodsId(),goodsSkuStandardBasicDTO.getStandardGoodsBasic().getPrice()));
                orderGoodsVO.setBigPackage(goodsSkuStandardBasicDTO.getPackageNumber().intValue());
                orderGoodsVO.setGoodsRemark(goodsSkuStandardBasicDTO.getRemark());
                orderGoodsVO.setPrice(goodsPriceMap.getOrDefault(cartInfo.getDistributorGoodsId(),goodsSkuStandardBasicDTO.getStandardGoodsBasic().getPrice()));
                orderGoodsVO.setQuantity(cartInfo.getQuantity());
                orderGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(orderGoodsVO.getPrice(), orderGoodsVO.getQuantity()), 2));
                orderGoodsVOList.add(orderGoodsVO);
            });

            OrderDistributorVO orderDistributorVO = new OrderDistributorVO();
            orderDistributorVO.setDistributorEid(distributorEid);
            orderDistributorVO.setDistributorName(distributorDTOMap.get(distributorEid).getName());
            orderDistributorVO.setOrderGoodsList(orderGoodsVOList);
            orderDistributorVO.setGoodsSpeciesNum(orderGoodsVOList.stream().count());
            orderDistributorVO.setGoodsNum(orderGoodsVOList.stream().mapToLong(OrderGoodsVO::getQuantity).sum());
            orderDistributorVO.setTotalAmount(orderGoodsVOList.stream().map(OrderGoodsVO::getAmount).reduce(BigDecimal::add).get());
            orderDistributorVO.setFreightAmount(BigDecimal.ZERO);
            orderDistributorVO.setCashDiscountAmount(BigDecimal.ZERO);
            orderDistributorVO.setPaymentAmount(NumberUtil.sub(orderDistributorVO.getTotalAmount(), orderDistributorVO.getFreightAmount()));
            orderDistributorVO.setYilingFlag(yilingSubEids.contains(distributorEid));
            orderDistributorVO.setShowContractFile(yilingSubEids.contains(distributorEid) || industryDirectEids.contains(distributorEid));
            orderDistributorVO.setPaymentMethodList(setDistributorPaymentMethod(paymentMethodDTOMap.get(distributorEid)));

            orderDistributorVOList.add(orderDistributorVO);
        }

        OrderSettlementPageVO pageVO = new OrderSettlementPageVO();
        pageVO.setOrderDistributorList(orderDistributorVOList);
        pageVO.setGoodsSpeciesNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsSpeciesNum).sum());
        pageVO.setGoodsNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsNum).sum());
        pageVO.setTotalAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getTotalAmount).reduce(BigDecimal::add).get());
        pageVO.setFreightAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getFreightAmount).reduce(BigDecimal::add).get());
        pageVO.setPaymentAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getPaymentAmount).reduce(BigDecimal::add).get());
        // 配送地址
        List<DeliveryAddressDTO> deliveryAddressDTOs = deliveryAddressApi.selectDeliveryAddressList(new QueryDeliveryAddressRequest().setEid(staffInfo.getCurrentEid()));
        // 默认只展示前5条
        if (CollUtil.isNotEmpty(deliveryAddressDTOs)) {

            deliveryAddressDTOs = deliveryAddressDTOs.stream().limit(5).collect(Collectors.toList());
        }

        pageVO.setDeliveryAddressList(PojoUtils.map(deliveryAddressDTOs, DeliveryAddressVO.class));

        // 企业账号信息
        pageVO.setShowEasAccountInfoFlag(this.showEasAccountInfoFlag(yilingSubEids,distributorEids));
        pageVO.setEasAccountList(this.selectEasAccountList(pageVO.getShowEasAccountInfoFlag(),staffInfo.getCurrentEid()));
        //企业联系人
        pageVO.setContactorList(this.selectEnterPriceContaContactorList(staffInfo.getCurrentEid()));

        return Result.success(pageVO);
    }


    /**
     * 设置订单支付方式
     * @param paymentMethodDTOList
     * @return
     */
    private List<OrderDistributorVO.PaymentMethodVO> setDistributorPaymentMethod(List<PaymentMethodDTO> paymentMethodDTOList) {

        if (CollectionUtil.isEmpty(paymentMethodDTOList)) {

            return ListUtil.empty();
        }

        return  paymentMethodDTOList.stream().map(paymentMethod -> {
            OrderDistributorVO.PaymentMethodVO vo = new OrderDistributorVO.PaymentMethodVO();
            vo.setId(paymentMethod.getCode());
            vo.setName(paymentMethod.getName());
            vo.setEnabled(true);
            vo.setSelected(false);
            return vo;

        }).collect(Collectors.toList());
    }


    /**
     * 以岭直购，以及一级商向工业直属采购需要展示EAS企业账号
     * 查询是否展示企业信息
     * @param yilingSubEids 以岭企业ID信息
     * @param distributorEids 配送商企业信息ID
     * @return
     */
    private boolean showEasAccountInfoFlag( List<Long> yilingSubEids,List<Long> distributorEids) {

        if (yilingSubEids.stream().anyMatch(eid -> distributorEids.contains(eid))) {

            return true;
        }

        // 查询工业直属的企业ID信息
        List<Long> industryDirectEids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);

        return industryDirectEids.stream().anyMatch(eid -> distributorEids.contains(eid));
    }


    /**
     * 查询企业账号信息
     * @param showEasAccountInfoFlag 是否展示企业信息
     * @param currentEid  当前登录企业ID
     * @return
     */
    private List<OrderSettlementPageVO.EasAccountVO> selectEasAccountList(Boolean showEasAccountInfoFlag,Long currentEid) {

        if (!showEasAccountInfoFlag) {

            return ListUtil.empty();
        }

        List<EnterpriseCustomerEasDTO> enterpriseCustomerEasDTOList = customerApi.getCustomerEasInfos(Constants.YILING_EID, currentEid);
        List<OrderSettlementPageVO.EasAccountVO> easAccountVOList = new ArrayList<>(enterpriseCustomerEasDTOList.size());
        enterpriseCustomerEasDTOList.forEach(e -> {

            OrderSettlementPageVO.EasAccountVO easAccountVO = new OrderSettlementPageVO.EasAccountVO();
            easAccountVO.setEname(e.getEasName());
            easAccountVO.setAccount(e.getEasCode());
            easAccountVOList.add(easAccountVO);
        });

        return easAccountVOList;
    }

    /**
     *  查询企业联系人
     * @param currentEid  当前购买企业ID
     * @return
     */
    private List<OrderSettlementPageVO.ContactorVO> selectEnterPriceContaContactorList(Long currentEid) {

        List<EnterpriseCustomerContactDTO> customerContactDTOList = customerContactApi.listByEidAndCustomerEid(Constants.YILING_EID, currentEid);

        if (CollUtil.isEmpty(customerContactDTOList)) {
            return ListUtil.empty();
        }

        List<Long> contactUserIds = customerContactDTOList.stream().map(EnterpriseCustomerContactDTO::getContactUserId).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(contactUserIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        Map<Long, List<EnterpriseDepartmentDTO>> enterpriseDepartmentMap = departmentApi.getDepartmentByEidUser(Constants.YILING_EID, contactUserIds);

        if (log.isDebugEnabled()) {
            log.debug("getDepartmentByEidUser...query..param:{},...result:{}",contactUserIds,enterpriseDepartmentMap);
        }

        List<OrderSettlementPageVO.ContactorVO> contactorVOList = Lists.newArrayList();
        for (EnterpriseCustomerContactDTO e : customerContactDTOList) {
            List<EnterpriseDepartmentDTO> enterpriseDepartmentDTOS = enterpriseDepartmentMap.get(e.getContactUserId());

            if (CollectionUtil.isEmpty(enterpriseDepartmentDTOS)) {
                OrderSettlementPageVO.ContactorVO contactorVO = new OrderSettlementPageVO.ContactorVO();
                contactorVO.setId(e.getContactUserId());
                contactorVO.setName(userDTOMap.get(e.getContactUserId()).getName());
                contactorVO.setMobile(userDTOMap.get(e.getContactUserId()).getMobile());
                contactorVO.setDepartmentId(0l);
                contactorVO.setDepartmentName("");
                contactorVOList.add(contactorVO);
                continue;
            }

            for (EnterpriseDepartmentDTO enterpriseDepartmentDTO : enterpriseDepartmentDTOS) {

                OrderSettlementPageVO.ContactorVO contactorVO = new OrderSettlementPageVO.ContactorVO();
                contactorVO.setId(e.getContactUserId());
                contactorVO.setName(userDTOMap.get(e.getContactUserId()).getName());
                contactorVO.setMobile(userDTOMap.get(e.getContactUserId()).getMobile());
                contactorVO.setDepartmentId(enterpriseDepartmentDTO.getId());
                contactorVO.setDepartmentName(enterpriseDepartmentDTO.getName());
                contactorVOList.add(contactorVO);
            }
        }

        return contactorVOList;

    }

    @ApiOperation(value = "预订单\"编辑\"生成结算页信息")
    @GetMapping("/settlementByOrder")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<OrderSettlementPageVO> settlementByOrderNo(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "订单号", required = true) @RequestParam String orderNo) {
        // 订单信息
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
        if (orderDTO == null) {
            return Result.failed("订单信息不存在");
        } else if (!orderDTO.getBuyerEid().equals(staffInfo.getCurrentEid())) {
            return Result.failed("订单信息不存在");
        }

        // 订单明细列表
        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());

        // 1、设置购物车所有商品为“未选中”
        {
            List<CartDTO> buyerSelectedCartList = cartApi.listByBuyerEid(orderDTO.getBuyerEid(), PlatformEnum.POP, CartGoodsSourceEnum.POP, CartIncludeEnum.SELECTED);
            if (CollUtil.isNotEmpty(buyerSelectedCartList)) {
                List<Long> selectedCartIds = buyerSelectedCartList.stream().map(CartDTO::getId).collect(Collectors.toList());

                SelectCartGoodsRequest selectCartGoodsRequest = new SelectCartGoodsRequest();
                selectCartGoodsRequest.setIds(selectedCartIds);
                selectCartGoodsRequest.setSelected(false);
                selectCartGoodsRequest.setOpUserId(staffInfo.getCurrentUserId());
                cartApi.selectCartGoods(selectCartGoodsRequest);
            }
        }

        // 2、移除原有购物车中对应的商品信息
        {
            List<Long> cartIds = CollUtil.newArrayList();
            orderDetailDTOList.forEach(e -> {
                GetCartGoodsInfoRequest request = new GetCartGoodsInfoRequest();
                request.setGoodsSkuId(e.getGoodsSkuId());
                request.setGoodsId(e.getGoodsId());
                request.setBuyerEid(orderDTO.getBuyerEid());
                request.setDistributorEid(orderDTO.getDistributorEid());
                request.setPlatformEnum(PlatformEnum.POP);
                request.setGoodsSourceEnum(CartGoodsSourceEnum.POP);

                CartDTO cartDTO = cartApi.getCartGoodsInfo(request);
                if (cartDTO != null) {
                    cartIds.add(cartDTO.getId());
                }
            });

            if (CollUtil.isNotEmpty(cartIds)) {
                RemoveCartGoodsRequest removeCartGoodsRequest = new RemoveCartGoodsRequest();
                removeCartGoodsRequest.setIds(cartIds);
                removeCartGoodsRequest.setOpUserId(staffInfo.getCurrentUserId());
                cartApi.removeCartGoods(removeCartGoodsRequest);
            }
        }

        // 3、将订单商品重新添加到购物车
        {
            List<BatchAddToCartRequest.QuickPurchaseInfoDTO> quickPurchaseInfoDTOList = CollUtil.newArrayList();
            orderDetailDTOList.forEach(e -> {
                BatchAddToCartRequest.QuickPurchaseInfoDTO quickPurchaseInfoDTO = new BatchAddToCartRequest.QuickPurchaseInfoDTO();
                quickPurchaseInfoDTO.setGoodsId(e.getGoodsId());
                quickPurchaseInfoDTO.setQuantity(e.getGoodsQuantity());
                quickPurchaseInfoDTO.setDistributorEid(orderDTO.getDistributorEid());
                quickPurchaseInfoDTO.setDistributorGoodsId(e.getDistributorGoodsId());
                quickPurchaseInfoDTO.setGoodsSkuId(e.getGoodsSkuId());
                quickPurchaseInfoDTOList.add(quickPurchaseInfoDTO);
            });

            BatchAddToCartRequest batchAddToCartRequest = new BatchAddToCartRequest();
            batchAddToCartRequest.setBuyerEid(orderDTO.getBuyerEid());
            batchAddToCartRequest.setQuickPurchaseInfoList(quickPurchaseInfoDTOList);
            batchAddToCartRequest.setPlatformEnum(PlatformEnum.POP);
            batchAddToCartRequest.setGoodsSourceEnum(CartGoodsSourceEnum.POP);
            // 添加到购物车
            cartApi.batchAddToCart(batchAddToCartRequest);
        }

        return this.settlement(staffInfo);
    }

    @ApiOperation(value = "提交订单")
    @PostMapping("/submit")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CollectionObject<String>> submit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody OrderSubmitForm form) {
        // 获取以岭子企业ID列表
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 工业直属企业信息
        List<Long> industryDirectEids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);

        // 配送商ID信息
        List<Long> distributorEids = form.getDistributorOrderList().stream().map(e -> e.getDistributorEid()).collect(Collectors.toList());

        Class[] groups;
        if (this.showEasAccountInfoFlag(yilingSubEids,distributorEids)) {
            groups = new Class[] { OrderSubmitForm.YilingOrderSubmitValidateGroup.class };
        } else {
            groups = new Class[] { OrderSubmitForm.CommonOrderSubmitValidateGroup.class };
        }
        String errorMessage = ValidateUtils.failFastValidate(form, groups);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return Result.validateFailed(errorMessage);
        }

        for (OrderSubmitForm.DistributorOrderForm distributorOrderForm : form.getDistributorOrderList()) {
            if (yilingSubEids.contains(distributorOrderForm.getDistributorEid()) || industryDirectEids.contains(distributorOrderForm.getDistributorEid())) {
                groups = new Class[] { OrderSubmitForm.YilingOrderSubmitValidateGroup.class };
            } else {
                groups = new Class[] { OrderSubmitForm.CommonOrderSubmitValidateGroup.class };
            }

            errorMessage = ValidateUtils.failFastValidate(distributorOrderForm, groups);
            if (StrUtil.isNotEmpty(errorMessage)) {
                return Result.validateFailed(errorMessage);
            }
        }
        UserAgent userAgent = UserAgentUtil.parse(httpRequest.getHeader("User-Agent"));
        OrderSubmitRequest request = PojoUtils.map(form, OrderSubmitRequest.class);
        request.setOrderTypeEnum(OrderTypeEnum.POP);
        request.setOrderSourceEnum(OrderSourceEnum.POP_PC);
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setIpAddress(IPUtils.getIp(httpRequest));
        request.setUserAgent(JSONObject.toJSONString(userAgent));

        OrderSubmitBO orderSubmitBO = orderProcessApi.submit(request);
        List<String> orderNos = orderSubmitBO.getOrderDTOList().stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());

        return Result.success(new CollectionObject<>(orderNos));
    }




    @ApiOperation(value = "选择支付方式")
    @PostMapping("/selectOrderPaymentMethod")
    @ApiResponses({
            @ApiResponse(code = 100002, message = "未设置支付方式"),
            @ApiResponse(code = 100568, message = "账期余额不足"),
            @ApiResponse(code = 100004, message = "现折金额设置异常")
    })
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<OrderDiscountVO> selectOrderPaymentMethod(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CalculateDiscountForm discountForm) {

        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(staffInfo.getCurrentEid(), PlatformEnum.POP, CartGoodsSourceEnum.POP,CartIncludeEnum.SELECTED)
                .stream().filter(t -> discountForm.getDistributorEid().equals(t.getDistributorEid())).collect(Collectors.toList());

        if (CollUtil.isEmpty(cartDTOList)) {

            return Result.failed("进货单中无选中商品");
        }

        // 校验支付方式
        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(discountForm.getDistributorEid(), staffInfo.getCurrentEid(), PlatformEnum.POP);
        if (CollUtil.isEmpty(paymentMethodDTOList)) {
            throw new BusinessException(PaymentErrorCode.NO_PAYMENT_METHOD);
        }

        List<Long> paymentMethodIds = paymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).collect(Collectors.toList());

        if (!paymentMethodIds.contains(discountForm.getPaymentMethod().longValue())) {

            throw new BusinessException(PaymentErrorCode.PAYMENT_METHOD_UNUSABLE);
        }

        // 商品价格字典
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryGoodsPriceRequest.setGoodsIds(cartDTOList.stream().map(t -> t.getDistributorGoodsId()).collect(Collectors.toList()));
        Map<Long, BigDecimal> goodsPriceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

        List<OrderDetailDTO> orderDetailDTOList = cartDTOList.stream().map(t -> {
            OrderDetailDTO orderDetailDTO = PojoUtils.map(t,OrderDetailDTO.class);
            orderDetailDTO.setGoodsQuantity(t.getQuantity());
            orderDetailDTO.setGoodsPrice(goodsPriceMap.getOrDefault(t.getDistributorGoodsId(),BigDecimal.ZERO));
            orderDetailDTO.setGoodsAmount(NumberUtil.round(NumberUtil.mul(orderDetailDTO.getGoodsPrice(), orderDetailDTO.getGoodsQuantity()), 2));

            return orderDetailDTO;
        }).collect(Collectors.toList());

        // 订单总金额
        BigDecimal totalAmount = orderDetailDTOList.stream().map(t -> t.getGoodsAmount()).reduce(BigDecimal::add).get();
        // 运费
        BigDecimal freightMoney = BigDecimal.ZERO;
        // 计算现折金额
        BigDecimal orderCashDiscountAmount = cashDiscountFunction.calOrderCashDiscount(orderDetailDTOList,staffInfo.getCurrentEid(),discountForm.getDistributorEid());

        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.getByCode(discountForm.getPaymentMethod().longValue());

        if (paymentMethodEnum == PaymentMethodEnum.PAYMENT_DAYS) {

            // 账期可用额度
            BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getAvailableAmountByCustomerEid(discountForm.getDistributorEid(),staffInfo.getCurrentEid());
            // 订单应付金额
            BigDecimal orderPaymentAmount = NumberUtil.sub(totalAmount, freightMoney, orderCashDiscountAmount);

            if (paymentDaysAvailableAmount.compareTo(orderPaymentAmount) < 0) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_AMOUNT_LESS);
            }
            //获取集团可用账期，校验集团账期是否足够
            PaymentDaysCompanyDTO paymentDaysCompanyDTO = paymentDaysAccountApi.get();
            BigDecimal availableAmount = paymentDaysCompanyDTO.getTotalAmount().subtract(paymentDaysCompanyDTO.getUsedAmount()).add(paymentDaysCompanyDTO.getRepaymentAmount());

            if (availableAmount.compareTo(orderPaymentAmount) < 0) {

                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_COMPANY_ERROR);
            }
        }

        return Result.success(new OrderDiscountVO(discountForm.getDistributorEid(), totalAmount,orderCashDiscountAmount,NumberUtil.sub(totalAmount,orderCashDiscountAmount)));
    }


}
