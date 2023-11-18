package com.yiling.sjms.wash.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum SjmsUnlockWashErrorEnum implements IErrorCode {

    UNLOCK_WASH_HAS_DISTRIBUTION_ERROR(1, "非锁流向已经操作"),
    ;

    private final Integer code;
    private final String message;

    public static SjmsUnlockWashErrorEnum getFromCode(Integer code) {
        for (SjmsUnlockWashErrorEnum e : SjmsUnlockWashErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
