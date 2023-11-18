package com.yiling.activity.web.wx.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.model.ObjectMetadata;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

/**
 * 微信素材工具类
 */
@Slf4j
@Component
public class WxMediaUtil {

    @Autowired
    FileService fileService;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    /**
     * 生产素材图片
     *
     * @param actDocPatientBgImg
     * @param doctorName
     * @param title
     * @return
     */
    public String generateMediaPic(String actDocPatientBgImg, String doctorName, String title) {

        // String bgUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/kapian-bg.png";
        try {
            URL url = new URL(actDocPatientBgImg);
            BufferedImage lotteryActivityBackImage = ImageIO.read(url.openStream());

            BufferedImage baseImage = new BufferedImage(lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

            drawImgInImg(baseImage, lotteryActivityBackImage, 0, 0, lotteryActivityBackImage.getWidth(), lotteryActivityBackImage.getHeight());

            String content = "%s  %s";
            drawTextInImgCenter(baseImage, String.format(content, doctorName, title), 250, Color.white, 32);

            BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(baseImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "JPG", bs);

            byte[] bytes = bs.toByteArray();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(5) + Constants.FILE_SUFFIX, FileTypeEnum.LOTTERY_ACTIVITY_FILE, metadata);

            log.info("[generateMediaPic]fileService.upload返回参数:{}", JSONUtil.toJsonStr(fileInfo));

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            WxMediaUploadResult uploadResult = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "png", inputStream);
            log.info("[generateMediaPic]wxMpService.mediaUpload返回参数:{}", JSONUtil.toJsonStr(uploadResult));

            return uploadResult.getMediaId();
        } catch (Exception e) {
            log.error("generateMediaPic error ,{}", e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 生产素材图片
     *
     * @param actDocPatientBgImg
     * @param doctorName
     * @param title
     * @param doctorPic
     * @return
     */
    public String generateMediaPic(String actDocPatientBgImg, String doctorName, String title,  String doctorPic) {

        try {
            URL url = new URL(actDocPatientBgImg);

            URL doctorPicUrl = new URL(doctorPic);

            BufferedImage backImage = ImageIO.read(url.openStream());

            BufferedImage baseImage = new BufferedImage(backImage.getWidth(), backImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

            drawHeadPicInImg(baseImage, ImageIO.read(doctorPicUrl.openStream()));

            drawImgInImg(baseImage, backImage, 0, 0, backImage.getWidth(), backImage.getHeight());

            String content = "%s  %s";
            drawTextInImgCenter(baseImage, String.format(content, doctorName, title), 250, Color.white, 32);

            BufferedImage result = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            result.getGraphics().drawImage(baseImage, 0, 0, null);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(result, "JPG", bs);

            byte[] bytes = bs.toByteArray();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            FileInfo fileInfo = fileService.upload(bytes, RandomUtil.randomString(5) + Constants.FILE_SUFFIX, FileTypeEnum.LOTTERY_ACTIVITY_FILE, metadata);

            log.info("[generateMediaPic]fileService.upload返回参数:{}", JSONUtil.toJsonStr(fileInfo));

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            WxMediaUploadResult uploadResult = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, "png", inputStream);
            log.info("[generateMediaPic]wxMpService.mediaUpload返回参数:{}", JSONUtil.toJsonStr(uploadResult));

            return uploadResult.getMediaId();
        } catch (Exception e) {
            log.error("generateMediaPic error ,{}", e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
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


}
