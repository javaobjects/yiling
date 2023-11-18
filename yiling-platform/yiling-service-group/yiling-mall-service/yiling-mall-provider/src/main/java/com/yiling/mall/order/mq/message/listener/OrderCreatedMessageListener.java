package com.yiling.mall.order.mq.message.listener;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderExtendApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderExtendDTO;
import com.yiling.order.order.dto.request.CreateOrderExtendRequest;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.CustomerCreateOrderMqRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.member.api.EnterpriseMemberApi;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 新订单创建消息监听器
 *
 * @author: xuan.zhou
 * @date: 2021/7/21
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_CREATED, consumerGroup = Constants.TOPIC_ORDER_CREATED)
public class OrderCreatedMessageListener  extends AbstractMessageListener {
    @DubboReference
    OrderApi                                             orderApi;
    @DubboReference
    EnterpriseApi                                        enterpriseApi;
    @DubboReference
    CustomerApi                                          customerApi;
    @DubboReference
    MqMessageSendApi                                     mqMessageSendApi;
    @DubboReference
    OrderExtendApi                                       orderExtendApi;
    @DubboReference
    EnterpriseMemberApi                                  enterpriseMemberApi;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi                           mqMessageConsumeFailureApi;
    @Lazy
    @Autowired
    private OrderCreatedMessageListener                  _this;

    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String orderNo = null;

        try {
            orderNo = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);

            if (StrUtil.isEmpty(orderNo)) {
                log.error("订单号为空");
                return MqAction.CommitMessage;
            }

            OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
            if (orderDTO == null) {
                log.error("订单信息不存在：{}", orderNo);
                return MqAction.CommitMessage;
            }

            Map<OrderTypeEnum, Function<OrderDTO,Boolean>> orderCreateFunctions = Maps.newHashMapWithExpectedSize(2);
            orderCreateFunctions.put(OrderTypeEnum.POP,_this::popOrderCreated);
            orderCreateFunctions.put(OrderTypeEnum.B2B,this::b2bOrderCreated);

            orderCreateFunctions.get(OrderTypeEnum.getByCode(orderDTO.getOrderType())).apply(orderDTO);

        } catch (BusinessException e) {

            log.debug("[{}],errorMsg:{}",orderNo,e.getMessage());

            return MqAction.CommitMessage;

        }


        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {


        return 5;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {

        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }






    /**
     * 构建订单扩展信息
     * @param orderDTO
     */
    private void buildOrderExtendInfo(OrderDTO orderDTO) {

        List<Long> allEids = ListUtil.toList(orderDTO.getBuyerEid(),orderDTO.getSellerEid());

        // 查询下单用户企业信息
        List<EnterpriseDTO> allEnterpriseDTOList = enterpriseApi.listByIds(allEids);

        if (CollectionUtil.isEmpty(allEnterpriseDTOList)) {

            log.error("企业信息不存在：{}", allEids);

            throw new RuntimeException("企业信息数据异常!");
        }

        Map<Long, EnterpriseDTO> enterpriseDTOMap = allEnterpriseDTOList.stream().collect(Collectors.toMap(t -> t.getId(), Function.identity()));
        EnterpriseDTO buyerEnterpriseDTO = enterpriseDTOMap.get(orderDTO.getBuyerEid());
        EnterpriseDTO sellerEnterpriseDTO = enterpriseDTOMap.get(orderDTO.getSellerEid());

        // 企业信息异常
        if (ObjectUtil.isNull(buyerEnterpriseDTO) || ObjectUtil.isNull(sellerEnterpriseDTO)) {

            log.error("企业信息不存在：{}", allEids);

            throw new RuntimeException("企业信息数据异常!");
        }

        CreateOrderExtendRequest createOrderExtendRequest = new CreateOrderExtendRequest();
        createOrderExtendRequest.setOrderId(orderDTO.getId());
        createOrderExtendRequest.setOrderNo(orderDTO.getOrderNo());
        // 买家信息
        createOrderExtendRequest.setBuyerProvinceName(buyerEnterpriseDTO.getProvinceName());
        createOrderExtendRequest.setBuyerCityName(buyerEnterpriseDTO.getCityName());
        createOrderExtendRequest.setBuyerRegionName(buyerEnterpriseDTO.getRegionName());
        createOrderExtendRequest.setBuyerProvinceCode(buyerEnterpriseDTO.getProvinceCode());
        createOrderExtendRequest.setBuyerCityCode(buyerEnterpriseDTO.getCityCode());
        createOrderExtendRequest.setBuyerRegionCode(buyerEnterpriseDTO.getRegionCode());

        // 卖家信息
        createOrderExtendRequest.setSellerProvinceName(sellerEnterpriseDTO.getProvinceName());
        createOrderExtendRequest.setSellerCityName(sellerEnterpriseDTO.getCityName());
        createOrderExtendRequest.setSellerRegionName(sellerEnterpriseDTO.getRegionName());
        createOrderExtendRequest.setSellerProvinceCode(sellerEnterpriseDTO.getProvinceCode());
        createOrderExtendRequest.setSellerCityCode(sellerEnterpriseDTO.getCityCode());
        createOrderExtendRequest.setSellerRegionCode(sellerEnterpriseDTO.getRegionCode());
        createOrderExtendRequest.setVipFlag(CreateOrderExtendRequest.VipFlagEnum.NORMAL.getCode());

        // 设置是否是会员
        if (enterpriseMemberApi.getEnterpriseMemberStatus(orderDTO.getBuyerEid())) {

            createOrderExtendRequest.setVipFlag(CreateOrderExtendRequest.VipFlagEnum.VIP.getCode());
        }

        orderExtendApi.save(createOrderExtendRequest);
    }


    /**
     * b2b自动创建订单
     * @param orderDTO
     * @return
     */
    @GlobalTransactional
    public List<MqMessageBO> b2bOrderCreateProcessData(OrderDTO orderDTO) {

        OrderExtendDTO orderExtendInfo = orderExtendApi.getOrderExtendInfo(orderDTO.getId());

        if (orderExtendInfo != null) {

            return ListUtil.empty();
        }

        // 创建订单扩展信息
        this.buildOrderExtendInfo(orderDTO);

        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getByCode(orderDTO.getOrderStatus());

        // 不是待审核无需处理后续逻辑
        if (orderStatusEnum != OrderStatusEnum.UNAUDITED) {
            log.warn("订单状态不满足条件：{}", orderStatusEnum);

            return ListUtil.empty();
        }

        AddCustomerRequest request = new AddCustomerRequest();
        request.setCustomerEid(orderDTO.getBuyerEid());
        request.setCustomerName(orderDTO.getBuyerEname());
        request.setEid(orderDTO.getSellerEid());
        customerApi.addB2bCustomer(request);

        List<MqMessageBO> mqMessageBOList = Lists.newArrayList();
        // 无需在线支付的支付方式
        EnumSet<PaymentMethodEnum> hasPayMethods = EnumSet.of(PaymentMethodEnum.OFFLINE,PaymentMethodEnum.PREPAYMENT,PaymentMethodEnum.PAYMENT_DAYS);

        // 如果订单支付方式为线下或者账期时订单状态变成“待发货”
        if (orderDTO.getPaymentMethod() != null && hasPayMethods.contains(PaymentMethodEnum.getByCode(orderDTO.getPaymentMethod().longValue()))) {

            // 自动审核通过,变成待发货
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_ORDER_AUDITED, "", orderDTO.getOrderNo());
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

            mqMessageBOList.add(mqMessageBO);
        }


        CustomerCreateOrderMqRequest customerCreateOrderMqRequest = new CustomerCreateOrderMqRequest();
        customerCreateOrderMqRequest.setCustomerEid(orderDTO.getBuyerEid());
        customerCreateOrderMqRequest.setSellerEid(orderDTO.getSellerEid());
        customerCreateOrderMqRequest.setOrderCreateTime(orderDTO.getCreateTime());
        // 维护客户最后下单时间
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_CUSTOMER_CREATE_ORDER, Constants.TAG_TOPIC_CUSTOMER_CREATE_ORDER, JSON.toJSONString(customerCreateOrderMqRequest));

        // 持久到数据库库
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageBOList.add(mqMessageBO);

        // 注意:销售助手无需自动发券,销售助手创建订单没有支付方式,转到确认订单时触发自动发卷逻辑
        if (OrderSourceEnum.SA == OrderSourceEnum.getByCode(orderDTO.getOrderSource())) {

            return mqMessageBOList;
        }

        // 下动发券
        MqMessageBO couponMessageBo = new MqMessageBO(Constants.TOPIC_ORDER_COUPON_AUTOMATIC_SEND, Constants.TAG_ORDER_COUPON_AUTOMATIC_CREATED, orderDTO.getOrderNo());
        couponMessageBo = mqMessageSendApi.prepare(couponMessageBo);
        mqMessageBOList.add(couponMessageBo);

        return mqMessageBOList;

    }


    /**
     * 通知创建客户关系
     * @param orderDTO
     * @return
     */
    public Boolean b2bOrderCreated(OrderDTO orderDTO) {

        List<MqMessageBO> mqMessageBOList = _this.b2bOrderCreateProcessData(orderDTO);

        mqMessageBOList.stream().forEach(t -> mqMessageSendApi.send(t));

        return Boolean.TRUE;
    }

    /**
     * POP订单创建自动审核
     * @param orderDTO
     */
    @GlobalTransactional
    public Boolean popOrderCreated(OrderDTO orderDTO) {

        OrderAuditStatusEnum orderAuditStatusEnum = OrderAuditStatusEnum.getByCode(orderDTO.getAuditStatus());
        // 拦截扩展信息,防止出现消息重复
        OrderExtendDTO orderExtendInfo = orderExtendApi.getOrderExtendInfo(orderDTO.getId());

        if (orderExtendInfo != null) {

            return true;
        }

        // 创建订单扩展信息
        this.buildOrderExtendInfo(orderDTO);

        // 拦截
        if (orderAuditStatusEnum != OrderAuditStatusEnum.UNSUBMIT) {
            log.warn("订单状态不满足条件：{}", orderAuditStatusEnum);
            return true;
        }

        // 非以岭的，并且不是工业直属的，修改为自动审核
        Boolean isYilingOrder = needUpdateAuditStatus(Collections.singletonList(orderDTO.getDistributorEid()));

        if (!isYilingOrder) {
            log.info("该订单为非以岭卖家订单，订单审核状态将被修改为审核通过");
            orderApi.updateAuditStatus(orderDTO.getId(), OrderAuditStatusEnum.UNSUBMIT, OrderAuditStatusEnum.AUDIT_PASS, 0L, "系统自动审核");
        }

        // Pop pc 下单,如果是以岭订单,订单自动变成待审核
        if (isYilingOrder && OrderSourceEnum.POP_PC == OrderSourceEnum.getByCode(orderDTO.getOrderSource())) {

            log.info("该订单为以岭卖家订单，订单审核状态将被修改为待审核");
            orderApi.updateAuditStatus(orderDTO.getId(), OrderAuditStatusEnum.UNSUBMIT, OrderAuditStatusEnum.UNAUDIT, 0L, null);
        }

        return true;
    }


    /**
     * 是否需要修改审核状态
     * @param distributorEids
     * @return
     */
    private boolean needUpdateAuditStatus(List<Long> distributorEids) {
        // 以岭企业列表
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);

        if (yilingSubEids.stream().anyMatch(eid -> distributorEids.contains(eid))) {

            return true;
        }

        // 查询工业直属的企业ID信息
        List<Long> industryDirectEids = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);

        return industryDirectEids.stream().anyMatch(eid -> distributorEids.contains(eid));
    }
}
