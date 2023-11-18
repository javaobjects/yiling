package com.yiling.activity.web.wx.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公众号用户信息
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@NoArgsConstructor
@Data
public class GzhUserInfo {

    @JsonProperty("subscribe")
    private Integer subscribe;

    @JsonProperty("openid")
    private String openid;

    @JsonProperty("language")
    private String language;

    @JsonProperty("subscribe_time")
    private Integer subscribeTime;

    @JsonProperty("unionid")
    private String unionid;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("groupid")
    private Integer groupid;

    @JsonProperty("tagid_list")
    private List<Integer> tagidList;

    @JsonProperty("subscribe_scene")
    private String subscribeScene;

    @JsonProperty("qr_scene")
    private Integer qrScene;

    @JsonProperty("qr_scene_str")
    private String qrSceneStr;
}
