package com.yiling.activity.web.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信公众号场景二维码分类
 *
 * @Author fan.shen
 * @Date 2022/3/30
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WxQrCodeTypeEnum {


    /**
     * 公众号订阅事件
     */
    MP_SUBSCRIBE(0, "公众号订阅事件"),

    /**
     * 通心络福利二维码
     */
    TXL_WELFARE(1, "通心络福利二维码"),

    /**
     * 扫药盒二维码
     */
    BOX_QR_CODE(3, "扫药盒二维码"),

    /**
     * 药品福利员工二维码
     */
    STAFF_WELFARE(10, "药品福利员工二维码"),

    /**
     * 抽奖活动二维码
     */
    C_LOTTERY_ACTIVITY(20, "抽奖活动二维码"),

    /**
     * 健康测评二维码
     */
    C_HEALTH_EVALUATE(30, "健康测评二维码"),


    /**
     * 医带患二维码
     */
    C_ACT_DOC_PATIENT(40, "医带患二维码"),


    // ------------ 渠道二维码,标识段从 100开始

    /**
     * 市场健康包
     */
    CHANNEL_MARKET_PACKAGE(100, "市场健康包(1-9)"),

    /**
     * 市场渠道
     */
    CHANNEL_MARKET_01(101, "市场渠道01"),
    CHANNEL_MARKET_02(102, "市场渠道02"),
    CHANNEL_MARKET_03(103, "市场渠道03"),
    CHANNEL_MARKET_04(104, "市场渠道04"),
    CHANNEL_MARKET_05(105, "市场渠道05"),
    CHANNEL_MARKET_06(106, "市场渠道06"),
    CHANNEL_MARKET_07(107, "市场渠道07"),
    CHANNEL_MARKET_08(108, "市场渠道08"),
    CHANNEL_MARKET_09(109, "市场渠道09"),
    CHANNEL_MARKET_10(110, "市场渠道10"),

    ;

    private Integer type;

    private String name;


    /**
     * 判断是否市场渠道二维码
     *
     * @param code
     * @return
     */
    public static boolean isChannelMarket(Integer code) {
        List<WxQrCodeTypeEnum> channelCode = new ArrayList<>();
        channelCode.add(CHANNEL_MARKET_PACKAGE);
        channelCode.add(CHANNEL_MARKET_01);
        channelCode.add(CHANNEL_MARKET_02);
        channelCode.add(CHANNEL_MARKET_03);
        channelCode.add(CHANNEL_MARKET_04);
        channelCode.add(CHANNEL_MARKET_05);
        channelCode.add(CHANNEL_MARKET_06);
        channelCode.add(CHANNEL_MARKET_07);
        channelCode.add(CHANNEL_MARKET_08);
        channelCode.add(CHANNEL_MARKET_09);
        // channelCode.add(CHANNEL_MARKET_10);
        return channelCode.stream().anyMatch(item -> item.getType().equals(code));
    }

    /**
     * 判断是否市场渠道二维码-药盒落地页
     *
     * @param code
     * @return
     */
    public static boolean isChannelMarketBoxLandPage(Integer code) {
        List<WxQrCodeTypeEnum> channelCode = new ArrayList<>();
        channelCode.add(CHANNEL_MARKET_10);
        return channelCode.stream().anyMatch(item -> item.getType().equals(code));
    }

    public static WxQrCodeTypeEnum getByType(Integer type) {
        for (WxQrCodeTypeEnum e : WxQrCodeTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "WxQrCodeTypeEnum{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
