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
public enum GbDataExecStatusEnum {

    UN_START(1, "未开始"),
    AUTO(2, "自动处理中"),
    FINISH(3, "已处理"),
    FAIL(4, "处理失败"),
    ARTIFICIAL(5, "手动处理中"),
    ;

    private final Integer code;
    private final String name;

    public static GbDataExecStatusEnum getByCode(Integer code) {
        for (GbDataExecStatusEnum e : GbDataExecStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
