package com.yiling.f2b.admin.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.admin.order.form.QueryOrderExpectPageForm;
import com.yiling.f2b.admin.order.vo.EnterpriseInfoVO;
import com.yiling.f2b.admin.order.vo.OrderAddressVO;
import com.yiling.f2b.admin.order.vo.OrderDetailVO;
import com.yiling.f2b.admin.order.vo.OrderExpectDetailVO;
import com.yiling.f2b.admin.order.vo.OrderExpectPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderAttachmentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author:wei.wang
 * @date:2021/7/15
 */
@RestController
@Api(tags = "采购订单-预订单")
@RequestMapping("/order/expect")
public class OrderExpectController extends BaseController {
    @DubboReference
    OrderApi             orderApi;
    @DubboReference
    GoodsApi             goodsApi;
    @DubboReference
    UserApi              userApi;
    @DubboReference
    OrderDetailApi       orderDetailApi;
    @DubboReference
    EnterpriseApi        enterpriseApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderProcessApi      orderMallApi;
    @DubboReference
    OrderAddressApi      orderAddressApi;
    @DubboReference
    DataPermissionsApi   dataPermissionsApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @Autowired
    FileService     fileService;

    @ApiOperation(value = "预订单列表")
    @PostMapping("/get/page")
    public Result<Page<OrderExpectPageVO>> getOrderExpectInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                              @Valid @RequestBody QueryOrderExpectPageForm form) {
        QueryOrderExpectPageRequest request = PojoUtils.map(form, QueryOrderExpectPageRequest.class);
        //request.setBuyerEid(staffInfo.getCurrentEid());
        //List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());

        request.setOrderType(OrderTypeEnum.POP.getCode());
        List<Long> sellerEidList = new ArrayList<>();
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());

        if (staffInfo.getYilingFlag() ) {
            //以岭人员
            List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);
            sellerEidList.addAll(subEidLists);

            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            request.setContacterIdList(contacterIdList);
        }else if(EnterpriseChannelEnum.INDUSTRY_DIRECT == EnterpriseChannelEnum.getByCode(enterpriseDTO.getChannelId())){
            //工业直属人员
            sellerEidList.add(staffInfo.getCurrentEid());
            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            request.setContacterIdList(contacterIdList);
        }else{
            //一二级商
            request.setBuyerEid(staffInfo.getCurrentEid());
        }

        request.setSellerEidList(sellerEidList);

        Page<OrderDTO> expectInfo = orderApi.getOrderExpectInfo(request);
        Page<OrderExpectPageVO> result = PojoUtils.map(expectInfo, OrderExpectPageVO.class);
        List<Long> goodsIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();
        if (result != null && CollectionUtil.isNotEmpty(result.getRecords())) {
            List<Long> ids = result.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
            List<Long> userList = result.getRecords().stream().map(order -> order.getAuditUser()).collect(Collectors.toList());
            List<OrderDetailDTO> details = orderDetailApi.getOrderDetailByOrderIds(ids);
            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
            Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
            for (OrderExpectPageVO one : result.getRecords()) {
                //货款总额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //折扣金额
                BigDecimal discountAmount = BigDecimal.ZERO;
                List<OrderDetailVO> goodOrderList = new ArrayList<>();
                for (OrderDetailDTO detail : details) {
                    if (one.getId().equals(detail.getOrderId())) {
                        OrderDetailVO detailVO = PojoUtils.map(detail, OrderDetailVO.class);
                        OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(detail.getId());
                        detailVO.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                                .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                                .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                                .setGoodsAmount(orderDetailChangeOne.getGoodsAmount()
                                        .subtract(orderDetailChangeOne.getSellerReturnAmount()))
                                .setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount()
                                        .add(orderDetailChangeOne.getTicketDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
                        detailVO.setRealAmount(detailVO.getGoodsAmount().subtract(detailVO.getDiscountAmount()));
                        totalAmount = totalAmount.add(detailVO.getGoodsAmount());
                        discountAmount = discountAmount.add(detailVO.getDiscountAmount());
                        goodOrderList.add(detailVO);
                    }
                    goodsIds.add(detail.getGoodsId());
                    goodsSukIds.add(detail.getGoodsSkuId());
                }
                one.setOrderDetailList(goodOrderList);
                //设置金额
                one.setDiscountAmount(discountAmount)
                        .setTotalAmount(totalAmount)
                        .setPaymentAmount(totalAmount.subtract(discountAmount));

                OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(one.getId());
                one.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum())
                        .setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum());
            }

            List<UserDTO> userDto = userApi.listByIds(userList);
            Map<Long, String> mapUser = userDto.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName, (k1, k2) -> k1));
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);

            List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
            Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
                skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
            }

            for (OrderExpectPageVO one : result.getRecords()) {
                for (OrderDetailVO goods : one.getOrderDetailList()) {
                    goods.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(goods.getGoodsId())));
                    goods.setPackageNumber(skuGoodsMap.get(goods.getGoodsSkuId()) != null ? skuGoodsMap.get(goods.getGoodsSkuId()).getPackageNumber() : 1L );
                    goods.setGoodsRemark(skuGoodsMap.get(goods.getGoodsSkuId()) != null ? skuGoodsMap.get(goods.getGoodsSkuId()).getRemark() : "" );
                }
                one.setAuditUserName(mapUser.get(one.getAuditUser()));
            }
        }
        return Result.success(result);
    }

    @ApiOperation(value = "预订单详情")
    @GetMapping("/get/detail")
    public Result<OrderExpectDetailVO> getOrderExpectDetail(@CurrentUser CurrentStaffInfo staffInfo,
                                                            @RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderExpectDetailVO result = PojoUtils.map(orderInfo, OrderExpectDetailVO.class);

        List<Long> enterpriseIds = new ArrayList<Long>() {
            {
                add(orderInfo.getDistributorEid());
                add(orderInfo.getBuyerEid());
            }
        };
        List<EnterpriseDTO> enterpriseLists = enterpriseApi.listByIds(enterpriseIds);
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseLists.stream().collect(Collectors.toMap(EnterpriseDTO::getId, one -> one, (k1, k2) -> k1));

        List<Long> userIds = new ArrayList<Long>() {
            {
                add(orderInfo.getContacterId());
                add(orderInfo.getAuditUser());
            }
        };

        List<UserDTO> userList = userApi.listByIds(userIds);
        Map<Long, UserDTO> userMap = userList.stream().collect(Collectors.toMap(UserDTO::getId, one -> one, (k1, k2) -> k1));
        result.setContacterName(userMap.get(orderInfo.getContacterId()) != null ? userMap.get(orderInfo.getContacterId()).getName() : null);
        result.setContacterTelephone(userMap.get(orderInfo.getContacterId()) != null ? userMap.get(orderInfo.getContacterId()).getMobile() : null);
        result.setAuditUserName(userMap.get(orderInfo.getAuditUser()) != null ? userMap.get(orderInfo.getAuditUser()).getName() : null);

        EnterpriseDTO enterpriseDistributor = enterpriseMap.get(orderInfo.getDistributorEid());
        EnterpriseInfoVO enterpriseInfo = PojoUtils.map(enterpriseDistributor, EnterpriseInfoVO.class);
        if (enterpriseDistributor != null) {
            enterpriseInfo.setAddress(enterpriseDistributor.getProvinceName() + enterpriseDistributor.getCityName() + enterpriseDistributor.getRegionName() + enterpriseDistributor.getAddress());
        }
        result.setEnterpriseDistributorInfo(enterpriseInfo);


        EnterpriseDTO enterpriseBuyer = enterpriseMap.get(orderInfo.getBuyerEid());

        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderId);
        OrderAddressVO address = PojoUtils.map(addressInfo, OrderAddressVO.class);
        if (addressInfo != null) {
            address.setAddress(addressInfo.getAddress());
            if (enterpriseBuyer != null) {
                address.setProvinceName(enterpriseBuyer.getProvinceName());
                address.setBuyerEname(orderInfo.getBuyerEname());
            }

        }
        result.setOrderAddress(address);

        List<OrderAttachmentDTO> orderContractList = orderApi.listOrderAttachmentsByType(orderId, OrderAttachmentTypeEnum.SALES_CONTRACT_FILE);
        List<String> keyFiles = orderContractList.stream().map(one -> one.getFileKey()).collect(Collectors.toList());
        List<String> urlList = new ArrayList<>();
        for (String key : keyFiles) {
            String url = fileService.getUrl(key, FileTypeEnum.ORDER_SALES_CONTRACT);
            urlList.add(url);
        }

        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        List<OrderDetailVO> orderDetailLists = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(orderDetailInfo)) {
            List<Long> listIds = orderDetailInfo.stream().map(one -> one.getGoodsId()).collect(Collectors.toList());
            List<Long> goodsSukIds = orderDetailInfo.stream().map(one -> one.getGoodsSkuId()).collect(Collectors.toList());

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderId);
            Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
            //货款总额
            BigDecimal totalAmount = BigDecimal.ZERO;
            //票折总金额
            BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
            //现折金额
            BigDecimal cashDiscountAmount = BigDecimal.ZERO;

            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(listIds);

            List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
            Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(goodsSkuByIds)){
                skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
            }

            for (OrderDetailDTO one : orderDetailInfo) {
                OrderDetailVO orderDetailOne = PojoUtils.map(one, OrderDetailVO.class);
                OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(one.getId());

                orderDetailOne.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                        .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                        .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                        .setGoodsAmount(orderDetailChangeOne.getGoodsAmount()
                                .subtract(orderDetailChangeOne.getSellerReturnAmount()))
                        .setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount()
                                .add(orderDetailChangeOne.getTicketDiscountAmount())
                                .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount())
                                .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount()));
                orderDetailOne.setRealAmount(orderDetailOne.getGoodsAmount().subtract(orderDetailOne.getDiscountAmount()));

                ticketDiscountAmount = ticketDiscountAmount.add(orderDetailChangeOne.getTicketDiscountAmount()
                        .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));

                cashDiscountAmount = cashDiscountAmount.add(orderDetailChangeOne.getCashDiscountAmount()
                        .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount()));

                totalAmount = totalAmount.add(orderDetailOne.getGoodsAmount());

                orderDetailOne.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(one.getGoodsId())));
                orderDetailOne.setPackageNumber(skuGoodsMap.get(orderDetailOne.getGoodsSkuId()) != null ? skuGoodsMap.get(orderDetailOne.getGoodsSkuId()).getPackageNumber() : 1L );
                orderDetailOne.setGoodsRemark(skuGoodsMap.get(orderDetailOne.getGoodsSkuId()) != null ? skuGoodsMap.get(orderDetailOne.getGoodsSkuId()).getRemark() : "" );
                orderDetailLists.add(orderDetailOne);
            }
            result.setOrderDetailList(orderDetailLists);

            result.setTotalAmount(totalAmount);
            result.setPaymentAmount(totalAmount
                    .subtract(cashDiscountAmount)
                    .subtract(ticketDiscountAmount));
            result.setDiscountAmount(ticketDiscountAmount
                    .add(cashDiscountAmount));
        }


        result.setOrderContractUrl(urlList);

        return Result.success(result);
    }

    @ApiOperation(value = "预订单取消")
    @GetMapping("/cancel")
    public Result<BoolObject> cancelOrderExpect(@CurrentUser CurrentStaffInfo staffInfo,
                                                @RequestParam(value = "orderId") Long orderId) {
        Boolean result = orderMallApi.cancelOrderExpect(orderId, staffInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

}
