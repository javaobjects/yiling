package com.yiling.hmc.gzh;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.collect.Lists;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.gzh.api.HmcGzhApi;
import com.yiling.hmc.gzh.dto.ButtonDTO;
import com.yiling.hmc.gzh.dto.Menu;
import com.yiling.hmc.gzh.dto.SubButtonDTO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shichen
 * @类名 GoodsServiceTest
 * @描述
 * @创建时间 2022/4/2
 * @修改人 shichen
 * @修改时间 2022/4/2
 **/
@Slf4j
public class HmcGzhTest extends BaseTest {

    @DubboReference
    HmcGzhApi hmcGzhApi;

    @Autowired
    FileService fileService;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    @Autowired
    RedisService redisService;

    String menuJSON = "{\"button\":[{\"name\":\"健康福利\",\"sub_button\":[{\"type\":\"view\",\"name\":\"慢病关爱计划\",\"url\":\"https://www.wjx.cn/vm/QaB10oo.aspx#\",\"status\":\"1\"},{\"type\":\"view\",\"name\":\"\uD83D\uDD25转盘抽奖\",\"url\":\"https://h.59yi.com/#/active/luckyDraw/wechat?activeId=38\",\"status\":\"1\"}],\"status\":\"1\"},{\"name\":\"健康服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"\uD83D\uDD25健康自测\",\"url\":\"https://h.59yi.com/#/active/healthAssessment/\",\"status\":\"1\"},{\"type\":\"miniprogram\",\"name\":\"在线问诊\",\"url\":\"https://www.ylyy.org/\",\"appid\":\"wx19d5d0fb2b273808\",\"pagepath\":\"views/router\",\"status\":\"1\"},{\"type\":\"view\",\"name\":\"健康商城\",\"url\":\"https://j.youzan.com/g49Wn7\",\"status\":\"1\"},{\"type\":\"view\",\"name\":\"鉴别真伪\",\"url\":\"https://mp.weixin.qq.com/s?__biz=MzkwMjMyNjY4OQ==&mid=2247486615&idx=1&sn=b573e1c45334c88bfa4c0099630d6760&chksm=c0a67c59f7d1f54fcb4dfa1ff3bbb81b48af0668727c896572e7cea18fee879f02fcf14850f4#rd\",\"status\":\"1\"},{\"type\":\"miniprogram\",\"name\":\"用药提醒\",\"url\":\"https://hmc-api.59yi.com/\",\"appid\":\"wxc439962ebf26b00a\",\"pagepath\":\"pages/manager/index\",\"status\":\"1\"}],\"status\":\"1\"},{\"type\":\"miniprogram\",\"name\":\"个人中心\",\"url\":\"https://h.59yi.com/#/\",\"appid\":\"wxc439962ebf26b00a\",\"pagepath\":\"pages/my/index\",\"status\":\"1\"}]}";

    @Test
    public void getMenuFromRedis() {
        String key = RedisKey.generate("hmc", "gzh_menu");
        log.info("key:{}", key);
        redisService.set(key, menuJSON);

        String menu = redisService.get(key).toString();
        Menu bean = JSONUtil.toBean(menu, Menu.class);
        log.info("menu bean:{}", JSONUtil.toJsonPrettyStr(bean));
        List<ButtonDTO> buttonDTOList = bean.getButton().stream().filter(item -> item.getStatus().equals("1")).collect(Collectors.toList());
        WxMenu wxMenu = new WxMenu();
        List<WxMenuButton> buttons = new ArrayList<>();
        buttonDTOList.forEach(buttonDTO -> {
            if (buttonDTO.getStatus().equals("1")) {
                WxMenuButton wxMenuButton = new WxMenuButton();
                wxMenuButton.setName(buttonDTO.getName());
                if (CollUtil.isEmpty(buttonDTO.getSubButton())) {
                    wxMenuButton.setType(buttonDTO.getType());
                    wxMenuButton.setUrl(buttonDTO.getUrl());
                    if (StrUtil.isNotBlank(buttonDTO.getAppid())) {
                        wxMenuButton.setAppId(buttonDTO.getAppid());
                    }
                    if (StrUtil.isNotBlank(buttonDTO.getPagepath())) {
                        wxMenuButton.setPagePath(buttonDTO.getPagepath());
                    }
                } else {
                    List<SubButtonDTO> subButtonDTOS = buttonDTO.getSubButton().stream().filter(subButtonDTO -> subButtonDTO.getStatus().equals("1")).collect(Collectors.toList());
                    List<WxMenuButton> wxSubButtons = Lists.newArrayList();
                    subButtonDTOS.forEach(subButtonDTO -> {
                        WxMenuButton wxMenuSubButton = new WxMenuButton();
                        wxMenuSubButton.setType(subButtonDTO.getType());
                        wxMenuSubButton.setName(subButtonDTO.getName());
                        wxMenuSubButton.setUrl(subButtonDTO.getUrl());
                        if (StrUtil.isNotBlank(subButtonDTO.getAppid())) {
                            wxMenuSubButton.setAppId(subButtonDTO.getAppid());
                        }
                        if (StrUtil.isNotBlank(subButtonDTO.getPagepath())) {
                            wxMenuSubButton.setPagePath(subButtonDTO.getPagepath());
                        }
                        wxSubButtons.add(wxMenuSubButton);
                    });
                    wxMenuButton.setSubButtons(wxSubButtons);
                }
                buttons.add(wxMenuButton);
            }
        });

        try {
            wxMenu.setButtons(buttons);
            log.info("ready to create menu：{}", JSONUtil.toJsonPrettyStr(wxMenu));
            wxMpService.getMenuService().menuCreate(wxMenu);
        } catch (WxErrorException e) {
            log.error("menuCreate error:{}", e.getMessage(), e);
        }
    }

    @Test
    public void testUpload() {
        // {"type":"image","mediaId":"iXEnMVQ6q7QQDGgJA3xrzgscGhpRg_EZh7TgvWyZykwy6RDPQFrh3M8nl719LSBT","createdAt":1675128237}
        File file = new File("/Users/shenfan/Downloads/ef094f6df6ae4d478e0011cbb6b0383e.png");
        try {
            WxMediaUploadResult res = wxMpService.getMaterialService().mediaUpload(WxConsts.MaterialType.IMAGE, file);
            System.out.println(res);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testBg() {

        String bgUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/kapian-bg.png";
        try {
            URL url = new URL(bgUrl);
            BufferedImage lotteryActivityBackImage = ImageIO.read(url.openStream());

            BufferedImage baseImage = new BufferedImage(lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

            drawImgInImg(baseImage, lotteryActivityBackImage, 0, 0, lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight());

            drawTextInImgCenter(baseImage, "张三丰  主任医师阿df阿萨德法师打发是到付啊实打实的发送到分", 240, Color.white, 24);
            // drawTextInImg(baseImage, "主任医师", 250, 240, Color.white, 24);


            BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(baseImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "png", bs);

            byte[] bytes = bs.toByteArray();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(5) + Constants.FILE_SUFFIX, FileTypeEnum.LOTTERY_ACTIVITY_FILE, metadata);

            log.info("[createEvaluateShareImage]fileService.upload返回参数:{}", JSONUtil.toJsonStr(fileInfo));
            System.out.println(fileInfo.getUrl());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            WxMediaUploadResult res = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "png", inputStream);
            System.out.println(res);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testDiagnosisBg() {

        // String bgUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/kapian-bg.png";

        String bgUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/diagnosis_bg.png";

        String Pic34 = "https://qnpic.ylhlwyy.com/FhSaK_MvqfTIXM7ITYsACC79sQYl";
        String Pic15 = "https://qnpic.ylhlwyy.com/FhK5g-IMgRSA9gK8g7-5y1aXQM8r";
        String Pic399 = "https://qnpic.ylhlwyy.com/1681544350631.797";

        String headPicUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/docHead.png";
        try {
            URL url = new URL(bgUrl);
            URL headPic = new URL(Pic399);

            BufferedImage backImage = ImageIO.read(url.openStream());
            BufferedImage headImage = ImageIO.read(headPic.openStream());

            BufferedImage baseImage = new BufferedImage(backImage.getWidth(), backImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

            drawHeadPicInImg(baseImage, headImage);

            drawImgInImg(baseImage, backImage, 0, 0, backImage.getWidth(), backImage.getHeight());

            drawTextInImgCenter(baseImage, "张三丰  主任医师阿df阿萨德法师打发是到付啊实打实的发送到分", 240, Color.white, 24);
            // drawTextInImg(baseImage, "主任医师", 250, 240, Color.white, 24);


            BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(baseImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "png", bs);

            byte[] bytes = bs.toByteArray();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(5) + Constants.FILE_SUFFIX, FileTypeEnum.LOTTERY_ACTIVITY_FILE, metadata);

            log.info("[createEvaluateShareImage]fileService.upload返回参数:{}", JSONUtil.toJsonStr(fileInfo));
            System.out.println(fileInfo.getUrl());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            WxMediaUploadResult res = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "png", inputStream);
            System.out.println(res);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 写字
     *
     * @param baseImage
     * @param textToWrite
     * @param y
     * @param color
     */
    public static void drawTextInImgCenter(BufferedImage baseImage, String textToWrite, int y, Color color, int fontSize) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.setColor(color);
        Font font = new Font("Microsoft YaHei", Font.PLAIN, fontSize);
        g2D.setFont(font);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (textToWrite.length() > 14) {
            textToWrite = textToWrite.substring(0, 14);
        }

        FontMetrics fontMetrics = g2D.getFontMetrics(font);
        int stringWidth = fontMetrics.stringWidth(textToWrite);
        int x = (baseImage.getWidth() - stringWidth) / 2;

        g2D.drawString(textToWrite, x, y);
        g2D.dispose();
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
    public static void drawTextInImg(BufferedImage baseImage, String textToWrite, int x, int y, Color color, int fontSize) {
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
    public static void drawImgInImg(BufferedImage baseImage, BufferedImage imageToWrite, int x, int y, int width, int height) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        g2D.drawImage(imageToWrite, x, y, width, height, null);
        g2D.dispose();
    }

    /**
     * 画图
     *
     * @param baseImage
     * @param imageToWrite
     */
    public static void drawHeadPicInImg(BufferedImage baseImage, BufferedImage imageToWrite) {
        Graphics2D g2D = (Graphics2D) baseImage.getGraphics();
        int x;
        int y = 60;
        int width = 140;
        int height = 140;
        x = (baseImage.getWidth() - width) / 2;
        g2D.drawImage(imageToWrite, x, y, width, height, null);
        g2D.dispose();
    }

    @Test
    public void test() {
        boolean b = hmcGzhApi.hasUserSubscribeHmcGZH(161L);
        System.out.println("================>b:" + b);
    }

    @Test
    public void randomString() {
        String seeds = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code = RandomUtil.randomString(seeds, 8) + RandomUtil.randomInt(3);
        // 得到邀请码之后去数据库查询，while 循环，直到查询结果为数据库不存在（说明得到的code是不重复的）
        System.out.println(code);
    }

    @Test
    public void createActivityShareImage() {
        String url = hmcGzhApi.createActivityShareImage(208L, 1L, null);
        System.out.println("================>url:" + url);
    }

}
