package com.yiling.hmc.order.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsSignatureEnum;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.framework.common.util.BarCodeUtil;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.hmc.config.TkConfigProperties;
import com.yiling.hmc.config.WxTemplateConfig;
import com.yiling.hmc.control.dto.GoodsPurchaseControlDTO;
import com.yiling.hmc.control.service.GoodsPurchaseControlService;
import com.yiling.hmc.insurance.bo.DrugDto;
import com.yiling.hmc.insurance.bo.InsuranceTrialBO;
import com.yiling.hmc.insurance.bo.OrderDetail;
import com.yiling.hmc.insurance.bo.SyncOrderStatusBO;
import com.yiling.hmc.insurance.bo.TkRecipelInfoBO;
import com.yiling.hmc.insurance.bo.TkResult;
import com.yiling.hmc.insurance.bo.TrialResult;
import com.yiling.hmc.insurance.dto.request.ConfirmTookRequest;
import com.yiling.hmc.insurance.dto.request.SaveClaimInformationRequest;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.service.InsuranceCompanyService;
import com.yiling.hmc.insurance.service.InsuranceService;
import com.yiling.hmc.order.bo.OrderBO;
import com.yiling.hmc.order.context.CreateOrderContext;
import com.yiling.hmc.order.dao.OrderMapper;
import com.yiling.hmc.order.dto.OrderClaimInformationDTO;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.OrderDetailDTO;
import com.yiling.hmc.order.dto.OrderPrescriptionDTO;
import com.yiling.hmc.order.dto.request.OrderDeliverRequest;
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.dto.request.OrderPrescriptionSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceiptsSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceivedRequest;
import com.yiling.hmc.order.dto.request.QueryCashPageRequest;
import com.yiling.hmc.order.dto.request.SyncOrderPageRequest;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.entity.OrderDetailControlDO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.entity.OrderPrescriptionDO;
import com.yiling.hmc.order.entity.OrderPrescriptionGoodsDO;
import com.yiling.hmc.order.enums.HmcCreateSourceEnum;
import com.yiling.hmc.order.enums.HmcDeliverTypeEnum;
import com.yiling.hmc.order.enums.HmcDeliveryTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.order.enums.HmcOrderOperateTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.order.enums.HmcPrescriptionStatusEnum;
import com.yiling.hmc.order.enums.HmcSynchronousTypeEnum;
import com.yiling.hmc.order.service.OrderDetailControlService;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderOperateService;
import com.yiling.hmc.order.service.OrderPrescriptionGoodsService;
import com.yiling.hmc.order.service.OrderPrescriptionService;
import com.yiling.hmc.order.service.OrderRelateUserService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.hmc.order.util.TkUtil;
import com.yiling.hmc.settlement.enums.InsuranceSettlementStatusEnum;
import com.yiling.hmc.settlement.enums.TerminalSettlementStatusEnum;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.MiniProgram;
import com.yiling.hmc.wechat.dto.WxMsgDTO;
import com.yiling.hmc.wechat.dto.WxMssData;
import com.yiling.hmc.wechat.dto.request.ConfirmOrderRequest;
import com.yiling.hmc.wechat.dto.request.InsuranceFetchPlanPageRequest;
import com.yiling.hmc.wechat.dto.request.OrderNotifyRequest;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;
import com.yiling.hmc.wechat.dto.request.SaveOrderRelateUserRequest;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDO;
import com.yiling.hmc.wechat.enums.HmcInsuranceBillTypeEnum;
import com.yiling.hmc.wechat.enums.InsuranceErrorCode;
import com.yiling.hmc.wechat.enums.InsuranceFetchStatusEnum;
import com.yiling.hmc.wechat.enums.SmsMedicineReminderEnum;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanDetailService;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanService;
import com.yiling.hmc.wechat.service.InsuranceRecordService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, OrderDO> implements OrderService {

    @DubboReference
    protected NoApi noApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    GzhUserApi gzhUserApi;

    @DubboReference
    SmsApi smsApi;

    private final FileService fileService;

    private final InsuranceService insuranceService;

    private final InsuranceCompanyService insuranceCompanyService;

    private final OrderDetailService orderDetailService;

    private final InsuranceRecordService insuranceRecordService;

    private final InsuranceFetchPlanService insuranceFetchPlanService;

    private final InsuranceFetchPlanDetailService insuranceFetchPlanDetailService;

    private final OrderPrescriptionService prescriptionService;

    private final OrderPrescriptionGoodsService prescriptionGoodsService;

    private final OrderOperateService orderOperateService;

    private final OrderRelateUserService orderRelateUserService;

    private final GoodsPurchaseControlService goodsPurchaseControlService;

    private final OrderDetailControlService orderDetailControlService;

    private final TkConfigProperties tkConfigProperties;

    @Lazy
    @Autowired
    OrderServiceImpl _this;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxService;

    /**
     * 小程序服务类
     */
    @Autowired
    WxMaService wxMaService;

    @Autowired
    WxTemplateConfig templateConfig;

    @Override
    public OrderDO queryByOrderNo(String orderNo) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderNo, orderNo);
        wrapper.lambda().last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public Page<OrderDO> pageList(OrderPageRequest request) {
        if (null != request.getStartTime()) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (null != request.getStopTime()) {
            request.setStopTime(DateUtil.endOfDay(request.getStopTime()));
        }
        return this.getBaseMapper().pageListByCondition(new Page<>(request.getCurrent(), request.getSize()), request);
    }

    @Override
    public Page<OrderDO> syncPageList(SyncOrderPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        if (null != request.getInsuranceCompanyId() && 0L != request.getInsuranceCompanyId()) {
            wrapper.lambda().eq(OrderDO::getInsuranceCompanyId, request.getInsuranceCompanyId());
        }
        if (null != request.getOrderStatus() && 0 != request.getOrderStatus()) {
            wrapper.lambda().eq(OrderDO::getOrderStatus, request.getOrderStatus());
        }
        if (null != request.getPrescriptionStatus() && 0 != request.getPrescriptionStatus()) {
            wrapper.lambda().eq(OrderDO::getPrescriptionStatus, request.getPrescriptionStatus());
        }
        if (CollUtil.isNotEmpty(request.getSynchronousTypeList())) {
            wrapper.lambda().in(OrderDO::getSynchronousType, request.getSynchronousTypeList());
        }
        wrapper.lambda().orderByAsc(OrderDO::getFinishTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @GlobalTransactional
    public MqMessageBO receiveOrder(OrderReceivedRequest request) {
        OrderDO orderDO = this.getById(request.getOrderId());
        // 1.校验用户自提提交的身份证后6位是否正确(已经校验过)

        // 2.校验订单是否满足自提要求
        checkReceived(orderDO);

        // 3.订单状态改为已完成(一次拿订单所有的商品，不允许部分拿)
        receivedOrder(orderDO, request);

        // 4.将订单完成信息推送给保司
        sendToInsurance(orderDO);

        // 5.新增订单操作数据
        orderOperateService.saveOrderOperate(request.getOrderId(), HmcOrderOperateTypeEnum.SELF_PICKUP, "", request.getOpUserId(), request.getOpTime());

        // 6、发送完成订单mq
        MqMessageBO finishOrderMq = new MqMessageBO(Constants.TOPIC_HMC_FINISH_ORDER, Constants.TAG_HMC_FINISH_ORDER, orderDO.getOrderNo());
        mqMessageSendApi.prepare(finishOrderMq);

        return finishOrderMq;
    }

    @Override
    public boolean received(OrderReceivedRequest request) {
        MqMessageBO messageBO = _this.receiveOrder(request);
        mqMessageSendApi.send(messageBO);
        return Boolean.TRUE;
    }

    /**
     * 更新拿药计划状态
     *
     * @param orderDO
     */
    @Override
    public boolean updateFetchPlanStatus(OrderDO orderDO) {
        InsuranceFetchPlanDTO fetchPlanDTO = insuranceFetchPlanService.getByOrderId(orderDO.getId());
        if (Objects.nonNull(fetchPlanDTO)) {
            log.info("[updateFetchPlanStatus]根据orderId已查询到拿药计划， orderId：{}, fetchPlanDTO:{}", orderDO.getId(), fetchPlanDTO);
            return Boolean.FALSE;
        }

        InsuranceFetchPlanDTO latestPlan = insuranceFetchPlanService.getLatestPlan(orderDO.getInsuranceRecordId());
        if (Objects.isNull(latestPlan)) {
            log.info("[updateFetchPlanStatus]当前无可兑付拿药计划， insuranceRecordId：{}", orderDO.getInsuranceRecordId());
            return Boolean.FALSE;
        }

        latestPlan.setFetchStatus(InsuranceFetchStatusEnum.TOOK.getType());
        latestPlan.setOrderId(orderDO.getId());
        return insuranceFetchPlanService.updateFetchStatus(latestPlan);
    }

    @Override
    public boolean saveOrderReceipts(OrderReceiptsSaveRequest request) {
        OrderDO order = PojoUtils.map(request, OrderDO.class);
        order.setId(request.getOrderId());
        return this.updateById(order);
    }

    @Override
    public boolean saveOrderPrescription(OrderPrescriptionSaveRequest request) {
        OrderDO orderDO = this.getById(request.getOrderId());
        if (HmcSynchronousTypeEnum.getByCode(orderDO.getSynchronousType()) == HmcSynchronousTypeEnum.SYNCED) {
            throw new BusinessException(HmcOrderErrorCode.INSURANCE_ORDER_HAVE_SYNC_PRESCRIPTION);
        }
        // 如果处方为空则新增，否则修改
        OrderPrescriptionDTO orderPrescriptionDTO = prescriptionService.getByOrderId(request.getOrderId());
        OrderPrescriptionDO orderPrescriptionDO = PojoUtils.map(request, OrderPrescriptionDO.class);
        if (null != orderPrescriptionDTO) {
            orderPrescriptionDO.setId(orderPrescriptionDTO.getId());
            return prescriptionService.updateById(orderPrescriptionDO);
        } else {
            InsuranceRecordDTO insuranceRecordDTO = insuranceRecordService.getById(orderDO.getInsuranceRecordId());

            orderPrescriptionDO.setReceiptDate(new Date());
            orderPrescriptionDO.setPatientsName(insuranceRecordDTO.getIssueName());
            String issueCredentialNo = insuranceRecordDTO.getIssueCredentialNo();
            int gender = IdcardUtil.getGenderByIdCard(issueCredentialNo);
            orderPrescriptionDO.setPatientsGender(0 == gender ? "女" : "男");
            int age = IdcardUtil.getAgeByIdCard(issueCredentialNo);
            orderPrescriptionDO.setPatientsAge(age);
            prescriptionService.save(orderPrescriptionDO);

            OrderDO order = new OrderDO();
            order.setId(request.getOrderId());
            order.setPrescriptionStatus(HmcPrescriptionStatusEnum.HAVE_PRESCRIPTION.getCode());
            order.setOpUserId(request.getOpUserId());
            return this.updateById(order);
        }
    }

    /**
     * 确认自提接口（同步订单状态）-同步给保司
     *
     * @param orderDO 订单信息
     */
    private void sendToInsurance(OrderDO orderDO) {
        // todo 确认自提接口（同步订单状态）-同步给保司
    }

    /**
     * 订单状态改为已完成(一次拿订单所有的商品，不允许部分拿)
     *
     * @param orderDO 订单信息
     * @param request 请求参数
     */
    private void receivedOrder(OrderDO orderDO, OrderReceivedRequest request) {
        OrderDO order = new OrderDO();
        order.setId(orderDO.getId());
        order.setOrderStatus(HmcOrderStatusEnum.FINISHED.getCode());
        order.setFinishTime(DateUtil.date());
        order.setOpUserId(request.getOpUserId());
        order.setOpTime(request.getOpTime());
        this.updateById(order);
    }

    /**
     * 校验订单是否满足自提要求
     *
     * @param orderDO 订单信息
     */
    private void checkReceived(OrderDO orderDO) {
        if (null == orderDO) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS);
        }
        if (HmcOrderStatusEnum.UN_PICK_UP != HmcOrderStatusEnum.getByCode(orderDO.getOrderStatus())) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_STATUS_ERROR);
        }
        if (HmcDeliverTypeEnum.SELF != HmcDeliverTypeEnum.getByCode(orderDO.getDeliverType())) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_DELIVER_TYPE_ERROR);
        }
    }

    @Override
    public boolean deliver(OrderDeliverRequest request) {
        // todo 一期没有快递，所以没有发货
        OrderDO orderDO = this.getById(request.getOrderId());
        // 1.校验订单是否满足发货要求
        checkDeliver(orderDO);

        // 2.校验发货地址是否存在

        // 3.更改订单状态待收货(一次发货订单所有的商品，不允许部分发)

        // 4.新增发货信息

        return false;
    }

    /**
     * 校验订单是否满足发货要求
     *
     * @param orderDO 订单信息
     */
    private void checkDeliver(OrderDO orderDO) {
        if (null == orderDO) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS);
        }
        if (HmcOrderStatusEnum.UN_DELIVERED == HmcOrderStatusEnum.getByCode(orderDO.getOrderStatus())) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_STATUS_ERROR);
        }
        if (HmcDeliverTypeEnum.DELIVER == HmcDeliverTypeEnum.getByCode(orderDO.getDeliverType())) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_DELIVER_TYPE_ERROR);
        }
    }

    @GlobalTransactional
    public void create(OrderSubmitRequest request, CreateOrderContext orderContext) throws ParseException {

        // 1、获取参保记录
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordService.getById(request.getInsuranceRecordId());
        if (Objects.isNull(insuranceRecordDTO)) {
            log.info("参保记录为空，参数:{}", request);
            throw new BusinessException(HmcOrderErrorCode.INSURANCE_NO_NOT_EXISTS);
        }

        // 2、获取拿药计划详情
        List<InsuranceFetchPlanDetailDTO> fetchPlanDetailDTOList = insuranceFetchPlanDetailService.getByInsuranceRecordId(request.getInsuranceRecordId());

        List<Long> goodsIdList = fetchPlanDetailDTOList.stream().map(InsuranceFetchPlanDetailDTO::getGoodsId).collect(Collectors.toList());

        // 3、校验库存
        List<GoodsSkuDTO> goodsSkuList = goodsHmcApi.getGoodsSkuByGids(goodsIdList);
        Map<Long, GoodsSkuDTO> goodsSkuMap = goodsSkuList.stream().collect(Collectors.toMap(GoodsSkuDTO::getGoodsId, o -> o, (k1, k2) -> k1));
        List<AddOrSubtractQtyRequest> qtyRequests = fetchPlanDetailDTOList.stream().map(item -> {
            Long qty = Optional.ofNullable(goodsSkuMap.get(item.getGoodsId())).map(GoodsSkuDTO::getQty).orElse(0L);
            if (item.getPerMonthCount().compareTo(qty) > 0) {
                throw new BusinessException(HmcOrderErrorCode.STOCK_NOT_ENOUGH_ERROR);
            }

            // 构建冻结库存对象
            AddOrSubtractQtyRequest qtyRequest = new AddOrSubtractQtyRequest();
            qtyRequest.setOpUserId(request.getUserId());
            qtyRequest.setOpTime(request.getOpTime());
            qtyRequest.setSkuId(Optional.ofNullable(goodsSkuMap.get(item.getGoodsId())).map(GoodsSkuDTO::getId).orElse(null));
            qtyRequest.setInventoryId(Optional.ofNullable(goodsSkuMap.get(item.getGoodsId())).map(GoodsSkuDTO::getInventoryId).orElse(null));
            qtyRequest.setFrozenQty(item.getPerMonthCount());

            return qtyRequest;

        }).collect(Collectors.toList());

        // 4、构建订单明细
        List<OrderDetailDO> orderDetailDOList = buildOrderDetail(request, fetchPlanDetailDTOList);

        // 5、构建订单信息
        OrderDO order = buildOrder(request, insuranceRecordDTO, orderDetailDOList);

        // 5、保存订单
        boolean saveOrderResult = this.save(order);
        if (!saveOrderResult) {
            throw new BusinessException(HmcOrderErrorCode.SAVE_ORDER_ERROR);
        }

        orderDetailDOList.stream().forEach(item -> item.setOrderId(order.getId()));

        // 6、保存订单明细
        boolean saveDetailResult = orderDetailService.saveBatch(orderDetailDOList);
        if (!saveDetailResult) {
            throw new BusinessException(HmcOrderErrorCode.SAVE_ORDER_DETAIL_ERROR);
        }

        // 7、冻结库存
        qtyRequests.forEach(item -> item.setOrderNo(order.getOrderNo()));
        goodsHmcApi.batchAddHmcFrozenQty(qtyRequests);

        // 8、保存处方
        if (HmcPrescriptionStatusEnum.HAVE_PRESCRIPTION.equals(request.getPrescriptionStatus())) {
            OrderPrescriptionDO prescription = buildPrescription(order.getId(), request);
            boolean savePrescription = prescriptionService.save(prescription);
            if (!savePrescription) {
                throw new BusinessException(HmcOrderErrorCode.SAVE_ORDER_PRESCRIPTION);
            }
        }

        // 9、订单状态已完成 & 自提 -> 保存订单创建人、发货人
        if (request.getHmcOrderStatus().equals(HmcOrderStatusEnum.FINISHED) && request.getDeliveryType().equals(HmcDeliveryTypeEnum.SELF_PICKUP)) {

            orderOperateService.saveOrderOperate(order.getId(), HmcOrderOperateTypeEnum.CREATE_ORDER, "", request.getOpUserId(), request.getOpTime());

            orderOperateService.saveOrderOperate(order.getId(), HmcOrderOperateTypeEnum.DELIVER, "", request.getOpUserId(), request.getOpTime());

            orderOperateService.saveOrderOperate(order.getId(), HmcOrderOperateTypeEnum.SELF_PICKUP, "", request.getOpUserId(), request.getOpTime());

        }

        // 10、后台兑付 -> 保存订单收货人信息
        if (HmcCreateSourceEnum.ADMIN_HMC.equals(request.getCreateSource())) {
            SaveOrderRelateUserRequest userRequest = PojoUtils.map(request, SaveOrderRelateUserRequest.class);
            userRequest.setOrderId(order.getId());
            userRequest.setUserTypeEnum(HmcOrderRelateUserTypeEnum.RECEIVER);
            userRequest.setUserName(insuranceRecordDTO.getHolderName());
            userRequest.setUserTel(insuranceRecordDTO.getHolderPhone());
            orderRelateUserService.add(userRequest);
        }

        // 11、预定单 -> 准备预定单mq
        if (request.getHmcOrderStatus().equals(HmcOrderStatusEnum.UN_PAY)) {
            MqMessageBO createOrderMq = new MqMessageBO(Constants.TOPIC_HMC_CREATE_ORDER, Constants.TAG_HMC_CREATE_ORDER, order.getOrderNo(), MqDelayLevel.TWENTY_MINUTES);
            mqMessageSendApi.prepare(createOrderMq);
            orderContext.getMqMessageList().add(createOrderMq);
        }

        // 12、订单已完成 -> 准备完成订单mq
        if (request.getHmcOrderStatus().equals(HmcOrderStatusEnum.FINISHED)) {
            MqMessageBO finishOrderMq = new MqMessageBO(Constants.TOPIC_HMC_FINISH_ORDER, Constants.TAG_HMC_FINISH_ORDER, order.getOrderNo());
            mqMessageSendApi.prepare(finishOrderMq);
            orderContext.getMqMessageList().add(finishOrderMq);
        }

        // 13、保存商品管控信息
        List<OrderDetailControlDO> detailControlList = buildOrderDetailControl(orderDetailDOList);
        if (CollUtil.isNotEmpty(detailControlList)) {
            log.info("保存订单明细管控记录：{}", detailControlList);
            orderDetailControlService.saveBatch(detailControlList);
        }

        // 14、运营后台创建订单 && 泰康保险 -> 调用泰康接口，理赔试算
        InsuranceCompanyDO insuranceCompany = insuranceCompanyService.getById(insuranceRecordDTO.getInsuranceCompanyId());
        if (HmcCreateSourceEnum.ADMIN_HMC.equals(request.getCreateSource()) && "91420102MA4KLC8M0A".equals(insuranceCompany.getInsuranceNo())) {
            orderSettleTrail(insuranceRecordDTO, order, orderDetailDOList);
        }

        OrderDTO orderDTO = PojoUtils.map(order, OrderDTO.class);

        // 15、判断是否维护过身份证手写签名
        if (StrUtil.isNotBlank(insuranceRecordDTO.getIdCardFront())) {
            orderDTO.setHasMaintainFlag(true);
        }
        orderContext.setOrderDTO(orderDTO);
    }

    /**
     * 调用泰康接口，理赔试算
     */
    private void orderSettleTrail(InsuranceRecordDTO insuranceRecordDTO, OrderDO order, List<OrderDetailDO> orderDetailDOList) throws ParseException {
        log.info(">>>>>准备调用泰康试算接口");
        InsuranceDO insuranceDO = insuranceService.getById(insuranceRecordDTO.getInsuranceId());
        InsuranceTrialBO trialBO = InsuranceTrialBO.builder().build();
        trialBO.setHasPayInfo(false);
        trialBO.setHasRecipel(true);
        trialBO.setSourceChannelCode(tkConfigProperties.getSourceChannelCode());
        trialBO.setSourceChannelName(tkConfigProperties.getSourceChannelName());
        if (HmcInsuranceBillTypeEnum.YEAR.getType().equals(insuranceRecordDTO.getBillType())) {
            trialBO.setProductCode(insuranceDO.getYearIdentification());
        }
        if (HmcInsuranceBillTypeEnum.QUARTER.getType().equals(insuranceRecordDTO.getBillType())) {
            trialBO.setProductCode(insuranceDO.getQuarterIdentification());
        }
        trialBO.setPolicyNo(insuranceRecordDTO.getPolicyNo());
        trialBO.setChannelMainId(order.getOrderNo());

        trialBO.setApplyUserName(insuranceRecordDTO.getIssueName());
        trialBO.setApplyUserPhone(insuranceRecordDTO.getIssuePhone());
        trialBO.setApplyUserCid(insuranceRecordDTO.getIssueCredentialNo());
        trialBO.setApplyUserCidType(1);
        trialBO.setPatientName(insuranceRecordDTO.getIssueName());
        String issueCredentialNo = insuranceRecordDTO.getIssueCredentialNo();
        String birth = IdcardUtil.getBirth(issueCredentialNo);
        trialBO.setPatientBirth(DateUtils.parseDate(birth, DatePattern.PURE_DATE_PATTERN));
        trialBO.setPatientCid(insuranceRecordDTO.getIssueCredentialNo());
        trialBO.setPatientCidType(1);
        trialBO.setPatientGender(IdcardUtil.getGenderByIdCard(issueCredentialNo) == 1 ? "1" : "2");
        trialBO.setPatientPhone(insuranceRecordDTO.getIssuePhone());
        trialBO.setPharmacyType(4);
        trialBO.setConsultationType(2);
        trialBO.setStatus(0);
        trialBO.setMarketPrices(order.getInsuranceSettleAmount().toPlainString());
        trialBO.setApplyTime(DateUtil.now());

        List<OrderDetail> orderList = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setChannelOrderId(order.getOrderNo());
        orderDetail.setChannelCode(tkConfigProperties.getChannelCode());
        orderDetail.setIsTraditional(0);
        orderDetail.setReceiveWay(0);
        orderDetail.setDeliverway(0);
        orderDetail.setDrugstoreName(order.getEname());
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(order.getEid());
        orderDetail.setDrugstoreAddress(Optional.ofNullable(enterpriseDTO).map(EnterpriseDTO::getAddress).orElse(null));
        orderDetail.setOrderStatus(0); //0新订单
        orderDetail.setOrderTotalPrices(order.getInsuranceSettleAmount().toPlainString());

        // 构造药品信息
        List<DrugDto> drugList = orderDetailDOList.stream().map(item -> {
            DrugDto drugDto = new DrugDto();
            drugDto.setDrugCode(item.getInsuranceGoodsCode());
            drugDto.setChannelDrugCode(item.getSellSpecificationsId().toString());
            drugDto.setDrugCategory("1");
            drugDto.setDrugGoodName(item.getGoodsName());
            drugDto.setDrugGenericName(item.getGoodsName());
            drugDto.setSpecification(item.getGoodsSpecifications());
            drugDto.setNum(item.getGoodsQuantity().intValue());
            drugDto.setMarketPrice(item.getSettlePrice().toPlainString());

            return drugDto;
        }).collect(Collectors.toList());

        orderDetail.setDrugList(drugList);
        orderList.add(orderDetail);

        // 构造处方对象
        TkRecipelInfoBO recipelInfo = new TkRecipelInfoBO();
        recipelInfo.setThirdId(order.getOrderNo());
        recipelInfo.setPrescribingTime(DateUtil.date());
        recipelInfo.setDiseaseCodeTable(1);
        recipelInfo.setDiseaseCode("I25.104");
        recipelInfo.setDiseaseName("冠心病心律失常型");
        recipelInfo.setRecipelTotalPrices(String.valueOf(order.getInsuranceSettleAmount()));

        trialBO.setRecipelInfo(recipelInfo);
        trialBO.setOrderList(orderList);

        log.info(">>>>>泰康试算对象原始参数:{}", JSONUtil.toJsonStr(trialBO));
        String params = TkUtil.encrypt(JSONUtil.toJsonStr(trialBO), tkConfigProperties.getParamsKey());
        long currentSeconds = DateUtil.currentSeconds();

        Map<String, String> signMap = new HashMap<>();
        signMap.put("appId", tkConfigProperties.getAppId());
        signMap.put("params", params);
        signMap.put("timestamp", String.valueOf(currentSeconds));
        signMap.put("cipher", "123456");
        String sign = TkUtil.generateSign(signMap, tkConfigProperties.getSecretKey());

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("timestamp", currentSeconds);
        paramMap.put("sign", sign);
        paramMap.put("appId", tkConfigProperties.getAppId());
        paramMap.put("secretKey", tkConfigProperties.getSecretKey());
        paramMap.put("params", params);

        String url = tkConfigProperties.getTkServerUrl() + "/pbm/adjustment/trialCalculate";
        log.info(">>>>>调用泰康试算接口路径：{}, ", url);
        log.info(">>>>>调用泰康试算接口参数：{}", JSONUtil.toJsonStr(paramMap));
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(paramMap));
        log.info(">>>>>调用泰康试算接口返回值：{}", result);

        TkResult<TrialResult> trialResult = JSONUtil.toBean(result, new TypeReference<TkResult<TrialResult>>() {
        }, false);

        // 如果结果不是成功状态，则异常提醒
        if (!"success".equals(trialResult.getResult()) || !trialResult.getData().getClaimResultCode().equals(0)) {
            throw new BusinessException(InsuranceErrorCode.CALL_TK_TRIAL_ERROR, trialResult.getData().getClaimResultMsg());
        }

        // 试算金额
        BigDecimal claimAmount = trialResult.getData().getClaimAmount();

        // 更新试算金额、订单状态->待自提
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, order.getId());

        OrderDO target = new OrderDO();
        target.setInsuranceSettleAmountTrial(claimAmount);
        target.setOrderStatus(HmcOrderStatusEnum.UN_PICK_UP.getCode());
        target.setPaymentStatus(HmcPaymentStatusEnum.PAYED.getCode());

        this.update(target, wrapper);


    }

    /**
     * 构建药品管控数据
     *
     * @param orderDetailDOList
     * @return
     */
    private List<OrderDetailControlDO> buildOrderDetailControl(List<OrderDetailDO> orderDetailDOList) {
        List<Long> sellSpecificationsIdList = orderDetailDOList.stream().map(OrderDetailDO::getSellSpecificationsId).collect(Collectors.toList());
        Map<Long, OrderDetailDO> orderSellSpecificationMap = orderDetailDOList.stream().collect(Collectors.toMap(OrderDetailDO::getSellSpecificationsId, o -> o, (k1, k2) -> k1));
        List<GoodsPurchaseControlDTO> goodsPurchaseControlDTOS = goodsPurchaseControlService.queryControlListBySpecificationsId(sellSpecificationsIdList);
        if (CollUtil.isEmpty(goodsPurchaseControlDTOS)) {
            log.info("未查询到商品管控信息");
            return Lists.newArrayList();
        }

        List<OrderDetailControlDO> detailControlDOList = goodsPurchaseControlDTOS.stream().map(item -> {
            OrderDetailDO orderDetailDO = orderSellSpecificationMap.get(item.getSellSpecificationsId());
            OrderDetailControlDO detailControlDO = PojoUtils.map(orderDetailDO, OrderDetailControlDO.class);
            detailControlDO.setEid(item.getSellerEid());
            detailControlDO.setControlId(item.getGoodsControlId());
            return detailControlDO;
        }).collect(Collectors.toList());
        return detailControlDOList;
    }

    @Override
    public OrderDTO createOrder(OrderSubmitRequest request) {

        CreateOrderContext orderContext = new CreateOrderContext();

        try {
            _this.create(request, orderContext);
        } catch (ParseException e) {
            log.error("创建HMC订单报错:{}", ExceptionUtils.getStackTrace(e), e);
        }

        // 发送延时消息
        orderContext.getMqMessageList().forEach(item -> mqMessageSendApi.send(item));

        return orderContext.getOrderDTO();

    }

    /**
     * 构建处方对象
     *
     * @param orderId
     * @param request
     * @return
     */
    private OrderPrescriptionDO buildPrescription(Long orderId, OrderSubmitRequest request) {
        OrderPrescriptionDO prescription = new OrderPrescriptionDO();
        prescription.setOrderId(orderId);
        prescription.setDoctor(request.getDoctor());
        prescription.setReceiptDate(request.getReceiptDate());
        prescription.setInterrogationResult(request.getInterrogationResult());
        prescription.setPrescriptionSnapshotUrl(request.getPrescriptionSnapshotUrl());
        prescription.setRemark(request.getRemark());
        prescription.setOpUserId(request.getOpUserId());
        prescription.setOpTime(request.getOpTime());
        return prescription;
    }

    /**
     * 构建订单明细信息
     *
     * @param request
     * @param fetchPlanDetailDTOList
     * @return
     */
    private List<OrderDetailDO> buildOrderDetail(OrderSubmitRequest request, List<InsuranceFetchPlanDetailDTO> fetchPlanDetailDTOList) {
        List<OrderDetailDO> result = Lists.newArrayList();

        fetchPlanDetailDTOList.forEach(item -> {
            OrderDetailDO orderDetail = new OrderDetailDO();
            orderDetail.setInsuranceRecordId(request.getInsuranceRecordId());
            orderDetail.setHmcGoodsId(item.getHmcGoodsId());
            orderDetail.setGoodsId(item.getGoodsId());
            orderDetail.setGoodsName(item.getGoodsName());
            orderDetail.setSellSpecificationsId(item.getSellSpecificationsId());
            orderDetail.setInsurancePrice(item.getInsurancePrice());
            orderDetail.setSettlePrice(item.getSettlePrice());
            orderDetail.setSettleAmount(item.getSettlePrice().multiply(BigDecimal.valueOf(item.getPerMonthCount())));
            orderDetail.setTerminalSettlePrice(item.getTerminalSettlePrice());
            orderDetail.setTerminalSettleAmount(item.getTerminalSettlePrice().multiply(BigDecimal.valueOf(item.getPerMonthCount())));
            orderDetail.setGoodsSpecifications(item.getSpecificInfo());
            orderDetail.setGoodsQuantity(item.getPerMonthCount());
            orderDetail.setGoodsAmount(item.getInsurancePrice().multiply(BigDecimal.valueOf(item.getPerMonthCount())));
            orderDetail.setInsuranceGoodsCode(item.getInsuranceGoodsCode());
            orderDetail.setCreateUser(request.getUserId());
            orderDetail.setCreateTime(request.getOpTime());
            orderDetail.setUpdateTime(request.getOpTime());
            orderDetail.setUpdateUser(request.getUserId());
            result.add(orderDetail);
        });
        return result;
    }

    /**
     * 构建订单信息
     *
     * @param request
     * @param insuranceRecordDTO
     * @param orderDetailDOList
     * @return
     */
    private OrderDO buildOrder(OrderSubmitRequest request, InsuranceRecordDTO insuranceRecordDTO, List<OrderDetailDO> orderDetailDOList) {

        // 获取保险信息
        Long insuranceId = insuranceRecordDTO.getInsuranceId();
        InsuranceDO insuranceDO = insuranceService.getById(insuranceId);

        // 获取公司信息
        Long eId = insuranceRecordDTO.getEid();
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eId);

        OrderDO order = new OrderDO();
        order.setOrderNo(noApi.gen(NoEnum.ORDER_NO));
        order.setInsuranceRecordId(request.getInsuranceRecordId());
        order.setPolicyNo(insuranceRecordDTO.getPolicyNo());
        order.setInsuranceCompanyId(insuranceDO.getInsuranceCompanyId());
        order.setPrescriptionStatus(request.getPrescriptionStatus().getCode());
        order.setOrderStatus(request.getHmcOrderStatus().getCode());

        // 如果订单状态已完成 -> 设置完成时间
        if (request.getHmcOrderStatus().equals(HmcOrderStatusEnum.FINISHED)) {
            order.setFinishTime(request.getOpTime());
        }

        // 终端结算状态 -> 待结算
        order.setTerminalSettleStatus(TerminalSettlementStatusEnum.UN_SETTLEMENT.getCode());

        // 终端结算状态 -> 待结算
        order.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.UN_SETTLEMENT.getCode());

        order.setEid(insuranceRecordDTO.getEid());
        order.setEname(enterpriseDTO.getName());
        order.setPaymentMethod(request.getPaymentMethodEnum().getCode());
        order.setPaymentStatus(request.getPaymentStatusEnum().getCode());
        order.setOrderUser(request.getUserId());
        order.setOrderTime(request.getOpTime());
        order.setOrderType(request.getOrderType().getCode());
        order.setIsInsuranceOrder(1);
        order.setDeliverType(request.getDeliveryType().getCode());
        order.setCreateUser(request.getUserId());
        order.setCreateTime(request.getOpTime());
        order.setUpdateTime(request.getOpTime());
        order.setUpdateUser(request.getUserId());
        order.setCreateSource(request.getCreateSource().getCode());
        if (CollUtil.isNotEmpty(request.getOrderReceipts())) {
            order.setOrderReceipts(request.getOrderReceipts().stream().collect(Collectors.joining(",")));
        }

        BigDecimal settleAmount = orderDetailDOList.stream().map(OrderDetailDO::getSettleAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal terminalSettleAmount = orderDetailDOList.stream().map(OrderDetailDO::getTerminalSettleAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = orderDetailDOList.stream().map(OrderDetailDO::getGoodsAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setInsuranceSettleAmount(settleAmount);
        order.setTerminalSettleAmount(terminalSettleAmount);
        order.setTotalAmount(totalAmount);

        if (Objects.nonNull(request.getPaymentTime())) {
            order.setPaymentTime(request.getPaymentTime());
        }
        return order;
    }

    @Override
    public Boolean hasOrder(Long insuranceRecordId) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getInsuranceRecordId, insuranceRecordId);
        OrderDO order = this.getOne(wrapper, false);
        if (Objects.isNull(order)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public OrderDTO getProcessingOrder(Long currentUserId) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderUser, currentUserId);

        // 已开方
        wrapper.lambda().eq(OrderDO::getPrescriptionStatus, HmcPrescriptionStatusEnum.HAVE_PRESCRIPTION.getCode());

        // 未支付
        wrapper.lambda().eq(OrderDO::getOrderStatus, HmcOrderStatusEnum.UN_PAY.getCode());

        wrapper.last("limit 1");
        OrderDO orderDO = this.getOne(wrapper);
        return PojoUtils.map(orderDO, OrderDTO.class);

    }

    @Override
    public OrderDTO getUnPayOrder(OrderSubmitRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderUser, request.getUserId());
        wrapper.lambda().eq(OrderDO::getInsuranceRecordId, request.getInsuranceRecordId());
        wrapper.lambda().eq(OrderDO::getOrderStatus, HmcOrderStatusEnum.UN_PAY.getCode());
        wrapper.last("limit 1");
        OrderDO orderDO = this.getOne(wrapper);
        return PojoUtils.map(orderDO, OrderDTO.class);
    }

    @Override
    public OrderDTO getUnPickUPOrder(OrderSubmitRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderUser, request.getUserId());
        wrapper.lambda().eq(OrderDO::getInsuranceRecordId, request.getInsuranceRecordId());
        wrapper.lambda().eq(OrderDO::getOrderStatus, HmcOrderStatusEnum.UN_PICK_UP.getCode());
        wrapper.last("limit 1");
        OrderDO orderDO = this.getOne(wrapper);
        return PojoUtils.map(orderDO, OrderDTO.class);
    }

    @Override
    @GlobalTransactional
    public boolean confirmOrder(ConfirmOrderRequest request) {
        // 1、获取订单
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getId, request.getOrderId());
        OrderDO order = this.getOne(wrapper);

        // 2、生成条码
        OrderDO orderDO = new OrderDO();
        byte[] bytes = BarCodeUtil.generateBarCode128(order.getOrderNo(), 10.00, 0.3, false, false);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        try {
            FileInfo fileInfo = fileService.upload(bytes, order.getOrderNo() + Constants.FILE_SUFFIX, FileTypeEnum.BARCODE_PIC, metadata);
            // 设置条形码key
            orderDO.setBarCodeKey(fileInfo.getKey());
        } catch (Exception e) {
            log.error("上传条形码到阿里云出错：{}", e.getMessage(), e);
        }

        // 3、更新订单状态、结算额、结算状态
        orderDO.setPaymentStatus(HmcPaymentStatusEnum.PAYED.getCode());
        orderDO.setOrderStatus(HmcOrderStatusEnum.UN_PICK_UP.getCode());

        if (order.getInsuranceSettleAmount().compareTo(BigDecimal.ZERO) == 0) {
            orderDO.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.PRE_SETTLEMENT.getCode());
        } else {
            orderDO.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.UN_SETTLEMENT.getCode());
        }
        orderDO.setTerminalSettleStatus(InsuranceSettlementStatusEnum.UN_SETTLEMENT.getCode());

        UpdateWrapper<OrderDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(OrderDO::getId, request.getOrderId());

        boolean updateOrderResult = this.update(orderDO, updateWrapper);
        if (!updateOrderResult) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_UPDATE_ERROR);
        }

        // 4、更新拿药计划状态
        InsuranceFetchPlanDTO latestPlan = insuranceFetchPlanService.getLatestPlan(order.getInsuranceRecordId());
        latestPlan.setFetchStatus(InsuranceFetchStatusEnum.APPLY_TAKE.getType());

        boolean updateFetchResult = insuranceFetchPlanService.updateFetchStatus(latestPlan);
        if (!updateFetchResult) {
            throw new BusinessException(HmcOrderErrorCode.FETCH_PLAN_UPDATE_ERROR);
        }

        // 5、调用保司接口同步订单数据
        // todo


        return Boolean.TRUE;
    }

    @Override
    public OrderDTO getByOrderNo(String orderNo) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderNo, orderNo);
        OrderDO order = this.getOne(wrapper);
        return PojoUtils.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO getByOrderId(Long id) {
        OrderDO order = this.getById(id);
        OrderDTO orderDTO = PojoUtils.map(order, OrderDTO.class);
        String url = fileService.getUrl(order.getBarCodeKey(), FileTypeEnum.BARCODE_PIC);
        orderDTO.setBarCodeUrl(url);
        return orderDTO;
    }

    @Override
    @Transactional
    public Long orderNotify(OrderNotifyRequest orderNotifyRequest) {
        // 获取订单
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO::getOrderNo, orderNotifyRequest.getOrderNo());
        wrapper.lambda().last("limit 1");
        OrderDO orderDO = this.getOne(wrapper);

        orderDO.setInsuranceSettleAmount(orderNotifyRequest.getInsuranceSettlementAmount());


        if (BigDecimal.ZERO.compareTo(orderNotifyRequest.getInsuranceSettlementAmount()) == 0) {
            orderDO.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.PRE_SETTLEMENT.getCode());
        } else {
            orderDO.setInsuranceSettleStatus(InsuranceSettlementStatusEnum.UN_SETTLEMENT.getCode());
        }

        UpdateWrapper<OrderDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(OrderDO::getId, orderDO.getId());

        // 1、 更新理赔款结算金额
        this.update(orderDO, updateWrapper);

        // 2、保存处方
        OrderPrescriptionDO prescriptionDO = PojoUtils.map(orderNotifyRequest, OrderPrescriptionDO.class);
        prescriptionService.save(prescriptionDO);

        // 3、保存处方药品
        List<OrderNotifyRequest.MedicineDetail> medicineList = orderNotifyRequest.getMedicineDetailList();
        List<OrderPrescriptionGoodsDO> goodsList = PojoUtils.map(medicineList, OrderPrescriptionGoodsDO.class);
        goodsList.stream().forEach(item -> item.setOrderPrescriptionId(prescriptionDO.getId()));
        prescriptionGoodsService.saveBatch(goodsList);

        return prescriptionDO.getId();
    }

    @Override
    public Page<OrderBO> queryCashPage(QueryCashPageRequest request) {
        LambdaQueryWrapper<OrderDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OrderDO::getInsuranceRecordId, request.getInsuranceRecordId());
        Page<OrderDO> orderDOPage = this.page(request.getPage(), wrapper);
        if (orderDOPage.getTotal() == 0) {
            return request.getPage();
        }
        Page<OrderBO> orderBOPage = PojoUtils.map(orderDOPage, OrderBO.class);
        List<OrderBO> orderBOList = orderBOPage.getRecords();
        List<Long> orderIdList = orderBOList.stream().map(OrderBO::getId).collect(Collectors.toList());
        //查询关联商品
        LambdaQueryWrapper<OrderDetailDO> detailWrapper = Wrappers.lambdaQuery();
        detailWrapper.in(OrderDetailDO::getOrderId, orderIdList);
        List<OrderDetailDO> detailDOS = orderDetailService.list(detailWrapper);
        Map<Long, List<OrderDetailDO>> detailMap = detailDOS.stream().collect(Collectors.groupingBy(OrderDetailDO::getOrderId));
        //查询关联处方
        List<OrderPrescriptionDO> list = prescriptionService.list();
        orderBOList.forEach(orderBO -> {
            List<OrderDetailDO> orderDetailDOS = detailMap.get(orderBO.getId());
            if (CollUtil.isNotEmpty(orderDetailDOS)) {
                List<OrderDetailDTO> goodsList = PojoUtils.map(orderDetailDOS, OrderDetailDTO.class);
                orderBO.setGoodsList(goodsList);
            }
        });
        return orderBOPage;
    }

    @Override
    public boolean sendMedicineRemind() {
        Date tomorrow = DateUtils.addDays(new Date(), 1);
        Date startTime = DateUtil.beginOfDay(tomorrow);
        Date stopTime = DateUtil.endOfDay(tomorrow);
        InsuranceFetchPlanPageRequest request = new InsuranceFetchPlanPageRequest();
        request.setInitFetchStartTime(startTime);
        request.setInitFetchStopTime(stopTime);
        request.setFetchStatus(2);
        Page<InsuranceFetchPlanDO> doPage;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(100);
            doPage = insuranceFetchPlanService.pageList(request);
            List<InsuranceFetchPlanDO> doList = doPage.getRecords();
            if (CollUtil.isEmpty(doList)) {
                continue;
            }
            for (InsuranceFetchPlanDO insuranceFetchPlanDO : doList) {
                // 取药人，取药时间，取药地点，药品名称
                List<InsuranceFetchPlanDetailDTO> fetchPlanDetailDTOList = insuranceFetchPlanDetailService.getByInsuranceRecordId(insuranceFetchPlanDO.getInsuranceRecordId());
                String goodsName = fetchPlanDetailDTOList.stream().map(InsuranceFetchPlanDetailDTO::getGoodsName).collect(Collectors.joining("、"));
                InsuranceRecordDTO insuranceRecordDO = insuranceRecordService.getById(insuranceFetchPlanDO.getInsuranceRecordId());
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(insuranceRecordDO.getEid());
                // 1. 推送公众号
                pushMessage(insuranceRecordDO.getUserId(), insuranceRecordDO.getIssueName(), enterpriseDTO.getName(), goodsName);

                // 2. 短信消息
                sendMessage(insuranceRecordDO.getIssuePhone(), insuranceRecordDO.getIssueName(), goodsName);
            }
            current = current + 1;
        } while (CollUtil.isNotEmpty(doPage.getRecords()));

        return true;
    }

    /**
     * 短信取药提醒推送
     *
     * @param issuePhone 被保人手机号
     * @param issueName  被保人姓名
     * @param goodsName  取药商品名称，多个商品时、隔开
     */
    private void sendMessage(String issuePhone, String issueName, String goodsName) {
        String content = StrFormatter.format(SmsMedicineReminderEnum.MEDICINE_REMINDER.getTemplateContent(), issueName, goodsName);
        log.info("【取药提醒】短信推送，推送内容为:[{}],手机号为:[{}]", content, issuePhone);
        smsApi.send(issuePhone, content, SmsTypeEnum.INSURANCE_FETCH_REMINDER, SmsSignatureEnum.YILING_HEALTH);
    }

    /**
     * 公众号取药提醒推送
     *
     * @param userId    用户id
     * @param issueName 被保人姓名
     * @param ename     取药药店名称
     * @param goodsName 取药商品名称，多个商品时、隔开
     */
    private void pushMessage(Long userId, String issueName, String ename, String goodsName) {
        // 1.通过用户的id难道对应的公众号openId
        HmcUser hmcUser = hmcUserApi.getByIdAndAppId(userId,  wxMaService.getWxMaConfig().getAppid());
        if (null == hmcUser) {
            return;
        }
        GzhUserDTO gzhUserDTO = gzhUserApi.getByUnionIdAndAppId(hmcUser.getUnionId(), wxService.getWxMpConfigStorage().getAppId());
        if (null == gzhUserDTO) {
            return;
        }

        // 2.公众号推送地址
        String accessToken = null;
        try {
            accessToken = wxService.getAccessToken();
        } catch (WxErrorException e) {
            log.error("获取accessToken出错：{}", e.getMessage(), e);
            return;
        }

        String url = String.format(WxConstant.URL_TEMPLATE_SEND_POST, accessToken);

        String toUser = gzhUserDTO.getGzhOpenId();

        WxMsgDTO msgVO = WxMsgDTO.builder().touser(toUser).template_id(templateConfig.getTakeMedReminder()).build();

        Map<String, WxMssData> data = new HashMap<>();
        data.put("first", WxMssData.builder().value("你好，我是您的专属服务师").build());
        data.put("keyword1", WxMssData.builder().value(issueName).build());
        data.put("keyword2", WxMssData.builder().value("请在当天或者明天取药").build());
        data.put("keyword3", WxMssData.builder().value(ename).build());
        data.put("remark", WxMssData.builder().value("您好，距离您上次使用<" + goodsName + ">，已经超过一个月，小亿管家提醒您，及时到药店取药哦！").build());

        // TODO 这里跳转小程序 （带上orderId）
        MiniProgram miniProgram = MiniProgram.builder().appid(wxMaService.getWxMaConfig().getAppid()).pagepath(null).build();

        msgVO.setData(data);
        msgVO.setMiniprogram(miniProgram);

        // 公众号推送
        log.info("【取药提醒】推送公众号，请求数据为:[{}],请求地址:[{}]", JSONUtil.toJsonStr(msgVO), url);
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(msgVO));
        log.info("【取药提醒】推送公众号，返回数据为:[{}]", result);

    }

    @Override
    public OrderClaimInformationDTO getClaimInformation(Long id) {
        OrderClaimInformationDTO claimInformationDTO = new OrderClaimInformationDTO();
        OrderDO order = this.getById(id);
        if (Objects.isNull(order)) {
            log.info("根据参数id未查询到订单信息：{}", id);
            return claimInformationDTO;
        }
        Long insuranceRecordId = order.getInsuranceRecordId();
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordService.getById(insuranceRecordId);
        if (Objects.isNull(insuranceRecordDTO)) {
            log.info("根据参保记录id未查询到保单信息：{}", insuranceRecordId);
            return claimInformationDTO;
        }

        InsuranceCompanyDO insuranceCompany = insuranceCompanyService.getById(insuranceRecordDTO.getInsuranceCompanyId());

        String orderReceipts = order.getOrderReceipts();
        if (StrUtil.isNotBlank(orderReceipts)) {
            List<String> orderReceiptsList = Arrays.stream(orderReceipts.split("\\,")).map(item -> fileService.getUrl(item, FileTypeEnum.ORDER_RECEIPTS)).collect(Collectors.toList());
            claimInformationDTO.setOrderReceiptsList(orderReceiptsList);
            claimInformationDTO.setOrderReceiptsKeyList(Arrays.stream(orderReceipts.split("\\,")).collect(Collectors.toList()));
        }

        if (StrUtil.isNotBlank(insuranceRecordDTO.getIdCardFront())) {
            String frontUrl = fileService.getUrl(insuranceRecordDTO.getIdCardFront(), FileTypeEnum.ID_CARD_FRONT_PHOTO);
            claimInformationDTO.setIdCardFront(frontUrl);
            claimInformationDTO.setIdCardFrontKey(insuranceRecordDTO.getIdCardFront());

        }

        if (StrUtil.isNotBlank(insuranceRecordDTO.getIdCardBack())) {
            String backUrl = fileService.getUrl(insuranceRecordDTO.getIdCardBack(), FileTypeEnum.ID_CARD_BACK_PHOTO);
            claimInformationDTO.setIdCardBack(backUrl);
            claimInformationDTO.setIdCardBackKey(insuranceRecordDTO.getIdCardBack());
        }

        if (StrUtil.isNotBlank(insuranceRecordDTO.getHandSignature())) {
            String handSignaturePicture = fileService.getUrl(insuranceRecordDTO.getHandSignature(), FileTypeEnum.HAND_SIGNATURE_PICTURE);
            claimInformationDTO.setHandSignature(handSignaturePicture);
            claimInformationDTO.setHandSignatureKey(insuranceRecordDTO.getHandSignature());
        }

        claimInformationDTO.setIssueName(StrUtil.hide(insuranceRecordDTO.getIssueName(), 1, 4));
        claimInformationDTO.setIssuePhone(StrUtil.hide(insuranceRecordDTO.getIssuePhone(), 3, 9));
        claimInformationDTO.setPolicyNo(insuranceRecordDTO.getPolicyNo());
        claimInformationDTO.setClaimProtocolUrl(insuranceCompany.getClaimProtocolUrl());

        return claimInformationDTO;
    }

    @Override
    @Transactional
    public boolean submitClaimInformation(SaveClaimInformationRequest request) {
        OrderDO order = this.getById(request.getId());

        if (Objects.isNull(order)) {
            log.info("根据参数id未查询到订单信息：{}", request);
            return false;
        }

        // 1、更新订单票据
        if (CollUtil.isNotEmpty(request.getOrderReceiptsList())) {
            String orderReceipts = String.join(",", request.getOrderReceiptsList());

            QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(OrderDO::getId, order.getId());

            OrderDO target = new OrderDO();
            target.setOrderReceipts(orderReceipts);

            this.update(target, wrapper);
        }

        // 2、更新身份证照片、手写签名
        insuranceRecordService.updateSfzAndSignature(order.getInsuranceRecordId(), request);

        return true;
    }

    @GlobalTransactional
    public MqMessageBO confirm(ConfirmTookRequest request) {
        OrderDO order = this.getById(request.getId());
        if (Objects.isNull(order)) {
            log.info("根据id未获取到订单数据，参数:{}", request.getId());
            throw new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS);
        }
        order.setOrderStatus(HmcOrderStatusEnum.FINISHED.getCode());
        order.setPaymentStatus(HmcPaymentStatusEnum.PAYED.getCode());
        order.setFinishTime(DateUtil.date());
        order.setUpdateTime(DateUtil.date());
        order.setUpdateUser(request.getOpUserId());
        this.updateById(order);

        // 增加订单操作人
        orderOperateService.saveOrderOperate(order.getId(), HmcOrderOperateTypeEnum.SELF_PICKUP, "", request.getOpUserId(), request.getOpTime());

        // 准备完成订单mq
        MqMessageBO finishOrderMq = new MqMessageBO(Constants.TOPIC_HMC_FINISH_ORDER, Constants.TAG_HMC_FINISH_ORDER, order.getOrderNo());
        mqMessageSendApi.prepare(finishOrderMq);
        return finishOrderMq;
    }

    @Override
    public boolean confirmTook(ConfirmTookRequest request) {
        MqMessageBO confirmMq = _this.confirm(request);
        mqMessageSendApi.send(confirmMq);
        return Boolean.TRUE;
    }

    /**
     * 订单状态同步
     */
    @Override
    public void syncOrderStatus(Long orderId) {

        OrderDO order = this.getById(orderId);
        if (Objects.isNull(order)) {
            log.info("未获取到订单对象");
            return;
        }

        log.info(">>>>>准备调用泰康同步订单状态接口");
        SyncOrderStatusBO syncOrderStatusBO = new SyncOrderStatusBO();
        syncOrderStatusBO.setChannelMainId(order.getOrderNo());
        syncOrderStatusBO.setChannelOrderId(order.getOrderNo());
        syncOrderStatusBO.setOrderStatus(23);
        syncOrderStatusBO.setPaymentTime(order.getPaymentTime());
        syncOrderStatusBO.setGetGoodsTime(order.getFinishTime());
        syncOrderStatusBO.setRefundFinishTime(order.getFinishTime());

        log.info(">>>>>泰康泰康同步订单原始参数:{}", JSONUtil.toJsonStr(syncOrderStatusBO));

        String params = TkUtil.encrypt(JSONUtil.toJsonStr(syncOrderStatusBO), tkConfigProperties.getParamsKey());
        long currentSeconds = DateUtil.currentSeconds();

        Map<String, String> signMap = new HashMap<>();
        signMap.put("appId", tkConfigProperties.getAppId());
        signMap.put("params", params);
        signMap.put("timestamp", String.valueOf(currentSeconds));
        signMap.put("cipher", "123456");
        String sign = TkUtil.generateSign(signMap, tkConfigProperties.getSecretKey());

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("timestamp", currentSeconds);
        paramMap.put("sign", sign);
        paramMap.put("appId", tkConfigProperties.getAppId());
        paramMap.put("secretKey", tkConfigProperties.getSecretKey());
        paramMap.put("params", params);

        String url = tkConfigProperties.getTkServerUrl() + "/pbm/adjustment/orderStatus";
        log.info(">>>>>调用泰康同步订单状态接口路径：{}, ", url);
        log.info(">>>>>调用泰康同步订单状态接口参数：{}", JSONUtil.toJsonStr(paramMap));
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(paramMap));
        log.info(">>>>>调用泰康同步订单状态接口返回值：{}", result);


        TkResult trialResult = JSONUtil.toBean(result, new TypeReference<TkResult>() {
        }, false);

        // 如果结果不是成功状态，则异常提醒
        if (!"success".equals(trialResult.getResult())) {
            throw new BusinessException(InsuranceErrorCode.SYNC_TK_ORDER_ERROR, trialResult.getMessage());
        }

    }

}
