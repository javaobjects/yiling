package com.yiling.activity.web.wx.vo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.activity.web.config.WxConstant;

import cn.hutool.json.JSONUtil;
import lombok.Data;

/**
 * 客服发送 文本信息
 * @Author fan.shen
 * @Date 2022/3/25
 *
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html#7
 * eg：
 * {
 *     "touser":"OPENID",
 *     "msgtype":"text",
 *     "text":
 *     {
 *          "content":"Hello World"
 *     }
 * }
 */
@Data
public class WxKfTextMsgVO {

    private static final long serialVersionUID = 1L;

    /**
     * 发送的用户
     */
    @JsonProperty("touser")
    protected String touser;

    /**
     * 消息类型  MSG_TYPE_*
     */
    @JsonProperty("msgtype")
    protected String msgtype;

    /**
     * 消息内容<br>
     * 支持插入跳小程序的文字链：<br>
     * {@literal <a href="http://www.qq.com" data-miniprogram-appid="appid" data-miniprogram-path="pages/index/index">点击跳小程序</a>}
     * <ol>
     *     <li>data-miniprogram-appid 项，填写小程序appid，则表示该链接跳小程序；</li>
     *     <li>data-miniprogram-path项，填写小程序路径，路径与app.json中保持一致，可带参数；</li>
     *     <li>对于不支持data-miniprogram-appid 项的客户端版本，如果有herf项，则仍然保持跳 href 中的网页链接；</li>
     *     <li>data-miniprogram-appid对应的小程序必须与公众号有绑定关系。</li>
     * </ol>
     */
    private Map<String, String> text;

    /**
     * 初始化，并返回
     *
     * @param toUser  发送对象
     * @param content 消息内容
     * @return {@link WxKfTextMsgVO}
     */
    public static String getInstance(String toUser, String content) {
        WxKfTextMsgVO msgBean = new WxKfTextMsgVO();
        msgBean.setTouser(toUser);
        msgBean.setMsgtype(WxConstant.MSG_TYPE_TEXT);
        Map<String, String> map = new HashMap<>();
        map.put(WxConstant.PARAM_CONTENT, content);
        msgBean.setText(map);
        return JSONUtil.toJsonStr(msgBean);
    }
}
