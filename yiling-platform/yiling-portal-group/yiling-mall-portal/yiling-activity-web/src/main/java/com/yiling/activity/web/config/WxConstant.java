package com.yiling.activity.web.config;

/**
 * 微信公众号开发相关常量
 *
 * @Author fan.shen
 * @Date 2022/3/24
 */
public interface WxConstant {
    /**
     * 返回消息：success
     */
    String MSG_SUCCESS = "success";

    /**
     * 消息类型：文本信息
     */
    String MSG_TYPE_TEXT = "text";

    /**
     * 消息类型：图片信息
     */
    String MSG_TYPE_IMAGE = "image";

    /**
     * 消息类型：图文信息
     */
    String MSG_TYPE_NEWS = "news";

    /**
     * 消息类型：语音消息
     */
    String MSG_TYPE_VOICE = "voice";

    /**
     * 消息类型：音乐消息
     */
    String MSG_TYPE_MUSIC = "music";

    /**
     * 消息类型：视频消息
     */
    String MSG_TYPE_VIDEO = "video";

    /**
     * 消息类型：小视频消息
     */
    String MSG_TYPE_SHORT_VIDEO = "shortvideo";

    /**
     * 消息类型：菜单消息，客户菜单类型
     */
    String MSG_TYPE_MSG_MENU = "msgmenu";

    /**
     * 消息类型：地理位置信息
     */
    String MSG_TYPE_LOCATION = "location";

    /**
     * 消息类型：链接信息
     */
    String MSG_TYPE_LINK = "link";

    /**
     * 消息类型：事件
     */
    String MSG_TYPE_EVENT = "event";

    /**
     * 消息类型：转发到客户系统
     */
    String MSG_TYPE_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";

    /**
     * 事件类型：自定义菜单事件(点击菜单拉取消息时的事件推送)
     */
    String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 事件类型：点击菜单跳转链接时的事件推送
     */
    String EVENT_TYPE_VIEW = "VIEW";

    /**
     * 事件类型：上报地理位置事件
     */
    String EVENT_TYPE_LOCATION = "LOCATION";

    /**
     * 事件类型：用户未关注时，进行关注后的事件推送
     */
    String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：用户取消关注
     */
    String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：扫描带参数二维码事件:用户已关注时的事件推送
     */
    String EVENT_TYPE_SCAN = "SCAN";

    /**
     * 事件类型：模版消息发送返回的事件推送
     */
    String EVENT_TYPE_TEMPLATE_SEND_JOB_FINISH = "TEMPLATESENDJOBFINISH";

    /**
     * 菜单的响应动作类型，view表示网页类型
     */
    String MENU_TYPE_VIEW = "view";

    /**
     * 菜单的响应动作类型，click表示点击类型
     */
    String MENU_TYPE_CLICK = "click";

    /**
     * 菜单的响应动作类型，miniprogram表示小程序类型
     */
    String MENU_TYPE_MINI_PROGRAM = "miniprogram";

    /**
     * 参数 access_token
     */
    String PARAM_ACCESS_TOKEN = "ACCESS_TOKEN";

    /**
     * 参数 content
     */
    String PARAM_CONTENT = "content";

    /**
     * 前缀
     */
    String QR_SCENE = "qrscene_";

    /**
     * 参数 media_id
     */
    String PARAM_MEDIA_ID = "media_id";

    /**
     * 参数 thumb_media_id，缩略图的媒体id
     */
    String PARAM_THUMB_MEDIA_ID = "thumb_media_id";

    /**
     * 参数 title
     */
    String PARAM_TITLE = "title";

    /**
     * 参数 description
     */
    String PARAM_DESCRIPTION = "description";

    /**
     * 参数 musicurl
     */
    String PARAM_MUSIC_URL = "musicurl";

    /**
     * 参数 hqmusicurl
     */
    String PARAM_HQ_MUSIC_URL = "hqmusicurl";

    /**
     * 参数 url
     */
    String PARAM_URL = "url";

    /**
     * 参数 hqmusicurl
     */
    String PARAM_PIC_URL = "picurl";

    /**
     * 参数 articles
     */
    String PARAM_ARTICLES = "articles";

    /**
     * 扫描带参数二维码事件：用户未关注时，进行关注后的事件推送：事件KEY值，qrscene_为前缀
     */
    String PARAM_QR_CODE_EVENT_KEY_PRE = "qrscene_";

    /**
     * 获取 access_token URL，get 请求。%s 为要填入的参数
     */
    String URL_ACCESS_TOKEN_GET = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    /**
     * 获取 userInfo URL，get 请求。%s 为要填入的参数
     */
    String URL_USER_INFO_GET = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * 客服发送信息接口， post 请求。%s 为要填入的参数<br>
     */
    String URL_CUSTOM_SEND_POST = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";


}
