package com.yiling.hmc.remind.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.config.WxTemplateConfig;
import com.yiling.hmc.remind.dao.MedsRemindSubscribeMapper;
import com.yiling.hmc.remind.dto.request.MedsRemindSubscribeRequest;
import com.yiling.hmc.remind.entity.MedsRemindSubscribeDO;
import com.yiling.hmc.remind.enums.HmcRemindSubscribeEnum;
import com.yiling.hmc.remind.service.MedsRemindSubscribeService;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 用药提醒消息订阅表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Service
@Slf4j
@RefreshScope
public class MedsRemindSubscribeServiceImpl extends BaseServiceImpl<MedsRemindSubscribeMapper, MedsRemindSubscribeDO> implements MedsRemindSubscribeService {

    @Autowired
    WxTemplateConfig templateConfig;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    GzhUserApi gzhUserApi;

    /**
     * 小程序服务类
     */
    @Autowired
    WxMaService wxMaService;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    public MedsRemindSubscribeDO get(String appId, Long userId, String templateId, Integer subscribeStatus) {
        QueryWrapper<MedsRemindSubscribeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(MedsRemindSubscribeDO::getAppId, appId)
                .eq(MedsRemindSubscribeDO::getUserId, userId)
                .eq(MedsRemindSubscribeDO::getTemplateId, templateId)
                .last("limit 1");
        MedsRemindSubscribeDO subscribeDO = this.getOne(queryWrapper);
        return subscribeDO;
    }

    @Override
    public void saveOrUpdateRemindSub(List<MedsRemindSubscribeRequest> requestList) {

        requestList.forEach(item -> {

            MedsRemindSubscribeDO medsRemindSubscribeDO = get(item.getAppId(), item.getUserId(), item.getTemplateId(), item.getSubscribeStatus());

            if (Objects.nonNull(medsRemindSubscribeDO)) {
                if (medsRemindSubscribeDO.getSubscribeStatus().equals(item.getSubscribeStatus())) {
                    log.info("当前订阅状态一致，跳过修改：{}", medsRemindSubscribeDO);
                    return;
                }
                log.info("存在订阅记录，修改订阅状态：{}", medsRemindSubscribeDO);
                medsRemindSubscribeDO.setSubscribeStatus(item.getSubscribeStatus());
                this.saveOrUpdate(medsRemindSubscribeDO);

            } else {
                MedsRemindSubscribeDO subscribeDO = PojoUtils.map(item, MedsRemindSubscribeDO.class);
                log.info("准备新增订阅数据：{}", subscribeDO);
                this.save(subscribeDO);
            }
//
//            // 接受
//            if (HmcRemindSubscribeEnum.ACCEPT.getType().equals(item.getSubscribeStatus())) {
//
//                if (Objects.nonNull(medsRemindSubscribeDO) && HmcRemindSubscribeEnum.ACCEPT.getType().equals(medsRemindSubscribeDO.getSubscribeStatus())) {
//                    log.info("当前数据在数据库已存在，跳过处理：{}", medsRemindSubscribeDO);
//                    return;
//                }
//                MedsRemindSubscribeDO subscribeDO = PojoUtils.map(item, MedsRemindSubscribeDO.class);
//                log.info("准备保存订阅数据：{}", subscribeDO);
//                this.save(subscribeDO);
//
//            }
//
//            // 拒绝
//            if (HmcRemindSubscribeEnum.REJECT.getType().equals(item.getSubscribeStatus())) {
//                if (Objects.isNull(medsRemindSubscribeDO)) {
//                    log.info("当前数据在数据库不存在，准备创建新数据");
//                    medsRemindSubscribeDO = PojoUtils.map(item, MedsRemindSubscribeDO.class);
//                } else {
//                    medsRemindSubscribeDO.setSubscribeStatus(HmcRemindSubscribeEnum.REJECT.getType());
//                }
//                log.info("准备更新订阅状态，目标数据：{}", medsRemindSubscribeDO);
//                this.saveOrUpdate(medsRemindSubscribeDO);
//
//            }

        });

    }

    @Override
    public MedsRemindSubscribeDO getByUserIdAndTemplateId(Long receiveUserId, String templateId) {
        QueryWrapper<MedsRemindSubscribeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(MedsRemindSubscribeDO::getUserId, receiveUserId)
                .eq(MedsRemindSubscribeDO::getTemplateId, templateId)
                .eq(MedsRemindSubscribeDO::getSubscribeStatus, HmcRemindSubscribeEnum.REJECT.getType())
                .orderByDesc(MedsRemindSubscribeDO::getUpdateTime)
                .last("limit 1");
        MedsRemindSubscribeDO subscribeDO = this.getOne(queryWrapper);
        return subscribeDO;
    }

    /**
     * "subscriptionsSetting": {
     * "mainSwitch": true,
     * "itemSettings": {
     * "1jIALWH4x5n1K1EfRhYushGEh7vF8NVZRYKsp1o3hzE": "accept",
     * "ouA-VoT7Czi-KZVxMtc_4NaRgYAS8v27HxJVQcYYyg4": "reject"
     * },
     * "1jIALWH4x5n1K1EfRhYushGEh7vF8NVZRYKsp1o3hzE": "accept",
     * "ouA-VoT7Czi-KZVxMtc_4NaRgYAS8v27HxJVQcYYyg4": "reject"
     * }
     *
     * @param currentUserId
     * @param subscribeStr
     * @return
     */
    @Override
    public boolean updateMedsRemindSubscribe(Long currentUserId, String subscribeStr) {
        if (StrUtil.isBlank(subscribeStr)) {
            log.info("[updateMedsRemindSubscribe]更新订阅状态,入参为空，跳过处理");
            return Boolean.FALSE;
        }

        // 转json对象
        JSONObject jsonObject = JSONUtil.parseObj(subscribeStr);
        if (!jsonObject.containsKey("errMsg")) {
            log.info("[updateMedsRemindSubscribe]更新订阅状态,不包含errMsg属性，跳过处理");
            return Boolean.FALSE;
        }

        if (!jsonObject.getStr("errMsg").contains("ok")) {
            log.info("[updateMedsRemindSubscribe]更新订阅状态,获取errMsg属性值不包含ok，跳过处理");
            return Boolean.FALSE;
        }

        if (!jsonObject.containsKey("subscriptionsSetting")) {
            log.info("[updateMedsRemindSubscribe]更新订阅状态,不包含subscriptionsSetting属性，跳过处理");
            return Boolean.FALSE;
        }

        JSONObject subscriptionsSetting = jsonObject.getJSONObject("subscriptionsSetting");
        if (Objects.isNull(subscriptionsSetting)) {
            log.info("[updateMedsRemindSubscribe]更新订阅状态,获取subscriptionsSetting属性为空，跳过处理");
            return Boolean.FALSE;
        }

        if (!subscriptionsSetting.containsKey("mainSwitch")) {
            log.info("[updateMedsRemindSubscribe]更新订阅状态,不包含mainSwitch属性，跳过处理");
            return Boolean.FALSE;
        }

        if (!subscriptionsSetting.getBool("mainSwitch")) {
            log.info("[updateMedsRemindSubscribe]更新订阅状态,mainSwitch未开启，跳过处理");
            return Boolean.FALSE;
        }

        HmcUser hmcUser = hmcUserApi.getByIdAndAppId(currentUserId, wxMaService.getWxMaConfig().getAppid());
        if (Objects.isNull(hmcUser)) {
            log.info("[updateMedsRemindSubscribe]根据用户id未获取到用户信息，跳过处理");
            return Boolean.FALSE;
        }

        List<MedsRemindSubscribeRequest> requestList = Lists.newArrayList();

        // 1、用药提醒
        if (subscriptionsSetting.containsKey(templateConfig.getMedsReminder())) {
            MedsRemindSubscribeRequest request = new MedsRemindSubscribeRequest();
            request.setAppId(wxMaService.getWxMaConfig().getAppid());
            request.setOpenId(hmcUser.getMiniProgramOpenId());
            request.setTemplateId(templateConfig.getMedsReminder());
            request.setUserId(hmcUser.getUserId());
            int subscribeStatus = 0;
            if (subscriptionsSetting.getStr(templateConfig.getMedsReminder()).contains(HmcRemindSubscribeEnum.ACCEPT.getName())) {
                subscribeStatus = HmcRemindSubscribeEnum.ACCEPT.getType();
            }
            if (subscriptionsSetting.getStr(templateConfig.getMedsReminder()).contains(HmcRemindSubscribeEnum.REJECT.getName())) {
                subscribeStatus = HmcRemindSubscribeEnum.REJECT.getType();
            }
            request.setSubscribeStatus(subscribeStatus);
            requestList.add(request);
        }

        // 2、八子补肾
        if (subscriptionsSetting.containsKey(templateConfig.getBaZiBuShenDeliverMsgTemplate())) {
            MedsRemindSubscribeRequest request = new MedsRemindSubscribeRequest();
            request.setAppId(wxMaService.getWxMaConfig().getAppid());
            request.setOpenId(hmcUser.getMiniProgramOpenId());
            request.setTemplateId(templateConfig.getBaZiBuShenDeliverMsgTemplate());
            request.setUserId(hmcUser.getUserId());
            int subscribeStatus = 0;
            if (subscriptionsSetting.getStr(templateConfig.getBaZiBuShenDeliverMsgTemplate()).contains(HmcRemindSubscribeEnum.ACCEPT.getName())) {
                subscribeStatus = HmcRemindSubscribeEnum.ACCEPT.getType();
            }
            if (subscriptionsSetting.getStr(templateConfig.getBaZiBuShenDeliverMsgTemplate()).contains(HmcRemindSubscribeEnum.REJECT.getName())) {
                subscribeStatus = HmcRemindSubscribeEnum.REJECT.getType();
            }
            request.setSubscribeStatus(subscribeStatus);
            requestList.add(request);
        }

        if (CollUtil.isEmpty(requestList)) {
            log.info("[updateMedsRemindSubscribe]无符合要求的数据，跳过处理");
            return Boolean.FALSE;
        }

        saveOrUpdateRemindSub(requestList);

        return true;
    }

    @Override
    public void midAutumnFestivalPushWxTemplateMsg() {
        QueryPageListRequest request = new QueryPageListRequest();
        request.setCurrent(1);
        request.setSize(500);
        Page<GzhUserDTO> page = gzhUserApi.pageList(request);
        long total = page.getTotal();
        if (total <= 0) {
            log.info("totalCount小于0，跳过处理");
            return;
        }
        while (CollUtil.isNotEmpty(page.getRecords())) {
            log.info(">>>>>>准备发送模板消息，当前待发送条数：{}", page.getRecords().size());
            page.getRecords().forEach(gzhUserDTO -> sendMsg(gzhUserDTO.getGzhOpenId()));
            request.setCurrent(request.getCurrent() + 1);
            page = gzhUserApi.pageList(request);
        }
    }

    @Override
    public void fatherFestivalPushWxTemplateMsg() {
        if (StrUtil.isBlank(templateConfig.getUserIdList())) {
            QueryPageListRequest request = new QueryPageListRequest();
            request.setCurrent(1);
            request.setSize(500);
            Page<GzhUserDTO> page = gzhUserApi.pageList(request);
            long total = page.getTotal();
            if (total <= 0) {
                log.info("totalCount小于0，跳过处理");
                return;
            }
            while (CollUtil.isNotEmpty(page.getRecords())) {
                log.info(">>>>>>准备发送模板消息，当前待发送条数：{}", page.getRecords().size());
                page.getRecords().forEach(gzhUserDTO -> sendFatherMsg(gzhUserDTO.getGzhOpenId()));
                request.setCurrent(request.getCurrent() + 1);
                page = gzhUserApi.pageList(request);
            }
        } else {
            String[] split = templateConfig.getUserIdList().split("\\|");
            List<Long> userIdList = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
            log.info("测试发送用户参数：{}", JSONUtil.toJsonStr(userIdList));
            List<GzhUserDTO> userDTOList = gzhUserApi.getByIdList(userIdList);
            log.info("测试发送用户列表：{}", JSONUtil.toJsonStr(userDTOList));
            if(CollUtil.isEmpty(userDTOList)) {
                log.info("为获取到目标用户，跳过处理...");
                return;
            }
            userDTOList.forEach(gzhUserDTO -> sendFatherMsg(gzhUserDTO.getGzhOpenId()));
        }


    }

    /**
     * 推送消息
     *
     * @param toUser
     */
    public void sendFatherMsg(String toUser) {
        WxMpTemplateMessage msg = new WxMpTemplateMessage();
        msg.setTemplateId(templateConfig.getFatherFestivalMsgTemplate());
        msg.setToUser(toUser);
        List<WxMpTemplateData> list = new ArrayList<>();
        WxMpTemplateData e1 = new WxMpTemplateData();
        e1.setName("first");
        e1.setValue("您好，父亲节活动正式开启");
        WxMpTemplateData e2 = new WxMpTemplateData();
        e2.setName("keyword1");
        e2.setValue("9.9元购原价558元「以岭八子补肾胶囊」");
        WxMpTemplateData e3 = new WxMpTemplateData();
        e3.setName("keyword2");
        e3.setValue("报名成功，点击抢购");
        WxMpTemplateData e4 = new WxMpTemplateData();
        e4.setName("keyword3");
        e4.setValue("6月16日 15:00");
        WxMpTemplateData e5 = new WxMpTemplateData();
        e5.setName("remark");
        e5.setValue("数量有限，点击抢购");
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        msg.setData(list);

        WxMpTemplateMessage.MiniProgram miniprogram = new WxMpTemplateMessage.MiniProgram();
        miniprogram.setAppid("wxaffb925297ef4578");
        miniprogram.setPagePath("src/pages/live/other/live-detail/index?id=7336");
        msg.setMiniProgram(miniprogram);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(msg);
        } catch (WxErrorException e) {
            log.error("推送中秋活动模板消息报错，取消关注报错可忽略:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 推送消息
     *
     * @param toUser
     */
    public void sendMsg(String toUser) {
        String toUrl = "https://mp.weixin.qq.com/s?__biz=MzkwMjMyNjY4OQ==&mid=2247486639&idx=1&sn=9a4c946cd165c729ac696d33110d5f10&chksm=c0a67c61f7d1f5771ab67a3fb53595ebd685717370031a8b9391b710b3aa75425d21a78cab31#rd";
        WxMpTemplateMessage msg = new WxMpTemplateMessage();
        msg.setTemplateId(templateConfig.getMidAutumnFestivalTemplateId());
        msg.setUrl(toUrl);
        msg.setToUser(toUser);
        List<WxMpTemplateData> list = new ArrayList<>();
        WxMpTemplateData e1 = new WxMpTemplateData();
        e1.setName("first");
        e1.setValue("您有1个「健康盲盒」待领取:");
        WxMpTemplateData e2 = new WxMpTemplateData();
        e2.setName("keyword1");
        e2.setValue("以岭健康管理中心");
        WxMpTemplateData e3 = new WxMpTemplateData();
        e3.setName("keyword2");
        e3.setValue("连花福利项目");
        WxMpTemplateData e4 = new WxMpTemplateData();
        e4.setName("keyword3");
        e4.setValue("福利待领取");
        WxMpTemplateData e5 = new WxMpTemplateData();
        e5.setName("keyword4");
        e5.setValue("参与福利");
        WxMpTemplateData e6 = new WxMpTemplateData();
        e6.setName("remark");
        e6.setValue("点击查看福利详情");
        list.add(e1);
        list.add(e2);
        list.add(e3);
        list.add(e4);
        list.add(e5);
        list.add(e6);
        msg.setData(list);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(msg);
        } catch (WxErrorException e) {
            log.error("推送中秋活动模板消息报错，取消关注报错可忽略:{}", ExceptionUtils.getStackTrace(e));
        }
    }
}

