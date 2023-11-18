package com.yiling.hmc.third.controller;

import cn.hutool.json.JSONUtil;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.api.OrderPrescriptionApi;
import com.yiling.hmc.order.api.OrderPrescriptionGoodsApi;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.third.form.OrderPrescriptionNotify;
import com.yiling.hmc.wechat.dto.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 保单兑付回调控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/4/8
 */
@Slf4j
@RestController
@RequestMapping("/order_notify")
@Api(tags = "保单兑付回调控制器")
public class OrderNotifyController {

    @DubboReference
    OrderApi orderApi;

    @ApiOperation(value = "兑付回调")
    @PostMapping("/call_back")
    @Log(title = "兑付回调", businessType = BusinessTypeEnum.OTHER)
    public Result<Long> callBack(@RequestBody String content) {

        log.info("兑付回调,请求参数 ： {}", JSONUtil.toJsonStr(content));

//        String decryptStr = AESUtils.decryptStr(content, key);
//
//        log.info("兑付回调,解密结果：{}", decryptStr);

        OrderPrescriptionNotify orderPrescriptionNotify = JSONUtil.toBean(content, OrderPrescriptionNotify.class);

        String orderNo = orderPrescriptionNotify.getOrderNo();

        OrderDTO orderDTO = orderApi.getByOrderNo(orderNo);
        if (Objects.isNull(orderDTO)) {
            return Result.failed("根据订单编号未查询到订单");
        }

        OrderNotifyRequest orderNotifyRequest = PojoUtils.map(orderPrescriptionNotify, OrderNotifyRequest.class);

        Long id = orderApi.orderNotify(orderNotifyRequest);

        log.info("兑付回调处理完成...");
        return Result.success(id);
    }


}
