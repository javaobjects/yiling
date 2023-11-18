package com.yiling.hmc.order.mq.listener;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.hmc.config.H5DomainProperties;
import com.yiling.hmc.config.WxTemplateConfig;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.order.context.WxMsgContext;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.MiniProgram;
import com.yiling.hmc.wechat.dto.WxMsgDTO;
import com.yiling.hmc.wechat.dto.WxMssData;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanService;
import com.yiling.hmc.wechat.service.InsuranceRecordService;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * HMC 订单完成消息监听器
 * 发送用药指导通知
 *
 * @author: fan.shen
 * @date: 2022/4/26
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_FINISH_ORDER, consumerGroup = Constants.TAG_HMC_FINISH_ORDER)
public class HmcOrderFinishListener extends AbstractMessageListener {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    InsuranceRecordService insuranceRecordService;

    @Autowired
    InsuranceFetchPlanService fetchPlanService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    GzhUserApi gzhUserApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    /**
     * 小程序服务类
     */
    @Autowired
    WxMaService wxMaService;

    @Autowired
    WxTemplateConfig templateConfig;

    @Autowired
    RedisService redisService;

    @Autowired
    private H5DomainProperties h5DomainProperties;

    @MdcLog
    @Override
    @GlobalTransactional
    public MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("[HmcOrderFinishListener]HMC监听器收到消息：{}", body);

        MDC.put(Constants.TRACE_ID, TraceIdUtil.genTraceId());

        // 获取订单
        OrderDO orderDO = orderService.queryByOrderNo(body);

        if (Objects.isNull(orderDO)) {
            log.error("[HmcOrderFinishListener]根据orderNo未获取到订单:{}", body);
            return MqAction.CommitMessage;
        }

        // 获取保险
        InsuranceRecordDTO insuranceRecordDTO = insuranceRecordService.getById(orderDO.getInsuranceRecordId());
        if (Objects.isNull(insuranceRecordDTO)) {
            log.error("[HmcOrderFinishListener]根据insuranceRecordId未获取到参保记录:{}", orderDO.getInsuranceRecordId());
            return MqAction.CommitMessage;
        }

        // 获取C端用户
        HmcUser hmcUser = hmcUserApi.getByIdAndAppId(insuranceRecordDTO.getUserId(), wxMaService.getWxMaConfig().getAppid());
        if (Objects.isNull(hmcUser)) {
            log.error("[HmcOrderFinishListener]根据userId未获取到用户信息:{}", insuranceRecordDTO.getUserId());
            return MqAction.CommitMessage;
        }

        // 获取订单详情
        List<OrderDetailDO> orderDetailDOList = orderDetailService.listByOrderId(orderDO.getId());

        if (CollUtil.isEmpty(orderDetailDOList)) {
            log.error("[HmcOrderFinishListener]根据orderId未获取到订单明细:{}", orderDO.getId());
            return MqAction.CommitMessage;
        }

        // 更新拿药计划状态 -> 已拿

        // 5.更新拿药计划状态
        boolean updateResult = orderService.updateFetchPlanStatus(orderDO);
        if (!updateResult) {
            log.error("[HmcOrderFinishListener]根据参保记录更新拿药状态失败:{}", orderDO.getInsuranceRecordId());
            return MqAction.CommitMessage;
        }

        List<Long> goodsIdList = orderDetailDOList.stream().map(OrderDetailDO::getGoodsId).collect(Collectors.toList());
        List<GoodsSkuDTO> goodsSkuList = goodsHmcApi.getGoodsSkuByGids(goodsIdList);
        Map<Long, GoodsSkuDTO> goodsSkuMap = goodsSkuList.stream().collect(Collectors.toMap(GoodsSkuDTO::getGoodsId, o -> o, (k1, k2) -> k1));

        // 6、扣减库存
        List<AddOrSubtractQtyRequest> requestList = orderDetailDOList.stream().map(item -> {
            AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
            request.setOrderNo(orderDO.getOrderNo());
            request.setSkuId(Optional.ofNullable(goodsSkuMap.get(item.getGoodsId())).map(GoodsSkuDTO::getId).orElse(null));
            request.setInventoryId(Optional.ofNullable(goodsSkuMap.get(item.getGoodsId())).map(GoodsSkuDTO::getInventoryId).orElse(null));
            request.setFrozenQty(item.getGoodsQuantity());
            request.setQty(item.getGoodsQuantity());
            request.setOpUserId(orderDO.getOrderUser());
            request.setOpTime(new Date());
            return request;
        }).collect(Collectors.toList());
        goodsHmcApi.batchSubtractFrozenQtyAndQty(requestList);

        // 获取公众号用户
        GzhUserDTO gzhUserDTO = gzhUserApi.getByUnionIdAndAppId(hmcUser.getUnionId(), wxMpService.getWxMpConfigStorage().getAppId());
        if (Objects.isNull(gzhUserDTO) || StrUtil.isBlank(gzhUserDTO.getGzhOpenId())) {
            log.error("[HmcOrderFinishListener]根据unionId + appId 未获取到公众号用户信息:{},{}", hmcUser.getUnionId(), wxMpService.getWxMpConfigStorage().getAppId());
            return MqAction.CommitMessage;
        }

        OrderDetailDO orderDetailDO = orderDetailDOList.get(0);

        HmcGoodsDTO goodsDTO = goodsApi.findById(orderDetailDO.getHmcGoodsId());
        if (Objects.isNull(goodsDTO)) {
            log.error("[HmcOrderFinishListener]根据hmcGoodsId未获取到goods:{}", orderDetailDO.getHmcGoodsId());
            return MqAction.CommitMessage;
        }

        StandardGoodsAllInfoDTO standardGoodsById = standardGoodsApi.getStandardGoodsById(goodsDTO.getStandardId());
        String keyword2 = Optional.ofNullable(standardGoodsById).map(StandardGoodsAllInfoDTO::getGoodsInstructionsInfo).map(StandardInstructionsGoodsDTO::getUsageDosage).orElse("请查看纸质药品说明书或按医嘱执行");

        if (keyword2.length() >= 81) {
            keyword2 = keyword2.substring(0, 81) + "......";
        }

        WxMsgDTO wxMsgDTO = WxMsgDTO.builder().touser(gzhUserDTO.getGzhOpenId()).template_id(templateConfig.getMedInstruction()).build();

        Map<String, WxMssData> data = new HashMap<>();
        data.put("first", WxMssData.builder().value("用药指导通知").build());
        data.put("keyword1", WxMssData.builder().value(orderDetailDO.getGoodsName()).build());
        data.put("keyword2", WxMssData.builder().value(keyword2).build());
        data.put("remark", WxMssData.builder().value(insuranceRecordDTO.getIssueName() + "您好，快来查看您的专属用药指导单，用药时间、用药注意事项，禁忌症等都在这里，快来查看吧！").build());

        // 这里跳转小程序 （带上orderId）
        String pagePath = "pagesSub/main/medicationList/medicationList?id=" + orderDO.getId();
        // todo 等小程序发布之后，再加上pagePath
        MiniProgram miniProgram = MiniProgram.builder().appid(wxMaService.getWxMaConfig().getAppid()).pagepath(pagePath).build();

        wxMsgDTO.setData(data);
        wxMsgDTO.setMiniprogram(miniProgram);

        String url;
        try {
            url = String.format(WxConstant.URL_TEMPLATE_SEND_POST, wxMpService.getAccessToken());
        } catch (WxErrorException e) {
            log.error("[HmcOrderFinishListener]获取accessToken出错：{}", e.getMessage(), e);
            return MqAction.CommitMessage;
        }

        if (StrUtil.isBlank(url)) {
            log.error("[HmcOrderFinishListener]推送url为空");
            return MqAction.CommitMessage;
        }

        String result = HttpUtil.post(url, JSONUtil.toJsonStr(wxMsgDTO));

        log.info("[HmcOrderFinishListener]HMC监听器处理完成，参数：{}, 处理结果：{}", JSONUtil.toJsonStr(wxMsgDTO), result);

        // 如果是测试公众号 -> 模板消息
        if (wxMpService.getWxMpConfigStorage().getAppId().equals("wxbdb7fce20be7cf4d")) {

            log.info("[HmcOrderFinishListener]准备针对测试公众号发送模板消息.....");
            WxMsgContext msgContext = new WxMsgContext();
            String key = RedisKey.generate("wechat", "pagePath", UUID.randomUUID().toString());

            msgContext.setKey(key);
            msgContext.setPage("pagesSub/main/medicationList/medicationList");
            msgContext.setScene("id=" + orderDO.getId());

            redisService.set(key, JSONUtil.toJsonStr(msgContext), 60 * 60 * 24);
            log.info("[HmcOrderFinishListener]redisKey:{},redisValue:{}", key, msgContext);

            String jumpUrl = h5DomainProperties.getDomainUrl() + "cmp/code?id=" + key;
            wxMsgDTO.setMiniprogram(null);
            wxMsgDTO.setUrl(jumpUrl);

            String sendTemplateResult = HttpUtil.post(url, JSONUtil.toJsonStr(wxMsgDTO));

            log.info("[HmcOrderFinishListener]准备针对测试公众号发送模板结束，结果：{}, 参数：{}", sendTemplateResult, wxMsgDTO);


        }
        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
        };
    }
}
