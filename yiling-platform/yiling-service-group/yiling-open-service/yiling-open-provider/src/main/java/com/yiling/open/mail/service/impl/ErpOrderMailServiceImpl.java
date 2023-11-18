package com.yiling.open.mail.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.yiling.basic.mail.api.MailApi;
import com.yiling.basic.mail.bo.MailConfigBO;
import com.yiling.open.erp.dto.ErpOrderPushDTO;
import com.yiling.open.erp.enums.PushTypeEnum;
import com.yiling.open.erp.service.ErpOrderPushService;
import com.yiling.open.mail.config.OrderPushMailConfig;
import com.yiling.basic.mail.enums.MailEnum;
import com.yiling.open.mail.service.ErpOrderMailService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author shichen
 * @类名 ErpOrderMailServiceImpl
 * @描述
 * @创建时间 2022/2/9
 * @修改人 shichen
 * @修改时间 2022/2/9
 **/
@Service
@Slf4j
public class ErpOrderMailServiceImpl implements ErpOrderMailService {
    @Autowired
    private TemplateEngine          templateEngine;

    @Autowired
    private OrderPushMailConfig orderPushMailConfig;

    @DubboReference
    private MailApi                 mailApi;

    @DubboReference
    private OrderApi                orderApi;

    @Autowired
    private ErpOrderPushService     erpOrderPushService;

    @Override
    public boolean sendMailByPushOrderFail(List<Long> orderIds) {
        if(CollectionUtils.isEmpty(orderIds)){
            log.error("失败订单发送邮件失败，参数orderIds为空");
            return false;
        }
        List<ErpOrderPushDTO> erpPushOrderList = erpOrderPushService.getErpPushOrderListByOrderIds(orderIds, PushTypeEnum.ORDER_PUSH.getCode());
        //获取不是提取成功的订单
        List<ErpOrderPushDTO> filterOrderPushs = erpPushOrderList.stream().filter(orderPush ->!OrderErpPushStatusEnum.EXTRACT_SUCCESS.getCode().equals(orderPush.getErpPushStatus())).collect(Collectors.toList());
        //监听数据没有推送不成功的数据时不发送邮件
        if(CollectionUtils.isEmpty(filterOrderPushs)){
            return true;
        }
        Map<Long,Integer> filterMap = filterOrderPushs.stream().collect(Collectors.toMap(ErpOrderPushDTO::getOrderId, ErpOrderPushDTO::getErpPushStatus));
        List<Long>  filterOrderIds= Lists.newArrayList(filterMap.keySet());
        List<OrderDTO> filterOrders = orderApi.listByIds(filterOrderIds);
        //获取已发送成功过的订单idList
        List<Long> recordOrderIds = mailApi.getSendSuccessBusinessIds(MailEnum.ORDER_PUSH_FAIL,filterOrderIds);
        //已发送过邮件的订单不重复发送
        List<OrderDTO> orders = filterOrders.stream().map(order->{
            order.setErpPushStatus(filterMap.get(order.getId()));
            return order;
        }).filter(order -> !recordOrderIds.contains(order.getId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(orders)){
            return true;
        }
        //邮件唯一标识
        String mailId= UUID.randomUUID().toString().replace("-", "");
        //数据渲染模板
        Context context = new Context();
        context.setVariable("orders",orders);
        context.setVariable("mailId",mailId);
        String emailContent = templateEngine.process(MailEnum.ORDER_PUSH_FAIL.getTemplate(), context);
        MailConfigBO mailConfig = orderPushMailConfig.getConfig();
        mailConfig.setText(emailContent);
        return mailApi.sendHtmlMail(mailConfig,mailId, JSONArray.parseArray(JSON.toJSONString(orders)));
    }
}
