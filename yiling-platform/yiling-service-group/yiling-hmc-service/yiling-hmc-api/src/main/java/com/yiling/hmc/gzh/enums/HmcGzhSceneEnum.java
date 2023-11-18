package com.yiling.hmc.gzh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HMC公众号场景类型枚举类
 *
 * @Author fan.shen
 * @Date 2022-09-15
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcGzhSceneEnum {

    /**
     * 公众号自然关注订阅
     */
    SUBSCRIBE(1, "公众号自然关注订阅"),

    /**
     * 扫描药盒落地页二维码
     * <p>
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
     * <xml>
     * <ToUserName><![CDATA[gh_79d1a2b73df7]]></ToUserName>
     * <FromUserName><![CDATA[ooceY6LaKRVAASYN3tP1OPOC7nRc]]></FromUserName>
     * <CreateTime>1680166357</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[SCAN]]></Event>
     * <EventKey><![CDATA[so:3]]></EventKey>
     * <Ticket><![CDATA[gQFX8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyblZ1NlZOeDdlb0UxMDAwMGcwN2QAAgTlEntiAwQAAAAA]]></Ticket>
     * </xml>
     */
    BOX_LAND_PAGE_QR(2, "扫描药盒落地页二维码"),

    /**
     * 扫描药盒落地页弹窗二维码(市场健康包的码)
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
     * <xml>
     * <ToUserName><![CDATA[gh_79d1a2b73df7]]></ToUserName>
     * <FromUserName><![CDATA[ooceY6LaKRVAASYN3tP1OPOC7nRc]]></FromUserName>
     * <CreateTime>1680166396</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[SCAN]]></Event>
     * <EventKey><![CDATA[qt:110]]></EventKey>
     * <Ticket><![CDATA[gQFN8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyVTMtblZveDdlb0UxMDAwME0wNzgAAgR40aNjAwQAAAAA]]></Ticket>
     * </xml>
     */
    BOX_LAND_PAGE_QR_POP_UP(3, "扫描药盒落地页弹窗二维码(市场健康包的码)"),

    /**
     * 市场健康包
     */
    CHANNEL_QR_CODE(4, "市场健康包"),

    ;

    private Integer type;

    private String name;


}
