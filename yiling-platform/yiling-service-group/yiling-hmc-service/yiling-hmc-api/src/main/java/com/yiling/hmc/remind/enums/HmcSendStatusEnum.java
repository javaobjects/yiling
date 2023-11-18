package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 消息发送状态：1-待发送，2-发送完成，3-发送失败，4-取消发送
 * @Author fan.shen
 * @Date 2022/5/31
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcSendStatusEnum {

    /**
     * 待发送
     */
    WAIT(1,"待发送"),

    /**
     * 发送完成
     */
    FINISHED(2,"发送完成"),

    /**
     * 发送失败
     */
    FAILED(3,"发送失败"),

    /**
     * 取消发送
     */
    CANCELED(4,"取消发送"),

    ;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 名称
     */
    private String  name;


    public static HmcSendStatusEnum getByCode(Integer code) {
        for (HmcSendStatusEnum e : HmcSendStatusEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
