package com.yiling.hmc.gzh.api.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.model.ObjectMetadata;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.gzh.api.HmcGzhApi;
import com.yiling.hmc.gzh.enums.HmcGzhSubscribeStatusEnum;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * @author: fan.shen
 * @date: 2022-09-22
 */
@Slf4j
@DubboService
public class HmcGzhApiImpl implements HmcGzhApi {

    @DubboReference
    private HmcUserApi hmcUserApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    private GzhUserApi gzhUserApi;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    @Autowired
    FileService fileService;

    @Override
    public boolean hasUserSubscribeHmcGZH(Long userId) {

        HmcUser hmcUser = hmcUserApi.getById(userId);
        if (Objects.isNull(hmcUser)) {
            log.error("根据userID未查询到用户信息,userId：{}", userId);
            return false;
        }

        GzhUserDTO gzhUserDTO = gzhUserApi.getByUnionIdAndAppId(hmcUser.getUnionId(), wxMpService.getWxMpConfigStorage().getAppId());
        if (Objects.isNull(gzhUserDTO)) {
            log.info("根据unionId + appId 未获取到公众号用户信息，unionId:{}, appId:{}", hmcUser.getUnionId(), wxMpService.getWxMpConfigStorage().getAppId());
            return false;
        }

        if (gzhUserDTO.getSubscribeStatus().equals(HmcGzhSubscribeStatusEnum.SUBSCRIBE.getType())) {
            return true;
        }

        return false;
    }

    @Override
    public String createActivityShareImage(Long userId, Long activityId, String bgUrl) {

        // 1、获取用户信息
        UserDTO userDTO = userApi.getById(userId);
        log.info("[createActivityShareImage]获取用户信息结果:{}", JSONUtil.toJsonStr(userDTO));
        if (Objects.isNull(userDTO)) {
            log.error("[createActivityShareImage]未查询到用户信息,userId：{}", userId);
            return null;
        }

        String headImage = userDTO.getAvatarUrl();
        if (StrUtil.isBlank(headImage)) {
            log.info("[createActivityShareImage]用户头像为空信息：{}", JSONUtil.toJsonStr(userDTO));
            headImage = com.yiling.user.common.util.Constants.HZ_DEFAULT_AVATAR;
        }

        String sceneStr = String.format("qt:20_actId:%s_uId:%s", activityId, userId);
        log.info("[createActivityShareImage]sceneStr：{}", JSONUtil.toJsonStr(sceneStr));
        File qrFile = null;
        try {
            WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(sceneStr);
            qrFile = this.wxMpService.getQrcodeService().qrCodePicture(ticket);
            log.info("[createActivityShareImage]qrCodePicture：{}", JSONUtil.toJsonStr(qrFile));

        } catch (WxErrorException e) {
            log.error("[createActivityShareImage]生成微信二维码报错：{}", e.getMessage(), e);
        }
        if (Objects.isNull(qrFile)) {
            log.error("[createActivityShareImage]生成微信二维码qrFile为空");
            return null;
        }

        try {
            // 底图
//            URL url = new URL(Constants.LOTTERY_ACTIVITY_BACK);
            URL url = new URL(bgUrl);
            BufferedImage lotteryActivityBackImage = ImageIO.read(url.openStream());

            BufferedImage qrCodeImage = ImageIO.read(qrFile);

            URL avatarUrl = new URL(headImage);
            BufferedImage avatar = ImageIO.read(avatarUrl);

            // 底层空白 bufferedImage
            BufferedImage baseImage = new BufferedImage(lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

            // 画上图片
            log.info("[createActivityShareImage]画底图...");
            drawImgInImg(baseImage, lotteryActivityBackImage, 0, 0, lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight());

            // 画上公众号二维码
            drawImgInImg(baseImage, qrCodeImage, 680, 1513, 185, 185);

            // 画上用户头像
            drawImgInImg(baseImage, avatar, 100, 1530, 110, 110);

            // 用户昵称
            Color colorComm = new Color(60, 60, 60);

            drawTextInImg(baseImage, userDTO.getNickName(), 230, 1570, colorComm, 32);
            drawTextInImg(baseImage, "邀请您参与活动", 230, 1620, colorComm, 32);

            log.info("[createActivityShareImage]drawTextInImg,nickName：{},avatar:{}", userDTO.getNickName(), userDTO.getAvatarUrl());


            BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(baseImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "png", bs);

            byte[] bytes = bs.toByteArray();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(5) + Constants.FILE_SUFFIX, FileTypeEnum.LOTTERY_ACTIVITY_FILE, metadata);
            log.info("[createActivityShareImage]fileService.upload返回参数:{}", JSONUtil.toJsonStr(fileInfo));
            return fileInfo.getUrl();

        } catch (Exception e) {
            log.error("[createActivityShareImage]生成分享海报报错：{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String createEvaluateShareImage(Long userId, Long evaluateId, String bgUrl) {

        // 1、获取用户信息
        UserDTO userDTO = userApi.getById(userId);
        log.info("[createEvaluateShareImage]获取用户信息结果:{}", JSONUtil.toJsonStr(userDTO));
        if (Objects.isNull(userDTO)) {
            log.error("[createEvaluateShareImage]未查询到用户信息,userId：{}", userId);
            return null;
        }

        String headImage = userDTO.getAvatarUrl();
        if (StrUtil.isBlank(headImage)) {
            log.info("[createEvaluateShareImage]用户头像为空信息：{}", JSONUtil.toJsonStr(userDTO));
            headImage = com.yiling.user.common.util.Constants.HZ_DEFAULT_AVATAR;
        }

        String sceneStr = String.format("qt:30_evaId:%s_uId:%s", evaluateId, userId);
        log.info("[createEvaluateShareImage]sceneStr：{}", JSONUtil.toJsonStr(sceneStr));
        File qrFile = null;
        try {
            WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(sceneStr);
            qrFile = this.wxMpService.getQrcodeService().qrCodePicture(ticket);
            log.info("[createEvaluateShareImage]qrCodePicture：{}", JSONUtil.toJsonStr(qrFile));

        } catch (WxErrorException e) {
            log.error("[createEvaluateShareImage]生成微信二维码报错：{}", e.getMessage(), e);
        }
        if (Objects.isNull(qrFile)) {
            log.error("[createEvaluateShareImage]生成微信二维码qrFile为空");
            return null;
        }

        try {
            // 底图
            URL url = new URL(bgUrl);
            BufferedImage lotteryActivityBackImage = ImageIO.read(url.openStream());

            BufferedImage qrCodeImage = ImageIO.read(qrFile);

            URL avatarUrl = new URL(headImage);
            BufferedImage avatar = ImageIO.read(avatarUrl);

            // 底层空白 bufferedImage
            BufferedImage baseImage = new BufferedImage(lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

            // 画上图片
            log.info("[createEvaluateShareImage]画底图...");
            drawImgInImg(baseImage, lotteryActivityBackImage, 0, 0, lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight());

            // 画上公众号二维码
            drawImgInImg(baseImage, qrCodeImage, 680, 1513, 185, 185);

            // 画上用户头像
            drawImgInImg(baseImage, avatar, 100, 1530, 110, 110);

            // 用户昵称
            Color colorComm = new Color(60, 60, 60);

            drawTextInImg(baseImage, userDTO.getNickName(), 230, 1570, colorComm, 32);
            drawTextInImg(baseImage, "邀请您参与活动", 230, 1620, colorComm, 32);

            log.info("[createEvaluateShareImage]drawTextInImg,nickName：{},avatar:{}", userDTO.getNickName(), userDTO.getAvatarUrl());


            BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(baseImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "png", bs);

            byte[] bytes = bs.toByteArray();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(5) + Constants.FILE_SUFFIX, FileTypeEnum.LOTTERY_ACTIVITY_FILE, metadata);
            log.info("[createEvaluateShareImage]fileService.upload返回参数:{}", JSONUtil.toJsonStr(fileInfo));
            return fileInfo.getUrl();

        } catch (Exception e) {
            log.error("[createEvaluateShareImage]生成分享海报报错：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 写字
     *
     * @param baseImage
     * @param textToWrite
     * @param x
     * @param y
     * @param color
     */
    private void drawTextInImg(BufferedImage baseImage, String textToWrite, int x, int y, Color color, int fontSize) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.setColor(color);
        g2D.setFont(new Font("Microsoft YaHei", Font.PLAIN, fontSize));
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.drawString(textToWrite, x, y);
        g2D.dispose();
    }

    /**
     * 画图
     *
     * @param baseImage
     * @param imageToWrite
     * @param x
     * @param y
     * @param width
     * @param height
     */
    private void drawImgInImg(BufferedImage baseImage, BufferedImage imageToWrite, int x, int y, int width, int height) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.drawImage(imageToWrite, x, y, width, height, null);
        g2D.dispose();
    }
}
