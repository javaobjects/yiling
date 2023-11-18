package com.yiling.user.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户相关附件类型枚举
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Getter
@AllArgsConstructor
public enum UserAttachmentTypeEnum {

    ID_CARD_FRONT_PHOTO(1, "身份证正面照"),
    ID_CARD_BACK_PHOTO(2, "身份证反面照"),
    ;

    private Integer code;
    private String name;

    public static UserAttachmentTypeEnum getByCode(Integer code) {
        for (UserAttachmentTypeEnum e : UserAttachmentTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
