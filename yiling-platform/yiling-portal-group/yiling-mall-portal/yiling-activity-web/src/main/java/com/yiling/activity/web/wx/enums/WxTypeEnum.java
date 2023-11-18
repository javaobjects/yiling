package com.yiling.activity.web.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HMC 公众号相关枚举
 *
 * @Author fan.shen
 * @Date 2022/7/22
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WxTypeEnum {

    /**
     * 以岭健康管理中心公众号
     */
    HMC_MP_PROD(1, "wxfbd61abfd05c127e", "以岭健康管理中心公众号", "mp"),

    /**
     * 以岭健康管理中心小程序
     */
    HMC_MA_PROD(2, "wxc439962ebf26b00a", "以岭健康管理中心小程序", "ma"),

    /**
     * 补方测试公众号
     */
    HMC_MP_TEST(3, "wxbdb7fce20be7cf4d", "补方测试公众号", "mp"),

    /**
     * 补方测试小程序
     */
    HMC_MA_TEST(4, "wx4015099f1fca8328", "补方测试小程序", "ma"),

    ;

    private Integer code;

    private String appId;

    private String name;

    /**
     * mp - 公众号
     * ma - 小程序
     */
    private String type;

    public static WxTypeEnum getByType(Integer type) {
        for (WxTypeEnum e : WxTypeEnum.values()) {
            if (e.getCode().equals(type)) {
                return e;
            }
        }
        return null;
    }

    public static WxTypeEnum getByAppId(String appId) {
        for (WxTypeEnum e : WxTypeEnum.values()) {
            if (e.getAppId().equals(appId)) {
                return e;
            }
        }
        return null;
    }
}
