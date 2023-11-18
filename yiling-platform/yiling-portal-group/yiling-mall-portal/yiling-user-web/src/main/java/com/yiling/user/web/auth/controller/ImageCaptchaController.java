package com.yiling.user.web.auth.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.yiling.basic.captcha.api.ImageCaptchaApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: <br>
 * @date: 2020/7/7 <br>
 * @author: fei.wu <br>
 */

@RestController
@Slf4j
@RequestMapping("/captcha")
@Api(tags = "图形验证码接口")
public class ImageCaptchaController {

    @Resource
    DefaultKaptcha defaultKaptcha;

    @DubboReference
    ImageCaptchaApi imageCaptchaApi;

    @ApiOperation(value = "获取图形验证码",notes = "服务端在需要的验证图形验证的地方,调用 imageCaptchaApi.checkCaptcha(captcha, captchaToken)")
    @GetMapping(value = {"/image"})
    public void image(@RequestParam @ApiParam(name = "serialNo", value = "验证码流水号，由调用方生成，全局唯一", required = true) String serialNo, HttpServletResponse response) {
        log.debug("serialNo={}", serialNo);
        try {
            String captcha = defaultKaptcha.createText();
            String cacheCaptcha = imageCaptchaApi.cacheCaptcha(captcha, serialNo);
            if (cacheCaptcha != null) {
                //向客户端写出
                BufferedImage bi = defaultKaptcha.createImage(captcha);
                ServletOutputStream out = response.getOutputStream();
                ImageIO.write(bi, "jpg", out);
                try {
                    out.flush();
                } finally {
                    out.close();
                }
            } else {
                log.error("[ImageCController][validation] 异常！参数: " + serialNo + " : " + cacheCaptcha);
            }
        } catch (IOException e) {
            log.error("[ImageCController][validation] 异常！参数: " + serialNo + "\n" + e.getMessage(), e);
        }
    }


}
