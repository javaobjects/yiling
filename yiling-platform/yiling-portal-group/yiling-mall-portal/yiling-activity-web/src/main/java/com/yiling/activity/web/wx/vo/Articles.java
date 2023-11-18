package com.yiling.activity.web.wx.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

/**
 * 图文信息
 *
 * @Author fan.shen
 * @Date 2022/4/29
 */
@Data
public class Articles implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图文消息标题
     */
    private String title;

    /**
     * 图文消息描述
     */
    private String description;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    private String picurl;

    /**
     * 点击图文消息跳转链接
     */
    private String url;

    /**
     * 初始化
     *
     * @param title       图文消息标题
     * @param description 图文消息描述
     * @param picUrl      图片链接
     * @param url         点击图文消息跳转链接
     * @return {@link Articles}
     */
    public static Articles getInstance(String title, String description, String picUrl, String url) {
        Articles bean = new Articles();
        bean.setTitle(title);
        bean.setDescription(description);
        bean.setPicurl(picUrl);
        bean.setUrl(url);
        return bean;
    }
}
