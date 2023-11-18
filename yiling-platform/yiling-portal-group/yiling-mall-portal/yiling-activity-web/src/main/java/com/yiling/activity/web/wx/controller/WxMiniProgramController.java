package com.yiling.activity.web.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.yiling.activity.web.config.HmcMiniProgramProperties;
import com.yiling.activity.web.wx.form.WxQrCodeForm;
import com.yiling.activity.web.wx.handler.WxMsgContext;
import com.yiling.activity.web.wx.vo.MiniAppQrCodeVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.WxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;

/**
 * 微信小程序接口
 *
 * @author: fan.shen
 * @date: 2022/4/28
 */
@Slf4j
@RestController
@Api(tags = "微信小程序接口")
public class WxMiniProgramController extends BaseController {

    @Autowired
    private HmcMiniProgramProperties hmcMiniProgramProperties;

    @Autowired
    WxUtils wxUtils;

    @Autowired
    RedisService redisService;

    /**
     * 小程序服务类
     */
    @Autowired
    WxMaService wxMaService;


    @PostMapping(value = "/getQrCode")
    @ApiOperation(value = "获取小程序码")
    public Result<MiniAppQrCodeVO> getQrCode(@RequestBody WxQrCodeForm qrCodeForm) {

        log.info("生成小程序码参数：{}", qrCodeForm);

        if (Objects.isNull(redisService.get(qrCodeForm.getKey()))) {
            return Result.failed("根据key未获取到redis参数，key:" + qrCodeForm.getKey());
        }

        String accessToken;
        try {
            accessToken = wxMaService.getAccessToken();
            log.info("获取accessToken结果：{},参数：{}", accessToken, qrCodeForm);
        } catch (WxErrorException e) {
            log.error("获取小程序码失败：{}", e.getMessage(), e);
            return Result.failed("获取小程序码失败");
        }

        MiniAppQrCodeVO result = getMiniQr(accessToken, qrCodeForm);

        log.info("二维码生成成功,base64:{}", result);
        return Result.success(result);
    }


    /**
     * 生成小程序码返回base64字符串
     *
     * @param accessToken
     * @param qrCodeForm
     * @return
     */
    public MiniAppQrCodeVO getMiniQr(String accessToken, WxQrCodeForm qrCodeForm) {
        MiniAppQrCodeVO qrCodeVO = new MiniAppQrCodeVO();

        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(String.format(WxConstant.GET_WX_A_CODE_UN_LIMIT_URL, accessToken));
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
//            String scene = "qr=" + redisService.get(qrCodeForm.getKey()).toString();
//            String page = "pagesSub/main/drugWelfare/index";
            WxMsgContext msgContext = JSONUtil.toBean(redisService.get(qrCodeForm.getKey()).toString(), WxMsgContext.class);


            JSONObject paramJson = new JSONObject();
            paramJson.put("scene", msgContext.getScene());
            paramJson.put("page", msgContext.getPage());
            paramJson.put("env_version", qrCodeForm.getEnvVersion());
            paramJson.put("check_path", false);

            log.info("生成小程序码参数:{}", paramJson);

            printWriter.write(paramJson.toString());
            printWriter.flush();

            try (InputStream is = httpURLConnection.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

                qrCodeVO.setPage(msgContext.getPage());
                qrCodeVO.setScene(msgContext.getScene());
                qrCodeVO.setKey(qrCodeForm.getKey());
                qrCodeVO.setEnvVersion(qrCodeForm.getEnvVersion());
                qrCodeVO.setBase64(base64);

                return qrCodeVO;
            }
        } catch (Exception e) {
            log.error("调用微信小程序码接口报错:{}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

}
