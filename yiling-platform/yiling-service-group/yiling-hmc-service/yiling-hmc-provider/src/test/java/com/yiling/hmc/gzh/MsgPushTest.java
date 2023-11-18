package com.yiling.hmc.gzh;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.yiling.framework.common.pojo.vo.WxAccessToken;
import com.yiling.framework.common.pojo.vo.WxConstant;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 公众号消息推送test
 */
@Slf4j
public class MsgPushTest {


    String token = "64_qQ5UdtvHdRBN7y_Nd5r3-46_4XDjkPkZ33v72Cm4aHNvc1rOtQ7x3fddzJ7WFU_M-mvSDHSty5MSgTQovLnVCIVwcVV4mvNmxVlvmPzK1KCr2bh4i-_2GTeIhKoERQaAEALYX";

    String toUrl = "https://mp.weixin.qq.com/s?__biz=MzkwMjMyNjY4OQ==&mid=2247486639&idx=1&sn=9a4c946cd165c729ac696d33110d5f10&chksm=c0a67c61f7d1f5771ab67a3fb53595ebd685717370031a8b9391b710b3aa75425d21a78cab31#rd";

    /**
     * 推送模板消息
     */
    @Test
    public void testPush() {
        // String templateId = "OvzVs5SrYwQSD67HYTmxUxlwBb7YNLG0u7LMcoqdas0";
        String templateId = "o00y0U3JxAlF0JZaiambPT3xn4I5XAwsC9SbuHn-OP4";
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
        // String toUser = "ooceY6LaKRVAASYN3tP1OPOC7nRc";
        String toUser = "oD2xR6HoWyESQxMLr12M7-BEmcIw";
        WxMsgVO msgVO = WxMsgVO.builder().touser(toUser).template_id(templateId).build();
        Map<String, WxMssData> data = new HashMap<>();
        data.put("first", WxMssData.builder().value("您有1个「健康盲盒」待领取:").build());
        data.put("keyword1", WxMssData.builder().value("以岭健康管理中心").build());
        data.put("keyword2", WxMssData.builder().value("连花福利项目").build());
        data.put("keyword3", WxMssData.builder().value("福利待领取").build());
        data.put("keyword4", WxMssData.builder().value("参与福利").build());
        data.put("remark", WxMssData.builder().value("点击查看福利详情").build());
        msgVO.setData(data);
        msgVO.setUrl(toUrl);
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(msgVO));
        System.out.println(result);
    }

    @Test
    public void testCustom() {
        String URL_CUSTOM_SEND_POST = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
        String result = HttpUtil.post(URL_CUSTOM_SEND_POST, "{\"news\":{\"articles\":[{\"description\":\"点击图片领取药品\",\"title\":\"感谢您关注\\\"以岭健康管理中心\\\"\",\"url\":\"https://h-dev.59yi.com/#/cmp/code?id=wechat:8b171628-a7ff-4517-aba8-25be04b8ca05\",\"picurl\":\"https://s3.bmp.ovh/imgs/2022/03/3a9fca9ff4c2d932.png\"}]},\"touser\":\"oD2xR6HoWyESQxMLr12M7-BEmcIw\",\"msgtype\":\"news\"}");
        System.out.println(result);

    }


}

@Data
@Builder
class WxMsgVO {

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;

    /**
     * 模板数据
     */
    private Map<String, WxMssData> data;

    /**
     * 小程序
     */
    private MiniProgram miniprogram;
}

@Data
@Builder
class WxMssData {

    private String value;

    private String color;
}

@Data
@Builder
class MiniProgram {

    private String appid;

    private String pagepath;
}
