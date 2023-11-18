package com.yiling.basic.sms.service.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.sms.config.SmsConfig;
import com.yiling.basic.sms.entity.SmsResultDO;
import com.yiling.basic.sms.service.TxSmsChannelService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.pojo.ServiceResult;
import com.yiling.framework.common.util.JsonUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: fei.wu <br>
 * @date: 2021/7/12 <br>
 */
@Slf4j
@Service
public class TxSmsChannelServiceImpl implements TxSmsChannelService {

    @Autowired
    SmsConfig smsConfig;

    static final String SEND_UTF8 = "/Api/Sendutf8.aspx";

    Map<Integer, String> errorMap = new HashMap() {
        {
            put(1, "提交参数不能为空");
            put(2, "用户名或密码错误");
            put(3, "账号未启用");
            put(4, "计费账号无效");
            put(5, "定时时间无效");
            put(6, "业务未开通");
            put(7, "权限不足");
            put(8, "余额不足");
            put(9, "号码中含有无效号码");
            put(10, "内容中含有非法关键字");
            put(11, "随机加密域无效");
            put(12, "系统错误");
            put(13, "没有加签名，或者签名格式不正确");
            put(14, "审核失败提交号码数量过少");
            put(15, "不在发送时段");
            put(16, "一次提交号码过多,单次提交号码个数不能多于600");
            put(17, "短信内容超长,请联系客服解决");
            put(18, "提交计费错误,请联系客服解决");
            put(19, "暂无通道或者通道设置错误,请联系客服解决");
            put(20, "时间戳格式不正确或时间戳时间不正确");
            put(21, "签名不正确,请在短信的末尾加【会员】");
            put(22, "同一内容发送号码数过多");
            put(23, "短信模板未报备");
            put(24, "IP地址绑定错误");
        }
    };

    private String md5Password(String timestamp) {
        return DigestUtils.md5Hex(smsConfig.getSmsPassword() + timestamp);
    }

    @Override
    public ServiceResult sendSms(String mobile, String content, String signature) {
        String url = smsConfig.getSmsInterfaceUrl() + SEND_UTF8;
        String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        OkHttpClient client = new OkHttpClient();
        try {
            RequestBody requestbody = new FormBody.Builder()
                    .add("username", smsConfig.getSmsUsername())
                    .add("password", this.md5Password(timestamp))
                    .add("mobiles", mobile)
                    .addEncoded("content", URLEncoder.encode(content, "UTF-8") + "【" + signature + "】")
                    .add("timestamp", timestamp)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestbody)
                    .build();

            Call call = client.newCall(request);

            Response re = call.execute();
            String respBody = JsonUtil.url2json(URLDecoder.decode(re.body().string(), "UTF-8"));
            SmsResultDO smsResultDO = JSON.parseObject(respBody, SmsResultDO.class);
            Integer result = smsResultDO.getResult();
            if (result != 0) {
                String error = StrUtil.format("[{}]{}", result, errorMap.getOrDefault(result, "未知错误"));
                log.error("短信发送失败：mobile={}, content={}, error={}", mobile, content, error);
                return ServiceResult.failed(error);
            }

            log.info("短信发送成功：mobile={}, content={}", mobile, content);
            return ServiceResult.success();
        } catch (Exception e) {
            log.error("短信发送失败：mobile={}, content={}", mobile, content, e);
            throw new ServiceException(ResultCode.FAILED, e.getMessage());
        }
    }
}
