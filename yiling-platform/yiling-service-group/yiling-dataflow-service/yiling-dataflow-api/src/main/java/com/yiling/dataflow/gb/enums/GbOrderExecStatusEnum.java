package com.yiling.dataflow.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购处理状态枚举类
 * 数据字典：gb_appeal_form_data_exec_status
 *
 * @author: houjie.sun
 * @date: 2023/5/16
 */
@Getter
@AllArgsConstructor
public enum GbOrderExecStatusEnum {

    UN_START(1, "未处理"),
    FINISH(2, "已处理"),
    ;

    private final Integer code;
    private final String name;

    public static GbOrderExecStatusEnum getByCode(Integer code) {
        for (GbOrderExecStatusEnum e : GbOrderExecStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
