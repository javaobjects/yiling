package com.yiling.mall.agreement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态：1-待导入 2-导入成功 3-导入失败
 * @author: dexi.yao
 * @date: 2021/6/17
 */
@AllArgsConstructor
@Getter
public enum AgreeImportStatusEnum {
    //待导入
    UN_EXECUTION(1, "待导入"),
    //导入失败
    SUCCESS(2, "导入失败"),
    //导入失败
    FAIL(3,"导入失败");

    private Integer code;
    private String  name;

    public static String getNameByCode(Integer code) {
        for (AgreeImportStatusEnum em : values()) {
            if (em.getCode().equals(code)) {
                return em.getName();
            }
        }
        return null;
    }


    public static AgreeImportStatusEnum getByCode(Integer code) {
        for (AgreeImportStatusEnum e : AgreeImportStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
