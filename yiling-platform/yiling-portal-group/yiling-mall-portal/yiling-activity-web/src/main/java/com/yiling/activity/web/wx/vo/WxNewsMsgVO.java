package com.yiling.activity.web.wx.vo;


import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.yiling.framework.common.pojo.vo.WxConstant;
import lombok.Data;

import java.util.*;

/**
 * 回复图文消息
 *
 * @Author fan.shen
 * @Date 2022/3/25
 */
@Data
public class WxNewsMsgVO {
    private static final long serialVersionUID = 1L;

    /**
     * 发送的用户
     */
    protected String touser;

    /**
     * 消息类型  MSG_TYPE_*
     */
    protected String msgtype;


    /**
     * 图文消息
     */
    private Map<String, List<Articles>> news;

    /**
     * 初始化
     *
     * @param toUser   接收者，openID
     * @param articles Articles节点，最多三个节点
     * @return WxNewsMsgBean
     */
    public static String getInstance(String toUser, Articles articles) {
        WxNewsMsgVO msgBean = new WxNewsMsgVO();
        msgBean.setTouser(toUser);
        msgBean.setMsgtype(WxConstant.MSG_TYPE_NEWS);
        // 图文信息
        if (Objects.nonNull(articles)) {
            Map<String, List<Articles>> map = new HashMap<>();
            map.put(WxConstant.PARAM_ARTICLES, new ArrayList<>(Arrays.asList(articles)));
            msgBean.setNews(map);
        }
        return JSONUtil.toJsonStr(msgBean);
    }
}
