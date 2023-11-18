package com.yiling.activity.web.wx.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yiling.activity.web.config.H5DomainProperties;
import com.yiling.activity.web.wx.context.WxMsgHandlerContext;
import com.yiling.activity.web.wx.enums.SubscribeSourceEnum;
import com.yiling.activity.web.wx.enums.SubscribeStatusEnum;
import com.yiling.activity.web.wx.enums.WxQrCodeTypeEnum;
import com.yiling.activity.web.wx.enums.WxTypeEnum;
import com.yiling.activity.web.wx.handler.WxTextMsgHandler;
import com.yiling.activity.web.wx.service.WxMsgService;
import com.yiling.activity.web.wx.util.WxMediaUtil;
import com.yiling.activity.web.wx.util.WxQrCodeTypeUtil;
import com.yiling.activity.web.wx.vo.WxKfTextMsgVO;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.basic.wx.dto.request.CreateGzhUserRequest;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.hmc.gzh.api.HmcGzhGreetingApi;
import com.yiling.hmc.gzh.dto.GzhGreetingDTO;
import com.yiling.hmc.gzh.enums.GzhGreetingPublishStatusEnum;
import com.yiling.hmc.gzh.enums.HmcGzhSceneEnum;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.ih.user.dto.request.CheckUserDoctorRelRequest;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * 微信消息服务 - 以岭健康管理中心公众号 - wxfbd61abfd05c127e
 * <p>
 * 自然流量：1
 * 店员二维码：so:2_eId:220_uId:13
 * 药盒二维码：so:3
 *
 * @Author fan.shen
 * @Date 2022/7/22
 */
@Service
@Slf4j
@RefreshScope
public class WxMsgMpProdServiceImpl implements WxMsgService {

    @DubboReference
    GzhUserApi gzhUserApi;

    @DubboReference
    LotteryActivityApi lotteryActivityApi;

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
    private H5DomainProperties h5DomainProperties;

    // 兔年大吉
    @Value("${rabbit_year_happy}")
    private String rabbitYearHappy;

    // C端医带患活动卡片背景图
    @Value("${C_act_doc_patient_bg_img}")
    private String actDocPatientBgImg;

    // 问诊医生卡片背景图
    @Value("${C_diagnosis_bg_img}")
    private String diagnosisBgImg;

    @Autowired
    WxTextMsgHandler textMsgHandler;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    WxMediaUtil wxMediaUtil;

    @Autowired
    RedisService redisService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @DubboReference
    HmcGzhGreetingApi gzhGreetingApi;


    // 0元福利 - 中秋节
    // private static String welfareUrl = "https://mp.weixin.qq.com/s?__biz=MzkwMjMyNjY4OQ==&mid=2247486639&idx=1&sn=9a4c946cd165c729ac696d33110d5f10&chksm=c0a67c61f7d1f5771ab67a3fb53595ebd685717370031a8b9391b710b3aa75425d21a78cab31#rd";

    // 0元福利 - 1019
    private static String welfareUrl = "https://mp.weixin.qq.com/s?__biz=MzkwMjMyNjY4OQ==&mid=2247486728&idx=1&sn=325ca3a72ebd7f10a9007587b63fdb3f&chksm=c0a67dc6f7d1f4d057fe29c1e08968de05e0dc871ad2a53b32a64e8ed15132d58807a9bfc8f1#rd";

    // 药鉴真伪
    private static String checkFake = "https://mp.weixin.qq.com/s?__biz=MzkwMjMyNjY4OQ==&mid=2247486615&idx=1&sn=b573e1c45334c88bfa4c0099630d6760&chksm=c0a67c59f7d1f54fcb4dfa1ff3bbb81b48af0668727c896572e7cea18fee879f02fcf14850f4#rd";

    // 抽奖活动
    // private static String drawActivityUrl = "https://h.59yi.com/#/active/luckyDraw/wechat?activeId=32";

    // C端抽奖
    @Value("${C_draw_activity_url}")
    private String drawActivityUrl;

    // 使用新版欢迎语flag
    @Value("${c_greeting_new_version_flag}")
    private Boolean greetingNewVersionFlag;

    @Override
    public WxTypeEnum getWxType() {
        return WxTypeEnum.HMC_MP_PROD;
    }

    @Override
    public void doMsg(String msgBody) {

        // 1、处理用户信息
        handleUserInfo(msgBody);

        // 2、发送客服消息
        sendMsg(msgBody);

    }

    /**
     * 发送客服消息
     *
     * @param msgBody
     */
    private void sendMsg(String msgBody) {

        if (StrUtil.isEmpty(msgBody)) {
            log.info("消息体为空，跳过处理");
            return;
        }

        WxMpXmlMessage msg = WxMpXmlMessage.fromXml(msgBody);

        if (WxConstant.MSG_TYPE_EVENT.equals(msg.getMsgType())) {

            // 事件类型：点击菜单跳转链接时的事件推送(h5链接、小程序)
            if (WxConstant.EVENT_TYPE_VIEW.equalsIgnoreCase(msg.getEvent()) || WxConstant.EVENT_TYPE_VIEW_MINIPROGRAM.equalsIgnoreCase(msg.getEvent())) {
                log.info("===========收到点击菜单事件推送，处理开始===========");
                textMsgHandler.msgToRedis(msg);
                log.info("===========收到点击菜单事件推送，处理结束===========");
                return;
            } else {
                log.info("===========处理事件消息开始===========");
                handleEventMsg(msg);
                log.info("===========处理事件消息结束===========");
                return;
            }
        }

        if (WxConstant.MSG_TYPE_TEXT.equals(msg.getMsgType())) {
            log.info("===========处理文本信息开始===========");
            textMsgHandler.msgToRedis(msg);
            log.info("===========处理文本信息结束===========");
            return;
        }
        log.info("未知事件类型，参数:{}", JSONUtil.toJsonStr(msg));
    }

    /**
     * 处理事件消息
     *
     * @param msg
     * @return
     */
    private void handleEventMsg(WxMpXmlMessage msg) {
        Map<WxQrCodeTypeEnum, Function<WxMpXmlMessage, Boolean>> handlerMap = new HashMap<>();

        // 订阅公众号
        handlerMap.put(WxQrCodeTypeEnum.MP_SUBSCRIBE, this::mpSubscribeMsgHandler);

        // 通心络用药福利(周杰)
        handlerMap.put(WxQrCodeTypeEnum.TXL_WELFARE, this::txlWelfareMsgHandler);

        // 药品福利计划（文灵）
        handlerMap.put(WxQrCodeTypeEnum.STAFF_WELFARE, this::staffWelfareMsgHandler);

        // 药盒二维码
        handlerMap.put(WxQrCodeTypeEnum.BOX_QR_CODE, this::boxQrCodeMsgHandler);

        // C端抽奖活动
        handlerMap.put(WxQrCodeTypeEnum.C_LOTTERY_ACTIVITY, this::cLotteryActivityMsgHandler);

        // 市场健康包
        handlerMap.put(WxQrCodeTypeEnum.CHANNEL_MARKET_PACKAGE, this::channelMarketPackageHandler);

        // 市场健康包 药盒落地页弹框
        handlerMap.put(WxQrCodeTypeEnum.CHANNEL_MARKET_10, this::channelMarketBoxLandPageHandler);

        // 健康测评
        handlerMap.put(WxQrCodeTypeEnum.C_HEALTH_EVALUATE, this::cHealthEvaluateMsgHandler);

        // 医带患活动
        handlerMap.put(WxQrCodeTypeEnum.C_ACT_DOC_PATIENT, this::cActivityDocPatientHandler);

        WxQrCodeTypeEnum type = WxQrCodeTypeUtil.getWxQrCodeType(msg);
        if (Objects.isNull(type)) {
            log.info("根据场景二维码值未匹配到对应业务类型");
            return;
        }
        log.info("根据场景二维码值匹配到的结果为:{}", type);
        handlerMap.get(type).apply(msg);
    }

    /**
     * 订阅公众号消息处理器
     *
     * @param msg
     * @return
     */
    private boolean mpSubscribeMsgHandler(WxMpXmlMessage msg) {
        if (WxConstant.EVENT_TYPE_UNSUBSCRIBE.equals(msg.getEvent())) {
            log.info("取消订阅事件，跳过处理");
            return false;
        }
        // 欢迎语
        String content = "欢迎关注以岭健康管理中⼼，您身边值得信赖的⽤药管家 \r\n \r\n";

        // 转盘抽奖
        content += "\uD83E\uDDE7 戳这里<a href ='" + drawActivityUrl + " '>「转盘抽奖」</a> \r\n \r\n";

        // 健康自测
        String healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        content += "️️❗️️ 戳这里<a href ='" + healthEvaluate + "'>「健康自测」</a> \r\n \r\n ";

        // 药鉴真伪
        content += "\uD83D\uDD0D 戳这里<a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";

        content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";

        WxMpKefuMessage message1 = new WxMpKefuMessage();
        message1.setMsgType(WxConsts.KefuMsgType.TEXT);
        message1.setToUser(msg.getFromUser());
        // 如果开关打开 -> 走运营后台配置
        log.info("欢迎语开关配置:{}", greetingNewVersionFlag);
        if (greetingNewVersionFlag) {
            GzhGreetingDTO greetingDTO = gzhGreetingApi.getDetailBySceneId(Long.valueOf(HmcGzhSceneEnum.SUBSCRIBE.getType()));
            if (Objects.nonNull(greetingDTO) && StrUtil.isNotBlank(greetingDTO.getPublishVersion())) {
                log.info("后台设置自然关注欢迎语已发布，设置对象:{}", JSONUtil.toJsonStr(greetingDTO));
                message1.setContent(greetingDTO.getPublishVersion());

                // 更新触发数量
                gzhGreetingApi.updateTriggerCount(greetingDTO.getId());

            }
        } else {
            message1.setContent(content);
        }

        try {
            String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(message1);
            log.info("发送客服消息参数：{},结果：{}, accessToken：{}", JSONUtil.toJsonStr(message1), pushResult, wxMpService.getAccessToken());
        } catch (WxErrorException e) {
            log.error("发送客服消息报错,错误信息:{}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * 处理C端医带患消息
     * 医带患消息二维码参数：qt:40_actId:1_docId:2
     *
     * @param msg
     */
    private Boolean cActivityDocPatientHandler(WxMpXmlMessage msg) {
        String qrValue = null;
        if (WxConstant.EVENT_TYPE_SUBSCRIBE.equals(msg.getEvent())) {
            qrValue = msg.getEventKey().substring(8);
        }
        if (WxConstant.EVENT_TYPE_SCAN.equals(msg.getEvent())) {
            qrValue = msg.getEventKey();
        }
        Map<String, String> paramMap = YlStrUtils.dealParam(qrValue);

        if (!paramMap.containsKey("docId")) {
            log.info("C端医带患消息,docId为空");
            return false;
        }
        WxMpUser wxMpUser;
        try {
            wxMpUser = wxMpService.getUserService().userInfo(msg.getFromUser());
        } catch (WxErrorException e) {
            log.error("获取公众号用户信息出错：{}", e.getMessage(), e);
            return false;
        }
        if (Objects.isNull(wxMpUser) || StrUtil.isBlank(wxMpUser.getUnionId())) {
            log.info("C端医带患消息,unionId为空");
            return false;
        }

        HmcUser hmcUser = hmcUserApi.getByUnionId(wxMpUser.getUnionId());
        DoctorAppInfoDTO doctor = doctorApi.getDoctorInfoByDoctorId(Integer.valueOf(paramMap.get("docId")));
        if (Objects.isNull(doctor)) {
            log.info("C端医带患消息,根据docId未获取到医生，paramMap:{}", JSONUtil.toJsonStr(paramMap));
            return false;
        }
        // 0、如果用户不为空->判断当前用户和当前活动医生是否绑定
        boolean checkFlag = false;
        if (Objects.nonNull(hmcUser)) {
            CheckUserDoctorRelRequest request = new CheckUserDoctorRelRequest();
            request.setDoctorId(Integer.valueOf(paramMap.get("docId")));
            request.setUserIdList(Collections.singletonList(hmcUser.getUserId().intValue()));
            checkFlag = doctorApi.checkUserDoctorRel(request);
        }
        // 1、未获取到C端用户 || 未绑定 -> 首次绑定,推送小程序绑定卡片
        if (Objects.isNull(hmcUser) || checkFlag) {
            try {
                // 1.1、推送欢迎语
                String content = "欢迎来到以岭健康管理中心 \r\n \r\n";
                content += "请点击\uD83D\uDC47\uD83D\uDC47下方卡片，完成医生绑定。";
                WxMpKefuMessage message1 = new WxMpKefuMessage();
                message1.setMsgType(WxConsts.KefuMsgType.TEXT);
                message1.setToUser(msg.getFromUser());
                message1.setContent(content);
                String pushResult1 = wxMpService.getKefuService().sendKefuMessageWithResponse(message1);
                log.info("发送客服消息结果：{}, ", pushResult1);

                // 1.2、推送小程序卡片
                String mediaId = wxMediaUtil.generateMediaPic(actDocPatientBgImg, doctor.getDoctorName(), doctor.getProfession());
                if (StrUtil.isBlank(mediaId)) {
                    log.error("创建素材图片出错，跳过处理");
                    return false;
                }
                String page = "pagesSub/main/patientAttend/index?actSo=2&docId=" + paramMap.get("docId");
                if (paramMap.containsKey("actId")) {
                    page += "&actId=" + paramMap.get("actId");
                }
                String title = "点击\uD83D\uDC47\uD83D\uDC47立即添加，方便后期线上沟通";
                WxMpKefuMessage message2 = WxMpKefuMessage.MINIPROGRAMPAGE().title(title).thumbMediaId(mediaId).appId(wxMaService.getWxMaConfig().getAppid()).pagePath(page).toUser(msg.getFromUser()).build();
                String pushResult2 = wxMpService.getKefuService().sendKefuMessageWithResponse(message2);
                log.info("发送客服消息结果：{}, ", pushResult2);
            } catch (WxErrorException e) {
                log.error("发送客服消息失败：{}", e.getMessage(), e);
            }
        } else {
            // 2、已绑定
            try {
                // 2.1、推送欢迎语
                String content = "欢迎来到以岭健康管理中心 \r\n \r\n";
                content += "您已扫码绑定过" + doctor.getDoctorName() + "医生，点击👇🏻👇🏻下方卡片可随时进行复诊咨询。";
                WxMpKefuMessage message1 = new WxMpKefuMessage();
                message1.setMsgType(WxConsts.KefuMsgType.TEXT);
                message1.setToUser(msg.getFromUser());
                message1.setContent(content);
                String pushResult1 = wxMpService.getKefuService().sendKefuMessageWithResponse(message1);
                log.info("发送客服消息结果：{}, ", pushResult1);

                // 2.2、推送医生卡片
                String mediaId = wxMediaUtil.generateMediaPic(diagnosisBgImg, doctor.getDoctorName(), doctor.getProfession(), doctor.getPicture());
                if (StrUtil.isBlank(mediaId)) {
                    log.error("创建素材图片出错，跳过处理");
                    return false;
                }
                String page = "/pagesSub/myDoctor/doctorDetail/index?docId=" + paramMap.get("docId");
                String title = "点击👇🏻👇🏻复诊咨询，可随时像我咨询";
                WxMpKefuMessage message2 = WxMpKefuMessage.MINIPROGRAMPAGE().title(title).thumbMediaId(mediaId).appId(wxMaService.getWxMaConfig().getAppid()).pagePath(page).toUser(msg.getFromUser()).build();
                String pushResult2 = wxMpService.getKefuService().sendKefuMessageWithResponse(message2);
                log.info("推送医生复诊卡片结果：{}, ", pushResult2);

            } catch (WxErrorException e) {
                log.error("发送客服消息失败：{}", e.getMessage(), e);
            }
        }
        return true;
    }

    /**
     * 健康包（药盒落地页弹窗）
     *
     * @param msg
     */
    private Boolean channelMarketBoxLandPageHandler(WxMpXmlMessage msg) {
        WxMsgHandlerContext context = null;
        switch (msg.getEvent()) {
            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
            case WxConstant.EVENT_TYPE_SCAN:
                context = buildContentMarketPackageBoxLandPage(msg);
                break;
            default:
                break;
        }

        if (Objects.isNull(context)) {
            log.info("消息上下文对象为空，跳过推送模板消息，发送客服消息结束");
            return false;
        }
        try {
            String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(context.getMessage());
            log.info("发送客服消息结果：{}, 参数：{}", pushResult, JSONUtil.toJsonStr(context));
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }
        // 获取公众号标签
        List<WxUserTag> wxUserTags = Lists.newArrayList();
        try {
            wxUserTags.addAll(wxMpService.getUserTagService().tagGet());
        } catch (WxErrorException e) {
            log.error("获取公众号标签列表失败：{}", e.getMessage(), e);
        }

        Map<String, String> valueMap = YlStrUtils.dealParam(context.getSceneValue());
        WxQrCodeTypeEnum typeEnum = WxQrCodeTypeEnum.getByType(Integer.valueOf(valueMap.get("qt")));
        if (Objects.isNull(typeEnum)) {
            log.error("未知二维码类型，参数:{}", JSONUtil.toJsonStr(context));
            return false;
        }

        log.info("二维码类型:{}", typeEnum);
        Optional<WxUserTag> wxUserTag = wxUserTags.stream().filter(item -> item.getName().equals(typeEnum.getName())).findFirst();
        WxUserTag userTag = null;
        if (wxUserTag.isPresent()) {
            userTag = wxUserTag.get();
        } else {
            // 如果未匹配到标签 -> 创建公众号标签
            try {
                userTag = wxMpService.getUserTagService().tagCreate(typeEnum.getName());
            } catch (WxErrorException e) {
                log.error("创建公众号标签失败：{}", e.getMessage(), e);
            }
        }
        if (Objects.isNull(userTag)) {
            log.info("创建或获取微信公众号标签失败，跳过公众号打标签逻辑");
            return false;
        }
        // 打标签
        try {
            boolean b = wxMpService.getUserTagService().batchTagging(userTag.getId(), new String[]{context.getOpenId()});
            log.info("给用户打标签完成,结果：{},openId:{}", b, context.getOpenId());
        } catch (WxErrorException e) {
            log.error("公众号用户打标签失败：{}", e.getMessage(), e);
        }

        return true;
    }

    /**
     * 市场健康包提示语
     *
     * @param msg
     * @return
     */
    private WxMsgHandlerContext buildContentMarketPackageBoxLandPage(WxMpXmlMessage msg) {

        String content = "欢迎关注以岭健康管理中心，您身边值得信赖的用药管家 \r\n \r\n";

        // 转盘抽奖
        content += "\uD83E\uDDE7 戳这里 <a href ='" + drawActivityUrl + " '>「转盘抽奖」</a> \r\n \r\n";

        // 健康自测
        String healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        content += "️️❗️️ 戳这里 <a href ='" + healthEvaluate + "'>「健康自测」</a> \r\n \r\n ";

        // 药鉴真伪
        content += "\uD83D\uDD0D 戳这里 <a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";

        content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";

        WxMsgHandlerContext context = new WxMsgHandlerContext();
        WxMpKefuMessage message = new WxMpKefuMessage();
        message.setMsgType(WxConsts.KefuMsgType.TEXT);
        message.setToUser(msg.getFromUser());
        // 如果开关打开 -> 走运营后台配置
        log.info("欢迎语开关配置:{}", greetingNewVersionFlag);
        if (greetingNewVersionFlag) {
            GzhGreetingDTO greetingDTO = gzhGreetingApi.getDetailBySceneId(Long.valueOf(HmcGzhSceneEnum.BOX_LAND_PAGE_QR_POP_UP.getType()));
            if (Objects.nonNull(greetingDTO) && StrUtil.isNotBlank(greetingDTO.getPublishVersion())) {
                log.info("后台设置药盒落地页弹框二维码欢迎语已发布，设置对象:{}", JSONUtil.toJsonStr(greetingDTO));
                message.setContent(greetingDTO.getPublishVersion());

                // 更新触发数量
                gzhGreetingApi.updateTriggerCount(greetingDTO.getId());

            }
        } else {
            message.setContent(content);
        }
        context.setMessage(message);
        context.setOpenId(msg.getFromUser());
        String qrValue = null;
        if (WxConstant.EVENT_TYPE_SUBSCRIBE.equals(msg.getEvent())) {
            qrValue = msg.getEventKey().substring(7);
        }
        if (WxConstant.EVENT_TYPE_SCAN.equals(msg.getEvent())) {
            qrValue = msg.getEventKey();
        }
        context.setSceneValue(qrValue);
        return context;
    }

    /**
     * 市场渠道消息处理器
     *
     * @param msg
     */
    private Boolean channelMarketPackageHandler(WxMpXmlMessage msg) {
        WxMsgHandlerContext context = null;
        switch (msg.getEvent()) {
            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
            case WxConstant.EVENT_TYPE_SCAN:
                context = buildContentMarketPackage(msg);
                break;
            default:
                break;
        }

        if (Objects.isNull(context)) {
            log.info("消息上下文对象为空，跳过推送模板消息，发送客服消息结束");
            return false;
        }
        try {
            String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(context.getMessage());
            log.info("发送客服消息结果：{}, 参数：{}", pushResult, JSONUtil.toJsonStr(context));
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }
        // 获取公众号标签
        List<WxUserTag> wxUserTags = Lists.newArrayList();
        try {
            wxUserTags.addAll(wxMpService.getUserTagService().tagGet());
        } catch (WxErrorException e) {
            log.error("获取公众号标签列表失败：{}", e.getMessage(), e);
        }

        Map<String, String> valueMap = YlStrUtils.dealParam(context.getSceneValue());
        WxQrCodeTypeEnum typeEnum = WxQrCodeTypeEnum.getByType(Integer.valueOf(valueMap.get("qt")));
        if (Objects.isNull(typeEnum)) {
            log.error("未知二维码类型，参数:{}", JSONUtil.toJsonStr(context));
            return false;
        }

        log.info("二维码类型:{}", typeEnum);
        Optional<WxUserTag> wxUserTag = wxUserTags.stream().filter(item -> item.getName().equals(typeEnum.getName())).findFirst();
        WxUserTag userTag = null;
        if (wxUserTag.isPresent()) {
            userTag = wxUserTag.get();
        } else {
            // 如果未匹配到标签 -> 创建公众号标签
            try {
                userTag = wxMpService.getUserTagService().tagCreate(typeEnum.getName());
            } catch (WxErrorException e) {
                log.error("创建公众号标签失败：{}", e.getMessage(), e);
            }
        }
        if (Objects.isNull(userTag)) {
            log.info("创建或获取微信公众号标签失败，跳过公众号打标签逻辑");
            return false;
        }
        // 打标签
        try {
            boolean b = wxMpService.getUserTagService().batchTagging(userTag.getId(), new String[]{context.getOpenId()});
            log.info("给用户打标签完成,结果：{},openId:{}", b, context.getOpenId());
        } catch (WxErrorException e) {
            log.error("公众号用户打标签失败：{}", e.getMessage(), e);
        }

        return true;
    }

    /**
     * 市场健康包提示语
     *
     * @param msg
     * @return
     */
    private WxMsgHandlerContext buildContentMarketPackage(WxMpXmlMessage msg) {

        String content = "欢迎关注以岭健康管理中心，您身边值得信赖的用药管家 \r\n \r\n";

        // 转盘抽奖
        content += "\uD83E\uDDE7 戳这里 <a href ='" + drawActivityUrl + " '>「转盘抽奖」</a> \r\n \r\n";

        // 健康自测
        String healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        content += "️️❗️️ 戳这里 <a href ='" + healthEvaluate + "'>「健康自测」</a> \r\n \r\n ";

        // 药鉴真伪
        content += "\uD83D\uDD0D 戳这里 <a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";

        content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";

        WxMsgHandlerContext context = new WxMsgHandlerContext();
        WxMpKefuMessage message = new WxMpKefuMessage();
        message.setMsgType(WxConsts.KefuMsgType.TEXT);
        message.setToUser(msg.getFromUser());

        // 如果开关打开 -> 走运营后台配置
        log.info("欢迎语开关配置:{}", greetingNewVersionFlag);
        if (greetingNewVersionFlag) {
            GzhGreetingDTO greetingDTO = gzhGreetingApi.getDetailBySceneId(Long.valueOf(HmcGzhSceneEnum.CHANNEL_QR_CODE.getType()));
            if (Objects.nonNull(greetingDTO) && StrUtil.isNotBlank(greetingDTO.getPublishVersion())) {
                log.info("后台设置市场健康包欢迎语已发布，设置对象:{}", JSONUtil.toJsonStr(greetingDTO));
                message.setContent(greetingDTO.getPublishVersion());

                // 更新触发数量
                gzhGreetingApi.updateTriggerCount(greetingDTO.getId());

            }
        } else {
            message.setContent(content);
        }

        message.setContent(content);
        context.setMessage(message);
        context.setOpenId(msg.getFromUser());
        String qrValue = null;
        if (WxConstant.EVENT_TYPE_SUBSCRIBE.equals(msg.getEvent())) {
            qrValue = msg.getEventKey().substring(7);
        }
        if (WxConstant.EVENT_TYPE_SCAN.equals(msg.getEvent())) {
            qrValue = msg.getEventKey();
        }
        context.setSceneValue(qrValue);
        return context;
    }

    /**
     * 通心络消息处理器
     *
     * @param msg
     */
    private Boolean txlWelfareMsgHandler(WxMpXmlMessage msg) {
        String pushMsg = null;
        switch (msg.getEvent()) {
            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
                pushMsg = WxKfTextMsgVO.getInstance(msg.getFromUser(), buildSubscribeContentTXL(msg));
                break;

            // 事件类型：扫描带参数二维码事件:用户已关注时的事件推送
            case WxConstant.EVENT_TYPE_SCAN:
                pushMsg = WxKfTextMsgVO.getInstance(msg.getFromUser(), buildScanContentTXL(msg));
                break;
            default:
                break;
        }

        if (StrUtil.isBlank(pushMsg)) {
            log.info("pushMsg为空，跳过推送模板消息，发送客服消息结束");
            return false;
        }
        try {
            String url = String.format(WxConstant.URL_CUSTOM_SEND_POST, wxMpService.getAccessToken());
            String pushResult = HttpUtil.post(url, pushMsg);
            log.info("发送客服消息结果：{}, 参数：{}", pushResult, pushMsg);
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * 构建提示语
     *
     * @param msg
     * @return
     */
    private String buildSubscribeContentTXL(WxMpXmlMessage msg) {
        String content = "终于等到您~ \r\n \r\n";
        String qrValue = msg.getEventKey().substring(8);
        String drugWelfare = "pagesSub/main/welfarePlan/index?qr=" + qrValue;
        content += "\uD83D\uDC49 戳我领取 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + drugWelfare + "'>「通心络用药福利」</a>";
        content += "最高减免11盒通心络用药费用 \r\n \r\n";
        return content;
    }

    /**
     * 构建提示语
     *
     * @param msg
     * @return
     */
    private String buildScanContentTXL(WxMpXmlMessage msg) {
        String content = "终于等到您~\r\n \r\n";
        String qrValue = msg.getEventKey();
        String drugWelfare = "pagesSub/main/welfarePlan/index?qr=" + qrValue;
        content += "\uD83D\uDC49 戳我领取 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + drugWelfare + "'>「通心络用药福利」</a>";
        content += "最高减免11盒通心络用药费用 \r\n \r\n";
        return content;
    }

    /**
     * 处理抽奖活动消息
     *
     * @param msg
     */
    private Boolean cLotteryActivityMsgHandler(WxMpXmlMessage msg) {
        WxMsgHandlerContext context = null;
        switch (msg.getEvent()) {
            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
                context = buildSubscribeContentLottery(msg);
                break;

            // 事件类型：扫描带参数二维码事件:用户已关注时的事件推送
            case WxConstant.EVENT_TYPE_SCAN:
                context = buildScanContentLottery(msg);
                break;
            default:
                break;
        }

        if (Objects.isNull(context)) {
            log.info("消息上下文对象为空，跳过推送模板消息，发送客服消息结束");
            return false;
        }
        try {
            String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(context.getMessage());
            log.info("发送客服消息结果：{}, 参数：{}", pushResult, JSONUtil.toJsonStr(context));
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }
        // 获取公众号标签
        List<WxUserTag> wxUserTags = Lists.newArrayList();
        try {
            wxUserTags.addAll(wxMpService.getUserTagService().tagGet());
        } catch (WxErrorException e) {
            log.error("获取公众号标签列表失败：{}", e.getMessage(), e);
        }

        LotteryActivityDetailBO activityDetail = lotteryActivityApi.get(context.getActivityId(), LotteryActivityPlatformEnum.ZGH);
        if (Objects.isNull(activityDetail)) {
            log.info("未获取到抽奖活动对象，跳过公众号打标签逻辑");
            return false;
        }
        Optional<WxUserTag> wxUserTag = wxUserTags.stream().filter(item -> item.getName().equals(activityDetail.getLotteryActivityBasic().getActivityName())).findFirst();
        WxUserTag userTag = null;
        if (wxUserTag.isPresent()) {
            userTag = wxUserTag.get();
        } else {
            // 如果未匹配到标签 -> 创建公众号标签
            try {
                userTag = wxMpService.getUserTagService().tagCreate(activityDetail.getLotteryActivityBasic().getActivityName());
            } catch (WxErrorException e) {
                log.error("创建公众号标签失败：{}", e.getMessage(), e);
            }
        }
        if (Objects.isNull(userTag)) {
            log.info("创建或获取微信公众号标签失败，跳过公众号打标签逻辑");
            return false;
        }
        // 打标签
        try {
            boolean b = wxMpService.getUserTagService().batchTagging(userTag.getId(), new String[]{context.getOpenId()});
            log.info("给用户打标签完成,结果：{},openId:{}.....", b, context.getOpenId());
        } catch (WxErrorException e) {
            log.error("公众号用户打标签失败：{}", e.getMessage(), e);
        }

        return true;
    }

    /**
     * 处理C端健康测评消息
     *
     * @param msg
     */
    private Boolean cHealthEvaluateMsgHandler(WxMpXmlMessage msg) {
        WxMsgHandlerContext context = null;
        switch (msg.getEvent()) {
            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
                context = buildSubscribeContentHealthEvaluate(msg);
                break;

            // 事件类型：扫描带参数二维码事件:用户已关注时的事件推送
            case WxConstant.EVENT_TYPE_SCAN:
                context = buildScanContentHealthEvaluate(msg);
                break;
            default:
                break;
        }

        if (Objects.isNull(context)) {
            log.info("消息上下文对象为空，跳过推送模板消息，发送客服消息结束");
            return false;
        }
        try {
            log.info("发送客服消息参数：{}", JSONUtil.toJsonStr(context));
            String pushResult = wxMpService.getKefuService().sendKefuMessageWithResponse(context.getMessage());
            log.info("发送客服消息结果：{}, ", pushResult);
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * 构建提示语
     * <p>
     * 健康测评二维码参数：qt:30_evaId:111_uId:222
     *
     * @param msg
     * @return
     */
    private WxMsgHandlerContext buildSubscribeContentHealthEvaluate(WxMpXmlMessage msg) {
        String content = "终于等到你！欢迎关注以岭健康管理中心 \r\n \r\n";
        String qrValue = msg.getEventKey().substring(8);
        Map<String, String> paramMap = YlStrUtils.dealParam(qrValue);
        String healthEvaluate;
        if (paramMap.containsKey("evaId")) {
            healthEvaluate = String.format(h5DomainProperties.getDomainUrl() + "/active/healthAssessment/list?courseId=%s", paramMap.get("evaId"));
        } else {
            healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        }
        content += "\uD83D\uDC49 <a href ='" + healthEvaluate + "'>「健康自测」</a> 戳这里免费参与\r\n";
        content += "\uD83D\uDC49 完成测评即可立即获取测评报告~更多测评进入专题页均可免费参与哦~ ";

        WxMsgHandlerContext context = new WxMsgHandlerContext();

        WxMpKefuMessage message = new WxMpKefuMessage();
        message.setMsgType(WxConsts.KefuMsgType.TEXT);
        message.setToUser(msg.getFromUser());
        message.setContent(content);

        context.setMessage(message);
        context.setSceneValue(qrValue);
        if (paramMap.containsKey("uId")) {
            context.setShareUserId(Long.valueOf(paramMap.get("uId")));
        }
        context.setOpenId(msg.getFromUser());

        return context;
    }

    /**
     * 构建提示语
     * <p>
     * 健康测评二维码参数：qt:30_evaId:111_uId:222
     *
     * @param msg
     * @return
     */
    private WxMsgHandlerContext buildScanContentHealthEvaluate(WxMpXmlMessage msg) {
        String content = "终于等到你！欢迎关注以岭健康管理中心 \r\n \r\n";
        String qrValue = msg.getEventKey();
        Map<String, String> paramMap = YlStrUtils.dealParam(qrValue);
        String healthEvaluate;
        if (paramMap.containsKey("evaId")) {
            healthEvaluate = String.format(h5DomainProperties.getDomainUrl() + "/active/healthAssessment/list?courseId=%s", paramMap.get("evaId"));
        } else {
            healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        }
        content += "\uD83D\uDC49 <a href ='" + healthEvaluate + "'>「健康自测」</a> 戳这里免费参与\r\n \r\n";
        content += "\uD83D\uDC49 完成测评即可立即获取测评报告~更多测评进入专题页均可免费参与哦~ ";

        WxMsgHandlerContext context = new WxMsgHandlerContext();

        WxMpKefuMessage message = new WxMpKefuMessage();
        message.setMsgType(WxConsts.KefuMsgType.TEXT);
        message.setToUser(msg.getFromUser());
        message.setContent(content);

        context.setMessage(message);
        context.setSceneValue(qrValue);
        if (paramMap.containsKey("uId")) {
            context.setShareUserId(Long.valueOf(paramMap.get("uId")));
        }
        context.setOpenId(msg.getFromUser());

        return context;
    }

    /**
     * 构建提示语
     * <p>
     * 抽奖二维码参数：actId:11_uId:22
     *
     * @param msg
     * @return
     */
    private WxMsgHandlerContext buildSubscribeContentLottery(WxMpXmlMessage msg) {
        String content = "终于等到你！欢迎关注以岭健康管理中心 \r\n \r\n";
        String qrValue = msg.getEventKey().substring(8);
        Map<String, String> paramMap = YlStrUtils.dealParam(qrValue);
        String lotteryActivity = String.format(h5DomainProperties.getDomainUrl() + "/active/luckyDraw/wechat?activeId=%s", paramMap.get("actId"));
        content += "\uD83D\uDC49 戳这里 <a href ='" + lotteryActivity + "'>「转盘抽奖」</a> \r\n \r\n";
        content += "\uD83D\uDC49 更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您赚到相关部门询问解答";

        WxMsgHandlerContext context = new WxMsgHandlerContext();

        WxMpKefuMessage message = new WxMpKefuMessage();
        message.setMsgType(WxConsts.KefuMsgType.TEXT);
        message.setToUser(msg.getFromUser());
        message.setContent(content);

        context.setMessage(message);
        context.setSceneValue(qrValue);
        if (paramMap.containsKey("uId")) {
            context.setShareUserId(Long.valueOf(paramMap.get("uId")));
        }
        context.setOpenId(msg.getFromUser());

        return context;
    }

    /**
     * 构建提示语
     * <p>
     * 抽奖二维码参数：actId:11_uId:22
     *
     * @param msg
     * @return
     */
    private WxMsgHandlerContext buildScanContentLottery(WxMpXmlMessage msg) {
        String content = "终于等到你！欢迎关注以岭健康管理中心 \r\n \r\n";
        String qrValue = msg.getEventKey();
        Map<String, String> paramMap = YlStrUtils.dealParam(qrValue);
        String lotteryActivity = String.format(h5DomainProperties.getDomainUrl() + "/active/luckyDraw/wechat?activeId=%s", paramMap.get("actId"));
        content += "\uD83D\uDC49 戳这里 <a href ='" + lotteryActivity + "'>「转盘抽奖」</a> \r\n \r\n";
        content += "\uD83D\uDC49 更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您赚到相关部门询问解答";

        WxMsgHandlerContext context = new WxMsgHandlerContext();

        WxMpKefuMessage message = new WxMpKefuMessage();
        message.setMsgType(WxConsts.KefuMsgType.TEXT);
        message.setToUser(msg.getFromUser());
        message.setContent(content);

        context.setMessage(message);
        context.setSceneValue(qrValue);
        context.setActivityId(Long.valueOf(paramMap.get("actId")));
        if (paramMap.containsKey("uId")) {
            context.setShareUserId(Long.valueOf(paramMap.get("uId")));
        }
        context.setOpenId(msg.getFromUser());

        return context;
    }


    /**
     * 用药福利消息处理器
     *
     * @param msg
     * @return
     */
    private boolean staffWelfareMsgHandler(WxMpXmlMessage msg) {
        String pushMsg = null;
        switch (msg.getEvent()) {

            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
                pushMsg = WxKfTextMsgVO.getInstance(msg.getFromUser(), buildSubscribeContentDrugWelfare(msg));
                break;

            // 事件类型：扫描带参数二维码事件:用户已关注时的事件推送
            case WxConstant.EVENT_TYPE_SCAN:
                pushMsg = WxKfTextMsgVO.getInstance(msg.getFromUser(), buildScanContentDrugWelfare(msg));
                break;

            default:
                break;
        }

        if (StrUtil.isBlank(pushMsg)) {
            log.info("pushMsg为空，跳过推送模板消息，发送客服消息结束");
            return true;
        }

        try {
            String url = String.format(WxConstant.URL_CUSTOM_SEND_POST, wxMpService.getAccessToken());
            String pushResult = HttpUtil.post(url, pushMsg);
            log.info("发送客服消息结果：{}, 参数：{},token:{}", pushResult, pushMsg, wxMpService.getAccessToken());
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * 药盒二维码消息处理器
     *
     * @param msg
     * @return
     */
    private boolean boxQrCodeMsgHandler(WxMpXmlMessage msg) {
        String pushMsg = null;
        switch (msg.getEvent()) {

            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
                pushMsg = WxKfTextMsgVO.getInstance(msg.getFromUser(), buildSubscribeContentBoxQrCode(msg));
                break;

            // 事件类型：扫描带参数二维码事件:用户已关注时的事件推送
            case WxConstant.EVENT_TYPE_SCAN:
                pushMsg = WxKfTextMsgVO.getInstance(msg.getFromUser(), buildScanContentBoxQrCode(msg));
                break;

            default:
                break;
        }

        if (StrUtil.isBlank(pushMsg)) {
            log.info("pushMsg为空，跳过推送模板消息，发送客服消息结束");
            return true;
        }

        try {
            String url = String.format(WxConstant.URL_CUSTOM_SEND_POST, wxMpService.getAccessToken());
            String pushResult = HttpUtil.post(url, pushMsg);
            log.info("发送客服消息结果：{}, 参数：{},token:{}", pushResult, pushMsg, wxMpService.getAccessToken());
        } catch (WxErrorException e) {
            log.error("发送客服消息失败：{}", e.getMessage(), e);
        }
        return true;
    }

    /**
     * 构建提示语
     * <p>
     * 店员二维码：so:2_sEId:220_sUId:13
     * 药盒二维码：so:3
     *
     * @param msg
     * @return
     */
    private String buildSubscribeContentDrugWelfare(WxMpXmlMessage msg) {
        String content = "欢迎关注以岭健康管理中心，您身边值得信赖的用药管家 \r\n \r\n";

        String qrValue = msg.getEventKey().substring(WxConstant.QR_SCENE.length());

        String drugWelfare = "pagesSub/main/drugWelfare/index?qr=" + qrValue;
        content += "\uD83D\uDC49 戳这里 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + drugWelfare + "'>「用药福利」</a> \r\n \r\n";

        content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";
        return content;

        // if (StrUtil.isNotBlank(msg.getEventKey()) && msg.getEventKey().startsWith(WxConstant.QR_SCENE)) {
        //     String qrValue = msg.getEventKey().substring(WxConstant.QR_SCENE.length());
        //     Map<String, String> qrMap = YlStrUtils.dealParam(qrValue);
        //     // 销售 + 店员
        //     if (qrMap.containsKey("so") && SubscribeSourceEnum.STAFF_SELLER.getType().equals(Integer.valueOf(qrMap.get("so")))) {
        //         String drugWelfare = "pagesSub/main/drugWelfare/index?qr=" + qrValue;
        //         content += "\uD83D\uDC49 戳这里 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + drugWelfare + "'>「用药福利」</a> \r\n \r\n";
        //     }
        //     // 药盒
        //     if (qrMap.containsKey("so") && SubscribeSourceEnum.BOX_CODE.getType().equals(Integer.valueOf(qrMap.get("so")))) {
        //         // 转盘抽奖
        //         content += "\uD83E\uDDE7 戳这里 <a href ='" + drawActivityUrl + " '>「转盘抽奖」</a> \r\n \r\n";
        //
        //         // 健康自测
        //         String healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        //         content += "️️❗️️ 戳这里<a href ='" + healthEvaluate + "'>「健康自测」</a> \r\n \r\n ";
        //
        //         // 药鉴真伪
        //         content += "\uD83D\uDD0D 戳这里 <a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";
        //     }
        // } else {
        //     // 暂时注释 用药福利 入口
        //     // String drugWelfare = "pagesSub/main/drugWelfare/index";
        //     // content += "\uD83D\uDC49 戳这里 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + drugWelfare + "'>「用药福利」</a> \r\n \r\n";
        //
        //     // 注释 0元福利入口
        //     // content += "\uD83D\uDC49 戳这里 <a href ='" + welfareUrl + " '>「0元福利」</a> \r\n \r\n";
        //
        //     // 转盘抽奖
        //     content += "\uD83E\uDDE7 戳这里 <a href ='" + drawActivityUrl + " '>「转盘抽奖」</a> \r\n \r\n";
        //
        //     // 健康自测
        //     String healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        //     content += "️️❗️️ 戳这里 <a href ='" + healthEvaluate + "'>「健康自测」</a> \r\n \r\n ";
        //
        //     // 药鉴真伪
        //     content += "\uD83D\uDD0D 戳这里 <a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";
        //
        // }
        // content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";
        // return content;
    }

    /**
     * 构建提示语
     * <p>
     * 药盒二维码：so:3
     *
     * @param msg
     * @return
     */
    private String buildSubscribeContentBoxQrCode(WxMpXmlMessage msg) {
        String content = "欢迎关注以岭健康管理中心，您身边值得信赖的用药管家 \r\n \r\n";

        // 转盘抽奖
        content += "\uD83E\uDDE7 戳这里 <a href ='" + drawActivityUrl + " '>「转盘抽奖」</a> \r\n \r\n";

        // 健康自测
        String healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        content += "️️❗️️ 戳这里<a href ='" + healthEvaluate + "'>「健康自测」</a> \r\n \r\n ";

        // 药鉴真伪
        content += "\uD83D\uDD0D 戳这里 <a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";

        content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";

        // 如果开关打开 -> 走运营后台配置
        log.info("欢迎语开关配置:{}", greetingNewVersionFlag);
        if (greetingNewVersionFlag) {
            GzhGreetingDTO greetingDTO = gzhGreetingApi.getDetailBySceneId(Long.valueOf(HmcGzhSceneEnum.BOX_LAND_PAGE_QR.getType()));
            if (Objects.nonNull(greetingDTO) && StrUtil.isNotBlank(greetingDTO.getPublishVersion())) {
                log.info("后台设置药盒二维码欢迎语已发布，设置对象:{}", JSONUtil.toJsonStr(greetingDTO));

                // 更新触发数量
                gzhGreetingApi.updateTriggerCount(greetingDTO.getId());

                return greetingDTO.getPublishVersion();
            }
        }

        return content;
    }

    /**
     * 构建提示语
     *
     * @param msg
     * @return
     */
    private String buildScanContentDrugWelfare(WxMpXmlMessage msg) {
        String content = "欢迎回到以岭健康管理中心 \r\n \r\n";

        String drugWelfare = "pagesSub/main/drugWelfare/index?qr=" + msg.getEventKey();
        content += "\uD83D\uDC49 戳这里 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + drugWelfare + "'>「用药福利」</a> \r\n \r\n";

        content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";
        return content;

        // if (StrUtil.isNotBlank(msg.getEventKey())) {
        //     Map<String, String> qrMap = YlStrUtils.dealParam(msg.getEventKey());
        //     // 销售 + 店员
        //     if (qrMap.containsKey("so") && SubscribeSourceEnum.STAFF_SELLER.getType().equals(Integer.valueOf(qrMap.get("so")))) {
        //         String drugWelfare = "pagesSub/main/drugWelfare/index?qr=" + msg.getEventKey();
        //         content += "\uD83D\uDC49 戳这里 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + drugWelfare + "'>「用药福利」</a> \r\n \r\n";
        //     }
        //     // 药盒
        //     if (qrMap.containsKey("so") && SubscribeSourceEnum.BOX_CODE.getType().equals(Integer.valueOf(qrMap.get("so")))) {
        //         // content += "\uD83D\uDC49 戳这里 <a href ='" + rabbitYearHappy + "'>「抢兔年大吉限量版红包封面」</a> \r\n \r\n";
        //
        //         String medsInstruct = "pagesSub/main/medicationList/medicationList";
        //         content += "\uD83E\uDDE7 戳这里 <a href ='' data-miniprogram-appid='" + wxMaService.getWxMaConfig().getAppid() + "' data-miniprogram-path='" + medsInstruct + "'>「用药指导」</a>\r\n \r\n";
        //
        //         content += "\uD83E\uDDE7 戳这里 <a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";
        //
        //     }
        // }
        // content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";
        // return content;
    }

    /**
     * 构建提示语
     * 药盒二维码
     *
     * @param msg
     * @return
     */
    private String buildScanContentBoxQrCode(WxMpXmlMessage msg) {
        String content = "欢迎回到以岭健康管理中心 \r\n \r\n";

        // 转盘抽奖
        content += "\uD83E\uDDE7 戳这里 <a href ='" + drawActivityUrl + " '>「转盘抽奖」</a> \r\n \r\n";

        // 健康自测
        String healthEvaluate = h5DomainProperties.getDomainUrl() + "/active/healthAssessment/";
        content += "️️❗️️ 戳这里<a href ='" + healthEvaluate + "'>「健康自测」</a> \r\n \r\n ";

        // 药鉴真伪
        content += "\uD83D\uDD0D 戳这里 <a href ='" + checkFake + "'>「药鉴真伪」</a> \r\n \r\n";

        content += "更多用药服务可点击下方菜单，如有任何问题可平台留言，小岭将帮助您转到相关部门询问解答";

        // 如果开关打开 -> 走运营后台配置
        log.info("欢迎语开关配置:{}", greetingNewVersionFlag);
        if (greetingNewVersionFlag) {
            GzhGreetingDTO greetingDTO = gzhGreetingApi.getDetailBySceneId(Long.valueOf(HmcGzhSceneEnum.BOX_LAND_PAGE_QR.getType()));
            if (Objects.nonNull(greetingDTO) && StrUtil.isNotBlank(greetingDTO.getPublishVersion())) {
                log.info("后台设置药盒二维码欢迎语已发布，设置对象:{}", JSONUtil.toJsonStr(greetingDTO));

                // 更新触发数量
                gzhGreetingApi.updateTriggerCount(greetingDTO.getId());

                return greetingDTO.getPublishVersion();
            }
        }
        return content;
    }

    /**
     * 处理用户信息
     *
     * @param msgBody
     */
    private void handleUserInfo(String msgBody) {

        log.info("处理用户信息开始....");

        WxMpXmlMessage msg = WxMpXmlMessage.fromXml(msgBody);

        // 非关注事件、取关事件
        if (!WxConstant.EVENT_TYPE_SUBSCRIBE.equalsIgnoreCase(msg.getEvent()) && !WxConstant.EVENT_TYPE_UNSUBSCRIBE.equalsIgnoreCase(msg.getEvent())) {
            log.info("非关注事件、取关事件,跳过处理用户信息");
            return;
        }
        String openId = msg.getFromUser();

        GzhUserDTO gzhUserDTO = gzhUserApi.getByGzhOpenId(msg.getFromUser());
        if (WxConstant.EVENT_TYPE_UNSUBSCRIBE.equalsIgnoreCase(msg.getEvent())) {

            if (Objects.isNull(gzhUserDTO)) {
                log.info("取消订阅事件，根据openId未获取到用户信息，参数：{}", msg.getFromUser());
                return;
            }

            // 清除
            String key15Day = RedisKey.generate("hmc", "gzh_15day", msg.getFromUser());
            redisService.del(key15Day);

            String subscribeKey = RedisKey.generate("hmc", "gzh_subscribe", gzhUserDTO.getGzhOpenId());
            redisService.del(subscribeKey);

            String interActKey = RedisKey.generate("hmc", "gzh_interact", msg.getFromUser());
            redisService.del(interActKey);


            // 更新订阅状态 -> 取消
            gzhUserDTO.setSubscribeStatus(SubscribeStatusEnum.UN_SUBSCRIBE.getType());
            gzhUserApi.updateGzhUser(gzhUserDTO);
            log.info("用户取消订阅处理完成，参数：{}", gzhUserDTO);
            return;

        }

        if (StrUtil.isBlank(openId)) {
            log.info("消息体 发送方帐号（一个OpenID）为空");
            return;
        }
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = wxMpService.getUserService().userInfo(openId);
        } catch (WxErrorException e) {
            log.error("获取公众号用户信息出错：{}", e.getMessage(), e);
        }
        if (Objects.isNull(wxMpUser) || StrUtil.isBlank(wxMpUser.getUnionId())) {
            log.info("unionId为空");
            return;
        }

        try {
            if (WxConstant.EVENT_TYPE_SUBSCRIBE.equalsIgnoreCase(msg.getEvent())) {
                if (Objects.isNull(gzhUserDTO)) {
                    // 创建用户
                    String eventKey = msg.getEventKey();
                    Integer subscribeSource = SubscribeSourceEnum.NATURE.getType();
                    CreateGzhUserRequest gzhUserRequest = CreateGzhUserRequest.builder().appId(wxMpService.getWxMpConfigStorage().getAppId()).gzhOpenId(openId).unionId(wxMpUser.getUnionId()).build();
                    if (StrUtil.isNotBlank(eventKey) && eventKey.startsWith(WxConstant.QR_SCENE)) {
                        Map<String, String> paramMap = YlStrUtils.dealParam(eventKey.substring(8));
                        if (paramMap.containsKey("so")) {
                            subscribeSource = Integer.parseInt(paramMap.get("so"));
                        }
                        gzhUserRequest.setQrValue(eventKey.substring(8));
                    }
                    gzhUserRequest.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE.getType());
                    gzhUserRequest.setSubscribeSource(subscribeSource);
                    gzhUserDTO = gzhUserApi.createGzhUser(gzhUserRequest);
                    log.info("创建公众号用户结果：{}", gzhUserDTO);
                } else {
                    // 更新订阅状态 -> 订阅
                    gzhUserDTO.setSubscribeStatus(SubscribeStatusEnum.SUBSCRIBE.getType());
                    gzhUserApi.updateGzhUser(gzhUserDTO);
                    log.info("用户再次订阅公众号处理完成，参数：{}", gzhUserDTO);
                }

                // 发送公众号延迟消息
                /** HMC关注公众号延迟mq */
                String TOPIC_HMC_GZH_SUBSCRIBE_DELAY = "topic_hmc_gzh_subscribe_delay";
                String TAG_HMC_GZH_SUBSCRIBE_DELAY = "tag_hmc_gzh_subscribe_delay";
                MqMessageBO gzhSubscribeDelay = new MqMessageBO(TOPIC_HMC_GZH_SUBSCRIBE_DELAY, TAG_HMC_GZH_SUBSCRIBE_DELAY, gzhUserDTO.getGzhOpenId(), MqDelayLevel.TEN_SECONDS);
                MqMessageBO prepare = mqMessageSendApi.prepare(gzhSubscribeDelay);
                mqMessageSendApi.send(prepare);
                log.info("关注之后发送延迟mq openId:{}", gzhUserDTO.getGzhOpenId());
            }

            // todo 保存操作记录

        } catch (Exception e) {
            log.error("处理公众号用户出错:{}", ExceptionUtils.getStackTrace(e));
        }

    }
}
