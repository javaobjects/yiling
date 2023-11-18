package com.yiling.activity.web.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.XmlUtil;
import com.yiling.activity.web.wx.enums.WxTypeEnum;
import com.yiling.activity.web.wx.factory.WxMsgFactory;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 微信消息通知控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/24
 */
@RestController
@Slf4j
@Api(tags = "微信消息通知控制器")
public class WxMsgNotifyController extends BaseController {

    @Autowired
    private SpringAsyncConfig asyncConfig;

    @Autowired
    WxMpService wxMpService;

    @Autowired
    WxMaService wxMaService;

    @Autowired
    WxMsgFactory wxMsgFactory;


    /**
     * 微信配置服务器 验证
     */
    @GetMapping("/notify/{appId}")
    public String check(@PathVariable("appId") String appId, @RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestParam("echostr") String echoStr) {
        log.info("微信配置服务器验证token开始,appId:{}.....", appId);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echoStr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        WxTypeEnum wxType = WxTypeEnum.getByAppId(appId);

        if (Objects.isNull(wxType)) {
            throw new IllegalArgumentException("根据appId未获取到配置信息!");
        }

        if ("mp".equals(wxType.getType()) && wxMpService.checkSignature(timestamp, nonce, signature)) {
            log.info("微信配置服务器验证token结束，返回：{}", echoStr);
            return echoStr;
        }

        if ("ma".equals(wxType.getType()) && wxMaService.checkSignature(timestamp, nonce, signature)) {
            log.info("微信配置服务器验证token结束，返回：{}", echoStr);
            return echoStr;
        }

        return "非法请求";
    }


    /**
     * 接收微信消息通知
     * 根据appId调用不同消息服务，推送客服消息
     */
    @ResponseBody
    @PostMapping(value = "/notify/{appId}", produces = "application/xml; charset=UTF-8")
    public void handleMsg(@PathVariable("appId") String appId, @RequestBody String msgBody) {

        log.info("收到微信通知appId:{}, 消息体： \n{}", appId, XmlUtil.format(msgBody));

       if ("wxbdb7fce20be7cf4d".equals(appId)) {
           wxMsgFactory.getWxMsgServiceByAppId("wxfbd61abfd05c127e").doMsg(msgBody);
           // CompletableFuture.runAsync(() -> wxMsgFactory.getWxMsgServiceByAppId("wxfbd61abfd05c127e").doMsg(msgBody), asyncConfig.getAsyncExecutor());
           return;
       }

        wxMsgFactory.getWxMsgServiceByAppId(appId).doMsg(msgBody);

        // 异步处理消息
        // CompletableFuture.runAsync(() -> wxMsgFactory.getWxMsgServiceByAppId(appId).doMsg(msgBody), asyncConfig.getAsyncExecutor());

    }

}
