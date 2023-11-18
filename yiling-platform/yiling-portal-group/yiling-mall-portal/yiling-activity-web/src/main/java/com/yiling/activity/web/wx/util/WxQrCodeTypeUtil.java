package com.yiling.activity.web.wx.util;

import cn.hutool.core.util.StrUtil;
import com.yiling.activity.web.wx.enums.WxQrCodeTypeEnum;
import com.yiling.framework.common.pojo.vo.WxConstant;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.hmc.welfare.enums.WxQrTypeEnum;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

import java.util.Map;

/**
 * fan.shen
 * 微信二维码类型工具类
 * 🔍🧧🔥❗️
 */
public class WxQrCodeTypeUtil {

    /**
     * 获取微信二维码类型
     *
     * @param msg
     * @return
     * @see WxQrCodeTypeEnum
     */
    public static WxQrCodeTypeEnum getWxQrCodeType(WxMpXmlMessage msg) {
        if (StrUtil.isBlank(msg.getEventKey())) {
            return WxQrCodeTypeEnum.MP_SUBSCRIBE;
        }
        String qrValue = null;
        switch (msg.getEvent()) {
            // 事件类型：用户未关注时，进行关注后的事件推送
            case WxConstant.EVENT_TYPE_SUBSCRIBE:
                qrValue = msg.getEventKey().substring(7);
                break;
            // 事件类型：扫描带参数二维码事件:用户已关注时的事件推送
            case WxConstant.EVENT_TYPE_SCAN:
                qrValue = msg.getEventKey();
                break;
            default:
                break;
        }
        if (StrUtil.isBlank(qrValue)) {
            return WxQrCodeTypeEnum.MP_SUBSCRIBE;
        }

        Map<String, String> valueMap = YlStrUtils.dealParam(qrValue);

        // 药盒二维码
        if (qrValue.contains("so") && (WxQrCodeTypeEnum.BOX_QR_CODE.getType().equals(Integer.valueOf(valueMap.get("so"))))) {
            return WxQrCodeTypeEnum.BOX_QR_CODE;
        }

        // 药品福利员工二维码（"qt:10_so:2_eId:111_uId:222）
        if (qrValue.contains("so") && (valueMap.containsKey("qt") && WxQrTypeEnum.STAFF_WELFARE.getCode().equals(Integer.valueOf(valueMap.get("qt"))))) {
            return WxQrCodeTypeEnum.STAFF_WELFARE;
        }

        // C端抽奖活动二维码
        if (valueMap.containsKey("qt") && valueMap.get("qt").equals(WxQrTypeEnum.C_LOTTERY_ACTIVITY.getCode() + "")) {
            return WxQrCodeTypeEnum.C_LOTTERY_ACTIVITY;
        }

        // 通心络二维码
        if (valueMap.containsKey("qt") && valueMap.get("qt").equals(WxQrTypeEnum.TXL_WELFARE.getCode() + "")) {
            return WxQrCodeTypeEnum.TXL_WELFARE;
        }

        // 市场健康包 || 市场渠道 qt:101 - qt:109
        if (valueMap.containsKey("qt") && valueMap.containsKey("qt") && WxQrCodeTypeEnum.isChannelMarket(Integer.valueOf(valueMap.get("qt")))) {
            return WxQrCodeTypeEnum.CHANNEL_MARKET_PACKAGE;
        }

        // 市场健康包 || 市场渠道(药盒落地页) qt:110
        if (valueMap.containsKey("qt") && WxQrCodeTypeEnum.isChannelMarketBoxLandPage(Integer.valueOf(valueMap.get("qt")))) {
            return WxQrCodeTypeEnum.CHANNEL_MARKET_10;
        }

        // 健康测评二维码
        if (valueMap.containsKey("qt") && valueMap.get("qt").equals(WxQrTypeEnum.C_HEALTH_EVALUATE.getCode() + "")) {
            return WxQrCodeTypeEnum.C_HEALTH_EVALUATE;
        }

        // 医带患二维码
        if (valueMap.containsKey("qt") && valueMap.get("qt").equals(WxQrTypeEnum.C_ACT_DOC_PATIENT.getCode() + "")) {
            return WxQrCodeTypeEnum.C_ACT_DOC_PATIENT;
        }
        return WxQrCodeTypeEnum.STAFF_WELFARE;
    }

}
