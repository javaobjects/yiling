package com.yiling.activity.web.wx.form;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * 微信公众号消息参数
 * @Description
 * @Author fan.shen
 * @Date 2022/3/24
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxInMessageForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 开发者微信号
     */
    private String ToUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    private String FromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private Long CreateTime;

    /**
     * 消息类型: MSG_TYPE_*
     */
    private String MsgType;

    /**
     * 消息id，64位整型
     */
    private Long MsgId;

    /**
     * 文本消息内容
     */
    private String Content;

    /**
     * 图片链接（由系统生成）
     */
    private String PicUrl;

    /**
     * 图片消息媒体id，可以调用获取临时素材接口拉取数据。
     * 语音消息媒体id，可以调用获取临时素材接口拉取数据。
     * 视频消息媒体id，可以调用获取临时素材接口拉取数据。
     */
    private String MediaId;

    /**
     * 语音格式，如amr，speex等
     */
    private String Format;

    /**
     * 语音识别结果，UTF8编码<br>
     * 需开通语音识别
     */
    private String Recognition;

    /**
     * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    private String ThumbMediaId;

    /**
     * 地理位置消息：地理位置纬度
     */
    private String Location_X;

    /**
     * 地理位置消息：地理位置经度
     */
    private String Location_Y;

    /**
     * 地理位置消息：地图缩放大小
     */
    private String Scale;

    /**
     * 地理位置消息：地理位置信息
     */
    private String Label;

    /**
     * 链接消息：消息标题
     */
    private String Title;

    /**
     * 链接消息：消息描述
     */
    private String Description;

    /**
     * 链接消息：消息链接
     */
    private String Url;

    /**
     * 事件类型 EVENT_TYPE_*
     */
    private String Event;

    /**
     * 扫描带参数二维码事件：用户未关注时，进行关注后的事件推送：事件KEY值，qrscene_为前缀，后面为二维码的参数值
     * 扫描带参数二维码事件：用户已关注时的事件推送：事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
     * 自定义菜单事件：点击菜单拉取消息时的事件推送：事件KEY值，与自定义菜单接口中KEY值对应
     * 自定义菜单事件：点击菜单跳转链接时的事件推送：事件KEY值，设置的跳转URL
     */
    private String EventKey;

    /**
     * 扫描带参数二维码事件：二维码的ticket，可用来换取二维码图片
     */
    private String Ticket;

    /**
     * 上报地理位置事件：地理位置纬度
     */
    private String Latitude;

    /**
     * 上报地理位置事件：地理位置经度
     */
    private String Longitude;

    /**
     * 上报地理位置事件：地理位置精度
     */
    private String Precision;

    /**
     * aes 加密后的文本信息
     */
    private String Encrypt;

    /**
     * 解密后携带的访问路径
     */
    private String URL;

    /**
     * 微信公众号发送模板消息给用户的发送状态
     */
    private String Status;
}
