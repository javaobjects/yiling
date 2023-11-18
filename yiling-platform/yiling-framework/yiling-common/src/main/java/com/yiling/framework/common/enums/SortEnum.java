package com.yiling.framework.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: <br>
 * @date: 2020/7/8 <br>
 * @author: fei.wu <br>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum SortEnum {
    DESC("desc"),
    ASC("asc"),
    ;

    @JsonValue
    private String value;


}
