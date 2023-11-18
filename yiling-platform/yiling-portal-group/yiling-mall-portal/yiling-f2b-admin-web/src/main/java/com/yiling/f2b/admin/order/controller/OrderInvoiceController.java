package com.yiling.f2b.admin.order.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.dict.api.DictTypeApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.f2b.admin.order.form.OrderApplyComputeBatchForm;
import com.yiling.f2b.admin.order.form.QueryOrderInvoiceAmountDetailForm;
import com.yiling.f2b.admin.order.form.QueryOrderInvoiceAmountErpDeliveryNoForm;
import com.yiling.f2b.admin.order.form.QueryOrderInvoiceAmountForm;
import com.yiling.f2b.admin.order.form.SaveOrderTicketBatchForm;
import com.yiling.f2b.admin.order.vo.OrderInvoiceAmountVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceApplyDetailOneVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceApplyListVO;
import com.yiling.f2b.admin.order.vo.OrderTicketDiscountVO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderTransitionRuleCodeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.admin.order.form.OrderApplyTicketComputeForm;
import com.yiling.f2b.admin.order.form.QueryInvoicePageForm;
import com.yiling.f2b.admin.order.form.SaveOrderDetailTicketDiscountForm;
import com.yiling.f2b.admin.order.vo.OrderApplyInvoiceDetailVO;
import com.yiling.f2b.admin.order.vo.OrderApplyInvoiceVO;
import com.yiling.f2b.admin.order.vo.OrderApplyTicketComputeVO;
import com.yiling.f2b.admin.order.vo.OrderBatchVO;
import com.yiling.f2b.admin.order.vo.OrderChoiceTicketInfoVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceErpDeliveryNoVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceGroupVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceInfoPageVO;
import com.yiling.f2b.admin.order.vo.OrderMakeTicketInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.mall.order.api.OrderInvoiceApplyProcessApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryErpApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderInvoiceApi;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.api.OrderInvoiceDeliveryGroupApi;
import com.yiling.order.order.api.OrderInvoiceDetailApi;
import com.yiling.order.order.api.OrderTicketDiscountApi;
import com.yiling.order.order.api.TicketDiscountRecordApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryErpDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.dto.OrderInvoiceDeliveryGroupDTO;
import com.yiling.order.order.dto.OrderInvoiceDetailDTO;
import com.yiling.order.order.dto.OrderTicketDiscountDTO;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.QueryInvoicePageRequest;
import com.yiling.order.order.dto.request.SaveOrderTicketApplyInfoRequest;
import com.yiling.order.order.enums.OrderInvoiceDiscountTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:wei.wang
 * @date:2021/7/13
 */
@Slf4j
@RestController
@Api(tags = "订单发票管理")
@RefreshScope
@RequestMapping("/order/invoice")
public class OrderInvoiceController extends BaseController {
    @DubboReference
    TicketDiscountRecordApi      ticketDiscountRecordApi;
    @DubboReference
    OrderApi                     orderApi;
    @DubboReference
    OrderTicketDiscountApi       orderTicketDiscountApi;
    @DubboReference
    OrderInvoiceApi              orderInvoiceApi;
    @DubboReference
    UserApi                      userApi;
    @DubboReference
    EnterpriseApi                enterpriseApi;
    @DubboReference
    OrderDetailApi               orderDetailApi;
    @DubboReference
    GoodsApi                     goodsApi;
    @DubboReference
    OrderInvoiceDetailApi        orderInvoiceDetailApi;
    @DubboReference
    OrderInvoiceApplyApi         orderInvoiceApplyApi;
    @DubboReference
    OrderInvoiceApplyProcessApi  orderInvoiceApplyProcessApi;
    @DubboReference
    OrderDetailChangeApi         orderDetailChangeApi;
    @DubboReference
    OrderDeliveryErpApi          orderDeliveryErpApi;
    @DubboReference
    OrderInvoiceDeliveryGroupApi orderInvoiceDeliveryGroupApi;
    @DubboReference
    DataPermissionsApi           dataPermissionsApi;
    @DubboReference
    DictTypeApi                  dictTypeApi;
    @DubboReference
    DepartmentApi departmentApi;

    @Value("#{${common.order-max-amount}}")
    private Map<String, String> ruleMap ;

    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @Autowired
    RedisService    redisService;


    @ApiOperation(value = "选择票折")
    @GetMapping("/choice/invoice")
    public Result<CollectionObject<OrderChoiceTicketInfoVO>> getTicketDiscount(@CurrentUser CurrentStaffInfo staffInfo,
                                                                               @ApiParam(value = "订单ID", required = true) @RequestParam("orderId") Long orderId) {
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);

        String sellerErpCode = orderDTO.getSellerErpCode();
        String customerErpCode = orderDTO.getCustomerErpCode();
        if (StrUtil.isEmpty(sellerErpCode) || StrUtil.isEmpty(customerErpCode)) {
            return Result.failed("订单数据不完整");
        }
        List<TicketDiscountRecordDTO> list = ticketDiscountRecordApi.listCustomerTicketDiscounts(sellerErpCode, customerErpCode);
        List<OrderChoiceTicketInfoVO> result = PojoUtils.map(list, OrderChoiceTicketInfoVO.class);
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> nos = result.stream().map(order -> order.getTicketDiscountNo()).collect(Collectors.toList());
            List<OrderTicketDiscountDTO> orderTicketDiscountList = orderTicketDiscountApi.getOrderTicketDiscountByListNos(nos);
            Map<String, List<Long>> map = new HashMap<>();
            for (OrderTicketDiscountDTO one : orderTicketDiscountList) {
                if (map.containsKey(one.getTicketDiscountNo())) {
                    List<Long> longs = map.get(one.getTicketDiscountNo());
                    if (!longs.contains(one.getOrderId())) {
                        longs.add(one.getOrderId());
                    }
                } else {
                    map.put(one.getTicketDiscountNo(), new ArrayList<Long>() {{
                        add(one.getOrderId());
                    }});
                }
            }
            for (OrderChoiceTicketInfoVO one : result) {
                one.setUsedOrder(map.get(one.getTicketDiscountNo()) == null ? 0 : map.get(one.getTicketDiscountNo()).size());
            }
        }
        return Result.success(new CollectionObject(result));
    }


    @ApiOperation(value = "使用票折订单个数信息")
    @GetMapping("/use/invoice")
    public Result<CollectionObject<OrderMakeTicketInfoVO>> getOrderTicketDiscountInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                                                      @RequestParam(value = "ticketDiscountNo") String ticketDiscountNo) {
        List<OrderMakeTicketInfoVO> ticketInfo = new ArrayList<>();
        List<OrderTicketDiscountDTO> discountList = orderTicketDiscountApi.getOrderTicketDiscountByNo(ticketDiscountNo);
        if (CollectionUtil.isNotEmpty(discountList)) {
            List<Long> orderIds = discountList.stream().map(OrderTicketDiscountDTO::getOrderId).collect(Collectors.toList());
            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(orderIds);

            for (OrderTicketDiscountDTO one : discountList) {
                OrderMakeTicketInfoVO resultOne = new OrderMakeTicketInfoVO();
                //货款总金额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //票折总金额
                BigDecimal ticketAmount = BigDecimal.ZERO;
                //现在总金额
                BigDecimal cashAmount = BigDecimal.ZERO;
                for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
                    if (one.getOrderId().equals(changeOne.getOrderId())) {
                        totalAmount = totalAmount.add(changeOne.getDeliveryAmount());
                        ticketAmount = ticketAmount.add(changeOne.getDeliveryTicketDiscountAmount());
                        cashAmount = cashAmount.add(changeOne.getCashDiscountAmount());
                    }
                }
                resultOne.setOrderNo(one.getOrderNo())
                        .setCreateTime(one.getCreateTime())
                        .setCashDiscountAmount(cashAmount)
                        .setTotalAmount(totalAmount)
                        .setUseAmount(one.getUseAmount())
                        .setPaymentAmount(totalAmount.subtract(ticketAmount).subtract(cashAmount));
                ticketInfo.add(resultOne);
            }
        }
        return Result.success(new CollectionObject(ticketInfo));
    }

    /**
     * 最新接口
     *
     * @param staffInfo
     * @param form
     * @return
     */
    @ApiOperation(value = "发票管理列表")
    @PostMapping("/manage")
    public Result<Page<OrderInvoiceInfoPageVO>> getOrderInvoiceInfoPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                        @RequestBody @Valid QueryInvoicePageForm form) {
        QueryInvoicePageRequest request = PojoUtils.map(form, QueryInvoicePageRequest.class);

        request.setOrderType(OrderTypeEnum.POP.getCode());

        List<Long> eidList = new ArrayList<>();

        if (staffInfo.getYilingFlag()) {
            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            request.setContacterIdList(contacterIdList);
            if (form.getDistributorEid() == null || form.getDistributorEid() == 0) {
                List<Long> ids = enterpriseApi.listSubEids(Constants.YILING_EID);
                eidList.addAll(ids);
            } else {
                eidList.addAll(new ArrayList<Long>() {{
                    form.getDistributorEid();
                }});
            }

        } else {
            if (form.getDistributorEid() == null || form.getDistributorEid() == 0) {
                eidList.add(staffInfo.getCurrentEid());
                EnterpriseDepartmentDTO enterpriseDepartment = null;
                if(form.getDepartmentType() != null){
                    if (form.getDepartmentType() == 3){
                        //大运河数拓部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_ST_DEPARTMENT_CODE);
                    }else if(form.getDepartmentType() == 4){
                        //大运河分销部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_FX_DEPARTMENT_CODE);
                    }
                }

                if(enterpriseDepartment != null){
                    request.setDepartmentId(enterpriseDepartment.getId());
                }
            } else {
                return Result.success(new Page<OrderInvoiceInfoPageVO>());
            }
        }
        request.setEidLists(eidList);

        Page<OrderDTO> page = orderApi.getOrderInvoiceInfoPage(request);
        Page<OrderInvoiceInfoPageVO> result = PojoUtils.map(page, OrderInvoiceInfoPageVO.class);
        if (page != null && CollectionUtil.isNotEmpty(page.getRecords())) {
            List<Long> orderList = page.getRecords().stream().map(o -> o.getId()).collect(Collectors.toList());
            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(orderList);

            for (OrderInvoiceInfoPageVO one : result.getRecords()) {
                //货款总金额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //票折总金额
                BigDecimal ticketAmount = BigDecimal.ZERO;
                //现在总金额
                BigDecimal cashAmount = BigDecimal.ZERO;
                if (one.getInvoiceStatus() == 0) {
                    one.setInvoiceStatus(1);
                }
                for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
                    if (changeOne.getOrderId().equals(one.getId())) {
                        totalAmount = totalAmount.add(changeOne.getGoodsAmount()
                                .subtract(changeOne.getSellerReturnAmount()));
                        ticketAmount = ticketAmount.add(changeOne.getTicketDiscountAmount()
                                .subtract(changeOne.getSellerReturnTicketDiscountAmount()));
                        cashAmount = cashAmount.add(changeOne.getCashDiscountAmount()
                                .subtract(changeOne.getSellerReturnCashDiscountAmount()));
                    }
                }
                one.setTotalAmount(totalAmount)
                        .setDiscountAmount(ticketAmount.add(cashAmount))
                        .setPaymentAmount(totalAmount.subtract(ticketAmount).subtract(cashAmount));
                List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyApi.listByOrderId(one.getId());
                if (CollectionUtil.isNotEmpty(orderInvoiceApplyList)) {
                    List<Long> applyIdList = orderInvoiceApplyList.stream().map(o -> o.getId()).collect(Collectors.toList());
                    List<OrderInvoiceDTO> orderInvoiceByApplyIdList = orderInvoiceApi.getOrderInvoiceByApplyIdList(applyIdList);
                    if (CollectionUtil.isNotEmpty(orderInvoiceByApplyIdList)) {
                        String invoiceNo = orderInvoiceByApplyIdList.stream().map(o -> o.getInvoiceNo()).collect(Collectors.joining(","));
                        BigDecimal invoiceFinishAmount = orderInvoiceApplyList.stream().map(OrderInvoiceApplyDTO::getInvoiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

                        //String invoiceNo = invoiceList.stream().collect(Collectors.joining(","));
                        one.setInvoiceNo(invoiceNo);
                        one.setInvoiceFinishAmount(invoiceFinishAmount);
                    }
                }
            }
        }

        return Result.success(result);

    }


    @ApiOperation(value = "申请发票页面信息")
    @GetMapping("/get/apply")
    public Result<OrderApplyInvoiceVO> getOrderApplyInvoiceInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                                @RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderApplyInvoiceVO result = PojoUtils.map(orderInfo, OrderApplyInvoiceVO.class);
        if (OrderInvoiceApplyStatusEnum.DEFAULT_VALUE_APPLY == OrderInvoiceApplyStatusEnum.getByCode(result.getInvoiceStatus())) {
            result.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
        }
        //出库单商品信息
        Map<String, List<OrderDeliveryErpDTO>> deliveryErpMap = getDeliveryErpMap(orderId);

        //已开票信息
        Map<String, List<OrderInvoiceDetailDTO>> orderInvoiceDetailMap = getOrderInvoiceDetailMap(orderId);

        //明细信息
        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        Map<Long, OrderDetailDTO> detailMap = orderDetailInfo.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));

        List<OrderInvoiceErpDeliveryNoVO> orderInvoiceErpDeliveryNoList = new ArrayList<>();

        //商品id
        List<Long> goodIdList = new ArrayList<>();

        Map<String, List<OrderApplyInvoiceDetailVO>> mapResult = new HashMap<>();

        List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyApi.listByOrderId(orderId);
        if(CollectionUtil.isNotEmpty(orderInvoiceApplyList)){
            if( OrderTransitionRuleCodeEnum.getByCode(orderInvoiceApplyList.get(orderInvoiceApplyList.size() -1 ).getTransitionRuleCode()) == OrderTransitionRuleCodeEnum.SALE_OUT_ORDER_OPEN_NOT_INVOICE){
                result.setTransitionRuleCode("");
            }else{
                result.setTransitionRuleCode(orderInvoiceApplyList.get(orderInvoiceApplyList.size() -1 ).getTransitionRuleCode());

            }
        }

        for (Map.Entry<String, List<OrderDeliveryErpDTO>> entry : deliveryErpMap.entrySet()) {
            String detailId = entry.getKey().split("_")[0];
            String erpDeliveryNo = entry.getKey().split("_")[1];

            List<OrderDeliveryErpDTO> batchList = entry.getValue();
            //发货数量
            Integer deliveryNumber = 0;
            //出库单批次现折金额
            BigDecimal cashAllDiscountAmount = BigDecimal.ZERO;

            //保存同一批次号的数据
            Map<String,OrderDeliveryErpDTO>  erpDeliveryMap = new HashMap<>();
            for (OrderDeliveryErpDTO one : batchList) {
                deliveryNumber = deliveryNumber + one.getDeliveryQuantity();
                cashAllDiscountAmount = cashAllDiscountAmount.add(one.getCashDiscountAmount());


                if(erpDeliveryMap.containsKey(one.getBatchNo())){
                    OrderDeliveryErpDTO deliveryErpDTO = erpDeliveryMap.get(one.getBatchNo());
                    deliveryErpDTO.setDeliveryQuantity(deliveryErpDTO.getDeliveryQuantity() + one.getDeliveryQuantity());
                }else{
                    erpDeliveryMap.put(one.getBatchNo(),one);
                }
            }
            if (deliveryNumber == 0) {
                continue;
            }
            List<OrderInvoiceDetailDTO> orderInvoiceDetailList = orderInvoiceDetailMap.get(entry.getKey());

            List<OrderDeliveryErpDTO> erpBatchDTOList = erpDeliveryMap.values().stream().collect(Collectors.toList());
            List<OrderBatchVO> applyBatchList = PojoUtils.map(erpBatchDTOList, OrderBatchVO.class);

            //使用现折金额
            BigDecimal usedCashDiscountAmount = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(orderInvoiceDetailList)) {
                for (OrderInvoiceDetailDTO orderInvoiceDetailOne : orderInvoiceDetailList) {
                    if (StringUtils.isBlank(orderInvoiceDetailOne.getBatchNo()) && orderInvoiceDetailOne.getInvoiceQuantity().compareTo(0) == 0) {
                        //如果为空，直接使用现折数变为全部使用的兼容旧数据全部发票情况
                        usedCashDiscountAmount = cashAllDiscountAmount;
                        break;
                    } else {
                        usedCashDiscountAmount = usedCashDiscountAmount.add(orderInvoiceDetailOne.getCashDiscountAmount());
                    }
                }

            }
            Integer remainInvoiceAllQuantity = 0;
            for (OrderBatchVO applyBatchOne : applyBatchList) {
                Integer invoiceQuantity = 0;
                if (CollectionUtil.isNotEmpty(orderInvoiceDetailList)) {
                    for (OrderInvoiceDetailDTO orderInvoiceDetailOne : orderInvoiceDetailList) {
                        if (StringUtils.isBlank(orderInvoiceDetailOne.getBatchNo()) && orderInvoiceDetailOne.getInvoiceQuantity().compareTo(0) == 0) {
                            invoiceQuantity = applyBatchOne.getDeliveryQuantity();
                            break;
                        } else if (applyBatchOne.getBatchNo().equals(orderInvoiceDetailOne.getBatchNo())) {
                            invoiceQuantity = invoiceQuantity + orderInvoiceDetailOne.getInvoiceQuantity();
                        }
                    }
                }

                applyBatchOne.setRemainInvoiceQuantity(applyBatchOne.getDeliveryQuantity() - invoiceQuantity);
                remainInvoiceAllQuantity = remainInvoiceAllQuantity + applyBatchOne.getRemainInvoiceQuantity();
            }


            OrderDetailDTO detailDTO = detailMap.get(Long.valueOf(detailId));
            OrderApplyInvoiceDetailVO applyInvoiceDetailVO = PojoUtils.map(detailDTO, OrderApplyInvoiceDetailVO.class);

            applyInvoiceDetailVO.setBatchList(applyBatchList);
            applyInvoiceDetailVO.setDeliveryQuantity(deliveryNumber);
            applyInvoiceDetailVO.setCashDiscountAmount(cashAllDiscountAmount.subtract(usedCashDiscountAmount));
            applyInvoiceDetailVO.setTicketDiscountAmount(BigDecimal.ZERO);
            applyInvoiceDetailVO.setRemainInvoiceAllAmount(applyInvoiceDetailVO.getGoodsPrice().multiply(BigDecimal.valueOf(remainInvoiceAllQuantity)));
            //发货金额
            BigDecimal deliveryAmount = applyInvoiceDetailVO.getGoodsPrice().multiply(BigDecimal.valueOf(deliveryNumber)).setScale(2, BigDecimal.ROUND_HALF_UP);
            //现折比率
            BigDecimal caseDiscountRate = detailDTO.getCashDiscountAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(detailDTO.getGoodsAmount(), 2, BigDecimal.ROUND_HALF_UP);
            applyInvoiceDetailVO.setCaseDiscountRate(caseDiscountRate);
            applyInvoiceDetailVO.setGoodsAmount(deliveryAmount);
            applyInvoiceDetailVO.setInvoiceDiscountType(2);
            applyInvoiceDetailVO.setGoodsDiscountAmount(applyInvoiceDetailVO.getCashDiscountAmount());
            applyInvoiceDetailVO.setGoodsDiscountRate(applyInvoiceDetailVO.getCaseDiscountRate());
            if (mapResult.containsKey(erpDeliveryNo)) {
                List<OrderApplyInvoiceDetailVO> orderApplyInvoiceDetailVOS = mapResult.get(erpDeliveryNo);
                orderApplyInvoiceDetailVOS.add(applyInvoiceDetailVO);
            } else {
                mapResult.put(erpDeliveryNo, new ArrayList<OrderApplyInvoiceDetailVO>() {{
                    add(applyInvoiceDetailVO);
                }});
            }
            goodIdList.add(applyInvoiceDetailVO.getGoodsId());
        }

        for (Map.Entry<String, List<OrderApplyInvoiceDetailVO>> entry : mapResult.entrySet()) {
            OrderInvoiceErpDeliveryNoVO one = new OrderInvoiceErpDeliveryNoVO();
            one.setErpDeliveryNo(entry.getKey());
            one.setApplyInvoiceDetailInfo(entry.getValue());
            orderInvoiceErpDeliveryNoList.add(one);
        }
        //图片信息
        if(CollectionUtil.isNotEmpty(goodIdList)){
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodIdList);
            for (OrderInvoiceErpDeliveryNoVO detailVO : orderInvoiceErpDeliveryNoList) {
                for (OrderApplyInvoiceDetailVO invoiceDetail : detailVO.getApplyInvoiceDetailInfo()) {
                    invoiceDetail.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(invoiceDetail.getGoodsId())));
                }
            }
        }


        String invoiceEmail = (String )redisService.get(RedisKey.generate("order", "invoice", "email", orderInfo.getBuyerEid().toString()));
        result.setInvoiceEmail(invoiceEmail);
        result.setOrderInvoiceErpDeliveryNoList(orderInvoiceErpDeliveryNoList);
        if(StringUtils.isNotBlank(result.getTransitionRuleCode())){
            //最大金额
            log.info("数据展示：{}", JSON.toJSONString(ruleMap));

            if(StringUtils.isNotEmpty(ruleMap.get(result.getTransitionRuleCode()))){
                BigDecimal amount = new BigDecimal(ruleMap.get(result.getTransitionRuleCode()));
                result.setInvoiceMaxAmount(amount);
            }
        }
        return Result.success(result);
    }

    private Map<String, List<OrderDeliveryErpDTO>> getDeliveryErpMap(Long orderId) {
        List<OrderDeliveryErpDTO> orderDeliveryErpList = orderDeliveryErpApi.listByOrderIds(new ArrayList<Long>() {{
            add(orderId);
        }});
        Map<String, List<OrderDeliveryErpDTO>> deliveryErpMap = new HashMap<>();
        for (OrderDeliveryErpDTO deliveryErpOne : orderDeliveryErpList) {
            if (deliveryErpMap.containsKey(deliveryErpOne.getDetailId() + "_" + deliveryErpOne.getErpDeliveryNo())) {
                List<OrderDeliveryErpDTO> deliveryErpList = deliveryErpMap.get(deliveryErpOne.getDetailId() + "_" + deliveryErpOne.getErpDeliveryNo());
                deliveryErpList.add(deliveryErpOne);
            } else {
                deliveryErpMap.put(deliveryErpOne.getDetailId() + "_" + deliveryErpOne.getErpDeliveryNo(), new ArrayList<OrderDeliveryErpDTO>() {{
                    add(deliveryErpOne);
                }});
            }
        }
        return deliveryErpMap;
    }

    private Map<String, List<OrderInvoiceDetailDTO>> getOrderInvoiceDetailMap(@RequestParam("orderId") Long orderId) {
        List<OrderInvoiceDetailDTO> orderInvoiceDetailList = orderInvoiceDetailApi.listByOrderIds(new ArrayList<Long>() {{
            add(orderId);
        }});
        Map<String, List<OrderInvoiceDetailDTO>> orderInvoiceDetailMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderInvoiceDetailList)) {
            orderInvoiceDetailMap = orderInvoiceDetailList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo()));
        }
        return orderInvoiceDetailMap;
    }

    @ApiOperation(value = "发票列表页面")
    @GetMapping("/apply/list")
    public Result<OrderInvoiceApplyListVO> getApplyInvoiceList(@CurrentUser CurrentStaffInfo staffInfo,
                                                               @RequestParam(value = "orderId") Long orderId) {

        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderInvoiceApplyListVO result = PojoUtils.map(orderInfo, OrderInvoiceApplyListVO.class);
        List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyApi.listByOrderId(orderId);
        List<OrderInvoiceApplyListVO.OrderInvoiceApplyOneVO> orderInvoiceApplyOneList = PojoUtils.map(orderInvoiceApplyList, OrderInvoiceApplyListVO.OrderInvoiceApplyOneVO.class);
        BigDecimal invoiceAmount = BigDecimal.ZERO;
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(orderInvoiceApplyOneList)) {
            //获取发票编号
            List<Long> applyIdList = orderInvoiceApplyOneList.stream().map(o -> o.getId()).collect(Collectors.toList());
            List<OrderInvoiceDTO> orderInvoiceByApplyIdList = orderInvoiceApi.getOrderInvoiceByApplyIdList(applyIdList);
            Map<Long, String> invoiceNoMap = orderInvoiceByApplyIdList.stream().collect(Collectors.groupingBy(OrderInvoiceDTO::getApplyId, Collectors.mapping(OrderInvoiceDTO::getInvoiceNo, Collectors.joining(","))));

            //申请人姓名
            List<Long> userList = orderInvoiceApplyOneList.stream().map(o -> o.getCreateUser()).collect(Collectors.toList());
            List<UserDTO> userDto = userApi.listByIds(userList);
            Map<Long, String> mapUser = userDto.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName, (k1, k2) -> k1));

            //规则名称转换
            List<DictDataBO> ruleList = dictTypeApi.mapByName("order_invoice_transition_rule");
            Map<String, String> ruleNameMap = ruleList.stream().collect(Collectors.toMap(DictDataBO::getValue, DictDataBO::getLabel, (k1, k2) -> k1));

            for (OrderInvoiceApplyListVO.OrderInvoiceApplyOneVO one : orderInvoiceApplyOneList) {
                one.setInvoiceNo(invoiceNoMap.get(one.getId()));
                one.setCreateUserName(mapUser.get(one.getCreateUser()));
                one.setTransitionRuleName(ruleNameMap.get(one.getTransitionRuleCode()));
                one.setTicketDiscountName(one.getTicketDiscountFlag() == 0 ? "否" : "是");
                invoiceAmount = invoiceAmount.add(one.getInvoiceAmount());
                ticketDiscountAmount = ticketDiscountAmount.add(one.getTicketDiscountAmount());
            }
            result.setInvoiceAmount(invoiceAmount);
            result.setTicketDiscountAmount(ticketDiscountAmount);
            result.setOrderInvoiceApplyOneList(orderInvoiceApplyOneList);
        }
        if (OrderInvoiceApplyStatusEnum.DEFAULT_VALUE_APPLY == OrderInvoiceApplyStatusEnum.getByCode(result.getInvoiceStatus())) {
            result.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
        }
        return Result.success(result);
    }

    @ApiOperation(value = "发票详情页面")
    @GetMapping("/apply/detail")
    public Result<OrderInvoiceApplyDetailOneVO> getApplyInvoiceDetail(@CurrentUser CurrentStaffInfo staffInfo,
                                                                      @RequestParam(value = "applyId") Long applyId) {
        OrderInvoiceApplyDTO applyDTO = orderInvoiceApplyApi.getOneById(applyId);
        OrderInvoiceApplyDetailOneVO result = PojoUtils.map(applyDTO, OrderInvoiceApplyDetailOneVO.class);
        if (applyDTO != null) {
            //规则转换
            List<DictDataBO> ruleList = dictTypeApi.mapByName("order_invoice_transition_rule");
            Map<String, String> ruleNameMap = ruleList.stream().collect(Collectors.toMap(DictDataBO::getValue, DictDataBO::getLabel, (k1, k2) -> k1));

            UserDTO user = userApi.getById(applyDTO.getCreateUser());
            List<OrderInvoiceDTO> orderInvoiceByApplyIdList = orderInvoiceApi.getOrderInvoiceByApplyId(applyId);
            if (CollectionUtil.isNotEmpty(orderInvoiceByApplyIdList)) {
                String invoiceNo = orderInvoiceByApplyIdList.stream().map(o -> o.getInvoiceNo()).collect(Collectors.joining(","));
                result.setInvoiceQuantity(orderInvoiceByApplyIdList.size());
                result.setInvoiceNo(invoiceNo);
            }
            List<OrderTicketDiscountDTO> ticketList = orderTicketDiscountApi.listByApplyId(applyId);
            if (CollectionUtil.isNotEmpty(ticketList)) {
                List<OrderTicketDiscountVO> orderTicketDiscountList = new ArrayList<>();
                for (OrderTicketDiscountDTO one : ticketList) {
                    OrderTicketDiscountVO ticketOne = new OrderTicketDiscountVO();
                    ticketOne.setTicketDiscountAmount(one.getUseAmount());
                    ticketOne.setTicketDiscountNo(one.getTicketDiscountNo());
                    orderTicketDiscountList.add(ticketOne);
                }
                result.setOrderTicketDiscountList(orderTicketDiscountList);
            }
            result.setCreateUserName(user != null ? user.getName() : "");
            result.setTransitionRuleName(ruleNameMap.get(applyDTO.getTransitionRuleCode()));

            //根据申请id找到组
            List<OrderInvoiceDeliveryGroupDTO> orderInvoiceDeliveryGroupList = orderInvoiceDeliveryGroupApi.listByApplyId(applyId);
            List<String> groupList = orderInvoiceDeliveryGroupList.stream().map(o -> o.getGroupNo()).collect(Collectors.toList());

            //根据组id找到开票信息
            List<OrderInvoiceDetailDTO> orderInvoiceDetailList = orderInvoiceDetailApi.listByGroupNoList(groupList);
            Map<String, List<OrderInvoiceDetailDTO>> orderInvoiceDetailMap = orderInvoiceDetailList.stream().collect(Collectors.groupingBy(s -> s.getGroupNo()));

            //明细id
            List<OrderDetailDTO> orderDetail = orderDetailApi.getOrderDetailInfo(applyDTO.getOrderId());
            Map<Long, OrderDetailDTO> mapDetail = orderDetail.stream().collect(Collectors.toMap(o -> o.getId(), o -> o, (k1, k2) -> k1));

            //发货信息
            List<OrderDeliveryErpDTO> deliveryErpList = orderDeliveryErpApi.listByOrderIds(new ArrayList<Long>() {{
                add(applyDTO.getOrderId());
            }});
            Map<String, List<OrderDeliveryErpDTO>> deliveryErpMap = deliveryErpList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo()));

            //商品Id
            List<Long> goodsList = new ArrayList<>();

            if (StringUtils.isEmpty(orderInvoiceDetailList.get(0).getBatchNo())) {
                //旧数据组装
                getOldApplyDetail(result, orderInvoiceDeliveryGroupList, orderInvoiceDetailMap, mapDetail, deliveryErpMap, goodsList);
            } else {
                //新数据组装
                List<OrderInvoiceGroupVO> orderInvoiceGroupList = new ArrayList<>();
                Map<String, OrderDeliveryErpDTO> deliveryErpOneMap = new HashMap<>();
                for(OrderDeliveryErpDTO erpDTO : deliveryErpList){
                    if(deliveryErpOneMap.containsKey(erpDTO.getDetailId() + "_" + erpDTO.getErpDeliveryNo() + "_" + erpDTO.getBatchNo())){
                        OrderDeliveryErpDTO erpOneDTO = deliveryErpOneMap.get(erpDTO.getDetailId() + "_" + erpDTO.getErpDeliveryNo() + "_" + erpDTO.getBatchNo());
                        erpOneDTO.setDeliveryQuantity(erpOneDTO.getDeliveryQuantity() + erpDTO.getDeliveryQuantity());
                        erpOneDTO.setCashDiscountAmount(erpOneDTO.getCashDiscountAmount().add(erpDTO.getCashDiscountAmount()));
                    }else{
                        OrderDeliveryErpDTO erpOne = new OrderDeliveryErpDTO();
                        erpOne.setOrderId(erpDTO.getOrderId());
                        erpOne.setDetailId(erpDTO.getDetailId());
                        erpOne.setBatchNo(erpDTO.getBatchNo());
                        erpOne.setExpiryDate(erpDTO.getExpiryDate());
                        erpOne.setProduceDate(erpDTO.getProduceDate());
                        erpOne.setDeliveryQuantity(erpDTO.getDeliveryQuantity());
                        erpOne.setCashDiscountAmount(erpDTO.getCashDiscountAmount());
                        erpOne.setErpDeliveryNo(erpDTO.getErpDeliveryNo());
                        erpOne.setErpSendOrderId(erpDTO.getErpSendOrderId());
                        erpOne.setId(erpDTO.getId());
                        deliveryErpOneMap.put(erpDTO.getDetailId() + "_" + erpDTO.getErpDeliveryNo() + "_" + erpDTO.getBatchNo(),erpOne);
                    }
                }

                for (OrderInvoiceDeliveryGroupDTO groupOne : orderInvoiceDeliveryGroupList) {
                    OrderInvoiceGroupVO groupVO = new OrderInvoiceGroupVO();
                    groupVO.setGroupDeliveryNos(groupOne.getGroupNo());
                    groupVO.setInvoiceSummary(groupOne.getInvoiceSummary());
                    List<OrderInvoiceErpDeliveryNoVO> orderInvoiceErpDeliveryNoList = new ArrayList<>();
                    List<OrderInvoiceDetailDTO> invoiceDetailList = orderInvoiceDetailMap.get(groupOne.getGroupNo());
                    Map<String, List<OrderInvoiceDetailDTO>> erpDeliveryList = invoiceDetailList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo()));
                    Map<String, List<OrderApplyInvoiceDetailVO>> resultMap = new HashMap<>();
                    for (Map.Entry<String, List<OrderInvoiceDetailDTO>> entry : erpDeliveryList.entrySet()) {
                        String erpDeliveryNo = entry.getKey().split("_")[1];
                        String detailId = entry.getKey().split("_")[0];
                        List<OrderInvoiceDetailDTO> entryValueList = entry.getValue();
                        OrderDetailDTO detailDTO = mapDetail.get(Long.valueOf(detailId));
                        OrderApplyInvoiceDetailVO resultOne = PojoUtils.map(detailDTO, OrderApplyInvoiceDetailVO.class);
                        goodsList.add(resultOne.getGoodsId());
                        //发货数量
                        Integer deliveryQuantity = 0;
                        List<OrderDeliveryErpDTO> orderDeliveryErpDTOS = deliveryErpMap.get(entry.getKey());
                        for (OrderDeliveryErpDTO deliveryErpOne : orderDeliveryErpDTOS) {
                            deliveryQuantity = deliveryQuantity + deliveryErpOne.getDeliveryQuantity();
                        }
                        resultOne.setDeliveryQuantity(deliveryQuantity);

                        //开票数量
                        Integer invoiceAllQuantity = 0;
                        //折扣金额
                        BigDecimal goodsDiscountAmount = BigDecimal.ZERO;
                        //批次信息
                        List<OrderBatchVO> batchList = new ArrayList<>();
                        Map<String, List<OrderInvoiceDetailDTO>> batchInvoice = entryValueList.stream().collect(Collectors.groupingBy(s -> s.getBatchNo()));
                        for (Map.Entry<String, List<OrderInvoiceDetailDTO>> entryInvoice : batchInvoice.entrySet()) {
                            Integer invoiceBatchQuantity = 0;
                            for(OrderInvoiceDetailDTO dto : entryInvoice.getValue()){
                                invoiceAllQuantity = invoiceAllQuantity + dto.getInvoiceQuantity();
                                goodsDiscountAmount = goodsDiscountAmount.add(dto.getCashDiscountAmount().add(dto.getTicketDiscountAmount()));
                                invoiceBatchQuantity = invoiceBatchQuantity +dto.getInvoiceQuantity();
                            }
                            OrderDeliveryErpDTO orderDeliveryErpOne = deliveryErpOneMap.get(detailId + "_" + erpDeliveryNo + "_" + entryInvoice.getKey());
                            OrderBatchVO batchOne = PojoUtils.map(orderDeliveryErpOne, OrderBatchVO.class);
                            batchOne.setInvoiceQuantity(invoiceBatchQuantity);
                            batchList.add(batchOne);
                        }

                        resultOne.setInvoiceAllQuantity(invoiceAllQuantity);
                        resultOne.setGoodsDiscountAmount(goodsDiscountAmount);
                        resultOne.setGoodsAmount(resultOne.getGoodsPrice().multiply(BigDecimal.valueOf(invoiceAllQuantity)).setScale(2, BigDecimal.ROUND_HALF_UP));
                        BigDecimal goodsDiscountRate = resultOne.getGoodsDiscountAmount().multiply(BigDecimal.valueOf(100))
                                .divide(resultOne.getGoodsAmount(), 2, BigDecimal.ROUND_HALF_UP);
                        resultOne.setGoodsDiscountRate(goodsDiscountRate);
                        resultOne.setBatchList(batchList);
                        if (resultMap.containsKey(erpDeliveryNo)) {
                            List<OrderApplyInvoiceDetailVO> applyInvoiceDetailVOS = resultMap.get(erpDeliveryNo);
                            applyInvoiceDetailVOS.add(resultOne);
                        } else {
                            resultMap.put(erpDeliveryNo, new ArrayList<OrderApplyInvoiceDetailVO>() {{
                                add(resultOne);
                            }});
                        }

                    }
                    for (Map.Entry<String, List<OrderApplyInvoiceDetailVO>> entryResult : resultMap.entrySet()) {
                        OrderInvoiceErpDeliveryNoVO invoiceErpDeliveryNo = new OrderInvoiceErpDeliveryNoVO();
                        invoiceErpDeliveryNo.setErpDeliveryNo(entryResult.getKey());
                        invoiceErpDeliveryNo.setApplyInvoiceDetailInfo(entryResult.getValue());
                        orderInvoiceErpDeliveryNoList.add(invoiceErpDeliveryNo);
                    }
                    groupVO.setOrderInvoiceErpDeliveryNoList(orderInvoiceErpDeliveryNoList);
                    orderInvoiceGroupList.add(groupVO);
                    result.setOrderInvoiceGroupList(orderInvoiceGroupList);
                }
            }
            //图片信息
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsList);
            for (OrderInvoiceGroupVO detailVO : result.getOrderInvoiceGroupList()) {
                for (OrderInvoiceErpDeliveryNoVO erpDeliveryNoVO : detailVO.getOrderInvoiceErpDeliveryNoList()) {
                    for (OrderApplyInvoiceDetailVO invoiceDetail : erpDeliveryNoVO.getApplyInvoiceDetailInfo()) {
                        invoiceDetail.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(invoiceDetail.getGoodsId())));
                    }
                }
            }

        }
        return Result.success(result);
    }

    /**
     * 旧数据展示信息
     *
     * @param result
     * @param orderInvoiceDeliveryGroupList
     * @param orderInvoiceDetailMap
     * @param mapDetail
     * @param deliveryErpMap
     * @param goodsList
     */
    private void getOldApplyDetail(OrderInvoiceApplyDetailOneVO result,
                                   List<OrderInvoiceDeliveryGroupDTO> orderInvoiceDeliveryGroupList,
                                   Map<String, List<OrderInvoiceDetailDTO>> orderInvoiceDetailMap,
                                   Map<Long, OrderDetailDTO> mapDetail,
                                   Map<String, List<OrderDeliveryErpDTO>> deliveryErpMap,
                                   List<Long> goodsList) {
        List<OrderInvoiceGroupVO> orderInvoiceGroupList = new ArrayList<>();
        for (OrderInvoiceDeliveryGroupDTO groupOne : orderInvoiceDeliveryGroupList) {

            OrderInvoiceGroupVO groupVO = new OrderInvoiceGroupVO();
            groupVO.setGroupDeliveryNos(groupOne.getGroupNo());
            groupVO.setInvoiceSummary(groupOne.getInvoiceSummary());
            List<OrderInvoiceErpDeliveryNoVO> orderInvoiceErpDeliveryNoList = new ArrayList<>();
            List<OrderInvoiceDetailDTO> invoiceDetailList = orderInvoiceDetailMap.get(groupOne.getGroupNo());

            Map<String, OrderInvoiceDetailDTO> erpDeliveryList = invoiceDetailList.stream().collect(Collectors.toMap(s -> s.getDetailId() + "_" + s.getErpDeliveryNo(), o -> o, (k1, k2) -> k1));
            Map<String, List<OrderApplyInvoiceDetailVO>> resultMap = new HashMap<>();
            for (Map.Entry<String, OrderInvoiceDetailDTO> entry : erpDeliveryList.entrySet()) {

                OrderInvoiceDetailDTO detailOne = entry.getValue();
                String detailId = entry.getKey().split("_")[0];
                String erpDeliveryNo = entry.getKey().split("_")[1];
                OrderApplyInvoiceDetailVO resultOne = getOrderApplyInvoiceDetailVO(mapDetail, deliveryErpMap, entry, detailId, detailOne);
                goodsList.add(resultOne.getGoodsId());

                if (resultMap.containsKey(erpDeliveryNo)) {
                    List<OrderApplyInvoiceDetailVO> applyInvoiceDetailVOS = resultMap.get(erpDeliveryNo);
                    applyInvoiceDetailVOS.add(resultOne);
                } else {
                    resultMap.put(erpDeliveryNo, new ArrayList<OrderApplyInvoiceDetailVO>() {{
                        add(resultOne);
                    }});
                }
            }

            for (Map.Entry<String, List<OrderApplyInvoiceDetailVO>> entryResult : resultMap.entrySet()) {
                OrderInvoiceErpDeliveryNoVO invoiceErpDeliveryNo = new OrderInvoiceErpDeliveryNoVO();
                invoiceErpDeliveryNo.setErpDeliveryNo(entryResult.getKey());
                invoiceErpDeliveryNo.setApplyInvoiceDetailInfo(entryResult.getValue());
                orderInvoiceErpDeliveryNoList.add(invoiceErpDeliveryNo);
            }
            groupVO.setOrderInvoiceErpDeliveryNoList(orderInvoiceErpDeliveryNoList);
            orderInvoiceGroupList.add(groupVO);
        }
        result.setOrderInvoiceGroupList(orderInvoiceGroupList);
    }

    private OrderApplyInvoiceDetailVO getOrderApplyInvoiceDetailVO(Map<Long, OrderDetailDTO> mapDetail,
                                                                   Map<String, List<OrderDeliveryErpDTO>> deliveryErpMap,
                                                                   Map.Entry<String, OrderInvoiceDetailDTO> entry,
                                                                   String detailId,
                                                                   OrderInvoiceDetailDTO detailOne) {
        OrderDetailDTO detailDTO = mapDetail.get(Long.valueOf(detailId));
        OrderApplyInvoiceDetailVO resultOne = PojoUtils.map(detailDTO, OrderApplyInvoiceDetailVO.class);
        Integer deliveryQuantity = 0;
        List<OrderDeliveryErpDTO> orderDeliveryErpDTOS = deliveryErpMap.get(entry.getKey());
        List<OrderBatchVO> batchList = new ArrayList<>();
        for (OrderDeliveryErpDTO deliveryErpOne : orderDeliveryErpDTOS) {
            OrderBatchVO batchOne = PojoUtils.map(deliveryErpOne, OrderBatchVO.class);
            batchOne.setInvoiceQuantity(batchOne.getDeliveryQuantity());
            batchList.add(batchOne);
            deliveryQuantity = deliveryQuantity + batchOne.getDeliveryQuantity();
        }
        resultOne.setDeliveryQuantity(deliveryQuantity);
        resultOne.setInvoiceAllQuantity(deliveryQuantity);
        resultOne.setGoodsAmount(resultOne.getGoodsPrice().multiply(BigDecimal.valueOf(deliveryQuantity)).setScale(2, BigDecimal.ROUND_HALF_UP));
        resultOne.setGoodsDiscountAmount(detailOne.getCashDiscountAmount().add(detailOne.getTicketDiscountAmount()));
        BigDecimal goodsDiscountRate = resultOne.getGoodsDiscountAmount().multiply(BigDecimal.valueOf(100))
                .divide(resultOne.getGoodsAmount(), 2, BigDecimal.ROUND_HALF_UP);
        resultOne.setGoodsDiscountRate(goodsDiscountRate);
        resultOne.setBatchList(batchList);
        return resultOne;
    }


    @ApiOperation(value = "申请发票提交")
    @PostMapping("/apply/submit")
    public Result<BoolObject> saveTicketDiscountInfo(@CurrentUser CurrentStaffInfo staffInfo,
                                                     @Valid @RequestBody SaveOrderDetailTicketDiscountForm form) {
        SaveOrderTicketApplyInfoRequest request = PojoUtils.map(form, SaveOrderTicketApplyInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        Boolean result = orderInvoiceApplyProcessApi.apply(request);
        return Result.success(new BoolObject(result));
    }


    @ApiOperation(value = "选择票折计算金额")
    @PostMapping("/compute/amount")
    public Result<OrderApplyTicketComputeVO> computeAmount(@CurrentUser CurrentStaffInfo staffInfo,
                                                           @Valid @RequestBody OrderApplyTicketComputeForm form) {
        log.info("选择票折计算金额，OrderApplyTicketComputeForm：{}", JSON.toJSONString(form));
        List<Long> ids = new ArrayList<Long>() {{
            add(form.getOrderId());
        }};
        OrderApplyTicketComputeVO result = new OrderApplyTicketComputeVO();

        List<OrderDeliveryErpDTO> orderDeliveryErpList = orderDeliveryErpApi.listByOrderIds(ids);
        Map<String, OrderDeliveryErpDTO> orderDeliveryMap = new HashMap<>();
        for(OrderDeliveryErpDTO deliveryOne : orderDeliveryErpList){
            if(orderDeliveryMap.containsKey(deliveryOne.getDetailId() + "_" + deliveryOne.getErpDeliveryNo() + "_" + deliveryOne.getBatchNo())){
                OrderDeliveryErpDTO erpDTO = orderDeliveryMap.get(deliveryOne.getDetailId() + "_" + deliveryOne.getErpDeliveryNo() + "_" + deliveryOne.getBatchNo());
                erpDTO.setDeliveryQuantity(erpDTO.getDeliveryQuantity() +deliveryOne.getDeliveryQuantity() );
                erpDTO.setCashDiscountAmount(erpDTO.getCashDiscountAmount().add(deliveryOne.getCashDiscountAmount()));
            }else{
                OrderDeliveryErpDTO erpOne = new OrderDeliveryErpDTO();
                erpOne.setOrderId(deliveryOne.getOrderId());
                erpOne.setDetailId(deliveryOne.getDetailId());
                erpOne.setBatchNo(deliveryOne.getBatchNo());
                erpOne.setExpiryDate(deliveryOne.getExpiryDate());
                erpOne.setProduceDate(deliveryOne.getProduceDate());
                erpOne.setDeliveryQuantity(deliveryOne.getDeliveryQuantity());
                erpOne.setCashDiscountAmount(deliveryOne.getCashDiscountAmount());
                erpOne.setErpDeliveryNo(deliveryOne.getErpDeliveryNo());
                erpOne.setErpSendOrderId(deliveryOne.getErpSendOrderId());
                erpOne.setId(deliveryOne.getId());
                orderDeliveryMap.put(deliveryOne.getDetailId() + "_" + deliveryOne.getErpDeliveryNo() + "_" + deliveryOne.getBatchNo(),erpOne);
            }
        }

        List<OrderInvoiceDetailDTO> orderInvoiceDetailList = orderInvoiceDetailApi.listByOrderIds(ids);
        Map<String, List<OrderInvoiceDetailDTO>> orderInvoiceDetailMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderInvoiceDetailList)) {
            orderInvoiceDetailMap = orderInvoiceDetailList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo() + "_" + s.getBatchNo()));
        }
        //现折金额
        BigDecimal cashDiscountAmount = BigDecimal.ZERO;
        //开票数量
        Integer invoiceQuantity = 0;

        for (OrderApplyComputeBatchForm one : form.getBatchFormList()) {
            OrderDeliveryErpDTO orderDeliveryErpOne = orderDeliveryMap.get(form.getDetailId() + "_" + form.getErpDeliveryNo() + "_" + one.getBatchNo());

            List<OrderInvoiceDetailDTO> invoiceDetailList = orderInvoiceDetailMap.get(form.getDetailId() + "_" + form.getErpDeliveryNo() + "_" + one.getBatchNo());
            //已开票数量
            Integer pastInvoiceQuantity = 0;
            //已开现折金额
            BigDecimal pastCashDiscountAmount = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(invoiceDetailList)) {
                for (OrderInvoiceDetailDTO detailOne : invoiceDetailList) {
                    pastInvoiceQuantity = pastInvoiceQuantity + detailOne.getInvoiceQuantity();
                    pastCashDiscountAmount = pastCashDiscountAmount.add(detailOne.getCashDiscountAmount());
                }
            }

            if (orderDeliveryErpOne.getDeliveryQuantity().compareTo(pastInvoiceQuantity + one.getInvoiceQuantity()) == 0) {
                cashDiscountAmount = cashDiscountAmount.add(orderDeliveryErpOne.getCashDiscountAmount().subtract(pastCashDiscountAmount));
            } else if (orderDeliveryErpOne.getDeliveryQuantity().compareTo(pastInvoiceQuantity + one.getInvoiceQuantity()) > 0) {
                BigDecimal cashDiscountOne = BigDecimal.valueOf(one.getInvoiceQuantity()).multiply(orderDeliveryErpOne.getCashDiscountAmount())
                        .divide(BigDecimal.valueOf(orderDeliveryErpOne.getDeliveryQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                cashDiscountAmount = cashDiscountAmount.add(cashDiscountOne);
            } else if (orderDeliveryErpOne.getDeliveryQuantity().compareTo(pastInvoiceQuantity + one.getInvoiceQuantity()) < 0) {
                throw new BusinessException(OrderErrorCode.ORDER_INVOICE_QUANTITY_ERROR);
            }
            invoiceQuantity = invoiceQuantity + one.getInvoiceQuantity();
        }
        //金额小计
        BigDecimal remainInvoiceAllAmount = form.getGoodsPrice().multiply(BigDecimal.valueOf(invoiceQuantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (form.getInvoiceDiscountType().equals(OrderInvoiceDiscountTypeEnum.USE_RATE_TYPE.getCode())) {
            BigDecimal ticketDiscountRate = form.getValue();
            BigDecimal ticketDiscountAmount = remainInvoiceAllAmount.multiply(form.getValue()).divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);
            result.setTicketDiscountAmount(ticketDiscountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            result.setTicketDiscountRate(ticketDiscountRate);
            result.setCashDiscountAmount(cashDiscountAmount);
            result.setRemainInvoiceAllAmount(remainInvoiceAllAmount);
            result.setGoodsDiscountAmount(result.getTicketDiscountAmount().add(cashDiscountAmount));
            if(remainInvoiceAllAmount.compareTo(BigDecimal.ZERO) != 0){
                BigDecimal goodsDiscountRate = result.getGoodsDiscountAmount().multiply(BigDecimal.valueOf(100)).divide(remainInvoiceAllAmount, 2, BigDecimal.ROUND_HALF_UP);
                result.setGoodsDiscountRate(goodsDiscountRate);
            }else{
                result.setGoodsDiscountRate(BigDecimal.ZERO);
            }



        } else {
            result.setTicketDiscountAmount(form.getValue());

            result.setCashDiscountAmount(cashDiscountAmount);
            result.setGoodsDiscountAmount(result.getTicketDiscountAmount().add(cashDiscountAmount));
            result.setRemainInvoiceAllAmount(remainInvoiceAllAmount);

            if(remainInvoiceAllAmount.compareTo(BigDecimal.ZERO) != 0){
                BigDecimal ticketDiscountRate = form.getValue().multiply(BigDecimal.valueOf(100))
                        .divide(remainInvoiceAllAmount, 2, BigDecimal.ROUND_HALF_UP);
                result.setTicketDiscountRate(ticketDiscountRate);
                BigDecimal goodsDiscountRate = result.getGoodsDiscountAmount().multiply(BigDecimal.valueOf(100)).divide(remainInvoiceAllAmount, 2, BigDecimal.ROUND_HALF_UP);
                result.setGoodsDiscountRate(goodsDiscountRate);
            }else{
                result.setTicketDiscountRate(BigDecimal.ZERO);
                result.setGoodsDiscountRate(BigDecimal.ZERO);
            }
        }

        return Result.success(result);
    }

    @ApiOperation(value = "获取最开票限制金额")
    @GetMapping("/get/max/amount")
    public Result getOrderInvoiceMaxAmount(@RequestParam(value = "orderId") Long orderId,
                                           @RequestParam(value = "transitionRuleCode") String transitionRuleCode) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        //最大金额
        log.info("数据展示：{}", JSON.toJSONString(ruleMap));

        if(StringUtils.isNotEmpty(ruleMap.get(transitionRuleCode))){
            BigDecimal amount = new BigDecimal(ruleMap.get(transitionRuleCode));
            return Result.success(amount);
        }else{
            return Result.success();
        }
    }

    @ApiOperation(value = "计算开票总金额字段")
    @PostMapping("/get/amount")
    public Result<OrderInvoiceAmountVO> getAllMount( @Valid @RequestBody QueryOrderInvoiceAmountForm form){
        OrderInvoiceAmountVO result = new OrderInvoiceAmountVO();
         List<QueryOrderInvoiceAmountErpDeliveryNoForm> list = form.getList();
        BigDecimal invoiceAllAmount = BigDecimal.ZERO;
        BigDecimal goodsAmount = BigDecimal.ZERO;
        BigDecimal cashDiscountAmount = BigDecimal.ZERO;
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
        for(QueryOrderInvoiceAmountErpDeliveryNoForm erpDeliveryNoForm : list ){
            List< Long> ids = erpDeliveryNoForm.getDetailFormList().stream().map(o -> o.getDetailId()).collect(Collectors.toList());
            List<OrderDetailDTO> orderDetailList = orderDetailApi.listByIdList(ids);
            Map<Long, OrderDetailDTO> detailMap = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o));

            for(QueryOrderInvoiceAmountDetailForm one : erpDeliveryNoForm.getDetailFormList() ){
                OrderDetailDTO detailDTO = detailMap.get(one.getDetailId());
                Integer invoiceAllQuantity = 0;
                for(SaveOrderTicketBatchForm batchOne : one.getSaveOrderTicketBatchList()){
                    invoiceAllQuantity = invoiceAllQuantity + batchOne.getInvoiceQuantity();
                }
                BigDecimal multiply = detailDTO.getGoodsPrice().multiply(BigDecimal.valueOf(invoiceAllQuantity));
                goodsAmount = goodsAmount.add(multiply);
                invoiceAllAmount = invoiceAllAmount.add(multiply).subtract(one.getGoodsDiscountAmount());
                cashDiscountAmount = cashDiscountAmount.add(one.getCashDiscountAmount());
                ticketDiscountAmount = ticketDiscountAmount.add(one.getTicketDiscountAmount());
            }
        }

        result.setInvoiceAllAmount(invoiceAllAmount);
        result.setGoodsAmount(goodsAmount);
        result.setCashDiscountAmount(cashDiscountAmount);
        result.setTicketDiscountAmount(ticketDiscountAmount);
        return  Result.success(result);
    }

}
