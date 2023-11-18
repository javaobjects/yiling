package com.yiling.open.erp.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发货类型
 * @author shuan
 */
@Getter
@AllArgsConstructor
public enum SendTypeEnum {
    NORMAL(1,"正常发货"),
    CLOSE(2,"订单关闭"),
    DELETE(3,"删除发货"),
	;

	private Integer code;
	private String message;

    public static SendTypeEnum getByCode(Integer code) {
        for (SendTypeEnum e : SendTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
