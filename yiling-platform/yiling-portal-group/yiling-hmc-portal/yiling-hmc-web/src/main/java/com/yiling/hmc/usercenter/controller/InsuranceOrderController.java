package com.yiling.hmc.usercenter.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.yiling.hmc.insurance.dto.request.ConfirmTookRequest;
import com.yiling.hmc.insurance.dto.request.SaveClaimInformationRequest;
import com.yiling.hmc.order.dto.*;
import com.yiling.hmc.usercenter.form.*;
import com.yiling.hmc.usercenter.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.api.OrderDetailApi;
import com.yiling.hmc.order.api.OrderPrescriptionApi;
import com.yiling.hmc.order.api.OrderPrescriptionGoodsApi;
import com.yiling.hmc.order.api.OrderRelateUserApi;
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.enums.HmcCreateSourceEnum;
import com.yiling.hmc.order.enums.HmcDeliveryTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcOrderTypeEnum;
import com.yiling.hmc.order.enums.HmcPaymentMethodEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.order.enums.HmcPrescriptionStatusEnum;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanApi;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanDetailApi;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanGroupDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.request.ConfirmOrderRequest;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;
import com.yiling.user.system.bo.CurrentUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 兑保记录 Controller
 *
 * @author: fan.shen
 * @date: 2022/4/6
 */
@RestController
@RequestMapping("/insurance_order/")
@Api(tags = "兑付订单")
@Slf4j
public class InsuranceOrderController extends BaseController {

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    InsuranceFetchPlanDetailApi fetchPlanDetailApi;

    @DubboReference
    InsuranceFetchPlanApi fetchPlanApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    OrderPrescriptionApi prescriptionApi;

    @DubboReference
    OrderPrescriptionGoodsApi prescriptionGoodsApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    OrderRelateUserApi orderRelateUserApi;

    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    FileService fileService;


    /**
     * 创建订单
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/create_order")
    @Log(title = "创建订单", businessType = BusinessTypeEnum.OTHER)
    public Result<OrderVO> createOrder(@CurrentUser CurrentUserInfo currentUser, @RequestBody CreateOrderForm form) {

        OrderSubmitRequest request = new OrderSubmitRequest();
        request.setInsuranceRecordId(form.getInsuranceRecordId());
        request.setUserId(currentUser.getCurrentUserId());
        request.setPrescriptionStatus(HmcPrescriptionStatusEnum.NOT_PRESCRIPTION);
        request.setHmcOrderStatus(HmcOrderStatusEnum.UN_PAY);
        request.setPaymentStatusEnum(HmcPaymentStatusEnum.UN_PAY);
        request.setPaymentMethodEnum(HmcPaymentMethodEnum.INSURANCE_PAY);
        request.setOrderType(HmcOrderTypeEnum.MEDICINE);
        request.setDeliveryType(HmcDeliveryTypeEnum.SELF_PICKUP);
        request.setCreateSource(HmcCreateSourceEnum.HMC_MA);

        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(form.getInsuranceRecordId());
        if (Objects.isNull(insuranceRecordDTO)) {
            log.info("未获取到参保记录，参保记录id：{}", form.getInsuranceRecordId());
            return Result.failed("未获取到参保记录");
        }

        // 1、判断是否还有可拿药次数
        InsuranceFetchPlanDTO latestPlan = fetchPlanApi.getLatestPlan(form.getInsuranceRecordId());
        if (Objects.isNull(latestPlan)) {
            log.info("当前保单已经兑付完，参保记录id：{}", form.getInsuranceRecordId());
            return Result.failed("已经兑付完");
        }

        // 2、判断是否有未支付的订单
        OrderDTO unPayOrder = orderApi.getUnPayOrder(request);

        if (Objects.nonNull(unPayOrder)) {
            log.info("当前存在未支付的订单：{}", unPayOrder);
            return Result.success(PojoUtils.map(unPayOrder, OrderVO.class));
        }

        // 3、创建订单
        OrderDTO orderDTO = orderApi.createOrder(request);
        log.info("创建订单完成：{}", orderDTO);

        return Result.success(PojoUtils.map(orderDTO, OrderVO.class));
    }

    /**
     * 确认订单
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "确认订单")
    @PostMapping("/confirm_order")
    @Log(title = "确认订单", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> confirmOrder(@CurrentUser CurrentUserInfo currentUser, @RequestBody ConfirmOrderForm form) {

        ConfirmOrderRequest request = new ConfirmOrderRequest();
        request.setOrderId(form.getOrderId());
        request.setUserId(currentUser.getCurrentUserId());

        OrderDTO orderDTO = orderApi.queryById(form.getOrderId());
        if (HmcPrescriptionStatusEnum.NOT_PRESCRIPTION.getCode().equals(orderDTO.getPrescriptionStatus())) {
            return Result.failed("订单未开方，请先去开方");
        }

        return Result.success(orderApi.confirmOrder(request));
    }

    /**
     * 订单列表
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "订单列表")
    @PostMapping("/page_list")
    @Log(title = "订单列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<OrderPageVO>> pageList(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryOrderPageForm form) {

        log.info("开始查询订单列表，参数：{}", JSONUtil.toJsonStr(form));
        OrderPageRequest request = PojoUtils.map(form, OrderPageRequest.class);
        request.setOrderUser(currentUser.getCurrentUserId());

        Page<OrderDTO> dtoPage = orderApi.pageList(request);
        Page<OrderPageVO> voPage = PojoUtils.map(dtoPage, OrderPageVO.class);
        if (CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(voPage);
        }

        List<Long> insuranceRecordIdList = dtoPage.getRecords().stream().map(OrderDTO::getInsuranceRecordId).collect(Collectors.toList());

        // 获取分组拿药计划数量
        List<InsuranceFetchPlanGroupDTO> fetchPlanGroupList = fetchPlanDetailApi.groupByInsuranceRecordId(insuranceRecordIdList);

        Map<Long, InsuranceFetchPlanGroupDTO> fetchPlanGroupMap = fetchPlanGroupList.stream().collect(Collectors.toMap(InsuranceFetchPlanGroupDTO::getInsuranceRecordId, o -> o, (k1, k2) -> k1));

        voPage.getRecords().stream().forEach(item -> item.setGoodsCount(Optional.ofNullable(fetchPlanGroupMap.get(item.getInsuranceRecordId())).map(InsuranceFetchPlanGroupDTO::getGoodsCount).orElse(null)));

        log.info("查询订单列表完成，返回：{}", JSONUtil.toJsonStr(voPage));
        return Result.success(voPage);
    }

    /**
     * 订单详情
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "订单详情")
    @PostMapping("/detail")
    @Log(title = "订单详情", businessType = BusinessTypeEnum.OTHER)
    public Result<OrderDetailInfoVO> detail(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryOrderDetailForm form) {

        log.info("开始查询订单详情，参数：{}", JSONUtil.toJsonStr(form));

        // 获取订单
        OrderDTO orderDTO = orderApi.queryById(form.getId());
        if (Objects.isNull(orderDTO)) {
            log.error("未获取到订单，Id:{}", form.getId());
            return Result.failed("未获取到订单");
        }

        // 获取订单详情
        List<OrderDetailDTO> orderDetailList = orderDetailApi.listByOrderId(orderDTO.getId());

        List<Long> goodsIdList = orderDetailList.stream().map(OrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIdList);
        Map<Long, GoodsDTO> goodsMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, o -> o, (k1, k2) -> k1));


        // 获取参保记录
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getById(orderDTO.getInsuranceRecordId());
        OrderVO orderVO = PojoUtils.map(orderDTO, OrderVO.class);
        InsuranceRecordVO insuranceRecordVO = PojoUtils.map(insuranceRecordDTO, InsuranceRecordVO.class);

        // 处理被保人手机号
        insuranceRecordVO.setIssuePhone(StrUtil.hide(insuranceRecordVO.getIssuePhone(), 3, 9));

        List<OrderDetailVO> orderDetailVOList = PojoUtils.map(orderDetailList, OrderDetailVO.class);

        orderDetailVOList.forEach(item -> {

            // 商品图片
            String pic = Optional.ofNullable(goodsMap.get(item.getGoodsId())).map(GoodsDTO::getPic).orElse(null);
            item.setPic(fileService.getUrl(pic, FileTypeEnum.GOODS_PICTURE));
        });


        OrderDetailInfoVO detailVO = new OrderDetailInfoVO();
        detailVO.setOrderVO(orderVO);
        detailVO.setOrderDetailVOList(orderDetailVOList);
        detailVO.setInsuranceRecordVO(insuranceRecordVO);

        // 待支付，设置倒计时时间
        if (HmcOrderStatusEnum.UN_PAY.getCode().equals(orderDTO.getOrderStatus())) {
            Date now = new Date();
            Instant instant = orderVO.getOrderTime().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
            ZonedDateTime zdt = localDateTime.plusMinutes(20).atZone(zoneId);

            long time = (Date.from(zdt.toInstant()).getTime() - now.getTime()) / 1000;

            // 倒计时剩余时间
            detailVO.setCountDownTime(time);

        }

        // 未开方，设置问诊地址
        if (HmcPrescriptionStatusEnum.NOT_PRESCRIPTION.getCode().equals(orderDTO.getPrescriptionStatus())) {
            Long insuranceCompanyId = orderDTO.getInsuranceCompanyId();
            InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyApi.queryById(insuranceCompanyId);
            // 设置问诊地址
            detailVO.setInternetConsultationUrl(insuranceCompanyDTO.getInternetConsultationUrl());
        }

        // 获取收货人
        OrderRelateUserDTO receiverDTO = orderRelateUserApi.queryByOrderIdAndRelateType(form.getId(), HmcOrderRelateUserTypeEnum.RECEIVER);
        OrderRelateUserVO receiverVO = PojoUtils.map(receiverDTO, OrderRelateUserVO.class);
        receiverVO.setUserTel(StrUtil.hide(receiverVO.getUserTel(), 3, 9));
        detailVO.setReceiveVO(receiverVO);


        log.info("查询订单详情完成，返回：{}", JSONUtil.toJsonStr(detailVO));
        return Result.success(detailVO);
    }


    /**
     * 处方详情
     *
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "处方详情")
    @PostMapping("/prescription_detail")
    @Log(title = "订单详情", businessType = BusinessTypeEnum.OTHER)
    public Result<OrderPrescriptionDetailVO> prescriptionDetail(@CurrentUser CurrentUserInfo currentUser, @RequestBody QueryOrderPrescriptionForm form) {

        log.info("开始查询处方详情，参数：{}", JSONUtil.toJsonStr(form));

        // 获取处方详情
        OrderPrescriptionDTO prescriptionDTO = prescriptionApi.getByOrderId(form.getId());

        // 获取商品信息
        List<OrderPrescriptionGoodsDTO> goodsDTOList = prescriptionGoodsApi.getByOrderId(form.getId());

        OrderPrescriptionVO prescriptionVO = PojoUtils.map(prescriptionDTO, OrderPrescriptionVO.class);
        List<OrderPrescriptionGoodsVO> goodsVO = PojoUtils.map(goodsDTOList, OrderPrescriptionGoodsVO.class);
        OrderPrescriptionDetailVO detailVO = new OrderPrescriptionDetailVO();
        detailVO.setPrescription(prescriptionVO);
        detailVO.setGoodsList(goodsVO);

        log.info("查询处方详情完成，返回：{}", JSONUtil.toJsonStr(detailVO));
        return Result.success(detailVO);
    }

    /**
     * 理赔资料详情
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "理赔资料详情")
    @PostMapping("/getClaimInformation")
    @Log(title = "理赔资料详情", businessType = BusinessTypeEnum.OTHER)
    public Result<OrderClaimInformationVO> getClaimInformation(@RequestBody QueryClaimInformationForm form) {
        log.info("开始查询理赔资料详情，参数：{}", JSONUtil.toJsonStr(form));
        OrderClaimInformationDTO claimInformation = orderApi.getClaimInformation(form.getId());
        OrderClaimInformationVO claimInformationVO = PojoUtils.map(claimInformation, OrderClaimInformationVO.class);
        log.info("查询理赔资料详情完成，返回：{}", JSONUtil.toJsonStr(claimInformationVO));
        return Result.success(claimInformationVO);
    }

    /**
     * 提交理赔资料
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "提交理赔资料")
    @PostMapping("/submitClaimInformation")
    @Log(title = "理赔资料详情", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> submitClaimInformation(@RequestBody SubmitClaimInformationForm form) {
        log.info("开始提交理赔资料，参数：{}", JSONUtil.toJsonStr(form));
        SaveClaimInformationRequest request = PojoUtils.map(form, SaveClaimInformationRequest.class);
        boolean result = orderApi.submitClaimInformation(request);
        log.info("提交理赔资料完成，返回：{}", result);
        return Result.success(result);
    }

    /**
     * 设为已拿
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "设为已拿")
    @PostMapping("/confirmTook")
    @Log(title = "设为已拿", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> confirmTook(@RequestBody ConfirmTookForm form) {
        log.info("设为已拿，参数：{}", JSONUtil.toJsonStr(form));
        ConfirmTookRequest request = PojoUtils.map(form, ConfirmTookRequest.class);
        request.setOpUserId(form.getCurrentUserId());
        request.setOpTime(DateUtil.date());
        boolean result = orderApi.confirmTook(request);
        log.info("设为已拿，返回：{}", result);
        return Result.success(result);
    }


}
