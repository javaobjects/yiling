package com.yiling.basic.no.service;

import com.yiling.basic.no.enums.INoEnum;
import com.yiling.basic.no.enums.MiddelPartMode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: xuan.zhou
 * @date: 2023/2/27
 */
@Getter
@AllArgsConstructor
public enum TestEnum implements INoEnum {

    NO_TEST("NO-TEST", MiddelPartMode.DATESTR, 6)
    ;

    /**
     * 单据号前缀
     */
    private String prefix;

    /**
     * 中间部分生成方式
     */
    private MiddelPartMode middelPartMode;

    /**
     * 随机数位数
     */
    private Integer randomNum;
}
